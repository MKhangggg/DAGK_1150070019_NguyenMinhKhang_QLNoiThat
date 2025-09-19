package com.example.nguyenminhkhang.Models;

public class LoginRequest {
    private String Email;
    private String Password;

    public LoginRequest(String Email, String Password) {
        this.Email = Email;
        this.Password = Password;
    }
}
