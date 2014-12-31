package pl.waw.mizinski.alarm.model;

public class Riddle {
	
	private String riddle;
	private String correctAnswer;
	private String[] otherAnswers;
	
	public Riddle(String riddle, String correctAnswer, String... otherAnswers) {
		this.riddle = riddle;
		this.correctAnswer = correctAnswer;
		this.otherAnswers = otherAnswers;
	}

	public String getRiddle() {
		return riddle;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public String[] getOtherAnswers() {
		return otherAnswers;
	}
	
}
