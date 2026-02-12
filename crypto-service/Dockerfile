FROM openjdk:25-ea-25-jdk-slim
LABEL maintainer=Attila
COPY application/target/application-0.0.2-SNAPSHOT.jar application-0.0.2-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/application-0.0.2-SNAPSHOT.jar"]