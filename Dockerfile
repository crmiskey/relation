# Use JDK 21 (Alpine is a lightweight Linux version)
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the generated JAR file into the container
# Ensure you run './mvnw package' or './gradlew build' before this
COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]