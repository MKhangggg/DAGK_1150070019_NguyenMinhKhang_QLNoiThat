// com.example.nguyenminhkhang.ViewModels.ProductViewModel
package com.example.nguyenminhkhang.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nguyenminhkhang.Models.Product;
import com.example.nguyenminhkhang.Network.ApiService;
import com.example.nguyenminhkhang.Network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.*;

public class ProductViewModel extends ViewModel {
    private final ApiService api = RetrofitClient.getProductService();
    private final MutableLiveData<List<Product>> _products = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Product>> products = _products;

    public void loadProducts() {
        api.getProducts().enqueue(new Callback<List<Product>>() {
            @Override public void onResponse(Call<List<Product>> c, Response<List<Product>> r) {
                if (r.isSuccessful() && r.body()!=null) _products.setValue(r.body());
            }
            @Override public void onFailure(Call<List<Product>> c, Throwable t) {}
        });
    }
    public void createProduct(Product p){ api.createProduct(p).enqueue(new Callback<Product>() {
        @Override public void onResponse(Call<Product> c, Response<Product> r){
            if (r.isSuccessful() && r.body()!=null){
                List<Product> cur = new ArrayList<>(_products.getValue()); cur.add(r.body()); _products.setValue(cur);
            }}
        @Override public void onFailure(Call<Product> c, Throwable t){} });}
    public void updateProduct(Product p){ api.updateProduct(p.getId(), p).enqueue(new Callback<Void>() {
        @Override public void onResponse(Call<Void> c, Response<Void> r){
            if (r.isSuccessful()){
                List<Product> cur = new ArrayList<>(_products.getValue());
                for (int i=0;i<cur.size();i++) if (cur.get(i).getId()==p.getId()) { cur.set(i,p); break; }
                _products.setValue(cur);
            }}
        @Override public void onFailure(Call<Void> c, Throwable t){} });}
    public void deleteProduct(int id){ api.deleteProduct(id).enqueue(new Callback<Void>() {
        @Override public void onResponse(Call<Void> c, Response<Void> r){
            if (r.isSuccessful()){
                List<Product> cur = new ArrayList<>(_products.getValue());
                for (int i=0;i<cur.size();i++) if (cur.get(i).getId()==id){ cur.remove(i); break; }
                _products.setValue(cur);
            }}
        @Override public void onFailure(Call<Void> c, Throwable t){} });}
}
