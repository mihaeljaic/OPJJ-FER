<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="hr.fer.zemris.java.p12.dao.model.Poll"%>
<%@ page import="java.util.List"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Voting polls</title>
</head>
<body>
	
	<h2>
		Choose voting poll
	</h2>
	
	<%
		List<Poll> polls = (List<Poll>) request.getAttribute("polls");
	
		out.write("<ol>");
		for (Poll poll : polls) {
			out.write(String.format("<li><a href=\"/voting-app/servlets/voting?pollID=%d\">%s</a></li>", poll.getId(), poll.getTitle()));
		}
		out.write("</ol>");
	%>
	
</body>
</html>