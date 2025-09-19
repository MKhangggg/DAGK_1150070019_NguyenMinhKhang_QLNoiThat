package com.example.nguyenminhkhang.Models;

public class LoginResponse {
    private int Userid;
    private String FullName;
    private String Email;
    private String message;
    private boolean success;

    public int getId() { return Userid; }
    public String getName() { return FullName; }
    public String getEmail() { return Email; }
    public String getMessage() { return message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
}
