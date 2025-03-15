#!/bin/bash
lockbox_output=$(yc lockbox payload get --id e6qflue1o0pd06f2en24)

export AWS_ACCESS_KEY_ID=$(echo "$lockbox_output" | awk '/AWS_ACCESS_KEY_ID/{getline; print $2}')
export AWS_SECRET_ACCESS_KEY=$(echo "$lockbox_output" | awk '/AWS_SECRET_ACCESS_KEY/{getline; print $2}')
export PG_CONNECTION_STRING=$(echo "$lockbox_output" | awk '/DEV_PG_CONN_STRING/{getline; print $2}')
export PG_USERNAME='sm_test_admin'
export PG_PASSWORD=$(echo "$lockbox_output" | awk '/DEV_PG_PASS/{getline; print $2}')
export MAIL_PASSWORD=$(echo "$lockbox_output" | awk '/MAIL_PASSWORD/{getline; print $2}')
export APP_SECRET=$(echo "$lockbox_output" | awk '/TEST_APP_SECRET/{getline; print $2}')
export TG_BOT_SECRET_TOKEN=$(echo "$lockbox_output" | awk '/TELEGRAM_BOT_SECRET_TOKEN/{getline; print $2}')

cd ~/sm/test/sharingmap

./gradlew bootRun --args='--spring.profiles.active=test'
