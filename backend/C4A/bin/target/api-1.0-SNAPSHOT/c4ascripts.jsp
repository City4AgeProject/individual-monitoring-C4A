<%-- 
    Document   : c4ascripts
    Created on : Dec 8, 2016, 11:19:56 AM
    Author     : mnou2
--%>

<%@page import="java.net.URL"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String dynamicRoot = "var dynamicRoot = '" + new URL(request.getScheme(), 
        request.getServerName(), 
        request.getServerPort(), 
        request.getContextPath()).toString() + "';";
    response.getWriter().write(dynamicRoot);
%>