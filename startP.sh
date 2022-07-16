#!/bin/sh

JAVA_D_PARAMS="
  -Dspring.profiles.active=prod
"

nohup java -jar "${JAVA_D_PARAMS}" \
 NotifyService.jar \
 >> "${CONSOLE_LOG_FILE_NAME}" > 2>&1 &

exit 0
