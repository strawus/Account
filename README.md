# Manage accounts API

A Java RESTful API for managing bank accounts in DDD style.
To store accounts used embedded PostgreSQL

### Technologies
- Spring (Boot, Data, Web, WebFlux, AOP, Actuator, Test)
- Tomcat
- Hibernate
- Hibernate Validator
- Reactor
- Lombok
- MapStruct
- EhCache
- Swagger
- Logback
- otj-pg-embedded (as Embedded PostgreSQL)

### How to run
```sh
mvn exec:java
```

Application starts a embedded tomcat server on localhost:8080 

API documentation available by address: *http://localhost:8080/swagger-ui.html*

Some application info available by addresses:
- Health check: *http://localhost:8080/actuator/health*
- Application properties: *http://localhost:8080/actuator/configprops*
- Metrics: *http://localhost:8080/actuator/metrics*