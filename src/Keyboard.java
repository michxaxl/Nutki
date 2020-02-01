//import sun.applet.Main;

import com.sun.tools.javac.Main;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Keyboard {

    private JButton[] buttonsArray;
    private static String[] notesArray = {"C", "D", "E", "F", "G", "A", "H", "C#", "D#", "F#", "G#", "B"};
    private Map<String, Integer> pointsForNote = new HashMap<>();
    private int attempts = 3;
    private Settings settings;

    public Keyboard(JButton[] butArray) {
        settings = new Settings();
        attempts = settings.getAttempts();
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
        pointsForNote.put(note, points);
    }

    /* Dodaje 1 punkt */
    public void addPoint(String note)
    {
        try {
            Integer actualPoints = pointsForNote.get(note);
            pointsForNote.put(note, ++actualPoints);
            for(JButton but : buttonsArray){ // Pętla po buttonach
                if(note.equals(but.getName())){ // Jeżeli wylosowany dźwięk jest taki sam jak nazwa buttona
                    if(getPoints(note)==attempts){ // Jezeli x razy odgadnieto dzwiek
                        but.setText(note); // Pokazanie go na klawiaturze
                        but.addActionListener(new ActionListener() { //Dodaje listener do buttona zeby mogl wydawac dzwiek
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                playSound(note);
//                                System.out.println(attempts); //TEST
                            }
                        });
                    }

                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

//    void button(JButton button) {
//        button.addActionListener(new ActionListener() { // Nastepny
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        }
//    }

    public void playSound(String note) {
        new Thread(new Runnable() {
            public void run() {
                try {
//                    Thread.sleep(1500);
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            App.class.getResourceAsStream("/notes/" + note + ".wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }



}
