package dict.translators.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dict.Messages;
import dict.exceptions.ContainsDigitException;
import dict.exceptions.EmptyLineException;
import dict.exceptions.InvalidFormatException;
import dict.exceptions.language.LanguageSyntaxException;
import dict.exceptions.language.NotEnglishWordException;
import dict.exceptions.language.NotRussianWordException;
import dict.translators.Translator;

public class TranslatorCache implements Translator, Cache {
	private final File fileCache;

	private static final Logger logger = LoggerFactory.getLogger(TranslatorCache.class);

	// Simple constructor creates new cache-file
	public TranslatorCache() {
		this("dictionary.txt");
	}

	// Constructor for current cache file
	public TranslatorCache(String fileName) {
		File fileCache = new File(fileName);
		if (!fileCache.exists()) {
			try {
				fileCache.createNewFile();
				logger.debug("File {} is created", fileName);
			} catch (IOException ex) {
				logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, "to create file:", fileName, ex);
			}
		}
		this.fileCache = fileCache;
	}

	// Insert new word to cacheFile
	public void addWord(String en, String rus) throws LanguageSyntaxException {
		Matcher wordMatcher = russianWord.matcher(rus);
		if (!wordMatcher.matches()) {
			throw new NotRussianWordException();
		}
		wordMatcher = englishWord.matcher(en);
		if (!wordMatcher.matches()) {
			throw new NotEnglishWordException();
		}
		try (PrintWriter printer = new PrintWriter(new FileWriter(fileCache, true))) {
			printer.println(String.format("%s=%s", en, rus));
		} catch (IOException ex) {
			logger.error("{} {} add translate({}={}) to file: {}", ex.getClass(), Messages.logCaughtException, en, rus,
					fileCache, ex);
		}
	}

	public String translate(String en) {
		try (BufferedReader bf = new BufferedReader(new FileReader(fileCache))) {
			String line;
			Pattern format = Pattern.compile("([^=]*)=([^=]*)");
			String nonDigit = "[\\D]+";
			Matcher matcher;
			for (int i = 1; (line = bf.readLine()) != null; i++) {
				if (line.isEmpty()) {
					throw new EmptyLineException("empty line: " + i);
				}
				matcher = format.matcher(line); // check for valid content
												// format
				if (!matcher.matches()) {
					throw new InvalidFormatException("not valid format at line: " + i);
				}
				if (!matcher.group(1).matches(nonDigit)) {
					throw new ContainsDigitException("Contains digit in word " + matcher.group(1));
				}
				if (!matcher.group(2).matches(nonDigit)) {
					throw new ContainsDigitException("Contains digit in word " + matcher.group(2));
				}
				if (matcher.group(1).equals(en))
					return matcher.group(2);
			}
		} catch (FileNotFoundException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, "read file:", fileCache, ex);
		} catch (IOException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, "read file:", fileCache, ex);
		} catch (EmptyLineException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, Messages.logWhileInitFile,
					fileCache, ex);
		} catch (InvalidFormatException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, Messages.logWhileInitFile,
					fileCache, ex);
		} catch (ContainsDigitException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, Messages.logWhileInitFile,
					fileCache, ex);
		}
		logger.debug("CacheFile doesn't cointain translate for word: {}", en);
		return null;
	}

	public void loadNewWords(Map<String, String> ramCache) {
		try (PrintWriter printer = new PrintWriter(new FileWriter(fileCache, true))) {
			for (Entry<String,String> dictEntry : ramCache.entrySet())
				printer.println(String.format("%s=%s", dictEntry.getKey(), dictEntry.getValue()));			
		} catch (IOException ex) {
			logger.error("{} {} buckup new words from RAM to file: {}", ex.getClass(), Messages.logCaughtException, fileCache);
		}
	}
}
