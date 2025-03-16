#!/bin/bash

set -x

cat > ./.env.dev << EOF                                                     
AWS_ACCESS_KEY_ID=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key AWS_ACCESS_KEY_ID)
AWS_SECRET_ACCESS_KEY=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key AWS_SECRET_ACCESS_KEY)
PG_CONNECTION_STRING=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key DEV_PG_CONN_STRING)
PG_PASSWORD=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key DEV_PG_PASS)
PG_USERNAME=sm_test_admin
MAIL_PASSWORD=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key MAIL_PASSWORD)
APP_SECRET=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key TEST_APP_SECRET)
TG_BOT_SECRET_TOKEN=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key TELEGRAM_BOT_SECRET_TOKEN)
EOF

cat > ./.env.test << EOF                                                     
AWS_ACCESS_KEY_ID=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key AWS_ACCESS_KEY_ID)
AWS_SECRET_ACCESS_KEY=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key AWS_SECRET_ACCESS_KEY)
PG_CONNECTION_STRING=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key DEV_PG_CONN_STRING)
PG_PASSWORD=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key DEV_PG_PASS)
PG_USERNAME=sm_test_admin
MAIL_PASSWORD=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key MAIL_PASSWORD)
APP_SECRET=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key TEST_APP_SECRET)
TG_BOT_SECRET_TOKEN=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key TELEGRAM_BOT_SECRET_TOKEN)
EOF

cat > ./.env.prod << EOF
AWS_ACCESS_KEY_ID=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key AWS_ACCESS_KEY_ID)
AWS_SECRET_ACCESS_KEY=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key AWS_SECRET_ACCESS_KEY)
PG_CONNECTION_STRING=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key PROD_PG_CONN_STRING)
PG_PASSWORD=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key PROD_PG_PASSWORD)
MAIL_PASSWORD=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key MAIL_PASSWORD)
PG_USERNAME=sm_admin
APP_SECRET=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key PROD_APP_SECRET)
TG_BOT_SECRET_TOKEN=$(yc lockbox payload get --id e6qflue1o0pd06f2en24 --key TELEGRAM_BOT_SECRET_TOKEN)
EOF

chmod 600 ./.env.dev
chmod 600 ./.env.test
chmod 600 ./.env.prod
