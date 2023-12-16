package dacd.gil.control;

import dacd.gil.control.exception.CustomException;
import dacd.gil.model.Hotel;

public interface SendHotel {
    void sendHotel(String hotelJSON) throws CustomException;
}
