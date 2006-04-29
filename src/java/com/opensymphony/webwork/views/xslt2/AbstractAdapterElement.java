/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.xslt2;

import org.w3c.dom.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;


/**
 * AbstractAdapterElement extends the abstract Node type and implements
 * the DOM Element interface.
 *
 * @author <a href="mailto:meier@meisterbohne.de">Philipp Meier</a>
 * @author Pat Niemeyer (pat@pat.net)
 */
public abstract class AbstractAdapterElement
	extends AbstractAdapterNode implements Element
{
	//~ Instance fields ////////////////////////////////////////////////////////
	private Log log = LogFactory.getLog( this.getClass() );
	private Map attributeAdapters;

	//~ Constructors ///////////////////////////////////////////////////////////

	public AbstractAdapterElement() { }

	//~ Methods ////////////////////////////////////////////////////////////////

	public void setAttribute(String string, String string1) throws DOMException {
		operationNotSupported();
	}

	protected Map getAttributeAdapters() {
		if ( attributeAdapters == null )
			attributeAdapters = buildAttributeAdapters();
		return attributeAdapters;
	}

	protected Map buildAttributeAdapters() {
		return new HashMap();
	}

	/**
	 * No attributes, return empty attributes if asked.
	 */
	public String getAttribute(String string) {
		return "";
	}

	public void setAttributeNS(String string, String string1, String string2) throws DOMException {
		operationNotSupported();
	}

	public String getAttributeNS(String string, String string1) {
		return null;
	}

	public Attr setAttributeNode(Attr attr) throws DOMException {
		operationNotSupported();
		return null;
	}

	public Attr getAttributeNode( String name ) {
		return (Attr)getAttributes().getNamedItem( name );
	}

	public Attr setAttributeNodeNS(Attr attr) throws DOMException {
		operationNotSupported();

		return null;
	}

	public Attr getAttributeNodeNS(String string, String string1) {
		operationNotSupported();

		return null;
	}

	public String getNodeName() {
		return getTagName();
	}

	public short getNodeType() {
		return Node.ELEMENT_NODE;
	}

	public String getTagName() {
		return getPropertyName();
	}

	public boolean hasAttribute(String string) {
		return false;
	}

	public boolean hasAttributeNS(String string, String string1) {
		return false;
	}

	public boolean hasChildNodes() {
		return getElementsByTagName("*").getLength() > 0;
	}

	public void removeAttribute(String string) throws DOMException {
		operationNotSupported();
	}

	public void removeAttributeNS(String string, String string1) throws DOMException {
		operationNotSupported();
	}

	public Attr removeAttributeNode(Attr attr) throws DOMException {
		operationNotSupported();

		return null;
	}

}
