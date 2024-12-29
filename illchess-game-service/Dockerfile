FROM maven:3.9-eclipse-temurin-17-alpine as build

# copy main pom
RUN mkdir -p /usr/src/app/illchess/illchess-game
COPY pom.xml /usr/src/app/illchess
# copy domain
RUN mkdir /usr/src/app/illchess/illchess-game/illchess-game-domain
COPY illchess-game/illchess-game-domain/pom.xml /usr/src/app/illchess/illchess-game/illchess-game-domain
COPY illchess-game/illchess-game-domain/src /usr/src/app/illchess/illchess-game/illchess-game-domain/src
# copy application
RUN mkdir /usr/src/app/illchess/illchess-game/illchess-game-application
COPY illchess-game/illchess-game-application/pom.xml /usr/src/app/illchess/illchess-game/illchess-game-application
COPY illchess-game/illchess-game-application/src /usr/src/app/illchess/illchess-game/illchess-game-application/src
# copy adapter
RUN mkdir /usr/src/app/illchess/illchess-game/illchess-game-adapter
COPY illchess-game/illchess-game-adapter/pom.xml /usr/src/app/illchess/illchess-game/illchess-game-adapter
COPY illchess-game/illchess-game-adapter/src /usr/src/app/illchess/illchess-game/illchess-game-adapter/src
# copy server
RUN mkdir /usr/src/app/illchess/illchess-game-server
COPY illchess-game-server/pom.xml /usr/src/app/illchess/illchess-game-server
COPY illchess-game-server/src /usr/src/app/illchess/illchess-game-server/src

RUN mvn -f /usr/src/app/illchess clean install -DskipTests -N
RUN mvn -f /usr/src/app/illchess clean install -DskipTests
RUN ls /usr/src/app/illchess/illchess-game-server/target

FROM eclipse-temurin:17-alpine
COPY --from=build /usr/src/app/illchess/illchess-game-server/target/illchess-game-server.jar illchess-game-server.jar
ENTRYPOINT ["java", "-jar", "illchess-game-server.jar"]
EXPOSE 8080