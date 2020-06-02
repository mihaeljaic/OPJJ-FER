<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Blog entry</title>
</head>
<body>

	<c:choose>
		<c:when test="${sessionScope[\"current.user.id\"] != null }">
			<h2>You are logged in as ${sessionScope["current.user.fn"]}
				${sessionScope["current.user.ln"] }(${sessionScope["current.user.nick"] })</h2>
			<p>
				<a href="/blog/servleti/logout">Log out</a><br> <a
					href="/blog/servleti/main">Main page</a>
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

	<h3>Blog entry text:</h3>

	<textarea readonly rows="20" cols="50">${blogEntry.text}</textarea><br>

	<h3>
		Posted by: ${blogEntry.blogUser.nick}<br>Last modified at: ${blogEntry.lastModifiedAt }
	</h3>

	<c:choose>
		<c:when test="${logedin==true}">
			<br>
			<h4>Edit blog entry:</h4>
			<a
				href="/blog/servleti/author/${blogEntry.blogUser.nick}/edit?id=${blogEntry.id}">Edit
				blog entry</a>
		</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
	<br>



	<h4>Comments:</h4>
	<c:choose>
		<c:when test="${nocomments==true}">No comments were posted yet. Be first to comment!</c:when>
		<c:otherwise>
			<ol>
				<c:forEach var="comment" items="${blogEntry.comments}">
					<li>${comment.message}<br>Posted by:
						${comment.usersNick}, on: ${comment.postedOn}
					</li>
				</c:forEach>
			</ol>
		</c:otherwise>
	</c:choose>

	<br>
	<h4>Post new comment:</h4>
	<form action="/blog/servleti/newComment?id=${blogEntry.id}"
		method="POST">
		<textarea name="message" rows="6" cols="50"></textarea>
		<br> <br> <input type="submit" value="Post comment">
	</form>

</body>
</html>