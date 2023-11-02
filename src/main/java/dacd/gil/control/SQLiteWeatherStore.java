package dacd.gil.control;

import dacd.gil.model.Location;
import dacd.gil.model.Weather;

import java.sql.*;
import java.time.Instant;

import static javax.management.remote.JMXConnectorFactory.connect;

public class SQLiteWeatherStore implements WeatherStore {

    public SQLiteWeatherStore() {
    }

    @Override
    public void save(Weather weather, Statement statement){
        try {
            deleteTable(statement, weather.location.name);
            createTable(statement, weather.location.name);
            insert(statement, weather);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTable(Statement statement, String tableName){
        try {
            String query = "DELETE FROM " + tableName;
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de excepciones o registro de errores si es necesario
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

}
