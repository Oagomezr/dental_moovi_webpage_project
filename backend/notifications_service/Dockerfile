FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=build/libs/notifications_service-0.0.1.jar
COPY ${JAR_FILE} notifications.jar
ENTRYPOINT [ "java", "-jar", "notifications.jar" ]