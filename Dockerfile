
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/review-0.0.1-SNAPSHOT.jar review-service.jar

EXPOSE 10100

ENTRYPOINT ["java", "-jar", "review-service.jar"]

ENV TZ=Asia/Seoul