package com.kokoa.huecapp.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.dd.nuevo.ProgressGenerator;
import com.dd.processbutton.iml.ActionProcessButton;
import com.hardik.floatinglabel.FloatingLabelView;
import com.kokoa.huecapp.IngresoActivity;
import com.kokoa.huecapp.MainActivity;
import com.kokoa.huecapp.R;
import com.kokoa.huecapp.bd.BasedRegister_User;
import com.kokoa.huecapp.controller.Controlador;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LoginFragment extends Fragment implements ProgressGenerator.OnCompleteListener{
	EditText txtEmail, txtContrasena;
	View view;
	ProgressGenerator progressGenerator;
	ActionProcessButton btnIngreso;
	public LoginFragment(){}
	
	@Override
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		view = inflater.inflate(R.layout.fragment_login, container, false);
		txtEmail=((FloatingLabelView)view.findViewById(R.id.txtCorreo)).getEditText();
		txtContrasena=((FloatingLabelView)view.findViewById(R.id.txtContrasena)).getEditText();
		progressGenerator = new ProgressGenerator(this);
		btnIngreso = (ActionProcessButton) view.findViewById(R.id.btnIngresar);
		btnIngreso.setMode(ActionProcessButton.Mode.ENDLESS);
		btnIngreso.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Ingreso();				
			}
		});

		TextView lblRegistrate=(TextView)view.findViewById(R.id.lblRegistrate); 
		lblRegistrate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IngresoActivity.fragment=new RegistroFragment();
				if (IngresoActivity.fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, IngresoActivity.fragment).commit();
					
				} else {
					
					Log.e("MainActivity", "Error in creating fragment");
				}
			}
		});
		

		return view;
		
		
	}
	
	public void Ingreso(){
		
		if (!verificaConexion(view.getContext())){
			final BasedRegister_User helper1= new BasedRegister_User(view.getContext());
			boolean b=false;
	
			helper1.abrir();
			Cursor c = helper1.getReadableDatabase().rawQuery("SELECT id, username,contrasena FROM usuario", null);
			if (c.moveToFirst()) {
			     //Recorremos el cursor hasta que no haya m�s registros
			     do {
			 		btnIngreso.setEnabled(false);
					txtContrasena.setEnabled(false);
					txtEmail.setEnabled(false);
					progressGenerator.start(btnIngreso);
					btnIngreso.setProgress(10);
					
			    	  String id,usuario,contrasena1;
			    	  id=c.getString(0);
			    	  usuario=c.getString(1);
			    	  contrasena1=c.getString(2);
			    	  if(usuario.equals(txtEmail.getText().toString())){
			    		  if(contrasena1.equals(txtContrasena.getText().toString())){
			    			  helper1.getWritableDatabase().execSQL("UPDATE usuario SET estado='1' WHERE id="+id);
			    			  b=true;
			    			  Intent i = new Intent(view.getContext(),MainActivity.class);
			    			  
			    			  startActivity(i);
			    			  
			    		  }
			    	  }
			    	 
			    	  //Toast.makeText(view.getContext(), nombre1 + cedula1 + contraseña1 , Toast.LENGTH_LONG).show() ;
			     } while(c.moveToNext());
		
			}else {
				new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText("Datos no validos.")
	            .show();
			}
			if (!b){ 
				new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText("Datos no validos.")
	            .show();
			}
		}else{
			final BasedRegister_User helper=new BasedRegister_User(view.getContext());
			boolean bo=false;
			helper.abrir();
			Cursor c = helper.getReadableDatabase().rawQuery("SELECT id, username,contrasena FROM usuario", null);
			if (c.moveToFirst()) {
			     //Recorremos el cursor hasta que no haya m�s registros
			     do {
			    	  String id,user,contrasena1;
			    	  id=c.getString(0);
			    	  user=c.getString(1);
			    	  contrasena1=c.getString(2);
			    	  if(user.equals(txtEmail.getText().toString())){
			    		  if(contrasena1.equals(txtContrasena.getText().toString())){
			    			  helper.getWritableDatabase().execSQL("UPDATE usuario SET estado='1' WHERE id="+id);
			    			  helper.cerrar();
			    			  MainActivity.id=id;
			    			  bo=true;
			    			  Intent i=new Intent(view.getContext(), MainActivity.class);
			    			  startActivity(i);				    			  
			    		  }else{
			    			  //Toast.makeText(getApplicationContext(), "No coinciden", Toast.LENGTH_LONG).show();
			    			  
			    		  }
			    	  }else{
			    		  //Toast.makeText(getApplicationContext(), "Correo no guardado", Toast.LENGTH_LONG).show();
			    	  }
			    	 
			    	  //Toast.makeText(getApplication(), nombre1 + cedula1 + contraseña1 , Toast.LENGTH_LONG).show() ;
			     } while(c.moveToNext());
			     
			     if (!bo){ 
			    	 new Post((Activity) view.getContext()).execute("","","");
				 }
			}else {
				new Post((Activity) view.getContext()).execute("","","");
			}
			helper.cerrar();
		}

	
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
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
	
	private class Post extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;
		
		private Activity activity;
		public Controlador c;
		// private List<Message> messages;
		public Post(Activity activity) {
			this.activity = activity;
			context = activity;
			dialog = new ProgressDialog(context);
		}

		/** progress dialog to show user that the backup is processing. */

		/** application context. */
		private Context context;
		public String id, nombre, apellido, correo;
		protected void onPreExecute() {
			dialog.setMessage("Progress start");
			dialog.show();
			c= new Controlador();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if(success){
	  	        Log.i("log_tag", "TODO BIEN");
	  	        final BasedRegister_User helper1=new BasedRegister_User(view.getContext());
	  	        helper1.insertarReg(id, correo, txtEmail.getText().toString(),
	  	        		txtContrasena.getText().toString(), nombre, apellido, "1");
				helper1.cerrar();
				new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
            	.setTitleText("Excelente!")
            	.setContentText("Se ha registrado correctamente!")
            	.show();
	  			  Intent i = new Intent(view.getContext(),MainActivity.class);
				  
	  			  startActivity(i);
				
				dialog.dismiss();
			}else{
				new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
					.setContentText("Correo ya esta registrado");
				dialog.dismiss();
			}
		}
		
		protected Boolean doInBackground(String... args) {
			/*String nombre = args[0];
			String apellido = args[1];
			String cedula = args[2];
			String correo = args[3];*/
			String cd;
	    	JSONObject JO=c.login(txtEmail.getText().toString(), txtContrasena.getText().toString());
	    	
			try {
				if (JO.getString("username")==null){
					new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText("Error de conexion.")
		            .show();
					return false;
				}else{
					return true;
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText("Error de conexion.")
	            .show();
				e.printStackTrace();
				return false;
			}
		}
	}

	
}