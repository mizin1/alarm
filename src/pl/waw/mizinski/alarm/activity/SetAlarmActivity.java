package pl.waw.mizinski.alarm.activity;

import java.util.Calendar;

import pl.waw.mizinski.alarm.R;
import pl.waw.mizinski.alarm.dao.AlarmDao;
import pl.waw.mizinski.alarm.dao.SQLiteAlarmDao;
import pl.waw.mizinski.alarm.exception.AlarmException;
import pl.waw.mizinski.alarm.model.AlarmEntry;
import pl.waw.mizinski.alarm.model.AlarmEntryValidator;
import pl.waw.mizinski.alarm.model.AlarmLocation;
import pl.waw.mizinski.alarm.model.DayOfWeek;
import pl.waw.mizinski.alarm.tools.LocationTool;
import pl.waw.mizinski.alarm.tools.ScheduleTool;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView.BufferType;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SetAlarmActivity extends Activity {

	private AlarmDao alarmDao;
	private ScheduleTool scheduleTool;
	private LocationTool locationTool;
	private boolean locationUpdateRequested;
	
	private AlarmEntry alarmEntry;
	
	private TimePicker timePicker;
	
	private ToggleButton[] weekToogleButtons;
	
	private SeekBar snoozeSeekBar;
	private SeekBar disableAlarmSeekBar;
	private CheckBox riddleCheckBox;
	private CheckBox locationCheckBox;
	
	private EditText xEditText;
	private EditText yEditText;
	private Button locationButton;
	
	private Button saveButton;
	private Button deleteButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.set_alarm);
		alarmDao = new SQLiteAlarmDao(getApplicationContext()).open();
		scheduleTool = new ScheduleTool(getApplicationContext());
		locationTool = new LocationTool(getApplicationContext());
		alarmEntry = (AlarmEntry) getIntent().getSerializableExtra(AlarmEntry.class.getCanonicalName());
		initUIComponents();
		setUIComponentsValues();
		setButtonsListeners();
	}

	private void setButtonsListeners() {
		locationButton.setOnClickListener(new LocationButtonListerner());
		saveButton.setOnClickListener(new SaveButtonListerner());
		deleteButton.setOnClickListener(new DeleteButtonListener());
		locationCheckBox.setOnCheckedChangeListener(new LocationCheckBoxChangeListener());
	}

	private void setUIComponentsValues() {
		if (alarmEntry.getId()!= null){
			timePicker.setCurrentHour(alarmEntry.getHours());
			timePicker.setCurrentMinute(alarmEntry.getMinutes());
		} else {	
			Calendar calendar = Calendar.getInstance();
			timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			deleteButton.setEnabled(false);
		}
		for (DayOfWeek dayOfWeek : alarmEntry.getDaysOfWeek()) {
			weekToogleButtons[dayOfWeek.ordinal()].setChecked(true);
		}
		snoozeSeekBar.setProgress(alarmEntry.getSnoozePriority());
		disableAlarmSeekBar.setProgress(alarmEntry.getDisablePriority());
		riddleCheckBox.setChecked(alarmEntry.getRiddleActive());
		if (alarmEntry.getLocationActive()){
			locationCheckBox.setChecked(true);
			xEditText.setText(alarmEntry.getAlarmLocation().getXAsString());
			yEditText.setText(alarmEntry.getAlarmLocation().getYAsString());
			requestLocationUpdate();
		} else {
			locationButton.setEnabled(false);
			xEditText.setEnabled(false);
			yEditText.setEnabled(false);
		}
	}

	private void initUIComponents() {
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true);
		weekToogleButtons = new ToggleButton[7];
		weekToogleButtons[0] = (ToggleButton) findViewById(R.id.mon);
		weekToogleButtons[1] = (ToggleButton) findViewById(R.id.tue);
		weekToogleButtons[2] = (ToggleButton) findViewById(R.id.wed);
		weekToogleButtons[3] = (ToggleButton) findViewById(R.id.thu);
		weekToogleButtons[4] = (ToggleButton) findViewById(R.id.fri);
		weekToogleButtons[5] = (ToggleButton) findViewById(R.id.sat);
		weekToogleButtons[6] = (ToggleButton) findViewById(R.id.sun);
		
		snoozeSeekBar = (SeekBar) findViewById(R.id.snoozeSeekBar);
		disableAlarmSeekBar = (SeekBar) findViewById(R.id.disableAlarmSeekBar);
		riddleCheckBox = (CheckBox) findViewById(R.id.riddleCheckBox);
		locationCheckBox = (CheckBox) findViewById(R.id.locationCheckBox);
		
		xEditText = (EditText) findViewById(R.id.xEditText);
		yEditText = (EditText) findViewById(R.id.yEditText);
		
		locationButton = (Button) findViewById(R.id.locationButton);
		saveButton = (Button) findViewById(R.id.saveButton);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		
	}
	
	@Override
	protected void onDestroy() {
		alarmDao.close();
		super.onDestroy();
	}
	
	private void requestLocationUpdate() {
		if (!locationUpdateRequested) {
			locationTool.requestLocationUpdate();
			locationUpdateRequested = true;
		}
	}
	
	private class LocationButtonListerner implements View.OnClickListener {
		
		@Override
		public void onClick(View view) {
			AlarmLocation alarmLocation = locationTool.getAlarmLocation();
			if (alarmLocation == null && locationTool.getLastKnownAlarmLocation() != null) {
				alarmLocation =  locationTool.getLastKnownAlarmLocation();
				Toast.makeText(getApplicationContext(), getText(R.string.location_aut_of_date), Toast.LENGTH_LONG).show();
			}
			
			if (alarmLocation != null) {
				EditText xEditText = (EditText) findViewById(R.id.xEditText);
				EditText yEditText = (EditText) findViewById(R.id.yEditText);
				xEditText.setText(alarmLocation.getXAsString(), BufferType.EDITABLE);
				yEditText.setText(alarmLocation.getYAsString(), BufferType.EDITABLE);

			} else {
				Toast.makeText(getApplicationContext(), getText(R.string.location_unavaivable), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private class SaveButtonListerner implements View.OnClickListener {
	
		@Override
		public void onClick(View view) {
			try {
				alarmEntry.setHours(timePicker.getCurrentHour());
				alarmEntry.setMinutes(timePicker.getCurrentMinute());

				alarmEntry.setSnoozePriority(snoozeSeekBar.getProgress());
				alarmEntry.setDisablePriority(disableAlarmSeekBar.getProgress());
				
				alarmEntry.setRiddleActive(riddleCheckBox.isChecked());
				
				if (locationCheckBox.isChecked()) {
					alarmEntry.setLocationActive(true);
					alarmEntry.setAlarmLocation(new AlarmLocation(xEditText.getText().toString(), yEditText.getText().toString()));
				} else {
					alarmEntry.setLocationActive(false);
					alarmEntry.setLocationX(null);
					alarmEntry.setLocationY(null);
				}
				alarmEntry.getDaysOfWeek().clear();
				for(int i=0; i<7; ++i){
					if (weekToogleButtons[i].isChecked()) {
						alarmEntry.addDayOfWeek(DayOfWeek.values()[i]);
					}
				}
				AlarmEntryValidator.validateAlarmEnrty(alarmEntry);
				alarmDao.saveAlarmEntry(alarmEntry);
				scheduleTool.scheduleNext(alarmEntry);
				finish();
			} catch (AlarmException e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private class DeleteButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			alarmDao.deleteArarmEntry(alarmEntry);
			scheduleTool.unschedule(alarmEntry);
			finish();
		}
	}
	
	private class LocationCheckBoxChangeListener implements OnCheckedChangeListener {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			requestLocationUpdate();
			locationButton.setEnabled(isChecked);
			xEditText.setEnabled(isChecked);
			yEditText.setEnabled(isChecked);
		}
	
	}
}
