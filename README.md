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



## Design:

### predict-provider

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





