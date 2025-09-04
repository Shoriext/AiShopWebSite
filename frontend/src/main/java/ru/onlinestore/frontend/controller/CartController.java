package ru.onlinestore.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.onlinestore.frontend.client.CartClient;
import ru.onlinestore.frontend.client.ProductClient;

@Controller
class CartController {

    @Autowired
    private CartClient cartClient;

    @Autowired
    private ProductClient productClient;

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(required = false) String size,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String sessionId = session.getId();

        try {
            // Проверим, существует ли товар
            ProductClient.ProductDto product = productClient.getProductById(productId);
            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Товар не найден");
                return "redirect:/products";
            }

            // Вызов cart-service: POST /api/cart/{id}/add?productId=...&quantity=1
            CartClient.CartDto cart = cartClient.addItem(sessionId, productId.toString(), 1, size);
            // Или без size: cartClient.addItem(sessionId, productId.toString(), 1);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Товар \"" + product.getName() + "\" добавлен в корзину!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не удалось добавить товар в корзину");
            e.printStackTrace();
        }

        return "redirect:/products";
    }
}
