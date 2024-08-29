FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

RUN apt-get update && \
    apt-get upgrade && \
    # 불필요한 패키지를 설치하지 않음
    apt-get install -y --no-install-recommends netcat && \
    # apt cache 삭제
    rm -rf /var/lib/apt/lists/*