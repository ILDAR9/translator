package dict.exceptions;


public class InvalidFormatException extends DictionaryException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3769875028338120945L;

	public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFormatException() {
        super() ;
    }

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return InvalidFormatException.class.getName();
    }
}
