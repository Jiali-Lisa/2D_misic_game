import javax.sound.sampled.*;

public class Track extends Thread {
    private AudioInputStream stream;
    private Clip clip;




    public Track(String filePath) {
        try {
            stream = AudioSystem.getAudioInputStream(new java.io.File(filePath));
            clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class,stream.getFormat()));
            clip.open(stream);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    public void run(){
        try {
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}