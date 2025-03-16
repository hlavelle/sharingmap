#!/bin/bash

docker run \
    -p 8095:8095\
    -e ENVIRONMENT=test\
    --env-file ./.env.test\
    -v ~/.postgresql/root.crt:/root/.postgresql/root.crt\
    sharingmap:latest
