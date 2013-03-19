package dict.exceptions;


public class NotFoundException extends DictionaryException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2844916100250485532L;

	public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException() {
        super() ;
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return InvalidFormatException.class.getName();
    }
}
