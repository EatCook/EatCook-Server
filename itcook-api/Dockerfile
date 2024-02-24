FROM openjdk:17

EXPOSE 8080

ARG JAR_FILE=build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ARG PROFILE=local
ENV PROFILE=${PROFILE}

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]