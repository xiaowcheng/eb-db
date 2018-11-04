FROM openjdk:8u102-jre
MAINTAINER wangfeng_1@ebupt.com
VOLUME /tmp
add microservice-yellowpagelibbak-server-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8014
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
