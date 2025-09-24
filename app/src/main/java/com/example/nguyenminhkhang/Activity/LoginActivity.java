package com.example.nguyenminhkhang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenminhkhang.Models.LoginRequest;
import com.example.nguyenminhkhang.Models.LoginResponse;
import com.example.nguyenminhkhang.Network.ApiService;
import com.example.nguyenminhkhang.Network.RetrofitClient;
import com.example.nguyenminhkhang.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvSignUp, tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgetPassword = findViewById(R.id.tvForgotPassword);

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, Register.class));
        });

        tvForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, FPassActivity.class));
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getLoginService();
            LoginRequest request = new LoginRequest(email, pass);

            apiService.login(request).enqueue(new Callback<LoginResponse>() {

                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse res = response.body();
                        if (res.isSuccess()) {
                            int userId = res.getUserId();
                            if (userId <= 0) {
                                Toast.makeText(LoginActivity.this, "⚠️ UserId không hợp lệ từ server", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            // 👇 Lấy role và chuẩn hoá
                            String role = res.getRole() == null ? "" : res.getRole().trim().toLowerCase();

                            // 👇 Điều hướng theo role — chỉ thay đổi đoạn này
                            Intent intent;
                            switch (role) {
                                case "admin":
                                    intent = new Intent(LoginActivity.this, AdminHome.class);
                                    break;
                                case "user":
                                default:
                                    intent = new Intent(LoginActivity.this, Home.class);
                                    break;
                            }

                            // Truyền kèm thông tin như cũ + role
                            intent.putExtra("userId", userId);
                            intent.putExtra("fullName", res.getFullName());
                            intent.putExtra("email", res.getEmail());
                            intent.putExtra("role", res.getRole());

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
