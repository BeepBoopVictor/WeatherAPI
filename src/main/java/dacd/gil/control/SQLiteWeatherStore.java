package dacd.gil.control;

import dacd.gil.control.WeatherStore;
import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.sql.*;
import java.time.Instant;

import static javax.management.remote.JMXConnectorFactory.connect;

public class SQLiteWeatherStore implements WeatherStore {

    public SQLiteWeatherStore() {
    }

    @Override
    public void save(Weather weather){
        Statement statement = connecting("dacd/gil/stuff/database.db");
        try {
            createTable(statement, weather.location.name);
            insert(statement, weather);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(Statement statement, Weather weather) {
        String insertQuery = "INSERT INTO " + weather.location.name + " VALUES (?, ?, ?, ?, ?, ?)";

        // Preparar la consulta
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, weather.ts.toString());
            preparedStatement.setDouble(2, weather.temp);
            preparedStatement.setDouble(3, weather.rain);
            preparedStatement.setInt(4, weather.humidity);
            preparedStatement.setDouble(5, weather.clouds);
            preparedStatement.setDouble(6, weather.windSpeed);

            // Ejecutar la consulta preparada
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void createTable(Statement statement, String name) throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS " + name + " (" +
                "Date TEXT," +
                "Temperature REAL," +
                "Rainfall REAL," +
                "Humidity REAL," +
                "Clouds REAL," +
                "WindSpeed REAL" +
                ");");
    }

    @Override
    public void load(Location location, Instant instant) {
        //TODO
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
            //String url = "jdbc:sqlite:" + dbPath;
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/java/dacd/gil/stuff/database.db");
            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
