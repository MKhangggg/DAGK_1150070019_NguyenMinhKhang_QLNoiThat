package com.example.nguyenminhkhang.Network;

import com.example.nguyenminhkhang.Models.LoginRequest;
import com.example.nguyenminhkhang.Models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    // Nếu swagger hiển thị là: https://xxx/api/User/login
    @POST("api/User/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // Nếu swagger hiển thị là: https://xxx/User/login
    // @POST("User/login")
    // Call<LoginResponse> login(@Body LoginRequest request);
}
