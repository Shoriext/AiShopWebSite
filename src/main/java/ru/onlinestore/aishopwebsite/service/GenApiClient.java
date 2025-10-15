package ru.onlinestore.aishopwebsite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class GenApiClient {

    private static final Logger logger = LoggerFactory.getLogger(GenApiClient.class);

    @Value("${api.key}")
    private String API_KEY;

    // Используем локальный прокси через Nginx
    private static final String GEN_API_BASE_URL = "https://merchii.ru/gen-api";
    private static final String STATUS_URL = GEN_API_BASE_URL + "/api/v1/request/get/";
    private static final String GENERATE_URL = GEN_API_BASE_URL + "/api/v1/networks/kling-image";

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String aiGenerateImg(String prompt) throws IOException, InterruptedException, JsonProcessingException {
        logger.info("=== НАЧАЛО ГЕНЕРАЦИИ ИЗОБРАЖЕНИЯ ===");
        logger.info("Prompt: {}", prompt);

        ObjectNode input = objectMapper.createObjectNode();
        input.put("prompt", prompt);

        String json = objectMapper.writeValueAsString(input);
        logger.info("JSON тело запроса: {}", json);
        logger.info("URL запроса: {}", GENERATE_URL);
        logger.info("API Key: {}...", API_KEY.substring(0, Math.min(10, API_KEY.length())));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GENERATE_URL))
                .headers(
                        "Content-Type", "application/json",
                        "Accept", "application/json",
                        "Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        logger.info("Заголовки запроса:");
        logger.info("  Content-Type: application/json");
        logger.info("  Accept: application/json");
        logger.info("  Authorization: Bearer {}...", API_KEY.substring(0, Math.min(10, API_KEY.length())));

        long startTime = System.currentTimeMillis();
        logger.info("Отправка запроса...");

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long responseTime = System.currentTimeMillis() - startTime;

            logger.info("=== ОТВЕТ ОТ API ===");
            logger.info("Время ответа: {} мс", responseTime);
            logger.info("HTTP статус: {}", response.statusCode());
            logger.info("Тело ответа: {}", response.body());
            logger.info("Заголовки ответа: {}", response.headers().map());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                JsonNode root = objectMapper.readTree(response.body());
                logger.info("Парсинг JSON ответа: {}", root.toPrettyString());

                if (root.has("request_id")) {
                    String requestId = root.get("request_id").asText();
                    logger.info("Успешно получен request_id: {}", requestId);
                    logger.info("=== ЗАВЕРШЕНИЕ ГЕНЕРАЦИИ ===");
                    return requestId;
                } else {
                    logger.error("Ответ не содержит ID задачи. Полный ответ: {}", root.toPrettyString());
                    throw new RuntimeException("Ответ не содержит ID задачи");
                }
            } else {
                logger.error("Ошибка HTTP {}: {}", response.statusCode(), response.body());
                throw new RuntimeException("Ошибка генерации: HTTP " + response.statusCode() + " " + response.body());
            }
        } catch (Exception e) {
            logger.error("Исключение при выполнении запроса: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Map<String, Object> getStatus(String taskId) throws Exception {
        logger.info("=== ЗАПРОС СТАТУСА ЗАДАЧИ ===");
        logger.info("Task ID: {}", taskId);

        String statusUrl = STATUS_URL + taskId;
        logger.info("URL статуса: {}", statusUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(statusUrl))
                .header("Authorization", "Bearer " + API_KEY)
                .GET()
                .build();

        logger.info("Заголовки запроса статуса:");
        logger.info("  Authorization: Bearer {}...", API_KEY.substring(0, Math.min(10, API_KEY.length())));

        long startTime = System.currentTimeMillis();
        logger.info("Отправка запроса статуса...");

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            long responseTime = System.currentTimeMillis() - startTime;

            logger.info("=== ОТВЕТ СТАТУСА ===");
            logger.info("Время ответа: {} мс", responseTime);
            logger.info("HTTP статус: {}", response.statusCode());
            logger.info("Тело ответа статуса: {}", response.body());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                logger.info("JSON ответ статуса: {}", root.toPrettyString());

                String status = root.has("status") ? root.get("status").asText() : null;
                if (status == null) {
                    logger.error("Ответ не содержит поля 'status'. Полный ответ: {}", root.toPrettyString());
                    throw new RuntimeException("Ответ не содержит поля 'status'");
                }

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("status", status);
                logger.info("Статус задачи: {}", status);

                if ("success".equals(status)) {
                    JsonNode resultNode = root.get("result");
                    if (resultNode != null && resultNode.isArray() && !resultNode.isEmpty()) {
                        String imageUrl = resultNode.get(0).asText().trim();
                        result.put("imageUrl", imageUrl);
                        result.put("result", List.of(imageUrl));
                        logger.info("Успешно! URL изображения: {}", imageUrl);
                    } else {
                        logger.error("Изображение не найдено в ответе. Result node: {}", resultNode);
                        throw new RuntimeException("Изображение не найдено в ответе");
                    }
                } else if ("error".equals(status) || "failed".equals(status)) {
                    String error = Optional.ofNullable(root.get("error"))
                            .map(JsonNode::asText)
                            .orElse("Неизвестная ошибка");
                    result.put("error", error);
                    logger.error("Ошибка задачи: {}", error);
                } else {
                    logger.info("Задача в процессе, статус: {}", status);
                }

                logger.info("=== ЗАВЕРШЕНИЕ ЗАПРОСА СТАТУСА ===");
                return result;

            } else {
                logger.error("Ошибка API статуса: {} {}", response.statusCode(), response.body());
                throw new RuntimeException("Ошибка API: " + response.statusCode() + " " + response.body());
            }
        } catch (Exception e) {
            logger.error("Исключение при запросе статуса: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Дополнительный метод для тестирования подключения
    public void testConnection() {
        logger.info("=== ТЕСТ ПОДКЛЮЧЕНИЯ ===");
        logger.info("Base URL: {}", GEN_API_BASE_URL);
        logger.info("API Key length: {}", API_KEY.length());

        try {
            HttpRequest testRequest = HttpRequest.newBuilder()
                    .uri(URI.create(GEN_API_BASE_URL + "/api/v1/networks"))
                    .header("Authorization", "Bearer " + API_KEY)
                    .GET()
                    .build();

            logger.info("Отправка тестового запроса...");
            HttpResponse<String> response = httpClient.send(testRequest, HttpResponse.BodyHandlers.ofString());
            logger.info("Тестовый запрос - Статус: {}, Тело: {}", response.statusCode(), response.body());

        } catch (Exception e) {
            logger.error("Ошибка тестового запроса: {}", e.getMessage(), e);
        }

        logger.info("=== ЗАВЕРШЕНИЕ ТЕСТА ===");
    }
}