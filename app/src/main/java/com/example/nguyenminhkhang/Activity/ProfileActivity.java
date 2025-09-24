package com.example.nguyenminhkhang.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenminhkhang.R;

public class ProfileActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvRole = findViewById(R.id.tvRole);
        Button btnLogout = findViewById(R.id.btnLogout);

        String fullName = getIntent().getStringExtra("fullName");
        String email = getIntent().getStringExtra("email");
        String role = getIntent().getStringExtra("role");

        tvName.setText(fullName != null ? fullName : "—");
        tvEmail.setText(email != null ? email : "—");
        tvRole.setText(role != null ? role : "—");

        btnLogout.setOnClickListener(v -> {
            // TODO: clear token/session nếu có
            // Quay về màn hình đăng nhập
            Intent it = new Intent(ProfileActivity.this, LoginActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        });
    }
}
