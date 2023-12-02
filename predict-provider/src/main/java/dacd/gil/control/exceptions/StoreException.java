package dacd.gil.control.exceptions;

public class StoreException extends Exception {

    public StoreException(String errorMessage){
        super(errorMessage);
    }

    public StoreException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
