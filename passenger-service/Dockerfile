FROM  openjdk:17-jdk-slim

WORKDIR /app

COPY target/passenger-service.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]