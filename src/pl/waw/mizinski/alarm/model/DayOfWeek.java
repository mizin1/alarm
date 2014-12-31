package pl.waw.mizinski.alarm.model;

import java.util.Calendar;

public enum DayOfWeek {
	MONDAY ("Pon"),
	TUESDAY ("Wto"),
	WEDNESDAY ("Śro"),
	THURSDAY ("Czw"),
	FRIDAY ("Pią"),
	SATURDAY ("Sob"),
	SUNDAY ("Nie");
	
	private String name;
	
	private DayOfWeek(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static DayOfWeek fromCalendar(Calendar calendar) {
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			return MONDAY;
		case Calendar.TUESDAY:
			return TUESDAY;
		case Calendar.WEDNESDAY:
			return WEDNESDAY;
		case Calendar.THURSDAY:
			return THURSDAY;
		case Calendar.FRIDAY:
			return FRIDAY;
		case Calendar.SATURDAY:
			return SATURDAY;
		case Calendar.SUNDAY:
			return SUNDAY;
		default:
			return null;
		}
	}
} 
