#!/bin/bash
lockbox_output=$(yc lockbox payload get --id e6qflue1o0pd06f2en24)

export AWS_ACCESS_KEY_ID=$(echo "$lockbox_output" | awk '/AWS_ACCESS_KEY_ID/{getline; print $2}')
export AWS_SECRET_ACCESS_KEY=$(echo "$lockbox_output" | awk '/AWS_SECRET_ACCESS_KEY/{getline; print $2}')
export PG_CONNECTION_STRING=$(echo "$lockbox_output" | awk '/PROD_PG_CONN_STRING/{getline; print $2}')
export PG_USERNAME='sm_admin'
export PG_PASSWORD=$(echo "$lockbox_output" | awk '/PROD_PG_PASSWORD/{getline; print $2}')
export MAIL_PASSWORD=$(echo "$lockbox_output" | awk '/MAIL_PASSWORD/{getline; print $2}')
export APP_SECRET=$(echo "$lockbox_output" | awk '/PROD_APP_SECRET/{getline; print $2}')

cd ~/sm/sharingmap

./gradlew bootRun --args='--spring.profiles.active=prod'
