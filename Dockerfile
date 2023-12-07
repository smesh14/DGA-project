# First stage: Build with Maven
FROM maven:3.6.3-openjdk-17 AS build

# Set the working directory
WORKDIR /workspace

# Copy the entire project
COPY . /workspace

# Build the application
RUN mvn clean package

# Second stage: Run with Amazon Corretto 17
FROM amazoncorretto:17

# Set the working directory
WORKDIR /app

# Copy only the built JAR from the first stage
COPY --from=build /workspace/target/*.jar /app/

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "contact_book-0.0.1-SNAPSHOT.jar"]
