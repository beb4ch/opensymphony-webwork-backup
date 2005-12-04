/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.dispatcher;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.WebWorkStatics;
import com.opensymphony.webwork.views.velocity.VelocityManager;
import com.opensymphony.webwork.views.velocity.ui.JSPTagAdapterFactory;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.util.TextParseUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;


/**
 * Pulls the HttpServletResponse object from the action context,
 * and calls sendRedirect() using the location specified as the
 * parameter "location". The following params are required:
 * <ul>
 *  <li>location - the URL to use when calling sendRedirect()</li>
 * </ul>
 *
 * @version $Id$
 * @author Matt Ho <a href="mailto:matt@indigoegg.com">&lt;matt@indigoegg.com&gt;</a>
 */
public class VelocityResult implements Result, WebWorkStatics {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log log = LogFactory.getLog(VelocityResult.class);
    private static VelocityEngine velocityEngine = VelocityManager.getVelocityEngine();
    public static final String DEFAULT_PARAM = "location";

    //~ Instance fields ////////////////////////////////////////////////////////

    private JSPTagAdapterFactory adapterFactory = new JSPTagAdapterFactory();
    private String location;
    private boolean parse;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setLocation(String location) {
        this.location = location;
    }

    public void setParse(boolean parse) {
        this.parse = parse;
    }

    public void execute(ActionInvocation invocation) throws Exception {
        OgnlValueStack stack = ActionContext.getContext().getValueStack();

        if (parse) {
            location = TextParseUtil.translateVariables(location, stack);
        }

        try {
            Template t = velocityEngine.getTemplate(this.location);
            Context context = VelocityManager.createContext(ServletActionContext.getServletConfig(), ServletActionContext.getRequest(), ServletActionContext.getResponse());
            HttpServletResponse response = ServletActionContext.getResponse();

            Writer writer = response.getWriter();

            // @todo can t.getEncoding() ever return a null value?
            if (t.getEncoding() != null) {
                response.setContentType("text/html; charset=" + t.getEncoding());
            } else {
                response.setContentType("text/html");
            }

            t.merge(context, response.getWriter());

            // flush the buffer as resin fails to render in some cases without this
            writer.flush();
        } catch (Exception e) {
            log.error("Unable to render Velocity Template, '" + location + "'", e);
            throw e;
        }

        return;
    }
}