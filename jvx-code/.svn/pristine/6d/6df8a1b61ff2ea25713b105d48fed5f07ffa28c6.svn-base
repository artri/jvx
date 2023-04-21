package research;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Blocker extends EventQueue {
  private Component[] restrictedComponents;
  private Vector helperVector;
  private boolean inBlockedState = false;
  private EventQueue sysQ = 
      Toolkit.getDefaultToolkit().getSystemEventQueue();
  private boolean alreadyBlockedOnce = false;
  private static Blocker instance = null;
  public static synchronized Blocker Instance() {
    if( instance == null ) {
      instance = new Blocker();
    }
    return instance;
  }
  private Blocker() {
    restrictedComponents = null;
  }
  private void reset() {
    if( inBlockedState ) {
      setBlockingEnabled( false );     
    }
    restrictedComponents = null; 
  }
  public void setRestrictedComponents( Component[] restrictedComponents ) {
    reset();   // puts the Blocker into an unblocked state, and clears the 
               // restrictedComponents array (see private method below)
    helperVector = new Vector();
      // global Vector variable
    if( restrictedComponents != null ) {
      extractAllComponents( restrictedComponents );
    }
  
    // builds the blockedComponent array
    if( helperVector.size() >= 1 ) {
      this.restrictedComponents = new Component[ helperVector.size() ];
      for( int k = 0; k < helperVector.size(); k++ ) {
        this.restrictedComponents[k] = ( Component )helperVector.elementAt( k );
      }
    }
    else {
      this.restrictedComponents = null;
    } 
  }
  private void extractAllComponents( Component[] array ) {
    for( int i = 0; i < array.length; i++ ) {
      if( array[i] != null ) {
        helperVector.addElement( array[i] );
        if( ( ( Container )array[i] ).getComponentCount() != 0 ) {
          extractAllComponents( ( ( Container )array[i] ).getComponents() );
        }
      }
    } 
  }
  
  private void adjustFocusCapabilities( boolean blocked ) {
    if( blocked ) {
      for( int i = 0; i < restrictedComponents.length; i++ ) {
        if( restrictedComponents[i] instanceof JComponent ) {
          ( ( JComponent )restrictedComponents[i] ).setRequestFocusEnabled( false );
        }
        
        // removes the focus indicator from all components that are capable
        // of painting their focus
        if( restrictedComponents[i] instanceof AbstractButton ) {
          ( ( AbstractButton )restrictedComponents[i] ).setFocusPainted( false );
        }
      }  
    }
    else {
      for( int k = 0; k < restrictedComponents.length; k++ ) {
        if( restrictedComponents[k] instanceof JComponent ) {
          ( ( JComponent )restrictedComponents[k] ).setRequestFocusEnabled( true );
        }
        if( restrictedComponents[k] instanceof AbstractButton ) {
          ( ( AbstractButton )restrictedComponents[k] ).setFocusPainted( true );
        }
      }
    }
  }
  private Component getSource( AWTEvent event ) {
    Component source = null;
    // each of these five MouseEvents will still be valid (regardless
    // of their source), so we still want to process them.
    if( ( event instanceof MouseEvent )
        && ( event.getID() != MouseEvent.MOUSE_DRAGGED )
        && ( event.getID() != MouseEvent.MOUSE_ENTERED )
        && ( event.getID() != MouseEvent.MOUSE_EXITED )
        && ( event.getID() != MouseEvent.MOUSE_MOVED ) 
        && ( event.getID() != MouseEvent.MOUSE_RELEASED ) ) {
      MouseEvent mouseEvent = ( MouseEvent )event;
      source = SwingUtilities.getDeepestComponentAt(
                 mouseEvent.getComponent(),
                 mouseEvent.getX(),

                 mouseEvent.getY() );
    }
    else if( event instanceof KeyEvent 
               && event.getSource() instanceof Component ) {
      source = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    }
    return source;
  }
  private boolean isSourceBlocked( Component source ) {
    boolean blocked = false;
    if( ( restrictedComponents != null ) && ( source != null ) ) {
      int i = 0;
      while( i < restrictedComponents.length && 
             ( restrictedComponents[i].equals( source ) == false ) )
        i++;
    
      blocked = i < restrictedComponents.length; 
    }
    return blocked; 
  }
  protected void dispatchEvent( AWTEvent event ) {
    boolean blocked = false;
  
    if( inBlockedState ) {
      // getSource is a private helper method
      blocked = isSourceBlocked( getSource( event ) );
    }
    if( blocked && ( event.getID() == MouseEvent.MOUSE_CLICKED 
                     || event.getID() == MouseEvent.MOUSE_PRESSED ) ) {
      Toolkit.getDefaultToolkit().beep(); 
    }
 
    else if( blocked && event instanceof KeyEvent 
               && event.getSource() instanceof Component ) {

    	Component currentFocusOwner = getSource( event );
    
      boolean focusNotFound = true;
      do {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent( currentFocusOwner );

        currentFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

        if( currentFocusOwner instanceof JComponent ) {
        focusNotFound = 
            ( ( ( JComponent )currentFocusOwner ).isRequestFocusEnabled() == false );
        }
      } while( focusNotFound );
    }
    else {
      super.dispatchEvent( event );
    }
  }
  public void setBlockingEnabled( boolean block ) {
    // this methods must be called from the AWT thread to avoid
    // toggling between states while events are being processed
    if( block && !inBlockedState 
          && restrictedComponents != null ) {
    
      adjustFocusCapabilities( true );
      // "adjustFocusCapabilities" is a private helper function that
      // sets the focusEnabled & focusPainted flags for the 
      // appropriate components.  Its boolean parameter signifies 
      // whether we are going into a blocked or unblocked state
      // (true = blocked, false = unblocked)
    
      if( !alreadyBlockedOnce ) {
     
        // here is where we replace the SystemQueue
        sysQ.push( this );
        alreadyBlockedOnce = true;
      }
      inBlockedState = true;
    }
    else if( !block && inBlockedState ) {
      adjustFocusCapabilities( false );
      inBlockedState = false;
    }
  }
}

