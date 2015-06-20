import java.io.IOException;

import org.json.JSONException;

import com.facepp.error.FaceppParseException;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;


public class MotionDetector implements WebcamMotionListener {
	WebcamMotionDetector detector = null;

	static public Object LOCK = new Object();
	static boolean isDetected = false;
	
	Thread SRTask = new Thread(new SpeakerRecognitionTask());
	Thread FRTask = new Thread(new FaceRecognitionTask());
	
	class SpeakerRecognitionTask implements Runnable {
	     public void run() {
	         // do stuff here
	    	 System.out.println("Started SpeakerRecognition");
	     }
	}
	
	class FaceRecognitionTask implements Runnable {
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
	
	public MotionDetector() {

		SRTask = new Thread(new SpeakerRecognitionTask());
		FRTask = new Thread(new FaceRecognitionTask());
		
		detector = new WebcamMotionDetector(Webcam.getDefault());
		detector.setInterval(500); // one check per 500 ms
		detector.addMotionListener(this);
		detector.start();
		
		waitMotion();
		
		if(Config.DEBUG)
			System.out.println("Detected");
		
		
	}
	
	protected void waitMotion(){
		synchronized (LOCK) {
		    while (!isDetected) {
		        try { LOCK.wait(); }
		        catch (InterruptedException e) {
		            // treat interrupt as exit request
		            break;
		        }
		    }
		}
	}

	@Override
	public void motionDetected(WebcamMotionEvent wme) {

		close();
		isDetected = true;

		synchronized (LOCK) {
			LOCK.notifyAll();
		}
		GUI.toggleCoverPanel();
		Tools.speakText(Speech.getRandomString(Speech.GREETINGS_INTRO_TYPE));
		

		SRTask.start();
		FRTask.start();
		
	}
	
	public void close(){
		if(detector != null)
		{
			detector.stop();
			detector = null;
		}
	}
}