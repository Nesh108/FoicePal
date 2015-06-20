import java.util.Random;


public class Speech {
	
	// Greetings when motion is detected
	static protected String GREETINGS_INTRO_TYPE = "G_INTRO";
	static protected String[] GREETINGS_INTRO =  
		{"Hello there!", "Hey there!","Hello!","Hi!"}; 
	
	static protected String getRandomString(String type){
		
		Random r = new Random();
		
		if(type.equals(GREETINGS_INTRO_TYPE))
		{
			return GREETINGS_INTRO[r.nextInt(GREETINGS_INTRO.length)];
		}
		
		// Not a recognized type
		else return "";
	}

}
