package ru.onlinestore.frontend.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.onlinestore.frontend.client.CartClient;
import ru.onlinestore.frontend.client.ProductClient;
import ru.onlinestore.frontend.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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
    public String cart(Model model, HttpSession session) {
        String sessionId = session.getId();
        return pageService.fetchCartAndRender(model, sessionId, "cart", "cart");
    }
}
