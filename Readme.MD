# Reactive REST API
BCI test with reactive REST API developed using Spring Boot with RxJava.

Se requiere tener JDK 11 y maven 3.6.3 instalados 

### Para ejecutar los test del servicio core
mvn clean test

### Para compilar el proyecto necesita ejecutar el siguiente compando
mvn clean package

### Jar para correr solo jar desde terminal/consola
java -jar target/rest-test-0.0.1-SNAPSHOT.jar

#### Ejecutar mediante Maven con spring boot desde terminal/consola
mvn spring-boot:run

## Docker

### Para generar la imagen
docker build -t bci_test .

### Docker Run
docker run --rm --name=reactive -p 8081:8081 bci_test:latest

### Pruebas
Se puede usar cualquier herrameinta para realizar invocaciones rest. Una opcion es utilizar Imsomnia( https://insomnia.rest/download ). En la raiz del proyecto, se tiene el archivo insomnia_test.json donde se colocan los request para realizar las pruebas.
En la carpeta evidencias, se enceuntra el archivo evidencias.pdf, donde se puede vizualizar las pruebas realziadas segun los casos presentados en la evalaucion. 

### Swagger
La documentación swagger esta disponible en [ http://localhost:8081/v2/api-docs ]. 