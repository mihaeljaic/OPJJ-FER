<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="hr.fer.zemris.java.webapp2.Voting.BandInfo"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Vote for favorite band</title>
</head>

<body bgcolor="<%=Util.getBgColor(request)%>">

	<h1>Voting for favorite band:</h1>
	<p>From following bands, which is your favorite? Click on link to
		vote!</p>

	<%
		String votingPath = "/webapp2/voting-vote";
		Map<String, BandInfo> bands = (Map<String, BandInfo>) request.getSession().getAttribute("bands");

		out.print("<ol>");
		for (Map.Entry<String, BandInfo> band : bands.entrySet()) {
			out.print(String.format("<li><a href=\"%s?id=%s\">%s</a></li>", votingPath, band.getKey(),
					band.getValue().getBandName()));
		}
		out.print("</ol>");
	%>

</body>
</html>