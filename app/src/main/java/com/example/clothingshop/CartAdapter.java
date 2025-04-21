package com.example.clothingshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private int lastAnimatedPosition = -1;

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
        // Mindig a legfrissebb pozíciót használjuk
        final int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) {
            return;
        }

        ShoppingItem currentItem = mCartItems.get(adapterPosition);
        holder.bind(currentItem);

        // Mennyiség növelése
        holder.mIncreaseButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                currentItem.increaseQuantity();
                holder.mQuantityText.setText(String.valueOf(currentItem.getQuantity()));
                mContext.getCartManager().updateItemQuantity(
                        currentItem._getId(),
                        currentItem.getQuantity()
                );
                mContext.updateTotalPrice();
            }
        });

        // Mennyiség csökkentése
        holder.mDecreaseButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                if (currentItem.getQuantity() > 1) {
                    currentItem.decreaseQuantity();
                    holder.mQuantityText.setText(String.valueOf(currentItem.getQuantity()));
                    mContext.getCartManager().updateItemQuantity(
                            currentItem._getId(),
                            currentItem.getQuantity()
                    );
                } else {
                    mCartItems.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, mCartItems.size());
                    mContext.getCartManager().updateItemQuantity(
                            currentItem._getId(),
                            0
                    );
                }
                mContext.updateTotalPrice();
            }
        });

        // Animáció alkalmazása (csak ha még nem animáltuk ezt a pozíciót)
        if (adapterPosition > lastAnimatedPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_cart_item);
            holder.itemView.startAnimation(animation);
            lastAnimatedPosition = adapterPosition;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
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

        public void bind(ShoppingItem item) {
            mTitleText.setText(item.getName());
            mPriceText.setText(item.getPrice());
            mQuantityText.setText(String.valueOf(item.getQuantity()));
            Glide.with(itemView.getContext()).load(item.getImageResource()).into(mItemImage);
        }
    }
}