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
    private JButton repeatButton;
    private JButton restartButton;
    private JPanel keyboardPanel;
    private String pathToNotes;

    private String[] notesArray = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "B", "H"};
    private String drawnNote = "", drawnNoteTemp = "";
    private int points = 0;
    private Keyboard keyboard;
    private Settings settings;
    private boolean alreadyExecuted;
    private ActionListener checkListener;
    private float volume;
//    private int attempts = 3;

// Schemat: https://drive.google.com/file/d/1BHK_Ki221aOXI0TF1RiG8r_LD06wB4SK/view

    public App() throws IOException{
        pathToNotes = "/Notes/";
        keyboard = new Keyboard(buttonsArray);
        settings = new Settings();
        volume = settings.getVolume();

//        System.out.println(settings.getAttempts());
        alreadyExecuted = false;

        checkListener = new ActionListener() {
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
        };

        checkButton.addActionListener(checkListener); // Button
        noteField.addActionListener(checkListener); // Enter

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Ustawienia
                settings.show();
            }
        });

        repeatButton.addActionListener(new ActionListener() { // Powtórz
            @Override
            public void actionPerformed(ActionEvent e) {
                play(drawnNote);
            }
        });
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });
    }

    public synchronized void drawSound() {
        keyboard.setVolume(settings.getVolume());
        if(!keyboard.isFinished()) {
            Random rand = new Random();
            drawnNote = notesArray[rand.nextInt(12)];

            if (drawnNote.equals(drawnNoteTemp)) { // Jesli dzwiek sie powtorzyl - losuj jeszcze raz
                drawnNote = notesArray[rand.nextInt(12)];
            }

            drawnNoteTemp = drawnNote;
            checkButton.setEnabled(false);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        volume = settings.getVolume();
                        Thread.sleep(1500);
                        noteLabel.setText("Jaki to dźwięk?");
                        play(drawnNote);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            }).start();
        } else {
            endGame();
        }
    }

    public void play(String noteName){
        try {
            volume = settings.getVolume();
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream( App.class.getResourceAsStream(pathToNotes + noteName + ".wav") );
            clip.open(inputStream);
            /* Glosnosc */
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume); // Reduce volume by 10 decibels.
            clip.start();
            checkButton.setEnabled(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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

    public void restart() {
        points = 0;
        keyboard.restart();
        keyboard.setAttempts(settings.getAttempts());
        keyboard.setVolume(settings.getVolume());
        updatePoints();
        drawSound();
    }

    public void endGame() {
        JFrame endFrame = new JFrame("Koniec Gry!");
        endFrame.setSize(new Dimension(500, 300));
        endFrame.setResizable(false);
        JPanel panel = new JPanel();
        Font labelFont = pointsLabel.getFont();
        JLabel text = new JLabel("Koniec! Zebrane punkty: "+points);
        endFrame.add(panel);
        panel.add(text, BorderLayout.CENTER);

        endFrame.setVisible(true);
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
