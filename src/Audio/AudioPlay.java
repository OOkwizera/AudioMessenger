package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by reedmershon on 11/24/15.
 */
public class AudioPlay {

    public void playSound(String name) {
        try {
            File file = new File("/Users/reedmershon/Desktop/" + name + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
