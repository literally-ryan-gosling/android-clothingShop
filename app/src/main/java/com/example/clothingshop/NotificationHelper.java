package com.example.clothingshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.content.ContextCompat;

import androidx.core.app.NotificationCompat;


public class NotificationHelper {
    private static final String CHANNEL_ID = "shop_notification_channel";
    private final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifyManager;
    private Context mContext;


    public NotificationHelper(Context context) {
        this.mContext = context;
        this.mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "Shop Notification", NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setDescription("Notifications from Shop application");

        mNotifyManager.createNotificationChannel(channel);
    }

    public void send(String message) {

        if (!isConnectedToInternet()) {
            return; // Ha nincs internetkapcsolat, ne küldjön értesítést
        }

        Intent intent = new Intent(mContext, ShopListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Shop Application")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_shopping_cart)
                .setContentIntent(pendingIntent);

        mNotifyManager.notify(NOTIFICATION_ID, builder.build());
    }

    private boolean isConnectedToInternet() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            android.net.Network network = cm.getActiveNetwork();
            android.net.NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }


    public void cancel() {
        mNotifyManager.cancel(NOTIFICATION_ID);
    }
}
