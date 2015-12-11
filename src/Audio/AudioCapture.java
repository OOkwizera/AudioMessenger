package Audio;

import javax.sound.sampled.*;
import java.io.*;

public class AudioCapture {
	private File audioFile;
	private TargetDataLine line;
	private boolean stopCapture;
	private ByteArrayOutputStream byteArrayOutputStream;


	public AudioCapture() {
		this.stopCapture = true;

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

	public void captureAudio(String name) throws InterruptedException {
		
		Thread captureThread = new CaptureThread();
		captureThread.start();
		setAudioFileName(name);

	}

	public void setAudioFileName(String name) {
		if (name.equals("")) {
			name = "Unknown";
		}
		this.audioFile = new File(System.getProperty("user.dir") + "/src/AudioFiles/" + name + ".wav");

	}

	public boolean getStopCapture () {
		return stopCapture;
	}

	public void setStopCapture () {
		stopCapture = true;
	}

	private class CaptureThread extends Thread {

		byte tempBuffer[] = new byte[10000];


		public void run() {
			AudioFormat audioFormat = newFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
			try {
				line = (TargetDataLine) AudioSystem.getLine(info);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			try {
				line.open(audioFormat);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			line.start();
			byteArrayOutputStream = new ByteArrayOutputStream();
			stopCapture = false;
			AudioInputStream audioStream = new AudioInputStream(line);
			OutputStream outputstream = null;
			int frameSize = audioFormat.getFrameSize();
			int buffFrame = line.getBufferSize() / 8;
			int buffBytes = buffFrame * frameSize;
			byte[] data = new byte[buffBytes];

			try {
				outputstream = new FileOutputStream(audioFile);
				while (!stopCapture) {
					int numBytes = line.read(tempBuffer, 0, tempBuffer.length);
					if (numBytes > 0) {
						byteArrayOutputStream.write(tempBuffer, 0, numBytes);
					}
				}

				byteArrayOutputStream.close();
				byte audioBytes[] = byteArrayOutputStream.toByteArray();
				ByteArrayInputStream binput = new ByteArrayInputStream(audioBytes);
				audioStream = new AudioInputStream(binput, audioFormat, audioBytes.length / frameSize);
				AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
				line.close();
				line.stop();
				try {
					audioStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


		}		}

}


