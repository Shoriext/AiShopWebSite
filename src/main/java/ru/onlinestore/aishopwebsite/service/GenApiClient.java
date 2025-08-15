package ru.onlinestore.aishopwebsite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

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

    @Value("${api.key}")
    private String API_KEY;

    private static final String STATUS_URL = "https://api.gen-api.ru/api/v1/request/get/";
    private static String URL = "https://api.gen-api.ru/api/v1/networks/kling-image";

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Gson gson = new Gson();

    public String aiGenerateImg(String prompt) throws IOException, InterruptedException, JsonProcessingException {

        ObjectNode input = objectMapper.createObjectNode();

        input.put("prompt", prompt);

        String json = objectMapper.writeValueAsString(input);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .headers(
                        "Content-Type", "application/json",
                        "Accept", "application/json",
                        "Authorization", "Bearer " + API_KEY
                )
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            JsonNode root = objectMapper.readTree(response.body());
            if (root.has("request_id")) {
                return root.get("request_id").asText();
            } else {
                throw new RuntimeException("Ответ не содержит ID задачи");
            }
        } else {
            throw new RuntimeException("Ошибка генерации: HTTP " + response.statusCode() + " " + response.body());
        }
    }

    public Map<String, Object> getStatus(String taskId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STATUS_URL + taskId))
                .header("Authorization", "Bearer "+ API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = objectMapper.readTree(response.body());

            String status = root.has("status") ? root.get("status").asText() : null;
            if (status == null) {
                throw new RuntimeException("Ответ не содержит поля 'status'");
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", status);

            if ("success".equals(status)) {
                // Извлекаем URL изображения
                JsonNode resultNode = root.get("result");
                if (resultNode != null && resultNode.isArray() && !resultNode.isEmpty()) {
                    String imageUrl = resultNode.get(0).asText().trim();
                    result.put("imageUrl", imageUrl);
                    result.put("result", List.of(imageUrl));
                } else {
                    throw new RuntimeException("Изображение не найдено в ответе");
                }
            } else if ("error".equals(status) || "failed".equals(status)) {
                String error = Optional.ofNullable(root.get("error"))
                        .map(JsonNode::asText)
                        .orElse("Неизвестная ошибка");
                result.put("error", error);
            }

            return result;

        } else {
            throw new RuntimeException("Ошибка API: " + response.statusCode() + " " + response.body());
        }
    }
}