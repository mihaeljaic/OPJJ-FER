<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Status</title>
</head>

<body bgcolor="<%= Util.getBgColor(request)%>">
	<%
		String message = (String) request.getAttribute("status");
		out.print(message);
	%>
</body>
</html>