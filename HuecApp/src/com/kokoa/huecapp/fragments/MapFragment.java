package com.kokoa.huecapp.fragments;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.google.android.gms.internal.r;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kokoa.huecapp.DishActivity;
import com.kokoa.huecapp.MainActivity;
import com.kokoa.huecapp.R;
import com.kokoa.huecapp.bd.BasedRegister_HuecApp;
import com.kokoa.huecapp.classes.Restaurant;
import com.kokoa.huecapp.json.JSONParser;
import com.kokoa.huecapp.views.ViewUtils;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

/**
 * 
 * Simple custom SupportMapFragment that adds a margin.
 * 
 * @author ddewaele
 * 
 */
public class MapFragment extends SupportMapFragment {

	public ArrayList<MarkerOptions> markers_list = new ArrayList<MarkerOptions>();
	public ArrayList<Restaurant> restaurants_list = null;

	public IntentFilter lftIntentFilter;
	public double lastLon = 0, lastLat = 0;
	public ProgressDialog pd;
	public Polyline path;
	public Document doc = null;
	public GoogleMap map = null;
	public String id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = super.onCreateView(inflater, container, savedInstanceState);
		// ViewUtils.initializeMargin(getActivity(), view);
		initMap();
		// startLocation();
		return view;
	}

	/*
	@Override
	public void onResume() {
		super.onResume();

		((NotificationManager) getActivity().getSystemService(
				Context.NOTIFICATION_SERVICE)).cancel(1234);

		lftIntentFilter = new IntentFilter(
				LocationLibraryConstants
						.getLocationChangedPeriodicBroadcastAction());

		getActivity().registerReceiver(lftBroadcastReceiver, lftIntentFilter);

	}
	*/

	public void animateCamera() {
		if (map.getMyLocation() != null)
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(map
					.getMyLocation().getLatitude(), map.getMyLocation()
					.getLongitude()), 15));
	}

	public void initMap() {

		UiSettings settings = getMap().getUiSettings();
		// settings.setAllGesturesEnabled(true);
		settings.setMyLocationButtonEnabled(true);

		map = getMap();
		map.setMyLocationEnabled(true);

		// animateCamera();
		
		LatLng lat_lon = new LatLng(-3.258103, -79.955369);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_lon, 10));
		
		// show restaurants in map
		addMarkerRestaurant();
	}

	public void addMarkerRestaurant() {

		BasedRegister_HuecApp br_rest = new BasedRegister_HuecApp(
				getActivity());

		restaurants_list = br_rest.getRestaurants();

		if (!markers_list.isEmpty()) {

			for (int j = 0; j < markers_list.size(); j++) {

				markers_list.get(j).visible(false);
			}

			markers_list.clear();
		}

		if (map != null) {

			if (!restaurants_list.isEmpty()) {

				for (int i = 0; i < restaurants_list.size(); i++) {

					// Obtenemos cada restaurante
					final Restaurant r = restaurants_list.get(i);

					// Creacion del marker
					MarkerOptions mo = new MarkerOptions();
					LatLng lat_lon = new LatLng(r.getLatitude(),
							r.getLongitude());
					mo.position(lat_lon);
					mo.title(r.getName());

					map.setOnMarkerClickListener(new OnMarkerClickListener() {

						@Override
						public boolean onMarkerClick(Marker marker) {

							ProgressDialog pDialog;
							pDialog = new ProgressDialog(getActivity());
							pDialog.setTitle("Espere Por Favor");
							pDialog.setMessage("Cargando Restaurante...");
							pDialog.setCancelable(false);
							pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							pDialog.show();
							
							final Location location = map.getMyLocation();
							
							BasedRegister_HuecApp br_rest = new BasedRegister_HuecApp(
									getActivity());

							final Restaurant r = br_rest.getRestaurant(marker.getTitle());
							
							
							String title = title(r.getName());
							String content = "Descripcion: " + r.getDescription()
									+ "\nDirección: " + r.getPlace();
							
							//new getImage(marker,location).execute();
							
							final SweetAlertDialog sad = new SweetAlertDialog(getActivity(),
									SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText(title)
									.setContentText(content).setCancelText("Como llego?")
									.setConfirmText("Ver Platos")
									.setCustomImage(R.drawable.images)
									.showCancelButton(true);

							sad.setCanceledOnTouchOutside(true);

							sad.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {

									new getPath(location).execute(String.valueOf(r.getLatitude()),String.valueOf(r.getLongitude()));
									sad.dismiss();
								}
							});
							sad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {

									
									initializeDish(r);
									sad.dismiss();
								}
							});
							
							pDialog.dismiss();
							sad.show();

							return true;
						}

					});

					// Guardamos el marker en el array
					markers_list.add(mo);

					// Añadimos el marker al mapa
					map.addMarker(mo);

					// Añadimos la imagen del marker
					// MarkerOptions().position(lat_lon).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

				}
			}

		} else
			Toast.makeText(getActivity(),
					"Error, Por Favor cierre y abra de nuevo la aplicacion",
					Toast.LENGTH_SHORT).show();

	}

	/*
	 * private void GetCurrentLocation() {
	 * 
	 * double[] d = getlocation(); Share.lat = d[0]; Share.lng = d[1];
	 * 
	 * map .addMarker(new MarkerOptions() .position(new LatLng(Share.lat,
	 * Share.lng)) .title("Current Location") .icon(BitmapDescriptorFactory
	 * .fromResource(R.drawable.dot_blue)));
	 * 
	 * googleMap .animateCamera(CameraUpdateFactory.newLatLngZoom( new
	 * LatLng(Share.lat, Share.lng), 5)); }
	 */
	public String title(String text) {
		String result = "";
		String[] tmp = text.split(" ");

		for (String s : tmp) {

			char[] charArray = s.toCharArray();
			charArray[0] = Character.toUpperCase(charArray[0]);
			result = result + new String(charArray) + " ";

		}

		return result.trim();
	}

	/*
	public void startLocation() {

		pd = new ProgressDialog(getActivity());
		pd.setTitle("GPS");
		pd.setMessage("Buscando Ubicacion...\nEspere Por Favor!!!");
		// pd.setCancelable(false);
		pd.show();

		LocationLibrary.startAlarmAndListener(getActivity()
				.getApplicationContext());

		LocationLibrary.forceLocationUpdate(getActivity()
				.getApplicationContext());

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

			Toast.makeText(
					getActivity(),
					"lat: " + String.valueOf(locationInfo.lastLat) + "\nlon: "
							+ String.valueOf(locationInfo.lastLong),
					Toast.LENGTH_SHORT).show();

			LocationLibrary.stopAlarmAndListener(getActivity()
					.getApplicationContext());

			getActivity().unregisterReceiver(lftBroadcastReceiver);

		}
	}*/
	
	public void initializeDish(final Restaurant rest){
		
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage("Cargando...");
		pd.show();
		
		final WebView webview = new WebView(getActivity());

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				pd.dismiss();
				
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle(title(rest.getName()));
				alert.setView(webview);

				AlertDialog ad =  alert.create();
				ad.show();
		        
				
			}
		});
		
		webview.loadUrl("http://104.131.50.216:8888/filter/?restaurant=" + rest.getId());
		
		
		
		
	}

	private class getImage extends AsyncTask<Void, Void, Bitmap> {

		ProgressDialog pDialog;
		Restaurant restaurant = null;
		Location location = null;
		
		public getImage(Marker marker, Location location) {

			BasedRegister_HuecApp br_rest = new BasedRegister_HuecApp(
					getActivity());

			Restaurant r = br_rest.getRestaurant(marker.getTitle());

			this.restaurant = r;
			this.location = location;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setTitle("Espere Por Favor");
			pDialog.setMessage("Cargando Restaurante...");
			pDialog.setCancelable(false);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();

		}

		@Override
		protected Bitmap doInBackground(Void... params) {

			Bitmap imagen = null;
			if (!restaurant.getImage_restaurant().isEmpty()) {
				String url = "http://192.168.10.101:8001/media/"
						+ restaurant.getImage_restaurant().replace(" ", "%20");
				// String url =
				// "http://192.168.10.101:8001/media/restaurants/El%20Patacon/dora.jpg";
				imagen = descargarImagen(url);
			}
			return imagen;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Bitmap result) {

			String title = "";
			String content = "";

			title = title(restaurant.getName());
			content = "Descripcion: " + restaurant.getDescription()
					+ "\nDirección: " + restaurant.getPlace();

					
			final SweetAlertDialog sad = new SweetAlertDialog(getActivity(),
					SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText(title)
					.setContentText(content).setCancelText("Como llego?")
					.setConfirmText("Ver Platos")
					.setCustomImage(R.drawable.list_item)
					.showCancelButton(true);

			sad.setCanceledOnTouchOutside(true);

			sad.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sDialog) {

					new getPath(location).execute(String.valueOf(restaurant.getLatitude()),String.valueOf(restaurant.getLongitude()));
					sad.dismiss();
				}
			});
			sad.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sDialog) {

					
					initializeDish(restaurant);
					sad.dismiss();
				}
			});

			if (result != null) {
				sad.setCustomImage(new BitmapDrawable(getResources(), result));
			}

			pDialog.dismiss();
			sad.show();

		}

		private Bitmap descargarImagen(String imageHttpAddress) {
			java.net.URL imageUrl = null;
			Bitmap imagen = null;
			try {
				imageUrl = new java.net.URL(imageHttpAddress);
				HttpURLConnection conn = (HttpURLConnection) imageUrl
						.openConnection();
				conn.connect();
				imagen = BitmapFactory.decodeStream(conn.getInputStream());
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			return imagen;
		}

	}

	private class getPath extends AsyncTask<String, Void, Void> {
		private ProgressDialog pDialog;
		private Location location;

		public getPath(Location location){
			
			this.location = location;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setTitle("Espere Por Favor");
			pDialog.setMessage("Buscando ruta...");
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {

			fetchData(params[0], params[1]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (doc != null) {
				NodeList _nodelist = doc.getElementsByTagName("status");
				Node node1 = _nodelist.item(0);
				String _status1 = node1.getChildNodes().item(0).getNodeValue();
				if (_status1.equalsIgnoreCase("OK")) {
					NodeList _nodelist_path = doc
							.getElementsByTagName("overview_polyline");
					Node node_path = _nodelist_path.item(0);
					Element _status_path = (Element) node_path;
					NodeList _nodelist_destination_path = _status_path
							.getElementsByTagName("points");
					Node _nodelist_dest = _nodelist_destination_path.item(0);
					String _path = _nodelist_dest.getChildNodes().item(0)
							.getNodeValue();
					List<LatLng> directionPoint = decodePoly(_path);

					PolylineOptions rectLine = new PolylineOptions().width(10)
							.color(Color.RED);
					for (int i = 0; i < directionPoint.size(); i++) {
						rectLine.add(directionPoint.get(i));
					}
					
					if(path != null ){
						if (path.isVisible())
							path.remove();
					}
					
					// Adding route on the map
					path = map.addPolyline(rectLine);
					
					// Set camera position
					//map.animateCamera(CameraUpdateFactory.newLatLngZoom(rectLine.getPoints().get((int)(rectLine.getPoints().size()/2)), 25));
					
					
				} else {
					Toast.makeText(getActivity(),"Unable to find the route", Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(getActivity(),"Unable to find the route", Toast.LENGTH_LONG).show();
			}

			pDialog.dismiss();

		}

		public void fetchData(String dest_lat, String dest_long) {

			if ( location != null){

				StringBuilder urlString = new StringBuilder();
				urlString
						.append("http://maps.google.com/maps/api/directions/xml?origin=");
				urlString.append(location.getLatitude());
				urlString.append(",");
				urlString.append(location.getLongitude());
				urlString.append("&destination=");// to
				urlString.append(dest_lat);
				urlString.append(",");
				urlString.append(dest_long);
				urlString.append("&sensor=true&mode=driving");

				Log.d("huecapp", "::" + urlString.toString());

				HttpURLConnection urlConnection = null;
				URL url = null;
				try {
					url = new URL(urlString.toString());
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoOutput(true);
					urlConnection.setDoInput(true);
					urlConnection.connect();
					DocumentBuilderFactory dbf = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					doc = (Document) db.parse(urlConnection.getInputStream());// Util.XMLfromString(response);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		
		public ArrayList<LatLng> decodePoly(String encoded) {
	         ArrayList<LatLng> poly = new ArrayList<LatLng>();
	         int index = 0, len = encoded.length();
	         int lat = 0, lng = 0;
	         while (index < len) {
	             int b, shift = 0, result = 0;
	             do {
	                 b = encoded.charAt(index++) - 63;
	                 result |= (b & 0x1f) << shift;
	                 shift += 5;
	             } while (b >= 0x20);
	             int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	             lat += dlat;
	             shift = 0;
	             result = 0;
	             do {
	                 b = encoded.charAt(index++) - 63;
	                 result |= (b & 0x1f) << shift;
	                 shift += 5;
	             } while (b >= 0x20);
	             int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	             lng += dlng;

	             LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
	             poly.add(position);
	         }
	         return poly;
	     }

	}

}
