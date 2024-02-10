FROM maven:3.8.5-openjdk-17 AS build
COPY . .

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/cooperaApp-0.0.1-SNAPSHOT.jar cooperaApp.jar
EXPOSE  8081
ENTRYPOINT ["java", "-jar","cooperaApp.jar"]
