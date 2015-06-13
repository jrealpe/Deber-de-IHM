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

public class BasedRegister_User extends SQLiteOpenHelper {

	public BasedRegister_User(Context context) {
		super(context, "Usuario", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE usuario(id TEXT, nombre TEXT, apellido TEXT, correo TEXT, username TEXT, contrasena TEXT, estado TEXT)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int va, int vn) {
		db.execSQL("DROP TABLE IF EXIST usuario");
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

	public void abrir() {
		this.getWritableDatabase();
	}

	public void cerrar() {
		this.close();
	}

	public int contar() {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM usuario", null);
		return cursor.getCount();
	}

	public boolean estaVacia() {
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
}