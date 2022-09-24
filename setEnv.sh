#!/bin/sh

### Logs ###
LOG_LEVEL='info'
CONSOLE_LOG_FILE_NAME='logs/console.log'
LOG_FILE_NAME='/var/log/Notifier.log'
### Application ###
API_KEY_PARAMETER_NAME='api-key'
API_KEY_VALUE='Ffe9QA7S563kp9rUe6uCHewEft4UznAhYZybMezmnhBPMXrb3wqzmTFcC3NgFBdG'
APP_PORT='23445'
### Telegramm parameters
TG_URL='https://api.telegram.org'
TG_API_KEY='You_TG_API-key_here'
TG_CHAT_ID='You_TG_chat_ID_here'
### Viber parameters
VB_API_KEY='You_VB_API-key_here'
VB_MEDIA_FOLDER='media.tmp'
### Matrix parameters
MX_BASE_URL='You_Matrix_server_URL_here'
MX_USER='You_Matrix_BOT_user_name'
MX_USER_PASS='You_Matrix_BOT_user_Password'
MX_ROOM_ID='You_Matrix_Default_Room_ID'
### E-mail parameters
EML_SMTP='smtp.gmail.com'
EML_PORT='587'
EML_USER='You_e-mail_user-name'
EML_PASS='You_e-mail_user_password'
EML_SMPT_AUTH='true'
EML_TLS='true'
EML_FROM='You_e-mail_from'
EML_TO='You_e-mail_to'

if  [ -z ${CONSOLE_LOG_FILE_NAME} ]; then
  CONSOLE_LOG_FILE_NAME='/dev/null'
fi
