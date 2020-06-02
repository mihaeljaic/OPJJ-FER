<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="hr.fer.zemris.java.webapp2.VotingResults.Result"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
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

<body bgcolor="<%=Util.getBgColor(request)%>">
	<h1>Results of voting</h1>
	<p>This are voting results.</p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Band name</th>
				<th>Vote count</th>
			</tr>
		</thead>
		<tbody>

			<%
				List<Result> results = (List<Result>) request.getAttribute("results");

				for (Result result : results) {
					out.print(
							String.format("<tr><td>%s</td><td>%d</td></tr>", result.getBandName(), result.getVoteCount()));
				}
			%>

		</tbody>
	</table>

	<h2>Graphical display of results</h2>
	<img alt="Pie-chart" src="/webapp2/voting-graphics" height="400"
		width="400" />

	<h2>Results in XLS format</h2>
	<p>
		Results in XLS formate are available <a href="/webapp2/voting-xls">here</a>
	</p>

	<h2>Miscellaneous</h2>
	<p>Song examples of victorious band/s:</p>

	<%
		out.print("<ul>");
		int voteCount = 0;
		for (Result result : results) {
			if (result.getVoteCount() < voteCount) {
				break;
			}

			out.print(String.format("<li><a href=\"%s\" target=\"blank\">%s</a></li>", result.getSongurl(),
					result.getBandName()));
			voteCount = result.getVoteCount();
		}
		out.print("</ul>");
	%>

</body>
</html>