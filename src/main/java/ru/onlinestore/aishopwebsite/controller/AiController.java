package ru.onlinestore.aishopwebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.onlinestore.aishopwebsite.service.AiService;

import java.io.IOException;
import java.net.http.HttpResponse;


@RestController
public class AiController {

    @Autowired
    AiService aiService;

    @PostMapping("/ai/gen")
    public HttpResponse<String> gen() throws IOException, InterruptedException {
        return AiService.aiGenerateImg();
    }
}
