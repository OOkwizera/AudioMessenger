package Audio;

import javax.sound.sampled.*;
import java.io.*;


public class AudioCapture {
	private File audioFile;
	private AudioFileFormat.Type fileType;
	private TargetDataLine line;

	
	public AudioCapture() {
		this.audioFile = new File(System.getProperty("user.dir") + "/src/AudioFiles/record.wav");
		this.fileType = AudioFileFormat.Type.WAVE;
		
	}
	
	public AudioFormat newFormat() {
		float sampleRate = 16000;
		int bitsInSample = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		
		AudioFormat audioFormat = new AudioFormat(sampleRate, bitsInSample, channels, signed, bigEndian);
		
		return audioFormat;
	}
	
	public void start() throws InterruptedException {
		try {
			AudioFormat audioFormat = newFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
			line.start();
			//we need a different thread to take care of writing the sound to a file.
			
			Thread thread = new Thread() 
			{
				@Override public void run() {
					AudioInputStream audioStream = new AudioInputStream (line);
					try { 
						AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
					} catch (IOException e) {e.printStackTrace();}
					
					System.out.println("Stopped recording");
				}
			};
			
			thread.start();
			Thread.sleep(15000);
			line.stop();
			line.close();
				
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void endCapture() {
		line.start();
		line.drain();
		line.close();
		System.out.println("Done recording.");
	}
	
	public void setAudioFileName(String name) {
	
		File newAudio = new File (audioFile.getParent() + "/src/AudioFiles/" + name + ".wav");
		audioFile.renameTo(newAudio);
	}
	
	
}

