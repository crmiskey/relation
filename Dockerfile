# --- STAGE 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application (skipping tests to save time/memory)
RUN mvn clean package -DskipTests

# --- STAGE 2: Run the application ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the JAR from the 'build' stage to this final stage
COPY --from=build /app/target/*.jar app.jar

# Set memory limits for Render's free tier
ENV JAVA_OPTS="-Xmx384m -Xms384m"

ENTRYPOINT ["java", "-jar", "app.jar"]