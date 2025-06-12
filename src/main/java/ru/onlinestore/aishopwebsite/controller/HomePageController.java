package ru.onlinestore.aishopwebsite.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.onlinestore.aishopwebsite.model.Product;
import ru.onlinestore.aishopwebsite.service.ProductService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "homePage";
    }

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-details";
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PostMapping("/order/{id}")
    public String processOrder(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            Model model) {

        // TODO: здесь можно реализовать логику сохранения заказа или отправку письма

        // Для MVP просто покажем страницу подтверждения
        model.addAttribute("name", name);
        model.addAttribute("productId", id);

        return "order-success";
    }
}
