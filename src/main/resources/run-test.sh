#!/bin/bash
lockbox_output=$(yc lockbox payload get --id e6qflue1o0pd06f2en24)

# Retrieve the secret using the lockbox command and export it as an environment variable
export AWS_ACCESS_KEY_ID=$(echo "$lockbox_output" | awk '/AWS_ACCESS_KEY_ID/{getline; print $2}')
export AWS_SECRET_ACCESS_KEY=$(echo "$lockbox_output" | awk '/AWS_SECRET_ACCESS_KEY/{getline; print $2}')
export PG_CONNECTION_STRING=$(echo "$lockbox_output" | awk '/DEV_PG_CONN_STRING/{getline; print $2}')
export PG_USERNAME='sm_test_admin'
export PG_PASSWORD=$(echo "$lockbox_output" | awk '/DEV_PG_PASS/{getline; print $2}')
export MAIL_PASSWORD=$(echo "$lockbox_output" | awk '/MAIL_PASSWORD/{getline; print $2}')
export APP_SECRET=$(echo "$lockbox_output" | awk '/TEST_APP_SECRET/{getline; print $2}')

# Navigate to your Gradle project directory
cd ~/sm/sharingmap

# Run the Gradle build with the environment variable passed as a property
./gradlew bootRun --args='--spring.profiles.active=dev'
