package ru.onlinestore.aishopwebsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Главная страница
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        return "index"; // src/main/resources/templates/index.html
    }

//    @GetMapping("/products")
//    public String products(Model model) {
//        model.addAttribute("activePage", "products");
//        return "products"; // products.html
//    }

    @GetMapping("/constructor")
    public String constructor(Model model) {
        model.addAttribute("activePage", "constructor");
        return "constructor"; // constructor.html
    }

    @GetMapping("/contacts")
    public String contacts(Model model) {
        model.addAttribute("activePage", "contacts");
        return "contacts"; // contacts.html
    }
}