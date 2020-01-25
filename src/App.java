import sun.applet.Main;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;

public class App {
    private JPanel mainPanel;
    private JTextField noteField;
    private JButton checkButton;
    private JButton nextButton;
    private JLabel noteLabel;
    private JButton buttonC;
    private JButton buttonD;
    private JButton buttonE;
    private JButton buttonF;
    private JButton buttonG;
    private JButton buttonA;
    private JButton buttonH;
    private JButton buttonCis;
    private JButton buttonDis;
    private JButton buttonFis;
    private JButton buttonGis;
    private JButton buttonB;
    private JLabel pointsLabel;
    private Keyboard keyboard;
    private static String pathToNotes;
    private static String[] notesArray = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "B", "H"};
    private static String drawnNote = "";


    public App() throws IOException{
//        keyboard.loadImage();

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
                if(noteField.getText().equals(drawnNote)){
                    good();
                }else{
                    bad();
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
        noteLabel.setText("Dobrze!");
        noteField.setText("");

        drawSound();
    }

    public void bad() {
        noteLabel.setText("Niestety, był to dźwięk: "+drawnNote);
        noteField.setText("");
        drawSound();
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
