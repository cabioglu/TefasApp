# Use OpenJDK 23 early-access build as the base image
FROM openjdk:23-ea-slim

# Set the working directory
WORKDIR /app

# Copy the application WAR file
COPY target/tefas-0.0.1-SNAPSHOT.war app.war

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.war"]
