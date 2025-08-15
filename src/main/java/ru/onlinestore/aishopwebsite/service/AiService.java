package ru.onlinestore.aishopwebsite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    @Value("${api.key}")
    private static String API_KEY;

    private static String URL = "https://api.gen-api.ru/api/v1/networks/kling-image";

    private static final Gson gson = new Gson();

    public static String aiGenerateImg(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode input = mapper.createObjectNode();

        input.put( "prompt", prompt);

        String json = mapper.writeValueAsString(input);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .headers(
                        "Content-Type", "application/json" ,
                        "Accept", "application/json" ,
                        "Authorization", "Bearer " + API_KEY
                        )
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), HashMap.class).get("id").toString();
        } else {
            throw new RuntimeException("Ошибка: " + response.body());
        }
    }

    public Map<String, Object> getStatus(String taskId) throws Exception {
        String url = URL + "/" + taskId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + API_KEY)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Пример ответа:
            // {"id":"task_123","status":"ready","image_url":"https://...","time":18}
            return gson.fromJson(response.body(), HashMap.class);
        } else {
            throw new RuntimeException("Ошибка проверки статуса: " + response.body());
        }
    }
}
