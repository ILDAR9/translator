package dict.exceptions.language;

import dict.exceptions.ContainsDigitException;
import dict.exceptions.DictionaryException;

public class LanguageSyntaxException extends DictionaryException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4690656540733301721L;

	public LanguageSyntaxException() {
		super();
	}

	public LanguageSyntaxException(String message) {
		super(message);
	}

	public LanguageSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public LanguageSyntaxException(Throwable cause) {
		super(cause);
	}

	public LanguageSyntaxException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String toString() {
		return ContainsDigitException.class.getName();
	}
}
