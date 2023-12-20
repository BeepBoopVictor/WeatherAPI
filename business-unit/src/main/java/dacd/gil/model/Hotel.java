package dacd.gil.model;

import java.time.Instant;

public class Hotel {
    private String location;
    private String hotelKey;
    private String priceStatus;
    private Instant day;

    public Hotel(String location, String hotelKey, String priceStatus, Instant day) {
        this.location = location;
        this.hotelKey = hotelKey;
        this.priceStatus = priceStatus;
        this.day = day;
    }

    public String getLocation() {
        return location;
    }

    public String getHotelKey() {
        return hotelKey;
    }

    public String getPriceStatus() {
        return priceStatus;
    }

    public Instant getDay() {
        return day;
    }
}
