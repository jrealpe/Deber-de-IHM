package com.kokoa.huecapp;

import java.util.ArrayList;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.drive.internal.GetMetadataRequest;
import com.kokoa.huecapp.adapters.TabsPagerAdapter;
import com.kokoa.huecapp.bd.BasedRegister_HuecApp;
import com.kokoa.huecapp.classes.Category;
import com.kokoa.huecapp.classes.Restaurant;
import com.kokoa.huecapp.classes.Restaurant_Dish;
import com.kokoa.huecapp.fragments.MapFragment;
import com.kokoa.huecapp.fragments.RegistroFragment;
import com.kokoa.huecapp.function.NetworkFunction;
import com.kokoa.huecapp.json.JSONParser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	public static String id;
	public ProgressDialog pd;
	
	// Tab titles
	private String[] tabs = { "Mapa", "Top", "Filter" };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnuranking:
			final ProgressDialog pd = new ProgressDialog(MainActivity.this);
			pd.setMessage("Cargando...");
			pd.show();
			
			initializeRanking(pd);
			return true;
		case R.id.mnuregplato:
			initializeRegisterDish();
			return true;

		case R.id.mnuregres:
			initializeRegisterRestaurant();
			return true;
		case R.id.mnuultimas:
			final ProgressDialog pd2 = new ProgressDialog(MainActivity.this);
			pd2.setMessage("Cargando...");
			pd2.show();
			initializeHuecas(pd2);
			return true;

		case R.id.mnucerrar:
			final BasedRegister_HuecApp b = new BasedRegister_HuecApp(
					getApplicationContext());
			b.getWritableDatabase().execSQL(
					"UPDATE usuario SET estado='0' WHERE id=" + id);
			b.close();
			Intent i = new Intent(MainActivity.this, IngresoActivity.class);
			startActivity(i);
			finish();

			// showHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		

		pd = new ProgressDialog(MainActivity.this);
		pd.setMessage("Inicializando...");
		pd.show();
		
		final BasedRegister_HuecApp b = new BasedRegister_HuecApp(
				getApplicationContext());

		if (!b.getUpdate()) {

			if (NetworkFunction.verificaConexion(getBaseContext())) {
				
				// Update Information HuecApp				
				new GetRestaurants(MainActivity.this).execute("", "", "");

			} else {
				Initialize();
				Toast.makeText(getApplicationContext(),
						"Porfavor, Ingrese nuevamente con Conexión a Internet",
						Toast.LENGTH_LONG).show();

			}

		} else {

			Initialize();

		}
		
		b.close();

	}
	

	public void Initialize() {

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		pd.dismiss();

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public void initializeRegisterDish() {

		Intent i = new Intent(MainActivity.this, DishActivity.class);
		startActivity(i);

	}

	public void initializeRegisterRestaurant() {

		Intent i = new Intent(MainActivity.this, RestaurantActivity.class);
		startActivity(i);

	}
	
	public void initializeRanking(final ProgressDialog pd) {

		
		
		final WebView webview = new WebView(MainActivity.this);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				pd.dismiss();
				
			}
		});
		
		webview.loadUrl("http://104.131.50.216:8888/rank/");
		
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle("Ranking");
		alert.setView(webview);

		AlertDialog ad =  alert.create();
		ad.show();

		
	}
	
	public void initializeHuecas(final ProgressDialog pd) {

		final WebView webview = new WebView(MainActivity.this);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				pd.dismiss();

			}
		});

		webview.loadUrl("http://104.131.50.216:8888/lasthuecas/");

		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle("Las Ultimas Huecas");
		alert.setView(webview);

		AlertDialog ad = alert.create();
		ad.show();

	}

	public class GetRestaurants extends AsyncTask<String, Void, String> {

		private final static String URL_GETRESTAURANTS = "http://104.131.50.216:8888/restaurants/";
		private ArrayList<Restaurant> restaurants_list = new ArrayList<Restaurant>();
		//private ProgressDialog dialog;
		private Context context;
		private Activity activity;
		private BasedRegister_HuecApp helper;

		public GetRestaurants(Activity activity) {
			this.activity = activity;
			context = activity;
			//dialog = new ProgressDialog(context);
			helper = new BasedRegister_HuecApp(context);
		}

		@Override
		protected void onPreExecute() {
			/*
			dialog.setTitle("Espere Por Favor");
			dialog.setMessage("Actualizando Restaurantes...");
			dialog.setCancelable(false);
			dialog.show();
			*/

		}

		@Override
		protected void onPostExecute(String result) {

			//dialog.dismiss();
			if (result.equals("True")) {

				// SI la Base Datos esta llena, eliminamos para actualizarla
				if (!helper.restaurant_isEmpty()) {

					activity.deleteDatabase("restaurante");
					helper = new BasedRegister_HuecApp(context);

				}

				// Abrimos conexión de escritura con la BD restaurante
				helper.abrir();

				for (int j = 0; j < restaurants_list.size(); j++) {
					Restaurant rest = restaurants_list.get(j);
					helper.insertarRest(rest.getId(), rest.getName(),
							rest.getPlace(), "", rest.getLatitude(),
							rest.getLongitude(), rest.getImage_restaurant());

				}
				
				Initialize();

				// Cerramos conexión de escritura con la BD resturante
				helper.close();
				Toast.makeText(activity, "Correcto, Información Actualizada",
						Toast.LENGTH_SHORT).show();

			} else if (result.equals("Empty")) {
				Toast.makeText(activity,
						"Advertencia, No se encontraron registros!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity,
						"Error, No se pudo establecer conexión!",
						Toast.LENGTH_SHORT).show();
			}

			pd.dismiss();
			new GetDishes(MainActivity.this).execute();

		}

		@Override
		protected String doInBackground(String... args) {
			String result = "False";
			JSONParser json_parcer = new JSONParser();
			JSONArray restaurants_array = null;
			JSONObject restaurant_object = null;
			int len = 0;

			try {

				// Obtenemos el array de restaurantes
				restaurant_object = json_parcer
						.getJSONFromUrl(URL_GETRESTAURANTS);
				restaurants_array = restaurant_object.getJSONArray("results");

				Log.i("huecapp-length",
						String.valueOf(restaurants_array.length()));
				 
				len = restaurants_array.length(); 
				if ( len != 0) {
					
					for (int i = 0; i < len; i++) {

						int j;
						
						j = i;
					
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
								"Plato Gastronomico de Machala", latitude, longitude,
								image_restaurant);

						// Añadimos a la lista de restaurantes
						restaurants_list.add(rest);
					}

					result = "True";

				} else
					result = "Empty";

			} catch (ParseException e) {
				Log.e("ParseException", e.toString());

			} catch (JSONException e) {
				e.printStackTrace();
				Log.i("huecapp", restaurants_array.toString());
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}

			return result;

		}
	}

	public class GetDishes extends AsyncTask<String, Void, String> {

		private final static String URL_GETDISHES = "http://104.131.50.216:8888/dishes/";
		private ArrayList<Restaurant_Dish> dishes_list = new ArrayList<Restaurant_Dish>();
		//private ProgressDialog dialog;
		private Context context;
		private Activity activity;
		private BasedRegister_HuecApp helper;

		public GetDishes(Activity activity) {
			this.activity = activity;
			context = activity;
			//dialog = new ProgressDialog(context);
			helper = new BasedRegister_HuecApp(context);
		}

		@Override
		protected void onPreExecute() {
			/*
			dialog.setTitle("Espere Por Favor");
			dialog.setMessage("Actualizando Platos...");
			dialog.setCancelable(false);
			dialog.show();
			*/

		}

		@Override
		protected void onPostExecute(String result) {

			//dialog.dismiss();
			if (result.equals("True")) {

				// SI la Base Datos esta llena, eliminamos para actualizarla
				if (!helper.restaurant_isEmpty()) {

					activity.deleteDatabase("restaurante_plato");
					helper = new BasedRegister_HuecApp(context);

				}

				// Abrimos conexión de escritura con la BD restaurante
				helper.abrir();

				for (int j = 0; j < dishes_list.size(); j++) {
					Restaurant_Dish rest_dish = dishes_list.get(j);
					helper.insertarRest_Plat(rest_dish.getId(),
							rest_dish.getName(), rest_dish.getCosto(),
							rest_dish.getId_res(), rest_dish.getFoto());

				}

				// Cerramos conexión de escritura con la BD resturante
				helper.close();
				Toast.makeText(activity, "Correcto, Información Actualizada",
						Toast.LENGTH_SHORT).show();

			} else if (result.equals("Empty")) {
				Toast.makeText(activity,
						"Advertencia, No se encontraron registros!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity,
						"Error, No se pudo establecer conexión!",
						Toast.LENGTH_SHORT).show();
			}

			new GetCategories(MainActivity.this).execute();

		}

		@Override
		protected String doInBackground(String... args) {
			String result = "False";
			JSONParser json_parcer = new JSONParser();
			JSONArray restaurantsDish_array = null;
			JSONObject restaurantDish_object = null;

			try {

				// Obtenemos el array de restaurantes
				restaurantDish_object = json_parcer
						.getJSONFromUrl(URL_GETDISHES);
				restaurantsDish_array = restaurantDish_object
						.getJSONArray("results");

				Log.i("huecapp-length",
						String.valueOf(restaurantsDish_array.length()));
				if (restaurantsDish_array.length() != 0) {
					for (int i = 0; i < restaurantsDish_array.length(); i++) {

						// Obtenemos los restaurantes del array
						restaurantDish_object = restaurantsDish_array
								.getJSONObject(i);

						int id = Integer.valueOf(restaurantDish_object
								.getString("id"));
						String name = restaurantDish_object.getString("name");
						String price = restaurantDish_object.getString("price");
						String image_dish = restaurantDish_object
								.getString("image_dish");
						int id_rest = restaurantDish_object
								.getInt("id_restaurant");

						// Creamos un objeto de tipo Restaurante
						Restaurant_Dish rest_dish = new Restaurant_Dish(id,
								id_rest, price, name, image_dish);

						// Añadimos a la lista de restaurantes
						dishes_list.add(rest_dish);
					}

					result = "True";

				} else
					result = "Empty";

			} catch (ParseException e) {
				Log.e("ParseException", e.toString());

			} catch (JSONException e) {
				e.printStackTrace();
				Log.i("huecapp", dishes_list.toString());
			} catch (Exception e) {
			}

			return result;

		}

	}

	public class GetCategories extends AsyncTask<String, Void, String> {

		private final static String URL_GETCATEGORIES = "http://104.131.50.216:8888/categories/";
		private ArrayList<Category> categories_list = new ArrayList<Category>();
		//private ProgressDialog dialog;
		private Context context;
		private Activity activity;
		private BasedRegister_HuecApp helper;

		public GetCategories(Activity activity) {
			this.activity = activity;
			context = activity;
			//dialog = new ProgressDialog(context);
			helper = new BasedRegister_HuecApp(context);
		}

		@Override
		protected void onPreExecute() {
			/*
			dialog.setTitle("Espere Por Favor");
			dialog.setMessage("Actualizando Categorias...");
			dialog.setCancelable(false);
			dialog.show();
			*/

		}

		@Override
		protected void onPostExecute(String result) {
			
			//dialog.dismiss();
			if (result.equals("True")) {

				// SI la Base Datos esta llena, eliminamos para actualizarla
				if (!helper.restaurant_isEmpty()) {

					activity.deleteDatabase("restaurante_plato");
					helper = new BasedRegister_HuecApp(context);

				}

				// Abrimos conexión de escritura con la BD restaurante
				helper.abrir();

				for (int j = 0; j < categories_list.size(); j++) {
					Category category = categories_list.get(j);
					helper.insertarCategory(category.getId(),
							category.getName());

				}

				helper.insertarUpdate("1");
				
				// Cerramos conexión de escritura con la BD resturante
				helper.close();
				Toast.makeText(activity, "Correcto, Información Actualizada",
						Toast.LENGTH_SHORT).show();

			} else if (result.equals("Empty")) {
				Toast.makeText(activity,
						"Advertencia, No se encontraron registros!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity,
						"Error, No se pudo establecer conexión!",
						Toast.LENGTH_SHORT).show();
			}

			//Initialize();

		}

		@Override
		protected String doInBackground(String... args) {
			String result = "False";
			JSONParser json_parcer = new JSONParser();
			JSONArray category_array = null;
			JSONObject category_object = null;

			try {

				// Obtenemos el array de restaurantes
				category_object = json_parcer.getJSONFromUrl(URL_GETCATEGORIES);
				category_array = category_object.getJSONArray("results");

				Log.i("huecapp-length", String.valueOf(category_array.length()));

				if (category_array.length() != 0) {
					for (int i = 0; i < category_array.length(); i++) {

						// Obtenemos los restaurantes del array
						category_object = category_array.getJSONObject(i);

						int id = Integer.valueOf(category_object
								.getString("id"));
						String name = category_object.getString("name");

						// Creamos un objeto de tipo Restaurante
						Category category = new Category(id, name);

						// Añadimos a la lista de restaurantes
						categories_list.add(category);
					}

					result = "True";

				} else
					result = "Empty";

			} catch (ParseException e) {
				Log.e("ParseException", e.toString());

			} catch (JSONException e) {
				e.printStackTrace();
				Log.i("huecapp", categories_list.toString());
			} catch (Exception e) {
			}

			return result;

		}

	}

}