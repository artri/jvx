package research;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MouseInputAdapter;

public class Modal  {

  static class ModalAdapter 
      extends InternalFrameAdapter {
    JComponent glass;
    JDesktopPane desktop;
    JInternalFrame ifrSelected;

    public ModalAdapter(JDesktopPane desktop, JComponent glass) {
    this.desktop = desktop;
      this.glass = glass;

      // Associate dummy mouse listeners
      // Otherwise mouse events pass through
      MouseInputAdapter adapter = new MouseInputAdapter()
      {
      };
      
      glass.addMouseListener(adapter);
      glass.addMouseMotionListener(adapter);
    }
boolean b = false;

	public void internalFrameOpened(InternalFrameEvent e)
	{
		System.out.println(e.getInternalFrame().getDesktopPane().getSelectedFrame());
		
		ifrSelected = e.getInternalFrame().getDesktopPane().getSelectedFrame();
	}

    public void internalFrameClosing(InternalFrameEvent e)
    {
    	if (!b)
    	{
    		b = true;
    	try
    	{
    		
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	desktop.remove(glass);
		desktop.repaint();
    	
    	b = false;
    	}
    }
    
    public void internalFrameClosed(InternalFrameEvent e) 
    {
    	System.out.println("EXIT");
//    	try
//    	{
//	    	Method m = e.getInternalFrame().getClass().getSuperclass().getSuperclass().getDeclaredMethod("stopLWModal");
//	    	m.setAccessible(true);
//	    	m.invoke(e.getInternalFrame());
//    	}
//    	catch (Exception ex)
//    	{
//    		ex.printStackTrace();
//    	}

//    	glass.remove(e.getInternalFrame());
//    	glass.setVisible(false);
    	
    	if (ifrSelected != null)
    	{
    		desktop.getDesktopManager().openFrame(ifrSelected);
    	}
    	
    }
  }

  public static void main(String args[]) {
    final JFrame frame = new JFrame("Modal Internal Frame");
    frame.setDefaultCloseOperation(
      JFrame.EXIT_ON_CLOSE);

    final JDesktopPane desktop = new JDesktopPane();
    final JDesktopPane desktopInner = new JDesktopPane();

    
    ActionListener showModal = 
        new ActionListener() {
      public void actionPerformed(ActionEvent e) 
      {
/*
        // Manually construct a message frame popup
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage("Hello, World");
        optionPane.setMessageType(
          JOptionPane.INFORMATION_MESSAGE);
        JInternalFrame modal = optionPane.
          createInternalFrame(desktop, "Modal");

        // create opaque glass pane
        JPanel glass = new JPanel();
        glass.setOpaque(false);

        // Attach modal behavior to frame
        modal.addInternalFrameListener(
          new ModalAdapter(glass));

        // Add modal internal frame to glass pane
        glass.add(modal);

        // Change glass pane to our panel
        frame.setGlassPane(glass);

        // Show glass pane, then modal dialog
        glass.setVisible(true);
        modal.setVisible(true);

        System.out.println("Returns immediately");
*/
		JLayeredPane glass = new JLayeredPane();
		
		glass.setOpaque(false);

//		JInternalFrame frGlass = new JInternalFrame("GLASS Frame");
//    	frGlass.setBounds(20, 20, 300, 200);
//		frGlass.setResizable(true);
//    	frGlass.setClosable(true);
//		frGlass.setModal(true);
    	
    	JComboBox cbox = new JComboBox(new String[] {"ASDB", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
    	cbox.setBounds(10, 40, 200, 20);
//    	frGlass.setLayout(null);
//    	frGlass.add(cbox);
//    	
//    	frGlass.addInternalFrameListener(new ModalAdapter(glass));
//    	
//    	desktop.add(frGlass);
//    	glass.add(frGlass);
//    	glass.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//    	desktop.getRootPane().setGlassPane(glass);
    	
//    	glass.setVisible(true);
//    	frGlass.setVisible(true);
    	
    	ModalInternalFrame ifr = new ModalInternalFrame("TESTE");
    	ifr.setResizable(true);
    	ifr.setClosable(true);
    	ifr.setLayout(null);
    	ifr.add(cbox);
    	ifr.setModal(true);
    	ifr.setBounds(20, 20, 300, 500);
//    	ifr.setVisible(true);
    	ifr.addInternalFrameListener(new ModalAdapter(desktop, glass));
    	
//    	cbox.putClientProperty("__force_heavy_weight_popup__", Boolean.TRUE);
//    	cbox.setLightWeightPopupEnabled(false);
    
    	JMenuBar menu = new JMenuBar();
    	
    	ifr.setJMenuBar(menu);
    	
    	JMenu menDatei = new JMenu("Datei");
    	menDatei.add(new JMenuItem("1"));
    	menDatei.add(new JMenuItem("1"));
    	menDatei.add(new JMenuItem("1"));
    	menDatei.add(new JMenuItem("1"));
    	menDatei.add(new JMenuItem("1"));
    	menDatei.add(new JMenuItem("1"));
    	menDatei.add(new JMenuItem("1"));
    	menDatei.add(new JMenuItem("1"));
    	
    	menu.add(menDatei);

//    	desktop.add(ifr, JLayeredPane.MODAL_LAYER);
    	glass.setBounds(0, 0, 1024, 768);
//    	desktop.add(glass, JLayeredPane.MODAL_LAYER);
    	desktop.add(ifr, JLayeredPane.MODAL_LAYER);
    	
    	desktopInner.setEnabled(false);
//    	glass.add(ifr);
//    	glass.setVisible(true);
    	
    	
    	ifr.setVisible(true);
    	
//    	try
//    	{
//	    	Method m = ifr.getClass().getSuperclass().getSuperclass().getDeclaredMethod("startLWModal");
//	    	m.setAccessible(true);
//	    	m.invoke(ifr);
//    	}
//    	catch (Exception ex)
//    	{
//    		ex.printStackTrace();
//    	}
    	
    	
    	
    	
    	System.out.println("Returns immediately");
      }
    };

    JInternalFrame internal = new JInternalFrame("Opener");
    desktopInner.add(internal);

    JButton button = new JButton("Open");
    button.addActionListener(showModal);

    Container iContent = internal.getContentPane();
    iContent.add(button, BorderLayout.CENTER);
    internal.setBounds(25, 25, 200, 100);
    internal.setVisible(true);

    JMenuBar mbMain = new JMenuBar();
    
	JMenu menMain = new JMenu("Datei");
	menMain.add(new JMenuItem("1"));
	menMain.add(new JMenuItem("1"));
	menMain.add(new JMenuItem("1"));
	menMain.add(new JMenuItem("1"));
	menMain.add(new JMenuItem("1"));
	menMain.add(new JMenuItem("1"));
	menMain.add(new JMenuItem("1"));
	
	JMenuItem miExit = new JMenuItem("Exit");
	miExit.setAccelerator(KeyStroke.getKeyStroke('E'));
	miExit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent pEvent)
		{
			System.out.println("EXIT");
		}
	});
	menMain.add(miExit);

	
	mbMain.add(menMain);

	JRootPane root = new JRootPane();
	root.setOpaque(true);
	root.setBounds(0, 0, 1024, 768);

	root.setJMenuBar(mbMain);

	desktop.add(root);
    frame.getContentPane().add(desktop, BorderLayout.CENTER);
    
    root.getContentPane().add(desktopInner);
    
    frame.setSize(500, 700);
    frame.setVisible(true);
  }
}

