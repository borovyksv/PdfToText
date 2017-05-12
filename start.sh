#! /bin/bash

set -e

docker-compose stop
docker-compose rm -v --force

mvn clean package -DskipTests docker:build

docker-compose up