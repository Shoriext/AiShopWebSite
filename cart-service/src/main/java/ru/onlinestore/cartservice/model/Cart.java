package ru.onlinestore.cartservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    private String sessionId;
    private Map<String, Integer> items = new HashMap<>(); // productId → quantity

    public Cart() {}

    public Cart(String sessionId) {
        this.sessionId = sessionId;
    }

    // Getters and Setters
    @JsonIgnore
    private int totalItems;

    public void setTotalItems(int totalItems) {
        // игнорируем
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public int getTotalItems() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void addItem(String productId, int quantity) {
        items.merge(productId, quantity, Integer::sum);
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public void updateItem(String productId, int quantity) {
        if (quantity <= 0) {
            items.remove(productId);
        } else {
            items.put(productId, quantity);
        }
    }
}