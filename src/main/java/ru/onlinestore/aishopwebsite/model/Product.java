// Сущность представляющая товар в магазине
package ru.onlinestore.aishopwebsite.model;

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
    private String size; // Размер футболки
    // Геттеры и сеттеры
    private Long id;
    private String name;
    private Double price;
    private String imageUrl;

    public Product() {
    }

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

    public Product(Long id, String name, Double price, String imageUrl, String size) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.size = size;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    // Геттеры
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getShirtColor() {
        return shirtColor;
    }

    public String getShirtImageUrl() {
        return shirtImageUrl;
    }

    public String getPrintImageUrl() {
        return printImageUrl;
    }

    public String getSize() {
        return size;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}