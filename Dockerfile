FROM openjdk:8u102-jre
MAINTAINER wangfeng_1@ebupt.com
VOLUME /tmp
add /target/microservice-yellowpagelibbak-server-0.0.1.jar microservice-yellowpagelibbak-server-0.0.1.jar
EXPOSE 10022
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /microservice-yellowpagelibbak-server-0.0.1.jar"]
