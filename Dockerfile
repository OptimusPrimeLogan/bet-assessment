# --- Stage 1: Build the application ---
FROM gradle:8.14-jdk21-alpine AS build

WORKDIR /home/gradle/src

COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

COPY src ./src
RUN gradle bootJar --no-daemon

# --- Stage 2: Create the final, optimized production image ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

EXPOSE 9090

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]