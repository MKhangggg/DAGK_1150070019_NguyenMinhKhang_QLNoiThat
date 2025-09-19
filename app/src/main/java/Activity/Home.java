package Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenminhkhang.Adapter.ProductAdapter;
import com.example.nguyenminhkhang.Models.Product;
import com.example.nguyenminhkhang.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home extends AppCompatActivity {

    private List<Product> Product;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Xử lý insets cho layout chính
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       /* // === Menu Avatar góc phải ===
        ImageView btnMenu = findViewById(R.id.btnMenu);


        btnMenu.setOnClickListener(v -> {
            android.widget.PopupMenu popup = new android.widget.PopupMenu(Home.this, v);
            popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_account) {
                    android.widget.Toast.makeText(this, "Thông tin tài khoản", android.widget.Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_cart) {
                    android.widget.Toast.makeText(this, "Mở giỏ hàng", android.widget.Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_logout) {
                    android.widget.Toast.makeText(this, "Đăng xuất", android.widget.Toast.LENGTH_SHORT).show();
                    startActivity(new android.content.Intent(this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            });

            popup.show();
        });*/

        // === Hiển thị danh sách sản phẩm ===
        RecyclerView recyclerProducts = findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new GridLayoutManager(this, 2)); // hiển thị 2 cột

        List<Product> products = new ArrayList<>();
        products.add(new Product("Black Simple Lamp", "$12.00", R.drawable.ic_bed));
        products.add(new Product("Chair Wood", "$35.00", R.drawable.ic_chair));
        products.add(new Product("Table Modern", "$99.00", R.drawable.ic_table));

        ProductAdapter adapter = new ProductAdapter(products);
        recyclerProducts.setAdapter(adapter);



    }
}
