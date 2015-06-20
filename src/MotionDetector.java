import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;


public class MotionDetector implements WebcamMotionListener {
	WebcamMotionDetector detector = null;

	static public Object LOCK = new Object();
	static boolean isDetected = false;
	
	
	public MotionDetector() {
		detector = new WebcamMotionDetector(Webcam.getDefault());
		detector.setInterval(500); // one check per 500 ms
		detector.addMotionListener(this);
		detector.start();
		
		waitMotion();
		
		if(Config.DEBUG)
			System.out.println("Detected");
		
		
	}
	
	protected static void waitMotion(){
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
		FaceRecognition.isDetected = true;

		synchronized (FaceRecognition.LOCK) {
			FaceRecognition.LOCK.notifyAll();
		}
		GUI.toggleCoverPanel();
		Tools.speakText(Speech.getRandomString(Speech.GREETINGS_INTRO_TYPE));
		
		
	}
	
	public void close(){
		if(detector != null)
		{
			detector.stop();
			detector = null;
		}
	}
}