package pl.waw.mizinski.alarm.activity;

import pl.waw.mizinski.alarm.R;
import pl.waw.mizinski.alarm.model.AlarmEntry;
import pl.waw.mizinski.alarm.tools.AudioTool;
import pl.waw.mizinski.alarm.tools.MoveTool;
import pl.waw.mizinski.alarm.tools.ScheduleTool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class WakeUpActivity extends Activity {

	private AlarmEntry alarmEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wake_up);
		alarmEntry = (AlarmEntry) getIntent().getSerializableExtra(AlarmEntry.class.getCanonicalName());
		Window window = this.getWindow();
		window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
		window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.setType(LayoutParams.TYPE_KEYGUARD);
		new WakeUpThread().start();
	}
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
	}
	
	private class WakeUpThread extends Thread {
		
		@Override
		public void run() {
			AudioTool audioTool = null;
			try {
				MoveTool moveTool = new MoveTool(getApplicationContext()).open();
				audioTool = new AudioTool(getApplicationContext());
				audioTool.startPlaying();
				moveTool.waitForPriority(alarmEntry.getSnoozePriority());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (audioTool!=null) {
					audioTool.stopPlaying();
				}
				Intent intent = new Intent(getApplicationContext(), WakeUp2Activity.class);
				intent.putExtra(AlarmEntry.class.getCanonicalName(), alarmEntry);
				ScheduleTool scheduleTool = new ScheduleTool(getApplicationContext());
				scheduleTool.scheduleSnooze(alarmEntry);
				finish();
				startActivity(intent);
			}
		}
	}
}
