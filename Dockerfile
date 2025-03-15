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
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/sharingmap-0.5.1.jar /app/application.jar

# Create PostgreSQL directory and add certificate
RUN mkdir -p /root/.postgresql

# Copy all environment scripts
COPY src/main/resources/run-dev.sh /app/run-dev.sh
COPY src/main/resources/run-test.sh /app/run-test.sh
COPY src/main/resources/run-prod.sh /app/run-prod.sh

# Make all scripts executable
RUN chmod +x /app/*.sh

# Set a default environment
ENV ENVIRONMENT=dev

EXPOSE 8195
EXPOSE 8086
EXPOSE 8095

# Create an entrypoint script to select the right environment script
RUN echo '#!/bin/bash\n\
if [ "$ENVIRONMENT" = "dev" ]; then\n\
  exec /app/run-dev.sh\n\
elif [ "$ENVIRONMENT" = "test" ]; then\n\
  exec /app/run-test.sh\n\
elif [ "$ENVIRONMENT" = "prod" ]; then\n\
  exec /app/run-prod.sh\n\
else\n\
  echo "Unknown environment: $ENVIRONMENT"\n\
  exit 1\n\
fi' > /app/entrypoint.sh && chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]
