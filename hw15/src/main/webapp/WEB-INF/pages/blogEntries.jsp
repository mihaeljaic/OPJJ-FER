<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>List of blog entries by ${author}</title>
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
	
	<h2>Blog entries by: ${author}</h2>
	
	<c:choose>
		<c:when test="${noBlogs==true}">User has not posted any blog entries yet<br></c:when>
	<c:otherwise>
	<ol>
	<c:forEach var="entry" items="${blogs}">
		<li><a href="/blog/servleti/author/${author}/${entry.id}">${entry.title}</a></li>
	</c:forEach>
	</ol>
	</c:otherwise>
	</c:choose>
	
	<c:choose>
   		<c:when test="${logedin==true}"><a href="/blog/servleti/author/${author}/new">Add new blog entry</a></c:when>
		<c:otherwise><p>Log in as '${author}' to add blog entry</p></c:otherwise>
	</c:choose>
	
</body>
</html>