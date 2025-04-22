package com.example.clothingshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private OrdersAdapter mAdapter;
    private List<Order> mOrders = new ArrayList<>();
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mFirestore = FirebaseFirestore.getInstance();
        mRecyclerView = findViewById(R.id.ordersRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Rendelés Értesítések";
            String description = "Értesítések rendelésekkel kapcsolatban";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("orders_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        mFirestore.collection("Orders")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mOrders.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = document.toObject(Order.class);
                        order.setId(document.getId());
                        mOrders.add(order);
                    }
                    mAdapter = new OrdersAdapter(this, mOrders, this::deleteOrder);
                    mRecyclerView.setAdapter(mAdapter);
                });
    }

    private void deleteOrder(String orderId) {
        new AlertDialog.Builder(this)
                .setTitle("Rendelés törlése")
                .setMessage("Biztosan törölni szeretnéd ezt a rendelést?")
                .setPositiveButton("Igen", (dialog, which) -> {
                    mFirestore.collection("Orders").document(orderId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Rendelés törölve", Toast.LENGTH_SHORT).show();
                                loadOrders(); // Frissítjük a listát

                                // --- Értesítés ---
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "orders_channel")
                                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                                        .setContentTitle("Rendelés törölve")
                                        .setContentText("A rendelést sikeresen törölted.")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                                notificationManager.notify(1, builder.build());
                            });
                })
                .setNegativeButton("Mégse", null)
                .show();
    }

}