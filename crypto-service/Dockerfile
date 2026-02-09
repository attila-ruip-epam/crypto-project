FROM openjdk:25-ea-25-jdk-slim
LABEL maintainer=Attila
COPY application/target/application-0.0.1-SNAPSHOT.jar application-0.0.1-SNAPSHOT.jar
COPY application/target/application-0.0.1-SNAPSHOT-exec.jar application-0.0.1-SNAPSHOT-exec.jar
ENTRYPOINT ["java","-jar","/application-0.0.2-SNAPSHOT-exec.jar"]