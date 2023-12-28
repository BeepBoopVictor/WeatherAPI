package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

import java.util.ArrayList;

public interface Subscriber {
    void start(storeInterface storeInterface) throws CustomException;
}
