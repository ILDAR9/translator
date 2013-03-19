package dict.exceptions;

public class DictionaryException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8707929193662492077L;

	public DictionaryException() {
        super();
    }

    public DictionaryException(String message) {
        super(message);
    }

    public DictionaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DictionaryException(Throwable cause) {
        super(cause);
    }

    public DictionaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return DictionaryException.class.getName();
    }
}
