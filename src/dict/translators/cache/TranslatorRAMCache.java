package dict.translators.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dict.exceptions.language.LanguageSyntaxException;
import dict.exceptions.language.NotEnglishWordException;
import dict.exceptions.language.NotRussianWordException;
import dict.translators.Translator;

public class TranslatorRAMCache implements Translator, Cache {
	private Map<String, String> dict;
	private static final Logger logger = LoggerFactory.getLogger(TranslatorCache.class);
	private final TranslatorCache cache;
	
	public TranslatorRAMCache(TranslatorCache cache) {
		this.cache = cache;
		dict = new HashMap<>();
	}
	public TranslatorRAMCache(){
		this(null);
	}
	
	//Insert new word to cache
	public void addWord(String en, String rus)throws LanguageSyntaxException {
		Matcher wordMatcher = russianWord.matcher(rus);
		if (!wordMatcher.matches()) {
			throw new NotRussianWordException();
		}
		wordMatcher = englishWord.matcher(en);
		if (!wordMatcher.matches()) {
			throw new NotEnglishWordException();
		}
		dict.put(en,rus);
	}
	
	
	public String translate(String en) {
		if (!dict.containsKey(en)) {
			logger.debug("RAMCache doesn't cointain translate for word: {}", en);
			return null;
		}
		return dict.get(en);
	}
	public void backUp(){
		cache.loadNewWords(dict);
	}
}
