package pl.waw.mizinski.alarm.activity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pl.waw.mizinski.alarm.R;
import pl.waw.mizinski.alarm.model.AlarmEntry;
import pl.waw.mizinski.alarm.model.Riddle;
import pl.waw.mizinski.alarm.model.Riddles;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RiddleActivity extends Activity {
	
	private static final String LAST_CLICKED_BUTTON_ID = "pl_waw_mizinski_alarm_lcbi";
	
	private Riddle riddle;
	private AlarmEntry alarmEntry;
	
	private TextView riddleTextView;
	
	private Button buttonA;
	private Button buttonB;
	private Button buttonC;
	private Button buttonD;
	
	private Button buttonWithCorrectAnswer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.riddle);
		alarmEntry = (AlarmEntry) getIntent().getSerializableExtra(AlarmEntry.class.getCanonicalName());
		riddle = Riddles.getRandomRiddle();
		initUIComponents();
		setUIComponentsValues();
		setButtonsListeners();
	}


	private void setButtonsListeners() {
		for (Button button : Arrays.asList(buttonA, buttonB, buttonC, buttonD)){
			if (button == buttonWithCorrectAnswer) {
				button.setOnClickListener(new CorrectAnswerListener());
			} else {
				button.setOnClickListener(new WrongAnswerListener());
			}
		}
	}


	private void setUIComponentsValues() {
		riddleTextView.setText(riddle.getRiddle());
		List<Button> buttons = Arrays.asList(buttonA, buttonB, buttonC, buttonD);
		shuffle(buttons);
		for (int i=0; i<3; ++i){
			buttons.get(i).setText(riddle.getOtherAnswers()[i]);
		}
		buttons.get(3).setText(riddle.getCorrectAnswer());
		buttonWithCorrectAnswer = buttons.get(3);
	}

	private void shuffle(List<Button> buttons) {
		int lastClickedButtonId = getIntent().getIntExtra(LAST_CLICKED_BUTTON_ID, -1);
		do {
			Collections.shuffle(buttons);
		} while (buttons.get(3).getId() == lastClickedButtonId );
	}


	private void initUIComponents() {
		riddleTextView = (TextView) findViewById(R.id.riddleTextView);
		buttonA = (Button) findViewById(R.id.buttonA);
		buttonB = (Button) findViewById(R.id.buttonB);
		buttonC= (Button) findViewById(R.id.buttonC);
		buttonD = (Button) findViewById(R.id.buttonD);
	}
	
	private class CorrectAnswerListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(getApplicationContext(), DisableAlarmActivity.class);
			intent.putExtra(AlarmEntry.class.getCanonicalName(), alarmEntry);
			finish();
			startActivity(intent);
		}
	} 
	
	private class WrongAnswerListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			Toast.makeText(getApplicationContext(), "Błędna odpowiedź", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(), RiddleActivity.class);
			intent.putExtra(AlarmEntry.class.getCanonicalName(), alarmEntry);
			intent.putExtra(LAST_CLICKED_BUTTON_ID, view.getId());
			finish();
			startActivity(intent);
		}
	} 
}
