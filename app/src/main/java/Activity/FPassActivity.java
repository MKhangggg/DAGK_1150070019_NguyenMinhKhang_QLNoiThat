package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyenminhkhang.R;

public class FPassActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnReset;
    TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpass);

        etEmail = findViewById(R.id.etEmail);
        btnReset = findViewById(R.id.btnReset);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Xử lý nút Gửi yêu cầu
        btnReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email hoặc tên đăng nhập", Toast.LENGTH_SHORT).show();
            } else {
                // Ở đây bạn có thể gọi API gửi mail reset mật khẩu
                Toast.makeText(this, "Yêu cầu đặt lại mật khẩu đã được gửi tới: " + email, Toast.LENGTH_LONG).show();
            }
        });

        // Quay lại Login
        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(FPassActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
