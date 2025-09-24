package com.example.nguyenminhkhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenminhkhang.Models.CartItems;
import com.example.nguyenminhkhang.Models.QuantityRequest;
import com.example.nguyenminhkhang.Network.ApiService;
import com.example.nguyenminhkhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
    private final List<CartItems> items;
    private final Context context;
    private final ApiService api;
    private final int userId;

    // 🔹 callback cập nhật giỏ hàng
    public interface OnCartChangeListener {
        void onCartUpdated(int totalItems);
    }
    private OnCartChangeListener listener;

    public void setOnCartChangeListener(OnCartChangeListener l) {
        this.listener = l;
    }

    public CartAdapter(Context ctx, List<CartItems> items, ApiService api, int userId) {
        this.context = ctx;
        this.items = items;
        this.api = api;
        this.userId = userId;
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnDelete;
        Button btnMinus, btnPlus;
        VH(View v){
            super(v);
            imgProduct  = v.findViewById(R.id.imgProduct);
            tvName      = v.findViewById(R.id.txtProductName);
            tvPrice     = v.findViewById(R.id.txtPrice);
            tvQuantity  = v.findViewById(R.id.txtQuantity);
            btnDelete   = v.findViewById(R.id.btnDelete);
            btnMinus    = v.findViewById(R.id.btnMinus);
            btnPlus     = v.findViewById(R.id.btnPlus);
        }
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        CartItems it = items.get(pos);
        h.tvName.setText(it.getProductName());
        double totalPrice = it.getProductPrice() * it.getQuantity();
        h.tvPrice.setText("$ " + totalPrice);
        h.tvQuantity.setText(String.valueOf(it.getQuantity()));
        Picasso.get().load(it.getProductImage()).into(h.imgProduct);

        // Nút cộng
        h.btnPlus.setOnClickListener(v -> {
            int currentPos = h.getBindingAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            CartItems target = items.get(currentPos);
            int oldQty = target.getQuantity();
            int newQty = oldQty + 1;
            target.setQuantity(newQty);

            notifyItemChanged(currentPos);

            api.updateCartQuantity(target.getId(), new QuantityRequest(newQty))
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                notifyCartChanged(); // ✅ cập nhật badge
                            } else {
                                target.setQuantity(oldQty);
                                notifyItemChanged(currentPos);
                                Toast.makeText(context, "❌ Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            target.setQuantity(oldQty);
                            notifyItemChanged(currentPos);
                            Toast.makeText(context, "⚠️ Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Nút trừ
        h.btnMinus.setOnClickListener(v -> {
            int currentPos = h.getBindingAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            CartItems target = items.get(currentPos);
            int oldQty = target.getQuantity();

            if (oldQty > 1) {
                int newQty = oldQty - 1;
                target.setQuantity(newQty);
                notifyItemChanged(currentPos);

                api.updateCartQuantity(target.getId(), new QuantityRequest(newQty))
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    notifyCartChanged(); // ✅ cập nhật badge
                                } else {
                                    target.setQuantity(oldQty);
                                    notifyItemChanged(currentPos);
                                    Toast.makeText(context, "❌ Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                target.setQuantity(oldQty);
                                notifyItemChanged(currentPos);
                                Toast.makeText(context, "⚠️ Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút xoá
        h.btnDelete.setOnClickListener(v -> {
            int currentPos = h.getBindingAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            CartItems target = items.get(currentPos);
            api.removeFromCart(target.getId()).enqueue(new retrofit2.Callback<Void>() {
                @Override public void onResponse(Call<Void> c, Response<Void> r) {
                    if (r.isSuccessful()) {
                        int idx = indexOfId(target.getId());
                        if (idx != -1) {
                            items.remove(idx);
                            notifyItemRemoved(idx);
                        }
                        notifyCartChanged(); // ✅ cập nhật badge
                        Toast.makeText(context, "Đã xoá", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(Call<Void> c, Throwable t) {
                    Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private int indexOfId(int id) {
        for (int i = 0; i < items.size(); i++) if (items.get(i).getId() == id) return i;
        return -1;
    }

    @Override public int getItemCount() { return items.size(); }

    // ✅ Hàm tính lại tổng số lượng và gọi listener
    private void notifyCartChanged() {
        if (listener != null) {
            int total = 0;
            for (CartItems ci : items) {
                total += ci.getQuantity();
            }
            listener.onCartUpdated(total);
        }
    }
}
