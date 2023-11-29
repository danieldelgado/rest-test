# Reactive REST API
BCI test with reactive REST API developed using Spring Boot with RxJava.

### Docker image
Para generar la imagen
docker build -t bci_test .

### Docker Run
docker run --rm --name=reactive -p 8081:8081 bci_test:latest

### Jar para correr solo jar
java -jar reactive-0.0.1-SNAPSHOT.jar

#### o mediante Maven

mvn spring-boot:

### Pruebas
Se puede usar cualquier herrameinta para realizar invocaciones rest. Una opcion es utilizar Imsomnia. En la raiz del proyecto, se tiene el archivo insomnia_test.json donde se colocan los request para realizar las pruebas.


### Swagger
Para obtener el archivo swagger se obtiene de [http://localhost:8081/v2/api-docs]. 