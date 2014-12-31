package pl.waw.mizinski.alarm.tools;

import java.io.Serializable;

import pl.waw.mizinski.alarm.model.AlarmLocation;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;


public class LocationTool implements Serializable{

	private static final long serialVersionUID = 1L;

	private LocationManager locationManager;
	
	private Location lastNetworkLocation;
	private Location lastGpsLocation;
	 
	private static LocationTool instance;
	
	public synchronized static LocationTool getInstance(Context context) {
		if (instance == null){
			instance = new LocationTool(context);
		}
		return instance;
	}
	
	public LocationTool(Context context) {
		super();
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	public void requestLocationUpdate() {
		lastNetworkLocation = null;
		lastGpsLocation = null;
		if  (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new GpsLocationListener(), Looper.getMainLooper());
		}
		if  (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new NetworkLocationListener(), Looper.getMainLooper());
		}
	}
	
	public AlarmLocation getAlarmLocation() {
		Location location = getLaterLocation(lastNetworkLocation, lastGpsLocation);
		return location != null ? AlarmLocation.fromAdroidLocation(location) : null;
	}
	
	public AlarmLocation getLastKnownAlarmLocation() {
		Location location = getLastKnownLocation();
		return location != null ? AlarmLocation.fromAdroidLocation(location) : null;
	}

	public long getLastKnownAlarmLocationTime() {
		Location location = getLastKnownLocation();
		return location != null ? location.getTime() : 0;
	}
	
	private Location getLastKnownLocation() {
		Location newtworkLocation = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?
				locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : lastNetworkLocation;
		Location gpsLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ?
				locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : lastGpsLocation;
		return getLaterLocation(newtworkLocation, gpsLocation);
	}
	
	private Location getLaterLocation(Location location1, Location location2) {
		if (location1 == null) {
			return location2 != null ? location2 : null;
		} else {
			return location2 == null ? location1 : 
				location2.getTime() > location1.getTime() ? 
				location2  : location1;
		}
	}
	
	
	private abstract class LocationListenerStub implements LocationListener {

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
	}
	
	private class GpsLocationListener extends LocationListenerStub {
		
		@Override
		public void onLocationChanged(Location location) {
			lastGpsLocation = location;
		}
	}
	
	private class NetworkLocationListener extends LocationListenerStub {
		
		@Override
		public void onLocationChanged(Location location) {
			lastNetworkLocation = location;
		}
	}
}


