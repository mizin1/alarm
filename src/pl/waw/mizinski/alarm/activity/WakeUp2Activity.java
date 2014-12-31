package pl.waw.mizinski.alarm.activity;

import pl.waw.mizinski.alarm.R;
import pl.waw.mizinski.alarm.model.AlarmEntry;
import pl.waw.mizinski.alarm.tools.MoveTool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WakeUp2Activity extends Activity {

	private AlarmEntry alarmEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wake_up2);
		alarmEntry = (AlarmEntry) getIntent().getSerializableExtra(AlarmEntry.class.getCanonicalName());
	}
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		new WakeUp2Thread().start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private class WakeUp2Thread extends Thread {
		
		@Override
		public void run() {
			try {
				MoveTool moveTool = MoveTool.getActiveMoveTool();
				moveTool.waitForPriority(alarmEntry.getDisablePriority());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				finish();
					Class<? extends Activity> activityClass = 
							alarmEntry.getRiddleActive() ? RiddleActivity.class : DisableAlarmActivity.class;
					Intent intent = new Intent(getApplicationContext(), activityClass);
					intent.putExtra(AlarmEntry.class.getCanonicalName(), alarmEntry);
					startActivity(intent);
			}
		
		}
	}
}
