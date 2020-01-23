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
	
	<!-- empty vaut vrai si l'objet est null ou si
	 c'est une collection vide.
	 
	 ici movies est un attribut de la requête http, positionée
	 dans MovieListServlet. Il contient un vecteur de paires
	 id de film, titre
 -->
	<c:choose>
		<c:when test="${! empty movies }">
			<ul>
				<!--  une itération avec un for-each, la variable de boucle est m -->
				<c:forEach var="m" items="${movies}">
					<!--  m.first appelle m.getFirst() et m.second appelle m.getSecond() -->
					<li><c:out value="${m.first}" /> : <b></b><c:out value="${m.second}" /></b></li>
				</c:forEach>
			</ul>
		</c:when>
		<c:when test="${ movies == null}">
			<!--  la variable de requête 'movies' n'a pas été initialisée -->
			<c:redirect url="MovieListServlet?from=0&to=10" />
		</c:when>
		<c:otherwise>
		<b>Aucun film dans la base</b>
		</c:otherwise>
	</c:choose>

	<!--  'from' et 'to' sont des paramètres de la requête, placés par le contrôleur -->
	<c:set var="from1" value="${from - 10}" />
	<c:set var="from2" value="${from + 10}" />
	<c:set var="to1" value="${to - 10}" />
	<c:set var="to2" value="${to + 10}" />

	<div style="text-align: center">
		<c:if test="${from1 >= 0 }">
			<a href="MovieListServlet?from=${from1}&to=${to1}">&lt;</a>
		</c:if>
		---
		<c:if test="${from2 < sessionScope.db.numMovies }">
			<a href="MovieListServlet?from=${from2}&to=${to2}">&gt;</a>
		</c:if>
	</div>
	<form method="post" action="LogoutServlet" > 
	<input type="submit" value="déconnexion" />
	</form>
</body>
</html>