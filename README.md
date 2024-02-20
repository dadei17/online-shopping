# Online-shopping
Online Shopping is a demo application built with Spring Boot '3.2.2' to demonstrate the creation of products and orders via REST APIs.

### Getting Started
To start the application, simply run it using your preferred IDE or by executing the JAR file. Once the application is running, you can access the Swagger API documentation to explore the available endpoints.

### Swagger-api documentation
For detailed API documentation, visit [here](http://localhost:8080/docs)

### Authentication/Authorization
The application utilizes Basic Authentication with disabled CSRF (Cross-Site Request Forgery) protection. Predefined users with MD5 hashed passwords are provided for authentication purposes.

### Database
The application uses an H2 in-memory database to demonstrate the entire workflow. For the sake of authentication and authorization, predefined users with MD5 hashed passwords are created.
![Alt text](images/users.png?raw=true "Title")

### Testing
The application is thoroughly tested using JUnit and Mockito frameworks to ensure reliability and functionality.