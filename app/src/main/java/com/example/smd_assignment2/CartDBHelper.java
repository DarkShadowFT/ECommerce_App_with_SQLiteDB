package com.example.smd_assignment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CartDBHelper extends SQLiteOpenHelper {
		private static CartDBHelper mInstance = null;
		public static final int DATABASE_VERSION = 3;
		public static final String DATABASE_NAME = "Cart.db";
		public CartDBHelper(Context context){
				super(context,DATABASE_NAME,null,DATABASE_VERSION);
		}

		public static CartDBHelper getInstance(Context ctx) {
				// Use the application context, which will ensure that you
				// don't accidentally leak an Activity's context.
				// See this article for more information: http://bit.ly/6LRzfx
				if (mInstance == null) {
						mInstance = new CartDBHelper(ctx.getApplicationContext());
				}
				return mInstance;
		}

		public void onCreate(SQLiteDatabase db){
				String sql = "CREATE TABLE Cart (id TEXT PRIMARY KEY, " +
								"description TEXT," +
								"name TEXT," +
								"price TEXT," +
								"rating TEXT," +
								"quantity TEXT," +
								"imgResourceId TEXT)";
				db.execSQL(sql);
		}
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS Cart");
				onCreate(db);
		}
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				onUpgrade(db,oldVersion,newVersion);
		}
}
