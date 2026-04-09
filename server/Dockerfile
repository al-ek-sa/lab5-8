FROM eclipse-temurin:17-jdk-alpine
LABEL authors="Aliaksandra"
WORKDIR /app
COPY target/server-1.0-SNAPSHOT-jar-with-dependencies.jar /app/server.jar
EXPOSE 6688
CMD ["java", "-jar", "server.jar"]

ENTRYPOINT ["java", "-jar", "/app/server.jar"]