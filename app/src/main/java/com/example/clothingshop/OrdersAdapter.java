package com.example.clothingshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private final List<Order> mOrders;
    private final OrdersActivity mContext;
    private final OrderDeleteListener mDeleteListener;

    public interface OrderDeleteListener {
        void onOrderDelete(String orderId);
    }

    public OrdersAdapter(OrdersActivity context, List<Order> orders, OrderDeleteListener listener) {
        this.mContext = context;
        this.mOrders = orders;
        this.mDeleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order order = mOrders.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        String dateStr = sdf.format(order.getDate());

        holder.mImageContainer.removeAllViews();
        for (ShoppingItem item : order.getItems()) {
            ImageView imageView = new ImageView(mContext);
            int size = (int) (64 * mContext.getResources().getDisplayMetrics().density);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setImageResource(item.getImageResource()); // 🔥 EZ A LÉNYEG

            holder.mImageContainer.addView(imageView);
        }


        holder.mDateText.setText(dateStr);
        holder.mTotalText.setText(String.format("%,.0f Ft", order.getTotal()));
        holder.mItemCountText.setText(String.format("%d termék", order.getItems().size()));

        holder.mDeleteButton.setOnClickListener(v -> {
            mDeleteListener.onOrderDelete(order.getId());
        });
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mDateText;
        public final TextView mTotalText;
        public final TextView mItemCountText;
        public final Button mDeleteButton;
        public final LinearLayout mImageContainer;


        public ViewHolder(View itemView) {
            super(itemView);
            mDateText = itemView.findViewById(R.id.orderDate);
            mTotalText = itemView.findViewById(R.id.orderTotal);
            mItemCountText = itemView.findViewById(R.id.orderItemCount);
            mDeleteButton = itemView.findViewById(R.id.deleteOrderButton);
            mImageContainer = itemView.findViewById(R.id.imageContainer);

        }
    }
}