FROM openjdk:8u151-jre-slim

COPY wait-for-it.sh /wait-for-it.sh
COPY build/libs/ /build/libs
COPY run-dnam-functional-acceptance-tests.sh /run-dnam-functional-acceptance-tests.sh
ENTRYPOINT ["/bin/bash","./run-dnam-functional-acceptance-tests.sh"]