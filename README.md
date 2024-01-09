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

### Execution Requirements:

#### predict-provider:

This module takes two arguments in this order:
1. The OpenWeatherMapProvider API
2. The direction to 'locations.txt'

#### hotel-prices-sensor:

This module takes one argument:
1. The direction to 'APIKeys.txt'

#### datalake-builder:

This module takes one argument:
1. The direction where you want to create the datalake.

#### business-unit:

This module takes five arguments:
1. The complete direction to the datamart.
2. The topic name: 'prediction.Weather'
3. The topic name: 'prediction.Hotel'
4. The complete direction to the file that stores the .events about hotels.
5. 4. The complete direction to the file that stores the .events about weathers.


## Design:

### predict-provider:

**MODEL:**
* Location: This class has three attributes, name, latitute and longitude.
* Weather: This class represents the weather values of a location, its attributes are: Location, temperature, humidity, rain, wind speed, clouds, prediction time, ss ('predict-provider') and ts (TimeStamp).

**CONTROL:**
* SendWeatherTopic: This interface has a void method, 'sendWeather', that takes as an argument a json string.
* WeatherProvides: This interface has a method, 'weatherGet', that returns an ArrayList<String> and receives as an argument a Location.
* OpenWeatherMapProvider: This class implements WeatherProvides, the constructor takes an APIkey as an argument and has a predefined template_url attribute that represents an url to get the data. the method 'weatherGet' takes a Location and replaces certain parts of the base URL with the latitute and longitude, connecting to this URL and extracting the data to insert into the ArrayList that will be returned.
* TopicWeather: This class implements SendWeatherTopic, the constructor has a predefined broker_URL ('tcp://localhost:61616'), the method SendWeather connects to this broker and sends each json string that receives to the topic 'prediction.Weather'.
* WeatherControl: This class has a method called 'Execute' that loops the locations array received from the function readLocations, that reads the file with all the locations, inserting them in the array, which will loop, sending its items to OpenWeatherMapProvider, receiving the strings of every prediction and inserting them in another array, then loops again and sends them to the broker by the function sendWeathers.
* Main: calls to the function Execute from WeatherControl every six hours.

<img src="https://github.com/BeepBoopVictor/WeatherAPI/assets/145380029/adeceade-e0ca-4872-982d-2449541b792c" alt="Descripción de la imagen" width="900" height="600">

### hotel-prices-sensor

**MODEL:**
* Hotel: This class will be converted to the event, representing the hotel, it has a HotelValues, three ArrayList<String> with the dates in which the prices are either cheap, average or expensive, a timestamp and an ss ('hotel-provider')
* HotelValues: This class represents the values taken from the text file, it has two attributes, location and hotelToken.

**CONTROL:**
* SendHotel: This interface has a void method, 'sendHotel', that takes as an argument a json string.
* InterfacePriceProvider: This interface has a method, 'priceGet', that returns a Hotel and receives as an argument a hotelValues string and a checkOut string.
* PriceProvider: This class implements InterfacePriceProvider, the constructor has a predefined template_url attribute that represents an url to get the data. The method 'priceGet' takes a hotelValues and a checkOut and replaces certain parts of the base URL with these, connects to this URL and extracts the data to create the Hotel that is returned.
* TopicHotel: This class implements SendHotel, the constructor has a predefined broker_URL ('tcp://localhost:61616'), the method SendHotel connects to this broker and sends each json string that receives to the topic 'prediction.Hotel'.
* PriceControl: This class has a method called 'Execute' that loops the hotelValues array received from the function readHotelTokens, that reads the file with all the hotelValues, inserting them in the array, which will loop, sending its items to PriceProvider, receiving the strings of every prediction and inserting them in another array, then loops again and sends them to the broker by the function sendHotel.
* Main: calls to the function Execute from PriceControl every six hours.

<img src="https://github.com/BeepBoopVictor/WeatherAPI/assets/145380029/69a1b567-40a1-4cf3-b038-7b68773883b6" alt="Descripción de la imagen" width="900" height="600">


### datalake-builder

**MODEL:**

* Weather: This class represents the weather values taken as events from the broker, its attributes are: Location, temperature, humidity, rain, wind speed, clouds, prediction time, ss ('predict-provider') and ts (TimeStamp).
* Location: This class has three attributes, name, latitute and longitude.

**CONTROL:**

* Storage: This interface has a void method, 'consume', that takes as arguments a json string and the name of the topic.
* Subscriber: This interface has a void method, 'start'.
* FileEventStoreBuilder: This class implements 'Storage', the constructor takes as an argument a directory a path to store the events, the method 'consume' stores the events taken from AMQTopicSubscriber in written files with the format: 'datalake/eventstore/{topic}/{ss}/{YYYYMMDD}.events'.
* AMQTopicSubscriber: This class implements 'Subscriber', the constructor takes as an argument a 'Storage' class and has a predefined brokerURL('tcp://localhost:61616'), the start function creates two subscriptions to both topics and sends the events to the 'Storage' method: 'consume'. 
* Main: The main method creates an instance of 'FileEventStoreBuilder' and 'AMQTopicSubscriber', then calls to the 'start' function.

<img src="https://github.com/BeepBoopVictor/WeatherAPI/assets/145380029/de7092d8-557d-462f-a74e-bcd64e70029d" alt="Descripción de la imagen" width="900" height="600">


### business-unit

**MODEL:**

* Weather: This class will be stored in the SQLite database, its attributes are: locationName, temperature, humidity, rain, wind speed, clouds and prediction time.
* Hotel: This class will be stored in the SQLite database, locationName, hotel key, date and priceStatus.

**CONTROL:**

* DatalakeLoader: This interface has a void method 'readEvents' that takes as arguments the datalake direction and the topic name.
* Manager: This interface has a void method 'manageEvents' that takes as argument a json string.
* StoreInterface: This interface has a void method 'save' that takes as argument a Map<String, String> with the attributes of the Hotel or the Weather.
* Subscriber: This interface has a void method 'start' that takes as argument a 'StoreInterface' class.

* DataMartSQL: This class implements 'StoreInterface', the constructor takes as an argument the direction of the datamart and has a 'Statement' attribute. The 'save' method takes the Map with the attributes of the model classes and stores them in the datamart.
* HotelManager: This class implements 'Manager', the constructor takes as an argument a 'StoreInterface'. The events sent to the 'manageEvents' method are split in many Hotel objects kept in an ArrayList, this ArrayList is looped and every Hotel attributes are kept in the Map that will be sent to the 'save' method.
* WeatherManager: This class implements 'Manager', the constructor takes as an argument a 'StoreInterface'. The events sent to the 'manageEvents' method are split in many Hotel objects kept in an ArrayList, this ArrayList is looped and every Hotel attributes are kept in the Map that will be sent to the 'save' method.
* ReadTextDatalake: This class implements 'DataLakeLoader', the constructor takes as an argument a Map<String, Manager>. The 'readEvents' method sends the last events from the datalake to the 'Manager'.
* TopicReceiver: This class implements 'Subscriber', the constructor takes as an argument a Map<String, Manager> and has a brokerURL predefined. The start function creates two subscriptions to both topics and sends the events to the 'Manager' method: 'manageEvents'. 
* Main: The main method creates instances of the classes and calls to the 'start' function.








