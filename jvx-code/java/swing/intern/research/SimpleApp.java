package research;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;

import javax.rad.ui.IAlignmentConstants;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;

public class SimpleApp extends JFrame
{
    public static void main(String[] args) throws Exception
    {
    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	
        new SimpleApp();
    }
    
    protected SimpleApp()
    {
        JButton butOK = new JButton("OK");
        
        setLayout(new BorderLayout());
        add(butOK, BorderLayout.SOUTH);
        
        pack();
       
        Window winRecorder = new Window(this);
		winRecorder.setBackground(new Color(20, 20, 20, 120));
		
		JVxFormLayout layout = new JVxFormLayout();
		layout.setMargins(new Insets(5, 5, 5, 5));
		layout.setVerticalAlignment(IAlignmentConstants.ALIGN_CENTER);
		
        winRecorder.setLayout(layout);
        
		JButton button = new JButton();
		button.setName("REC_STARTSTOP");
		button.setIcon(JVxUtil.getIcon("FontAwesome.CIRCLE;size=18;color=183,0,0"));
		button.setFocusable(false);
		button.setPreferredSize(new Dimension(40, 40));
		button.setMargin(new Insets(0, 0, 0, 0));
//		button.setBackground(new Color(20, 20, 20, 0));
		button.setOpaque(false);

		winRecorder.add(button, layout.createConstraint(0, 0, 0, -1));

        winRecorder.setBounds(20, 100, 500, 50);
		winRecorder.setVisible(true);
		winRecorder.setAlwaysOnTop(true);
		
        setVisible(true);
    }
}
