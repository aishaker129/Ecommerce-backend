# -------- Stage 1: Build --------
FROM gradle:8.7-jdk21 AS build

WORKDIR /app

COPY build.gradle settings.gradle* gradlew ./
COPY gradle ./gradle

RUN chmod +x gradlew

# download dependencies first (better caching)
RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew clean bootJar --no-daemon


# -------- Stage 2: Runtime --------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# run app
ENTRYPOINT ["java", "-jar", "app.jar"]