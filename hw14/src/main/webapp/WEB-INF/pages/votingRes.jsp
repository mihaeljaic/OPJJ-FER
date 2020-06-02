<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="hr.fer.zemris.java.p12.dao.model.PollOption"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Results of voting</title>
<style type="text/css">
table.rez td {
	text-align: center;
}
</style>
</head>

<body>
	<h1>Results of voting</h1>
	<p>This are voting results.</p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>${poll.title}</th>
				<th>Vote count</th>
			</tr>
		</thead>
		<tbody>

			<%
				List<PollOption> results = (List<PollOption>) request.getAttribute("results");

				for (PollOption result : results) {
					out.print(String.format("<tr><td>%s</td><td>%d</td></tr>", result.getTitle(), result.getVotesCount()));
				}
			%>

		</tbody>
	</table>

	<h2>Graphical display of results</h2>
	<img alt="Pie-chart" src="/voting-app/servlets/voting-graphics"
		height="400" width="400" />

	<h2>Results in XLS format</h2>
	<p>
		Results in XLS formate are available <a href="/voting-app/servlets/voting-xls">here</a>
	</p>

	<h2>Miscellaneous</h2>
	<p>Links to victorious contestant/s:</p>

	<%
		out.print("<ul>");
		int voteCount = 0;
		for (PollOption result : results) {
			if (result.getVotesCount() < voteCount) {
				break;
			}

			out.print(String.format("<li><a href=\"%s\" target=\"blank\">%s</a></li>", result.getLink(),
					result.getTitle()));
			voteCount = Math.max(voteCount, result.getVotesCount());
		}
		out.print("</ul>");
	%>
	
	<a href="/voting-app/servlets/index.html">Back to vote poll</a>

</body>
</html>