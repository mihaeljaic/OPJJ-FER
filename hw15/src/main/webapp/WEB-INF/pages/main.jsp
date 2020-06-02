<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welcome</title>
</head>
<body>

	<c:choose>
		<c:when test="${sessionScope[\"current.user.id\"] != null }">
			<h2>Welcome, ${sessionScope["current.user.fn"]} ${sessionScope["current.user.ln"] }(${sessionScope["current.user.nick"] })</h2>
			<p>
				<a href="/blog/servleti/logout">Log out</a>
			</p>
		</c:when>
		<c:otherwise>
			<h2>${status}</h2>
			<form action="/blog/servleti/login" method="POST">
				Nickname:<br> <input type="text" name="nick" value="${nick }"><br>
				Password:<br> <input type="password" name="password"><br>
				<input type="submit" value="Log in">
			</form>
			<p>
				<a href="/blog/servleti/register">Sign up</a>
			</p>
		</c:otherwise>
	</c:choose>

	<h3>List of registered authors:</h3>

	<c:choose>
		<c:when test="${isEmpty==true}">No authors registered yet. Be first one to register!</c:when>
		<c:otherwise>
			<ol>
				<c:forEach var="user" items="${users}">
					<li><a href="/blog/servleti/author/${user.nick}">${user.nick}</a></li>
				</c:forEach>
			</ol>
		</c:otherwise>
	</c:choose>
</body>
</html>