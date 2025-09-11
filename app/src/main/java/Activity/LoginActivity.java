package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenminhkhang.R;

public class LoginActivity extends AppCompatActivity {

    TextView tvRegister;
    private View tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // gắn layout đăng nhập

        // Ánh xạ TextView "Đăng ký ngay"
        tvRegister = findViewById(R.id.tvRegister);

        // Bấm vào -> chuyển sang màn hình Register
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Register.class);
            startActivity(intent);
        });
        // Ánh xạ TextView "Quên mật khẩu"
        tvForgetPassword = findViewById(R.id.tvForgotPassword);

        // Bấm vào -> chuyển sang màn hình Quên mật khẩu
        tvForgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, FPassActivity.class);
            startActivity(intent);
        });
    }
}
