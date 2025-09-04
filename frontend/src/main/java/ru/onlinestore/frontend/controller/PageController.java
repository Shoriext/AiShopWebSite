package ru.onlinestore.frontend.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.onlinestore.frontend.client.CartClient;
import ru.onlinestore.frontend.client.ProductClient;
import ru.onlinestore.frontend.model.CartItemDetail;
import ru.onlinestore.frontend.service.PageService;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PageController {


    private final CartClient cartClient;

    private final ProductClient productClient;

    private final PageService pageService;

    public PageController(CartClient cartClient, ProductClient productClient, PageService pageService) {
        this.cartClient = cartClient;
        this.productClient = productClient;
        this.pageService = pageService;
    }
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        String sessionId = session.getId();
        return pageService.fetchCartAndRender(model, sessionId, "index", "home");
    }

    @GetMapping("/constructor")
    public String constructor(Model model, HttpSession session) {
        String sessionId = session.getId();
        return pageService.fetchCartAndRender(model, sessionId, "constructor", "constructor");
    }

    @GetMapping("/contacts")
    public String contacts(Model model, HttpSession session) {
        String sessionId = session.getId();
        return pageService.fetchCartAndRender(model, sessionId, "contacts", "contacts");
    }

//    @GetMapping("/products")
//    public String products(Model model, HttpSession session) {
//        String sessionId = session.getId();
//        return pageService.fetchCartAndRender(model, sessionId, "products", "products");
//    }
    @GetMapping("/products")
    public String products(Model model, HttpSession session) {
        // Получаем корзину
        String sessionId = session.getId();
        try {
            CartClient.CartDto cart = cartClient.getCart(sessionId).block();
            model.addAttribute("cart", cart);
        } catch (Exception e) {
            model.addAttribute("cart", new CartClient.CartDto(sessionId, Map.of(), 0));
        }

        // Получаем товары
        List<ProductClient.ProductDto> products = productClient.getAllProducts();
        model.addAttribute("products", products);

        model.addAttribute("activePage", "products");
        return "products"; // → templates/products.html
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        String sessionId = session.getId();

        try {
            // Получаем корзину из cart-service
            CartClient.CartDto cart = cartClient.getCart(sessionId).block();
            model.addAttribute("cart", cart);

            // Подтягиваем детали товаров
            List<CartItemDetail> cartItems = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
                String key = entry.getKey(); // например: "1:S", "2"
                Integer quantity = entry.getValue();

                String productIdStr = key.contains(":") ? key.split(":")[0] : key;
                String size = key.contains(":") ? key.split(":")[1] : null;

                try {
                    Long productId = Long.valueOf(productIdStr);
                    ProductClient.ProductDto product = productClient.getProductById(productId);

                    cartItems.add(new CartItemDetail(product, size, quantity));
                } catch (Exception e) {
                    System.err.println("Товар не найден: " + productIdStr);
                }
            }

            model.addAttribute("cartItems", cartItems);

            // Считаем общую сумму
            double total = cartItems.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
            model.addAttribute("total", total);

        } catch (Exception e) {
            model.addAttribute("cart", new CartClient.CartDto(sessionId, Map.of(), 0));
            model.addAttribute("cartItems", Collections.emptyList());
            model.addAttribute("total", 0.0);
            e.printStackTrace();
        }

        model.addAttribute("activePage", "cart");
        return "cart";
    }
}
