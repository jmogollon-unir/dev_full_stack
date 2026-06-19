FROM maven:3.9.16-eclipse-temurin-25-noble AS build
WORKDIR /build
COPY pom.xml .
COPY src src
RUN mvn -B -q package -DskipTests

FROM eclipse-temurin:25-jre-noble
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
