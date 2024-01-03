package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

public interface Subscriber {
    void start(storeInterface storeInterface) throws CustomException;
}
