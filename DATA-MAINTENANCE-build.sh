#! /bin/bash
docker network create mongo-net

docker build -t="itad/data-maintenance:v1" .
