package ru.onlinestore.aishopwebsite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onlinestore.aishopwebsite.service.GenApiClient;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final GenApiClient genApiClient;


    @GetMapping("/form")
    public String showForm() {
        return "ai/form"; // шаблон form.html
    }

    @PostMapping("/generate")
    public String generateImage(@RequestParam String prompt, Model model) {
        try {
            String taskId = genApiClient.aiGenerateImg(prompt);
            String imageUrl = waitForImage(taskId);


            model.addAttribute("imageUrl", imageUrl);
            return "ai/result"; // шаблон result.html
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка генерации: " + e.getMessage());
            return "ai/form";
        }
    }

    private String waitForImage(String taskId) throws InterruptedException {
        for (int i = 0; i < 30; i++) { // максимум 30 попыток × 2 сек = 60 сек
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
                // Логируем, но продолжаем
                System.err.println("Проверка статуса... ошибка: " + e.getMessage());
            }
            Thread.sleep(2000); // ждём 2 секунды
        }
        throw new RuntimeException("Таймаут ожидания изображения");
    }

}
