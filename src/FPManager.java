
public class FPManager {
	
	
	public static void main(String[] args){
		
		
		Thread MTask;
		Thread GUITask;
		
		MTask = new Thread(new FPTasks.MotionRecognitionTask());
		GUITask = new Thread(new FPTasks.GUITask());

		MTask.start();
		GUITask.start();
		
	}


}
