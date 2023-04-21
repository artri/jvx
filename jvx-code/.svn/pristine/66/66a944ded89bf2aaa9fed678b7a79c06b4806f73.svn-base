package research;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public class TestTextField {

    public static void main(String[] args) {
        new TestTextField();
    }

    public TestTextField() {
        EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                try 
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                }

                JTextField field = new JTextField("Hello", 10);

                UIManager.put("TextField.inactiveBackground", new ColorUIResource(new Color(255, 0, 0)));

                field = new JTextField("Hello", 10);
                field.setEditable(false);

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new FlowLayout());
                frame.add(field);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

}