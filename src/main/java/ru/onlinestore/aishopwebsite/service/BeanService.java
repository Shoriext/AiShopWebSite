package ru.onlinestore.aishopwebsite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class BeanService {

    public static void buyProduct(String phone, String productName, String size) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        HttpClient client = HttpClient.newHttpClient();
        rootNode.put("phone", phone);
        rootNode.put("productName", productName);
        rootNode.put("size", size);
        String jsonToMember = mapper.writeValueAsString(rootNode);

        HttpRequest requestToMembers = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8000/notify-order"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonToMember))
                .build();


        client.send(requestToMembers, HttpResponse.BodyHandlers.ofString());
    }
}
