# Build stage
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

# Cache dependencies
RUN gradle dependencies --no-daemon

# Build the application
RUN gradle bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/application.jar

# Create PostgreSQL directory and add certificate
RUN mkdir -p /root/.postgresql

# Set a default environment
ENV ENVIRONMENT=dev

EXPOSE 8195
EXPOSE 8086
EXPOSE 8095

# Create an entrypoint script to select the right environment script
ENTRYPOINT ["/bin/sh", "-c", "java -jar /app/application.jar --spring.profiles.active=${ENVIRONMENT}"]
