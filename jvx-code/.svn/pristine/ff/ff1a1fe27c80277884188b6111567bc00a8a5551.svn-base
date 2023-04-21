package research;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sibvisions.rad.ui.swing.ext.JVxUtil;

public class SimpleSplitButtonTest extends JFrame
{
    public static void main(String[] pArgs) throws Exception
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        SimpleSplitButtonTest spt = new SimpleSplitButtonTest();
        spt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        spt.setSize(new Dimension(300, 200));
        spt.setLocationRelativeTo(null);

        JMenuItem miDefault = new JMenuItem("Default");
        miDefault.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Default action pressed");
            }
        });
        
        JPopupMenu menu = new JPopupMenu();
        menu.add(new JMenuItem("First"));
        menu.add(miDefault);
        menu.add(new JMenuItem("Second"));
        
        SimpleSplitButton button = new SimpleSplitButton("Next");
        button.setPopupMenu(menu);
        button.setText("");
        button.setIcon(JVxUtil.getIcon("C:\\administrator_32.png"));
        button.setMargin(new Insets(2, 2, 2, 5));
        button.setFocusable(false);
        button.setDefaultAction(miDefault);
//        button.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                System.out.println("Action pressed");
//            }
//        });
        
        
        spt.setLayout(new FlowLayout());
        spt.add(button);

        SwingUtilities.updateComponentTreeUI(spt);
        
        spt.setVisible(true);
    }

}
