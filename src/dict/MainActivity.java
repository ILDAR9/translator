package dict;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dict.translators.Translator;
import dict.translators.TranslatorWeb;
import dict.translators.cache.TranslatorCache;

import java.io.IOException;

public class MainActivity {
	private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

	public static void main(String... args) {
		Scanner scanner = new Scanner(System.in);
		String en;
		// initializing Translator's objects
		TranslatorCache dictCache = new TranslatorCache("translates.txt");
		// preparing for bridge pattern
		Translator[] translators = { dictCache, new TranslatorWeb(dictCache) };
		while (true) {
			System.out.println(Messages.commands);
			en = scanner.nextLine();
			if (en == null)
				continue;
			switch (getAction(en)) {
			case -1:
				System.out.println(Messages.sayGoodbye);
				logger.info("exit");
				try {
					// Closing cache stream
					dictCache.close();
				} catch (IOException ex) {
					logger.error("{} {} close Output Stream for translates file-BD", ex.getClass(),
							Messages.logCaughtException, ex);
				}
				return;
			case 0:
				System.out.println(Messages.insertEmpty);
				logger.info("empty line inserted");
				break;
			case 1:
				String rus = null;
				for (Translator translaor : translators) {
					if (rus != null)
						break;
					rus = translaor.translate(en);
				}
				if (rus == null || rus.equals(en) || rus.isEmpty()) {
					System.out.println(Messages.notExists + en);
					logger.info("NO translate for {}", en);
				} else {
					System.out.println(Messages.translate + rus);
					logger.info("Translated {}: {}", en, rus);
				}
				break;
			case 2:
				System.out.println(Messages.digitInWord + en);
				logger.info("Inserted word contains digit: {}", en);
				break;
			default:
				System.out.println(Messages.onlyEnglish);
				logger.info("Expected english word instead of: {}", en);
			}
		}
	}

	private static Pattern patValidEnWord = Pattern.compile("[a-zA-Z]+[-]?[a-zA-Z]+");

	private static int getAction(String word) {
		if (word == null || word.isEmpty())
			return 0;
		if ("exit".equals(word))
			return -1;
		if (word.matches(".*\\d.*"))
			return 2;
		if (patValidEnWord.matcher(word).matches())
			return 1;
		return 404;
	}
}
