# --- Build stage ---
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app

# Cache dependencies first (only re-runs if build.gradle changes)
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon --quiet

# Build the app
COPY src ./src
RUN gradle bootJar --no-daemon -x test

# --- Run stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/CampusKart-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]