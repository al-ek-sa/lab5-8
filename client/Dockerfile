FROM eclipse-temurin:17-jdk-alpine
LABEL authors="Aliaksandra"
WORKDIR /app
COPY target/client-1.0-SNAPSHOT-jar-with-dependencies.jar /app/client.jar
CMD ["java", "-jar", "client.jar"]
ENTRYPOINT ["java", "-jar", "/app/client.jar"]