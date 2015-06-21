import java.util.Random;

public class Speech {

	// Greetings when motion is detected
	static protected String GREETINGS_INTRO_TYPE = "G_INTRO";
	static protected String[] GREETINGS_INTRO = { "Hello there!", "Hey there!",
			"Hello!", "Hi!" };

	// Reply to feelings
	static protected String HOW_ARE_YOU_REPLY_TYPE = "HOW_ARE_YOU_REPLY";
	static protected String[] HOW_ARE_YOU_REPLY = { "I am good, thanks.",
			"I feel good, thank you.", "I am fine. Thanks for asking!" };

	// How is your day
	static protected String HOWS_DAY_TYPE = "HOWS_DAY";
	static protected String[] HOWS_DAY = { "So, how was your day?",
			"Tell me a bit about your day.", "How was your day?" };
	
	// How is your day reply
	static protected String HOWS_DAY_REPLY_TYPE = "HOWS_DAY_REPLY";
	static protected String[] HOWS_DAY_REPLY = { "It was ok, I didn't do much", "It was alright, thanks!" };

	// Plans for today
	static protected String PLANS_TODAY_TYPE = "PLANS_TODAY";
	static protected String[] PLANS_TODAY = { "So, what's your plans for today?",
			"Got plans for today?", "Any plans for today?" };
	
	// Plans for today reply
	static protected String PLANS_TODAY_REPLY_TYPE = "PLANS_TODAY_REPLY";
	static protected String[] PLANS_TODAY_REPLY = { "Yeah, I am going for a walk later on!", "No, I am quite busy with school." };
	
	
	// Greetings when customer has finished
	static protected String GREETINGS_OUTRO_TYPE = "G_OUTRO";
	static protected String[] GREETINGS_OUTRO = { "Goodbye!", "See you soon!",
			"Have a nice day!", "Have a great day!" };

	// Voice Training
	static protected String VOICE_TRAINING_TYPE = "V_TRAIN";
	static protected String[] VOICE_TRAINING = {
			"All we have to decide is what to do with the time that is given us.",
			"Courage is found in unlikely places.",
			"You cannot create experience. You must undergo it.",
			"Freedom is nothing but a chance to be better.",
			"You have to go on and be crazy. Craziness is like heaven.",
			"I'm gonna put a curse on you and all your kids will be born completely naked.",
			"The chief function of the body is to carry the brain around.",
			"Your worth consists in what you are and not in what you have." };

	static protected String getRandomString(String type) {

		Random r = new Random();

		if (type.equals(GREETINGS_INTRO_TYPE))
			return GREETINGS_INTRO[r.nextInt(GREETINGS_INTRO.length)];

		if (type.equals(GREETINGS_OUTRO_TYPE))
			return GREETINGS_OUTRO[r.nextInt(GREETINGS_OUTRO.length)];

		if (type.equals(HOW_ARE_YOU_REPLY_TYPE))
			return HOW_ARE_YOU_REPLY[r.nextInt(HOW_ARE_YOU_REPLY.length)];
		
		if (type.equals(HOWS_DAY_TYPE))
			return HOWS_DAY[r.nextInt(HOWS_DAY.length)];
		
		if (type.equals(HOWS_DAY_REPLY_TYPE))
			return HOWS_DAY_REPLY[r.nextInt(HOWS_DAY_REPLY.length)];
		

		// Not a recognized type
		else
			return "";
	}

}
