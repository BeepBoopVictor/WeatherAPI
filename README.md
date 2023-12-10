# WeatherAPI

Subject: Desarrollo de aplicaciones de Ciencia de Datos
Grade: Second of Data science
School: Universidad de Las Palmas de Gran Canaria
Name: Víctor Gil Bernal

## Functionality:

The projects consists on two modules, the first one conects to an OpenWeather API with a 5 day forecast, takes the values for the next five days and sends it to a broker every six hours, the second one receives the json objects and store the in a file.

## Resources:

The development enviroment used in this project is IntelliJ.

 ## Requirements

 To use this code it is neccessary to insert **two arguments** in the predict-provider's Main, the first one is the **apiKey** and the second one 
 with a **direction to a txt file** with the json of each locations. Here is the text to put in the file:

* {"name": "GranCanaria","lat": 28.1,"lon": -15.41}
* {"name": "Tenerife","lat": 28.46,"lon": -16.25}
* {"name": "Fuerteventura","lat": 28.2,"lon": -14.00}
* {"name": "Lanzarote","lat": 28.95,"lon": -13.76}
* {"name": "LaPalma","lat": 28.71,"lon": -17.9}
* {"name": "ElHierro","lat": 27.75,"lon": -18}
* {"name": "LaGomera","lat": 28.1,"lon": -17.11}
* {"name": "LaGraciosa","lat": 28.05,"lon": -15.44}

In the event-store-builder's Main it is only necessary **one argument**, which is the location of the location in which you want the json to be kept, for example in the Desktop.

## Design:

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





