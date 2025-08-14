package ru.onlinestore.aishopwebsite.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import ru.onlinestore.aishopwebsite.model.CartItem;
import ru.onlinestore.aishopwebsite.model.Product;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    public List<CartItem> getCart(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Product product) {
        List<CartItem> cart = getCart(session);

        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new CartItem(product, 1));
        }
    }

    public void removeFromCart(HttpSession session, Long productId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public double getTotal(List<CartItem> cart) {
        return cart.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public void clearCart(HttpSession session) {
        List<CartItem> cart = getCart(session);
        cart.clear();
    }
}