#FROM openjdk:8u131-jre-alpine
#RUN apk update && apk upgrade && apk add bash
FROM openjdk:8u151-jre-slim
COPY wait-for-it.sh /wait-for-it.sh
COPY run-dnam-functional-acceptance-tests.sh /run-dnam-functional-acceptance-tests.sh
COPY build/libs/ /build/libs
ENTRYPOINT ["/bin/bash","./run-dnam-functional-acceptance-tests.sh"]