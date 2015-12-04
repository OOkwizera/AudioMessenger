package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by reedmershon on 11/24/15.
 */
public class AudioPlay {

    private long clipTime;

    //random comment
    public void playSound(String name) {
        try {
            File file = new File(System.getProperty("user.dir") + "/src/AudioFiles/" + name );
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

    private void pauseSound(Clip clip) {
        clipTime = clip.getMicrosecondPosition();
        clip.stop();
    }

    private void resumeSound(Clip clip) {
        clip.setMicrosecondPosition(clipTime);
        clip.start();
    }
}
