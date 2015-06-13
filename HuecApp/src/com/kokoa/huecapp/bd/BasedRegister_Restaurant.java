package com.kokoa.huecapp.bd;



import java.util.ArrayList;

import com.kokoa.huecapp.MainActivity;
import com.kokoa.huecapp.classes.Restaurant;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class BasedRegister_Restaurant extends SQLiteOpenHelper {
	
	public BasedRegister_Restaurant(Context context) {
		super(context, "Restaurante", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE restaurante(id INTEGER, name TEXT, place TEXT, latitude FLOAT, longitude FLOAT, image_restaurant TEXT)";
		db.execSQL(sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int va, int vn) {
		db.execSQL("DROP TABLE IF EXIST restaurante");
		onCreate(db);
	}
	
	public void insertarRest(int id,String name,String place,float latitude,float longitude,String image_restaurant){
		ContentValues valores=new ContentValues();
		valores.put("image_restaurant", image_restaurant);
		valores.put("longitude", longitude);
		valores.put("latitude", latitude);
		valores.put("place", place);
		valores.put("name", name);
		valores.put("id", id);
		this.getWritableDatabase().insert("restaurante", null, valores);
	}
	
	public ArrayList<Restaurant> getRestaurants() {
		ArrayList<Restaurant> restaurants_list = new ArrayList<Restaurant>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM restaurante", null);

		if (cursor.moveToFirst()) {
			
			//Recorremos el cursor hasta que no haya más registros
			do {
				int id;
				float latitude,longitude;
				String name, place, image;
				
				id = cursor.getInt(0);
				name = cursor.getString(1);
				place = cursor.getString(2);
				latitude = cursor.getFloat(3);
				longitude = cursor.getFloat(4);
				image = cursor.getString(5);
			
				restaurants_list.add(new Restaurant(id, name, place, latitude, longitude, image));
				
				
			} while (cursor.moveToNext());

		}
		
		return restaurants_list;

	}
	
	public void abrir(){
		this.getWritableDatabase();
	}
	
	public void cerrar(){
		this.close();
	}
	
	public int contar(){
		
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM restaurante", null);
		return cursor.getCount();
	}
	
	public boolean estaVacia(){
		boolean vacia=true;
		SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM restaurante", null);

        if(cursor.getCount() == 0){
            vacia=true;
        }
        if(cursor.getCount() > 0){
            vacia=false;
        }

        cursor.close();
		return vacia;
	}
}