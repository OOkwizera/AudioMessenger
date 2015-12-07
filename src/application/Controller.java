package application;

import java.io.File;

import Audio.AudioCapture;
import Audio.AudioPlay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.*;


public class Controller {
	
	AudioCapture audio = new AudioCapture();
	AudioPlay aPlay = new AudioPlay();
	
	@FXML
	Button record;
	@FXML
	Button stop;
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
	ListView<String> displayAudios;


	@FXML
	void initialize() {
		tabPane.autosize();
		refresh();
		pause.setVisible(false);
		pause.setDisable(true);
		pause1.setVisible(false);
		pause1.setDisable(true);
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
			aPlay.audioPlay(name);
			if (aPlay.isDone()) {
				playButtons();
			} else {
				pauseButtons();
			}
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

	@FXML
	private void delFile() {
		displayAudios.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.BACK_SPACE)) {
					int fileIdx = displayAudios.getSelectionModel().getSelectedIndex();
					displayAudios.getItems().remove(fileIdx);
				}
			}
		});
	}
}
