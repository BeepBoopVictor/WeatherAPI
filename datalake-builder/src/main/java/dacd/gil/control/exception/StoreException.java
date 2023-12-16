package dacd.gil.control.exception;

public class StoreException extends Exception {

    public StoreException(String errorMessage){
        super(errorMessage);
    }

    public StoreException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }

    public StoreException(Throwable cause) {
        super(cause);
    }
}
