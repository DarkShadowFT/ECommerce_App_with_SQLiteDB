package com.example.smd_assignment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDBHelper extends SQLiteOpenHelper {
		private static ProductDBHelper mInstance = null;
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "Products.db";

		public static ProductDBHelper getInstance(Context ctx) {

				// Use the application context, which will ensure that you
				// don't accidentally leak an Activity's context.
				// See this article for more information: http://bit.ly/6LRzfx
				if (mInstance == null) {
						mInstance = new ProductDBHelper(ctx.getApplicationContext());
				}
				return mInstance;
		}

		public ProductDBHelper(Context context){
				super(context,DATABASE_NAME,null,DATABASE_VERSION);
		}
		public void onCreate(SQLiteDatabase db){
				String sql = "CREATE TABLE Products (id TEXT PRIMARY KEY, " +
								"description TEXT," +
								"name TEXT," +
								"price TEXT," +
								"rating TEXT," +
								"quantity TEXT," +
								"imgResourceId TEXT)";
				db.execSQL(sql);
		}
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS Products");
				onCreate(db);
		}
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				onUpgrade(db,oldVersion,newVersion);
		}
}
