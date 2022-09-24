#!/bin/sh

SCRIPT_PATH=$( cd -- $(dirname "$0") > /dev/null 2>&1 || exit ; pwd -P )

# Set parameters and environment variables
. "${SCRIPT_PATH}/setEnv.sh"

JAVA_D_PARAMS="
  -Dserver.port=${APP_PORT}
  -Dlogging.file.name=${LOG_FILE_NAME}
  -Dlogging.level.das=${LOG_LEVEL}
  -Dapp.api.key.parameter=${}
  -Dapp.api.key=${API_KEY_VALUE}
  -Dtg.baseUrl=${TG_URL}
  -Dtg.api.key=${TG_API_KEY}
  -Dtg.chatId=${TG_CHAT_ID}
  -Dvb.api.key=${VB_API_KEY}
  -Dvb.media.folder=${VB_MEDIA_FOLDER}
  -DbaseUrl=${MX_BASE_URL}
  -Dmx.bot.user=${MX_USER}
  -Dmx.bot.pass=${MX_USER_PASS}
  -Dmx.bot.default.roomid=${MX_ROOM_ID}
  -Dspring.mail.host=${EML_SMTP}
  -Dspring.mail.port=${EML_PORT}
  -Dspring.mail.username=${EML_USER}
  -Dspring.mail.password=${EML_PASS}
  -Dspring.mail.properties.mail.smtp.auth=${EML_SMPT_AUTH}
  -Dspring.mail.properties.mail.smtp.starttls.enable=${EML_TLS}
  -Deml.from.addr=${EML_FROM}
  -Deml.to.addr=${EML_TO}
"

nohup java -jar "${JAVA_D_PARAMS}"  NotifyService.jar >> "${CONSOLE_LOG_FILE_NAME}" > 2>&1 &

exit 0
