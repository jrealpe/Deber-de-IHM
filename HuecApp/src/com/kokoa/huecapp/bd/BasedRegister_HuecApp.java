package com.kokoa.huecapp.bd;

import java.util.ArrayList;

import com.kokoa.huecapp.MainActivity;
import com.kokoa.huecapp.classes.Category;
import com.kokoa.huecapp.classes.Restaurant;
import com.kokoa.huecapp.classes.Restaurant_Dish;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class BasedRegister_HuecApp extends SQLiteOpenHelper {

	public BasedRegister_HuecApp(Context context) {
		super(context, "HuecApp", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql1 = "CREATE TABLE usuario(id TEXT, nombre TEXT, apellido TEXT, correo TEXT, username TEXT, contrasena TEXT, estado TEXT)";
		db.execSQL(sql1);
		
		String sql2 = "CREATE TABLE restaurante(id INTEGER, name TEXT, place TEXT, description TEXT, latitude FLOAT, longitude FLOAT, image_restaurant TEXT)";
		db.execSQL(sql2);

		String sql3 = "CREATE TABLE restaurante_plato(id INTEGER, name TEXT, price TEXT, restaurant INTEGER, image_dish TEXT)";
		db.execSQL(sql3);
		
		String sql4 = "CREATE TABLE categoria(id INTEGER, name TEXT)";
		db.execSQL(sql4);
		
		String sql5 = "CREATE TABLE update_info(flag TEXT)";
		db.execSQL(sql5);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int va, int vn) {
		db.execSQL("DROP TABLE IF EXIST usuario");
		db.execSQL("DROP TABLE IF EXIST restaurante");
		db.execSQL("DROP TABLE IF EXIST restaurante_plato");
		db.execSQL("DROP TABLE IF EXIST categoria");
		onCreate(db);
	}

	public void insertarReg(String id, String correo, String usuario,
			String contrasena, String nombre, String apellido, String estado) {
		ContentValues valores = new ContentValues();
		valores.put("contrasena", contrasena);
		valores.put("estado", estado);
		valores.put("correo", correo);
		valores.put("username", usuario);
		valores.put("apellido", apellido);
		valores.put("nombre", nombre);
		valores.put("id", id);
		this.getWritableDatabase().insert("usuario", null, valores);
	}
	
	public void insertarRest(int id,String name,String place,String description, float latitude,float longitude,String image_restaurant){
		ContentValues valores=new ContentValues();
		valores.put("image_restaurant", image_restaurant);
		valores.put("longitude", longitude);
		valores.put("latitude", latitude);
		valores.put("description", description);
		valores.put("place", place);
		valores.put("name", name);
		valores.put("id", id);
		this.getWritableDatabase().insert("restaurante", null, valores);
	}
											
	public void insertarRest_Plat(int id,String name, String precio, int id_restaurant, String foto){
		ContentValues valores=new ContentValues();
		valores.put("image_dish", foto);
		valores.put("name", name);
		valores.put("price", precio);
		valores.put("restaurant", id_restaurant);
		valores.put("id", id);
		this.getWritableDatabase().insert("restaurante_plato", null, valores);
	}
	
	public void insertarCategory(int id, String name){
		ContentValues valores=new ContentValues();
		valores.put("name", name);
		valores.put("id", id);
		this.getWritableDatabase().insert("categoria", null, valores);
	}
	
	public void insertarUpdate(String flag){
		ContentValues valores = new ContentValues();
		valores.put("flag", flag);
		this.getWritableDatabase().insert("update_info", null, valores);
	}

	public void abrir() {
		this.getWritableDatabase();
	}

	public void cerrar() {
		this.close();
	}

	/*
	public int contar() {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM usuario", null);
		return cursor.getCount();
	}
	*/
	
	public boolean usuario_isEmpty() {
		boolean vacia = true;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM usuario", null);

		if (cursor.getCount() == 0) {
			vacia = true;
		}
		if (cursor.getCount() > 0) {
			vacia = false;
		}

		cursor.close();
		return vacia;
	}
	
	
	//Table resturant
	
	public final Restaurant getRestaurant(String title) {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM restaurante", null);
		Restaurant restaurant = null;
		
		if (cursor.moveToFirst()) {

			// Recorremos el cursor hasta que encontremos el requerido
			do {
				
				String name = cursor.getString(1);
				if (name.equals(title)) {
					int id;
					float latitude, longitude;
					String place, image, description;

					id = cursor.getInt(0);
					place = cursor.getString(2);
					description = cursor.getString(3);
					latitude = cursor.getFloat(4);
					longitude = cursor.getFloat(5);
					image = cursor.getString(6);

					restaurant = new Restaurant(id, name, place, "",
							latitude, longitude, image);
					break;
				}


			} while (cursor.moveToNext());

		}
		
		return restaurant;

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
				String name, place, image, description;
				
				id = cursor.getInt(0);
				name = cursor.getString(1);
				place = cursor.getString(2);
				description = cursor.getString(3);
				latitude = cursor.getFloat(4);
				longitude = cursor.getFloat(5);
				image = cursor.getString(6);
			
				restaurants_list.add(new Restaurant(id, name, place,"",latitude, longitude, image));
				
				
			} while (cursor.moveToNext());

		}
		
		cursor.close();
		
		return restaurants_list;

	}
	
	
	//Table dish
	public ArrayList<Restaurant_Dish> getDishes() {

		ArrayList<Restaurant_Dish> dishes_list = new ArrayList<Restaurant_Dish>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM restaurante_plato", null);

		if (cursor.moveToFirst()) {

			// Recorremos el cursor hasta que no haya más registros
			do {
				int id, id_rest, votes;
				String name, image_dish,price;
											
				id = cursor.getInt(0);
				name = cursor.getString(1);
				price = cursor.getString(2);
				id_rest = cursor.getInt(3);
				image_dish = cursor.getString(4);

				dishes_list.add(new Restaurant_Dish(id, id_rest, price, name, image_dish));
				

			} while (cursor.moveToNext());

		}

		cursor.close();

		return dishes_list;

	}
	

	public boolean getUpdate() {

		ArrayList<Restaurant_Dish> dishes_list = new ArrayList<Restaurant_Dish>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM update_info", null);

		if (cursor.moveToFirst()) {

			// Recorremos el cursor hasta que no haya más registros
			do {
				String flag;
				
				flag = cursor.getString(0);
				if(flag.equals("1")) return true;
				else return false;

			} while (cursor.moveToNext());

		}else{
			
			return false;
		}
			

	}
	
	
	//Table dish
	public ArrayList<Category> getCategories() {

		ArrayList<Category> categories_list = new ArrayList<Category>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM categoria", null);

		if (cursor.moveToFirst()) {

			// Recorremos el cursor hasta que no haya más registros
			do {
				String name;
				int id;
				id = cursor.getInt(0);
				name = cursor.getString(1);

				categories_list.add(new Category(id,name));

			} while (cursor.moveToNext());

		}

		cursor.close();

		return categories_list;

	}
	
	public boolean restaurant_isEmpty(){
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