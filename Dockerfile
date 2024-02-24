FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/cooperaApp-0.0.1-SNAPSHOT.jar cooperaApp.jar
RUN --mount=type=secret,id=_secret.properties,dst=/etc/secrets/.env cat /etc/secrets/.secret.properties
EXPOSE  8081
ENTRYPOINT ["java", "-jar","cooperaApp.jar"]
