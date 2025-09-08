# Stage 1: Use a Maven image to build the application JAR file
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the final, lightweight image with only Java
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file created in the build stage
COPY --from=build /app/target/*.jar app.jar

# Tell Render that your application will run on port 8081
EXPOSE 8081

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]