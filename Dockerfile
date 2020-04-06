FROM openjdk:jdk-buster
VOLUME /tmp
ADD ./build/libs/shortenurl-0.1.jar shortenurl.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/shortenurl.jar"]

EXPOSE 8080