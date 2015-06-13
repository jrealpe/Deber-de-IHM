package com.kokoa.huecapp.fragments;

import java.util.ArrayList;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
import com.kokoa.huecapp.MainActivity;
import com.kokoa.huecapp.bd.BasedRegister_Restaurant;
import com.kokoa.huecapp.classes.Restaurant;
import com.kokoa.huecapp.json.JSONParser;
import com.kokoa.huecapp.views.ViewUtils;

/**
 * 
 * Simple custom SupportMapFragment that adds a margin.
 * 
 * @author ddewaele
 * 
 */
public class MapFragment extends SupportMapFragment {

	public ArrayList<MarkerOptions> markers_list = new ArrayList<MarkerOptions>();
	public GoogleMap map = null;
	public String id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = super.onCreateView(inflater, container, savedInstanceState);
		// ViewUtils.initializeMargin(getActivity(), view);
		initMap();
		return view;
	}

	public void initMap() {

		// UiSettings settings = getMap().getUiSettings();
		// settings.setAllGesturesEnabled(true);
		// settings.setMyLocationButtonEnabled(false);

		map = getMap();
		LatLng lat_lon = new LatLng(-3.258103, -79.955369);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_lon, 10));

		// show restaurants in map
		addMarkerRestaurant();
	}

	public void addMarkerRestaurant() {

		ArrayList<Restaurant> restaurants_list = null;
		BasedRegister_Restaurant br_rest = new BasedRegister_Restaurant(
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
					Restaurant r = restaurants_list.get(i);

					// Creacion del marker
					MarkerOptions mo = new MarkerOptions();
					LatLng lat_lon = new LatLng(r.getLatitude(),
							r.getLongitude());
					mo.position(lat_lon);
					mo.title(String.valueOf(i));
					/*
					 * 
					 * 
					 * map.setInfoWindowAdapter(new InfoWindowAdapter() {
					 * 
					 * // Use default InfoWindow frame
					 * 
					 * @Override public View getInfoWindow(Marker args) { return
					 * null; }
					 * 
					 * // Defines the contents of the InfoWindow
					 * 
					 * @Override public View getInfoContents(Marker args) {
					 * 
					 * // Getting view from the layout file info_window_layout
					 * View v =
					 * getLayoutInflater().inflate(R.layout.info_window_layout,
					 * null);
					 * 
					 * // Getting the position from the marker clickMarkerLatLng
					 * = args.getPosition();
					 * 
					 * TextView title = (TextView) v.findViewById(R.id.tvTitle);
					 * title.setText(args.getTitle());
					 * 
					 * map.setOnInfoWindowClickListener(new
					 * OnInfoWindowClickListener() { public void
					 * onInfoWindowClick(Marker marker) { if
					 * (SGTasksListAppObj.getInstance
					 * ().currentUserLocation!=null) { if
					 * (String.valueOf(SGTasksListAppObj
					 * .getInstance().currentUserLocation
					 * .getLatitude()).substring(0,
					 * 8).contains(String.valueOf(clickMarkerLatLng
					 * .latitude).substring(0, 8)) &&
					 * String.valueOf(SGTasksListAppObj
					 * .getInstance().currentUserLocation
					 * .getLongitude()).substring(0,
					 * 8).contains(String.valueOf(clickMarkerLatLng
					 * .longitude).substring(0, 8))) {
					 * Toast.makeText(getApplicationContext(),
					 * "This your current location, navigation is not needed.",
					 * Toast.LENGTH_SHORT).show(); } else { FlurryAgent.onEvent(
					 * "Start navigation window was clicked from daily map");
					 * tasksRepository =
					 * SGTasksListAppObj.getInstance().tasksRepository
					 * .getTasksRepository(); for (Task tmptask :
					 * tasksRepository) { String tempTaskLat =
					 * String.valueOf(tmptask.getLatitude()); String tempTaskLng
					 * = String.valueOf(tmptask.getLongtitude());
					 * 
					 * Log.d(TAG,
					 * String.valueOf(tmptask.getLatitude())+","+String
					 * .valueOf(clickMarkerLatLng.latitude).substring(0, 8));
					 * 
					 * if
					 * (tempTaskLat.contains(String.valueOf(clickMarkerLatLng.
					 * latitude).substring(0, 8)) &&
					 * tempTaskLng.contains(String.
					 * valueOf(clickMarkerLatLng.longitude).substring(0, 8))) {
					 * task = tmptask; break; } }
					 * 
					 * Intent intent = new Intent(getApplicationContext()
					 * ,RoadDirectionsActivity.class);
					 * intent.putExtra(TasksListActivity.KEY_ID, task.getId());
					 * startActivity(intent);
					 * 
					 * } } else { Toast.makeText(getApplicationContext(),
					 * "Your current location could not be found,\nNavigation is not possible."
					 * , Toast.LENGTH_SHORT).show(); } } });
					 * 
					 * // Returning the view containing InfoWindow contents
					 * return v;
					 * 
					 * } });
					 */

					map.setOnMarkerClickListener(new OnMarkerClickListener() {

						@Override
						public boolean onMarkerClick(Marker arg0) {

							id = arg0.getTitle();
							// h.sendEmptyMessage(0);
							/*
							 * new
							 * SweetAlertDialog(getActivity().getApplicationContext
							 * (), SweetAlertDialog.SUCCESS_TYPE).setTitleText(
							 * "Bien...").setContentText( "Marker select: " +
							 * id);
							 */
							// Toast.makeText(getActivity(), "Hola " +
							// arg0.getTitle(), 1000).show();
							return false;
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

	Handler h = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			new SweetAlertDialog(getActivity().getApplicationContext(),
					SweetAlertDialog.SUCCESS_TYPE).setTitleText("Bien...")
					.setContentText("Marker select: " + id);

		}

	};

}
