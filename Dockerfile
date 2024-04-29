# from gemini
#FROM openjdk:17-slim
#
#WORKDIR /app
#
#COPY . .
#
#RUN mvn clean package
#
#EXPOSE 8080
#
#CMD ["java", "-jar", "e-bank.jar"]

# from folksdev
FROM openjdk:17 AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package
FROM openjdk:11
WORKDIR e-bank
COPY --from=build target/*.jar e-bank.jar
ENTRYPOINT ["java", "-jar", "e-bank.jar"]