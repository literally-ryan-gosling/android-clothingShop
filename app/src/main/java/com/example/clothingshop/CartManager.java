package com.example.clothingshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String CART_PREFS = "cart_prefs";
    private static final String CART_ITEMS_KEY = "cart_items";
    private final SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();

    public CartManager(Context context) {
        sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
    }

    public void addToCart(ShoppingItem item) {
        List<ShoppingItem> cartItems = getCartItems();
        boolean itemExists = false;

        // Ellenőrizzük, hogy van-e már ilyen termék a kosárban
        for (ShoppingItem cartItem : cartItems) {
            if (cartItem._getId().equals(item._getId())) {
                cartItem.increaseQuantity();
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            item.setQuantity(1);
            cartItems.add(item);
        }

        saveCart(cartItems);
    }

    public void updateItemQuantity(String itemId, int newQuantity) {
        List<ShoppingItem> cartItems = getCartItems();
        for (int i = 0; i < cartItems.size(); i++) {
            ShoppingItem item = cartItems.get(i);
            if (item._getId().equals(itemId)) {
                if (newQuantity > 0) {
                    item.setQuantity(newQuantity);
                } else {
                    cartItems.remove(i);
                }
                break;
            }
        }
        saveCart(cartItems);
    }

    public void removeFromCart(ShoppingItem item) {
        List<ShoppingItem> cartItems = getCartItems();
        cartItems.remove(item);
        saveCart(cartItems);
    }

    public void clearCart() {
        saveCart(new ArrayList<>());
    }

    public List<ShoppingItem> getCartItems() {
        String json = sharedPreferences.getString(CART_ITEMS_KEY, "[]");

        try {
            Type type = new TypeToken<ArrayList<ShoppingItem>>() {}.getType();
            List<ShoppingItem> items = gson.fromJson(json, type);
            return items != null ? items : new ArrayList<ShoppingItem>();
        } catch (Exception e) {
            Log.e("CartManager", "Hiba a kosár betöltésekor", e);
            return new ArrayList<ShoppingItem>();
        }
    }

    private void saveCart(List<ShoppingItem> cartItems) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CART_ITEMS_KEY, gson.toJson(cartItems));
        editor.apply();
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (ShoppingItem item : getCartItems()) {
            String priceStr = item.getPrice().replaceAll("[^\\d.]", "");
            try {
                total += Double.parseDouble(priceStr) * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return total;
    }
}