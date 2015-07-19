package com.kokoa.huecapp.litlefluffy;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.util.Log;

public class Daemon extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// output debug to LogCat, with tag LittleFluffyLocationLibrary
		LocationLibrary.showDebugOutput(true);
		try {
			// in most cases the following initialising code using defaults is
			// probably sufficient:
			//
			// LocationLibrary.initialiseLibrary(getBaseContext(),
			// "com.your.package.name");
			//
			// however for the purposes of the test app, we will request
			// unrealistically frequent location broadcasts
			// every 1 minute, and force a location update if there hasn't been
			// one for 2 minutes.

			LocationLibrary.initialiseLibrary(getBaseContext(), 1 * 1000,
					2 * 1000, "com.kokoa.huecapp.MainActivity");

		} catch (UnsupportedOperationException ex) {
			Log.d("CacaoReporte",
					"UnsupportedOperationException thrown - the device doesn't have any location providers");
		}
	}

}
