package dict.exceptions.language;

import dict.exceptions.ContainsDigitException;

public class NotEnglishWordException extends LanguageSyntaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5705826579502489693L;

	public NotEnglishWordException() {
		super();
	}

	public NotEnglishWordException(String message) {
		super(message);
	}

	public NotEnglishWordException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnglishWordException(Throwable cause) {
		super(cause);
	}

	public NotEnglishWordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String toString() {
		return ContainsDigitException.class.getName();
	}
}
