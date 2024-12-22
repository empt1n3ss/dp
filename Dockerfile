FROM openjdk:22
EXPOSE 8080
COPY target/dp-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]