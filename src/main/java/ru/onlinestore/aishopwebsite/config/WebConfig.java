package ru.onlinestore.aishopwebsite.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.onlinestore.aishopwebsite.model.CartItem;

import java.util.ArrayList;

import java.util.List;

@Controller
@SessionAttributes("cart")
public class WebConfig {

    @ModelAttribute("cart")
    public List<CartItem> createCart() {

        return new ArrayList<>();

    }
}