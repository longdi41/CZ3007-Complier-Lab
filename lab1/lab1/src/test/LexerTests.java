package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) {
		Lexer lexer = new Lexer(new StringReader(input));
		int i=0;
		Token actual=new Token(MODULE, 0, 0, ""), expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch(Error e) {
					if(expected != null)
						fail(e.getMessage());
					/* return; */
				}
			} while(!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/** Example unit test. */
	@Test
	public void testKWs() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false return while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 0, 13, "return"),
				new Token(WHILE, 0, 20, "while"),
				new Token(EOF, 0, 25, ""));
	}
	
	@Test
	public void testKWs2() {
		runtest("module false\n return while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 1, 1, "return"),
				new Token(WHILE, 1, 8, "while"),
				new Token(EOF, 1, 13, ""));
	}

	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				new Token(STRING_LITERAL, 0, 0, ""),
				(Token)null,
				new Token(EOF, 0, 3, ""));
	}

	@Test
	public void testStringLiteral() {
		runtest("\"\\n\"", 
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}
	
	@Test
	public void testKeywords() {
		runtest("boolean break else false if import int module public return true type void while",
				new Token(BOOLEAN, 0, 0, "boolean"),
				new Token(BREAK, 0, 8, "break"),
				new Token(ELSE, 0, 14, "else"),
				new Token(FALSE, 0, 19, "false"),
				new Token(IF, 0, 25, "if"),
				new Token(IMPORT, 0, 28, "import"),
				new Token(INT, 0, 35, "int"),
				new Token(MODULE, 0, 39, "module"),
				new Token(PUBLIC, 0, 46, "public"),
				new Token(RETURN, 0, 53, "return"),
				new Token(TRUE, 0, 60, "true"),
				new Token(TYPE, 0, 65, "type"),
				new Token(VOID, 0, 70, "void"),
				new Token(WHILE, 0, 75, "while"),
				new Token(EOF, 0, 80, ""));
	}
	
	@Test
	public void testPunctuations() {
		runtest(", [ ] { } ( ) ;",
				new Token(COMMA, 0, 0, ","),
				new Token(LBRACKET, 0, 2, "["),
				new Token(RBRACKET, 0, 4, "]"),
				new Token(LCURLY, 0, 6, "{"),
				new Token(RCURLY, 0, 8, "}"),
				new Token(LPAREN, 0, 10, "("),
				new Token(RPAREN, 0, 12, ")"),
				new Token(SEMICOLON, 0, 14, ";"),
				new Token(EOF, 0, 15, ""));
	}
	
	@Test
	public void testOperators() {
		runtest("/ == = >= > <= < - != + *",
				new Token(DIV, 0, 0, "/"),
				new Token(EQEQ, 0, 2, "=="),
				new Token(EQL, 0, 5, "="),
				new Token(GEQ, 0, 7, ">="),
				new Token(GT, 0, 10, ">"),
				new Token(LEQ, 0, 12, "<="),
				new Token(LT, 0, 15, "<"),
				new Token(MINUS, 0, 17, "-"),
				new Token(NEQ, 0, 19, "!="),
				new Token(PLUS, 0, 22, "+"),
				new Token(TIMES, 0, 24, "*"),
				new Token(EOF, 0, 25, ""));
	}
	
	@Test
	public void testIdentifier() {
		runtest("aAa3",
				new Token(ID, 0, 0, "aAa3"),
				new Token(EOF, 0, 4, ""));
	}

	@Test
	public void testStringLiteralN() {
		runtest("\"n\"",
				new Token(STRING_LITERAL, 0, 0, "n"),
				new Token(EOF, 0, 3, ""));
	}
	
	@Test
	public void mixture1() {
		runtest("if i == j \n \t z = 0 \n else z = 1",
				new Token(IF, 0, 0, "if"),
				new Token(ID, 0, 3, "i"),
				new Token(EQEQ, 0, 5, "=="),
				new Token(ID, 0, 8, "j"),
				new Token(ID, 1, 3, "z"),
				new Token(EQL, 1, 5, "="),
				new Token(INT_LITERAL, 1, 7, "0"),
				new Token(ELSE, 2, 1, "else"),
				new Token(ID, 2, 6, "z"),
				new Token(EQL, 2, 8, "="),
				new Token(INT_LITERAL, 2, 10, "1"),
				new Token(EOF, 2, 11, ""));
	}
	
	@Test
	public void mixture2() {
		runtest("if x > y \n z = 1;",
				new Token(IF, 0, 0, "if"),
				new Token(ID, 0, 3, "x"),
				new Token(GT, 0, 5, ">"),
				new Token(ID, 0, 7, "y"),
				new Token(ID, 1, 1, "z"),
				new Token(EQL, 1, 3, "="),
				new Token(INT_LITERAL, 1, 5, "1"),
				new Token(SEMICOLON, 1, 6, ";"),
				new Token(EOF, 1, 7, ""));
	}
	
	@Test
	public void mixture3() {
		runtest("if x > y \n \t z = \"hello\" \n else \n \t z = \"bye\";",
				new Token(IF, 0, 0, "if"),
				new Token(ID, 0, 3, "x"),
				new Token(GT, 0, 5, ">"),
				new Token(ID, 0, 7, "y"),
				new Token(ID, 1, 3, "z"),
				new Token(EQL, 1, 5, "="),
				new Token(STRING_LITERAL, 1, 7, "hello"),
				new Token(ELSE, 2, 1, "else"),
				new Token(ID, 3, 3, "z"),
				new Token(EQL, 3, 5, "="),
				new Token(STRING_LITERAL, 3, 7, "bye"),
				new Token(SEMICOLON, 3, 12, ";"),
				new Token(EOF, 3, 13, ""));
	}
	
}
