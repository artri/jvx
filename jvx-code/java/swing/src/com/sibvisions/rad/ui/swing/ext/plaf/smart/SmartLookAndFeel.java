/*
 * Copyright 2009 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 01.10.2008 - [JR] - creation
 * 17.10.2008 - [JR] - getBorderInsets implemented 
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.io.InputStream;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JInternalFrame.JDesktopIcon;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyleFactory;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.painter.SmartRootPaneBorder;
import com.sibvisions.util.type.ResourceUtil;

/**
 * The <code>SmartLookAndFeel</code> is the default look and feel
 * implementation for applications.
 * 
 * @author René Jahn
 */
public class SmartLookAndFeel extends SynthLookAndFeel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the border UI constant for root panes. */
	public static final String NAME_ROOTPANE_BORDER = "RootPane.frameBorder";
	
	/** true if the current JVM is 1.5 (5.0) .*/
	private static final boolean JAVA5 = ResourceUtil.getAccessibleProperty("java.version", "unknown").startsWith("1.5.");

	/** the style factory for the look and feel. */
	private SynthStyleFactory styleFactory;
	
	/** the original UI Defaults, before initialize Smart/LF. */
	private UIDefaults uidOrig;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SmartLookAndFeel</code> with defaults
	 * loaded from <code>/com/sibvisions/rad/ui/swing/ext/plaf/smart/smart.xml</code>.
	 */
	public SmartLookAndFeel()
	{
		InputStream is = null;
		
		try
		{
			is = getClass().getResourceAsStream("/com/sibvisions/rad/ui/swing/ext/plaf/smart/smart.xml");
			
			load(is, getClass());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (Exception e)
				{
					//nothing to be done
				}
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription()
	{
		return "Smart/LF is a synth based look and feel";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getID()
	{
		return "Smart/LF";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return "Smart/LF";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean getSupportsWindowDecorations() 
    {
        return true;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize()
	{
		super.initialize();

		styleFactory = new SmartStyleFactory(getStyleFactory());
		SynthLookAndFeel.setStyleFactory(styleFactory);
		
		SmartPopupFactory.install();

		//siehe SmartPainter.paintScrollPaneBorder
		UIDefaults uid = UIManager.getDefaults();

		uidOrig = new UIDefaults();
		uidOrig.putAll(uid);
		
		uid.put("Table.scrollPaneBorder", new BorderUIResource(BorderFactory.createLineBorder(SmartTheme.COL_VIEWPORT_BORDER)));
		
		//für ToolBarUI DragWindow nötig, damit dieses sauber repaintet!
		uid.put("ToolBar.dockingForeground", SmartTheme.COL_TOOLBAR_DOCKING);
		uid.put("ToolBar.floatingForeground", SmartTheme.COL_TOOLBAR_FLOATING);
		
		//wichtig wenn man SCROLL LAYOUT aktiviert hat, denn dann existiert ein Viewport
		uid.put("TabbedPane.tabAreaBackground", SmartTheme.COL_BACKGROUND);
		
        uid.put("ToolBarUI", SmartToolBarUI.class.getName());

        //Border über UIDefaults setzen, da kein Synth UI verwendet wird!
        if (getSupportsWindowDecorations())
        {
	        uid.put(NAME_ROOTPANE_BORDER, new BorderUIResource(new SmartRootPaneBorder()));
	        uid.put("RootPaneUI", SmartRootPaneUI.class.getName());
        
	        JFrame.setDefaultLookAndFeelDecorated(true);
	        JDialog.setDefaultLookAndFeelDecorated(true);
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uninitialize()
	{
		//Smart/LF Specifics rückgängig machen
		UIDefaults uid = UIManager.getDefaults();
        
        uid.clear();
        uid.putAll(uidOrig);
        
		SmartPopupFactory.uninstall();
		
		super.uninitialize();
		
        if (getSupportsWindowDecorations())
        {
	        JFrame.setDefaultLookAndFeelDecorated(false);
	        JDialog.setDefaultLookAndFeelDecorated(false);
        }

        uid.put("TableHeaderUI", "javax.swing.plaf.basic.BasicTableHeaderUI");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks if a state is set in a context.
	 * 
	 * @param pContext the context
	 * @param pState the desired state
	 * @param pValue the expected client property value
	 * @return <code>true</code> if the desired state is set, otherwise <code>false</code>
	 */
	public static final boolean isState(SynthContext pContext, int pState, Object pValue)
	{
		if (pValue != null)
		{
			Object oValue;
			
			switch (pState)
			{	
				case SynthConstants.MOUSE_OVER:
					
					oValue = pContext.getComponent().getClientProperty("Smart/LF.MOUSE_OVER");
					
					if (oValue != null)
					{
						return pValue.equals(oValue);
					}
					break;
					
				case SynthConstants.PRESSED:
					oValue = pContext.getComponent().getClientProperty("Smart/LF.PRESSED");
					
					if (oValue != null)
					{
						return pValue.equals(oValue);
					}
					break;
					
				default:
					//Versuchen den Component State zu verwenden!
					break;
			}
		}
		
		//funktioniert erst ab jdk 1.6 problemlos
		return (pContext.getComponentState() & pState) == pState;
	}
	
	/**
	 * Returns if the current JVM is 1.5 (5.0).
	 * 
	 * @return true if 1.5 VM is in use
	 */
	public static final boolean isJava5()
	{
		return JAVA5;
	}
	
	/**
	 * Returns the north pane of an internal frame title.
	 * 
	 * @param pFrame an internal frame 
	 * @return the title pane or <code>null</code> if the north pane was not found
	 */
	public static final BasicInternalFrameTitlePane getNorthPane(JInternalFrame pFrame)
	{
		Component comp;
		
		BasicInternalFrameTitlePane title = null;
		
		for (int i = 0, anz = pFrame.getComponentCount(); i < anz && title == null; i++)
		{
			comp = pFrame.getComponent(i);
			
			if ("InternalFrame.northPane".equals(comp.getName()))
			{
				title = (BasicInternalFrameTitlePane)comp;
			}
		}
		
		return title;
	}
	
	/**
	 * Checks if an object name identifies the menu button in the internal frame
	 * title pane.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the menu button
	 */
	public static final boolean isNorthPaneMenuButton(String pName)
	{
		return pName != null 
		       && ("InternalFrameTitlePane.menuButton".equals(pName)
				   || "InternalFrameTitlePane.menuIcon".equals(pName));
	}
	
	/**
	 * Checks if an object name identifies the close button in the internal frame
	 * title pane.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the close button
	 */
	public static final boolean isNorthPaneCloseButton(String pName)
	{
		return pName != null 
		       && ("InternalFrameTitlePane.closeButton".equals(pName)
		    	   || "InternalFrameTitlePane.closeIcon".equals(pName));
	}

	/**
	 * Checks if an object name identifies the minimize/iconify button in the internal frame
	 * title pane.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the minimize/iconify button
	 */
	public static final boolean isNorthPaneMinButton(String pName)
	{
		return pName != null 
		       && ("InternalFrameTitlePane.iconifyButton".equals(pName)
		    	   || "InternalFrameTitlePane.iconifyIcon".equals(pName));
	}

	/**
	 * Checks if an object name identifies the maximize button in the internal frame
	 * title pane.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the maximize button
	 */
	public static final boolean isNorthPaneMaxButton(String pName)
	{
		return pName != null 
		       && ("InternalFrameTitlePane.maximizeButton".equals(pName)
		    	   || "InternalFrameTitlePane.maximizeIcon".equals(pName)
		    	   || "InternalFrameTitlePane.minimizeIcon".equals(pName));
	}
	
	/**
	 * Checks if an object name identifies one of the window buttons in the internal frame
	 * title pane.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to one of the window buttons
	 */
	public static final boolean isNorthPaneWindowButton(String pName)
	{
		return pName != null 
			   && ("InternalFrameTitlePane.maximizeButton".equals(pName)
				   || "InternalFrameTitlePane.iconifyButton".equals(pName)
				   || "InternalFrameTitlePane.closeButton".equals(pName)
				   || "InternalFrameTitlePane.maximizeIcon".equals(pName)
				   || "InternalFrameTitlePane.minimizeIcon".equals(pName)
				   || "InternalFrameTitlePane.iconifyIcon".equals(pName)
				   || "InternalFrameTitlePane.closeIcon".equals(pName));
	}

	/**
	 * Checks if an object name identifies the close button in the title pane of a frame
	 * or a dialog.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the close button
	 */
	public static final boolean isWindowCloseButton(String pName)
	{
		return pName != null && "SmartRootTitlePane.closeButton".equals(pName);
	}
	
	/**
	 * Checks if an object name identifies the minimize button in the title pane of a frame
	 * or a dialog.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the minimize button
	 */
	public static final boolean isWindowMinButton(String pName)
	{
		return pName != null && "SmartRootTitlePane.iconifyButton".equals(pName);
	}

	/**
	 * Checks if an object name identifies the maximize button in the title pane of a frame
	 * or a dialog.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the maximize button
	 */
	public static final boolean isWindowMaxButton(String pName)
	{
		return pName != null && "SmartRootTitlePane.maximizeButton".equals(pName);
	}

	/**
	 * Checks if the orientation of a component is left to right.
	 * 
	 * @param pComponent the component
	 * @return <code>true</code> if the component orientation is left to right otherwise <code>false</code>
	 */
	public static final boolean isLeftToRightOrientation(Component pComponent)
	{
		if (pComponent == null)
		{
			return false;
		}
		
		return pComponent.getComponentOrientation().isLeftToRight();		
	}
	
	/**
	 * Searches the window and menu buttons of an internal frame title pane.
	 * 
	 * @param pContainer an instance of BasicInternalFrameTitlePane
	 * @return an array with the buttons [menu, iconify, max, close] or 
	 *         an arry with the size of 4 where all elements are <code>null</code>
	 */
	public static final AbstractButton[] getNorthPaneButtons(Container pContainer)
	{
		AbstractButton[] buttons = new AbstractButton[4];
		
		String sName;
		
		Component comp;
		
		for (int i = 0, anz = pContainer.getComponentCount(); i < anz; i++)
		{
			comp = pContainer.getComponent(i);
			
			sName = comp.getName();
			
			if ("InternalFrameTitlePane.closeButton".equals(sName))
			{
				buttons[3] = (AbstractButton)comp;
			}
			else if ("InternalFrameTitlePane.iconifyButton".equals(sName))
			{
				buttons[1] = (AbstractButton)comp;
			}
			else if ("InternalFrameTitlePane.maximizeButton".equals(sName))
			{
				buttons[2] = (AbstractButton)comp;
			}
			else if ("InternalFrameTitlePane.menuButton".equals(sName))
			{
				buttons[0] = (AbstractButton)comp;
			}
		}
		
		return buttons;
	}
	
	/**
	 * Gets the {@link JInternalFrame} instance from an {@link Object}. The
	 * supported object types are {@link SynthContext}, {@link JInternalFrame}
	 * or {@link JDesktopIcon}
	 * 
	 * @param pObject the object instance which holds the internal frame
	 * @return the internal frame or <code>null</code> if the frame can not be found
	 */
	public static final JInternalFrame getInternalFrame(Object pObject)
	{
		Container con;
		
		if (pObject instanceof SynthContext)
		{
			con = ((SynthContext)pObject).getComponent();
		}
		else if (pObject instanceof Container)
		{
			con = (Container)pObject;
		}
		else
		{
			return null;
		}
		
		if (con instanceof JInternalFrame)
		{
			return (JInternalFrame)con;
		}
		else if (con instanceof JDesktopIcon)
		{
			return ((JDesktopIcon)con).getInternalFrame();
		}
		else
		{
			con = con.getParent();
			
			if (con instanceof JInternalFrame)
			{
				return (JInternalFrame)con;				
			}
			else if (con instanceof JDesktopIcon)
			{
				return ((JDesktopIcon)con).getInternalFrame();
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the arrow button of a {@link JComboBox}.
	 * 
	 * @param pBox the combobox
	 * @return the {@link JButton} or null if it can not be found
	 */
	public static JButton getComboBoxArrowButton(JComboBox pBox)
	{
		Component comp;
		
		for (int i = 0, anz = pBox.getComponentCount(); i < anz; i++)
		{
			comp = pBox.getComponent(i);
			
			if (isComboBoxArrowButton(comp.getName()))
			{
				return (JButton)comp;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the text field of a {@link JComboBox}.
	 * 
	 * @param pBox the combobox
	 * @return the {@link JTextField} or null if it can not be found
	 */
	public static JTextField getComboBoxTextField(JComboBox pBox)
	{
		Component comp;
		
		for (int i = 0, anz = pBox.getComponentCount(); i < anz; i++)
		{
			comp = pBox.getComponent(i);
			
			if (isComboBoxTextField(comp.getName()))
			{
				return (JTextField)comp;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if an object name identifies the textfield of a combo box.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the text field
	 */
	public static boolean isComboBoxTextField(String pName)
	{
		return "ComboBox.textField".equals(pName);		
	}
	
	/**
	 * Checks if an object name identifies the arrow button of a combo box.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the button
	 */
	public static boolean isComboBoxArrowButton(String pName)
	{
		return "ComboBox.arrowButton".equals(pName);		
	}
	
	/**
	 * Checks if an object name identifies the 'previous' arrow button of a spinner.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the 'previous' button
	 */
	public static boolean isSpinnerPreviousButton(String pName)
	{
		return "Spinner.previousButton".equals(pName);
	}

	/**
	 * Checks if an object name identifies the 'next' arrow button of a spinner.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the 'next' button
	 */
	public static boolean isSpinnerNextButton(String pName)
	{
		return "Spinner.nextButton".equals(pName);
	}
	
	/**
	 * Checks if an object name identifies the formatted text field of a spinner.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to the formatted text field
	 */
	public static boolean isSpinnerFormattedTextField(String pName)
	{
		return "Spinner.formattedTextField".equals(pName);
	}
	
	/**
	 * Checks if an object name identifies a spinner component, like the next or previous button.
	 * 
	 * @param pName the object name
	 * @return <code>true</code> if the objectname is identical to a spinner component
	 */
	public static boolean isSpinnerComponent(String pName)
	{
		return pName != null && pName.startsWith("Spinner.");
	}
	
	/**
	 * Returns the insets of a container.
	 * 
	 * @param pContainer the container
	 * @return the insets of the container border, if it is a {@link JComponent} or the
	 *         insets of the container if it's not a {@link JComponent}
	 */
	public static Insets getBorderInsets(Container pContainer)
	{
		if (pContainer instanceof JComponent
			&& ((JComponent)pContainer).getBorder() != null)
		{
			return ((JComponent)pContainer).getBorder().getBorderInsets(((JComponent)pContainer));
		}
		
		return pContainer.getInsets();
	}
	
}	// SmartLookAndFeel
