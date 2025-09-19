package com.example.nguyenminhkhang.Network;

import com.example.nguyenminhkhang.Models.LoginRequest;
import com.example.nguyenminhkhang.Models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("User/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
