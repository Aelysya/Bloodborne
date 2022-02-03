package bloodborne.sounds;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class SoundManager {

    private Clip sound;
    private String currentTheme;
    private float usualVolume = 0f;
    private boolean isSoundMuted;

    public SoundManager() {
        currentTheme = "nothing";
        try {
            sound = AudioSystem.getClip();
            sound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void setVolume(float volume, Clip clip) {
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume must be between 0f and 1f");
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public float getVolume(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void mute() {
        usualVolume = getVolume(sound);
        isSoundMuted = true;
        setVolume(0f, sound);
    }

    public void unMute() {
        isSoundMuted = false;
        setVolume(usualVolume, sound);
    }

    public void playSoundEffect(String fileName) {
        if (!isSoundMuted) {
            try {
                /*File audioFile;
                AudioInputStream audioIn;
                URL resource = SoundManager.class.getResource(fileName);
                assert resource != null;
                audioFile = Paths.get(resource.toURI()).toFile();
                audioIn = AudioSystem.getAudioInputStream(audioFile);*/
                byte[] audioData;
                try(InputStream inputStream = SoundManager.class.getResourceAsStream(fileName))  {
                    assert inputStream != null;
                    audioData = inputStream.readAllBytes();
                }
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioData));
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                setVolume(0.5f, clip);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public void setLoopingSound(String fileName) {
        if (!currentTheme.equals(fileName)) {
            try {
                currentTheme = fileName;
                /*File audioFile;
                AudioInputStream audioIn;
                URL resource = SoundManager.class.getResource("themes/" + fileName);
                assert resource != null;
                audioFile = Paths.get(resource.toURI()).toFile();
                audioIn = AudioSystem.getAudioInputStream(audioFile);*/
                byte[] audioData;
                try(InputStream inputStream = SoundManager.class.getResourceAsStream("themes/" + fileName))  {
                    assert inputStream != null;
                    audioData = inputStream.readAllBytes();
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioData));

                if (!sound.isRunning()) {
                    sound.stop();
                    sound.close();
                    sound.open(audioIn);
                    float clipVolume;
                    if (isSoundMuted) {
                        clipVolume = 0f;
                        usualVolume = 0.3f;
                    } else {
                        clipVolume = 0.3f;
                    }
                    setVolume(clipVolume, sound);
                    sound.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    Thread myThread = new Thread(() -> {
                        try {
                            while (0.001f < getVolume(sound)) {
                                float newVolume = getVolume(sound) / (1.5f);
                                setVolume(newVolume, sound);
                                Thread.sleep(500);
                            }
                            sound.stop();
                            sound.close();
                            sound.open(audioIn);
                            float clipVolume;
                            if (isSoundMuted) {
                                clipVolume = 0f;
                                usualVolume = 0.3f;
                            } else {
                                clipVolume = 0.3f;
                            }
                            setVolume(clipVolume, sound);
                            sound.loop(Clip.LOOP_CONTINUOUSLY);
                        } catch (LineUnavailableException | IOException | InterruptedException e) {
                            sound.stop();
                            sound.close();
                        }

                    });
                    myThread.start();
                }

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                sound.stop();
                sound.close();

                e.printStackTrace();
            }
        }
    }
}
