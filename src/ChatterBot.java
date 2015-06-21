import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class ChatterBot {

	public ChatterBot() throws IOException {
		try {
			
			

			Tools.speak("Tell me something while I try to recognize you.");
			
			Thread.sleep(5000);
			SpeechToText speechToText = new SpeechToText();
			speechToText.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void think(String text) {

		if (Config.prev_question.equals("HOW_ARE_YOU")) {
			if (!text.toLowerCase().contains("and youX")) {
				Tools.speakText(Speech.getRandomString(Speech.HOW_ARE_YOU_REPLY_TYPE));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Tools.speakText(Speech.getRandomString(Speech.HOWS_DAY_TYPE));
			Config.prev_question = "HOWS_DAY";
		} else if (Config.prev_question.equals("HOWS_DAY")) {

			if (!text.toLowerCase().contains("and yourX")) {
				Tools.speakText(Speech
						.getRandomString(Speech.HOWS_DAY_REPLY_TYPE));
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Tools.speakText(Speech.getRandomString(Speech.PLANS_TODAY_TYPE));
			Config.prev_question = "PLANS_TODAY";
		} else if (Config.prev_question.equals("PLANS_TODAY")) {

			if (!text.toLowerCase().contains("and youX")) {
				Tools.speakText(Speech
						.getRandomString(Speech.PLANS_TODAY_REPLY_TYPE));
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Tools.speakText("Did anything interesting happen to you today?");
			Config.prev_question = "HAPPEN";
		}
		else
			if(GUI.shoppingPanel.isVisible() && Config.prev_question.equals("HAPPEN")){
				Tools.speakText("How are things?");
				Config.prev_question = "HOW_THINGS";
				
			}
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
						
						if(GUI.isFaceRecognized && GUI.isVoiceRecognized)
							think(result.getHypothesis());
						
						Thread.sleep(1000);
					}
				}
				recognizer.stopRecognition();
			} catch (Exception e) {

			}
		}
	}
}