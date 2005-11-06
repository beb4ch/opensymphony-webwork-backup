/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jasperreports;

import com.opensymphony.util.TextUtils;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.WebWorkResultSupport;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.util.OgnlValueStack;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Genreates a JasperReports report using the specified format or PDF if no format is specified.
 * The following parameters are required:
 * <ul>
 * <li>location - the location where the compiled jasper report definition is (foo.jasper), relative from current URL</li>
 * <li>dataSource - the Ognl expression used to retrieve the datasource from the value stack (usually a List)</li>
 * </ul>
 * <p/>
 * Three optional parameter can also be specified:
 * <ul>
 * <li>format: the format in which the report should be generated. Valid values can be found
 * in {@link JasperReportConstants}. If no format is specified, PDF will be used.</li>
 * <li>contentDisposition: disposition (defaults to "inline", values are typically <i>filename="document.pdf"</i>)</li>
 * <li>documentName: name of the document (will generate the http header Content-disposition = X; filename=X.[format])</li>
 * <li>delimiter: the delimiter used when generating CSV reports. By default, the character used is ",".</li>
 * </ul>
 * <p/>
 * This result follows the same rules from {@link WebWorkResultSupport}.
 * Specifically, all three parameters will be parsed if the "parse" parameter is not set to false.
 *
 * @author Patrick Lightbody
 * @author <a href="mailto:hermanns@aixcept.de">Rainer Hermanns</a>
 */
public class JasperReportsResult extends WebWorkResultSupport implements JasperReportConstants {
    private final static Log LOG = LogFactory.getLog(JasperReportsResult.class);

    protected String IMAGES_DIR = "/images/";
    protected String dataSource;
    protected String format;
    protected String documentName;
    protected String contentDisposition;
    protected String delimiter;

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
        if (this.format == null) {
            this.format = FORMAT_PDF;
        }

        if (dataSource == null) {
            String message = "No dataSource specified...";
            LOG.error(message);
            throw new RuntimeException(message);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating JasperReport for dataSource = " + dataSource + ", format = " + this.format);
        }

        HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(ServletActionContext.HTTP_RESPONSE);

        //construct the data source for the report
        OgnlValueStack stack = invocation.getStack();
        OgnlValueStackDataSource stackDataSource = new OgnlValueStackDataSource(stack, dataSource);

        format = conditionalParse(format, invocation);
        dataSource = conditionalParse(dataSource, invocation);

        if (contentDisposition != null) {
            contentDisposition = conditionalParse(contentDisposition, invocation);
        }

        if (documentName != null) {
            documentName = conditionalParse(documentName, invocation);
        }

        // (Map) ActionContext.getContext().getSession().get("IMAGES_MAP");
        if (!TextUtils.stringSet(format)) {
            format = FORMAT_PDF;
        }

        if (!"contype".equals(request.getHeader("User-Agent"))) {
            // Determine the directory that the report file is in and set the reportDirectory parameter
            ServletContext servletContext = (ServletContext) invocation.getInvocationContext().get(ServletActionContext.SERVLET_CONTEXT);
            String systemId = servletContext.getRealPath(finalLocation);
            Map parameters = new OgnlValueStackShadowMap(stack);
            File directory = new File(systemId.substring(0, systemId.lastIndexOf(File.separator)));
            parameters.put("reportDirectory", directory);

            byte[] output;
            JasperPrint jasperPrint;

            // Fill the report and produce a print object
            try {
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(systemId);

                jasperPrint =
                        JasperFillManager.fillReport(jasperReport,
                                parameters,
                                stackDataSource);
            } catch (JRException e) {
                LOG.error("Error building report for uri " + systemId, e);
                throw new ServletException(e.getMessage(), e);
            }

            // Export the print object to the desired output format
            try {
                if (contentDisposition != null || documentName != null) {
                    final StringBuffer tmp = new StringBuffer();
                    tmp.append((contentDisposition == null) ? "inline" : contentDisposition);

                    if (documentName != null) {
                        tmp.append("; filename=");
                        tmp.append(documentName);
                        tmp.append(".");
                        tmp.append(format.toLowerCase());
                    }

                    response.setHeader("Content-disposition", tmp.toString());
                }

                if (format.equals(FORMAT_PDF)) {
                    response.setContentType("application/pdf");

                    // response.setHeader("Content-disposition", "inline; filename=report.pdf");
                    output = JasperExportManager.exportReportToPdf(jasperPrint);
                } else {
                    JRExporter exporter;

                    if (format.equals(FORMAT_CSV)) {
                        response.setContentType("text/plain");
                        exporter = new JRCsvExporter();
                    } else if (format.equals(FORMAT_HTML)) {
                        response.setContentType("text/html");

                        Map imagesMap = new HashMap();

                        request.getSession(true).setAttribute("IMAGES_MAP", imagesMap);
                        exporter = new JRHtmlExporter();
                        exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
                        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + IMAGES_DIR);
                    } else if (format.equals(FORMAT_XLS)) {
                        response.setContentType("application/vnd.ms-excel");
                        exporter = new JRXlsExporter();
                    } else if (format.equals(FORMAT_XML)) {
                        response.setContentType("text/xml");
                        exporter = new JRXmlExporter();
                    } else {
                        throw new ServletException("Unknown report format: " + format);
                    }

                    output = exportReportToBytes(jasperPrint, exporter);
                }
            } catch (JRException e) {
                String message = "Error producing " + format + " report for uri " + systemId;
                LOG.error(message, e);
                throw new ServletException(e.getMessage(), e);
            }

            response.setContentLength(output.length);

            ServletOutputStream ouputStream;

            try {
                ouputStream = response.getOutputStream();
                ouputStream.write(output);
                ouputStream.flush();
                ouputStream.close();
            } catch (IOException e) {
                LOG.error("Error writing report output", e);
                throw new ServletException(e.getMessage(), e);
            }
        } else {
            // Code to handle "contype" request from IE
            try {
                ServletOutputStream outputStream;
                response.setContentType("application/pdf");
                response.setContentLength(0);
                outputStream = response.getOutputStream();
                outputStream.close();
            } catch (IOException e) {
                LOG.error("Error writing report output", e);
                throw new ServletException(e.getMessage(), e);
            }
        }
    }

    /**
     * Run a Jasper report to CSV format and put the results in a byte array
     *
     * @param jasperPrint The Print object to render as CSV
     * @param exporter    The exporter to use to export the report
     * @return A CSV formatted report
     * @throws net.sf.jasperreports.engine.JRException
     *          If there is a problem running the report
     */
    private byte[] exportReportToBytes(JasperPrint jasperPrint, JRExporter exporter) throws JRException {
        byte[] output;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        if (delimiter != null) {
            exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, delimiter);
        }

        exporter.exportReport();

        output = baos.toByteArray();

        return output;
    }
}