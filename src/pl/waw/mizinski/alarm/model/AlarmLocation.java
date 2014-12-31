package pl.waw.mizinski.alarm.model;

import pl.waw.mizinski.alarm.exception.AlarmException;
import android.location.Location;

public class AlarmLocation {
	
	private double x;
	private double y;
	
	public AlarmLocation() {
	}
	
	public AlarmLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public AlarmLocation(String x, String y) throws AlarmException {
		try {
			this.x = Double.valueOf(x);
			this.y = Double.valueOf(y);
		} catch (NumberFormatException e) {
			throw new AlarmException("Nieprawidłowy format wpółrzędnych", e);
		}
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public String getXAsString(){
		return Double.valueOf(x).toString();
	}
	
	public String getYAsString(){
		return Double.valueOf(y).toString();
	}
	
	public static AlarmLocation fromAdroidLocation(Location location) {
		AlarmLocation alarmLocation = new AlarmLocation();
		alarmLocation.setX(location.getLongitude());
		alarmLocation.setY(location.getLatitude());
		return alarmLocation;
	}
	
}
