import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings {
    private JFrame frame;
    private JButton exitButton;
    private JPanel panel;
    private JLabel repeatLabel;
    private JSpinner attemptsSpinner;
    private JSlider volumeSlider;
    private int attempts = 3;


//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                }catch(Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public Settings(){

        frame = new JFrame("Settings");
        frame.setSize(new Dimension(500, 300));
        frame.setResizable(false);

        exitButton = new JButton("Zapisz i wyjdź");
        exitButton.setLayout(null);
        exitButton.setLocation(150, 200);

        repeatLabel = new JLabel("Ilosć wymaganych powtórzeń: ");

        SpinnerNumberModel attemptsSpinnerModel = new SpinnerNumberModel(attempts, 1.0, 9.0, 1.0);
        attemptsSpinner = new JSpinner(attemptsSpinnerModel);
        attemptsSpinner.setPreferredSize(new Dimension(50, 20));

//        volumeSlider = new JSlider();

        panel = new JPanel();
        frame.add(panel);

        panel.add(repeatLabel);
        panel.add(attemptsSpinner);
//        panel.add(volumeSlider);
        panel.add(exitButton);


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
    }

    public void show(){
        frame.setVisible(true);
    }

    public void hide(){
        frame.setVisible(false);
    }

    public int getAttempts() {
        //attempts = (int)attemptsSpinner.getValue();
        return attempts;
    }

    public void save() {

        System.out.println("Zapisano");

    }

}
