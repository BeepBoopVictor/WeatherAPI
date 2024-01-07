package dacd.gil.control;

import dacd.gil.control.exception.StoreException;
import jakarta.jms.JMSException;

public interface Subscriber {
    void start() throws StoreException;
}
