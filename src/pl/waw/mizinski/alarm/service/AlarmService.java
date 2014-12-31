package pl.waw.mizinski.alarm.service;


import java.util.Calendar;

import pl.waw.mizinski.alarm.activity.WakeUpActivity;
import pl.waw.mizinski.alarm.model.AlarmEntry;
import pl.waw.mizinski.alarm.model.AlarmLocation;
import pl.waw.mizinski.alarm.model.DayOfWeek;
import pl.waw.mizinski.alarm.tools.LocationTool;
import pl.waw.mizinski.alarm.tools.ScheduleTool;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class AlarmService extends IntentService {
	
	public static final Double X_MULTIPLIER = 0.62;
	public static final Double Y_MULTIPLIER  = 1.0;
	public static final Double LOCATION_LIMIT = 0.1;
	
	public static final String SNOOZE_KEY = "pl_waw_mizinski_alarm_alarmservice_snooze";
	
	private AlarmEntry alarmEntry;
	
	public AlarmService() {
		super("pl_waw_mizinski_alarm_alarmservice");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		alarmEntry = (AlarmEntry) intent.getSerializableExtra(AlarmEntry.class.getCanonicalName());
		ScheduleTool scheduleTool = new ScheduleTool(getApplicationContext());
		if (intent.getBooleanExtra(SNOOZE_KEY, false)) {
			fireAlarm(getApplicationContext());
		} else {
			scheduleTool.scheduleNext(alarmEntry);
			if (checkDayOfTheWeek() && checkLocation(getApplicationContext())) {
				fireAlarm(getApplicationContext());
			}
		} 
		
	}
	
	private void fireAlarm(Context context) {
		Intent intent = new Intent(context, WakeUpActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(AlarmEntry.class.getCanonicalName(), alarmEntry);
		context.startActivity(intent);
	}

	private boolean checkDayOfTheWeek() {
		DayOfWeek dayOfWeek = DayOfWeek.fromCalendar(Calendar.getInstance());
		return alarmEntry.getDaysOfWeek().contains(dayOfWeek);
	}
	
	private boolean checkLocation(Context context){
		if (alarmEntry.getLocationActive() == false){
			return true;
		}
		LocationTool locationTool = LocationTool.getInstance(context);
		AlarmLocation alarmLocation = locationTool.getAlarmLocation();
		if (alarmLocation == null) {
			//nieznamy lokalizacji wiec na wszelki wypadek uruchamiamy alarm
			return true;
		}
		AlarmLocation entryLocation = alarmEntry.getAlarmLocation();
		Double difference = getDifference(alarmLocation, entryLocation);
		return difference < LOCATION_LIMIT;
	}

	private Double getDifference(AlarmLocation alarmLocation1, AlarmLocation alarmLocation2) {
		Double xDiff = Math.abs(alarmLocation1.getX()-alarmLocation2.getX())*X_MULTIPLIER;
		xDiff*=xDiff;
		Double yDiff = Math.abs(alarmLocation1.getY()-alarmLocation2.getY())*Y_MULTIPLIER;
		yDiff*=yDiff;
		return Math.sqrt(xDiff + yDiff);
	}

	
	
}
