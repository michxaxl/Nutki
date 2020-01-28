import sun.applet.Main;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class App {
    private JPanel mainPanel;
    private JTextField noteField;
    private JButton checkButton;
    private JButton nextButton;
    private JLabel noteLabel;
    private JButton buttonC, buttonD, buttonE, buttonF, buttonG, buttonA, buttonH;
    private JButton buttonCis, buttonDis, buttonFis, buttonGis, buttonB;
    private JButton[] buttonsArray = {buttonC, buttonD, buttonE, buttonF, buttonG, buttonA, buttonH, buttonCis, buttonDis, buttonFis, buttonGis, buttonB};
    private JLabel pointsLabel;
    private JPanel keyboardPanel;
    private static String pathToNotes;
    private static String[] notesArray = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "B", "H"};
    private static String drawnNote = "";
    private int points = 0;
    private Keyboard keyboard;



    public App() throws IOException{
        keyboard = new Keyboard(buttonsArray);

        buttonC.setName("C");
        buttonD.setName("D");
        buttonE.setName("E");
        buttonF.setName("F");
        buttonG.setName("G");
        buttonA.setName("A");
        buttonH.setName("H");
        buttonCis.setName("C#");
        buttonDis.setName("D#");
        buttonFis.setName("F#");
        buttonGis.setName("G#");
        buttonB.setName("B");

        pathToNotes = "/Notes/";
        nextButton.addActionListener(new ActionListener() { // Nastepny
            @Override
            public void actionPerformed(ActionEvent e) {
                drawSound();
                System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));
            }
        });

        checkButton.addActionListener(new ActionListener() { // Sprawdz
            @Override
            public void actionPerformed(ActionEvent e) {
                String typed = noteField.getText().toUpperCase();
                /* Jezeli wpisany dzwiek jest taki sam jak wylosowany */
                if(typed.equals(drawnNote)){
                    good();
                }else{
                    bad();
//                    for(String note : notesArray){
//                        if(typed.equals(note))
//                            bad();
//                        else
//                            bad();
////                            noteLabel.setText("Nie ma takiego dźwięku");
//                    }

                }
            }
        });
    }

    public synchronized void drawSound() {
        Random rand = new Random();
        drawnNote = notesArray[rand.nextInt(12)];

        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {

                    Thread.sleep(1500);
                    noteLabel.setText("Jaki to dźwięk?");
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Main.class.getResourceAsStream(pathToNotes + drawnNote + ".wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public void good() {
        keyboard.addPoint(drawnNote);

        pointsLabel.setForeground(new Color(122, 226, 58));
        noteLabel.setText("Dobrze!");
        noteField.setText(""); // Wyczyszczenie pola tekstowego
        points+=2;
        updatePoints();
        drawSound(); // losowanie kolejnego dźwięku
    }

    public void bad() {
        pointsLabel.setForeground(new Color(255, 103, 73));
        noteLabel.setText("Niestety, był to dźwięk: "+drawnNote);
        noteField.setText("");
        points--;
        updatePoints();
        drawSound();
    }

    public void updatePoints(){
        if(points<0){ points=0;}


//

        Font labelFont = pointsLabel.getFont();
        pointsLabel.setText("PUNKTY: "+points);

        pointsLabel.setFont(labelFont.deriveFont(labelFont.getStyle() | Font.BOLD)); //Ustawienie pogrubionej czcionki
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1300);
                    pointsLabel.setFont(labelFont.deriveFont(labelFont.getStyle() & ~Font.BOLD)); // Wyłączenie pogrubionej czcionki po 1300 milisekundach
                    pointsLabel.setForeground(Color.BLACK);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
