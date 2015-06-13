package com.kokoa.huecapp.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.coreform.open.android.formidablevalidation.RegExpressionValueValidator;
import com.coreform.open.android.formidablevalidation.ValidationManager;
import com.dd.nuevo.ProgressGenerator;
import com.dd.nuevo.ProgressGenerator.OnCompleteListener;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class RegistroFragment extends Fragment implements ProgressGenerator.OnCompleteListener{
	TextView lblIngresar;
	TextView lblNombre;
	TextView lblApellido;
	TextView lblCorreo;
	TextView lblUsername;
	TextView lblContrasena;
	TextView lblReContrasena;
	ActionProcessButton btnSignIn;
	ProgressGenerator progressGenerator;
	View view;
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		view = inflater.inflate(R.layout.fragment_register, container, false);
		lblIngresar = (TextView)view.findViewById(R.id.lblIngresa);
		lblNombre = ((FloatingLabelView) view.findViewById(R.id.lblNombre)).getEditText();
		lblApellido = ((FloatingLabelView) view.findViewById(R.id.lblApellido)).getEditText();
		lblUsername = ((FloatingLabelView) view.findViewById(R.id.lblUsername)).getEditText();
		lblCorreo = ((FloatingLabelView) view.findViewById(R.id.lblCorreo)).getEditText();
		lblContrasena = ((FloatingLabelView) view.findViewById(R.id.lblContrasena)).getEditText();
		lblReContrasena = ((FloatingLabelView) view.findViewById(R.id.lblRepContrasena)).getEditText();
		
		ValidationManager v=new ValidationManager(view.getContext());
		v.add("emailAddress", new RegExpressionValueValidator((EditText) lblCorreo , "^([0-9a-zA-Z]([-\\.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$", "Email address must be valid."));
		
		v.validateAllAndSetError();
		
		progressGenerator = new ProgressGenerator(this);
		btnSignIn = (ActionProcessButton) view.findViewById(R.id.btnSignIn);
		btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
		btnSignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Ingreso();				
			}
		});
		
		lblIngresar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IngresoActivity.fragment=new LoginFragment();
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

		if (Validacion(lblNombre.getText().toString() , lblApellido.getText().toString() , lblContrasena.getText().toString() 
				,lblCorreo.getText().toString() , lblReContrasena.getText().toString())){
			if(!verificaConexion(view.getContext())){						
				new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
				.setContentText("No tiene Conexión a Internet...")
	            .show();
			}else{
				if (lblContrasena.getText().toString().equals(lblReContrasena.getText().toString())){
					if (validateEmail(lblCorreo.getText().toString())){
						
						new Post((Activity) view.getContext()).execute(lblNombre.getText().toString() , lblApellido.getText().toString() , lblContrasena.getText().toString() 
								,lblCorreo.getText().toString(), lblUsername.getText().toString() , lblReContrasena.getText().toString());
						/*btnSignIn.setEnabled(false);
						lblApellido.setEnabled(false);
						lblContrasena.setEnabled(false);
						lblCorreo.setEnabled(false);
						lblReContrasena.setEnabled(false);
						progressGenerator.start(btnSignIn);
						btnSignIn.setProgress(10);
						BaseRegistro base= new BaseRegistro(view.getContext());
						base.insertarReg((Integer.toString(base.contar()+1)), lblCorreo.getText().toString(), lblContrasena.getText().toString(), lblNombre.getText().toString(), lblApellido.getText().toString(), "0");
						base.cerrar();
						new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
		                	.setTitleText("Excelente!")
		                	.setContentText("Se ha registrado correctamente!")
		                	.show();
						IngresoActivity.fragment=new LoginFragment();
						if (IngresoActivity.fragment != null) {
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, IngresoActivity.fragment).commit();
							
						} else {
							
							Log.e("MainActivity", "Error in creating fragment");
						}
						
					*/
					}else{
						new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
						.setContentText("Correo ingresado no es valido.")
		                .show();
					}
				}else{
					new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
					.setContentText("Contraseñas no coinciden.")
	                .show();				
				}
			}
		}else{
			new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
			.setContentText("Llene todos los datos.")
            .show();
		}
	}

	public boolean Validacion(String nombre,String apellido,String contrasena,String correo, String repContrasena){
		if (nombre.equals("") || apellido.equals("") || contrasena.equals("") || correo.equals("") || repContrasena.equals("")){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static boolean validateEmail(String email) {
		try{
		    // Compiles the given regular expression into a pattern.
		    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		    // Match the given input against this pattern
		    Matcher matcher = pattern.matcher(email);
		    return matcher.matches();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
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
				
	  	      	helper1.abrir();
	  	        helper1.insertarReg((Integer.toString(helper1.contar()+1)), lblCorreo.getText().toString(), lblUsername.getText().toString(),
	  	        		lblContrasena.getText().toString(), lblNombre.getText().toString(), lblApellido.getText().toString(), "0");
				helper1.cerrar();
				
				new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
            	.setTitleText("Excelente!")
            	.setContentText("Se ha registrado correctamente!")
            	.show();
				IngresoActivity.fragment=new LoginFragment();
				if (IngresoActivity.fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.frame_container, IngresoActivity.fragment).commit();
					
				} else {
					
					Log.e("MainActivity", "Error in creating fragment");
				}
				
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
	    	JSONObject JO=c.registrar(lblNombre.getText().toString(), lblApellido.getText().toString(), 
	    					lblCorreo.getText().toString(), lblUsername.getText().toString()
	    					, lblContrasena.getText().toString());
	    	
			try {
				if (JO.getString("user")==null){
					return false;
				}else{
					return true;
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
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

}