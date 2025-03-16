#!/bin/bash

docker run \
    -p 8086:8086\
    -e ENVIRONMENT=prod\
    --env-file ./.env.prod\
    -v ~/.postgresql/root.crt:/root/.postgresql/root.crt\
    sharingmap:latest
