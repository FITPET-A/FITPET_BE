FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y --no-install-recommends netcat && \
    rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["java", "-jar", "app.jar"]