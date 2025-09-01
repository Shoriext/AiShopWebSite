package ru.onlinestore.aishopwebsite.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import ru.onlinestore.aishopwebsite.model.CartItem;
import ru.onlinestore.aishopwebsite.model.Product;

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
            // Проверяем ID и размер - товары с разными размерами считаются разными позициями
            if (item.getProduct().getId().equals(product.getId()) && 
                isSameSize(item.getProduct().getSize(), product.getSize())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new CartItem(product, 1));
        }
    }

    // Вспомогательный метод для сравнения размеров
    private boolean isSameSize(String size1, String size2) {
        // Если оба размера null или пустые - считаем их одинаковыми
        if ((size1 == null || size1.trim().isEmpty()) && 
            (size2 == null || size2.trim().isEmpty())) {
            return true;
        }
        // Если один размер null/пустой, а другой нет - считаем разными
        if ((size1 == null || size1.trim().isEmpty()) != 
            (size2 == null || size2.trim().isEmpty())) {
            return false;
        }
        // Сравниваем размеры
        return size1 != null && size1.equals(size2);
    }

    public void removeFromCart(HttpSession session, Long productId, String size) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProduct().getId().equals(productId) && 
                             isSameSize(item.getProduct().getSize(), size));
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