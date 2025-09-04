package ru.onlinestore.frontend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(@Value("${product.url:http://product-service:8083}") String productUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(productUrl)
                .build();
    }

    public record ProductDto(
            Long id,
            String name,
            String description,
            Double price,
            String imageUrl
    ) {}

    public List<ProductDto> getAllProducts() {
        return webClient.get()
                .uri("/api/products")
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .collectList()
                .block(); // синхронно, для Thymeleaf
    }

    public ProductDto getProductById(Long id) {
        return webClient.get()
                .uri("/api/products/{id}", id)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }
}