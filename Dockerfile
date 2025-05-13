# syntax=docker/dockerfile:1.4

FROM gradle:8.7-jdk17 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
# cache dependencies
RUN --mount=type=cache,target=/home/gradle/.gradle gradle dependencies --no-daemon

COPY src ./src

# use cache for gradle
RUN --mount=type=cache,target=/home/gradle/.gradle gradle bootJar --no-daemon

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/application.jar
RUN mkdir -p /root/.postgresql
ENV ENVIRONMENT=dev
EXPOSE 8195 8086 8095
ENTRYPOINT ["/bin/sh", "-c", "java -jar /app/application.jar --spring.profiles.active=${ENVIRONMENT}"]
