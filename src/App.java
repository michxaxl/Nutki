import javax.swing.*;

public class App {
    private JPanel mainPanel;
    private JTextField textField1;
    private JButton checkButton;
    private JButton nextButton;


    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
