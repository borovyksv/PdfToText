#! /bin/bash

#compile project
mvn clean package -DskipTests

#copy Dockerfile to compiled file and RUN
cp Dockerfile target/
cd target/
docker build -t="itad/data-maintenance:v1" .
