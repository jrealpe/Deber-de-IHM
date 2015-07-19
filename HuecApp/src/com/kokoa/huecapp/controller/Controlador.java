package com.kokoa.huecapp.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.kokoa.huecapp.json.JSONParser;

import android.util.Log;
/**
 * Clase Controlador que permite recibir/enviar datos desde el servidor
 * */
public class Controlador {
	
	private JSONParser jsonParser;
	
	private static String URL = "http://104.131.50.216:8888/";
	// constructor
	public Controlador(){
		jsonParser = new JSONParser();
	}
	
	public JSONObject registrar(String nombre,String apellido,String correo, String username, String contraseña)
	{
			// Se construyen los parametros que se enviarán al servidor
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("first_name", nombre));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("last_name", apellido));
			params.add(new BasicNameValuePair("email", correo));
			params.add(new BasicNameValuePair("password", contraseña));
			//params.add(new BasicNameValuePair("csrfmiddlewaretoken", "76V9DGrCuw0JzGLeq7WQ2DgIRpqOlK4Z"));
			// getting JSON Object
			
			JSONObject json = jsonParser.postJSONFromUrl(URL+"signup/", params);
			
			return json;
	}
	
	public JSONObject login(String username, String contraseña)
	{
			// Se construyen los parametros que se enviarán al servidor
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", contraseña));
			//params.add(new BasicNameValuePair("csrfmiddlewaretoken", "76V9DGrCuw0JzGLeq7WQ2DgIRpqOlK4Z"));
			// getting JSON Object
			
			JSONObject json = jsonParser.postJSONFromUrl(URL+"login/", params);
			
			return json;
	}
	
	public JSONObject IngresoPlato(String name)
	{
			// Se construyen los parametros que se enviarán al servidor
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			//params.add(new BasicNameValuePair("csrfmiddlewaretoken", "76V9DGrCuw0JzGLeq7WQ2DgIRpqOlK4Z"));
			// getting JSON Object
			
			JSONObject json = jsonParser.postJSONFromUrl(URL+"Platos/", params);
			
			return json;
	}
	
	public JSONObject IngresoPlato_Restaurante(String id_restaurante, String Plato, String precio, String Foto) throws UnsupportedEncodingException
	{
			// Se construyen los parametros que se enviarán al servidor
		
			URI uri_foto = null;
			try {
				uri_foto = new URI(Foto);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			File fileFoto = new File(uri_foto.getPath());
			ContentBody contentFoto = new FileBody(fileFoto);// ruta
			MultipartEntity params = new MultipartEntity();

			params.addPart("restaurant", new StringBody(id_restaurante));
			params.addPart("price", new StringBody(precio));
			params.addPart("name", new StringBody(Plato));
			params.addPart("image_dish", contentFoto);
			
			JSONObject json = jsonParser.postJSONFromUrl(URL+"RestaurantPlato/", params);
			
			return json;
	}
	
	public JSONObject IngresoRestaurante(String name, String place, String longitud, String latitud, String foto) throws UnsupportedEncodingException
	{
			// Se construyen los parametros que se enviarán al servidor
		MultipartEntity params = new MultipartEntity();
		if (!foto.equals("")){
			URI uri_foto = null;
			try {
				uri_foto = new URI(foto);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			File fileFoto = new File(uri_foto.getPath());
			ContentBody contentFoto = new FileBody(fileFoto);// ruta
			params.addPart("image_dish", contentFoto);
		}

		params.addPart("name", new StringBody(name));
		params.addPart("place", new StringBody(place));
		params.addPart("longitude", new StringBody(longitud));
		params.addPart("latitud", new StringBody(longitud));
		
		
		JSONObject json = jsonParser.postJSONFromUrl(URL+"Restaurantes" +
				"/", params);
		
		return json;
	}
	
}