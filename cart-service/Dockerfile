# cart-service/Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/cart-service-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]