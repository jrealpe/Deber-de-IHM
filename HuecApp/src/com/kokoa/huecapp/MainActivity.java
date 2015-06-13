package com.kokoa.huecapp;


import com.google.android.gms.drive.internal.GetMetadataRequest;
import com.kokoa.huecapp.adapters.TabsPagerAdapter;
import com.kokoa.huecapp.bd.BasedRegister_Restaurant;
import com.kokoa.huecapp.bd.BasedRegister_User;
import com.kokoa.huecapp.classes.GetRestaurants;
import com.kokoa.huecapp.classes.Restaurant;
import com.kokoa.huecapp.fragments.MapFragment;
import com.kokoa.huecapp.json.JSONParser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	public static String id;
	// Tab titles
	private String[] tabs = { "Mapa", "Top", "Filter", "Menu" };

	@Override
    public boolean onCreateOptionsMenu(Menu menu){

         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.main, menu);
          return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnuranking:
                //newGame();
                return true;
            case R.id.mnuregplato:
                //showHelp();
                return true;
                
            case R.id.mnuregres:
                //showHelp();
                return true;
            case R.id.mnuultimas:
                //showHelp();
                return true;
                
            case R.id.mnucerrar:
    			final BasedRegister_User b=new BasedRegister_User(getApplicationContext());
    			b.getWritableDatabase().execSQL("UPDATE usuario SET estado='0' WHERE id="+id);
    			b.close();
    			Intent i=new Intent(MainActivity.this,IngresoActivity.class);
    			startActivity(i);
    			finish();
            	
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		
		// Update Information HuecApp
		new GetRestaurants(MainActivity.this).execute("","","");
		
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
	
	

}