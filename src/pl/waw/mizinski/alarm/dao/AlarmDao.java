package pl.waw.mizinski.alarm.dao;

import java.util.List;

import pl.waw.mizinski.alarm.model.AlarmEntry;

public interface AlarmDao {
	
	public AlarmDao open();
	
	public void close();
	
	List<AlarmEntry> getAllAlarmEntries();
	
	void saveAlarmEntry(AlarmEntry alarmEntry);
	
	void deleteArarmEntry(AlarmEntry alarmEntry);
	
}
