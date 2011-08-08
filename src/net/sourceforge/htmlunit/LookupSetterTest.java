package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class LookupSetterTest {
	private final String defineSetterAndGetterX = 
		"Foo.__defineSetter__('x', function() {}); \n"
		+ "Foo.__defineGetter__('x', function() {return 'hello' });\n";

	@Test
	public void typeof() throws Exception {
		test("function", "typeof Foo.__lookupSetter__('x');");
		test("function", "typeof Foo.__lookupGetter__('x');");
		test("function", "typeof (new Foo()).__lookupSetter__('s')");
		test("function", "typeof (new Foo()).__lookupGetter__('s')");
	}

	@Test
	public void f() throws Exception {
		final String js = ""
            + "  function handler(e) {\n"
            + "    if (e && e.target.value.length > 2)\n"
            + "      e.preventDefault();\n"
            + "    else if (!e && window.event.srcElement.value.length > 2)\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('text1').onkeydown = handler;\n"
            + "  }\n";

		final ContextAction action = new ContextActionImpl() {
			@Override
			protected Object doRun(Context cx) throws Exception {
				final Scriptable scope = cx.initStandardObjects(new TopScope());
				ScriptableObject.defineClass(scope, Foo.class);
				Object result = cx.compileString(js, "test", 1, null);
				return null;
			}
		};

		Utils.runWithAllOptimizationLevels(action);
	}

	@Test
	public void toStringConversion() throws Exception {
	//	test("function(){return\"hello\";}", "Foo.__lookupGetter__('x').toString().replace(/\\s/g, '');");
		test("hello", "new Foo().__lookupGetter__('s').toString()");
	}

	private void test(final String expected, final String src) throws Exception {
		final ContextAction action = new ContextActionImpl() {
			@Override
			protected Object doRun(Context cx) throws Exception {
				final Scriptable scope = cx.initStandardObjects(new TopScope());
				ScriptableObject.defineClass(scope, Foo.class);
				cx.evaluateString(scope, defineSetterAndGetterX, "initX", 1, null);
				Object result = cx.evaluateString(scope, src, "test", 1, null);
				Assert.assertEquals(expected, result);
				return null;
			}
		};

		Utils.runWithAllOptimizationLevels(action);
	}

	public static class Foo extends ScriptableObject {
		private static final long serialVersionUID = 2997084265543252674L;
		private String s = "hello";

		public Foo() { /* Empty. */
		}

		public String jsGet_s() {
			return this.s;
		}

		public void jsSet_s(String s) {
			this.s = s;
		}

		public String getClassName() {
			return "Foo";
		}
	}

	public static class TopScope extends ScriptableObject {
		public String getClassName() {
			return "TopScope";
		}
	}
}
