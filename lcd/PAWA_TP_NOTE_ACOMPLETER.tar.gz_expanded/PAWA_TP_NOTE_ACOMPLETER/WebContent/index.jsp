<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Index</title>
</head>
<body>
<form method="get" action="AddServlet">
URL : <input type="text" name="url"> </input><br/>
<button type="submit">Générer un lien</button>
</form><br/>

<form method="get" action="RemoveServlet">
Hash : <input type="text" name="hash"> </input><br/>
Pass : <input type="text" name="pass"> </input><br/>
<button type="submit">Supprimer le lien</button>
</form><br/>

<a href="ListServlet">Page de statistiques</a><br/>


<form method="get" action="GoServlet">
URL : <input type="text" name="h"> hash</input><br/>
<button type="submit">go</button>
</form><br/>
</body>
</html>