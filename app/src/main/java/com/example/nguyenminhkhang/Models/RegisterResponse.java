package com.example.nguyenminhkhang.Models;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("id")
    private int id;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("role")
    private String role;
    @SerializedName("dateCreate")
    private String dateCreate;

    public String getMessage() { return message; }
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getDateCreate() { return dateCreate; }
}