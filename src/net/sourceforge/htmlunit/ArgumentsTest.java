package net.sourceforge.htmlunit;

import org.junit.Test;

/**
 * String conversion of Arguments array used to be '[object Object]',
 * with EcmaScript 5 it is now '[object Arguments]' what is not good for HtmlUnit.
 * @author Marc Guillemot
 */
public class ArgumentsTest
{
	/**
	 * @throws Exception if the test fails
	 */
	@Test
	public void argumentstoString() throws Exception {
		final String script = "function f() {\n"
			+ "var s = arguments.toString();"
			+ "if ('[object Object]' != s)\n"
			+ "  throw 'got: ' + s;"
			+ "}\n"
			+ "f()";
		
		Utilities.executeScript(script);
	}
}
