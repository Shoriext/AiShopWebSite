package ru.onlinestore.cartservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.onlinestore.cartservice.model.Cart;
import ru.onlinestore.cartservice.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // GET /api/cart/{sessionId}
    @GetMapping("/{sessionId}")
    public Cart getCart(@PathVariable String sessionId) {
        return cartService.getCart(sessionId);
    }

    // POST /api/cart/{sessionId}/add?productId=prod1&quantity=2
    @PostMapping("/{sessionId}/add")
    public Cart addItem(
            @PathVariable String sessionId,
            @RequestParam String productId,
            @RequestParam(defaultValue = "1") int quantity,
            @RequestParam(required = false) String size) {

        String key = size != null ? productId + ":" + size : productId;
        return cartService.addItem(sessionId, key, quantity);
    }

    // PUT /api/cart/{sessionId}/update?productId=prod1&quantity=3
    @PutMapping("/{sessionId}/update")
    public Cart updateItem(
            @PathVariable String sessionId,
            @RequestParam String productId,
            @RequestParam int quantity) {
        return cartService.updateItem(sessionId, productId, quantity);
    }

    // DELETE /api/cart/{sessionId}/remove?productId=prod1
    @DeleteMapping("/{sessionId}/remove")
    public Cart removeItem(
            @PathVariable String sessionId,
            @RequestParam String productId) {
        return cartService.removeItem(sessionId, productId);
    }

    // DELETE /api/cart/{sessionId}/clear
    @DeleteMapping("/{sessionId}/clear")
    public void clearCart(@PathVariable String sessionId) {
        cartService.clearCart(sessionId);
    }


}