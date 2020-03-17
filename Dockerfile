FROM openjdk:8
ADD application/target/desktop-service.jar /data/desktop-service.jar
ADD configuration.yml /data/configuration.yml

CMD java -jar /data/desktop-service.jar server /data/configuration.yml

