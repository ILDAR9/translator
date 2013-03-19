package dict.translators.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dict.Messages;
import dict.exceptions.ContainsDigitException;
import dict.exceptions.DictionaryException;
import dict.exceptions.EmptyLineException;
import dict.exceptions.InvalidFormatException;
import dict.exceptions.language.LanguageSyntaxException;
import dict.exceptions.language.NotEnglishWordException;
import dict.exceptions.language.NotRussianWordException;
import dict.translators.Translator;

public class TranslatorCache implements Translator {
	private Map<String, String> dict = new HashMap<>();
	private PrintWriter printer;
	private String fileName;
	private Pattern russianWord = Pattern.compile("[а-яА-Я]+[-]?+[а-яА-Я]+");
	private Pattern englishWord = Pattern.compile("[a-zA-Z]+[-]?+[a-zA-Z]+");
	private static final Logger logger = LoggerFactory.getLogger(TranslatorCache.class);
	
	//Simple constructor creates new cache-file
	public TranslatorCache() {
		this("dictionary.txt");
	}

	/*
	 * Constructor returns object with injected cache-file.
	 *  Before instantiating, cache file is checking and loading
	 *  into RAM, and cache-file can be filled out with new data. 
	 */
	public TranslatorCache(String fileName) {
		try {
			init(fileName);
		} catch (EmptyLineException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, Messages.logWhileInitFile,
					fileName, ex);
		} catch (InvalidFormatException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, Messages.logWhileInitFile,
					fileName, ex);
		} catch (ContainsDigitException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, Messages.logWhileInitFile,
					fileName, ex);
		} catch (DictionaryException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, Messages.logWhileInitFile,
					fileName, ex);
		}
	}
	//loading cache to random access memory 
	private void init(String fileName) throws DictionaryException {
		this.fileName = fileName;
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
				logger.debug("File {} is created", fileName);
			} catch (IOException ex) {
				logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, "to create file:", fileName, ex);
			}
			return;
		}
		try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
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
				dict.put(matcher.group(1), matcher.group(2));
			}
		} catch (FileNotFoundException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, "read file:", fileName, ex);
		} catch (IOException ex) {
			logger.error("{} {} {} {}", ex.getClass(), Messages.logCaughtException, "read file:", fileName, ex);
		}
	}
	
	//Insert new word to cache
	public void addWord(String en, String rus) throws LanguageSyntaxException {
		Matcher wordMatcher = russianWord.matcher(rus);
		if (!wordMatcher.matches()) {
			throw new NotRussianWordException();
		}
		wordMatcher = englishWord.matcher(en);
		if (!wordMatcher.matches()) {
			throw new NotEnglishWordException();
		}
		if (printer == null) {
			try {
				printer = new PrintWriter(new FileWriter(fileName, true));
			} catch (IOException ex) {
				logger.error("{} {} add translate({}={}) to file: {}", ex.getClass(), Messages.logCaughtException, en,
						rus, fileName, ex);
			}
		}
		printer.println(String.format("%s=%s", en, rus));
		dict.put(en, rus);
	}

	public String translate(String en) {
		if (!dict.containsKey(en)) {
			logger.debug("Cache doesn't cointain translate for word: {}", en);
			return null;
		}
		return dict.get(en);
	}

	public int getWordCount() {
		return dict.size();
	}

	public void close() throws IOException {
		if (printer != null) {
			printer.close();
			logger.debug("InputStream to File: {} is closed",fileName);
		}
	}

}
