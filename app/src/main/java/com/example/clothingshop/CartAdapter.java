package com.example.clothingshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final ArrayList<ShoppingItem> mCartItems;
    private final CartActivity mContext;

    public CartAdapter(CartActivity context, ArrayList<ShoppingItem> cartItems) {
        this.mContext = context;
        this.mCartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem currentItem = mCartItems.get(position);

        holder.mTitleText.setText(currentItem.getName());
        holder.mPriceText.setText(currentItem.getPrice());
        holder.mQuantityText.setText(String.valueOf(currentItem.getQuantity()));
        Glide.with(mContext).load(currentItem.getImageResource()).into(holder.mItemImage);

        // Mennyiség növelése
        holder.mIncreaseButton.setOnClickListener(v -> {
            currentItem.increaseQuantity();
            holder.mQuantityText.setText(String.valueOf(currentItem.getQuantity()));

            // Frissítjük a CartManager-ben is
            ((CartActivity)mContext).getCartManager().updateItemQuantity(
                    currentItem._getId(),
                    currentItem.getQuantity()
            );

            // Frissítjük az összeget
            ((CartActivity)mContext).updateTotalPrice();
        });

        holder.mDecreaseButton.setOnClickListener(v -> {
            if (currentItem.getQuantity() > 1) {
                currentItem.decreaseQuantity();
                holder.mQuantityText.setText(String.valueOf(currentItem.getQuantity()));

                // Frissítjük a CartManager-ben is
                ((CartActivity)mContext).getCartManager().updateItemQuantity(
                        currentItem._getId(),
                        currentItem.getQuantity()
                );
            } else {
                mCartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mCartItems.size());

                // Frissítjük a CartManager-ben is
                ((CartActivity)mContext).getCartManager().updateItemQuantity(
                        currentItem._getId(),
                        0
                );
            }

            // Frissítjük az összeget
            ((CartActivity)mContext).updateTotalPrice();
        });
    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitleText;
        public final TextView mPriceText;
        public final TextView mQuantityText;
        public final ImageView mItemImage;
        public final Button mIncreaseButton;
        public final Button mDecreaseButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.cartItemTitle);
            mPriceText = itemView.findViewById(R.id.cartItemPrice);
            mQuantityText = itemView.findViewById(R.id.cartItemQuantity);
            mItemImage = itemView.findViewById(R.id.cartItemImage);
            mIncreaseButton = itemView.findViewById(R.id.increaseButton);
            mDecreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }
}