
public class FPManager {
	
	
	public static void main(String[] args){
		
		class SpeakerRecognitionTask implements Runnable {
		     public void run() {
		         // do stuff here
		    	 System.out.println("Started SpeakerRecognition");
		     }
		}
		
		class FaceRecognitionTask implements Runnable {
		     public void run() {
		         // do stuff here
		    	 System.out.println("Started VoiceRecognition");
		     }
		}
		
		
		class GUITask implements Runnable {
		     public void run() {
		         // do stuff here
		    	 System.out.println("Started GUI");
		     }
		}
		
		Thread SRTask = new Thread(new SpeakerRecognitionTask());
		Thread FRTask = new Thread(new FaceRecognitionTask());
		Thread GUITask = new Thread(new GUITask());
		SRTask.start();
		FRTask.start();
		GUITask.start();
	}
	
	
	
	


}
