package ru.onlinestore.aishopwebsite.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.onlinestore.aishopwebsite.model.CartItem;
import ru.onlinestore.aishopwebsite.model.Order;
import ru.onlinestore.aishopwebsite.model.Product;
import ru.onlinestore.aishopwebsite.service.CartService;
import ru.onlinestore.aishopwebsite.service.TelegramNotificationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ShopController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TelegramNotificationService telegramService;

    private final List<Product> products = Arrays.asList(
            new Product(1L, "Футболка «Алфавит Студента»", 1990.0, "/images/product1.jpg"),
            new Product(2L, "Шоппер «Алфавит Студента»", 1490.0, "/images/product2.jpg")
    );

    @GetMapping("/products")
    public String products(Model model, HttpSession session) {
        model.addAttribute("products", products);
        model.addAttribute("cart", cartService.getCart(session)); // если нужно в шаблоне
        return "products";
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long productId,
            HttpSession session) {

        Product product = products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        cartService.addToCart(session, product);

        return "redirect:/products";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.getTotal(cart));
        return "cart";
    }

    @GetMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam Long productId,
            HttpSession session) {

        cartService.removeFromCart(session, productId);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session) {
        List<CartItem> cart = cartService.getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart"; // если корзина пуста — назад
        }

        double total = cart.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        model.addAttribute("order", new Order()); // пустой объект для формы

        return "checkout"; // шаблон checkout.html
    }

    @PostMapping("/checkout")
    public String processCheckout(
            @ModelAttribute Order order,
            HttpSession session,
            Model model) {

        List<CartItem> cart = cartService.getCart(session);
        double total = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();

        // Отправляем уведомление в Telegram
        telegramService.sendOrderNotification(order, cart, total);

        // Очищаем корзину
        cartService.clearCart(session);

        model.addAttribute("order", order);
        return "purchase-success";
    }
}