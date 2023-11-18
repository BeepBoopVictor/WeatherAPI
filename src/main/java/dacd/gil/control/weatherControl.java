package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class weatherControl {
    private final List<Location> locations;
    private final OpenWeatherMapProvider openWeatherMapProvider;
    private final SQLiteWeatherStore sqLiteWeatherStore;

    public weatherControl(OpenWeatherMapProvider openWeatherMapProvider, SQLiteWeatherStore sqLiteWeatherStore) {
        this.locations = List.of(new Location[]{
                new Location("GranCanaria", 28.1, -15.41),
                new Location("Tenerife", 28.46, -16.25),
                new Location("Fuerteventura", 28.2, -14.00),
                new Location("Lanzarote", 28.95, -13.76),
                new Location("LaPalma", 28.71, -17.9),
                new Location("ElHierro", 27.75, -18),
                new Location("LaGomera", 28.1, -17.11),
                new Location("LaGraciosa", 28.05, -15.44),
        });
        this.openWeatherMapProvider = openWeatherMapProvider;
        this.sqLiteWeatherStore = sqLiteWeatherStore;
    }

    public void execute(String dbPath){
        ArrayList<Weather> weathers;
        Statement statement = connecting(dbPath);
        Instant actualInstant = Instant.now();

        for(Location location: locations){
            weathers = this.openWeatherMapProvider.weatherGet(location, actualInstant);
            for (Weather weather: weathers){
                    this.sqLiteWeatherStore.save(weather, statement);
            }
        }
    }

    private Statement connecting(String contentRoot) {
        try {
            Connection connection = connect(contentRoot);
            return connection.createStatement();
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    public static Connection connect(String dbPath) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {System.out.println(e.getMessage());}
        return null;
    }
}
