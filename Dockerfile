FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} shoutlink.jar

ENTRYPOINT ["java", "-jar", "shoutlink.jar", "--spring.profiles.active=prod"]
