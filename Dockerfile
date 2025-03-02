FROM openjdk:17-jdk-alpine

LABEL authors="sahar"
VOLUME /tmp
COPY target/kaddem-0.0.1.jar kaddem-0.0.1.jar
ENTRYPOINT ["java","-jar","/kaddem-0.0.1.jar"]