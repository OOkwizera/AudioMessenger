package application;

import Audio.AudioCapture;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.*;



public class Controller {
	
	AudioCapture audio = new AudioCapture();
	
	@FXML
	Button record;
	@FXML
	Button stop;
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
		}
	}
	
	@FXML
	void no() {
		audio.endCapture();
	}
	
	String getAudioName() {
		return audioName.getText();
	}
}
