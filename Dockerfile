# Java 21 runtime
FROM eclipse-temurin:21-jre

# Application working directory
WORKDIR /app

# Copy Spring Boot executable JAR
COPY target/*.jar app.jar

# Render application port
EXPOSE 8080

# Start application
ENTRYPOINT ["java", "-Xms64m", "-Xmx384m", "-jar", "app.jar"]
