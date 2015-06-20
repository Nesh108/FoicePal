
public class FPManager {
	
	
	public static void main(String[] args){
		
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
		
		Thread MTask = new Thread(new MotionRecognitionTask());
		Thread GUITask = new Thread(new GUITask());

		GUITask.start();
		MTask.start();
	}
	
	protected void doStuff(){
		
	}
	
	
	
	


}
