FROM maven:3.9-eclipse-temurin-17-alpine as build

# copy main pom
RUN mkdir -p /usr/src/app/illchess/illchess-stockfish
COPY pom.xml /usr/src/app/illchess
# copy domain
RUN mkdir /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-domain
COPY illchess-stockfish/illchess-stockfish-domain/pom.xml /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-domain
COPY illchess-stockfish/illchess-stockfish-domain/src /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-domain/src
# copy application
RUN mkdir /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-application
COPY illchess-stockfish/illchess-stockfish-application/pom.xml /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-application
COPY illchess-stockfish/illchess-stockfish-application/src /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-application/src
# copy adapter
RUN mkdir /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-adapter
COPY illchess-stockfish/illchess-stockfish-adapter/pom.xml /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-adapter
COPY illchess-stockfish/illchess-stockfish-adapter/src /usr/src/app/illchess/illchess-stockfish/illchess-stockfish-adapter/src
# copy server
RUN mkdir /usr/src/app/illchess/illchess-stockfish-server
COPY illchess-stockfish-server/pom.xml /usr/src/app/illchess/illchess-stockfish-server
COPY illchess-stockfish-server/src /usr/src/app/illchess/illchess-stockfish-server/src

RUN mvn -f /usr/src/app/illchess clean install -DskipTests -N
RUN mvn -f /usr/src/app/illchess clean install -DskipTests

FROM ubuntu
RUN apt update --assume-yes
RUN apt install stockfish
RUN apt install default-jre --assume-yes
ENV PATH="$PATH:/usr/games"

COPY --from=build /usr/src/app/illchess/illchess-stockfish-server/target/illchess-stockfish-server-runner.jar illchess-stockfish-server.jar

ENTRYPOINT ["java", "-jar", "illchess-stockfish-server.jar"]
EXPOSE 2137
