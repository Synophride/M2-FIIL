<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="pawa.tpnote.URLInfo"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ADD URL</title>
</head>
<body>
	<!--  QUESTION 1.6 -->
	<!--  html ici -->

	<%
		URLInfo info = (URLInfo) request.getAttribute("info");
	%>
	<p>
		HASH=
		<%=info.getHash()%>
	</p>
	<br />
	<p>
		PASS=
		<%=info.getPass()%>
	</p>
	<br />
	<p>
		LINK= <a href="<%=info.getUrl() + "/GoServlet?h=" + info.getHash()%>">
			<%=info.getUrl() + "/GoServlet?h=" + info.getHash()%>
		</a>
	</p>
	<br />
</body>
</html>