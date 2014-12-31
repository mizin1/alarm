package pl.waw.mizinski.alarm.tools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;

public class MoveTool {
	
	private volatile double sum = 0;
	private BlockingQueue<Double> blockingQueue = new LinkedBlockingQueue<Double>();
	
	private SensorManager sensorManager;
	private AccelerateChangeListenerThread accelerateChangeListenerThread;
	
	private static MoveTool activeMoveTool;
	
	public MoveTool(Context context) {
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	
	public void waitForPriority(int priority) throws InterruptedException {
		double limit = createLimit(priority);
		while (sum < limit) {
			sum += blockingQueue.take();
		}
	}
	
	
	private double createLimit(int priority) {
		return priority < 33 ? priority * 5 + 1:
			priority < 66 ? priority * 10 :
			priority*20;
	}

	public MoveTool open() {
		if (accelerateChangeListenerThread == null) {
			accelerateChangeListenerThread = new AccelerateChangeListenerThread();
			accelerateChangeListenerThread.start();
			activeMoveTool = this;
		}
		sum = 0;
		return this;
	}

	public void reset() {
		open();
	}
	
	public void close() {
		if (accelerateChangeListenerThread != null) {
			accelerateChangeListenerThread.interrupt();
			accelerateChangeListenerThread = null;
			activeMoveTool = null;
		}
	}
	
	public static MoveTool getActiveMoveTool() {
		return activeMoveTool;
	}
	
	private class AccelerateChangeListener implements SensorEventListener {
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			//do nth
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			try {
				blockingQueue.put(scoreEvent(event));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		private double scoreEvent(SensorEvent event) {
			double value = Math.abs(event.values[0]) + Math.abs(event.values[1]) + Math.abs(event.values[2]);
			value-=10.0;
			return value < 0.0 ? 0.0 :
				value < 20.0 ? value :
				value < 40.0 ? 1.5*value :
					2.0*value;
		}
	}
	
	private class AccelerateChangeListenerThread extends Thread {

		private AccelerateChangeListener listener;
		
		
		public AccelerateChangeListenerThread() {
			super();
		}

		@Override
		public void run() {
			Looper.prepare();
			Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			listener = new AccelerateChangeListener();
			sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL, new Handler(Looper.myLooper()));
			Looper.loop();
		}

		@Override
		public void interrupt() {
			if (listener != null) {
				sensorManager.unregisterListener(listener);
				listener = null;
			}
			super.interrupt();
		}
	}
	
	
}
