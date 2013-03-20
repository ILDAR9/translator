package dict.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dict.exceptions.language.LanguageSyntaxException;
import dict.exceptions.language.NotEnglishWordException;
import dict.exceptions.language.NotRussianWordException;
import dict.translators.cache.TranslatorRAMCache;

@RunWith(Parameterized.class)
public class TranslatorRAMCacheTest {
	static TranslatorRAMCache trans;
	private String pExpected;
	private String pEn;

	@BeforeClass
	public static void initialFile() throws Exception {
		trans = new TranslatorRAMCache();
		trans.addWord("dog", "собака");
		trans.addWord("computer", "компьютер");
		trans.addWord("water", "вода");
		trans.addWord("tube", "труба");
	}

	@Parameters
	public static Collection Prepare() {
		return Arrays.asList(new Object[][] { { "собака", "dog" }, { "компьютер", "computer" }, { "вода", "water" },
				{ "труба", "tube" } });
	}

	// start param test
	public TranslatorRAMCacheTest(String pExpected, String pEn) {
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

}
