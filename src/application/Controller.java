package application;

import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import Audio.AudioCapture;
import Audio.AudioPlay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;


public class Controller {
	
	AudioCapture audio = new AudioCapture();
	AudioPlay aPlay = new AudioPlay();
	
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
	ListView<String> displayAudios;
	@FXML
	Text timerText;

	long startTimeNano;

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
		} catch (Exception e) {
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
		File[] files = new File(System.getProperty("user.dir") + "/src/audioFiles" ).listFiles();
		for (File file : files) {
		    if (file.isFile() && file.getName().equals(name)) {
		    	file.delete();
		    	displayAudios.getItems().remove(name);
		    	return;
		    }
		}
		
	}
	
	@FXML
	void refresh () {
		displayAudios.getItems().clear();
		File[] files = new File(System.getProperty("user.dir") + "/src/audioFiles" ).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 
		if (files.length != 0) {
			for (File file : files) {
			    if (file.isFile()) {
			        displayAudios.getItems().add(file.getName());
			    }
			}
		}
	}
	
	@FXML
	void IPAlert(){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Share an Audio");
		dialog.setContentText("IP Address:");
		dialog.setHeaderText("Enter the required IP Address below");
		dialog.showAndWait();
	
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

	@FXML
	private void trickUpdate() {
		tabPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (aPlay.isDone()) {
					playButtons();
				} else {
					pauseButtons();
				}
			}
		});
	}
}
