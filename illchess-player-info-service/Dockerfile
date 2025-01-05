FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17 as build

USER root

# copy wrapper
COPY illchess-player-info-server/.mvn .mvn
COPY illchess-player-info-server/mvnw mvnw
RUN chmod +x mvnw
RUN sed -i -e 's/\r$//' mvnw

# copy main pom
RUN mkdir illchess-player-info
COPY pom.xml pom.xml
# copy domain
RUN mkdir illchess-player-info/illchess-player-info-domain
COPY illchess-player-info/illchess-player-info-domain/pom.xml illchess-player-info/illchess-player-info-domain
COPY illchess-player-info/illchess-player-info-domain/src illchess-player-info/illchess-player-info-domain/src
# copy application
RUN mkdir illchess-player-info/illchess-player-info-application
COPY illchess-player-info/illchess-player-info-application/pom.xml illchess-player-info/illchess-player-info-application
COPY illchess-player-info/illchess-player-info-application/src illchess-player-info/illchess-player-info-application/src
# copy adapter
RUN mkdir illchess-player-info/illchess-player-info-adapter
COPY illchess-player-info/illchess-player-info-adapter/pom.xml illchess-player-info/illchess-player-info-adapter
COPY illchess-player-info/illchess-player-info-adapter/src illchess-player-info/illchess-player-info-adapter/src
# copy server
RUN mkdir illchess-player-info-server
COPY illchess-player-info-server/pom.xml illchess-player-info-server
COPY illchess-player-info-server/src illchess-player-info-server/src

RUN ./mvnw clean package -DskipTests -N
RUN ./mvnw clean package -DskipTests -Dnative

FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
USER root

COPY --from=build /project/illchess-player-info-server/target/illchess-player-info-server-runner /work/illchess-player-info-server

RUN chmod 775 /work /work/illchess-player-info-server \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

USER 1001

CMD ["./illchess-player-info-server", "-Dquarkus.http.host=0.0.0.0"]
