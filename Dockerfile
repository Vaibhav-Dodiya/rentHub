## Use official OpenJDK image
#FROM openjdk:17-jdk-slim
#
## Set working directory inside the container
#WORKDIR /app
#
## Copy the built JAR file into the container
#COPY target/*.jar app.jar
#
## Expose Spring Boot default port
#EXPOSE 8080
#
## Run the application
#ENTRYPOINT ["java", "-jar", "app.jar"]
# Use Maven to build the project
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller runtime image for final app
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
