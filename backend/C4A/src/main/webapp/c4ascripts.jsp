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
        8080, 
        request.getContextPath()).toString() + "';";
    response.getWriter().write(dynamicRoot);
    
    String dynamicServerRoot = "var dynamicServerRoot = '" + new URL(request.getScheme(), 
        request.getServerName(), 
        8080, 
        "").toString() + "';";
    response.getWriter().write(dynamicServerRoot);
%>