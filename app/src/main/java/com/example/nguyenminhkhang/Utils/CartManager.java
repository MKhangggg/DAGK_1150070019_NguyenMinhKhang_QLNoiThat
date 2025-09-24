package com.example.nguyenminhkhang.Utils;

import com.example.nguyenminhkhang.Models.CartItems;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItems> cartList = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItems> getCartList() {
        return cartList;
    }

    public void addToCart(CartItems item) {
        for (CartItems i : cartList) {
            if (i.getProductId() == item.getProductId()) {
                i.setQuantity(i.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartList.add(item);
    }

    public void updateQuantity(int productId, int newQty) {
        for (CartItems i : cartList) {
            if (i.getProductId() == productId) {
                i.setQuantity(newQty);
                return;
            }
        }
    }

    public void removeFromCart(int productId) {
        cartList.removeIf(i -> i.getProductId() == productId);
    }

    public int getTotalItems() {
        int total = 0;
        for (CartItems i : cartList) {
            total += i.getQuantity();
        }
        return total;
    }
}
