package ru.onlinestore.frontend.model;

import ru.onlinestore.frontend.client.ProductClient;

public class CartItemDetail {
    private final ProductClient.ProductDto product;
    private final String size;
    private final Integer quantity;

    public CartItemDetail(ProductClient.ProductDto product, String size, Integer quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }

    // геттеры
    public ProductClient.ProductDto getProduct() { return product; }
    public String getSize() { return size; }
    public Integer getQuantity() { return quantity; }
    public Double getTotalPrice() { return product.getPrice() * quantity; }
}