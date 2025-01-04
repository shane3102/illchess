FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17 as buildApp

USER root

# copy wrapper
COPY illchess-stockfish-server/.mvn .mvn
COPY illchess-stockfish-server/mvnw mvnw

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

RUN ./mvnw -f /usr/src/app/illchess clean package -DskipTests -N
RUN ./mvnw -f /usr/src/app/illchess clean package -DskipTests -Dnative

FROM registry.access.redhat.com/ubi8/ubi-minimal:8.10 as buildStockfish
ARG PV=17

RUN microdnf install make gcc-c++ wget ca-certificates tar gzip

RUN mkdir -p /root/tmp && \
	cd /root/tmp && \
	wget https://github.com/official-stockfish/Stockfish/archive/sf_${PV}.tar.gz && \
	tar xvf /root/tmp/sf_${PV}.tar.gz && \
	cd /root/tmp/Stockfish-sf_${PV}/src && \
	make build ARCH=x86-64-modern && \
	mv /root/tmp/Stockfish-sf_${PV}/src/stockfish /usr/local/bin/stockfish

RUN rm -rf /root/tmp

FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
USER root

COPY --from=buildStockfish /usr/local/bin/stockfish /usr/local/bin/stockfish
ENV PATH="$PATH:/usr/local/bin/stockfish"

COPY --from=buildApp /usr/src/app/illchess/illchess-stockfish-server/target/illchess-stockfish-server-runner /work/illchess-stockfish-server

RUN chmod 775 /work /work/illchess-stockfish-server \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

EXPOSE 2137
USER 1001

CMD ["./illchess-stockfish-server", "-Dquarkus.http.host=0.0.0.0"]
