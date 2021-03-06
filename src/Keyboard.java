//import sun.applet.Main;

import com.sun.tools.javac.Main;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Keyboard {

    private JButton[] buttonsArray;
    private String[] notesArray = {"C", "D", "E", "F", "G", "A", "H", "C#", "D#", "F#", "G#", "B"};
    private Map<String, Integer> pointsForNote = new HashMap<>();
    private int attempts=3;
    private Settings settings;
    private float volume;

    public Keyboard(JButton[] butArray) {
        this.buttonsArray = butArray;
        int i = 0;
        for(JButton b : buttonsArray) {
            pointsForNote.put(notesArray[i], 0);
            b.setName(notesArray[i]);
            b.setOpaque(true);
            b.setBorderPainted(false);
            if(i<7) // Biale klawisze
                b.setPreferredSize(new Dimension(40, 100));
            else // Czarne klawisze
                b.setPreferredSize(new Dimension(20, 100));
            i++;
        }
    }

    /* Pobiera ilosc punktow dla danego dzwieku */
    public Integer getPoints(String note) {
        return pointsForNote.get(note);
    }

    /* Ustawia ilosc punktow points dla dzwieku note*/
    public void setPoints(String note, Integer points) {
        if(pointsForNote.containsKey(note))
            pointsForNote.put(note, points);
    }

    /* Dodaje 1 punkt */
    public void addPoint(String note)
    {
        try {
            Integer actualPoints = pointsForNote.get(note);
            if(actualPoints<attempts) {
                pointsForNote.put(note, ++actualPoints);
                unlockButton(note);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /* Odblokowuje przycisk klawiatury, który bedzie od tej pory odtwarzal dzwiek */
    public void unlockButton(String note){
        for (JButton but : buttonsArray) { // Pętla po buttonach
            if (note.equals(but.getName())) { // Jeżeli wylosowany dźwięk jest taki sam jak nazwa buttona
                if (pointsForNote.get(note) == attempts) { // Jezeli x razy odgadnieto dzwiek
                    but.setText(note); // Pokazanie go na klawiaturze
                    but.addActionListener(new ActionListener() { //Dodaje listener do buttona zeby mogl wydawac dzwiek
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            playSound(note);
                        }
                    });
                }
            }
        }
    }

    /* Odtwarza podany w parametrze dzwiek */
    public void playSound(String note) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            App.class.getResourceAsStream("/notes/" + note + ".wav"));
                    clip.open(inputStream);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(volume);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public boolean isFinished(){
        int counter = 0;
        for(int i = 0; i<notesArray.length; i++){
//               System.out.print(pointsForNote.get(notesArray[i])); //test
            if(pointsForNote.get(notesArray[i])==attempts)
                counter++;
        }
        if(counter==12)
            return true;
        else
            return false;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void restart() {
        for(int i = 0; i<notesArray.length; i++){
            pointsForNote.put(notesArray[i], 0);
        }
        for (JButton but : buttonsArray) {
            but.setText("");
        }

    }

}
