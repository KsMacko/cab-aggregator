FROM eclipse-temurin:17-jre as build
WORKDIR /app
COPY target/eureka-service.jar eureka-service.jar
RUN java -Djarmode=layertools -jar eureka-service.jar extract

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]