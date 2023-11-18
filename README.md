# WeatherAPI

Subject: Desarrollo de aplicaciones de Ciencia de Datos
Grade: Second of Data science
School: Universidad de Las Palmas de Gran Canaria
Name: Víctor Gil Bernal

## Functionality:

This code conects to an OpenWeather API with a 5 day forecast, takes the values for the next five days and inserts it in a SQLITE database.

When activated starts working at 12.00 and every six hours updates the values or inserts the next one related to the next day.

## Resources:

The development enviroment used in this project is IntelliJ and the database is SQLITE.

 ## Requirements

 To use this code it is neccessary to insert to arguments to tha Main function, the first is the API key and the second one the direction to the database.

## Design:

The project has 2 model classes:
- Location: consists on the coordinates in latitude, longitude and the name of the region.
- Weather: temperature, rain probabilty, instant of the data, wind speed humidity, location and clouds

  Control models:
  - WeatherProvides: an interface with  a function that returns a list of five weathers.
  - OpenWeatherMapProvider: has two attributes, the apiKEY and the base URL, the function implemented from WeatherProvides takes two arguments, a location and an instant, connects to the database and reads the json, translating it into a Weather object and returning an array.
  - WeatherStore: an interface with two functions, saveWeather, that takes a Weather object and a Statement, and loadWeather that takes a Statement, a location and an instant.
  - SQLiteWeatherStore: implements WeatherStore, uses the functions saveWeather to connect to a database, and inserts or updates the weathers taken as arguments.
  - WeatherControl: Has a function called Execute that takes a direction to the database, it connects to OpenWeatherMapProvider to get an array of five weathers that inserts in the method saveWeather from SQLiteWeatherStore in a loop.
  - Main: starts working at 12.00 when activated, calls to the function Execute from WeatherControl every six hours.


![image](https://github.com/BeepBoopVictor/WeatherAPI/assets/145380029/ab3d4214-e820-4086-876b-7558611a6b23)
