package dict.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dict.exceptions.language.LanguageSyntaxException;
import dict.exceptions.language.NotEnglishWordException;
import dict.exceptions.language.NotRussianWordException;
import dict.translators.TranslatorCache;

@RunWith(Parameterized.class)
public class TranslatorCacheTest {
	final static File testFile = new File("test.txt");
	static TranslatorCache trans;
	private String pExpected;
	private String pEn;

	@BeforeClass
	public static void initialFile() throws Exception {
		List<String> words = new LinkedList<>();
		words.add("dog=собака");
		words.add("computer=компьютер");
		words.add("water=вода");
		words.add("tube=труба");
		try (PrintWriter writer = new PrintWriter(testFile)) {
			for (String word : words)
				writer.println(word);
		}
		trans = new TranslatorCache(testFile.toString());
	}

	@Parameters
	public static Collection Prepare() {
		return Arrays.asList(new Object[][] { { "собака", "dog" },
				{  "компьютер", "computer" }, {"вода", "water" },
				{  "труба", "tube" } });
	}

	// start param test
	public TranslatorCacheTest(String pExpected, String pEn) {
		this.pExpected = pExpected;
		this.pEn = pEn;
	}

	@Test
	public void testTranslateParam() throws Exception {
		assertEquals(pExpected, trans.translate(pEn));
	}
	// end param test

	// checking for right LanguageSyntaxException
	@Test(expected = NotRussianWordException.class)
	public void testRussianAdd() throws LanguageSyntaxException {
		trans.addWord("home", "house");
	}

	// checking for right LanguageSyntaxException
	@Test(expected = NotEnglishWordException.class)
	public void testEnglishAdd() throws LanguageSyntaxException {
		trans.addWord("дом", "домик");
	}

	@AfterClass
	public static void deleteTest() throws IOException {
		trans.close();
		if (testFile.exists()) {
			testFile.delete();
		}
	}
}
