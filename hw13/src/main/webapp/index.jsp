<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
</head>

<body bgcolor="<%=Util.getBgColor(request)%>">
	<a href="colors.jsp">Background color chooser</a>
	<br>
	<a href="/webapp2/trigonometric?a=0&b=90">Trigonometric</a>
	<br>
	<a href="stories/funny.jsp">Funny story</a>
	<br>
	<a href="/webapp2/powers?a=1&b=100&n=3">Powers</a>
	<br>
	<a href="appinfo.jsp">App info</a>
	<br>
	<a href="/webapp2/voting">Vote</a>
	<br>
	<a href="report.jsp">OS survey report</a>
	<br>

	<p>Trigonometric</p>
	<form action="trigonometric" method="GET">
		Start angle:<br> <input type="number" name="a" min="0" max="360"
			step="1" value="0"><br> End angle:<br> <input
			type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Get table"><input type="reset"
			value="Reset">
	</form>

	<p>Powers</p>
	<form action="powers" method="GET">
		Start number:<br> <input type="number" name="a" min="-100"
			max="100" step="1" value="0"><br> End number:<br> <input
			type="number" name="b" min="-100" max="100" step="1" value="0"><br>
		Power:<br> <input type="number" name="n" min="1" max="5" step="1"
			value="1"><br> <input type="submit" value="Get powers"><input
			type="reset" value="Reset">
	</form>

</body>
</html>