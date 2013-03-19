package dict.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dict.translators.TranslatorWeb;

@RunWith(Parameterized.class)
public class TranslatorWebTest {
	static TranslatorWeb trans;
	private String pExpected;
	private String pEn;

	@BeforeClass
	public static void initialFile() throws Exception {		
		trans = new TranslatorWeb();
	}
	
	@Parameters
	public static Collection Prepare() {
		return Arrays.asList(new Object[][] {
				{  "собака", "dog" },
				{  "компьютер", "computer" },
				{  "вода", "water" },
				{  "труба", "tube" }
				});
	}

	// start param test
	public TranslatorWebTest(String pExpected, String pEn) {
		this.pExpected = pExpected;
		this.pEn = pEn;
	}

	@Test
	public void testTranslateParam() throws Exception {
		assertEquals(pExpected, trans.translate(pEn));
	}
	// end param test
}
