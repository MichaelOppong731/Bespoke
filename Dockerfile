# Build Stage (Use a builder image to create the JAR)
FROM maven:3.8.3-openjdk-17 AS build
# Copy the project files and build the JAR
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
# Copy the JAR from the build stage
COPY --from=build /target/Bespoke-0.0.1-SNAPSHOT Bespoke.jar
# Expose the port your Spring Boot app uses (default 8080)
EXPOSE 8080
# Define the entry point (how to start your app)
ENTRYPOINT ["java","-jar","Bespoke.jar"]