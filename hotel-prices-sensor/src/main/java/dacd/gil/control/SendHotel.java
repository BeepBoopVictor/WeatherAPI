package dacd.gil.control;

import dacd.gil.control.exception.CustomException;

public interface SendHotel {
    void sendHotel(String hotelJSON) throws CustomException;
}
