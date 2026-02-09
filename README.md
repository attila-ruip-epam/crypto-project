# Crypto recommendation service

## Modules

- **api:** Contains crypto-api.yaml. Interface generated from it for Controllers.
- **application:** Application entry point. Rate limiting (set only for one endpoint as POC). Configuration for allowed
  cryptos.
- **controller:** Controller layer, REST endpoints, conversion to api model via MapStruct, validation, error handling.
- **integration-test:** MockMvc spring boot tests for all endpoints.
- **service:** Business logic/service layer. Csv loading.
- **chart:** Helm chart

## Coverage report

Command on root pom level **mvn clean verify** generates aggregated jacoco coverage report in
/integration-test/tasrget/site/jacoco-aggregate directory.

## Technology stack

- JDK 25
- Spring Boot 4
- Maven

## Dockerize

- run **mvn clean package**
- run **docker build --tag=crypto-app:0.0.2 .**

//TODO unfortunately due to a likely version incompatibility/plugin issue **docker run crypto-app:0.0.2** is not working
right now, might not be able to fix until deadline. Don't use latest versions

## Helm

- helm chart created via **helm create chart/crypto-app**
- installed via **helm install crypto-app chart/crypto-app**
- if kubectl installed, check via **kubectl get pods**
- uninstall via **helm uninstall crypto-app**

