<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>Funny</title>
</head>

<body bgcolor="<%=Util.getBgColor(request)%>">

	<%
		out.print(String.format("<font color=%s>%s</font>", Util.getRandomColor(), Util.STORY));
	%>

</body>
</html>