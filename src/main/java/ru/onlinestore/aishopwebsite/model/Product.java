// Сущность представляющая товар в магазине
package ru.onlinestore.aishopwebsite.model;

import lombok.Getter;

@Getter
public class Product {

    public void setPrintImageUrl(String printImageUrl) {
        this.printImageUrl = printImageUrl;
    }

    public void setShirtColor(String shirtColor) {
        this.shirtColor = shirtColor;
    }

    public void setShirtImageUrl(String shirtImageUrl) {
        this.shirtImageUrl = shirtImageUrl;
    }

    private String shirtColor;
    private String shirtImageUrl;
    private String printImageUrl;
    // Геттеры и сеттеры
    private Long id;
    private String name;
    private Double price;
    private String imageUrl;

    public Product() {}

    public Product(Long id, String name, Double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String shirtColor, String shirtImageUrl, String printImageUrl) {
        this.shirtColor = shirtColor;
        this.shirtImageUrl = shirtImageUrl;
        this.printImageUrl = printImageUrl;
    }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setPrice(Double price) { this.price = price; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}