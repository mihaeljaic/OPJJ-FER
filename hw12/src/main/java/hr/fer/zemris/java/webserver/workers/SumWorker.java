package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Web worker that gets 2 numbers and adds them. If 2 parameters weren't given
 * they are set to default values. Parameter's "a" default value is "1" and
 * parameter's "b" default value is "2".
 * 
 * @author Mihael Jaić
 *
 */

public class SumWorker implements IWebWorker {
	/**
	 * Path to calculation script.
	 */
	private static final String calculationScript = "/private/calc.smscr";
	/**
	 * Default value of parameter a.
	 */
	private static final int aDefaultValue = 1;
	/**
	 * Default value of parameter b.
	 */
	private static final int bDefaultValue = 2;

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String aParam = context.getParameter("a");
		String bParam = context.getParameter("b");

		int a = aDefaultValue;
		int b = bDefaultValue;
		try {
			a = Integer.parseInt(aParam);
		} catch (NumberFormatException ignorable) {
		}

		try {
			b = Integer.parseInt(bParam);
		} catch (NumberFormatException ignorable) {
		}

		context.setTemporaryParameter("sum", Integer.toString(a + b));
		context.setTemporaryParameter("a", Integer.toString(a));
		context.setTemporaryParameter("b", Integer.toString(b));

		context.getDispatcher().dispatchRequest(calculationScript);
	}

}
