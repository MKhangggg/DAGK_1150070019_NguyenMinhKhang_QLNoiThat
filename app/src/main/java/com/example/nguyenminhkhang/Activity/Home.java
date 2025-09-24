package com.example.nguyenminhkhang.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.nguyenminhkhang.Adapter.ProductAdapter;
import com.example.nguyenminhkhang.Models.CartItems;
import com.example.nguyenminhkhang.Models.Product;
import com.example.nguyenminhkhang.Network.ApiService;
import com.example.nguyenminhkhang.Network.RetrofitClient;
import com.example.nguyenminhkhang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    ImageView ivCartTop;
    TextView tvCartBadge;

    RecyclerView rvProducts;
    ProductAdapter productAdapter;

    public static Home instance;
    private int userId;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        try {
            setContentView(R.layout.activity_home);
        } catch (Throwable t) {
            Toast.makeText(this, "Lỗi inflate activity_home: " + t.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
            t.printStackTrace();
            finish();
            return;
        }

        // ====== INIT VIEW ======
        ivCartTop   = findViewById(R.id.ivCartTop);
        tvCartBadge = findViewById(R.id.tvCartBadge);
        rvProducts  = findViewById(R.id.recyclerProducts); // đảm bảo id này tồn tại trong activity_home.xml

        // User info
        userId = getIntent().getIntExtra("userId", -1);

        // ====== CART ICON ======
        ivCartTop.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, CartActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // ====== BOTTOM NAV ======
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                rvProducts.setVisibility(View.VISIBLE);
                return true;
            } else if (id == R.id.nav_user) {
                openProfile();
                return true;
            } else if (id == R.id.nav_fav) {
                Toast.makeText(this, "Favorites (đang phát triển)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_bell) {
                Toast.makeText(this, "Notifications (đang phát triển)", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        if (savedInstanceState == null) bottomNav.setSelectedItemId(R.id.nav_home);

        // ====== SETUP ADAPTER (khởi tạo TRƯỚC, rồi mới gọi API) ======
        productAdapter = new ProductAdapter(
                this,
                userId,
                delta -> updateCartBadge(getCurrentBadgeCount() + delta) // cập nhật badge khi add to cart thành công
        );
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cột
        rvProducts.setAdapter(productAdapter);

        // ====== LOAD SẢN PHẨM ======
        loadProducts();

        // ====== CART BADGE ======
        if (userId > 0) {
            loadCartCount(userId);
        } else {
            Toast.makeText(this, "Thiếu userId (debug) – vẫn vào Home", Toast.LENGTH_SHORT).show();
            updateCartBadge(0);
        }
    }

    private void loadProducts() {
        ApiService api = RetrofitClient.getProductService();
        api.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!isFinishing() && !isDestroyed()) {
                    if (response.isSuccessful() && response.body() != null) {
                        productAdapter.setData(response.body()); // <-- đổ vào adapter
                    } else {
                        Toast.makeText(Home.this, "Không tải được sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (!isFinishing() && !isDestroyed()) {
                    Toast.makeText(Home.this, "Lỗi sản phẩm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openProfile() {
        Intent it = new Intent(this, ProfileActivity.class);
        it.putExtra("fullName", getIntent().getStringExtra("fullName"));
        it.putExtra("email", getIntent().getStringExtra("email"));
        it.putExtra("role", getIntent().getStringExtra("role"));
        startActivity(it);
    }

    public void updateCartBadge(int count) {
        if (tvCartBadge == null) return;
        tvCartBadge.setText(String.valueOf(count));
        tvCartBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    public int getCurrentBadgeCount() {
        if (tvCartBadge == null || tvCartBadge.getText().toString().isEmpty()) return 0;
        try { return Integer.parseInt(tvCartBadge.getText().toString()); }
        catch (NumberFormatException e) { return 0; }
    }

    private void loadCartCount(int userId) {
        ApiService api = RetrofitClient.getCartService();
        api.getCartByUser(userId).enqueue(new Callback<List<CartItems>>() {
            @Override
            public void onResponse(Call<List<CartItems>> call, Response<List<CartItems>> response) {
                if (!isFinishing() && !isDestroyed()) {
                    if (response.isSuccessful() && response.body() != null) {
                        int total = 0;
                        for (CartItems item : response.body()) total += item.getQuantity();
                        updateCartBadge(total);
                    } else {
                        updateCartBadge(0);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<CartItems>> call, Throwable t) {
                if (!isFinishing() && !isDestroyed()) updateCartBadge(0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userId > 0) loadCartCount(userId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
