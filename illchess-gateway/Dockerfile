FROM maven:3.9-eclipse-temurin-17-alpine as build

RUN mkdir -p /usr/src/app/src
COPY pom.xml /usr/src/app
COPY src /usr/src/app/src

RUN mvn -f /usr/src/app clean install -DskipTests

FROM eclipse-temurin:17-alpine
COPY --from=build /usr/src/app/target/illchess-gateway.jar illchess-gateway.jar
ENTRYPOINT ["java", "-jar", "illchess-gateway.jar"]
EXPOSE 1337