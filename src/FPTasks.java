import java.io.IOException;

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
	         // do stuff here
	    	 System.out.println("Started SpeakerRecognition");
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
	
}
