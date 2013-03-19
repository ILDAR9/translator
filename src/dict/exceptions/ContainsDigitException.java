package dict.exceptions;

public class ContainsDigitException extends DictionaryException {

	private static final long serialVersionUID = 8469016027795313402L;

	public ContainsDigitException() {
        super();
    }

    public ContainsDigitException(String message) {
        super(message);
    }

    public ContainsDigitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainsDigitException(Throwable cause) {
        super(cause);
    }

    public ContainsDigitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return ContainsDigitException.class.getName();
    }
}
