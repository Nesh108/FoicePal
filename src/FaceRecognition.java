import java.awt.image.BufferedImage;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;


public class FaceRecognition {
	
	static String API_KEY = "ab04a5c7375946e5a7d1e09c9197b379";
	static String API_SECRET_KEY = "2cFI85Ehg_DQbu_hVDIkkSY1B84QxLlO";
	
	static HttpRequests httpRequests = new HttpRequests(API_KEY, API_SECRET_KEY, true, false);
	static String URL_ARNOLD = "http://ia.media-imdb.com/images/M/MV5BMTI3MDc4NzUyMV5BMl5BanBnXkFtZTcwMTQyMTc5MQ@@._V1_.jpg";
	
	static String session_id = null;
	
	static boolean person_found = false;
	static double confidence_min = 65.0;

	static String test_group = "People_Test1";
	
	static int PHOTOS_PER_SET = 3;
	
	static boolean isDetected = false;
	
	static public Object LOCK = new Object();
	
	public static void test() throws InterruptedException, FaceppParseException, JSONException {
		 
		try {
			
			// Detect Motion
			new MotionDetector();

			waitMotion();
			
			if(Config.DEBUG)
				System.out.println("Detected");
			
			ImageFetcher.openCamera();
			//sendRequest();
			//addNewPerson("Arnold Swarz2", URL_ARNOLD, "Arnold2[at]gmail.com", "Famous People");
			
			//createPersonSet("Test_Person#0", "person1_email[at]gmail.com", test_group);
			trainDetector("People_Test1");
			
			while(!person_found)
				detectPerson(ImageFetcher.fetchImage(),test_group);
			
			ImageFetcher.closeCamera();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void waitMotion(){
		synchronized (LOCK) {
		    while (!isDetected) {
		        try { LOCK.wait(); }
		        catch (InterruptedException e) {
		            // treat interrupt as exit request
		            break;
		        }
		    }
		}
	}
	
	protected static void createPersonSet(String person_name, String person_email, String group) throws FaceppParseException, JSONException, IOException, InterruptedException{
		
		for(int i = 0; i < PHOTOS_PER_SET; i++)
			addNewPerson(person_name, ImageFetcher.fetchImage(), person_email, group);
		
	}
	
	protected static void sendRequest() throws FaceppParseException, JSONException, IOException{
		PostParameters postParameters =
			      new PostParameters()
			          .setUrl("http://faceplusplus.com/static/img/demo/20.jpg")
			          .setAttribute("all");
		httpRequests.request("detection", "detect", postParameters);
		JSONObject result = httpRequests.detectionDetect(postParameters);
		
		String face_id = result.getJSONArray("face").getJSONObject(0).getString("face_id");
		
		double face_confidence = result.getJSONArray("face").getJSONObject(0).getJSONObject("attribute")
	    .getJSONObject("gender").getDouble("confidence");
		
		System.out.println("Found face: " + face_id + " with confidence: " + face_confidence);
		postParameters.getMultiPart().writeTo(System.out);
		
	}
	
	protected static void addNewPerson(String person_name, String url_face, String person_tag, String group) throws FaceppParseException, JSONException, IOException{
		JSONObject result = httpRequests.detectionDetect(new PostParameters().setUrl(url_face));
		System.out.println(result);
		
		//person/create
		try{
			if(Config.DEBUG)
				System.out.println("\nperson/create");
			System.out.println(httpRequests.personCreate(new PostParameters().setPersonName(person_name)));
			
		}catch(Exception e){
			// Person already exists
			System.out.println("\n>person already exists.");
		}
		
		//person/add_face
		if(Config.DEBUG)
			System.out.println("\nperson/add_face");
		System.out.println(httpRequests.personAddFace(new PostParameters().setPersonName(person_name).setFaceId(result.getJSONArray("face").getJSONObject(0).getString("face_id"))));
		
		//person/set_info
		if(Config.DEBUG)
			System.out.println("\nperson/set_info");
		System.out.println(httpRequests.personSetInfo(new PostParameters().setPersonName(person_name).setTag(person_tag)));
	
		try{
			//group/create
			if(Config.DEBUG)
				System.out.println("\ngroup/create");
			System.out.println(httpRequests.groupCreate(new PostParameters().setGroupName(group)));
				
		}catch(Exception e){
			// Group already exists
			System.out.println("\n>group already exists.");
		}
		//group/add_person
		if(Config.DEBUG)
			System.out.println("\ngroup/add_person");
		System.out.println(httpRequests.groupAddPerson(new PostParameters().setGroupName(group).setPersonName(person_name)));
		
		//group/set_info
		if(Config.DEBUG)
			System.out.println("\ngroup/set_info");
		System.out.println(httpRequests.groupSetInfo(new PostParameters().setGroupName(group).setTag(group + " - tag")));

	}
	
	protected static void addNewPerson(String person_name, BufferedImage img_face, String person_tag, String group) throws FaceppParseException, JSONException, IOException{
		
		JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(Tools.getByteFromBufferedImage(img_face), person_name + ".jpg"));
		if(Config.DEBUG)
			System.out.println(result);
		
		//person/create
		try{
			if(Config.DEBUG)
				System.out.println("\nperson/create");
			System.out.println(httpRequests.personCreate(new PostParameters().setPersonName(person_name)));
			
		}catch(Exception e){
			// Person already exists
			if(Config.DEBUG)
				System.out.println("\n>person already exists.");
		}
		
		//person/add_face
		if(Config.DEBUG)
			System.out.println("\nperson/add_face");
		System.out.println(httpRequests.personAddFace(new PostParameters().setPersonName(person_name).setFaceId(result.getJSONArray("face").getJSONObject(0).getString("face_id"))));
		
		//person/set_info
		if(Config.DEBUG)
			System.out.println("\nperson/set_info");
		System.out.println(httpRequests.personSetInfo(new PostParameters().setPersonName(person_name).setTag(person_tag)));
	
		//group/create
		try{
			System.out.println("\ngroup/create");
			System.out.println(httpRequests.groupCreate(new PostParameters().setGroupName(group)));

		}catch(Exception e){
			// Group already exists
			if(Config.DEBUG)
				System.out.println("\n>group already exists.");
		}
		//group/add_person
		if(Config.DEBUG)
			System.out.println("\ngroup/add_person");
		System.out.println(httpRequests.groupAddPerson(new PostParameters().setGroupName(group).setPersonName(person_name)));
		
		//group/set_info
		if(Config.DEBUG)
			System.out.println("\ngroup/set_info");
		System.out.println(httpRequests.groupSetInfo(new PostParameters().setGroupName(group).setTag(group + " - tag")));

	}
	
	protected static void trainDetector(String group_name) throws FaceppParseException, JSONException{
		//recognition/train
		if(Config.DEBUG)
			System.out.println("\nrecognition/train");
		JSONObject result = httpRequests.trainIdentify(new PostParameters().setGroupName(group_name).setType("all"));
		
		if(Config.DEBUG)
			System.out.println(result);
		
		session_id = result.getString("session_id");
	}
	
	
	protected static void detectPerson(BufferedImage img_face, String group_name) throws FaceppParseException, IOException, JSONException{
				
		if(session_id == null)
			return;
		
		//recognition/recognize
		if(Config.DEBUG)
			System.out.println("\nrecognition/recognize");
		JSONObject result = httpRequests.recognitionIdentify(new PostParameters().setGroupName(group_name).setImg(Tools.getByteFromBufferedImage(img_face)).setSessionId(session_id));
		
		if(Config.DEBUG)
			System.out.println(result);

		for(int i = 0;  i < result.getJSONArray("face").length(); i++)
		{
			JSONObject person = result.getJSONArray("face").getJSONObject(0).getJSONArray("candidate").getJSONObject(i);
			if(person.getDouble("confidence") >= confidence_min)
			{
				if(Config.DEBUG)
					System.out.println("Found face: " + person.getString("person_name") + " and email: " + Tools.convertTagIntoEmail(person.getString("tag")));
				Tools.speakText("Welcome to the store: " + person.getString("person_name") + ".");
				person_found = true;
			}	
		}
		
		
	}
	
	

}
