package com.example.nguyenminhkhang.Models;

public class CartResponse {
    private String message;
    private String product;   // tên sản phẩm (API trả về)
    private double price;     // giá sản phẩm
    private String image;     // link ảnh sản phẩm
    private int quantity;     // số lượng

    // Getters & Setters
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
