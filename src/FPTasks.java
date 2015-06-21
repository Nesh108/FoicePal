import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import org.json.JSONException;

import com.facepp.error.FaceppParseException;

public class FPTasks {

	protected static class MotionRecognitionTask implements Runnable {
		public void run() {
			// do stuff here
			System.out.println("Started MotionRecognition");
			new MotionDetector();
		}
	}

	protected static class SpeakerRecognitionTask implements Runnable {
		public void run() {
			System.out.println("Started SpeakerRecognition");
			try {
				new SpeakerRecognition();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	protected static class FaceRecognitionTask implements Runnable {
		public void run() {
			// do stuff here
			System.out.println("Started FaceRecognition");
			try {
				new FaceRecognition(false);

			} catch (InterruptedException | FaceppParseException
					| JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected static class GUITask implements Runnable {
		public void run() {
			// do stuff here
			System.out.println("Started GUI");
			new GUI();
		}
	}

	protected static class PaymentTask implements Runnable {
		public void run() {
			// do stuff here
			System.out.println("Started Payment");
			String transaction_id;
			if ((transaction_id = new PaymentHandler().sendPayment(
					GUI.getCustomerName(), GUI.getCustomerData(),
					GUI.getShoppingChart())) != null)
				GUI.goToCheckoutPanel(true, transaction_id);
			else
				GUI.goToCheckoutPanel(false, null);
		}
	}

	protected static class ProductScannerTask implements Runnable {
		public void run() {
			// do stuff here
			System.out.println("Started Product Scanner");
			new ProductScanner();
		}
	}

	public static String bot_action = "";

	protected static class BotControllerTask implements Runnable {
		public void run() {

			System.out.println("Started Video");
			VideoPlayer vp = new VideoPlayer(GUI.botPanel);

			while (Config.runBot) {
				if (!bot_action.equals("")) {
					if ((new File("video/" + bot_action + ".mov")).exists()) {
						Thread st = new Thread(new FPTasks.SpeakerTask());
						st.start();
						vp.playVideo("video/" + bot_action + ".mov", bot_action);
					} else if(bot_action.equals("So, how are you today?.mov")){

						Thread st = new Thread(new FPTasks.SpeakerTask());
						st.start();
						vp.playVideo("video/How are you?.mov", bot_action);
					}
					else{
						Thread st = new Thread(new FPTasks.SpeakerTask());
						st.start();
						vp.playVideo("video/Goodbye!.mov", bot_action);

						System.out.println("NOT PLAYING: video/" + bot_action
								+ ".mov");
					}

					bot_action = "";
				}
			}
		}
	}

	protected static class SpeakerTask implements Runnable {
		public void run() {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Tools.speakText(bot_action);
		}
	}

	protected static class ChatterBotTask implements Runnable {
		public void run() {
			try {
				System.out.println("Starter chatter bot");
				new ChatterBot();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
