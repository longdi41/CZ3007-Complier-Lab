package test;

import static org.junit.Assert.fail;

import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import parser.Parser;

public class ParserTests {
	private void runtest(String src) {
		runtest(src, true);
	}

	private void runtest(String src, boolean succeed) {
		Parser parser = new Parser();
		try {
			parser.parse(new Lexer(new StringReader(src)));
			if(!succeed) {
				fail("Test was supposed to fail, but succeeded");
			}
		} catch (beaver.Parser.Exception e) {
			if(succeed) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testEmptyModule() {
		runtest("module Test { }");
	}
	
	@Test
	public void testFuncDeclare() {
		runtest("module Test {public int func1() {}}");
	}

	@Test
	public void test() {
		runtest("module Test { import TensorFlow; }");
		runtest("module Test { import TensorFlow; int a; }");
		runtest("module Test { import TensorFlow; int a; public boolean foo(int b, int c){a = 2+3; return 35; break;} }");
		runtest("module Test { import TensorFlow; int a; public boolean foo(int b, int c){ if(a > 20){return b[23]==c;} } }");
		runtest("module Test { import TensorFlow; int a; public boolean foo(int b, int c){a; b = 3;  hello = dunno(c, d, 2018%99) + (25 / c*d); not_happy = \"NOT HAPPY\"; } }");
		runtest("module Test { import TensorFlow; int a; public boolean foo(int b, int c){a; b = 3;  hello = dunno(b) + a; not_happy = \"NOT HAPPY\"; } }");
	}

	@Test
	public void testModuleImports() {
		runtest("module Test {"
				+ "import module1;"
				+ "import module2;"
				+ "}");
	}

	@Test
	public void testModuleTypeDeclaration() {
		runtest("module Test {"
				+ "public type float = \"FLOAT\";"
				+ "type enum = \"ENUM\";"
				+ "}");
	}

	@Test
	public void testModuleFieldDeclaration() {
		runtest("module Test {"
				+ "public boolean booleanfield;"
				+ "int intfield;"
				+ "}");
	}

	@Test
	public void testModuleEmptyFunctionDeclaration() {
		runtest("module Test {"
				+ "public void fun() {}"
				+ "}");
	}

	@Test
	public void testModuleParameterFunctionDeclaration() {
		runtest("module Test {"
				+ "public void fun(int param) {}"
				+ "}");
	}

	@Test
	public void testModuleParameterListFunctionDeclaration() {
		runtest("module Test {"
				+ "public void fun(int param1, boolean param2) {}"
				+ "}");
	}

	@Test
	public void testModuleIfWhileFunctionDeclaration() {
		runtest("module Test {"
				+ "public void fun() {"
				+ "  int x;"
				+ "  if(x < 10) {return 10;}"
				+ "  else {return;}"
				+ ""
				+ "  while(x < 15) {x = 10; break; }"
				+ "}"
				+ "}");
	}

	@Test
	public void testModuleFunctionDeclaration() {
		runtest("module Test {public void fun() {a[(1+1) / 2] = 1;}}");
	}

	@Test
	public void testFunctionCall(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { abc(); } }");
		runtest("module Test{ public int function_name() { abc(a=1); } }");
		runtest("module Test{ public int function_name() { abc(a=1,b=2); } }");
		runtest("module Test{ public int function_name() { abc(a=1); } }");
		runtest("module Test{ public int function_name() { abc(a,b); } }");
		runtest("module Test{ public int function_name() { abc(1,2); } }");
	}

	@Test
	public void testArrayExpression(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { []; } }", false);
		runtest("module Test{ public int function_name() { [a=1]; } }");
		runtest("module Test{ public int function_name() { [a=1,b=2]; } }");
		runtest("module Test{ public int function_name() { [a]; } }");
		runtest("module Test{ public int function_name() { [a,b]; } }");
		runtest("module Test{ public int function_name() { [1]; } }");
		runtest("module Test{ public int function_name() { [1,2]; } }");
		
	}

	@Test
	public void testBooleanLiteral(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { true; } }");
		runtest("module Test{ public int function_name() { false; } }");
	}

	@Test
	public void testParenthesisExpression(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { (); } }", false);
		runtest("module Test{ public int function_name() { (1+1); } }");
		runtest("module Test{ public int function_name() { (true+true); } }");
		runtest("module Test{ public int function_name() { (a[1]+b[c=2]); } }");
	}
	
	@Test
	public void testFactor(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { -123; } }");
		runtest("module Test{ public int function_name() { -\"abc\"; } }");
		runtest("module Test{ public int function_name() { -true; } }");
		runtest("module Test{ public int function_name() { -a[1]; } }");
		runtest("module Test{ public int function_name() { -abc(a=1); } }");
		runtest("module Test{ public int function_name() { a=-3; } }");
	}

	@Test
	public void testTerm(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { -123*-\"abc\"; } }");
		runtest("module Test{ public int function_name() { -true*-a[1]; } }");
		runtest("module Test{ public int function_name() { -abc(a=1)*-3; } }");
		runtest("module Test{ public int function_name() { -abc(a=1)*def(); } }");
	}

	@Test
	public void testArithmeticExpression(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { -123*-\"abc\"--true*-a[1]; } }");
		runtest("module Test{ public int function_name() { -abc(a=1)*-3+-abc(a=1)*def(); } }");
	}

	@Test
	public void testRHSAssignment(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { a()<>1; } }", false);
		runtest("module Test{ public int function_name() { a()==true; } }");
		runtest("module Test{ public int function_name() { a()!=1; } }");
		runtest("module Test{ public int function_name() { a()<\"abc\"; } }");
		runtest("module Test{ public int function_name() { a()>a(); } }");
		runtest("module Test{ public int function_name() { a()<=a(); } }");
		runtest("module Test{ public int function_name() { a()>=true; } }");
		runtest("module Test{ public int function_name() { -123*-\"abc\"--true*-a[1]==-abc(a=1)*-3+-abc(a=1)*def(); } }");
	}

	@Test
	public void testAssignment(){
		runtest("module Test{ public int function_name() {  } }");
		runtest("module Test{ public int function_name() { a=1; } }");
		runtest("module Test{ public int function_name() { a=b=c=3; } }");
		runtest("module Test{ public int function_name() { a=a()==true==false==123==-3; } }", false);
	}

	@Test
	public void testStatement() {
		runtest("module Test { int a; int[] b; String c;}");
		runtest("module Test { int a; int b; boolean c; public void testFunction() { if(a-b<1) {} while(c == true) {} while(c == true) {break;} if(c == true) {return;}} }");
		runtest("module Test { int a; int b; boolean c; public void testFunction() { if(a-b<1) {} } }");
		runtest("module Test { int a; int b; boolean c; public void testFunction() { while(c == true) {} } }");
		runtest("module Test { int a; int b; boolean c; public void testFunction() { while(c == true) {break;} } }");
		runtest("module Test { int a; int b; boolean c; public int testFunction() { if(c == true) {return;} } }");
		runtest("module Test { int a; int b; boolean c; public boolean testFunction() { a == b; } }");
	}
}


