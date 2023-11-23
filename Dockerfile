FROM openjdk:17-oracle
WORKDIR /opt/app
COPY target/libs/ libs/
COPY target/Login_send_mail_confirm-0.0.1-SNAPSHOT.jar run.jar
ENTRYPOINT ["java","-jar","run.jar"]