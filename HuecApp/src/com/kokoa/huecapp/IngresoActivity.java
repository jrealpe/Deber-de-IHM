package com.kokoa.huecapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.kokoa.huecapp.R;
import com.kokoa.huecapp.fragments.LoginFragment;
import com.kokoa.huecapp.fragments.RegistroFragment;
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class IngresoActivity extends Activity {
	public static Fragment fragment;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);
		fragment= new LoginFragment();
		
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			setTitle("Login");
			
		} else {
			
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	public void elegirFragment(){
		
	}
	

	@Override
	public void onBackPressed() {
				
		RegistroFragment myFragment = (RegistroFragment) getFragmentManager().findFragmentByTag("Register");
		
		if (myFragment != null && myFragment.isVisible()) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, new LoginFragment()).commit();
		}else
			super.onBackPressed();
		
	}
}
