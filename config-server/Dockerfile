FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar
COPY config-repo ./config-repo

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]