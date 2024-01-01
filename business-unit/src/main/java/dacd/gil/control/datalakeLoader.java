package dacd.gil.control;

import dacd.gil.control.Exception.CustomException;

public interface datalakeLoader {
    void readEvents(String datalakePath, String topic) throws CustomException;
}
