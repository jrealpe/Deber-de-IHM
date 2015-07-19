package com.kokoa.huecapp.function;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.kokoa.huecapp.bd.BasedRegister_HuecApp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class NetworkFunction {

	public static String IdUser(Context context) {

		/*** Obtiene el idServer ****/
		//String Id = "";
		BasedRegister_HuecApp helper = new BasedRegister_HuecApp(context);
		helper.abrir();
		Cursor c = helper.getReadableDatabase().rawQuery(
				"SELECT id,flag FROM usuario", null);
		c.moveToFirst();
		
		do {
			if (c.getString(1).equals("1")) return c.getString(0);

		} while (c.moveToNext());
				
		c.close();
		helper.close();
		return null;

	}
	
	public static boolean verificaConexion(Context ctx) {

		boolean bConectado = false;

		ConnectivityManager connec = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// No sólo wifi, también GPRS
		NetworkInfo[] redes = connec.getAllNetworkInfo();

		// Redes con acceso a internet
		for (int i = 0; i < redes.length; i++) {
			// ¿Tenemos conexión? ponemos a true
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bConectado = true;
			}
		}
		return bConectado;
	}
	
	
	public static boolean isGpsEnabled(Context context) {
		try {
			LocationManager service = (LocationManager) context
					.getSystemService(context.LOCATION_SERVICE);
			return service.isProviderEnabled(LocationManager.GPS_PROVIDER)
					&& service
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			return false;
		}
	}

}
