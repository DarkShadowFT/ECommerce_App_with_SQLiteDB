package com.example.smd_assignment2;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemClickListener {
		RecyclerView.Adapter adapter;
		private ArrayList<Product> dataSet = new ArrayList<Product>();

		private TextView subtotal;
		private TextView shipping;
		private TextView taxes;
		private TextView total;

		private IProductDAO cartDAO;

		@Override
		protected void onCreate(@Nullable Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.cart);

				// setting recyclerview
				RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cart_list);
				recyclerView.setHasFixedSize(true);

				// use a linear layout manager
				RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
				recyclerView.setLayoutManager(layoutManager);

				cartDAO = new CartDAO(this);
				ProductViewModel productViewModel = new ViewModelProvider(CartActivity.this).get(ProductViewModel.class);
				productViewModel.setDao(cartDAO);
				dataSet = productViewModel.getProducts(savedInstanceState, "cart");

				// specify an adapter (see also next example)
				adapter = new CartAdapter(dataSet, this);
				recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
				recyclerView.setAdapter(adapter);

				Intent intent = getIntent();
				Product product = new Product(intent.getStringExtra("description"),
								intent.getStringExtra("name"), intent.getStringExtra("price"),
								intent.getFloatExtra("rating", 0));
				product.setImgResourceId(intent.getIntExtra("resId", 0));
				product.setId(intent.getStringExtra("id"));

				boolean productExists = false;
				for (int i = 0; i < dataSet.size(); i++){
						if (Objects.equals(dataSet.get(i).getId(), product.getId())){
								productExists = true;
								break;
						}
				}

				if (!productExists){
						dataSet.add(product);
						adapter.notifyDataSetChanged();
				}

				subtotal = (TextView) findViewById(R.id.cart_subtotal_value);
				shipping = (TextView) findViewById(R.id.cart_shipping_value);
				taxes = (TextView) findViewById(R.id.cart_taxes_value);
				total = (TextView) findViewById(R.id.card_total_value);

				Button btnSave = (Button) findViewById(R.id.btn_cart_order);
				btnSave.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								addToCart();
						}
				});

				Button btnCancel = (Button) findViewById(R.id.btn_cart_cancel);
				btnCancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								cancelCart();
						}
				});
				calculateTotal();
		}

		public void calculateTotal(){
				if (dataSet.size() == 0){
						subtotal.setText("$0");
						shipping.setText("FREE");
						taxes.setText("$0");
						total.setText("$0");
						return;
				}

				int subtotal_sum = 0;
				String price;
				int quantity;
				for (int i = 0; i < dataSet.size(); i++){
						price = dataSet.get(i).getPrice();
						quantity = dataSet.get(i).getQuantity();
						subtotal_sum += Integer.parseInt(price.substring(1, price.length())) * quantity;
				}
				subtotal.setText("$" + Integer.toString(subtotal_sum));
				shipping.setText("FREE");

				int taxes_sum = 15;
				taxes.setText("$" + Integer.toString(taxes_sum));

				total.setText("$" + Integer.toString(subtotal_sum + taxes_sum));
		}

		public void addToCart(){
				for (int i = 0; i < dataSet.size(); i++){
						dataSet.get(i).setDao(cartDAO);
						dataSet.get(i).save();
				}
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
		}

		public void cancelCart(){
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
		}

		@Override
		public void onClick(View view, String mode, int index) {
				if (mode.equals("inc")){
						TextView cartProductQuantity = (EditText) view.findViewById(R.id.product_quantity);
						int quantity = parseInt(cartProductQuantity.getText().toString());
						cartProductQuantity.setText(Integer.toString(quantity + 1));
						Product product = dataSet.get(index);
						product.setQuantity(product.getQuantity() + 1);
						dataSet.set(index, product);
						calculateTotal();
				}
				else if (mode.equals("dec")){
						TextView cartProductQuantity = (EditText) view.findViewById(R.id.product_quantity);
						int quantity = parseInt(cartProductQuantity.getText().toString());
						if (quantity > 1){
								cartProductQuantity.setText(Integer.toString(quantity - 1));
								Product product = dataSet.get(index);
								product.setQuantity(product.getQuantity() - 1);
								dataSet.set(index, product);
								calculateTotal();
						}
						else {
								dataSet.get(index).setDao(cartDAO);
								dataSet.get(index).delete();
								dataSet.remove(index);
								adapter.notifyDataSetChanged();
								calculateTotal();
						}
				}
		}

		public void onSaveInstanceState(Bundle state) {
				super.onSaveInstanceState(state);
				state.putSerializable("cart", dataSet);
		}
}
