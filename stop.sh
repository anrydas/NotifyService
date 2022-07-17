#!/bin/sh

PID=`ps ax | grep -m1 'NotifyService.jar' | awk '{print $1}'`
if [ -n "${PID}" ];then
    kill -9 ${PID}
    echo "Notify Service with PID=${PID} was stopped"
fi
