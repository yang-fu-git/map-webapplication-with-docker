FROM gradle:7.4.2-jdk8 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew shadowJar --no-daemon 

# FROM openjdk:8-jre-slim

EXPOSE 8080
RUN ls build/libs/
ENTRYPOINT ["java", "-Xmx6g", "-cp", "build/libs/MapServer-0.0.1-SNAPSHOT-all.jar", "org.programmierprojekt.server.MapServerApplication","bw"]