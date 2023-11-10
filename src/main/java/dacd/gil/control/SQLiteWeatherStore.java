package dacd.gil.control;

import dacd.gil.model.Weather;

import java.sql.*;

public class SQLiteWeatherStore implements WeatherStore {
    private int count = 0;

    public SQLiteWeatherStore() {
    }

    @Override
    public void save(Weather weather, Statement statement){
        try {
            createTable(statement, weather.location.name);
            if(getRecordCount(weather.location.name, statement) < 5){
                insert(statement, weather);
            } else {
                count++;
                System.out.println(count);
                if (count < 5){
                    updateValue(statement, weather);
                } else {
                    insert(statement, weather);
                    count = 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getRecordCount(String tableName, Statement statement) {
        int recordCount = -1; // Valor predeterminado en caso de error
        try{
            String query = "SELECT COUNT(*) FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                recordCount = resultSet.getInt(1); // El resultado se encuentra en la primera columna
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordCount;
    }


    public void updateValue(Statement statement, Weather weather){
        String query = "UPDATE " + weather.location.name + " SET Temperature = ?, Rainfall = ?, Humidity = ?, Clouds = ?, WindSpeed = ? WHERE Date = ?";
        try (PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query)) {
            preparedStatement.setDouble(2, weather.temp);
            preparedStatement.setDouble(3, weather.rain);
            preparedStatement.setInt(4, weather.humidity);
            preparedStatement.setDouble(5, weather.clouds);
            preparedStatement.setDouble(6, weather.windSpeed);
            preparedStatement.setString(1, weather.ts.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    /*public Optional<Weather> loadWeather(Location location, Instant instant) {
        // Me lo traigo para modificarlo --> lo utilizo para actualizar
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            String tableName = location.name;

            // Crear la tabla si no existe
            createTableIfNotExists(connection, tableName);

            String selectDataSQL = "SELECT * FROM " + tableName + " WHERE date = ?"; // Selecciono todos los campos de la base de datos

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectDataSQL)) {
                preparedStatement.setObject(1, instant); // Cambia la columna fecha por el instante actual
                ResultSet resultSet = preparedStatement.executeQuery(); // Datos de la base de datos

                if (resultSet.next()) {
                    double temperature = resultSet.getDouble("temperature");
                    double rainfall = resultSet.getDouble("rain");
                    int humidity = resultSet.getInt("humidity");
                    int clouds = resultSet.getInt("clouds");
                    double windSpeed = resultSet.getDouble("wind_speed");

                    Weather weather = new Weather(temperature, humidity, clouds, windSpeed, rainfall, location, instant);
                    return Optional.of(weather);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }*/
}
