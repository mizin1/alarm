package pl.waw.mizinski.alarm.tools;

import java.util.Calendar;
import java.util.Date;

import pl.waw.mizinski.alarm.model.AlarmEntry;
import pl.waw.mizinski.alarm.service.AlarmService;
import pl.waw.mizinski.alarm.service.LocationService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ScheduleTool {
	
	public static final long SNOOZE = 5 * 60 * 1000; 
	public static final long CECK_LOCATION = 150 * 1000; 
	
	private Context context;
	
	public ScheduleTool(Context context) {
		this.context = context;
	}
	
	public void scheduleNext(AlarmEntry alarmEntry) {
		Intent intent = new Intent(context, AlarmService.class);
		intent.putExtra(AlarmEntry.class.getCanonicalName(), alarmEntry);
		PendingIntent pendingIntent = PendingIntent.getService(context, alarmEntry.getId().intValue() , intent, PendingIntent.FLAG_UPDATE_CURRENT) ;
		long nextAlarmTimestamp = nextAlarmTime(alarmEntry);
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarmTimestamp, pendingIntent);
		
		if(alarmEntry.getLocationActive()) {
			Intent locationIntent = new Intent(context, LocationService.class);
			PendingIntent locationPendingIntent = PendingIntent.getService(context, -alarmEntry.getId().intValue(), locationIntent, 0);
			alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarmTimestamp - CECK_LOCATION, locationPendingIntent);
		}
	}
	

	public void scheduleSnooze(AlarmEntry alarmEntry) {
		Intent intent = new Intent(context, AlarmService.class);
		intent.putExtra(AlarmEntry.class.getCanonicalName(), alarmEntry);
		intent.putExtra(AlarmService.SNOOZE_KEY, true);
		PendingIntent pendingIntent = PendingIntent.getService(context, -alarmEntry.getId().intValue() , intent, PendingIntent.FLAG_UPDATE_CURRENT) ;
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, new Date().getTime() + SNOOZE, pendingIntent);		
	}
	
	public void unschedule(AlarmEntry alarmEntry) {
		Intent intent = new Intent(context, AlarmService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, alarmEntry.getId().intValue(), intent, 0) ;
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}
	
	public void unscheduleSnooze(AlarmEntry alarmEntry) {
		Intent intent = new Intent(context, AlarmService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, -alarmEntry.getId().intValue() , intent, 0) ;
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}
	
	
	private long nextAlarmTime(AlarmEntry alarmEntry) {
		Calendar calendar = Calendar.getInstance();
		calendar.getMinimum(Calendar.HOUR_OF_DAY);
		calendar.set(Calendar.HOUR_OF_DAY, alarmEntry.getHours());
		calendar.set(Calendar.MINUTE, alarmEntry.getMinutes());
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		if (calendar.before(Calendar.getInstance())) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		System.out.println(calendar.getTime());
		return calendar.getTimeInMillis();
	}
	
	
}
