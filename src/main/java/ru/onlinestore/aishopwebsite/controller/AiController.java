package ru.onlinestore.aishopwebsite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onlinestore.aishopwebsite.service.GenApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Controller
@RequestMapping("/ai")
public class AiController {

    private final GenApiClient genApiClient;

    public AiController(GenApiClient genApiClient) {
        this.genApiClient = genApiClient;
    }

    @GetMapping("/form")
    public String showForm() {
        return "ai/form"; // шаблон form.html
    }

    @GetMapping("/test")
    public String testConnection() {
        genApiClient.testConnection();
        return "Тест завершен, проверьте логи";
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateImage(@RequestParam String prompt, Model model) {
        try {
            String taskId;
            taskId = genApiClient.aiGenerateImg(prompt);

            String imageUrl = waitForImage(taskId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("imageUrl", imageUrl);

            return ResponseEntity.ok(response);
            // шаблон result.html
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Ошибка генерации: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    private String waitForImage(String taskId) throws InterruptedException {
        for (int i = 0; i < 60; i++) { // максимум 30 попыток × 2 сек = 60 сек
            try {
                Map<String, Object> status = genApiClient.getStatus(taskId);
                String responseStatus = (String) status.get("status");

                if ("success".equals(responseStatus)) {
                    List<Object> result = (List<Object>) status.get("result");
                    if (result != null && !result.isEmpty()) {
                        String url = ((String) result.get(0)).trim();

                        System.out.println(url);

                        return url;
                    }
                } else if ("error".equals(responseStatus)) {
                    throw new RuntimeException("Ошибка в генерации: " + status.get("error"));
                }
            } catch (Exception e) {
                System.err.println("Проверка статуса... ошибка: " + e.getMessage());
            }
            Thread.sleep(5000); // ждём 2 секунды
        }
        throw new RuntimeException("Таймаут ожидания изображения");
    }

}
