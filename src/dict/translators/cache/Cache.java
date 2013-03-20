package dict.translators.cache;

import java.util.regex.Pattern;

import dict.exceptions.language.LanguageSyntaxException;

public interface Cache {
	static final Pattern russianWord = Pattern.compile("[а-яА-Я]+[-]?+[а-яА-Я]+");
	static final Pattern englishWord = Pattern.compile("[a-zA-Z]+[-]?+[a-zA-Z]+");
	void addWord(String en, String rus)throws LanguageSyntaxException ;
}
