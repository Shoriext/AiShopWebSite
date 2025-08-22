package ru.onlinestore.aishopwebsite.model;

import lombok.Getter;

// Внутри контроллера или отдельно
@Getter
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
}