FROM openjdk:17-jdk-slim
WORKDIR /app
ARG JAR_FILE=./build/libs
COPY ${JAR_FILE}/*.jar app.jar
ENTRYPOINT ["java", "-jar", "./app.jar"]
ENV TZ=Asia/Seoul