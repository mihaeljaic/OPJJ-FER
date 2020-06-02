<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Edit blog entry</title>
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
	
	<h3>
		Blog entry id: ${blogEntry.id}<br>Title: ${blogEntry.title}
	</h3>
	
	<h4>Blog entry text:</h4>
	
	<form action="/blog/servleti/edit?id=${blogEntry.id}"
		method="POST">
		<textarea name="edited" rows="6" cols="50">${blogEntry.text}</textarea>
		<br> <br> <input type="submit" value="Submit change">
	</form>
	
	
	
</body>
</html>