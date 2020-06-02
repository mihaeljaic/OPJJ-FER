<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Trigonometric</title>
</head>

<body bgcolor="<%=Util.getBgColor(request)%>">

	<%
		out.print("<table border=\"1\"><tr><th>x</th><th>sin(x)</th><th>cos(x)</th></tr>");

		List<Double> sinus = (List<Double>) request.getAttribute("sinus");
		List<Double> cosinus = (List<Double>) request.getAttribute("cosinus");
		int a = (int) request.getAttribute("a");
		int b = (int) request.getAttribute("b");

		for (int x = a, i = 0, size = sinus.size(); i < size; i++, x++) {
			out.print(String.format("<tr><td>%d</td><td>%.3f</td><td>%.3f</td></tr>", x, sinus.get(i),
					cosinus.get(i)));
		}

		out.print("</table>");
	%>
	
</body>
</html>