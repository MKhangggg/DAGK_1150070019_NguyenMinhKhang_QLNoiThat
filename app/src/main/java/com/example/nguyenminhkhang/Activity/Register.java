    package com.example.nguyenminhkhang.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.TextView;
    import androidx.appcompat.app.AppCompatActivity;

    import com.example.nguyenminhkhang.R;

    public class Register extends AppCompatActivity {

        TextView tvSignIn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register); // gắn layout đăng ký

            // Ánh xạ TextView "Đăng nhập ngay"
            tvSignIn = findViewById(R.id.tvSignIn);

            // Bấm vào -> chuyển sang màn hình Login
            tvSignIn.setOnClickListener(v -> {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
                finish(); // đóng RegisterActivity để tránh quay lại bằng nút Back
            });
        }
    }
