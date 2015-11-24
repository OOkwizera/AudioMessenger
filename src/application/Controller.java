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
	void initialize() {
		tabPane.autosize();
	}
	
	
	@FXML
	void yes() {
		audio.start();
	}
	
	@FXML
	void no() {
		audio.endCapture();
	}
}
