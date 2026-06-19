<<<<<<< HEAD
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
=======
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:25-jre-jammy
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /app/target/eureka-server-*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
>>>>>>> 9f533c0d49dc694d06b34dec912d4f9f1d3571cf
