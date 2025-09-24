package com.example.nguyenminhkhang.Network;

import com.example.nguyenminhkhang.Models.CartItems;
import com.example.nguyenminhkhang.Models.CartResponse;
import com.example.nguyenminhkhang.Models.LoginRequest;
import com.example.nguyenminhkhang.Models.LoginResponse;
import com.example.nguyenminhkhang.Models.Product;
import com.example.nguyenminhkhang.Models.QuantityRequest;
import com.example.nguyenminhkhang.Models.RegisterRequest;
import com.example.nguyenminhkhang.Models.RegisterResponse;
import com.example.nguyenminhkhang.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // Nếu swagger hiển thị là: https://xxx/api/User/login
    @POST("api/User/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("api/User/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
    @GET("api/Product")
    Call<List<Product>> getProducts();
    @POST("api/Product")
    Call<Product> createProduct(@Body Product product);

    @PUT("api/Product/{id}")
    Call<Void> updateProduct(@Path("id") int id, @Body Product product);

    @DELETE("api/Product/{id}")
    Call<Void> deleteProduct(@Path("id") int id);

    // thêm sản phẩm vào giỏ
    @POST("api/CartItems/add")
    Call<CartResponse> addToCart(@Body CartItems cartItem);

    @PUT("api/CartItems/{id}/quantity")
    Call<Void> updateCartQuantity(
            @Path("id") int id,
            @Body QuantityRequest body
    );
    // lấy giỏ hàng theo user
    @GET("api/CartItems/user/{userId}")
    Call<List<CartItems>> getCartByUser(@Path("userId") int userId);
    @DELETE("api/CartItems/{id}")
    Call<Void> removeFromCart(@Path("id") int cartItemId);

    @POST("api/CartItems/checkout/{userId}")
    Call<Void> placeOrder(@Path("userId") int userId);
}
