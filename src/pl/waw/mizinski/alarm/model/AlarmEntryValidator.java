package pl.waw.mizinski.alarm.model;

import pl.waw.mizinski.alarm.exception.AlarmException;

public class AlarmEntryValidator {
	
	private static final double MAX_X = 180.0;
	private static final double MIN_X = -180.0;
	private static final double MAX_Y = 90.0;
	private static final double MIN_Y = -90.0;
	
	
	public static void validateAlarmEnrty(AlarmEntry alarmEntry) throws AlarmException {
		validateDaysOfWeek(alarmEntry);
		validateLocationIfNecessary(alarmEntry);
	}


	private static void validateLocationIfNecessary(AlarmEntry alarmEntry) throws AlarmException {
		if (alarmEntry.getLocationActive()) {
			validateLocation(alarmEntry);
		}
	}


	private static void validateLocation(AlarmEntry alarmEntry) throws AlarmException {
		if(alarmEntry.getLocationX() < MIN_X) {
			throw new AlarmException("Długość geograficzna nie może być niższa niż " + MIN_X);
		}
		if(alarmEntry.getLocationX() > MAX_X) {
			throw new AlarmException("Długość geograficzna nie może być większa niż " + MAX_X);
		}
		if(alarmEntry.getLocationY() < MIN_Y) {
			throw new AlarmException("Szerokość geograficzna Y nie może być niższa niż " + MIN_Y);
		}
		if(alarmEntry.getLocationY() > MAX_Y) {
			throw new AlarmException("Szerokość geograficzna nie może być większa niż " + MAX_Y);
		}
	}
	
	

	private static void validateDaysOfWeek(AlarmEntry alarmEntry) throws AlarmException {
		if (alarmEntry.getDaysOfWeek() == null || alarmEntry.getDaysOfWeek().isEmpty()) {
			throw new AlarmException("Proszę wybrać chociaż jeden z dni tygodnia");
		}
	}
}
