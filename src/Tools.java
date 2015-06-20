import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.MessageFormat;

import javax.imageio.ImageIO;

import javazoom.jl.player.Player;
import us.monoid.web.BinaryResource;
import us.monoid.web.Resty;

import com.gtranslate.Language;

public class Tools {
	
	private static String LISPEAK_PATH = "/home/nesh/Applications/LiSpeak/Recognition/bin/";
	private static final String BASE_URL = "http://translate.google.com/translate_tts?ie=UTF-8&q={0}&tl={1}&prev=input";
    
	protected static String convertTagIntoEmail(String tag) {
		return tag.replace("[at]", "@");
	}
	
	protected static byte[] getByteFromBufferedImage(BufferedImage img) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( img, "jpg", baos );
		baos.flush();
		byte[] img_data = baos.toByteArray();
		baos.close();
		
		return img_data;
	}
	
	protected static void speakText2(String words){
		if(Config.DEBUG)
			System.out.println("Saying: '" + words + "'");
		Runtime rt = Runtime.getRuntime();
		String[] cmd = {LISPEAK_PATH + "say",words};
		try {
			rt.exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void speakText(String text)
    {
		try{
			
			// Generate speech
	        File file = new File("toSpeech.mp3");
	        String sentence = URLEncoder.encode(text, "UTF-8");
	        String urlString = MessageFormat.format(BASE_URL, sentence, Language.ENGLISH);
	        BinaryResource binaryResource = new Resty().bytes(new URI(urlString));
	        binaryResource.save(file);
	        
	        // Speak
	        FileInputStream fileInputStream = new FileInputStream(file);
	        Player player = new Player(fileInputStream);
	        player.play();
	        player.close();
	        file.delete();
		}
		catch(Exception e)
		{
			
		}
    }
	

}
