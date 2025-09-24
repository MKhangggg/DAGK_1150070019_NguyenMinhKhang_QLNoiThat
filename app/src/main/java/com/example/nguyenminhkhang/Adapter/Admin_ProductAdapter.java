package com.example.nguyenminhkhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nguyenminhkhang.Models.Product;
import com.example.nguyenminhkhang.R;

import java.text.DecimalFormat;
import java.util.List;

public class Admin_ProductAdapter extends RecyclerView.Adapter<Admin_ProductAdapter.ProductViewHolder> {

    public interface OnProductAction {
        void onEdit(Product product);
        void onDelete(Product product, int position);
    }

    private final Context context;
    private final List<Product> productList;
    private final OnProductAction listener;
    private final boolean showActions;

    // Dùng cho ADMIN (có nút sửa/xóa)
    public Admin_ProductAdapter(Context context, List<Product> productList, OnProductAction listener) {
        this(context, productList, listener, true);
    }

    // Dùng cho CHỈ HIỂN THỊ (không nút)
    public Admin_ProductAdapter(Context context, List<Product> productList, boolean showActions) {
        this(context, productList, null, showActions);
    }

    // Constructor chung
    private Admin_ProductAdapter(Context context, List<Product> productList, OnProductAction listener, boolean showActions) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
        this.showActions = showActions;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);

        holder.tvName.setText(p.getName() == null ? "" : p.getName());
        holder.tvDescription.setText(p.getDescription() == null ? "" : p.getDescription());

        // format giá
        double price = p.getPrice();
        holder.tvPrice.setText(new DecimalFormat("#,###").format(price) + " VND");

        Glide.with(context)
                .load(p.getImageUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.imgProduct);

        if (showActions) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnEdit.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEdit(productList.get(pos));
                }
            });

            holder.btnDelete.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDelete(productList.get(pos), pos);
                }
            });
        } else {
            // Ẩn hoàn toàn nút
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnEdit.setOnClickListener(null);
            holder.btnDelete.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvDescription;
        ImageButton btnEdit, btnDelete;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct     = itemView.findViewById(R.id.imgProduct);
            tvName         = itemView.findViewById(R.id.tvProductName);
            tvPrice        = itemView.findViewById(R.id.tvProductPrice);
            tvDescription  = itemView.findViewById(R.id.tvProductDescription);
            btnEdit        = itemView.findViewById(R.id.btnEdit);
            btnDelete      = itemView.findViewById(R.id.btnDelete);
        }
    }
}
