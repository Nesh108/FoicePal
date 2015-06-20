import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.github.sarxos.webcam.Webcam;


public class ImageFetcher {
	
	private static boolean isCameraOn = false;
	
	private static Webcam webcam = null;
	
	protected static void openCamera(){
		
		if(webcam != null && webcam.isOpen())
			webcam.close();
		Webcam.setAutoOpenMode(true);
		webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(640, 480));
		webcam.open();
		isCameraOn = true;
	}

	protected static void closeCamera(){
		webcam.close();
		isCameraOn = false;
	}
	
	protected static BufferedImage fetchImage() throws IOException, InterruptedException{
		
		if(!isCameraOn)
			return null;

		Thread.sleep(100);
		BufferedImage img = webcam.getImage();

		return img;
	}
}
