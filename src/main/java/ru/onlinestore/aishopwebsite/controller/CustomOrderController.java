package ru.onlinestore.aishopwebsite.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.onlinestore.aishopwebsite.model.CartItem;
import ru.onlinestore.aishopwebsite.model.CustomOrderRequest;
import ru.onlinestore.aishopwebsite.model.Order;
import ru.onlinestore.aishopwebsite.model.Product;
import ru.onlinestore.aishopwebsite.service.TelegramNotificationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CustomOrderController {

    @Autowired
    private TelegramNotificationService telegramService;

    @GetMapping("/custom-order")
    public String showCustomOrderForm(
            @RequestParam String shirtColor,
            @RequestParam String shirtImageUrl,
            @RequestParam String printImageUrl,
            Model model) {

        model.addAttribute("shirtColor", shirtColor);
        model.addAttribute("shirtImageUrl", shirtImageUrl);
        model.addAttribute("printImageUrl", printImageUrl);

        return "custom-order"; // шаблон custom-order.html
    }

    @PostMapping("/custom-order")
    public String processCustomOrder(
            @ModelAttribute CustomOrderRequest request,
            RedirectAttributes redirectAttributes) {

        telegramService.sendCustomTshirtOrder(
                request.getName(),
                request.getPhone(),
                request.getShirtColor(),
                request.getPrintImageUrl()
        );

        // Уведомление пользователю
        redirectAttributes.addFlashAttribute("message", "Заказ оформлен! С вами свяжутся по телефону.");

        return "purchase-success";
    }
}