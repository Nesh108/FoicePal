
public class FPManager {
	
	
	public static void main(String[] args){
		
		Thread MTask;
		Thread GUITask;
		
		class MotionRecognitionTask implements Runnable {
		     public void run() {
		         // do stuff here
		    	 System.out.println("Started MotionRecognition");
		    	 new MotionDetector();
		     }
		}
		
		
		class GUITask implements Runnable {
		     public void run() {
		         // do stuff here
		    	 System.out.println("Started GUI");
		    	 new GUI();
		     }
		}
		
		MTask = new Thread(new MotionRecognitionTask());
		GUITask = new Thread(new GUITask());

		MTask.start();
		GUITask.start();
		
	}
	
	
	
	
	


}
