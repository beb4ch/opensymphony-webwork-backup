<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ taglib uri="/webwork" prefix="ww" %>
<%@ page contentType="text/plain;charset=UTF-8" language="java" %>
Category set to <ww:property value="categoryId"/>