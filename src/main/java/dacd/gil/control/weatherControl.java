package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class weatherControl {
    public final List<Location> locations;

    public weatherControl() {
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
    }

    public void execute(){
        Instant instant = Instant.now();
        OpenWeatherMapProvider openWeatherMapProvider = new OpenWeatherMapProvider(getAPIKEY("C:/Users/Usuario/Desktop/ULPGC/GRADO/SEGUNDO/DACD/EJERCICIOS/apiKEY.txt"));
        SQLiteWeatherStore sqLiteWeatherStore = new SQLiteWeatherStore();
        ArrayList<Weather> weathers;
        Statement statement = connecting("dacd/gil/stuff/database.db");
        Instant actualInstant = Instant.now();

        int count;

        for(Location location: locations){
            System.out.println(location);
            weathers = openWeatherMapProvider.weatherGet(location, actualInstant);
            System.out.println(weathers.size());
            count = 0;
            for (Weather weather: weathers){
                    sqLiteWeatherStore.save(weather, statement, count);
                    count++;
            }
        }
    }

    private String getAPIKEY(String fileRoot){
        StringBuilder contenido = new StringBuilder();

        try {
            FileReader fileReader = new FileReader(fileRoot);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contenido.toString();
    }

    private Statement connecting(String contentRoot) {
        try {
            Connection connection = connect(contentRoot);
            Statement statement = connection.createStatement();
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection connect(String dbPath) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/java/" + dbPath);
            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
