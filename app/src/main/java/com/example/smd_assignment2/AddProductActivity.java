package com.example.smd_assignment2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class AddProductActivity extends Activity {
		String name;
		String description;
		String price;
		IProductDAO productDAO;

		@Override
		protected void onCreate(@Nullable Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.add_product);

				productDAO = new ProductDAO(this);
				EditText nameTxt = (EditText) findViewById(R.id.product_name_value);
				EditText descriptionTxt = (EditText) findViewById(R.id.product_description_value);
				EditText priceTxt = (EditText) findViewById(R.id.product_price_value);

				Button saveBtn = (Button) findViewById(R.id.product_save_btn);
				saveBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								Product product = new Product(descriptionTxt.getText().toString(), nameTxt.getText().toString(), "$" + priceTxt.getText().toString(), 0);
								product.setDao(productDAO);
								product.save();

								Intent intent = new Intent();
								intent.putExtra("product", product);
								setResult(RESULT_OK, intent);
								finish();
						}
				});
		}
}
