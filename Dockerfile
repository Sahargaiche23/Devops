# Utilise une image légère avec OpenJDK 17
FROM openjdk:17-jdk-slim

# Crée un dossier de travail dans le conteneur
WORKDIR /app

# Copie le fichier .jar compilé dans le conteneur
COPY target/*.jar app.jar

# Expose le port utilisé par ton application Spring Boot
EXPOSE 8089

# Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
git add Dockerfile
