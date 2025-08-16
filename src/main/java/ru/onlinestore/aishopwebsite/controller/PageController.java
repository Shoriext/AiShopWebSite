package ru.onlinestore.aishopwebsite.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.onlinestore.aishopwebsite.service.CartService;

@Controller
public class PageController {

    @Autowired
    private CartService cartService;
    // Главная страница
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("activePage", "home");
        return "index"; // src/main/resources/templates/index.html
    }

    @GetMapping("/constructor")
    public String constructor(Model model, HttpSession session) {
        model.addAttribute("activePage", "constructor");
        model.addAttribute("cart", cartService.getCart(session));
        return "constructor"; // constructor.html
    }

    @GetMapping("/contacts")
    public String contacts(Model model, HttpSession session) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("activePage", "contacts");
        return "contacts"; // contacts.html
    }
}