import java.awt.image.BufferedImage;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;


public class FaceRecognition {
	
	private String API_KEY = "ab04a5c7375946e5a7d1e09c9197b379";
	private String API_SECRET_KEY = "2cFI85Ehg_DQbu_hVDIkkSY1B84QxLlO";
	
	private HttpRequests httpRequests = new HttpRequests(API_KEY, API_SECRET_KEY, true, false);
	
	private String session_id = null;
	
	private boolean person_found = false;
	private double confidence_min = 65.0;

	private String test_group = "People_Test1";
	private String main_group = "People";
	
	private int PHOTOS_PER_SET = 30;
	private int ATTEMPTS = 10;
	
	private String[] customer_data;	// {<name surname>, <email,phone>}
	
	public FaceRecognition() throws InterruptedException, FaceppParseException, JSONException, IOException {
		
		ImageFetcher.openCamera();
		boolean isTrained = false;
		
		while(!isTrained){
			try{
				if(Config.DEBUG)
					trainDetector(main_group);
				else
					trainDetector(main_group);
				
				isTrained = true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		while(!person_found && ATTEMPTS > 0  && Config.runRecognition){
			try{
				if(Config.DEBUG)
					customer_data = detectPerson(ImageFetcher.fetchImage(),main_group);
				else
					customer_data = detectPerson(ImageFetcher.fetchImage(),main_group);
			
			}
			catch(Exception e){
				e.printStackTrace();
			}

			ATTEMPTS--;
		}
		if(!Config.runRecognition)
		{
			System.out.println("Recognition stopped by the user");
			ImageFetcher.closeCamera();
			return;
		}

		// Person Found
		if(person_found && customer_data.length == 2 && Config.runRecognition)
		{
			if(Config.DEBUG)
				System.out.println("Found face: " + customer_data[0] + " and email: " + Tools.convertTagIntoEmail(customer_data[1]));

			GUI.setFaceRecognized(customer_data[0], customer_data[1]);
		}
		else if(person_found && customer_data.length != 2)
			new Throwable("This is not supposed to ever happen!");
		else if(!person_found)
		{
			GUI.setFaceRecognized(null, null);
		}
		
		ImageFetcher.closeCamera();
	}
	
	public FaceRecognition(boolean train) throws InterruptedException, FaceppParseException, JSONException, IOException {
	
		if(!train)
			new FaceRecognition();
		else
		{
			try {
				
				ImageFetcher.openCamera();
				
				createPersonSet("Alberto V.", "alberto[at]gmail.com-[p]4915733430313", main_group);
				
				ImageFetcher.closeCamera();
				System.exit(0);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void createPersonSet(String person_name, String person_email, String group) throws FaceppParseException, JSONException, IOException, InterruptedException{
		
		if(Config.DEBUG)
			System.out.println("Starting photo set");
		
		for(int i = 0; i < PHOTOS_PER_SET; i++)
			{
				try{
					if(Config.DEBUG)
						System.out.println("Photo: #" + (i + 1));
					addNewPerson(person_name, ImageFetcher.fetchImage(), person_email, group);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		
		if(Config.DEBUG)
			System.out.println("Photo set finished.");
	}
	
	protected void sendRequest() throws FaceppParseException, JSONException, IOException{
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
	
	protected void addNewPerson(String person_name, String url_face, String person_tag, String group) throws FaceppParseException, JSONException, IOException{
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
	
	protected void addNewPerson(String person_name, BufferedImage img_face, String person_tag, String group) throws FaceppParseException, JSONException, IOException{
		
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
	
	protected void trainDetector(String group_name) throws FaceppParseException, JSONException{
		//recognition/train
		if(Config.DEBUG)
			System.out.println("\nrecognition/train");
		JSONObject result = httpRequests.trainIdentify(new PostParameters().setGroupName(group_name).setType("all"));
		
		if(Config.DEBUG)
			System.out.println(result);
		
		session_id = result.getString("session_id");
	}
	
	
	protected String[] detectPerson(BufferedImage img_face, String group_name) throws FaceppParseException, IOException, JSONException{
				
		if(session_id == null)
			return new String[]{};
		
		//recognition/recognize
		if(Config.DEBUG)
			System.out.println("\nrecognition/recognize");
		
		// Checks if user continued to the next window
		if(!Config.runRecognition)
			return new String[]{};
		
		JSONObject result = httpRequests.recognitionIdentify(new PostParameters().setGroupName(group_name).setImg(Tools.getByteFromBufferedImage(img_face)).setSessionId(session_id));
		
		// Checks if user continued to the next window
		if(!Config.runRecognition)
			return new String[]{};
		
		if(Config.DEBUG)
			System.out.println(result);

		for(int i = 0;  i < result.getJSONArray("face").length(); i++)
		{
			// Checks if user continued to the next window
			if(!Config.runRecognition)
				return new String[]{};
			
			JSONObject person = result.getJSONArray("face").getJSONObject(0).getJSONArray("candidate").getJSONObject(i);
			if(person.getDouble("confidence") >= confidence_min)
			{
				person_found = true;
				return new String[]{person.getString("person_name"), Tools.convertTagIntoEmail(person.getString("tag"))};
				
			}	
		}
		
		return new String[]{};
		
		
	}
	
	

}
