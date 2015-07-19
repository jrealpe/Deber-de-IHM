package com.kokoa.huecapp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.kokoa.huecapp.classes.Restaurant;
import com.kokoa.huecapp.controller.Controlador;
import com.kokoa.huecapp.views.ScaleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kokoa.huecapp.R;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

public class RestaurantActivity extends Activity{
	ScaleImageView mImageView;
	Button botonGaleria, botonFoto;
	TextView direccion, nombre;
	Button botonRegistrar;
	String foto="";
	Dialog dialog;
	public IntentFilter lftIntentFilter;
	public double lastLon = 0, lastLat = 0;
	public ProgressDialog pd;
	ArrayList<Restaurant> listaRes;
   	public Uri mImageCaptureUri;
    public static String path= "";
	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_restaurant);
		mImageView = (ScaleImageView)findViewById(R.id.iv_pic3);
  		botonGaleria = (Button) findViewById(R.id.btnGaleria1);
  		botonFoto = (Button)findViewById(R.id.btnCamara1);
        direccion = (TextView) findViewById(R.id.txtDireccionRes);
        botonRegistrar = (Button)findViewById(R.id.btn_subir1);
        nombre = (TextView) findViewById(R.id.txtRegistrarRestaurante);
        
        botonRegistrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!direccion.getText().toString().equals("") && !nombre.getText().toString().equals("")){
					
					startLocation();
				
				}else{
					
					final SweetAlertDialog sad = new SweetAlertDialog(RestaurantActivity.this, SweetAlertDialog.ERROR_TYPE);
					sad.setTitleText("Oops...").setContentText("Llene todos los datos.");
		            sad.show();
					
				}
			}
		});
        
  		botonFoto.setOnClickListener(new OnClickListener() {			
  			@Override
  			public void onClick(View v) {

  				dispatchTakePictureIntent();
					
  			}
  		});
  		
  		botonGaleria.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
   				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
   				int code = SELECT_PICTURE;
   				
   				startActivityForResult(intent, code);
			}
		});
  
  		
	}
	
	public void startLocation() {
		pd = new ProgressDialog(this);
		pd.setTitle("GPS");
		pd.setMessage("Buscando Ubicacion...\nEspere Por Favor!!!");
		// pd.setCancelable(false);
		pd.show();
		LocationLibrary.startAlarmAndListener(getApplicationContext());
		LocationLibrary.forceLocationUpdate(getApplicationContext());
	}
	
	private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// extract the location info in the broadcast
			final LocationInfo locationInfo = (LocationInfo) intent
					.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
			foundLocation(locationInfo);
		}
	};
	
	private void foundLocation(final LocationInfo locationInfo) {
		if (locationInfo.anyLocationDataReceived()) {
			pd.dismiss();
			lastLat = locationInfo.lastLat;
			lastLon = locationInfo.lastLong;
					
			try {
				
				if(lastLat != 0 && lastLon != 0 ){
					LocationLibrary.stopAlarmAndListener(getApplicationContext());
					unregisterReceiver(lftBroadcastReceiver);
					Enviar();
				}else 
					LocationLibrary.forceLocationUpdate(getApplicationContext());
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSystemService(Context.NOTIFICATION_SERVICE);
		lftIntentFilter = new IntentFilter(
		LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction());
		registerReceiver(lftBroadcastReceiver, lftIntentFilter);
	}
	
	public void Enviar() throws UnsupportedEncodingException{
		//HttpPost hp=new HttpPost("http://kokoa.espol.edu.ec/serviguayas/usuario");	
		//hp.setEntsity(new UrlEncodedFormEntity(nvp));
		new Post(RestaurantActivity.this).execute("","","");
		
		//
	  	
		
	}
	
	// TAKE PICTURE AND SAVE
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent
				.resolveActivity(getApplicationContext().getPackageManager()) != null) {
			
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
			}
			
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, TAKE_PICTURE);
			}
		}
	}
	
	private File createImageFile() throws IOException {
		
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
				.format(new Date());
		String imageFileName = "HuecApp" + "_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		// Save a file: path for use with ACTION_VIEW intents
		foto = "file:" + image.getAbsolutePath();
		
		return image;
	}
	
	 /**
     * Funci�n que se ejecuta cuando concluye el intent en el que se solicita una imagen
     * ya sea de la c�mara o de la galer�a
     */
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	/**
    	 * Se revisa si la imagen viene de la c�mara (TAKE_PICTURE) o de la galer�a (SELECT_PICTURE)
    	 */
		if (requestCode == TAKE_PICTURE) {
			/**
			 * Si se reciben datos en el intent tenemos una vista previa
			 * (thumbnail)
			 */
			try {

				Bitmap bmp = MediaStore.Images.Media.getBitmap(
						getApplicationContext().getContentResolver(),
						Uri.parse(foto));
				ScaleImageView scaleimage = (ScaleImageView)findViewById(R.id.iv_pic3);
				scaleimage.setImageBitmap(bmp);

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			/**
			 * Recibimos el URI de la imagen y construimos un Bitmap a partir de
			 * un stream de Bytes
			 */
		} else if (requestCode == SELECT_PICTURE){
    		Uri selectedImage = data.getData();
    		InputStream is;
    		try {
    			is = getContentResolver().openInputStream(selectedImage);
    	    	BufferedInputStream bis = new BufferedInputStream(is);
    	    	Bitmap bitmap = BitmapFactory.decodeStream(bis);            
    	    	ImageView iv = (ImageView)findViewById(R.id.iv_pic3);
    	    	iv.setImageBitmap(bitmap);	
    	    	foto= "file:" + getPath(selectedImage);
    		} catch (FileNotFoundException e) {}
    	}
    }
  	
    public String getPath(Uri uri) { // just some safety built in 
    	if( uri == null ) { 
    		// TODO perform some logging or show user feedback 
    		return null; 
    	} // try to retrieve the image from the media store first 
    	// this will only work for images selected from gallery 
    	String[] projection = { MediaStore.Images.Media.DATA };
    	Cursor cursor = managedQuery(uri, projection, null, null, null);
    	if( cursor != null ){ 
    		int column_index = cursor .getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
    		cursor.moveToFirst(); 
    		return cursor.getString(column_index);
    		} // this is our fallback here 
    	return uri.getPath();
    	
    }
    
	private class Post extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;
		private Context context;
		public String id,imagen;
		private Activity activity;
		public Controlador c;
		
		public Post(Activity activity) {
			this.activity = activity;
			context = activity;
			dialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Progress start");
			dialog.show();
			c= new Controlador();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if(success){
				/*final BasedRegister_Restaurant helper1=new BasedRegister_Restaurant(getApplicationContext());
	  	        helper1.insertarRest_Plat(Integer.parseInt(id), Integer.parseInt("1"), nombre.getText().toString()
	  	        		, foto, Float.parseFloat(precio.getText().toString())
						);
				helper1.cerrar();*/
				//Toast.makeText(getApplicationContext(), "Correcto", Toast.LENGTH_LONG).show();
		
				final SweetAlertDialog sad = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
				
				sad.setTitleText("Excelente").setContentText("Se ha registrado correctamente!");
				sad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sad.dismiss();
						Intent i = new Intent(RestaurantActivity.this,MainActivity.class);
						startActivity(i);
						finish();
						
					}
				});
	            sad.show();
				
				
			}else{
				
				new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
				.setTitleText("Oops...").setContentText("Ingrese foto")
	            .show();
				
			}
			dialog.dismiss();
		}
		
		@Override
		protected Boolean doInBackground(String... args) {

			String cd;
	    	JSONObject JO=null;
			try {
				JO = c.IngresoRestaurante(nombre.getText().toString(), direccion.getText().toString()
						, String.valueOf(lastLat), String.valueOf(lastLon), foto);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
			try {
				if (JO.getString("id")==null){
					return false;
				}else{
					id = JO.getString("id");
					//imagen = JO.getString("image_dish");
					return true;
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				return false;
			}catch (Exception e) {
				return false;
			}
		}
	}

}