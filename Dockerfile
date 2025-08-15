# Используем OpenJDK 17 как базовый образ
FROM openjdk:17-jdk-slim
# Указываем рабочую директорию
WORKDIR /app
# Копируем JAR файл из target
COPY target/AiShopWebSite-0.0.1-SNAPSHOT.jar app.jar
# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]