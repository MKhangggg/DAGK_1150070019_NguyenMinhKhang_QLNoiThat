package com.example.nguyenminhkhang.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyenminhkhang.Adapter.Admin_ProductAdapter;
import com.example.nguyenminhkhang.Models.Product;
import com.example.nguyenminhkhang.R;
import com.example.nguyenminhkhang.ViewModels.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ProductViewModel viewModel;
    private final List<Product> data = new ArrayList<>();
    private Admin_ProductAdapter adapter;
    private final List<Product> productList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // ViewModel để auto cập nhật khi CRUD ở nơi khác
        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        RecyclerView rv = v.findViewById(R.id.recyclerAdminProducts);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // ✅ Chỉ hiển thị (ẩn nút sửa/xóa)
        adapter = new Admin_ProductAdapter(requireContext(), productList, /*showActions=*/false);
        rv.setAdapter(adapter);

        // Quan sát dữ liệu để tự update
        viewModel.products.observe(getViewLifecycleOwner(), list -> {
            productList.clear();
            if (list != null) productList.addAll(list);
            adapter.notifyDataSetChanged();
        });

        // Lần đầu nạp
        viewModel.loadProducts();

        return v;
    }
}
