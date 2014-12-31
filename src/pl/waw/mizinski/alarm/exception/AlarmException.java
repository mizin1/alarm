package pl.waw.mizinski.alarm.exception;

public class AlarmException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public AlarmException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AlarmException(String detailMessage) {
		super(detailMessage);
	}

}
