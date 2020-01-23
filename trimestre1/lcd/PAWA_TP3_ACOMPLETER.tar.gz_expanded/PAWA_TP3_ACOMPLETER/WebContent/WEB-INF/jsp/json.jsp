<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	out.print(request.getAttribute("json").toString());
%>