class ModalInternalFrame extends JInternalFrame
{
	public ModalInternalFrame(String pTitle)
	{
		super(pTitle);
	}
	
	// indica si aquest es modal o no.
    boolean modal = false;
 
    @Override
    public void show() {
        super.show();
        if (this.modal) {
            startModal();
        }
    }
 
    @Override
    public void setVisible(boolean value) {
        super.setVisible(value);
        if (modal) {
            if (value) {
                startModal();
            } else {
                stopModal();
            }
        }
    }
 
    private synchronized void startModal() {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                EventQueue theQueue =
                        getToolkit().getSystemEventQueue();
                while (isVisible()) {
                	
                    AWTEvent event = theQueue.getNextEvent();
                    Object source = event.getSource();
                    boolean dispatch = true;
 
                    if (event instanceof MouseEvent) {
                        MouseEvent e = (MouseEvent) event;
                        MouseEvent m =
                                SwingUtilities.convertMouseEvent((Component) e.getSource(), e, this);
                        
//                        System.out.println(source);
                        
                        if (!e.getSource().getClass().getName().startsWith("javax.swing.PopupFactory") &&
                        	    !this.contains(m.getPoint()) && e.getID() != MouseEvent.MOUSE_DRAGGED && e.getID() != MouseEvent.MOUSE_RELEASED) {
//                        	    dispatch = false;
                       	}

//                        if (!this.contains(m.getPoint()) && e.getID() != MouseEvent.MOUSE_DRAGGED) {
//                            dispatch = false;
//                        }
                    }
 
                    if (dispatch) {
                        if (event instanceof ActiveEvent) {
                            ((ActiveEvent) event).dispatch();
                        } else if (source instanceof Component) {
                            ((Component) source).dispatchEvent(
                                    event);
                        } else if (source instanceof MenuComponent) {
                            ((MenuComponent) source).dispatchEvent(
                                    event);
                        } else {
                            System.err.println(
                                    "Unable to dispatch: " + event);
                        }
                    }
                }
            } else {
                while (isVisible()) {
                    wait();
                }
            }
        } catch (InterruptedException ignored) {
        }

    }
 
    private synchronized void stopModal() {
        notifyAll();
    }
 
    public void setModal(boolean modal) {
        this.modal = modal;
    }
 
    public boolean isModal() {
        return this.modal;
    }
    
}

