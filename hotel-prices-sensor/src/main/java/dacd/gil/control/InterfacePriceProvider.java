package dacd.gil.control;

import dacd.gil.control.exception.CustomException;
import dacd.gil.model.Hotel;
import dacd.gil.model.hotelValues;

public interface InterfacePriceProvider {
    Hotel priceGet(hotelValues hotelValues, String checkOut) throws CustomException;
}
