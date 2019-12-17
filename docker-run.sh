#!/usr/bin/env bash

docker run -d \
    --name microservice-yellowpagelibbak-server \
    -p 10022:10022 \
    --env JAVA_OPTS=-Xmx256M \
    hub.ebupt.com/txcyapi/microservice-yellowpagelibbak-server:0.0.1