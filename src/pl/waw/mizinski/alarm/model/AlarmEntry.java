package pl.waw.mizinski.alarm.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class AlarmEntry implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer hours;
	private Integer minutes;
	private Set<DayOfWeek> daysOfWeek = new TreeSet<DayOfWeek>();
	
	private Integer snoozePriority = 0;
	private Integer disablePriority = 0;
	
	private Boolean riddleActive = false;
	private Boolean locationActive = false;
	
	private Double locationX;
	private Double locationY;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getHours() {
		return hours;
	}

	public String getHoursAsString(){
		return hours < 10 ? "0" + hours : String.valueOf(hours);
	}
	
	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public String getMinutesAsString(){
		return minutes < 10 ? "0" + minutes : String.valueOf(minutes);
	}
	
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public Set<DayOfWeek> getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
	
	public void addDayOfWeek(DayOfWeek dayOfWeek) {
		daysOfWeek.add(dayOfWeek);
	}

	public Integer getSnoozePriority() {
		return snoozePriority;
	}

	public void setSnoozePriority(Integer snoozePriority) {
		this.snoozePriority = snoozePriority;
	}

	public Integer getDisablePriority() {
		return disablePriority;
	}

	public void setDisablePriority(Integer disablePriority) {
		this.disablePriority = disablePriority;
	}

	public Boolean getRiddleActive() {
		return riddleActive;
	}

	public void setRiddleActive(Boolean riddleActive) {
		this.riddleActive = riddleActive;
	}

	public Boolean getLocationActive() {
		return locationActive;
	}

	public void setLocationActive(Boolean locationActive) {
		this.locationActive = locationActive;
	}

	public Double getLocationX() {
		return locationX;
	}

	public void setLocationX(Double locationX) {
		this.locationX = locationX;
	}

	public Double getLocationY() {
		return locationY;
	}

	public void setLocationY(Double locationY) {
		this.locationY = locationY;
	}
	
	public AlarmLocation getAlarmLocation() {
		return new AlarmLocation(locationX, locationY);
	}
	
	public void setAlarmLocation(AlarmLocation alarmLocation){
		this.locationX = alarmLocation.getX();
		this.locationY = alarmLocation.getY();
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getHoursAsString());
		stringBuilder.append(" : ");
		stringBuilder.append(getMinutesAsString());
		if (daysOfWeek != null){
			for (DayOfWeek dayOfWeek : daysOfWeek) {
				stringBuilder.append(", ");
				stringBuilder.append(dayOfWeek.getName());
			}
		}
		return stringBuilder.toString();
	}
	
}
