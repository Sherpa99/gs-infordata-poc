FROM adoptopenjdk/openjdk8-openj9:alpine-slim

#Installs curl
RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*

#Pull the jar from nexus repository
#RUN curl -f -L -o /infordata-dev.jar "http://nexus-dev.gs-cda-poc-2bef1f4b4097001da9502000c44fc2b2-0000.us-east.containers.appdomain.cloud/repository/nexus-infordata/com/infordata/infordata-poc-app/0.0.1-SNAPSHOT/infordata-poc-app-0.0.1-20200901.182349-4.jar"
COPY target/infordata-poc-app-0.0.1-SNAPSHOT.jar /
ENTRYPOINT ["java","-jar", "infordata-poc-app-0.0.1-SNAPSHOT.jar" ]
