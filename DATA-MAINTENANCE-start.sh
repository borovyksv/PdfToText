#! /bin/bash

docker run -p 8089:8080 --name data-maintenance  \
    --network=mongo-net  \
    -v /tmp/original:/tmp/original \
    -v /tmp/copy:/tmp/copy \
    itad/data-maintenance:v1
