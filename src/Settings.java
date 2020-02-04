import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings {
    private JFrame frame;
    private JButton exitButton;
    private JPanel attemptsPanel, volumePanel, ExitPanel;
    private JLabel attemptsLabel, volumeLabel;
    private JSpinner attemptsSpinner;
    private JSlider volumeSlider;
    private float volume;
    private int attempts = 3;
    GridLayout layout;

    public Settings(){
//        attempts=3;
        layout =  new GridLayout(3,2);

        frame = new JFrame("Settings");
        frame.setSize(new Dimension(500, 300));
        frame.setResizable(false);

        exitButton = new JButton("Zapisz i wyjdź");

        attemptsLabel = new JLabel("Ilosć wymaganych powtórzeń: ");
        volumeLabel = new JLabel("Głosnosć: ");

        SpinnerNumberModel attemptsSpinnerModel = new SpinnerNumberModel(attempts, 1, 9, 1);
        attemptsSpinner = new JSpinner(attemptsSpinnerModel);
        attemptsSpinner.setPreferredSize(new Dimension(50, 20));

        volumeSlider = new JSlider(-60, 0);
        volumeSlider.setValue(0);

        attemptsPanel = new JPanel();
        attemptsPanel.setLayout(layout);
        frame.add(attemptsPanel);
//        attemptsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        attemptsPanel.add(attemptsLabel);
        attemptsPanel.add(attemptsSpinner);

        exitButton.setBounds(50,50,50,50);
        attemptsPanel.add(volumeLabel);
        attemptsPanel.add(volumeSlider);
        attemptsPanel.add(exitButton);


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
        return attempts;
    }

    public float getVolume(){
        return volume;
    }

    public void save() {
        volume = volumeSlider.getValue();
        attempts = (int)attemptsSpinner.getValue();
        hide();

    }

}
