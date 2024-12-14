FROM maven:3.9-eclipse-temurin-17-alpine

RUN mkdir -p /usr/src/app/messaging/inbox-outbox
COPY inbox-outbox/pom.xml /usr/src/app/messaging/inbox-outbox

# copy commons
RUN mkdir /usr/src/app/messaging/inbox-outbox/inbox-outbox-commons
COPY inbox-outbox/inbox-outbox-commons/pom.xml /usr/src/app/messaging/inbox-outbox/inbox-outbox-commons
COPY inbox-outbox/inbox-outbox-commons/src /usr/src/app/messaging/inbox-outbox/inbox-outbox-commons/src

# copy spring impl
RUN mkdir /usr/src/app/messaging/inbox-outbox/inbox-outbox-spring
COPY inbox-outbox/inbox-outbox-spring/pom.xml /usr/src/app/messaging/inbox-outbox/inbox-outbox-spring
COPY inbox-outbox/inbox-outbox-spring/src /usr/src/app/messaging/inbox-outbox/inbox-outbox-spring/src

# copy quarkus impl
RUN mkdir /usr/src/app/messaging/inbox-outbox/inbox-outbox-quarkus
COPY inbox-outbox/inbox-outbox-quarkus/pom.xml /usr/src/app/messaging/inbox-outbox/inbox-outbox-quarkus

RUN mkdir /usr/src/app/messaging/inbox-outbox/inbox-outbox-quarkus/deployment
COPY inbox-outbox/inbox-outbox-quarkus/deployment/pom.xml /usr/src/app/messaging/inbox-outbox/inbox-outbox-quarkus/deployment
COPY inbox-outbox/inbox-outbox-quarkus/deployment/src /usr/src/app/messaging/inbox-outbox/inbox-outbox-quarkus/deployment/src

RUN mkdir /usr/src/app/messaging/inbox-outbox/inbox-outbox-quarkus/runtime
COPY inbox-outbox/inbox-outbox-quarkus/runtime/pom.xml /usr/src/app/messaging/inbox-outbox/inbox-outbox-quarkus/runtime
COPY inbox-outbox/inbox-outbox-quarkus/runtime/src /usr/src/app/messaging/inbox-outbox/inbox-outbox-quarkus/runtime/src

RUN mvn -f /usr/src/app/messaging/inbox-outbox clean install -N
RUN mvn -f /usr/src/app/messaging/inbox-outbox clean install