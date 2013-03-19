package dict.exceptions;


public class EmptyLineException extends DictionaryException{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4323806913617532946L;

	public EmptyLineException() {
    }

    public EmptyLineException(String message) {
        super(message);
    }

    public EmptyLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyLineException(Throwable cause) {
        super(cause);
    }

    public EmptyLineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return EmptyLineException.class.getName();
    }
}
