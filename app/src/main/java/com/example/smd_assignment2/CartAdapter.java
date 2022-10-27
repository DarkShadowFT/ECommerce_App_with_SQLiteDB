package com.example.smd_assignment2;

import static java.lang.Integer.parseInt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
		private ArrayList<Product> products;
		private CartItemClickListener listener;

		public CartAdapter(ArrayList<Product> ds, CartItemClickListener listener){
				this.products = ds;
				this.listener = listener;
		}

		public class CartViewHolder extends RecyclerView.ViewHolder {
				ImageView image;
				TextView cartProductName;
				TextView cartProductDescription;
				TextView cartProductPrice;
				Button cartButtonIncrement;
				EditText cartProductQuantity;
				Button cartButtonDecrement;

				public CartViewHolder(View view){
						super(view);
						image = (ImageView) view.findViewById(R.id.product_image);
						cartProductName = (TextView) view.findViewById(R.id.product_name);
						cartProductDescription = (TextView) view.findViewById(R.id.product_description);
						cartProductPrice = (TextView) view.findViewById(R.id.product_price);
						cartButtonIncrement = (Button) view.findViewById(R.id.inc_btn);
						cartProductQuantity = (EditText) view.findViewById(R.id.product_quantity);
						cartButtonDecrement = (Button) view.findViewById(R.id.dec_btn);

						cartButtonIncrement.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
										listener.onClick(view, "inc", (int) v.getTag());
								}
						});

						cartButtonDecrement.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
										listener.onClick(view, "dec", (int) v.getTag());
								}
						});
				}
		}

		@NonNull
		@Override
		public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cart_item, parent, false);
				return new CartViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
				Product product = products.get(position);
				holder.image.setImageResource(product.getImgResourceId());
				holder.cartProductName.setText(product.getName());
				holder.cartProductDescription.setText(product.getDescription());
				holder.cartProductPrice.setText(product.getPrice());
				holder.cartButtonIncrement.setTag(position);
				holder.cartButtonDecrement.setTag(position);
				holder.cartProductQuantity.setText(Integer.toString(product.getQuantity()));
		}

		@Override
		public int getItemCount() {
				return products.size();
		}

		public interface CartItemClickListener {
				void onClick(View view, String mode, int index);
		}
}
