# Utilise une image légère avec OpenJDK 17
FROM openjdk:17-jdk-slim

# Crée un utilisateur non-root
RUN useradd -m -u 1000 appuser

# Crée un dossier de travail dans le conteneur
WORKDIR /app

# Copie le fichier .jar compilé dans le conteneur
COPY target/*.jar app.jar

# Change les permissions du fichier JAR pour l'utilisateur non-root
RUN chown appuser:appuser app.jar

# Passe à l'utilisateur non-root
USER appuser

# Expose le port utilisé par ton application Spring Boot
EXPOSE 8089

# Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]