package dacd.gil.control;

import dacd.gil.control.exception.StoreException;

public interface Subscriber {
    void start() throws StoreException;
}
