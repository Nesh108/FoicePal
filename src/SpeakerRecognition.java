
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.bitsinharmony.recognito.MatchResult;
import com.bitsinharmony.recognito.Recognito;
import com.bitsinharmony.recognito.VoicePrint;

public class SpeakerRecognition {

	public ArrayList<VoicePrint> voicePrints;
	private Recognito<String> recognito = new Recognito<String>(44100.0f);
	private String outputFileName;
	private File outputFile;
	private int numberOfSamples; 
	private SoundRecorder recorder = new SoundRecorder();
	
	boolean isTraining = true;
	
	
	String[] voiceTrainingList = Speech.VOICE_TRAINING;
	int voiceTrainingSize = voiceTrainingList.length;
	
	public SpeakerRecognition() throws UnsupportedAudioFileException, IOException, InterruptedException{
		
		voicePrints = new ArrayList<VoicePrint>();
		init();
	}
	
	
	private void init() throws UnsupportedAudioFileException, IOException, InterruptedException{
		
		System.out.println("Speaker recognition started");
		
		while(isTraining && Config.runRecognition){
			
//			System.out.println("READ THE FOLLOWING OUT LOUD WHEN RECORDING STARTS.");
//			Thread.sleep(2000);
//			for(int i = 0; i < voiceTrainingSize; i++){
//				
//				System.out.println(voiceTrainingList[i]);
//				
//				
//				// TODO
//				// Record every recording one by one, name them "ID"+id+
//			}
			
			
		}
		
		if(!Config.runRecognition)
		{
			if(Config.DEBUG)
				System.out.println("Voice recognition stopped by the user");
			return;
		}
		
		importAudioSamples(new File("audio"));
		this.numberOfSamples = voicePrints.size();
		System.out.println("Audio samples imported: " + numberOfSamples + " samples");

		this.outputFileName ="audio/" + "audioSample" + numberOfSamples + ".wav";
	
		outputFile = new File(outputFileName);
		
		
		
		recorder.setOutputFile(outputFile);
		
		Thread stopper = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(8000);
				}catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				recorder.finish();
			}
		});

		stopper.start();

		// start recording
		recorder.start();
		
		
		
		List<MatchResult<String>> matches = recognito.identify(outputFile);
		System.out.println("Checking file name: " + outputFileName);
		int i = 0;
		MatchResult<String> match = matches.get(i);
		
		while(match.getKey().toString().equalsIgnoreCase(outputFileName)){
			System.out.println("It is same as itself!!!!!");
			match = matches.get(i+1);
			i++;
			}
		
		System.out.println("Audio sample count : " + matches.size());
		System.out.println("This is " + match.getKey() + "  " + match.getLikelihoodRatio() + "% positive about it...");
		
	}
	
	public void setOutputFileName(String filename){
		
		this.outputFileName = filename;
		
	}

	
	public void importAudioSamples(File folderPath) throws UnsupportedAudioFileException, IOException{
		
		String fileName;
		
		  File[] directoryListing = folderPath.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		    	fileName = child.getName();
		    	if(fileName.endsWith("wav"))
		    		this.voicePrints.add(recognito.createVoicePrint(child.getName(), child));
		    }
		  } else {
		    System.out.println("No such directory.");
		  }
	}
	
	
}
