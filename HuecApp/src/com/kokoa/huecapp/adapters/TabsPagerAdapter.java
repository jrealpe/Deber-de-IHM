package com.kokoa.huecapp.adapters;


import com.kokoa.huecapp.fragments.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			// GoogleMapApi fragment activity
			return new MapFragment();
		case 1:
			// TopDishes fragment activity
			return new TopFragment();
		case 2:
			// FilterCategory fragment activity
			return new FilterFragment();
		case 3:
			// Menu fragment activity
			return new MenuFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
