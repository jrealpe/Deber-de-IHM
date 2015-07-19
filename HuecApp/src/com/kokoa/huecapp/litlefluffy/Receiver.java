package com.kokoa.huecapp.litlefluffy;

import com.kokoa.huecapp.MainActivity;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.support.v4.app.NotificationCompat;

public class Receiver extends BroadcastReceiver {
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("LocationBroadcastReceiver",
				"onReceive: received location update");

		final LocationInfo locationInfo = (LocationInfo) intent
				.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);

		// The broadcast has woken up your app, and so you could do anything now
		// -
		// perhaps send the location to a server, or refresh an on-screen
		// widget.
		// We're gonna create a notification.
		
		/*
		 
		 new SendDatosTask().execute("",String.valueOf(locationInfo.lastLat),
				String.valueOf(locationInfo.lastLong));
		*/
		
		// Construct the notification.

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				//.setSmallIcon(R.drawable.notification)
				.setContentTitle("Ubicacion Actualizada")
				.setContentText(
						"En en tiempo:"
								+ LocationInfo
										.formatTimeAndDay(
												locationInfo.lastLocationUpdateTimestamp,
												true));
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1234, mBuilder.build());

	}
}