package pl.waw.mizinski.alarm.service;

import pl.waw.mizinski.alarm.tools.LocationTool;
import android.app.IntentService;
import android.content.Intent;

public class LocationService  extends IntentService {

	public LocationService() {
		super("pl_waw_mizinski_alarm_locationservcice");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LocationTool.getInstance(getApplicationContext()).requestLocationUpdate();
	}

}
