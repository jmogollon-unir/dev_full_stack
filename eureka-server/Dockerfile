# Build con Java 25 (Spring Boot 4). Etiqueta maven con Temurin 25: ver hub.docker.com/_/maven (p. ej. 3.9.16-eclipse-temurin-25-noble).
FROM maven:3.9.16-eclipse-temurin-25-noble AS build
WORKDIR /build
COPY pom.xml .
COPY src src
RUN mvn -B -q package -DskipTests

FROM eclipse-temurin:25-jre-noble
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
