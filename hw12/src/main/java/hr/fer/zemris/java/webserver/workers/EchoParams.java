package hr.fer.zemris.java.webserver.workers;

import java.util.Map;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web worker that gets list of arguments in request context
 * {@link RequestContext} and displays them.
 * 
 * @author Mihael Jaić
 *
 */

public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		Map<String, String> params = context.getParameters();
		context.setMimeType("text/html");
		context.write("<html><body><table style=\"width:100%\"><tr><th>Parameters</th></tr>");

		if (params.isEmpty()) {
			context.write("<tr>No parameters were given</tr>");
		}

		for (Map.Entry<String, String> parameter : params.entrySet()) {
			context.write(String.format("<tr><td>%s</td></tr>", parameter.getValue()));
		}

		context.write("</table></body></html>");
	}

}
