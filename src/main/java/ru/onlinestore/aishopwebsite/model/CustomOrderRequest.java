package ru.onlinestore.aishopwebsite.model;

// Внутри контроллера или отдельно
public class CustomOrderRequest {
    private String shirtColor;
    private String shirtImageUrl;
    private String printImageUrl;
    private String name;
    private String phone;


    public void setShirtColor(String shirtColor) { this.shirtColor = shirtColor; }

    public void setShirtImageUrl(String shirtImageUrl) { this.shirtImageUrl = shirtImageUrl; }

    public void setPrintImageUrl(String printImageUrl) { this.printImageUrl = printImageUrl; }

    public void setName(String name) { this.name = name; }

    public void setPhone(String phone) { this.phone = phone; }

    // Геттеры
    public String getShirtColor() { return shirtColor; }

    public String getShirtImageUrl() { return shirtImageUrl; }

    public String getPrintImageUrl() { return printImageUrl; }

    public String getName() { return name; }

    public String getPhone() { return phone; }
}