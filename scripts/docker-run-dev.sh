#!/bin/bash

docker run \
    -p 8195:8195\
    -e ENVIRONMENT=dev\
    --env-file ./.env.dev\
    -v ~/.postgresql/root.crt:/root/.postgresql/root.crt\
    sharingmap:latest
