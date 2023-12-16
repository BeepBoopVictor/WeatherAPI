package dacd.gil.model;

public class hotelValues {
    private String hotelToken;
    private String location;

    public hotelValues(String hotelToken, String location) {
        this.hotelToken = hotelToken;
        this.location = location;
    }

    public String getHotelToken() {
        return hotelToken;
    }

    public String getLocation() {
        return location;
    }
}
