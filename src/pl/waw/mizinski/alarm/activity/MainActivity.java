package pl.waw.mizinski.alarm.activity;

import pl.waw.mizinski.alarm.R;
import pl.waw.mizinski.alarm.dao.AlarmDao;
import pl.waw.mizinski.alarm.dao.SQLiteAlarmDao;
import pl.waw.mizinski.alarm.model.AlarmEntry;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	private AlarmDao alarmDao; 
	
	private ListView alarmsListView;
	private Button addAlarmButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        alarmDao = new SQLiteAlarmDao(getApplicationContext()).open();
        initAddAlamButton();
        initAlarmsListView();
    }

	@Override
    protected void onResume() {
    	super.onResume();
    	fillAlarmsListView();
    }
    
	private void fillAlarmsListView() {
		ArrayAdapter<AlarmEntry> arrayAdapter = new ArrayAdapter<AlarmEntry>(this, R.layout.alarm__row, alarmDao.getAllAlarmEntries());
		alarmsListView.setAdapter(arrayAdapter);
	}

	private void initAlarmsListView() {
		alarmsListView = (ListView) findViewById(R.id.alarmsListView);
		alarmsListView.setOnItemClickListener(new AlarmListItemOnClickListener());
		
	}

	private void initAddAlamButton() {
		addAlarmButton = (Button) findViewById(R.id.addAlarmButton);
		addAlarmButton.setOnClickListener(new AddAlarmButtonOnClickListener());
	}
	
	@Override
	protected void onDestroy() {
		alarmDao.close();
		super.onDestroy();
	}
	
	private class AlarmListItemOnClickListener implements OnItemClickListener {

		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
			ArrayAdapter<AlarmEntry> adapter = (ArrayAdapter<AlarmEntry>) parent.getAdapter();
			Intent intent = new Intent(view.getContext(), SetAlarmActivity.class);
			intent.putExtra(AlarmEntry.class.getCanonicalName(), adapter.getItem(position));
			startActivity(intent);
		}
		
	}
	
	private class AddAlarmButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(view.getContext(), SetAlarmActivity.class);
			intent.putExtra(AlarmEntry.class.getCanonicalName(), new AlarmEntry());
			startActivity(intent);
		}
	}
}
