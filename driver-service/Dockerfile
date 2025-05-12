FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/driver-service.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]