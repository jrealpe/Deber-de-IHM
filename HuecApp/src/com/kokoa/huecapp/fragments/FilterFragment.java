package com.kokoa.huecapp.fragments;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.kokoa.huecapp.DishActivity;
import com.kokoa.huecapp.MainActivity;
import com.kokoa.huecapp.R;
import com.kokoa.huecapp.bd.BasedRegister_HuecApp;
import com.kokoa.huecapp.classes.Category;
import com.kokoa.huecapp.classes.Restaurant;
import com.kokoa.huecapp.classes.Restaurant_Dish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

public class FilterFragment extends Fragment {

	private AutoCompleteTextView autoComplete_plato, autoComplete_restaurante;
	private Button buscar;
	private ArrayAdapter<String> adapter_dish;
	private ArrayAdapter<String> adapter_restaurant;
	private ArrayList<Restaurant_Dish> dishes_list = null;
	private ArrayList<Restaurant> restaurants_list = null;
	private ArrayList<String> dishes_names = null;
	private ArrayList<String> restaurants_names = null;
	private BasedRegister_HuecApp helper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_filter, container,
				false);

		autoComplete_plato = (AutoCompleteTextView) rootView
				.findViewById(R.id.autoComplete_plato);
		autoComplete_restaurante = (AutoCompleteTextView) rootView
				.findViewById(R.id.autoComplete_plato);
		
		buscar = (Button) rootView.findViewById(R.id.btn_buscar);

		// Initalize variables
		dishes_names = new ArrayList<String>();
		restaurants_names = new ArrayList<String>();
		helper = new BasedRegister_HuecApp(getActivity());

		// Get list of BD
		dishes_list = helper.getDishes();
		restaurants_list = helper.getRestaurants();

		for (Restaurant_Dish dish : dishes_list) {
			dishes_names.add(dish.getName());
		}

		for (Restaurant restaurants : restaurants_list) {
			restaurants_names.add(restaurants.getName());
		}

		// Set Adapter
		adapter_dish = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, dishes_names);
		
		adapter_restaurant = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,restaurants_names);
		
		autoComplete_plato.setAdapter(adapter_dish);
		
		autoComplete_restaurante.setAdapter(adapter_restaurant);
		
		
		// specify the minimum type of characters before drop-down list is shown
		autoComplete_plato.setThreshold(1);
		autoComplete_restaurante.setThreshold(1);
		
		buscar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				ProgressDialog pd =new ProgressDialog(getActivity());
				pd.setMessage("Buscando...");
				pd.show();
				
				String name = autoComplete_plato.getText().toString();
				String restaurant = autoComplete_plato.getText().toString();
				
				if(name.equals("") && restaurant.equals("")){
					
					new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
					.setTitleText("Oops...").setContentText("Ingrese un campo por los menos!")
		            .show();
					
				}else{
					
					if(!name.equals("")){
						
						initializeFilterName(name);
						
					}else if(!restaurant.equals("")){
						final BasedRegister_HuecApp helper = new BasedRegister_HuecApp(getActivity());
						
						Restaurant r = helper.getRestaurant(restaurant);
						initializeFilterRestaurant(String.valueOf(r.getId()));
					}else{
						
						final BasedRegister_HuecApp helper = new BasedRegister_HuecApp(getActivity());
						
						Restaurant r = helper.getRestaurant(restaurant);
						initializeFilterAll(name,String.valueOf(r.getId()));
					}
					
				}
				
				pd.dismiss();
				
			}
		});

		return rootView;
	}

	public void initializeFilterName(String name) {

		final WebView webview = new WebView(getActivity());

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {

			}
		});

		webview.loadUrl("http://104.131.50.216:8888/filter/?name=" + name);

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("HuecApp");
		alert.setView(webview);

		AlertDialog ad = alert.create();
		ad.show();

	}
	public void initializeFilterRestaurant(String restaurant) {

		final WebView webview = new WebView(getActivity());

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {

			}
		});

		webview.loadUrl("http://104.131.50.216:8888/filter/?restaurant=" + restaurant);

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("HuecApp");
		alert.setView(webview);

		AlertDialog ad = alert.create();
		ad.show();

	}
	
	public void initializeFilterAll(String name,String restaurant) {

		final WebView webview = new WebView(getActivity());

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {

			}
		});

		webview.loadUrl("http://104.131.50.216:8888/filter/?name=" + name + "&restaurant=" + restaurant);

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("HuecApp");
		alert.setView(webview);

		AlertDialog ad = alert.create();
		ad.show();

	}

}