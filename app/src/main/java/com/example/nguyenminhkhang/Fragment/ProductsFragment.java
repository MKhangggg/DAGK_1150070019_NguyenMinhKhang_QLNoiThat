package com.example.nguyenminhkhang.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenminhkhang.Adapter.Admin_ProductAdapter;
import com.example.nguyenminhkhang.Models.Product;
import com.example.nguyenminhkhang.Network.ApiService;
import com.example.nguyenminhkhang.Network.RetrofitClient;
import com.example.nguyenminhkhang.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment {

    private TextInputEditText edtName, edtPrice, edtImageUrl, edtDescription;
    private MaterialButton btnSave, btnClear;
    private RecyclerView recyclerProducts;

    private final List<Product> productList = new ArrayList<>();
    private Admin_ProductAdapter productAdapter;
    private ApiService apiService;


    private Product productEditing = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);

        apiService = RetrofitClient.getProductService();

        // Bind view
        edtName        = view.findViewById(R.id.edtName);
        edtPrice       = view.findViewById(R.id.edtPrice);
        edtImageUrl    = view.findViewById(R.id.edtImageUrl);
        edtDescription = view.findViewById(R.id.edtDescription);
        btnSave        = view.findViewById(R.id.btnSave);
        btnClear       = view.findViewById(R.id.btnClear);
        recyclerProducts = view.findViewById(R.id.recyclerProducts);

        recyclerProducts.setLayoutManager(new LinearLayoutManager(requireContext()));

        productAdapter = new Admin_ProductAdapter(
                requireContext(),
                productList,
                new Admin_ProductAdapter.OnProductAction() {
                    @Override public void onEdit(Product product) { fillFormForEdit(product); }
                    @Override public void onDelete(Product product, int position) { deleteProduct(product); }
                }
        );
        recyclerProducts.setAdapter(productAdapter);

        btnSave.setOnClickListener(v -> saveOrUpdateProduct());
        btnClear.setOnClickListener(v -> clearForm());

        loadProducts();
        return view;
    }

    /* ===================== CRUD ===================== */

    private void loadProducts() {
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override public void onResponse(Call<List<Product>> call, Response<List<Product>> res) {
                if (!isAdded()) return;
                if (res.isSuccessful() && res.body() != null) {
                    productList.clear();
                    productList.addAll(res.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    toast("Không tải được danh sách sản phẩm");
                }
            }
            @Override public void onFailure(Call<List<Product>> call, Throwable t) {
                if (isAdded()) toast("Lỗi tải sản phẩm: " + t.getMessage());
            }
        });
    }

    private void saveOrUpdateProduct() {
        String name = safeText(edtName);
        String priceStr = safeText(edtPrice);
        String imageUrl = safeText(edtImageUrl);
        String description = safeText(edtDescription);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
            toast("Vui lòng nhập TÊN và GIÁ");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            toast("Giá không hợp lệ");
            return;
        }

        setSaving(true);

        if (productEditing == null) {
            // THÊM MỚI
            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setPrice(price);
            newProduct.setImageUrl(imageUrl);
            newProduct.setDescription(description);
            imageUrl = imageUrl.isEmpty() ? null : imageUrl;

            apiService.createProduct(newProduct).enqueue(new Callback<Product>() {
                @Override public void onResponse(Call<Product> call, Response<Product> res) {
                    setSaving(false);
                    if (!isAdded()) return;
                    if (res.isSuccessful()) {
                        toast("Đã thêm sản phẩm");
                        clearForm();
                        loadProducts();
                    } else {
                        toast("Thêm sản phẩm thất bại");
                    }
                }
                @Override public void onFailure(Call<Product> call, Throwable t) {
                    setSaving(false);
                    if (isAdded()) toast("Lỗi thêm: " + t.getMessage());
                }
            });

        } else {
            // CẬP NHẬT
            productEditing.setName(name);
            productEditing.setPrice(price);
            productEditing.setImageUrl(imageUrl);
            productEditing.setDescription(description);
            imageUrl = imageUrl.isEmpty() ? null : imageUrl;

            apiService.updateProduct(productEditing.getId(), productEditing).enqueue(new Callback<Void>() {
                @Override public void onResponse(Call<Void> call, Response<Void> res) {
                    setSaving(false);
                    if (!isAdded()) return;
                    if (res.isSuccessful()) {
                        toast("Đã cập nhật sản phẩm");
                        clearForm();
                        loadProducts();
                    } else {
                        toast("Cập nhật thất bại");
                    }
                }
                @Override public void onFailure(Call<Void> call, Throwable t) {
                    setSaving(false);
                    if (isAdded()) toast("Lỗi cập nhật: " + t.getMessage());
                }
            });
        }
    }

    private void deleteProduct(Product product) {
        apiService.deleteProduct(product.getId()).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> res) {
                if (!isAdded()) return;
                if (res.isSuccessful()) {
                    toast("Đã xóa sản phẩm");
                    // Nếu đang sửa đúng item vừa xoá → reset form
                    if (productEditing != null && productEditing.getId() == product.getId()) {
                        clearForm();
                    }
                    loadProducts();
                } else {
                    toast("Xóa sản phẩm thất bại");
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                if (isAdded()) toast("Lỗi xóa: " + t.getMessage());
            }
        });
    }

    /* ===================== FORM HELPERS ===================== */

    private void fillFormForEdit(@NonNull Product p) {
        edtName.setText(p.getName());
        edtPrice.setText(String.valueOf(p.getPrice()));
        edtImageUrl.setText(p.getImageUrl());
        edtDescription.setText(p.getDescription());
        productEditing = p;
        btnSave.setText("Cập nhật");
    }

    private void clearForm() {
        edtName.setText("");
        edtPrice.setText("");
        edtImageUrl.setText("");
        edtDescription.setText("");
        productEditing = null;
        btnSave.setText("Lưu");
    }

    private void setSaving(boolean saving) {
        btnSave.setEnabled(!saving);
        btnClear.setEnabled(!saving);
    }

    private String safeText(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void toast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
