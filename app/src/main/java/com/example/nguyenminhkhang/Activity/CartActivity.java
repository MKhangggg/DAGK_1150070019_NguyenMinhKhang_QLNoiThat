package com.example.nguyenminhkhang.Activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenminhkhang.Models.CartItems;
import com.example.nguyenminhkhang.Network.RetrofitClient;
import com.example.nguyenminhkhang.R;
import com.example.nguyenminhkhang.Adapter.CartAdapter;
import com.example.nguyenminhkhang.Network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
    private ApiService apiService;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.cartToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // hiện mũi tên back
        }

        // Xử lý click vào mũi tên back
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerCart = findViewById(R.id.recyclerCart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getCartService();

        // 🟢 Nhận userId từ Intent (truyền từ Home)
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "⚠️ Không tìm thấy userId, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadCart();
    }

    private void loadCart() {
        apiService.getCartByUser(userId).enqueue(new Callback<List<CartItems>>() {
            @Override
            public void onResponse(Call<List<CartItems>> call, Response<List<CartItems>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartAdapter = new CartAdapter(CartActivity.this, response.body(), apiService, userId);

                    // 🔹 Callback để cập nhật badge ở Home khi thay đổi giỏ hàng
                    cartAdapter.setOnCartChangeListener(totalItems -> {
                        if (Home.instance != null) {
                            Home.instance.updateCartBadge(totalItems);
                        }
                    });

                    recyclerCart.setAdapter(cartAdapter);

                    // 🔹 Cập nhật badge ngay khi mở giỏ hàng
                    int total = 0;
                    for (CartItems item : response.body()) {
                        total += item.getQuantity();
                    }
                    if (Home.instance != null) {
                        Home.instance.updateCartBadge(total);
                    }

                } else {
                    Toast.makeText(CartActivity.this, "❌ Không tải được giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartItems>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "⚠️ Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
