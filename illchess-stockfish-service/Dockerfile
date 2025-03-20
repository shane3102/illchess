FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17 as buildStockfishApp

USER root

# copy wrapper
COPY illchess-stockfish-server/.mvn .mvn
COPY illchess-stockfish-server/mvnw mvnw
RUN chmod +x mvnw
RUN sed -i -e 's/\r$//' mvnw

# copy main pom
RUN mkdir illchess-stockfish
COPY pom.xml pom.xml
# copy domain
RUN mkdir illchess-stockfish/illchess-stockfish-domain
COPY illchess-stockfish/illchess-stockfish-domain/pom.xml illchess-stockfish/illchess-stockfish-domain
COPY illchess-stockfish/illchess-stockfish-domain/src illchess-stockfish/illchess-stockfish-domain/src
# copy application
RUN mkdir illchess-stockfish/illchess-stockfish-application
COPY illchess-stockfish/illchess-stockfish-application/pom.xml illchess-stockfish/illchess-stockfish-application
COPY illchess-stockfish/illchess-stockfish-application/src illchess-stockfish/illchess-stockfish-application/src
# copy adapter
RUN mkdir illchess-stockfish/illchess-stockfish-adapter
COPY illchess-stockfish/illchess-stockfish-adapter/pom.xml illchess-stockfish/illchess-stockfish-adapter
COPY illchess-stockfish/illchess-stockfish-adapter/src illchess-stockfish/illchess-stockfish-adapter/src
# copy server
RUN mkdir illchess-stockfish-server
COPY illchess-stockfish-server/pom.xml illchess-stockfish-server
COPY illchess-stockfish-server/src illchess-stockfish-server/src

RUN ls
RUN ./mvnw clean package -DskipTests -N
RUN ./mvnw clean package -DskipTests -Dnative -Dquarkus.native.additional-build-args="--initialize-at-run-time=com.sun.jna"


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

COPY --from=buildStockfishApp /project/illchess-stockfish-server/target/illchess-stockfish-server-runner /work/illchess-stockfish-server

RUN chmod 775 /work /work/illchess-stockfish-server \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

EXPOSE 2137
USER 1001

CMD ["./illchess-stockfish-server", "-Dquarkus.http.host=0.0.0.0"]
