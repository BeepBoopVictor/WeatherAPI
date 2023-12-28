package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

public interface Manager {
    void manageEvents(String jsonString) throws CustomException;
}