/**
 * This Test class demonstrates how the Blocker is used.
 * The Test opens a new JFrame object containing five different
 * components.  The button labeled "Block" will block "Button" 
 * and "CheckBox".  The button labeled "Unblock" will make "Button"
 * and "CheckBox" accessible to the user.  
 *
 * "Button" and "CheckBox" have no attached functionality.
 */
class TestBlocker {
  public static void main( String[] argv ) {
    JFrame frame = new JFrame();
    frame.setTitle( "Blocker Test" );
    frame.addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent e ) {
    System.exit(0);
      }
    } );
 
    frame.setSize( 200, 200 );
    JButton blockButton = new JButton( " Block  " );
    JButton unblockButton = new JButton( "Unblock " );
    JButton button = new JButton( " Button " );
    JCheckBox checkBox = new JCheckBox( "CheckBox" );
    JButton exitButton = new JButton( "  Exit  " );
    final Blocker blocker = Blocker.Instance();
    // this line sets "Button" and "CheckBox" as restricted
    // components...to change which components are restricted
    // simply call this method again, passing in a different
    // array of components.
    blocker.setRestrictedComponents( new Component[] {
        button, checkBox } );
    
    blockButton.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        blocker.setBlockingEnabled( true );
      }
    } );
    unblockButton.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        blocker.setBlockingEnabled( false );
      } 
    } );
    exitButton.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
        System.exit(0);
      } 
    } );
    Container contentPane = frame.getContentPane();
    contentPane.setLayout( new FlowLayout() );    
    contentPane.add( blockButton );
    contentPane.add( unblockButton );
    contentPane.add( button );
    contentPane.add( checkBox );   
    contentPane.add( exitButton );
    frame.setVisible( true );
  }
}
