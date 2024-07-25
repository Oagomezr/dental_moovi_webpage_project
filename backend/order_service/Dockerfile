FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/order_service-0.0.1.jar
COPY ${JAR_FILE} orders.jar
ENTRYPOINT [ "java", "-jar", "orders.jar" ]