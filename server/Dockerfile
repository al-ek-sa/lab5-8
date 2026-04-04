FROM eclipse-temurin:21-jdk-alpine
LABEL authors="Aliaksandra"
WORKDIR /app
COPY target/server-1.0-SNAPSHOT-jar-with-dependencies.jar /app/server.jar
EXPOSE 6688
CMD ["java", "-jar", "server.jar"]

ENTRYPOINT ["top", "-b"]