FROM openjdk:8-alpine

#ARG PROXY_HOST
#ARG PROXY_PORT

RUN apk update
RUN apk add maven

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY . /usr/src/app

#RUN mkdir -p /root/.m2
#RUN echo '\n\
#<settings> \n\
#    <proxies> \n\
#        <proxy> \n\
#            <active>true</active> \n\
#             <protocol>http</protocol> \n\
#             <host>${PROXY_HOST}</host> \n\
#             <port>${PROXY_PORT}</port> \n\
#             <nonProxyHosts>ill.fr,ill.eu</nonProxyHosts> \n\
#        </proxy> \n\
#    </proxies> \n\
#</settings>' > /root/.m2/settings.xml

#RUN mvn package -B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn -Dhttp.proxyHost=${PROXY_HOST} -Dhttp.proxyPort=${PROXY_PORT} -Dhttps.proxyHost=${PROXY_HOST} -Dhttps.proxyPort=${PROXY_PORT} -DskipTests=true
RUN mvn package -B -DskipTests=true

CMD java -jar application/target/desktop-service.jar server configuration.yml

