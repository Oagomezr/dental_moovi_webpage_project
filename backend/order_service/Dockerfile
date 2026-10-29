FROM openjdk:17-jdk-slim

# Install the necesary dependencies to fonts
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

ARG JAR_FILE=build/libs/order_service-0.0.1.jar
COPY ${JAR_FILE} orders.jar

# execute app in headless mode
ENTRYPOINT [ "java", "-Djava.awt.headless=true", "-jar", "orders.jar" ]