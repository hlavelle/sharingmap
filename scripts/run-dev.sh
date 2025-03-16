#!/bin/bash
# run-dev.sh

set -x

# Load environment variables from .env.dev file
set -a  # automatically export all variables
source .env.dev
set +a

cd ../

# Run the application
./gradlew bootRun --args='--spring.profiles.active=dev'