package com.kokoa.huecapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.kokoa.huecapp.json.JSONParser;

import android.util.Log;

/**
 * Clase Controlador que permite recibir/enviar datos desde el servidor
 * */
public class Controlador {
	
	private JSONParser jsonParser;
	
	private static String URL = "http://kokoa.espol.edu.ec:9090/signup/";
	private static String URL2 = "http://kokoa.espol.edu.ec:9090/login/";
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
			
			JSONObject json = jsonParser.postJSONFromUrl(URL, params);
			
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
			
			JSONObject json = jsonParser.postJSONFromUrl(URL2, params);
			
			return json;
	}
	
}