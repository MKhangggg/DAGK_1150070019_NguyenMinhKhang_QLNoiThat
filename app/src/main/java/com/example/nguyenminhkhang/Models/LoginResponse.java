package com.example.nguyenminhkhang.Models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")  private boolean success;
    @SerializedName("message")  private String message;
    @SerializedName("userId")   private int userId;
    @SerializedName("fullName") private String fullName;
    @SerializedName("email")    private String email;
    @SerializedName("role")     private String role;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
