import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;


public class MotionDetector implements WebcamMotionListener {
	WebcamMotionDetector detector = null;
	
	public MotionDetector() {
		detector = new WebcamMotionDetector(Webcam.getDefault());
		detector.setInterval(500); // one check per 500 ms
		detector.addMotionListener(this);
		detector.start();
	}

	@Override
	public void motionDetected(WebcamMotionEvent wme) {

		close();
		FaceRecognition.isDetected = true;

		synchronized (FaceRecognition.LOCK) {
			FaceRecognition.LOCK.notifyAll();
		}
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