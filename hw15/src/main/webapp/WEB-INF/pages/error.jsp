<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Error</title>
</head>
<body>
	
	<c:choose>
		<c:when test="${sessionScope[\"current.user.id\"] != null }">
			<h2>You are logged in as ${sessionScope["current.user.fn"]} ${sessionScope["current.user.ln"]}(${sessionScope["current.user.nick"] })</h2>
		</c:when>
		<c:otherwise>
			<h2>Not logged in</h2>
		</c:otherwise>
	</c:choose>
	
	<h2>${message}</h2>
	<a href="/blog/servleti/main">Back to main page</a>
	
</body>
</html>