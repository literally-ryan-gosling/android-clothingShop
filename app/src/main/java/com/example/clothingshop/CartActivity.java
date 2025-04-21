package com.example.clothingshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CartAdapter mAdapter;
    private ArrayList<ShoppingItem> mCartItems;
    private TextView mTotalPrice;
    private Button mClearCart, mCheckout;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        try {
            mRecyclerView = findViewById(R.id.cartRecyclerView);
            mTotalPrice = findViewById(R.id.totalPrice);
            mClearCart = findViewById(R.id.clearCart);
            mCheckout = findViewById(R.id.checkout);

            cartManager = new CartManager(this);
            // Biztosítsuk, hogy a lista ne legyen null
            mCartItems = new ArrayList<>();
            List<ShoppingItem> itemsFromManager = cartManager.getCartItems();
            if (itemsFromManager != null) {
                mCartItems.addAll(itemsFromManager);
            }

            // RecyclerView beállítása
            mAdapter = new CartAdapter(this, mCartItems);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);

            updateTotalPrice();

            // Gomb eseményfigyelők
            mClearCart.setOnClickListener(v -> clearCart());
            mCheckout.setOnClickListener(v -> checkout());
        } catch (Exception e) {
            Log.e("CartActivity", "Hiba az inicializálás során", e);
            Toast.makeText(this, "Hiba történt a kosár betöltésekor", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    protected void updateTotalPrice() {
        if (mCartItems == null) {
            mCartItems = new ArrayList<>();
        }

        double total = 0;
        try {
            for (ShoppingItem item : mCartItems) {
                if (item != null && item.getPrice() != null) {
                    String priceStr = item.getPrice().replaceAll("[^\\d.]", "");
                    try {
                        total += Double.parseDouble(priceStr) * item.getQuantity();
                    } catch (NumberFormatException e) {
                        Log.e("CartActivity", "Hibás árformátum: " + item.getPrice(), e);
                    }
                }
            }
            mTotalPrice.setText(String.format("Összesen: %,.0f Ft", total));
        } catch (Exception e) {
            Log.e("CartActivity", "Hiba az összeg számításánál", e);
            mTotalPrice.setText("Összesen: 0 Ft");
        }
    }

    public CartManager getCartManager() {
        return cartManager;
    }

    private void clearCart() {
        cartManager.clearCart();
        mCartItems.clear();
        mAdapter.notifyDataSetChanged();
        updateTotalPrice();

        // Visszajelzés küldése a főaktivításnak
        Intent resultIntent = new Intent();
        resultIntent.putExtra("cartCleared", true);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "A kosár kiürítve", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void checkout() {
        if (mCartItems.isEmpty()) {
            Toast.makeText(this, "A kosár üres", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore.getInstance().collection("Orders").add(createOrderData())
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Rendelés elküldve", Toast.LENGTH_SHORT).show();
                    clearCart();
                    finish();
                });
    }

    private Map<String, Object> createOrderData() {
        Map<String, Object> order = new HashMap<>();
        order.put("items", mCartItems);
        order.put("total", mTotalPrice.getText().toString());
        order.put("timestamp", System.currentTimeMillis());
        return order;
    }
}