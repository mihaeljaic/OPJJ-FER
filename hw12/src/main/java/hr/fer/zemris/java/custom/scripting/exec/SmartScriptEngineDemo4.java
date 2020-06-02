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
 * Demonstration of {@link SmartScriptEngine} that uses "fibonacci.smscr"
 * script.
 */

public class SmartScriptEngineDemo4 {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 * @throws Exception
	 *             If there were errors while executing script.
	 */

	public static void main(String[] args) throws Exception {
		String documentBody = Util.readFile("./src/main/resources/" + "fibonacci.smscr");
		
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
	}
}
