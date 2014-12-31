package pl.waw.mizinski.alarm.tools;

import pl.waw.mizinski.alarm.R;
import pl.waw.mizinski.alarm.exception.AlarmException;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;

public class AudioTool {

	private static final long VIBRATE_TIME = 2000;
	
	private MediaPlayer mediaPlayer;
	private Vibrator vibrator;
	
	private static AudioTool activeAudioTool;
	
	public AudioTool(Context context) throws AlarmException {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mediaPlayer.setLooping(true);
			String path ="android.resource://" + context.getResources().getResourcePackageName(R.raw.alarm)+"/"+R.raw.alarm;
			mediaPlayer.setDataSource(context,Uri.parse(path));
			mediaPlayer.prepare();
			vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		} catch (Exception e) {
			if (mediaPlayer != null){
				stopPlaying();
			}
			throw new AlarmException("", e);
		}
	}

	public void startPlaying() {
		if (mediaPlayer !=null && !mediaPlayer.isPlaying()) {
			if (vibrator.hasVibrator()) {
				vibrator.vibrate(VIBRATE_TIME);
			}
			mediaPlayer.start();
			activeAudioTool = this;
		}
	}
	
	public void stopPlaying() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.release();
		mediaPlayer = null;
		activeAudioTool = null;
	}
	
	public static AudioTool getActiveAudioTool() {
		return activeAudioTool;
	}

}
