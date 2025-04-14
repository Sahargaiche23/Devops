# Use Eclipse Temurin JRE (smaller than full JDK)
FROM eclipse-temurin:17-jre-alpine

# Add labels for better maintainability
LABEL maintainer="Student" \
      description="Spring Boot Kaddem Application"

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy only the JAR file
COPY target/*.jar app.jar

# Set ownership
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8089

# Set JVM options to reduce memory usage
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]