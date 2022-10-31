package com.example.smd_assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CartDAO implements IProductDAO {
		private Context context;

		public CartDAO(Context ctx){
				context = ctx;
		}

		@Override
		public void save(Hashtable<String, String> attributes) {
				CartDBHelper dbHelper = CartDBHelper.getInstance(context);
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				if (attributes.size() == 0)
						return;

				ContentValues content = new ContentValues();
				Enumeration<String> keys = attributes.keys();
				while (keys.hasMoreElements()){
						String key = keys.nextElement();
						content.put(key,attributes.get(key));
				}

				String [] arguments = new String[1];
				arguments[0] = attributes.get("id");
				Hashtable obj = load(arguments[0]);

				if (obj.get("id") != null && obj.get("id").equals(arguments[0])){
						db.update("Cart",content,"id = ?",arguments);
				}
				else{
						long rows = db.insert("Cart",null,content);
						System.out.println("Number of rows affected: " + rows);
				}
		}

		@Override
		public void save(ArrayList<Hashtable<String, String>> objects) {
				for(Hashtable<String,String> obj : objects){
						save(obj);
				}
		}

		@Override
		public ArrayList<Hashtable<String, String>> load() {
				CartDBHelper dbHelper = CartDBHelper.getInstance(context);
				SQLiteDatabase db = dbHelper.getReadableDatabase();

				String query = "SELECT * FROM Cart";
				Cursor cursor = db.rawQuery(query,null);

				ArrayList<Hashtable<String,String>> objects = new ArrayList<Hashtable<String, String>>();
				while(cursor.moveToNext()){
						Hashtable<String,String> obj = new Hashtable<String, String>();
						String [] columns = cursor.getColumnNames();
						int columnIndex;
						for(String col : columns){
								columnIndex = cursor.getColumnIndex(col);
								if (columnIndex != -1){
										obj.put(col.toLowerCase(),cursor.getString(columnIndex));
								}
						}
						objects.add(obj);
				}

				return objects;
		}

		@Override
		public Hashtable<String, String> load(String id) {
				CartDBHelper dbHelper = CartDBHelper.getInstance(context);
				SQLiteDatabase db = dbHelper.getReadableDatabase();

				String query = "SELECT * FROM Cart WHERE id = ?";
				String[] arguments = new String[1];
				arguments[0] = id;
				Cursor cursor = db.rawQuery(query,arguments);


				Hashtable<String,String> obj = new Hashtable<String, String>();
				while(cursor.moveToNext()){
						String [] columns = cursor.getColumnNames();
						int columnIndex;
						for(String col : columns){
								columnIndex = cursor.getColumnIndex(col);
								if (columnIndex != -1){
										obj.put(col.toLowerCase(),cursor.getString(columnIndex));
								}
						}
				}

				return obj;
		}

		@Override
		public void emptyTable(){
				CartDBHelper dbHelper = CartDBHelper.getInstance(context);
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				long rows = db.delete("Cart", "", null);
				System.out.println("Number of rows deleted: " + rows);
		}

		@Override
		public void delete(String id){
				CartDBHelper dbHelper = CartDBHelper.getInstance(context);
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				ArrayList<Hashtable<String, String>> results = load();

				// on below line we are calling a method to delete our
				// course and we are comparing it with our course name.
				long rows = db.delete("Cart", "id=?", new String[]{id});
				System.out.println("Number of rows deleted: " + rows);
				db.close();

		}
}
