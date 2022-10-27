package com.example.smd_assignment2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
		private ArrayList<Product> products;
		private ArrayList<Product> filteredProducts;
		private ProductItemClickListener listener;
		private Filter filter;

		public class ProductViewHolder extends RecyclerView.ViewHolder {
				public ImageView image;
				public TextView name;
				public RatingBar simpleRatingBar;
				public TextView price;

				public ProductViewHolder(View v){
						super(v);
						image = (ImageView) v.findViewById(R.id.product_image);
						name = (TextView) v.findViewById(R.id.product_name);
						simpleRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
						price = (TextView) v.findViewById(R.id.product_price);

						v.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
										int index = (int) v.getTag();
										listener.onClick(filteredProducts.get(index));
								}
						});
				}
		}

		public ProductAdapter(ArrayList<Product> ds, ProductItemClickListener ls){
				this.products = ds;
				this.filteredProducts = ds;
				this.listener = ls;
		}

		@Override
		public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
				View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
				return new ProductViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ProductViewHolder holder, int position) {
				Product product = filteredProducts.get(position);
				holder.image.setImageResource(product.getImgResourceId());
				holder.name.setText(product.getName());
				holder.simpleRatingBar.setRating(product.getRating());
				holder.price.setText(product.getPrice());
				holder.itemView.setTag(position);
		}

		@Override
		public int getItemCount() {
				return filteredProducts.size();
		}

		public interface ProductItemClickListener {
				void onClick(Product p);
		}

		@Override
		public Filter getFilter() {
				if (filter == null){
						filter = new ProductFilter();
				}
				return filter;
		}

		private class ProductFilter extends Filter {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
						FilterResults filterResults = new FilterResults();

						if (constraint != null && constraint.length() > 0){
								ArrayList<Product> filteredList = new ArrayList<Product>();
								for (int i = 0; i < products.size(); i++){
										if (products.get(i).getName().toLowerCase(Locale.ROOT).contains(constraint) ||
														products.get(i).getName().contains(constraint)
										){
												filteredList.add(products.get(i));
										}
								}

								filterResults.count = filteredList.size();
								filterResults.values = filteredList;
						}

						else {
								filterResults.count = products.size();
								filterResults.values = products;
						}

						return filterResults;
				}

				@Override
				protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
						filteredProducts = (ArrayList<Product>) filterResults.values;
						notifyDataSetChanged();
				}
		}
}
