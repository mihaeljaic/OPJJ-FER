package hr.fer.zemris.java.custom.scripting.exec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.util.Util;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Demonstration of {@link SmartScriptEngine} that uses "brojPoziva.smscr"
 * script.
 */

public class SmartScriptEngineDemo3 {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 * @throws Exception
	 *             If there were errors while executing script.
	 */

	public static void main(String[] args) throws Exception {
		String documentBody = Util.readFile("./src/main/resources/brojPoziva.smscr");

		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);

		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));

	}
}
