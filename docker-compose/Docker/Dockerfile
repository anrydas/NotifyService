FROM fabric8/java-alpine-openjdk8-jre

COPY app/application-dev.properties /deployments/application-dev.properties
COPY app/NotifyService.jar /deployments/NotifyService.jar
COPY app/run.sh /deployments/run.sh

RUN chmod 777 /deployments/run.sh
ENTRYPOINT ["/deployments/run.sh"]
