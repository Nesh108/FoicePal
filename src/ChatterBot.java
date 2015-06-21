import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class ChatterBot {

	public ChatterBot() throws IOException {
		try {
			SpeechToText speechToText = new SpeechToText();

			speechToText.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String prev_question = "HOW_ARE_YOU";

	public static void think(String text) {

		if (prev_question.equals("HOW_ARE_YOU")) {
			if (text.toLowerCase().contains("and you")) {
				Tools.speakText(Speech.getRandomString(Speech.HOW_ARE_YOU_REPLY_TYPE));
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Tools.speakText(Speech.getRandomString(Speech.HOWS_DAY_TYPE));
			prev_question = "HOWS_DAY";
		} else if (prev_question.equals("HOWS_DAY")) {

			if (text.toLowerCase().contains("and your")) {
				Tools.speakText(Speech
						.getRandomString(Speech.HOWS_DAY_REPLY_TYPE));
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Tools.speakText(Speech.getRandomString(Speech.PLANS_TODAY_TYPE));
			prev_question = "PLANS_TODAY";
		} else if (prev_question.equals("HOWS_DAY")) {

			if (text.toLowerCase().contains("and you")) {
				Tools.speakText(Speech
						.getRandomString(Speech.HOWS_DAY_REPLY_TYPE));
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Tools.speakText(Speech.getRandomString(Speech.PLANS_TODAY_TYPE));
			prev_question = "PLANS_TODAY";
		}
		else
			Tools.speakText(Speech.getRandomString("What's up?"));

	}

	private static class SpeechToText extends Thread {
		public void run() {
			try {
				Configuration configuration = new Configuration();
				configuration
						.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
				configuration
						.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
				configuration
						.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");

				LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(
						configuration);
				recognizer.startRecognition(true);

				SpeechResult result;
				while ((result = recognizer.getResult()) != null) {

					if (result.getHypothesis().length() > 3) {
						System.out.println("You said: "
								+ result.getHypothesis());
						think(result.getHypothesis());
					}
				}
				recognizer.stopRecognition();
			} catch (Exception e) {

			}
		}
	}
}