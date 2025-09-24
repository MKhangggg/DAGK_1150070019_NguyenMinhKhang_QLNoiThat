package com.example.nguyenminhkhang.Models;

import com.google.gson.annotations.SerializedName;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    // Getter
    public int getId() {     return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

