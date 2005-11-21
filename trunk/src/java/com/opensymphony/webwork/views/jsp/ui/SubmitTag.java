/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp.ui;

import com.opensymphony.webwork.components.Component;
import com.opensymphony.webwork.components.Submit;
import com.opensymphony.xwork.util.OgnlValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @jsp.tag name="submit" bodycontent="JSP"
 * @see Submit
 */
public class SubmitTag extends AbstractUITag {
    protected String action;
    protected String method;
    protected String align;
    protected String resultDivId;
    protected String onLoadJS;
    protected String notifyTopics;
    protected String listenTopics;
    protected String preInvokeJS;

    public Component getBean(OgnlValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Submit(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();

        Submit submit = ((Submit) component);
        submit.setAction(action);
        submit.setMethod(method);
        submit.setAlign(align);
        submit.setResultDivId(resultDivId);
        submit.setOnLoadJS(onLoadJS);
        submit.setNotifyTopics(notifyTopics);
        submit.setListenTopics(listenTopics);
        submit.setPreInvokeJS(preInvokeJS);
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setAlign(String align) {
        this.align = align;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setResultDivId(String resultDivId) {
        this.resultDivId = resultDivId;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setOnLoadJS(String onLoadJS) {
        this.onLoadJS = onLoadJS;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setNotifyTopics(String notifyTopics) {
        this.notifyTopics = notifyTopics;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setListenTopics(String listenTopics) {
        this.listenTopics = listenTopics;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setPreInvokeJS(String preInvokeJS) {
        this.preInvokeJS = preInvokeJS;
    }
}