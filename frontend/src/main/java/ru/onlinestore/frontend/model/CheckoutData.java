package ru.onlinestore.frontend.model;

import java.util.List;

public class CheckoutData {
    private List<CartItemDetail> cartItems;
    private double total;
    private String sessionId;

    // конструктор
    public CheckoutData(List<CartItemDetail> cartItems, double total, String sessionId) {
        this.cartItems = cartItems;
        this.total = total;
        this.sessionId = sessionId;
    }

    // геттеры
    public List<CartItemDetail> getCartItems() { return cartItems; }
    public double getTotal() { return total; }
    public String getSessionId() { return sessionId; }
}