package dacd.gil.control;

import dacd.gil.control.exception.StoreException;

public interface Listener {
    void consume(String weatherJson);
}