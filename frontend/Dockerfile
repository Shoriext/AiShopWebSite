# cart-service/Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/frontend-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]