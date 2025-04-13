# Use a lightweight OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Create a non-root user and group
RUN groupadd -r spring && useradd -r -g spring spring

# Create a working directory and set ownership
WORKDIR /app
RUN chown -R spring:spring /app

# Copy the JAR file (built from your Maven/Gradle project)
COPY --chown=spring:spring target/*.jar app.jar

# Switch to the non-root user
USER spring

# Expose the Spring Boot application port
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]