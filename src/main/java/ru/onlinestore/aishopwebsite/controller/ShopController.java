package ru.onlinestore.aishopwebsite.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import ru.onlinestore.aishopwebsite.model.CartItem;
import ru.onlinestore.aishopwebsite.model.Order;
import ru.onlinestore.aishopwebsite.model.Product;
import ru.onlinestore.aishopwebsite.service.CartService;
import ru.onlinestore.aishopwebsite.service.TelegramNotificationService;

@Controller
public class ShopController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TelegramNotificationService telegramService;

    private List<Product> products = Arrays.asList(
            new Product(1L, "Футболка «Алфавит Студента»", 1990.0, "/images/product1-1.jpg"),
            new Product(2L, "Шоппер «Алфавит Студента»", 1490.0, "/images/product2-1.jpeg"));

    @GetMapping("/products")
    public String products(Model model, HttpSession session) {

        model.addAttribute("products", products);
        model.addAttribute("activePage", "products");
        model.addAttribute("cart", cartService.getCart(session));

        // Проверяем наличие сообщения об успехе в сессии
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage"); // Удаляем сообщение из сессии
        }

        return "products";
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(required = false) String size,
            HttpSession session,
            Model model) {

        Product product = products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Проверяем, что для футболки выбран размер
        if (product.getName().contains("Футболка") && (size == null || size.trim().isEmpty())) {
            model.addAttribute("products", products);
            model.addAttribute("activePage", "products");
            model.addAttribute("cart", cartService.getCart(session));
            model.addAttribute("error", "Пожалуйста, выберите размер футболки");
            return "products";
        }

        // Создаем новый экземпляр продукта с размером
        Product productWithSize = new Product(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                size != null ? size : null);

        cartService.addToCart(session, productWithSize);

        // Добавляем сообщение об успешном добавлении в сессию
        session.setAttribute("successMessage", "Товар успешно добавлен в корзину!");

        return "redirect:/products";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.getTotal(cart));

        // Проверяем наличие сообщения об успехе в сессии
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage"); // Удаляем сообщение из сессии
        }

        return "cart";
    }

    @GetMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam Long productId,
            @RequestParam(required = false) String size,
            HttpSession session) {

        cartService.removeFromCart(session, productId, size);
        session.setAttribute("successMessage", "Товар удален из корзины");
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