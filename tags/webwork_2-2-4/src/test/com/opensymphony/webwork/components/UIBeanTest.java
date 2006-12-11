/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.components;

import com.opensymphony.webwork.components.Form;
import com.opensymphony.webwork.components.TextField;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.opensymphony.webwork.WebWorkTestCase;
import com.opensymphony.xwork.util.OgnlValueStack;

/**
 * 
 * @author tm_jee
 * @version $Date$ $Id$
 */
public class UIBeanTest extends WebWorkTestCase {
	public void testPopulateComponentHtmlId1() throws Exception {
		OgnlValueStack stack = new OgnlValueStack();
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		
		Form form = new Form(stack, req, res);
		form.getParameters().put("id", "formId");
		
		TextField txtFld = new TextField(stack, req, res);
		txtFld.setId("txtFldId");
		
		txtFld.populateComponentHtmlId(form);
		
		assertEquals("txtFldId", txtFld.getParameters().get("id"));
	}
	
	public void testPopulateComponentHtmlId2() throws Exception {
		OgnlValueStack stack = new OgnlValueStack();
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		
		Form form = new Form(stack, req, res);
		form.getParameters().put("id", "formId");
		
		TextField txtFld = new TextField(stack, req, res);
		txtFld.setName("txtFldName");
		
		txtFld.populateComponentHtmlId(form);
		
		assertEquals("formId_txtFldName", txtFld.getParameters().get("id"));
	}
}