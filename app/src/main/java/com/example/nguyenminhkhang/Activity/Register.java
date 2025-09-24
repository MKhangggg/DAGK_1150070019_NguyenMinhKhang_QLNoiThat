package com.example.nguyenminhkhang.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenminhkhang.Models.RegisterRequest;
import com.example.nguyenminhkhang.Models.RegisterResponse;
import com.example.nguyenminhkhang.Network.RetrofitClient;
import com.example.nguyenminhkhang.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    EditText etName, etEmail, etPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.edtName);
        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name))  { etName.setError("Nhập họ tên"); return; }
        if (TextUtils.isEmpty(email)) { etEmail.setError("Nhập email"); return; }
        if (pass.length() < 6)        { etPassword.setError("Mật khẩu >= 6 ký tự"); return; }

        RegisterRequest req = new RegisterRequest(name, email, pass);

        RetrofitClient.getRegisterService().register(req).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> res) {
                if (res.isSuccessful() && res.body() != null) {
                    Toast.makeText(Register.this, res.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish(); // quay về Login
                } else {
                    String err = "";
                    try { if (res.errorBody()!=null) err = res.errorBody().string(); } catch (Exception ignored) {}
                    Log.e("REGISTER_ERROR", "code=" + res.code() + " err=" + err);
                    Toast.makeText(Register.this, "❌ " + (err.isEmpty() ? "Đăng ký thất bại" : err), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(Register.this, "⚠️ Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
