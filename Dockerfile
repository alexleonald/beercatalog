FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/beer-catalogue-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
