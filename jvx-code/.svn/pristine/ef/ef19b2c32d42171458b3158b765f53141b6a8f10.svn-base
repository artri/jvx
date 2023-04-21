package research;

import javax.rad.application.IContent;
import javax.rad.application.genui.Application;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.IContainer;
import javax.swing.JButton;
import javax.swing.JComponent;

import com.sibvisions.rad.ui.swing.impl.SwingApplication;

public class SimpleJVxApp extends Application
{
    public static void main(String[] args)
    {
        SwingApplication app = new SwingApplication();
        app.startup(SimpleJVxApp.class.getName(), null, null);
    }
    
    public SimpleJVxApp(UILauncher launcher)
    {
        super(launcher);
        
        UIButton butOK = new UIButton("OK");
        
        if (butOK.getResource() instanceof JComponent)
        {
            ((JButton)butOK.getResource()).setText("Okay");
        }
        
        setLayout(new UIBorderLayout());

        add(butOK, UIBorderLayout.SOUTH);
    }

    public <OP> IContent showMessage(OP pOpener, int pIconType, int pButtonType, String pMessage, 
                                     String pOkAction, String pCancelAction) throws Throwable
    {
        return null;
    }

    public IContainer getContentPane()
    {
        return this;
    }
}
