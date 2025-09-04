package ru.onlinestore.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.onlinestore.frontend.client.CartClient;
import ru.onlinestore.frontend.client.OrderClient;

@Controller
public class OrderController {

    private final OrderClient orderClient;

    private final CartClient cartClient;

    public OrderController(OrderClient orderClient, CartClient cartClient) {
        this.orderClient = orderClient;
        this.cartClient = cartClient;
    }

    @PostMapping("/checkout")
    public String processCheckout(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String sessionId,
            RedirectAttributes redirectAttributes) {

        try {
            // Создаём заказ

            orderClient.createOrder(sessionId, name, phone);

            // Очищаем корзину
            cartClient.clearCart(sessionId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Спасибо, " + name + "! Ваш заказ принят. Мы свяжемся с вами по телефону " + phone);

            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не удалось оформить заказ");
            e.printStackTrace();
            return "redirect:/cart";
        }
    }
}
