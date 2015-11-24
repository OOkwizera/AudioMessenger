package application;

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
	TabPane tabPane;
	@FXML
	TextField audioName;
	
	
	@FXML
	void initialize() {
		tabPane.autosize();
	}
	
	
	@FXML
	void yes() throws InterruptedException {
		audio.start();
		if (getAudioName() != "") {
			audio.setAudioFileName(getAudioName());
		} else {
			audio.setAudioFileName("Unknown");
		}
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
		aPlay.playSound(getAudioName());
	}
}
