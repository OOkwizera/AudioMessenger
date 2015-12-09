package application;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import Audio.AudioCapture;
import Audio.AudioPlay;
import application.net.Connection;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Shape;



public class Controller {
	private int time;
	AudioCapture audio = new AudioCapture();
	AudioPlay aPlay = new AudioPlay();
	Connection connect = new Connection();
	
//	AnimationTimer animeTimer = new AnimationTimer() {
//		@Override
//		public void handle(long now) {
//
//        	long curTimeNano = System.nanoTime();
//        	if (curTimeNano > lastTimeFPS + 1000000000) {
//        		long seconds = (TimeUnit.SECONDS.convert(now - startTimeNano, TimeUnit.NANOSECONDS) % 60);
//        		long minutes = TimeUnit.MINUTES.convert(now - startTimeNano, TimeUnit.NANOSECONDS);
//        		timerText.setText(String.format("%02d:%02d", minutes, seconds));
//
//        		lastTimeFPS = curTimeNano;
//        	}
//        }
//        long lastTimeFPS = 0;
//	};


	@FXML
	Button record;
	@FXML
	Button stop;
	@FXML
	Button share;
	@FXML
	Shape play;
	@FXML
	Shape pause;
	@FXML
	Shape pause1;
	@FXML
	Button discard;
	@FXML
	TabPane tabPane;
	@FXML
	TextField audioName;
	@FXML
	TextField timeInterval;
	@FXML
	ListView<String> displayAudios;

	long startTimeNano;

	@FXML
	void initialize() {
		tabPane.autosize();
		refresh();
		pause.setVisible(false);
		pause.setDisable(true);
		pause1.setVisible(false);
		pause1.setDisable(true);
		new Thread(() -> {
			for (;;) {
				try {
					if (connect.hasMessage()) {
						String message = connect.retrieve();
						connect.Clear();
						String[] messageNames = message.split("\t");
						decode(messageNames[0], getFileNamePath(messageNames[1]));
						refresh();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
		
	}
	
	
	@FXML
	void yes() throws InterruptedException {
		if (!timeInterval.getText().matches("\\d+")) {
			time = 15;
			timeInterval.setText("15 seconds automatically");
		} else {
			time = Integer.parseInt(timeInterval.getText());
		}
		audio.start(time);
		if (!getAudioName().equals("")) {
			audio.setAudioFileName(getAudioName());
		} else {
			audio.setAudioFileName("Unknown");
		}
		//startTimeNano = System.nanoTime();
		//animeTimer.start();
		
		refresh();
	}
	
	@FXML
	void no() {
		//animeTimer.stop();
		audio.endCapture();
	}
	
	String getAudioName() {
		return audioName.getText();
	}

	@FXML
	void play() {
		String name = displayAudios.getSelectionModel().getSelectedItem();
		try {
			if (!name.equals("")) {
				pauseButtons();
				aPlay.audioPlay(name);
			}
		} catch (NullPointerException e) {
			System.out.println("Please select a file to play.");
		}
	}

	@FXML
	void pauseSound() {
		aPlay.pauseSound();
		playButtons();
	}

	void playButtons() {
		play.setDisable(false);
		play.setVisible(true);
		pause.setDisable(true);
		pause.setVisible(false);
		pause1.setDisable(true);
		pause1.setVisible(false);
	}

	void pauseButtons() {
		play.setDisable(true);
		play.setVisible(false);
		pause.setDisable(false);
		pause.setVisible(true);
		pause1.setDisable(false);
		pause1.setVisible(true);
	}
	
	@FXML
	void deleteAudio() {
		String name = displayAudios.getSelectionModel().getSelectedItem();
		File[] files = new File(System.getProperty("user.dir") + "/src/AudioFiles" ).listFiles();
		for (File file : files) {
		    if (file.isFile() && file.getName().equals(name)) {
		    	file.delete();
		    	displayAudios.getItems().remove(name);
		    	return;
		    }
		}
		
	}
	
	
	void refresh () {
		displayAudios.getItems().clear();
		File[] files = new File(System.getProperty("user.dir") + "/src/AudioFiles" ).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		for (File file : files) {
		    if (file.isFile()) {
		        displayAudios.getItems().add(file.getName());
		    }
		}
	}
	
	@FXML
	String getIPAddress() { //pulls a dialog box that asks the user to enter IP Address and returns the entered string.
		String ipAddress = "";
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Share an Audio");
		dialog.setContentText("IP Address:");
		dialog.setHeaderText("Enter the required IP Address below");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    ipAddress = result.get();
		}
		return ipAddress;
	
	}
	
	@FXML
	String getReceivedAudioName() { // pulls a pop box requesting the user to name the received audio file.
		String name = "";
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Receiving an AudioFile");
		dialog.setContentText("Rename the file:");
		dialog.setHeaderText("An audio file was sent to you" +"/n" + "You must give it a name!");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    name = result.get();
		}
		return name;
	}
	
	@FXML
	String getEncodedText() {  // returns a string that represents the selected audio file.
		String name = displayAudios.getSelectionModel().getSelectedItem();
		try {
			File audioFile = new File (getFileNamePath(name));
			byte[] bytes = FileUtils.readFileToByteArray(audioFile);
		    String encoded = Base64.encode(bytes);  
		    return encoded + "\t" + name;
		} catch (Exception e)  {
			e.printStackTrace();
		}
		return name;
	}
	
	String getFileNamePath(String fileName) { // takes a filename and returns the full filename path.
		return System.getProperty("user.dir") + "/src/AudioFiles/" + fileName;
	}
	
	public void decode (String str, String fileName) { //decodes a string to .wav file
		
		try {
			byte[] decoded = Base64.decode(str);
			FileOutputStream os = new FileOutputStream(fileName, true);
			os.write(decoded);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void shareAudio() { 
		String ipAddress = getIPAddress();
		System.out.println(ipAddress);
		String audioString = getEncodedText();
		System.out.println(audioString);
		connect.handShake(audioString, ipAddress);
	}
	

	@FXML
	private void delFile() {
		displayAudios.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.BACK_SPACE)) {
					deleteAudio();
				}
			}
		});
	}

}
