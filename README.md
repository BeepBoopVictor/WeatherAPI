# WeatherAPI

Subject: Desarrollo de aplicaciones de Ciencia de Datos
Grade: Second of Data science
School: Universidad de Las Palmas de Gran Canaria
Name: Víctor Gil Bernal

## Functionality:

The project consists on four modules, dedicated to create a business model about recomendation of hotels based on weather and prices data.

Two of them ('predict-provider' and 'hotel-prices-sensor') are in charge of collecting data about weather and hotels from APIs ('Xotelo' and 'OpenWeatherMap').

'datalake-builder' collects the events from the last two modules, saving them in a historic datalake divided by topic and date in json format.

'business unit' has the business logic, taking events from the datalake and the topics, saving it in a SQLite datamart, with an API in which the user can extract information from the datamart.

## Resources:

The development enviroment used in this project is IntelliJ, and mainly developed in Java language.

 ## Requirements

For the correct use of the project there must exist two files named 'locations.txt' and 'APIkeys.txt'.

* 'locations.txt': This file shall have the name, latitute and longitude of every location we want to extract data about the weather. Example of the content in the file:
```
{"name": "GranCanaria","lat": 28.1,"lon": -15.41}
{"name": "Tenerife","lat": 28.46,"lon": -16.25}
{"name": "Fuerteventura","lat": 28.2,"lon": -14.00}
{"name": "Lanzarote","lat": 28.95,"lon": -13.76}
{"name": "LaPalma","lat": 28.71,"lon": -17.9}
{"name": "ElHierro","lat": 27.75,"lon": -18}
{"name": "LaGomera","lat": 28.1,"lon": -17.11}
{"name": "LaGraciosa","lat": 28.05,"lon": -15.44}
```

* 'APIkeys.txt': This file shall have the location name of the hotel (if the location matches with another from the locations file they must be written the same), and a hotel key ([How to obtain the hotel key](https://xotelo.com/how-to-get-hotel-key.html)). Example of the content in the file:
```
{"hotelToken": "g187472-d228489", "location": "GranCanaria"}
{"hotelToken": "g562819-d600110", "location": "GranCanaria"}
{"hotelToken": "g562819-d289643", "location": "GranCanaria"}
{"hotelToken": "g662606-d296925", "location": "Tenerife"}
{"hotelToken": "g580321-d282759", "location": "Lanzarote"}
{"hotelToken": "g580322-d287995", "location": "Fuerteventura"}
{"hotelToken": "g1190272-d2645782", "location": "LaGraciosa"}
{"hotelToken": "g187474-d277394", "location": "ElHierro"}
{"hotelToken": "g187470-d190895", "location": "LaGomera"}
{"hotelToken": "g1177806-d3577949", "location": "LaPalma"}
```

## Design:

### predict-provider

**MODEL:**
* Location: This class has three attributes, name, latitute and longitude.
* Weather: This class represents the weather values of a location, its attributes are: Location, temperature, humidity, rain, wind speed, clouds, prediction time, ss ('prediction.Weather') and ts (TimeStamp).

**CONTROL:**
* SendWeatherTopic: This interface has a void method, 'sendWeather', that takes as an argument a json string.
* WeatherProvides: This interface has a method, 'weatherGet', that returns an ArrayList<String> and receives as an argument a Location.
* OpenWeatherMapProvider: This class implements WeatherProvides, the constructor takes an APIkey as an argument and has a predefined template_url attribute that represents an url to get the data. the method 'weatherGet' takes a Location and replaces certain parts of the base URL with the latitute and longitude, connecting to this URL and extracting the data to insert into the ArrayList that will be returned.
*  TopicWeather: This class implements SendWeatherTopic, the constructor has a predefined broker_URL ('tcp://localhost:61616'), the method SendWeather connects to this broker and sends each json string that receives to the topic 'prediction.Weather'.

### hotel-prices-sensor

### datalake-builder

### business-unit


The design of the predict-provider:

The module has 2 model classes:
- Location: consists on the coordinates in latitude, longitude and the name of the region.
- Weather: temperature, rain probabilty, instant of the data, wind speed humidity, location and clouds

  Control models:
  - StoreException: Custom exception.
  - WeatherProvides: an interface with  a function that returns a list of five weathers.
  - OpenWeatherMapProvider: has two attributes, the apiKEY and the base URL, the function implemented from WeatherProvides takes two arguments, a location and an instant, connects to the database and reads the json, translating it into a Weather object and returning an array.
  - SendWeatherTopic: an interface with a void function that receives as an argument a json String
  - TopicWeather: implements SendWeatherTopic, uses the functions sendWeather to connect to a broker and send the Strings that receives to the topic.
  - WeatherControl: Has a function called Execute that loops the locations array received from the function readLocations that reads the file with all the locations and splits and insert them in the array, and sends that array to OpenWeatherMapProvider, receiving the strings of every prediction, then loops again and sends them to the broker by the function sendWeathers.
  - Main: calls to the function Execute from WeatherControl every six hours.

![image](https://github.com/BeepBoopVictor/WeatherAPI/assets/145380029/ab3d4214-e820-4086-876b-7558611a6b23)

The design of the event-store-builder:

Control models:
  - Subscriber: an interface with a void function named start, that receives a listener interface.
  - AMQTopicSubscriber: a class that implements Subscriber with the start function, that receives the topicName, the brokerURL, the consumerName, clientID to create a durable subscriber to the topic that receives the JSON strings and calls to the consume function in the listener class.
  - Listener: and interface with a void function named consume, that receives as an argument a json String.
  - FileEventStore: a class that implements Listener with a function consume that creates a series of folders with a file that stores the json Strings.
  - The main class executes both classes and receives as an argument the direction im which you want to create the folders.

![image](https://github.com/BeepBoopVictor/WeatherAPI/assets/145380029/c42b689d-f699-425c-a5c2-94c892780120)





