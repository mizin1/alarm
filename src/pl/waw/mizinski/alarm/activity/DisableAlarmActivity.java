package pl.waw.mizinski.alarm.activity;

import pl.waw.mizinski.alarm.R;
import pl.waw.mizinski.alarm.model.AlarmEntry;
import pl.waw.mizinski.alarm.tools.ScheduleTool;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DisableAlarmActivity extends Activity {

	private AlarmEntry alarmEntry;
	private Button disableAlarmButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disable_alarm);
		alarmEntry = (AlarmEntry) getIntent().getSerializableExtra(AlarmEntry.class.getCanonicalName());
		disableAlarmButton = (Button) findViewById(R.id.disableAlarmButton);
		disableAlarmButton.setOnClickListener(new DisableAlarmListener());
	}
	
	private class DisableAlarmListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			ScheduleTool scheduleTool = new ScheduleTool(getApplicationContext());
			scheduleTool.unscheduleSnooze(alarmEntry);
			finish();
		}
		
	}
}
