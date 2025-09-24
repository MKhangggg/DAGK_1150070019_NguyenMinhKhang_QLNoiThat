package com.example.nguyenminhkhang.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nguyenminhkhang.Activity.LoginActivity;
import com.example.nguyenminhkhang.R;

public class ProfileFragment extends Fragment {
    public static ProfileFragment newInstance(String fullName, String email, String role) {
        Bundle b = new Bundle();
        b.putString("fullName", fullName);
        b.putString("email", email);
        b.putString("role", role);
        ProfileFragment f = new ProfileFragment();
        f.setArguments(b);
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle s) {
        View v = inf.inflate(R.layout.fragment_profile, c, false);
        ((TextView)v.findViewById(R.id.tvFullName)).setText(getArguments()!=null? getArguments().getString("fullName",""):"");
        ((TextView)v.findViewById(R.id.tvEmail)).setText(getArguments()!=null? getArguments().getString("email",""):"");
        ((TextView)v.findViewById(R.id.tvRole)).setText(getArguments()!=null? getArguments().getString("role","User"):"User");
        v.findViewById(R.id.btnLogout).setOnClickListener(btn -> {
            startActivity(new Intent(requireContext(), LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            requireActivity().finish();
        });
        return v;
    }
}

