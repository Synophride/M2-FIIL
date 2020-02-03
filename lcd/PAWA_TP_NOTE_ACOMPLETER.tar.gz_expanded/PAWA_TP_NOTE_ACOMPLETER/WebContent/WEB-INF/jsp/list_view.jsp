<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pawa.tpnote.URLInfo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ADD URL</title>
</head>
<body>
<!-- POUR L'EXERCICE 1.4, ne pas modifier -->
<ul>
<c:forEach var="info" items="${infos}">
<li>
<c:out value="${info.url }" />, <c:out value="${info.hash}" />, <c:out value="${info.count}" />, <c:out value="${info.pass}"/>   
</li>
</c:forEach>
</ul>
</body>
</html>