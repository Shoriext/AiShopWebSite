package ru.onlinestore.cartservice.service;

import ru.onlinestore.cartservice.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.onlinestore.cartservice.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart getCart(String sessionId) {
        Cart cart = cartRepository.findById(sessionId);
        if (cart == null) {
            cart = new Cart(sessionId);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart addItem(String sessionId, String productId, int quantity) {
        Cart cart = getCart(sessionId);
        cart.addItem(productId, quantity);
        cartRepository.save(cart);
        return cart;
    }

    public Cart updateItem(String sessionId, String productId, int quantity) {
        Cart cart = getCart(sessionId);
        cart.updateItem(productId, quantity);
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeItem(String sessionId, String productId) {
        Cart cart = getCart(sessionId);
        cart.removeItem(productId);
        cartRepository.save(cart);
        return cart;
    }

    public void clearCart(String sessionId) {
        cartRepository.delete(sessionId);
    }
}