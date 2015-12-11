package application;
import application.net.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import Audio.AudioCapture;
import Audio.AudioPlay;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.*;



public class Controller {
	AudioCapture audio = new AudioCapture();
	AudioPlay aPlay = new AudioPlay();
	Connection connect = new Connection();

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
	@FXML
	Shape square;
	@FXML
	Shape circle;


	@FXML
	void initialize() {
		tabPane.autosize();
		refresh();
		square.setVisible(false);
		square.setDisable(true);
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
	void record() throws InterruptedException {
		circle.setVisible(false);
		circle.setDisable(true);
		square.setDisable(false);
		square.setVisible(true);
		String name = getAudioName();
		audio.captureAudio(name);
		audio.setAudioFileName(name);

		refresh();
	}

	@FXML
	void stop() {

		square.setDisable(true);
		square.setVisible(false);
		circle.setVisible(true);
		circle.setDisable(false);
		audio.setStopCapture();
		audioName.clear();
		refresh();
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
	String getIPAddress() { //pulls a dialog box up that asks the user to enter IP Address and returns the entered string.
		String ipAddress = "";
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Share an Audio");
		dialog.setContentText("IP Address:");
		dialog.setHeaderText("Enter the required IP Address below");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			ipAddress  = result.get();
			return ipAddress;
		} else {
			return ipAddress;
		}


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
		if (ipAddress.equals("")) {
			return;
		} else if (!ipAddress.matches("(\\d+.){3}\\d+")) { //checks the IP Address pattern
			badNews();
			return;
		}
		String audioString = getEncodedText();
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

	@FXML
	void badNews() {
		Alert bad  = new Alert(Alert.AlertType.ERROR);
		bad.setContentText("Wrong IP Address!");
		bad.setHeaderText(null);
		bad.showAndWait();
	}


}


