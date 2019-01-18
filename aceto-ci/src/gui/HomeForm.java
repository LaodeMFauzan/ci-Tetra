package gui;

import javax.swing.*;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 18/01/19
 */

public class HomeForm extends JFrame {
    private JPanel panel1;
    private JLabel title;
    private JButton btn_generate_tc;
    private JButton btn_input_file;
    private JList list1;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1 = new JPanel();
        title = new JLabel();
        btn_generate_tc = new JButton();
        btn_input_file = new JButton();
        list1 = new JList();

        panel1.add(title);
        panel1.add(btn_generate_tc);


        setTitle("Test Case Generator");
        setSize(400,400);
        setVisible(true);
    }

}
