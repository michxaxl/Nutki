//import sun.applet.Main;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;

public class App {
    private JPanel mainPanel;
    private JTextField noteField;
    private JButton checkButton;
    private JLabel noteLabel;
    private JButton buttonC, buttonD, buttonE, buttonF, buttonG, buttonA, buttonH;
    private JButton buttonCis, buttonDis, buttonFis, buttonGis, buttonB;
    private JButton[] buttonsArray = {buttonC, buttonD, buttonE, buttonF, buttonG, buttonA, buttonH, buttonCis, buttonDis, buttonFis, buttonGis, buttonB};
    private JLabel pointsLabel;
    private JButton settingsButton;
    private JLabel statusLabel;
    private JPanel keyboardPanel;
    private static String pathToNotes;
    private static String[] notesArray = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "B", "H"};
    private static String drawnNote = "";
    private int points = 0;
    private Keyboard keyboard;
    private Settings settings;
    private boolean alreadyExecuted;
//    private int attempts = 3;



    public App() throws IOException{
        pathToNotes = "/Notes/";
        keyboard = new Keyboard(buttonsArray);
        settings = new Settings();

        System.out.println(settings.getAttempts());

        alreadyExecuted = false;

        checkButton.addActionListener(new ActionListener() { // Sprawdz
            @Override
            public void actionPerformed(ActionEvent e) {
                String typed = noteField.getText().toUpperCase();
                if(alreadyExecuted){ // Jeżeli już zostało wywołane
                    if(typed.equals(drawnNote)){ // Jezeli wpisany dzwiek jest taki sam jak wylosowany
                        good();
                    }else {
                        bad();
                    }
                }else {
                    drawSound();
                    checkButton.setText("Sprawdź");
                    alreadyExecuted = true;
                }
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                settings.show();

            }
        });
    }

    public synchronized void drawSound() {
        Random rand = new Random();
        drawnNote = notesArray[rand.nextInt(12)];

        checkButton.setEnabled(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1500);
                    noteLabel.setText("Jaki to dźwięk?");
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream( App.class.getResourceAsStream(pathToNotes + drawnNote + ".wav") );
                    clip.open(inputStream);
                    /* Glosnosc */
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
                    clip.start();
                    checkButton.setEnabled(true);
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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
