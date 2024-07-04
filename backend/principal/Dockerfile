FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/website-0.0.1.jar
COPY ${JAR_FILE} dentalmoovi.jar
ENTRYPOINT [ "java", "-jar", "dentalmoovi.jar" ]