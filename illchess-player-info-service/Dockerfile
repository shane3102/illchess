FROM maven:3.9-eclipse-temurin-17-alpine as build

# copy main pom
RUN mkdir -p /usr/src/app/illchess/illchess-player-info
COPY pom.xml /usr/src/app/illchess
# copy domain
RUN mkdir /usr/src/app/illchess/illchess-player-info/illchess-player-info-domain
COPY illchess-player-info/illchess-player-info-domain/pom.xml /usr/src/app/illchess/illchess-player-info/illchess-player-info-domain
COPY illchess-player-info/illchess-player-info-domain/src /usr/src/app/illchess/illchess-player-info/illchess-player-info-domain/src
# copy application
RUN mkdir /usr/src/app/illchess/illchess-player-info/illchess-player-info-application
COPY illchess-player-info/illchess-player-info-application/pom.xml /usr/src/app/illchess/illchess-player-info/illchess-player-info-application
COPY illchess-player-info/illchess-player-info-application/src /usr/src/app/illchess/illchess-player-info/illchess-player-info-application/src
# copy adapter
RUN mkdir /usr/src/app/illchess/illchess-player-info/illchess-player-info-adapter
COPY illchess-player-info/illchess-player-info-adapter/pom.xml /usr/src/app/illchess/illchess-player-info/illchess-player-info-adapter
COPY illchess-player-info/illchess-player-info-adapter/src /usr/src/app/illchess/illchess-player-info/illchess-player-info-adapter/src
# copy server
RUN mkdir /usr/src/app/illchess/illchess-player-info-server
COPY illchess-player-info-server/pom.xml /usr/src/app/illchess/illchess-player-info-server
COPY illchess-player-info-server/src /usr/src/app/illchess/illchess-player-info-server/src

RUN mvn -f /usr/src/app/illchess clean install -DskipTests -N
RUN mvn -f /usr/src/app/illchess clean install -DskipTests

FROM eclipse-temurin:17-alpine
COPY --from=build /usr/src/app/illchess/illchess-player-info-server/target/illchess-player-info-server-runner.jar illchess-player-info-server.jar
ENTRYPOINT ["java", "-jar", "illchess-player-info-server.jar"]
EXPOSE 420