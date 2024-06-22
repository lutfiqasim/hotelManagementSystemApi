# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=target/hotelManagmentAPI-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

EXPOSE 8080


# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]