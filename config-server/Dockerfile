FROM eclipse-temurin:21-jdk
ARG JAR_FILE=build/libs/*.jar
EXPOSE 8888
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]


# docker container run -p 8888:8888 --name helpdesk-config-server -e CONFIG_SERVER_URI=GITREPO -e CONFIG_SERVER_USERNAME=USER -e CONFIG_SERVER_PASSWORD=PASS rivald/backend-expert-configs:latest