#! /bin/bash
docker network create mongo-net

docker run -p 27017:27017 --name mongo \
    --network=mongo-net  \
    -v /data/db/:/data/db/ \
    mongo
