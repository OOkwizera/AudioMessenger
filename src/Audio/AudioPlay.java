package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by reedmershon on 11/24/15.
 */
public class AudioPlay {

    private long clipTime;
    static long done = 0;
    private Clip clip;
    private int plays = 0;

    //random comment
    private void playSound(String name) throws LineUnavailableException {
        try {
            File file = new File(System.getProperty("user.dir") + "/src/AudioFiles/" + name );
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            clip = AudioSystem.getClip();
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

    public void audioPlay(String name) {
        try {
            if (plays == 0) {
                playSound(name);
                plays += 1;
            } else {
                if (!isDone()) {
                    resumeSound();
                } else {
                    clipTime = done;
                    plays -= 1;
                }

            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void pauseSound() {
        clipTime = clip.getMicrosecondPosition();
        clip.stop();
    }

    private void resumeSound() {
        clip.setMicrosecondPosition(clipTime);
        clip.start();
    }

    private boolean isDone() {
        long clipLen = clip.getMicrosecondLength();
        long clipPos = clip.getMicrosecondPosition();
        if (clipPos >= clipLen) {
            return true;
        } else {
            return false;
        }
    }
}
