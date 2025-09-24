package com.example.nguyenminhkhang.Network;

import android.content.IntentFilter;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    // Không có khoảng trắng thừa
    private static final String BASE_URL_LOGIN = "http://10.0.2.2:7171/";
    public static final String BASE_URL_REGISTER = "http://10.0.2.2:7171/";
    public static final String BASE_URL_PRODUCT = "http://10.0.2.2:7171/";
    public static final String BASE_URL_CART = "http://10.0.2.2:7171/";
    private static Retrofit retrofitLogin;
    private static Retrofit retrofitProduct;
    private static Retrofit retrofitCart;
    private static Retrofit retrofitRegister;


    public static ApiService getCartService() {
        if (retrofitCart == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
            retrofitCart = new Retrofit.Builder()
                    .baseUrl(BASE_URL_CART)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitCart.create(ApiService.class);
    }

    // Product
    public static ApiService getProductService() {
        if (retrofitProduct == null) {
            retrofitProduct = new Retrofit.Builder()
                    .baseUrl(BASE_URL_PRODUCT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitProduct.create(ApiService.class);
    }

    // Login
    public static ApiService getLoginService() {
        if (retrofitLogin == null) {
            retrofitLogin = new Retrofit.Builder()
                    .baseUrl(BASE_URL_LOGIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitLogin.create(ApiService.class);
    }

    //Register
    public static ApiService getRegisterService() {
        if (retrofitRegister == null) {
            retrofitRegister = new Retrofit.Builder()
                    .baseUrl(BASE_URL_REGISTER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitRegister.create(ApiService.class);
    }

}
