import sun.applet.Main;

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
    private Map<String, Integer> pointsForNote = new HashMap<>();

    public Keyboard(JButton[] butArray) {
        this.buttonsArray = butArray;
        pointsForNote.put("C", 0);
        pointsForNote.put("C#", 0);
        pointsForNote.put("D", 0);
        pointsForNote.put("D#", 0);
        pointsForNote.put("E", 0);
        pointsForNote.put("F", 0);
        pointsForNote.put("F#", 0);
        pointsForNote.put("G", 0);
        pointsForNote.put("G#", 0);
        pointsForNote.put("A", 0);
        pointsForNote.put("B", 0);
        pointsForNote.put("H", 0);
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
                    if(getPoints(note)==1){ // Jezeli 3 razy odgadnieto dzwiek
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
                            Main.class.getResourceAsStream("/notes/" + note + ".wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }



}
