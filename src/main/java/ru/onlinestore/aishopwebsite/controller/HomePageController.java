package ru.onlinestore.aishopwebsite.controller;

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
        return "redirect:/api/products/" + id; // Пока просто перенаправляем на API
    }
}
