FROM gradle:jdk17 AS cache
WORKDIR /home/gradle/app
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies --no-daemon

FROM gradle:jdk17 AS builder
COPY --from=cache /home/gradle/.gradle /home/gradle/.gradle
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y docker.io && rm -rf /var/lib/apt/lists/* && apt-get clean
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]