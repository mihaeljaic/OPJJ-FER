<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Report</title>

</head>

<body bgcolor="<%=Util.getBgColor(request)%>">
	<h3>OS usage</h3>
	<p>Here are the results of OS usage in survey that we completed.</p>

	<img alt="OS usage" src="/webapp2/reportImage" width="400"
		height="400" />

</body>
</html>