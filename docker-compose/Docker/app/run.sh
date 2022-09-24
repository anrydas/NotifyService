#!/bin/sh

JAVA_D_PARAMS="
  -Dserver.port=${TCP_PORT}
  -Dspring.profiles.active=${PROFILE}
  -Dlogging.file.name=${LOG_FILE_NAME}
"
export JAVA_APP_DIR=/deployments
export JAVA_APP_JAR=NotifyService.jar
export JAVA_OPTIONS=${JAVA_D_PARAMS}

/deployments/run-java.sh

exit 0
