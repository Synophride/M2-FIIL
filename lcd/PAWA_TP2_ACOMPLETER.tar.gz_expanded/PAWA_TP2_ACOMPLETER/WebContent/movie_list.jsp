<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Liste des films</title>
</head>
<body>
	<h1>Liste des films</h1>
	<!-- A COMPLETER QUESTION 6, 8 et 9 -->
	<c:choose> 
		<c:when test="${empty movies}">
			PAS DE FILMS TROUVES
		</c:when>
		<c:otherwise>
			<ul>
			<c:forEach var="m" items="${movies}">
				<li> <c:out value="${m.first}"/> <b /> <c:out value="${m.second}" /> <b/> </li>
			</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
</body>
</html>