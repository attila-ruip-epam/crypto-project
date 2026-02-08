# Crypto recommendation service

## Modules

- **api:** Contains crypto-api.yaml
- **application:** Application entry point. Rate limiting (set only for one endpoint as POC).
- **controller:** Controller layer, REST endpoints, conversion to api model.
- **integration-test:** MockMvc spring boot tests for all endpoints.
- **service:** Business logic/service layer.

## Coverage report

Command on root pom level **mvn clean verify** generates aggregated jacoco coverage report in
/integration-test/tasrget/site/jacoco-aggregate directory.

## Technology stack

- JDK 25
- Spring Boot 4
- Maven



