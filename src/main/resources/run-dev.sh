#!/bin/bash

export AWS_ACCESS_KEY_ID=''
export AWS_SECRET_ACCESS_KEY=''
export PG_USERNAME=''
export MAIL_PASSWORD=''
export APP_SECRET=''
export DEV_PG_PASS=''
export TG_BOT_SECRET_TOKEN=''

java -jar /app/application.jar --spring.profiles.active=dev
