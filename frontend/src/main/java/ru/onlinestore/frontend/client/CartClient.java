package ru.onlinestore.frontend.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.channels.MembershipKey;
import java.time.Duration;
import java.util.Map;

@Component
public class CartClient {

    private final WebClient webClient;

    public CartClient() {
        String cartServiceUrl = "http://cart-service:8082";
        this.webClient = WebClient.builder()
                .baseUrl(cartServiceUrl)
                .build();
    }

    public record CartDto(
            String sessionId,
            Map<String, Integer> items,
            int totalItems
    ) {
    }

    public Mono<CartDto> getCart(String sessionId) {
        return webClient.get()
                .uri("/api/cart/{sessionId}", sessionId)
                .retrieve()
                .bodyToMono(CartDto.class)
                .timeout(Duration.ofSeconds(3)) // ✅ Теперь работает
                .onErrorReturn(new CartDto(sessionId, Map.of(), 0)); // заглушка при ошибке
    }
}
