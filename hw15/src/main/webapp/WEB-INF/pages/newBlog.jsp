<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>New blog</title>
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
	
	<form action="/blog/servleti/newBlog" method="POST">
		Blog entry title:<br> <input type="text" name="title"><br>Text:<br>
		<textarea name="text" rows="6" cols="50"></textarea><br><br>
		<input type="submit" value="Create blog entry">
	</form>

</body>
</html>