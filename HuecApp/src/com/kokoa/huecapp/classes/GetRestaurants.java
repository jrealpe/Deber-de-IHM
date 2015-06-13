package com.kokoa.huecapp.classes;

import java.util.ArrayList;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.kokoa.huecapp.bd.BasedRegister_Restaurant;
import com.kokoa.huecapp.json.JSONParser;

public class GetRestaurants extends AsyncTask<String, Void, String> {

	public final static String URL_GETRESTAURANTS = "http://kokoa.espol.edu.ec:9090/restaurants/";
	private ArrayList<Restaurant> restaurants_list = new ArrayList<Restaurant>();
	private ProgressDialog dialog;
	private Context context;
	private Activity activity;
	private BasedRegister_Restaurant helper;

	public GetRestaurants(Activity activity) {
		this.activity = activity;
		context = activity;
		dialog = new ProgressDialog(context);
		helper = new BasedRegister_Restaurant(context);
	}

	@Override
	protected void onPreExecute() {
		
		dialog.setTitle("Espere Por Favor");
		dialog.setMessage("Actualizando Platos...");
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	protected void onPostExecute(String result) {
		
		dialog.dismiss();
		if (result.equals("True")) {

			// SI la Base Datos esta llena, eliminamos para actualizarla
			if (!helper.estaVacia()) {

				activity.deleteDatabase("restaurante");
				helper = new BasedRegister_Restaurant(context);

			}

			// Abrimos conexión de escritura con la BD restaurante
			helper.abrir();

			for (int j = 0; j < restaurants_list.size(); j++) {
				Restaurant rest = restaurants_list.get(j);
				helper.insertarRest(rest.getId(), rest.getName(),
						rest.getPlace(), rest.getLatitude(),
						rest.getLongitude(), rest.getImage_restaurant());

			}
			
			//Cerramos conexión de escritura con la BD resturante
			helper.close();
			Toast.makeText(activity,
					"Correcto, Información Actualizada", Toast.LENGTH_SHORT)
					.show();

		} else if (result.equals("Empty")) {
			Toast.makeText(activity,
					"Advertencia, No se encontraron registros!", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(activity,
					"Error, No se pudo establecer conexión!", Toast.LENGTH_SHORT)
					.show();
		}
		

	}

	@Override
	protected String doInBackground(String... args) {
		String result = "False";
		JSONParser json_parcer = new JSONParser();
		JSONArray restaurants_array = null;
		JSONObject restaurant_object = null;

		try {
			
			// Obtenemos el array de restaurantes
			restaurant_object = json_parcer.getJSONFromUrl(URL_GETRESTAURANTS);
			restaurants_array = restaurant_object.getJSONArray("results");
			
			Log.i("huecapp-length", String.valueOf(restaurants_array.length()));
			if (restaurants_array.length() != 0) {
				for (int i = 0; i < restaurants_array.length(); i++) {

					// Obtenemos los restaurantes del array
					restaurant_object = restaurants_array.getJSONObject(i);

					int id = Integer.valueOf(restaurant_object
							.getString("id"));
					String name = restaurant_object.getString("name");
					String place = restaurant_object.getString("place");
					float latitude = Float.valueOf(restaurant_object
							.getString("latitude"));
					float longitude = Float.valueOf(restaurant_object
							.getString("longitude"));
					String image_restaurant = restaurant_object
							.getString("image_restaurant");

					// Creamos un objeto de tipo Restaurante
					Restaurant rest = new Restaurant(id, name, place,
							latitude, longitude, image_restaurant);

					// Añadimos a la lista de restaurantes
					restaurants_list.add(rest);
				}

				result = "True";

			} else
				result = "Empty";

		}catch (ParseException e){
	        Log.e("ParseException", e.toString());
	
	    } catch (JSONException e) {
			e.printStackTrace();
			Log.i("huecapp", restaurants_array.toString());
		}

		return result;

	}

}
