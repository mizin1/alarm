package pl.waw.mizinski.alarm.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.waw.mizinski.alarm.model.AlarmEntry;
import pl.waw.mizinski.alarm.model.DayOfWeek;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteAlarmDao implements AlarmDao {

	private static final int DB_VERSION = 10;
	private static final String DB_NAME = "pl_waw_mizinski_alarm_db";
	
	private static final String CREATE_TABLES[] ={ 
			"CREATE TABLE ALARM ("
			+ "id integer PRIMARY KEY AUTOINCREMENT,"
			+ "hours integer not null,"
			+ "minutes integer not null,"
			+ "snooze_priority integer not null,"
			+ "disable_priority integer not null,"
			+ "riddle_active boolean not null,"
			+ "location_active boolean not null,"
			+ "location_x decimal(2,2) null,"
			+ "location_y decimal(2,2) null"
			+ ");",
			"CREATE TABLE ALARM_DAY ("
			+ "alarm_id integer not null,"
			+ "day varchar(20) not null"
			+ ")"
		};
	
	
			
	private static final String DROP_TABLES[] ={ 
				"DROP TABLE ALARM;",
				"DROP TABLE ALARM_DAY;"
		};
	
	private SQLiteDatabase db;
	private Context context;
	private DatabaseHelper databaseHelper;
	
	public SQLiteAlarmDao(Context context) {
		super();
		this.context = context;
	}

	public AlarmDao open(){
	    databaseHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
	    db = databaseHelper.getWritableDatabase();
	    return this;
	}
	
	public void close() {
		databaseHelper.close();
	}
	
	@Override
	public void saveAlarmEntry(AlarmEntry alarmEntry) {
		db.beginTransaction();
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put("hours", alarmEntry.getHours());
			contentValues.put("minutes", alarmEntry.getMinutes());
			contentValues.put("snooze_priority", alarmEntry.getSnoozePriority());
			contentValues.put("disable_priority", alarmEntry.getDisablePriority());
			contentValues.put("riddle_active", alarmEntry.getRiddleActive());
			contentValues.put("location_active", alarmEntry.getLocationActive());
			contentValues.put("location_x", alarmEntry.getLocationX());
			contentValues.put("location_y", alarmEntry.getLocationY());
			if (alarmEntry.getId() != null) {
				db.update("ALARM", contentValues, "id=" + alarmEntry.getId(), null);
			} else {
				long id = db.insert("ALARM", null, contentValues);
				if (id != -1) {
					alarmEntry.setId(id);
				} else {
					throw new RuntimeException("Can not save alarm entry!");
				}
			}
			db.delete("ALARM_DAY", "alarm_id=" + alarmEntry.getId(), null);
			for (DayOfWeek dayOfWeek : alarmEntry.getDaysOfWeek()) { 
				ContentValues dayContentValues = new ContentValues();
				dayContentValues.put("day", dayOfWeek.name());
				dayContentValues.put("alarm_id", alarmEntry.getId());
				db.insert("ALARM_DAY", null, dayContentValues);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void deleteArarmEntry(AlarmEntry alarmEntry) {
		db.delete("ALARM", "id=" + alarmEntry.getId(), null);
	}
	
	@Override
	public List<AlarmEntry> getAllAlarmEntries() {
		Cursor cursor = db.query("ALARM", new String[]{"*"}, null, null, null, null, null);
		List<AlarmEntry> ret = new LinkedList<AlarmEntry>();
		if (cursor.moveToFirst()){
			do {
				AlarmEntry alarmEntry = extractAlarmEntryFromCursor(cursor);
				Set<DayOfWeek> daysOfWeek = getDayOfWeeksForAlarmEntry(alarmEntry.getId());
				alarmEntry.setDaysOfWeek(daysOfWeek);
				ret.add(alarmEntry);
			} while(cursor.moveToNext());
		}
		return ret;
	}
	
	private Set<DayOfWeek> getDayOfWeeksForAlarmEntry(Long alarm_id) {
		Cursor cursor = db.query("ALARM_DAY", new String[]{"day"}, "alarm_id=" + alarm_id, null, null, null, null);
		Set<DayOfWeek> ret = new TreeSet<DayOfWeek>();
		if (cursor.moveToFirst()){
			do {
				DayOfWeek dayOfWeek = DayOfWeek.valueOf(cursor.getString(0));
				ret.add(dayOfWeek);
			} while(cursor.moveToNext());
		}
		return ret;
	}

	private AlarmEntry extractAlarmEntryFromCursor(Cursor cursor) {
		AlarmEntry alarmEntry = new AlarmEntry();
		alarmEntry.setId(getLong(cursor, "id"));
		alarmEntry.setHours(getInteger(cursor, "hours"));
		alarmEntry.setMinutes(getInteger(cursor, "minutes"));
		alarmEntry.setSnoozePriority(getInteger(cursor, "snooze_priority"));
		alarmEntry.setDisablePriority(getInteger(cursor, "disable_priority"));
		alarmEntry.setRiddleActive(getBoolean(cursor, "riddle_active"));
		alarmEntry.setLocationActive(getBoolean(cursor, "location_active"));
		alarmEntry.setLocationX(getDouble(cursor, "location_x"));
		alarmEntry.setLocationY(getDouble(cursor, "location_y"));
		return alarmEntry;
	}

	private Integer getInteger(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		return columnIndex > -1 ? cursor.getInt(columnIndex) : null;
	}
	
	private Long getLong(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		return columnIndex > -1 ? cursor.getLong(columnIndex) : null;
	}
	
	private Boolean getBoolean(Cursor cursor, String columnName) {
		Integer integerValue = getInteger(cursor, columnName);
		return integerValue != null ? integerValue > 0 : null;
	}
	
	private Double getDouble(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		return columnIndex > -1 ? cursor.getDouble(columnIndex) : null;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for (String sql : CREATE_TABLES) { 
				db.execSQL(sql);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for (String sql : DROP_TABLES) { 
				db.execSQL(sql);
			}
			onCreate(db);
		}
	}

}
