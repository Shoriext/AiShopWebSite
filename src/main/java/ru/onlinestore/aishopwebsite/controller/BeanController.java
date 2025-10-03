package ru.onlinestore.aishopwebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import ru.onlinestore.aishopwebsite.service.BeanService;

import java.io.IOException;

@Controller
public class BeanController {
    @Autowired
    private BeanService beanService;

    @GetMapping("/order/add")
    public String addProduct() {
        return "";
    }

    @GetMapping("/order/buy")
    public String buyProduct(@RequestParam Long productId,
            @RequestParam String productName,
            @RequestParam Double price,
            Model model) throws IOException, InterruptedException {
        String phone = "124142";
        String size = "XXXXX";
        ;
        BeanService.buyProduct(phone, productName, size);
        return "purchase-success";
    }
}
