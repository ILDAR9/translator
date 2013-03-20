package dict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dict.exceptions.language.LanguageSyntaxException;
import dict.translators.Translator;
import dict.translators.TranslatorThread;
import dict.translators.TranslatorWeb;
import dict.translators.cache.TranslatorCache;
import dict.translators.cache.TranslatorRAMCache;

public class MainActivity {
	private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

	public static void main(String... args) {
		try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
			String en;
			// initializing Translator's objects
			TranslatorCache dictCache = new TranslatorCache("translates.txt");
			TranslatorRAMCache dictRAMCache = new TranslatorRAMCache(dictCache);
			TranslatorWeb dictWeb = new TranslatorWeb(dictRAMCache);
			while (true) {
				System.out.println(Messages.commands);
				en = console.readLine();
				if (en == null)
					continue;
				switch (getAction(en)) {
				case -1:
					// saving all new words from RAM to cache file
					dictRAMCache.backUp();

					System.out.println(Messages.sayGoodbye);
					logger.info("exit");
					return;
				case 0:
					System.out.println(Messages.insertEmpty);
					logger.info("empty line inserted");
					break;
				case 1:
					String rus = null;
					// searching translation in RAM(contains session words)
					rus = dictRAMCache.translate(en);
					
					/*
					 * if RAM doesn't contain translation then we will get it
					 * from file cache ant web
					 */
					if (rus == null) {
						// creating thread
						TranslatorThread threadWeb = new TranslatorThread(dictWeb, en);
						TranslatorThread threadCache = new TranslatorThread(dictCache, en);
						threadCache.start();
						threadWeb.start();
						try {
							threadCache.join();
							rus = threadCache.getTranslate();
						} catch (InterruptedException ex) {
							logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException,
									"join thread of cache to main thread", ex);
						}
						// if cache thread didn't get translation then we will
						// wait for web translation						
						if ( rus== null) {
							try {
								threadWeb.join();
								rus = threadWeb.getTranslate();
							} catch (InterruptedException ex) {
								logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException,
										"join thread of web translator to main thread", ex);
							}
						}
					}
					if (rus == null || rus.equals(en) || rus.isEmpty()) {
						System.out.println(Messages.notExists + en);
						logger.info("NO translate for {}", en);
					} else {
						System.out.println(Messages.translate + rus);
						
						//add new word to RAM
						dictRAMCache.addWord(en, rus);
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
		} catch (IOException | LanguageSyntaxException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, "read line from console", ex);
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
