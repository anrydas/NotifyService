#!/bin/sh

CONSOLE_LOG_FILE_NAME='logs/console.log'

if  [ -z ${CONSOLE_LOG_FILE_NAME} ]; then
  CONSOLE_LOG_FILE_NAME='/dev/null'
fi

JAVA_D_PARAMS="
  -Dspring.profiles.active=prod
  -Dlogging.file.name=logs/Notifier.log
"

nohup java -jar ${JAVA_D_PARAMS} NotifyService.jar >> ${CONSOLE_LOG_FILE_NAME} &

exit 0

