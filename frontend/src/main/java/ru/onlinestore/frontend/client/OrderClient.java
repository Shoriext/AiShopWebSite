package ru.onlinestore.frontend.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OrderClient {
    private final String orderUrl = "http://order-service:8084";
    private final WebClient webClient;

    public OrderClient(WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder
                .baseUrl(orderUrl)
                .build();
    }

    public record CreateOrderRequest(
            String sessionId
//            String customerName,
//            String phone
    ) {}

    public void createOrder(String sessionId, String name, String phone) {
//        CreateOrderRequest request = new CreateOrderRequest(sessionId, name, phone);
        CreateOrderRequest request = new CreateOrderRequest(sessionId);
        webClient.post()
                .uri("/api/order/create")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
