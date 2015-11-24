package Audio;

import javax.sound.sampled.*;
import java.io.*;


public class AudioCapture {
	private File audioFile;
	private AudioFileFormat.Type fileType;
	private TargetDataLine line;

	
	public AudioCapture() {
		this.audioFile = new File("/Users/kvgarimella/Desktop/audioFile.wav");
		this.fileType = AudioFileFormat.Type.WAVE;
		this.line = line;
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
	
	public void start() {
		try {
			AudioFormat audioFormat = newFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
			line.start();
			AudioInputStream audioInputStream = new AudioInputStream(line);
			AudioSystem.write(audioInputStream, fileType, audioFile);	
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void endCapture() {
		line.start();
		line.drain();
		line.close();
		System.out.println("Done recording.");
	}
	
	
	
	
}

