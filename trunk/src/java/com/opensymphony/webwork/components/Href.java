package com.opensymphony.webwork.components;

import com.opensymphony.util.TextUtils;
import com.opensymphony.webwork.config.Configuration;
import com.opensymphony.webwork.portlet.context.PortletContext;
import com.opensymphony.xwork.util.OgnlValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A tag that creates a HTML &gt;a href='' /&lt; that when clicked calls a URL remote XMLHttpRequest call
 * via the dojo framework.  The result from the URL is executed as JavaScript.
 * <p/>
 * If a "listenTopics" is supplied, it will publish a 'click' message to that topic when the result is
 * returned.  If utilizing the topic/event elements, then this tag needs to be contained within
 * a &gt;ww:topicScope /&lt; tag.
 *
 * @author Ian Roughley
 */
public class Href extends ClosingUIBean {
    final public static String OPEN_TEMPLATE = "a";
    final public static String TEMPLATE = "a-close";
    final public static String COMPONENT_NAME = Href.class.getName();

    protected String href;
    protected String errorText;
    protected String showErrorTransportText;
    protected String notifyTopics;
    protected String afterLoading;
    protected String preInvokeJS;

    public Href(OgnlValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    public String getDefaultOpenTemplate() {
        return OPEN_TEMPLATE;
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();

        //Fix for wwportlet Support -- Added by Henry Hu
        if (href != null) {
            String hrefValue = findString(href);
            //if findString could not evalu the string default to the href
            //basically this is a default thats better than null pointing in this code
            if (hrefValue == null) {
                href = removeStringFromStart(href,"'");
                hrefValue = removeStringFromEnd(href,"'");
            }
            String contextPath = request.getContextPath();
            StringBuffer sb = new StringBuffer();
            String actionUrl = PortletContext.getContext().getActionURL();

            if (!TextUtils.stringSet(actionUrl)) {
                if (hrefValue.startsWith("/") && hrefValue.startsWith(contextPath)) {
                    contextPath = "";
                }
                sb.append(contextPath).append(hrefValue);
            } else {
                String actionExtension = (String) Configuration.get("webwork.action.extension");

                if (actionExtension == null || "".equals(actionExtension)) {
                    actionExtension = ".action";
                } else {
                    actionExtension = "." + actionExtension;
                }

                boolean isWebWorkAction = hrefValue.indexOf(actionExtension) >= 0;

                if (isWebWorkAction) {
                    sb.append(actionUrl).append("?wwXAction=.").append(hrefValue);
                } else {
                    sb.append(actionUrl).append("?wwLink=").append(hrefValue);
                }
            }

            addParameter("href", sb.toString());
        }

        if (showErrorTransportText != null) {
            addParameter("showErrorTransportText", findValue(showErrorTransportText, Boolean.class));
        }

        if (errorText != null) {
            addParameter("errorText", findString(errorText));
        }

        if (notifyTopics != null) {
            addParameter("notifyTopics", findString(notifyTopics));
        }

        if (afterLoading != null) {
            addParameter("afterLoading", findString(afterLoading));
        }
        if (preInvokeJS != null) {
            addParameter("preInvokeJS", findString(preInvokeJS));
        }
    }

    /**
     * If the source string starts with the proided stringToRemove removes it and returns
     * the string without the leading string.
     * @param source
     * @param stringToRemove
     * @return The string without the leading stringToRemove
     */
    private String removeStringFromStart(String source, String stringToRemove) {
        //gate condition
        if (source == null || stringToRemove == null){
            return source;
        }

        if (source.startsWith(stringToRemove)){
            return source.substring(1, source.length());
        }
        return source;
    }


    /**
     * if the source string ends with the stringToReve returns the source
     * string without the tailing stringToRemove. Otherwise returns the source
     * string
     * @param source
     * @param stringToRemove
     * @return the string without the trailing stringToRemove
     */
    private String removeStringFromEnd(String source, String stringToRemove) {
        //gate condition
        if (source == null || stringToRemove == null){
            return source;
        }
        if (source.endsWith(stringToRemove)){
            return stringToRemove.substring(0, source.length() -1);
        }
        return source;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public void setShowErrorTransportText(String showErrorTransportText) {
        this.showErrorTransportText = showErrorTransportText;
    }

    public void setNotifyTopics(String notifyTopics) {
        this.notifyTopics = notifyTopics;
    }

    public void setAfterLoading(String afterLoading) {
        this.afterLoading = afterLoading;
    }

    public void setPreInvokeJS(String preInvokeJS) {
        this.preInvokeJS = preInvokeJS;
    }
}