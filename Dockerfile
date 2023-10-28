# Establish a base image with OpenJDK 17
FROM openjdk:17-jdk-slim AS base

# Set the working directory
WORKDIR /app

# Copy the necessary gradle files to the container
COPY gradlew build.gradle settings.gradle ./

# Copy the gradle folder to the container
COPY gradle/ gradle/

# Copy the actual source code of the application
COPY src/ src/

# Resolve the Gradle dependencies
RUN ./gradlew build --no-daemon

# Development stage
FROM base AS  development

# Command to run the Spring boot application
CMD ["./gradlew", "bootRun"]

# Build stage
FROM base AS build

# Build the Spring Boot application
RUN ./gradlew build --no-daemon

# Production Stage
FROM openjdk:17-oracle AS production

# Set the working directory
WORKDIR /app

# Expose the port
EXPOSE 8080

# Copy the built JAR file
COPY --from=build /app/build/libs/databaseAndAuth-0.0.1-SNAPSHOT.jar app.jar

# Command to run the Spring Boot Server
CMD ["java", "-Dspring.data.mongodb.uri=${MONGODB_URI}", "-jar", "app.jar"]
