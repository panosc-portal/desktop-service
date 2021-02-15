FROM openjdk:8-alpine

ARG MAVEN_OPTS

RUN apk update
RUN apk add maven

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY . /usr/src/app
RUN mvn package -B -DskipTests=true

ENV MAVEN_OPTS ${MAVEN_OPTS}

CMD java -jar application/target/desktop-service.jar server configuration.yml

