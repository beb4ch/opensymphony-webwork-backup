package com.opensymphony.webwork.views.jsp.ui;

import com.opensymphony.webwork.components.UIBean;
import com.opensymphony.xwork.util.OgnlUtil;
import com.opensymphony.xwork.util.OgnlValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: plightbo
 * Date: Jul 30, 2005
 * Time: 5:35:42 PM
 */
public class Debug extends UIBean {
    public static final String TEMPLATE = "debug";

    public Debug(OgnlValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    public void start(Writer writer) {
        super.start(writer);

        OgnlValueStack stack = getStack();
        Iterator iter = stack.getRoot().iterator();
        List stackValues = new ArrayList(stack.getRoot().size());
        while (iter.hasNext()) {
            Object o = iter.next();
            Map values;
            try {
                values = OgnlUtil.getBeanMap(o);
            } catch (Exception e) {
                throw new RuntimeException("Caught an exception while getting the property values of " + o, e);
            }
            stackValues.add(new DebugMapEntry(o.getClass().getName(), values));
        }

        addParameter("stackValues", stackValues);
    }

    private class DebugMapEntry implements Map.Entry {
        private Object key;
        private Object value;

        DebugMapEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object newVal) {
            Object oldVal = value;
            value = newVal;
            return oldVal;
        }
    }

}