FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/notifications_service-0.0.1.jar
COPY ${JAR_FILE} notifications.jar
ENTRYPOINT [ "java", "-jar", "notifications.jar" ]