FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/website-0.0.2.jar
COPY ${JAR_FILE} dentalmoovi.jar
ENTRYPOINT [ "java", "-jar", "dentalmoovi.jar" ]