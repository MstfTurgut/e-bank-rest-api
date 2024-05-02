FROM openjdk:17-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:17-slim
WORKDIR e-bank
COPY --from=build target/*.jar e-bank.jar
ENTRYPOINT ["java", "-jar", "e-bank.jar"]