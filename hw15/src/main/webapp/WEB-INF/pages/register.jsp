<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Blog user registration</title>
</head>
<body>
	
	<c:choose>
		<c:when test="${sessionScope[\"current.user.id\"] != null }">
			<h2>You are logged in as ${sessionScope["current.user.fn"]} ${sessionScope["current.user.ln"]}(${sessionScope["current.user.nick"] })</h2>
			<p>
				<a href="/blog/servleti/logout">Log out</a><br>
				<a href="/blog/servleti/main">Main page</a>
			</p>
		</c:when>
		<c:otherwise>
			<h2>Not logged in</h2>
			<a href="/blog/servleti/main">Main page</a>
		</c:otherwise>
	</c:choose>
	
	<form action="/blog/servleti/register" method="POST">
		First name:<br> <input type="text" name="fName"><br> Last name:<br>
		<input type="text" name="lName"><br> Email:<br> <input
			type="text" name="email"><br> Nick:<br> <input type="text" name="nick"><br>
		Password:<br> <input type="password" name="password"><br> <input
			type="submit" value="Sign up">
	</form>

</body>
</html>