package ru.onlinestore.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.onlinestore.orderservice.model.Order;
import ru.onlinestore.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Order createOrder(@RequestParam String sessionId) {
        return orderService.createOrder(sessionId);
    }

    @GetMapping("/my")
    public List<Order> getOrdersBySessionId(@RequestParam String sessionId) {
        return orderService.getOrdersBySessionId(sessionId);
    }
}
