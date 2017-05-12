#! /bin/bash

set -e

docker-compose stop
docker-compose rm -v --force

mvn clean package docker:build

docker-compose up