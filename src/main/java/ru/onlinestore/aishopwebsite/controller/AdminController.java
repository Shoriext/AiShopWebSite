package ru.onlinestore.aishopwebsite.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import ru.onlinestore.aishopwebsite.model.Product;
import ru.onlinestore.aishopwebsite.service.FileStorageService;
import ru.onlinestore.aishopwebsite.service.ProductService;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    // Отображение панели администратора
    @GetMapping
    public String showAdminPanel(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("product", new Product());
        return "admin-panel";
    }

    // Добавление нового товара
//    @PostMapping("/add")
//    public String addProduct(@ModelAttribute("product") Product product) {
//        productService.createProduct(product);
//        return "redirect:/admin";
//    }
    @PostMapping("/add")
    public String addProduct(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Product product;

        if (id != null && id > 0) {
            product = productService.getProductById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
        } else {
            product = new Product();
        }

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.saveFile(image);
            product.setImageUrl(imageUrl);
        }

        productService.createProduct(product);

        return "redirect:/admin";
    }

    // Переход к редактированию товара
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
        model.addAttribute("product", product);
        return "admin-panel";
    }

    // Удаление товара
    //@DeleteMapping
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin";
    }
}
