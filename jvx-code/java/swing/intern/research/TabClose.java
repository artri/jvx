package research;

import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
 
 
 
public class TabClose
 
{
 
	public static void main(String[] args)
 
	{
 
		JTabbedPane pane = new JTabbedPane();
 
		pane.setUI(new TestPlaf());
 
		pane.add("Panel 1",new JLabel("Content of Panel 1"));
 
		pane.add("Panel 2",new JLabel("Content of Panel 2"));
 
		pane.add("Panel 3",new JLabel("Content of Panel 3"));
 
		pane.add("Panel 4",new JLabel("Content of Panel 4"));
 
 
 
		JFrame frame = new JFrame();
 
		frame.getContentPane().add(pane);
 
		frame.setSize(400,100);
 
		frame.setVisible(true);
 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
	}
 
 
 
	static class TestPlaf extends BasicTabbedPaneUI
 
	{
 
		//override to return our layoutmanager
 
		protected LayoutManager createLayoutManager()
 
		{
 
			return new TestPlafLayout();
 
		}
 
 
 
		//add 40 to the tab size to allow room for the close button and 8 to the height
 
		protected Insets getTabInsets(int tabPlacement,int tabIndex)
 
		{
 
			//note that the insets that are returned to us are not copies.
 
			Insets defaultInsets = (Insets)super.getTabInsets(tabPlacement,tabIndex).clone();
 
			defaultInsets.right += 40;
 
			defaultInsets.top += 4;
 
			defaultInsets.bottom += 4;
 
			return defaultInsets;
 
		}
 
 
 
		class TestPlafLayout extends TabbedPaneLayout
 
		{
 
			//a list of our close buttons
 
			java.util.ArrayList closeButtons = new java.util.ArrayList();
 
 
 
			public void layoutContainer(Container parent)
 
			{
 
				super.layoutContainer(parent);
 
				//ensure that there are at least as many close buttons as tabs
 
				while(tabPane.getTabCount() > closeButtons.size())
 
				{
 
					closeButtons.add(new CloseButton(closeButtons.size()));
 
				}
 
				Rectangle rect = new Rectangle();
 
				int i;
 
				for(i = 0; i < tabPane.getTabCount();i++)
 
				{
 
					rect = getTabBounds(i,rect);
 
					JButton closeButton = (JButton)closeButtons.get(i);
 
					//shift the close button 3 down from the top of the pane and 20 to the left
 
					closeButton.setLocation(rect.x+rect.width-20,rect.y+5);
 
					closeButton.setSize(15,15);
 
					tabPane.add(closeButton);
 
				}
 
 
 
				for(;i < closeButtons.size();i++)
 
				{
 
					//remove any extra close buttons
 
					tabPane.remove((JButton)closeButtons.get(i));
 
				}
 
			}
 
 
 
			// implement UIResource so that when we add this button to the 
 
			// tabbedpane, it doesn't try to make a tab for it!
 
			class CloseButton extends JButton implements javax.swing.plaf.UIResource
 
			{
 
				public CloseButton(int index)
 
				{
 
					super(new CloseButtonAction(index));
                                        setToolTipText("Close this tab");
 
 
 
					//remove the typical padding for the button
 
					setMargin(new Insets(0,0,0,0));
 
                                         addMouseListener(new MouseAdapter()
 
				         {
 
					       public void mouseEntered(MouseEvent e)
 
					       {
 
						     setForeground(new Color(255,0,0));
 
					        }
 
					        public void mouseExited(MouseEvent e)
 
					        {
 
						     setForeground(new Color(0,0,0));
 
					         }
 
				         });
 
				}
 
			}
 
 
 
			class CloseButtonAction extends AbstractAction
 
			{
 
				int index;
 
				public CloseButtonAction(int index)
 
				{
 
					super("x");
 
					this.index = index;
 
				}
 
 
 
				public void actionPerformed(ActionEvent e)
 
				{
 
					tabPane.remove(index);
 
				}
 
			}	// End of CloseButtonAction
 
		}	// End of TestPlafLayout
 
	}	// End of static class TestPlaf
 
}	// End of main class

