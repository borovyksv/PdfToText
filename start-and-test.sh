#! /bin/bash

#create dir for input files
mkdir -p /tmp/original
#create dir for output files
mkdir -p /tmp/copy

#compile project
mvn clean package

#copy Dockerfile to compiled file and RUN
cp Dockerfile target/
cd target/
docker build -t borovyksv/pdf_to_text .

#run Docker-compose
cd ..
docker-compose up
