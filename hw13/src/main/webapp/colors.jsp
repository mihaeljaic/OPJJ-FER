<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Colors</title>
</head>

<body bgcolor="<%=Util.getBgColor(request)%>">
	<h3>Set background color</h3>
	<a href="/webapp2/setcolor?color=WHITE">WHITE</a>
	<br>
	<a href="/webapp2/setcolor?color=RED">RED</a>
	<br>
	<a href="/webapp2/setcolor?color=GREEN">GREEN</a>
	<br>
	<a href="/webapp2/setcolor?color=CYAN">CYAN</a>
</body>

</html>