package application;

import java.io.File;

import Audio.AudioCapture;
import Audio.AudioPlay;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.*;



public class Controller {
	
	AudioCapture audio = new AudioCapture();
	AudioPlay aPlay = new AudioPlay();
	
	@FXML
	Button record;
	@FXML
	Button stop;
	@FXML
	Button play;
	@FXML
	Button discard;
	@FXML
	TabPane tabPane;
	@FXML
	TextField audioName;
	@FXML
	ListView<String> displayAudios;
	
	
	@FXML
	void initialize() {
		
		tabPane.autosize();
		refresh();
	}
	
	
	@FXML
	void yes() throws InterruptedException {
		audio.start();
		if (!getAudioName().equals("")) {
			audio.setAudioFileName(getAudioName());
		} else {
			audio.setAudioFileName("Unknown");
		}
		refresh();
	}
	
	@FXML
	void no() {
		audio.endCapture();
	}
	
	String getAudioName() {
		return audioName.getText();
	}

	@FXML
	void play() {
		String name = displayAudios.getSelectionModel().getSelectedItem();
		if (!name.equals("")) {
			aPlay.playSound(name);
		}
		
	}
	
	@FXML
	void deleteAudio() {
		String name = displayAudios.getSelectionModel().getSelectedItem();
		File[] files = new File(System.getProperty("user.dir") + "/src/AudioFiles" ).listFiles();
		for (File file : files) {
		    if (file.isFile() && file.getName().equals(name)) {
		    	file.delete();
		    	return;
		    }
		}
		refresh();
	}
	
	@FXML
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
}
