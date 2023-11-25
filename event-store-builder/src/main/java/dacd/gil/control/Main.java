package dacd.gil.control;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Listener(), new SQLiteWeatherStore(args[0]));
        controller.execute();
    }
}
