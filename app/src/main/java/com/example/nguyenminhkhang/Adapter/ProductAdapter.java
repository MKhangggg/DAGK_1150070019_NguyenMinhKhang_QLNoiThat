package com.example.nguyenminhkhang.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenminhkhang.Models.CartItems;
import com.example.nguyenminhkhang.Models.CartResponse;
import com.example.nguyenminhkhang.Models.Product;
import com.example.nguyenminhkhang.Network.RetrofitClient;
import com.example.nguyenminhkhang.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    public interface OnCartChanged {
        void onBadgeIncrease(int delta);
    }

    private final Context context;
    private final int userId;
    private final OnCartChanged onCartChanged;
    private final LayoutInflater inflater;
    private final List<Product> products = new ArrayList<>();
    private final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));

    public ProductAdapter(Context context, int userId, OnCartChanged onCartChanged) {
        this.context = context;
        this.userId = userId;
        this.onCartChanged = onCartChanged;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<Product> items) {
        products.clear();
        if (items != null) products.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.activity_item_product, parent, false); // đổi đúng layout item của bạn
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Product p = products.get(position);
        h.tvName.setText(p.getName() != null ? p.getName() : "—");
        h.tvPrice.setText(currencyVN.format(p.getPrice()));

        String url = p.getImageUrl();
        if (url == null || url.trim().isEmpty()) {
            h.imgProduct.setImageResource(R.drawable.image_placeholder);
        } else {
            Picasso.get().load(url)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .fit().centerCrop()
                    .into(h.imgProduct);
        }

        h.btnAddToCart.setOnClickListener(v -> addToCart(p));
    }

    @Override public int getItemCount() { return products.size(); }

    private void addToCart(Product product) {
        if (userId <= 0) {
            Toast.makeText(context, "Chưa xác định user, vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }
        CartItems body = new CartItems();
        body.setUserId(userId);
        body.setProductId(product.getId());
        body.setQuantity(1);
        body.setPrice(product.getPrice());

        RetrofitClient.getCartService().addToCart(body).enqueue(new Callback<CartResponse>() {
            @Override public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "✅ Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
                    if (onCartChanged != null) onCartChanged.onBadgeIncrease(1);
                } else {
                    Toast.makeText(context, "❌ Không thể thêm vào giỏ", Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("API_ERROR","Code:"+response.code()+" | Body:"+
                                (response.errorBody()!=null? new Gson().toJson(response.errorBody().string()):"null"));
                    } catch (Exception ignored) {}
                }
            }
            @Override public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "⚠️ Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnAddToCart;
        TextView tvName, tvPrice;
        VH(@NonNull View itemView) {
            super(itemView);
            imgProduct   = itemView.findViewById(R.id.imgProduct);
            tvName       = itemView.findViewById(R.id.tvName);
            tvPrice      = itemView.findViewById(R.id.tvPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
