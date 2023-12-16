package dacd.gil.model;

import java.time.Instant;
import java.util.ArrayList;

public class Hotel {
    private hotelValues hotelValues;
    private ArrayList<String> averagePrices;
    private ArrayList<String> highPrices;
    private ArrayList<String> cheapPrices;
    private Instant ts;
    private String ss;


    public Hotel(hotelValues hotelValues, ArrayList<String> averagePrices, ArrayList<String> highPrices, ArrayList<String> cheapPrices, Instant ts) {
        this.hotelValues = hotelValues;
        this.averagePrices = averagePrices;
        this.highPrices = highPrices;
        this.cheapPrices = cheapPrices;
        this.ts = ts;
        this.ss = "hotel-provider";
    }


    public hotelValues getToken() {
        return hotelValues;
    }

    public ArrayList<String> getAveragePrices() {
        return averagePrices;
    }

    public ArrayList<String> getHighPrices() {
        return highPrices;
    }

    public ArrayList<String> getCheapPrices() {
        return cheapPrices;
    }

    public dacd.gil.model.hotelValues getHotelValues() {
        return hotelValues;
    }

    public Instant getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
