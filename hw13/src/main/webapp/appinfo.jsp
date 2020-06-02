<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.concurrent.TimeUnit"%>
<%@ page import="hr.fer.zemris.java.webapp2.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>App info</title>
</head>

<body bgcolor="<%= Util.getBgColor(request)%>">
	<%
		Long startMillis = ((Long) request.getServletContext().getAttribute("startTime"));
		if (startMillis == null) {
			out.print("<p>Time wasn't initialized.</p>");
			return;
		}

		long millis = System.currentTimeMillis() - startMillis;

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		millis -= TimeUnit.SECONDS.toMillis(seconds);

		out.print("<h3>Server uptime:</h3>");
		out.print(String.format("<p>%d days %d hours %d minutes %d seconds %d milliseconds</p>", days, hours,
				minutes, seconds, millis));
	%>
</body>
</html>