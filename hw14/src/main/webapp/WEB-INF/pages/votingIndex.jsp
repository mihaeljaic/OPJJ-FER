<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="hr.fer.zemris.java.p12.dao.model.Poll"%>
<%@ page import="hr.fer.zemris.java.p12.dao.model.PollOption"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Voting</title>
</head>

<body>

	<h1>${poll.title}:</h1>
	<p>${poll.message}</p>

	<%
		List<PollOption> pollOptions = (List<PollOption>) request.getAttribute("pollOptions");

		out.print("<ol>");
		for (PollOption pollOption : pollOptions) {
			out.write(String.format("<li><a href=\"/voting-app/servlets/voting-vote?voteID=%s\">%s</a></li>",
					pollOption.getId(), pollOption.getTitle()));
		}
		out.print("</ol>");
	%>

</body>
</html>