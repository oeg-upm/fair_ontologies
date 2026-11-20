# syntax=docker/dockerfile:1

# ---- Build stage ----------------------------------------------------------
# Builds the Spring Boot server JAR from the default pom.xml.
# JDK 11 matches the version the project was built and tested with.
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app

# Resolve dependencies first so they are cached as long as pom.xml is unchanged.
COPY pom.xml ./
RUN mvn -B dependency:go-offline

# Build the application (tests are skipped: several of them hit the network).
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Runtime stage --------------------------------------------------------
FROM eclipse-temurin:11-jre-jammy
WORKDIR /app

# Run as an unprivileged user.
RUN groupadd --system foops && useradd --system --gid foops foops

# Spring Boot repackages FOOPSServerApplication as the executable JAR.
COPY --from=build /app/target/fair_ontologies-*.jar app.jar

# Port the server listens on inside the container (Spring Boot default).
ENV SERVER_PORT=8080
ENV JAVA_OPTS=""
EXPOSE 8080

USER foops

# exec so the JVM is PID 1 and receives stop signals directly.
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -Dserver.port=${SERVER_PORT} -jar app.jar"]
