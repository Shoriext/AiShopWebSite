package ru.onlinestore.frontend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onlinestore.frontend.client.CartClient;

import java.time.Duration;
import java.util.Map;

@Service
public class PageService {
    private final CartClient cartClient;

    public PageService(CartClient cartClient) {
        this.cartClient = cartClient;
    }

    // Вспомогательный метод: получить корзину и отрендерить страницу
    public String fetchCartAndRender(Model model, String sessionId, String viewName, String activePage) {
        try {
            CartClient.CartDto cart = cartClient.getCart(sessionId)
                    .timeout(Duration.ofSeconds(3))
                    .block(); // синхронный вызов (для Thymeleaf)

            model.addAttribute("cart", cart);
        } catch (Exception e) {
            System.err.println("Failed to fetch cart: " + e.getMessage());
            // Отдать пустую корзину или заглушку
            model.addAttribute("cart", new CartClient.CartDto(sessionId, Map.of(), 0));
        }

        model.addAttribute("activePage", activePage);
        return viewName;
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ PageService успешно создан!");
    }
}
