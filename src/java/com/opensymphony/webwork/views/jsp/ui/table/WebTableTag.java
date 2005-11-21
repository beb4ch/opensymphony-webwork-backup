/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp.ui.table;

import com.opensymphony.webwork.components.Component;
import com.opensymphony.webwork.components.table.WebTable;
import com.opensymphony.webwork.views.jsp.ui.ComponentTag;
import com.opensymphony.xwork.util.OgnlValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @jsp.tag name="table" bodycontent="JSP"
 * @author $author$
 * @version $Revision$
 */
public class WebTableTag extends ComponentTag {
    protected String sortOrder;
    protected String modelName;
    protected boolean sortable;
    protected int sortColumn;

    public Component getBean(OgnlValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new WebTable(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();

        WebTable table = (WebTable) component;
        table.setSortOrder(sortOrder);
        table.setSortable(sortable);
        table.setModelName(modelName);
        table.setSortOrder(sortOrder);
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * @jsp.attribute required="true"  rtexprvalue="true"
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    /**
     * @jsp.attribute required="false"  rtexprvalue="true"
     */
    public void setSortColumn(int sortColumn) {
        this.sortColumn = sortColumn;
    }
}