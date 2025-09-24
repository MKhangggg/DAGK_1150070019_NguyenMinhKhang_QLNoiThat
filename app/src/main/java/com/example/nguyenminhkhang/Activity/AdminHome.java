package com.example.nguyenminhkhang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.nguyenminhkhang.Fragment.AccountFragment;
import com.example.nguyenminhkhang.Fragment.HomeFragment;
import com.example.nguyenminhkhang.Fragment.ProductsFragment;
import com.example.nguyenminhkhang.R;
import com.google.android.material.navigation.NavigationView;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        drawerLayout   = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar        = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Make your home BEUTIFUL");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Header “Xin chào, …” (an toàn nếu chưa gắn header)
        if (navigationView.getHeaderCount() > 0) {
            View header = navigationView.getHeaderView(0);
            TextView tvWelcome = header.findViewById(R.id.tvWelcome);
            if (tvWelcome != null) {
                String fullName = getIntent().getStringExtra("fullName");
                String role = getIntent().getStringExtra("role");
                String who = (fullName != null && !fullName.isEmpty())
                        ? fullName
                        : ((role != null && !role.isEmpty()) ? role : "Admin");
                tvWelcome.setText("Xin chào, " + who);
            }
        }

        // Mặc định: Trang chủ Admin (danh sách sản phẩm)
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
            replaceFragment(new HomeFragment());
            setTitleNow("Trang chủ");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            replaceFragment(new HomeFragment());
            setTitleNow("Trang chủ");
        } else if (id == R.id.nav_products) {
            replaceFragment(new ProductsFragment());
            setTitleNow("Quản lý sản phẩm");
        } else if (id == R.id.nav_account) {
            replaceFragment(new AccountFragment());
            setTitleNow("Tài khoản");
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void setTitleNow(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
