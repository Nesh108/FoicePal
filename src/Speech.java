import java.util.Random;


public class Speech {
	
	// Greetings when motion is detected
	static protected String GREETINGS_INTRO_TYPE = "G_INTRO";
	static protected String[] GREETINGS_INTRO =  
		{"Hello there!", "Hey there!","Hello!","Hi!"}; 
	
	// Voice Training
	static protected String VOICE_TRAINING_TYPE = "V_TRAIN";
	static protected String[] VOICE_TRAINING =
		{"All we have to decide is what to do with the time that is given us.", 
		"Courage is found in unlikely places.",
		"You cannot create experience. You must undergo it.", 
		"Freedom is nothing but a chance to be better.", 
		"You have to go on and be crazy. Craziness is like heaven.", 
		"I'm gonna put a curse on you and all your kids will be born completely naked.", 
		"The chief function of the body is to carry the brain around.", 
		"Your worth consists in what you are and not in what you have."};
	
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
