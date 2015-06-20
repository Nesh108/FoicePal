import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

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
	    	 new PaymentHandler().sendPayment(GUI.getCustomerName(),GUI.getCustomerData(), GUI.getProdData());
	     }
	}
	
}
