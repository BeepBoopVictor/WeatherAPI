package dacd.gil.view.ViewModel;

public class HotelViewModel {
    private String location;
    private String hotelKey;
    private String priceStatus;
    private String day;
    private double temp;
    private int humidity;
    private double rain;
    private double windSpeed;
    private double clouds;

    public HotelViewModel(String location, String hotelKey, String priceStatus, String day, double temp, int humidity, double rain, double windSpeed, double clouds) {
        this.location = location;
        this.hotelKey = hotelKey;
        this.priceStatus = priceStatus;
        this.day = day;
        this.temp = temp;
        this.humidity = humidity;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.clouds = clouds;
    }
}
