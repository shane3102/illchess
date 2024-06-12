FROM ubuntu
RUN apt update && apt upgrade --assume-yes
RUN apt install stockfish
RUN apt install default-jre --assume-yes
ENV PATH="$PATH:/usr/games"

COPY illchess-stockfish-server/target/illchess-stockfish-server-1.0.0-SNAPSHOT-runner.jar illchess-stockfish-server-1.0.0-SNAPSHOT-runner.jar

ENTRYPOINT ["java", "-jar", "illchess-stockfish-server-1.0.0-SNAPSHOT-runner.jar"]
EXPOSE 2137
