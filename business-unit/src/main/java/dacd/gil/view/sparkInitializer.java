package dacd.gil.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dacd.gil.view.ViewModel.HotelViewModel;
import dacd.gil.view.databaseConnection.SQLiteConnector;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class sparkInitializer {
    private static Map<String, List<Double>> mapOfVariables = new HashMap<>();
    private static String dbPath;

    public sparkInitializer(String dbPath) {
        this.dbPath = dbPath;
    }

    public void init() {
        mapOfVariables.put("WARM", List.of(25.0, 0.0, 60.0, 3.0, 0.80));
        mapOfVariables.put("COLD", List.of(10.0, 0.0, 60.0, 60.0, 0.80));
        mapOfVariables.put("SUNNY", List.of(25.0, 0.0, 60.0, 0.0, 0.80));
        mapOfVariables.put("SNOW", List.of(-2.5, 10.0, 80.0, 60.0, 0.80));
        Spark.port(8080);
        configureRoutes(this.dbPath);
    }

    private static void configureRoutes(String path){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Spark.get("/api/CHEAP", (req, res) -> {
            try (Connection connection = SQLiteConnector.connect(path)){
                List<HotelViewModel> hotelViewModelList = findCheapHotels(connection);
                return gson.toJson(hotelViewModelList);
            }
        });

        mapOfVariables.forEach(new BiConsumer<String, List<Double>>() {
            @Override
            public void accept(String s, List<Double> doubles) {
                Spark.get("/api/" + s, (req, res) -> {
                    try (Connection connection = SQLiteConnector.connect(path)) {
                        List<HotelViewModel> hotelViewModelList = findWeatherHotels(connection, doubles);

                        return gson.toJson(hotelViewModelList);
                    }
                });
            }
        });
    }

    private static List<HotelViewModel> findCheapHotels(Connection connection) throws SQLException {
        List<HotelViewModel> cheapHotels = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            String query = "SELECT * FROM " + tableName + " WHERE PriceStatus = 'cheap'";

            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    HotelViewModel hotelViewModel = new HotelViewModel(
                            tableName,
                            resultSet.getString("HotelKey"),
                            resultSet.getString("PriceStatus"),
                            resultSet.getString("Date"),
                            resultSet.getDouble("Temperature"),
                            resultSet.getInt("Humidity"),
                            resultSet.getDouble("Rainfall"),
                            resultSet.getDouble("WindSpeed"),
                            resultSet.getDouble("Clouds")
                    );
                    cheapHotels.add(hotelViewModel);
                }
            }
        }
        return cheapHotels;
    }

    private static List<HotelViewModel> findWeatherHotels(Connection connection, List<Double> doubles) throws SQLException {
        List<HotelViewModel> weatherHotels = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            String query = "SELECT * FROM " + tableName +
                    " WHERE (Temperature BETWEEN " + (doubles.get(0) - 5.0) + " AND " + (doubles.get(0) + 5.0) + ")" +
                    " AND (Rainfall BETWEEN " + (doubles.get(1) - 10.0) + " AND " + (doubles.get(1) + 10.0) + ")" +
                    " AND (Humidity BETWEEN " + (doubles.get(2) - 35.0) + " AND " + (doubles.get(2) + 35.0) + ")" +
                    " AND (Clouds BETWEEN " + (doubles.get(3) - 20.0) + " AND " + (doubles.get(3) + 20.0) + ")";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    HotelViewModel hotelViewModel = new HotelViewModel(
                            tableName,
                            resultSet.getString("HotelKey"),
                            resultSet.getString("PriceStatus"),
                            resultSet.getString("Date"),
                            resultSet.getDouble("Temperature"),
                            resultSet.getInt("Humidity"),
                            resultSet.getDouble("Rainfall"),
                            resultSet.getDouble("WindSpeed"),
                            resultSet.getDouble("Clouds")
                    );
                    weatherHotels.add(hotelViewModel);
                }
            }
        }
        return weatherHotels;
    }
}
