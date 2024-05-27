# Build Stage (Use a builder image to create the JAR)
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
# Copy the project files and build the JAR
COPY . .
COPY src ./src


RUN mvn clean package -DskipTests

# Run Stage
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/target/Bespoke-0.0.1-SNAPSHOT.jar Bespoke.jar
# Expose the port your Spring Boot app uses (default 8080)
EXPOSE 8080
# Define the entry point (how to start your app)
ENTRYPOINT ["java","-jar","Bespoke.jar"]
