
public class FPManager {
	
	
	public static void main(String[] args){
		
		class MotionRecognitionTask implements Runnable {
		     public void run() {
		         // do stuff here
		    	 System.out.println("Started MotionRecognition");
		    	 new MotionDetector();
		     }
		}
		
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
		     }
		}
		
		
		class GUITask implements Runnable {
		     public void run() {
		         // do stuff here
		    	 System.out.println("Started GUI");
		    	 new GUI();
		     }
		}
		
		Thread MTask = new Thread(new MotionRecognitionTask());
		Thread SRTask = new Thread(new SpeakerRecognitionTask());
		Thread FRTask = new Thread(new FaceRecognitionTask());
		Thread GUITask = new Thread(new GUITask());

		GUITask.start();
		MTask.start();
		SRTask.start();
		FRTask.start();
	}
	
	
	
	


}
