package com.example.nguyenminhkhang.Fragment;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nguyenminhkhang.R;
import com.example.nguyenminhkhang.Adapter.UserAdapter;
import com.example.nguyenminhkhang.Network.ApiService;
import com.example.nguyenminhkhang.Network.RetrofitClient;
import com.example.nguyenminhkhang.Models.User;

import java.util.*;
import retrofit2.*;

public class AccountFragment extends Fragment {
    private EditText edtFullName, edtEmail, edtPassword, edtRole;
    private Button btnUpdate;
    private RecyclerView recyclerUsers;
    private ApiService api;
    private UserAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private User editingUser = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_manager, container, false);

        edtFullName = view.findViewById(R.id.edtFullName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtRole = view.findViewById(R.id.edtRole);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        recyclerUsers = view.findViewById(R.id.recyclerUsers);

        recyclerUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEdit(User user) {
                editingUser = user;
                edtFullName.setText(user.getFullName());
                edtEmail.setText(user.getEmail());
                edtPassword.setText("");
                edtRole.setText(user.getRole());
            }

            @Override
            public void onDelete(User user) {
                deleteUser(user.getId());
            }
        });
        recyclerUsers.setAdapter(adapter);

        api = RetrofitClient.getUserService();

        btnUpdate.setOnClickListener(v -> updateUser());
        loadUsers();

        return view;
    }

    private void loadUsers() {
        api.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "Lỗi load user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser() {
        if (editingUser == null) {
            Toast.makeText(getContext(), "Chọn user để sửa", Toast.LENGTH_SHORT).show();
            return;
        }

        editingUser.setFullName(edtFullName.getText().toString());
        editingUser.setEmail(edtEmail.getText().toString());
        editingUser.setPassword(edtPassword.getText().toString());

        String roleInput = edtRole.getText().toString().trim();
        if (!roleInput.isEmpty()) {
            editingUser.setRole(roleInput);
        }

        api.updateUser(editingUser.getId(), editingUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadUsers();
                    editingUser = null;
                } else {
                    Toast.makeText(getContext(), "❌ Lỗi update", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser(int id) {
        api.deleteUser(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadUsers();
                } else {
                    Toast.makeText(getContext(), "❌ Lỗi xóa", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
