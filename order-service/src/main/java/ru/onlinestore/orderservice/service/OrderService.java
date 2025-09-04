package ru.onlinestore.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.onlinestore.orderservice.model.Order;
import ru.onlinestore.orderservice.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private final WebClient cartClient;

    // Внедряем cart-service URL через конфигурацию
    public OrderService(WebClient.Builder webClientBuilder) {
        this.cartClient = webClientBuilder
                .baseUrl("http://cart-service:8082") // ← URL cart-service в Docker
                .build();
    }

    /**
     * Создаёт заказ на основе корзины пользователя
     */
    public Order createOrder(String sessionId) {
        // 1. Получаем корзину из cart-service
        CartDto cart = getCartFromCartService(sessionId);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order: cart is empty");
        }

        // 2. Вычисляем сумму
        double total = calculateTotal(cart.getItems());

        // 3. Создаём заказ
        Order order = new Order();
        order.setSessionId(sessionId);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotal(total);
        order.setStatus("NEW");
        order.setItems(cart.getItems());

        // 4. Сохраняем заказ
        Order savedOrder = orderRepository.save(order);

        // 5. Очищаем корзину
        clearCartInCartService(sessionId);

        return savedOrder;
    }

    /**
     * Получает корзину из cart-service
     */
    private CartDto getCartFromCartService(String sessionId) {
        return cartClient.get()
                .uri("/api/cart/{sessionId}", sessionId)
                .retrieve()
                .bodyToMono(CartDto.class)
                .block(); // синхронный вызов (для простоты)
    }

    /**
     * Очищает корзину в cart-service
     */
    private void clearCartInCartService(String sessionId) {
        cartClient.delete()
                .uri("/api/cart/{sessionId}/clear", sessionId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    /**
     * Вычисляет общую стоимость заказа
     * В реальном проекте здесь будет вызов product-service для получения цен
     */
    private double calculateTotal(Map<String, Integer> items) {
        // Пока заглушка: предположим, что цена товара = 100
        // В будущем: вызов product-service для получения цен
        return items.values().stream()
                .mapToInt(Integer::intValue)
                .sum() * 100.0;
    }

    public List<Order> getOrdersBySessionId(String sessionId) {
        return orderRepository.findBySessionId(sessionId);
    }

    // Внутренний DTO для получения корзины из cart-service
    public static class CartDto {
        private String sessionId;
        private Map<String, Integer> items;

        // геттеры и сеттеры
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }

        public Map<String, Integer> getItems() { return items; }
        public void setItems(Map<String, Integer> items) { this.items = items; }
    }
}