package br.com.caelum.tubaina.parser.html;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.caelum.tubaina.chunk.CodeChunk;
import br.com.caelum.tubaina.parser.html.desktop.SyntaxHighlighter;

public class HtmlAndKindleCodeTagTest {

	private String code;
	private List<Integer> emptyList;

	@Before
	public void setUp() {
		code = "public static void main(String[] args) {\n" + "    String name = \"Gabriel\";\n"
				+ "    System.out.println(\"Hello, \" + name);\n" + "}";
		emptyList = Collections.emptyList();
	}

	@Test
	public void plainJavaCode() throws Exception {
		String options = "java";
		SyntaxHighlighter htmlCodeHighlighter = mock(SyntaxHighlighter.class);
		HtmlAndKindleCodeTag codeTag = new HtmlAndKindleCodeTag(htmlCodeHighlighter);
		CodeChunk chunk = new CodeChunk(code, options);
		codeTag.parse(chunk);
		verify(htmlCodeHighlighter).highlight(eq(code), eq(options), eq(false), eq(emptyList), Mockito.anyString());
	}

	@Test
	public void javaCodeWithNumberedLines() throws Exception {
		String options = "java #";
		SyntaxHighlighter htmlCodeHighlighter = mock(SyntaxHighlighter.class);
		HtmlAndKindleCodeTag codeTag = new HtmlAndKindleCodeTag(htmlCodeHighlighter);
		CodeChunk chunk = new CodeChunk(code, options);
		codeTag.parse(chunk);
		verify(htmlCodeHighlighter).highlight(eq(code), eq("java"), eq(true), eq(emptyList), Mockito.anyString());
	}

	@Test
	public void plainRubyCode() throws Exception {
		String options = "ruby";
		String rubyCode = "@name = \"Gabriel\"\n" + "puts \"Hello, \" + name";
		SyntaxHighlighter htmlCodeHighlighter = mock(SyntaxHighlighter.class);
		HtmlAndKindleCodeTag codeTag = new HtmlAndKindleCodeTag(htmlCodeHighlighter);
		CodeChunk chunk = new CodeChunk(rubyCode, options);
		codeTag.parse(chunk);
		verify(htmlCodeHighlighter).highlight(eq(rubyCode), eq("ruby"), eq(false), eq(emptyList), Mockito.anyString());
	}

	@Test
	public void noLanguageDefinedIsTreatedAsText() throws Exception {
		String options = "";
		String noParticularLanguage = "Some text explaining some new bizarre\n" + "syntax in a very code alike way";
		SyntaxHighlighter htmlCodeHighlighter = mock(SyntaxHighlighter.class);
		HtmlAndKindleCodeTag codeTag = new HtmlAndKindleCodeTag(htmlCodeHighlighter);
		CodeChunk chunk = new CodeChunk(noParticularLanguage, options);
		codeTag.parse(chunk);
		verify(htmlCodeHighlighter).highlight(eq(noParticularLanguage), eq("text"), eq(false), eq(emptyList), Mockito.anyString());
	}

	@Test
	public void noLanguageDefinedIsTreatedAsTextEvenWhenItIsNumbered() throws Exception {
		String options = "#";
		String noParticularLanguage = "Some text explaining some new bizarre\n" + "syntax in a very code alike way";
		SyntaxHighlighter htmlCodeHighlighter = mock(SyntaxHighlighter.class);
		HtmlAndKindleCodeTag codeTag = new HtmlAndKindleCodeTag(htmlCodeHighlighter);
		CodeChunk chunk = new CodeChunk(noParticularLanguage, options);
		codeTag.parse(chunk);
		verify(htmlCodeHighlighter).highlight(eq(noParticularLanguage), eq("text"), eq(true), eq(emptyList), Mockito.anyString());
	}

	@Test
	public void shouldNotConsiderLabelAsLanguage() throws Exception {
		String options = "label=world";
		String noParticularLanguage = "Some code";
		SyntaxHighlighter htmlCodeHighlighter = mock(SyntaxHighlighter.class);
		HtmlAndKindleCodeTag codeTag = new HtmlAndKindleCodeTag(htmlCodeHighlighter);
		CodeChunk chunk = new CodeChunk(noParticularLanguage, options);
		codeTag.parse(chunk);
		verify(htmlCodeHighlighter).highlight(eq(noParticularLanguage), eq("text"), eq(false), eq(emptyList), Mockito.anyString());
	}

	@Test
	public void shouldConsiderLineHighlightOption() throws Exception {
		String options = "h=1,2";
		String noParticularLanguage = "Some code";
		SyntaxHighlighter htmlCodeHighlighter = mock(SyntaxHighlighter.class);
		HtmlAndKindleCodeTag codeTag = new HtmlAndKindleCodeTag(htmlCodeHighlighter);
		CodeChunk chunk = new CodeChunk(noParticularLanguage, options);
		codeTag.parse(chunk);
		verify(htmlCodeHighlighter).highlight(eq(noParticularLanguage), eq("text"), eq(false), eq(Arrays.asList(1, 2)), Mockito.anyString());
	}
	
	@Test
	public void shouldConsiderPygmentsOptions() throws Exception {
		String options = "label=somelabel options=\'startinline=true\'";
		String noParticularLanguage = "Some code";
		SyntaxHighlighter htmlCodeHighlighter = mock(SyntaxHighlighter.class);
		HtmlAndKindleCodeTag codeTag = new HtmlAndKindleCodeTag(htmlCodeHighlighter);
		CodeChunk chunk = new CodeChunk(noParticularLanguage, options);
		codeTag.parse(chunk);
		verify(htmlCodeHighlighter).highlight(eq(noParticularLanguage), eq("text"), eq(false), eq(emptyList), eq("startinline=true"));
	}

}
