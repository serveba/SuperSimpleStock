# Super Simple Stock

This is the description file of the project. Here you will find tips for running the application also the architecture description. This project is a tech exercise example. 

### Project architecture

The project is a classic Maven Java Project. It uses some Spring libraries in order to have Dependency Injection, context support, unit testing (also with JUnit) and a very clear separation into the different application layers:

* **Service**: This layer has all the operations the library provides. **SuperSimpeStockService** interface.

* **Persistence** The layer that deals with storage. In this case we keep all the data in memory (HashMap and ArrayList) we use the interface **SuperSimpleStockEntityManager** for that purpose.

* **Model** For the model we have the classes **Stock** and **Trade**. Both classes have the attributes and operations of each concept involved with the exercise.

It's not a webapp, only a library (JAR file). Because of the structure is easy to expose the functionallity with  a SOAP or RESTFull webservice, also you can integrate as jar file with another projects.

In order to probe the required functionallity we have made one test class **SuperSimpleStockServiceImplTest** for testing purposes with the sample data generated for the exercise and all the required operations.

### Running the project

It has been run successfully with Apache Maven 3.1.1 and Java 1.7.0_60.
You can run the project with the following maven command (inside the project directory):

$> mvn package

This command downloads the required libraries to build the project. Compiles the project, run the tests (where we probe the required features) and generates the JAR file under the target folder for integration purposes.

