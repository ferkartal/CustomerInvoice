FROM maven:3.6.0 AS build
MAINTAINER Nur Erkartal
WORKDIR /temp
COPY pom.xml /temp/
COPY src /temp/src/
RUN mvn verify

FROM openjdk:11
WORKDIR /temp
COPY --from=build /temp/target/app.jar invoice-api.jar
ENTRYPOINT ["java", "-jar", "invoice-api.jar"]