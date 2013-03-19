package dict.exceptions.language;


public class NotRussianWordException extends LanguageSyntaxException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1853051700247477244L;

	public NotRussianWordException() {
		super();
	}

	public NotRussianWordException(String message) {
		super(message);
	}

	public NotRussianWordException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotRussianWordException(Throwable cause) {
		super(cause);
	}

	public NotRussianWordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String toString() {
		return NotRussianWordException.class.getName();
	}
}
