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
 * 15.10.2008 - [JR] - getColor: consider the user-defined foreground of buttons
 *                   - installDefaults: set preferred size(height) of combo boxes
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolTip;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartTheme;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.event.ComboBoxPropertyChangeListener;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.event.InternalFramePropertyChangeListener;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.event.MenuMouseListener;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.event.ScrollBarMouseListener;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.event.TabbedPaneMouseListener;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.event.TitlePaneActionListener;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.painter.SmartComboPopupBorder;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.painter.SmartPainter;
import com.sibvisions.rad.ui.swing.ext.plaf.smart.painter.SmartTableHeaderRenderer;

/**
 * The <code>SmartStyle</code> extends the <code>SynthStyle</code> and forwards
 * all methods to a predefined <code>SynthStyle</code>, if the method can not
 * handle the request itself.
 * 
 * @author René Jahn
 */
public class SmartStyle extends SynthStyle
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the default style. */
	private SynthStyle synthStyle = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new <code>SmartStyle</code> with a predefined <code>SynthStyle</code>.
	 * 
	 * @param pStyle the predefined style
	 */
	public SmartStyle(SynthStyle pStyle)
	{
		synthStyle = pStyle;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(SynthContext pContext, Object pObject)
	{
		if ("InternalFrameTitlePane.titlePaneLayout".equals(pObject))
		{
			return new SmartInternalFrameTitlePaneLayoutManager();
		}
		else if ("Button.margin".equals(pObject) || "ToggleButton.margin".equals(pObject))
		{
			return SmartTheme.INS_BUTTON_MARGINS;
		}
		
		return synthStyle.get(pContext, pObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBoolean(SynthContext pContext, Object pObject, boolean pFlag)
	{
		return synthStyle.getBoolean(pContext, pObject, pFlag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColor(SynthContext pContext, ColorType pColorType)
	{
		JComponent comp = pContext.getComponent();

		
		if (comp instanceof JTabbedPane)
		{
			if (!pContext.getComponent().isEnabled() || SmartLookAndFeel.isState(pContext, SynthConstants.DISABLED, null))
			{
				return SmartTheme.COL_TAB_BORDER_DISABLED;
			}
		}
		else if (comp instanceof JButton || comp instanceof JToggleButton)
		{
			AbstractButton button = (AbstractButton)comp;
			
			if (button.isForegroundSet())
			{
				return button.getForeground();
			}
			else if (button.isEnabled())
			{
				return SmartTheme.COL_BUTTON_FOREGROUND_ENABLED;
			}
			else
			{
				return SmartTheme.COL_BUTTON_FOREGROUND_DISABLED;
			}
		}
		else if (comp instanceof BasicInternalFrameTitlePane)
		{
			if (SmartLookAndFeel.getInternalFrame(comp).isSelected())
			{
				return SmartTheme.COL_INTFRAME_TITLE_ACTIVE;
			}
			else
			{
				return SmartTheme.COL_INTFRAME_TITLE_INACTIVE;
			}
		}
		else if (comp instanceof JMenu || comp instanceof JMenuItem)
		{
			if (comp.isEnabled())
			{
				if (pContext.getRegion() == Region.MENU_ITEM_ACCELERATOR)
				{
					return SmartTheme.COL_MENU_ACCELERATOR;
				}
				
				return SmartTheme.COL_MENU_FOREGROUND;
			}
			else
			{
				return SmartTheme.COL_MENU_DISABLED;
			}
		}
		else if (comp instanceof JList)
		{
			if (pColorType == ColorType.TEXT_BACKGROUND)
			{
				return SmartTheme.COL_LIST_SELECTED_BACKGROUND;
			}
		}
		else if (comp.getName() != null && comp.getName().equals("ComboBox.renderer"))
		{
			if (pColorType == ColorType.TEXT_FOREGROUND)
			{
				Component parent = comp.getParent();
				
				while (parent != null && !(parent instanceof JComboBox))
				{
					parent = parent.getParent();
				}
		
				if (parent != null && !parent.isEnabled())
				{
					return SmartTheme.COL_TEXT_DISABLED_FOREGROUND;
				}
			}
		}
		
		return synthStyle.getColor(pContext, pColorType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColorForState(SynthContext pContext, ColorType pColorType)
	{
		return synthStyle.getColor(pContext, pColorType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Font getFont(SynthContext pContext)
	{
		Component comp = pContext.getComponent();
		
		//Accelerator gibt es nicht für JMenu, da bekommt man eine Exception vom JMenu.setAccelerator
		if (comp instanceof JMenuItem)
		{
			if (pContext.getRegion() == Region.MENU_ITEM_ACCELERATOR)
			{
				return SmartTheme.FONT_MENU_ACCELERATOR;
			}
		}
		
		return synthStyle.getFont(pContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Font getFontForState(SynthContext pContext)
	{
		return synthStyle.getFont(pContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SynthGraphicsUtils getGraphicsUtils(SynthContext pContext)
	{
		return synthStyle.getGraphicsUtils(pContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Icon getIcon(SynthContext pContext, Object pObject)
	{
		JComponent comp = pContext.getComponent();
		
		if (comp instanceof JMenu)
		{
			if ("Menu.arrowIcon".equals(pObject))
			{
				return SmartPainter.getMenuArrowIcon((JMenu)comp);
			}
		}
		else if (comp instanceof JRadioButtonMenuItem)
		{
			if ("RadioButtonMenuItem.checkIcon".equals(pObject))
			{
				return SmartPainter.getRadioButtonIcon(pContext);
			}
		}
		else if (comp instanceof JCheckBoxMenuItem)
		{
			if ("CheckBoxMenuItem.checkIcon".equals(pObject))
			{
				return SmartPainter.getCheckBoxIcon(pContext);
			}
		}
		else if (comp instanceof JCheckBox)
		{
			if ("CheckBox.icon".equals(pObject))
			{
				return SmartPainter.getCheckBoxIcon(pContext);
			}
		}
		else if (comp instanceof JRadioButton)
		{
			if ("RadioButton.icon".equals(pObject))
			{
				return SmartPainter.getRadioButtonIcon(pContext);
			}
		}
		
		return synthStyle.getIcon(pContext, pObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Insets getInsets(SynthContext pContext, Insets pInsets)
	{
		//kann durch SmartRootPaneBorder passieren!
		if (pContext != null)
		{
			JComponent comp = pContext.getComponent();
			
			if (comp instanceof JTextField)
			{
				String sName = comp.getName();
				
				//die Insets übernimmt in diesem Fall die ComboBox
				if (SmartLookAndFeel.isComboBoxTextField(sName))
				{
					return new Insets(0, 0, 0, 0);
				}
				else if (SmartLookAndFeel.isSpinnerFormattedTextField(sName))
				{
					return new Insets(0, 0, 0, 0);
				}
			}
		}
		
		return synthStyle.getInsets(pContext, pInsets);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(SynthContext pContext, Object pObject, int pInt)
	{
		JComponent comp = pContext.getComponent();
		
		
		if (comp instanceof JButton)
		{
			//verhindern, daß es wie ein Button aussieht -> Image bewegt sich nicht!
			if (SmartLookAndFeel.isNorthPaneMenuButton(comp.getName()) && ((String)pObject).endsWith("textShiftOffset"))
			{
				return pInt;
			}
		}
		else if ("TabbedPane.textIconGap".equals(pObject))
		{
			int iGap = synthStyle.getInt(pContext, pObject, pInt);
			
			if (UIManager.get(pObject) == null)
			{
				UIManager.put("TabbedPane.textIconGap", Integer.valueOf(iGap));
				
			}
			
			return iGap;
		}
		
		return synthStyle.getInt(pContext, pObject, pInt);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SynthPainter getPainter(SynthContext pContext)
	{
		return SmartPainter.getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(SynthContext pContext, Object pObject, String pString)
	{
		return synthStyle.getString(pContext, pObject, pString);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void installDefaults(SynthContext pContext)
	{
		synthStyle.installDefaults(pContext);

		Component comp = pContext.getComponent();

		
		if (comp instanceof JViewport)
		{
			comp.setBackground(SmartTheme.COL_VIEWPORT_BACKGROUND);
		}
		else if (comp instanceof JTable)
		{
			JTable table = (JTable)comp;
			
			table.setBackground(SmartTheme.COL_TABLE_BACKGROUND);
			table.setGridColor(SmartTheme.COL_TABLE_GRIDLINES);
			table.setSelectionBackground(SmartTheme.COL_TABLE_ROW_ACTIVE);
		}
		else if (comp instanceof JDesktopPane)
		{
			comp.setBackground(new ColorUIResource(SmartTheme.COL_DESKTOP_PANE_BACKGROUND));
		}
		else if (comp instanceof JTableHeader)
		{
        	((JTableHeader)comp).setDefaultRenderer(new SmartTableHeaderRenderer());
		}
		else if (comp instanceof JTextComponent)
		{
			JTextComponent text = (JTextComponent)comp;
			
			text.setDisabledTextColor(SmartTheme.COL_TEXT_DISABLED_FOREGROUND);
			text.setSelectionColor(SmartTheme.COL_TEXT_SELECTION);

			//kein opaque check möglich, weil die Komponente !opaque ist,
			//aber dennoch muss die Property gesetzt werden weils sonst nicht
			//funktioniert!

			//nötig, da ansonsten der Rahmen nicht transparent wäre!
			text.setOpaque(false);
		}
		else if (comp instanceof JInternalFrame)
		{
			JInternalFrame frame = (JInternalFrame)comp;
			
			if (!frame.isOpaque())
			{
				//sichern für uninstall
				frame.putClientProperty("Smart/LF.oldopaque", Boolean.FALSE);			
	
				//kann nicht über die XML Konfiguration gesetzt werden!
				//(MUSS opaque sein, weil sonst die Performance in die Knie geht!)
				frame.setOpaque(true);
			}
	
			if (!containsListener(comp.getPropertyChangeListeners("frameIcon"), InternalFramePropertyChangeListener.class))
			{
				Boolean bIconSet = (Boolean)frame.getClientProperty("Smart/LF.set_icon"); 
				EventListener[] lis = null;

				//installDefaults kann desöfteren aufgerufen werden, aber das Icon wird nur beim 
				//ersten mal gesetzt!!!
				if (bIconSet == null)
				{
					//Bei jdk 1.5 gibt es noch keinen Listener der sich um das frameIcon kümmert!
					if (!SmartLookAndFeel.isJava5())
					{
						//Die Listener müssen entfernt werden, da es bereits einen Listener gibt der das Frame-Icon
						//im Button tauscht
						lis = frame.getPropertyChangeListeners();
						
						for (int i = 0, anz = lis.length; i < anz; i++)
						{
							frame.removePropertyChangeListener((PropertyChangeListener)lis[i]);
						}
					}
					frame.setFrameIcon(JVxUtil.getIcon("/com/sibvisions/rad/ui/swing/ext/plaf/smart/images/iframe.png"));
					frame.putClientProperty("Smart/LF.set_icon", Boolean.TRUE);
	
					if (lis != null)
					{
						for (int i = 0, anz = lis.length; i < anz; i++)
						{
							frame.addPropertyChangeListener((PropertyChangeListener)lis[i]);
						}
					}
				}
				
				frame.addPropertyChangeListener("frameIcon", new InternalFramePropertyChangeListener());
			}
		}
		else if (comp instanceof JTabbedPane)
		{
			//Workaround für 1.5, da dort das Mouse-Event-Handling nicht passt!
			if (SmartLookAndFeel.isJava5())
			{
				if (!containsListener(comp.getMouseMotionListeners(), TabbedPaneMouseListener.class))
				{
					TabbedPaneMouseListener listener = new TabbedPaneMouseListener(); 
					
					comp.addMouseListener(listener);
					comp.addMouseMotionListener(listener);
				}
			}
		}
		else if (comp instanceof JScrollBar)
		{
			//In 1.6 wird nur MOUSE_OVER gesetzt (und er wird für die Darstellung der Track-Bereiche
			//sowieso benötigt)
			if (!containsListener(comp.getMouseListeners(), ScrollBarMouseListener.class))
			{
				ScrollBarMouseListener listener = new ScrollBarMouseListener(); 
				
				comp.addMouseListener(listener);
				comp.addMouseMotionListener(listener);
			}
		}
		else if (comp instanceof BasicInternalFrameTitlePane)
		{
			comp.setFont(SmartTheme.FONT_INTFRAME_TITLE);
		}
		else if (comp instanceof AbstractButton)
		{
			if (((JComponent)comp).isOpaque())
			{
				//sichern für uninstall
				((JComponent)comp).putClientProperty("Smart/LF.oldopaque", Boolean.TRUE);
				
				((JComponent)comp).setOpaque(false);
			}
			
			if (comp instanceof JButton)
			{
				//bei 1.5 werden die States im Buttonmodel nicht aktualisiert. Wenn das InternalFrame
				//minimiert und dann restored wird, bleibt der restore Button selected!
				if (SmartLookAndFeel.isJava5())
				{
					if (SmartLookAndFeel.isNorthPaneWindowButton(comp.getName()))
					{
						((JButton)comp).addActionListener(new TitlePaneActionListener());
					}
				}
			}
		}
		else if (comp instanceof JToolTip)
		{
			if (((JToolTip)comp).isOpaque())
			{
				//sichern für uninstall
				((JToolTip)comp).putClientProperty("Smart/LF.oldopaque", Boolean.TRUE);
				
				((JToolTip)comp).setOpaque(false);
			}
		}
		else if (comp instanceof JMenu)
		{
			if (!containsListener(comp.getMouseListeners(), MenuMouseListener.class))
			{
				MenuMouseListener listener = new MenuMouseListener(); 
				
				comp.addMouseListener(listener);
			}
		}
		else if (comp instanceof JComboBox)
		{
			JComponent jcomp = (JComponent)comp;
			
			if (jcomp.isOpaque())
			{
				//sichern für uninstall
				jcomp.putClientProperty("Smart/LF.oldopaque", Boolean.TRUE);
			
				//Funktioniert via smart.xml nicht!
				jcomp.setOpaque(false);
			}
			
			//Nur wenn das Layout noch nicht installiert wurde!
			if (jcomp.getClientProperty("Smart/LF.oldlayout") == null && !containsListener(jcomp.getPropertyChangeListeners(), ComboBoxPropertyChangeListener.class))
			{
				jcomp.addPropertyChangeListener(new ComboBoxPropertyChangeListener());
			}
		}
		else if (comp instanceof ComboPopup)
		{
			if (!(((JComponent)comp).getBorder() instanceof SmartComboPopupBorder))
			{
				((JComponent)comp).setBorder(new SmartComboPopupBorder());
				((JComponent)comp).putClientProperty("Smart/LF.oldborder", ((JComponent)comp).getBorder());
			}
		}
		else if (comp instanceof JSpinner)
		{
			JComponent jcomp = (JComponent)comp;
			
			if (jcomp.isOpaque())
			{
				//sichern für uninstall
				jcomp.putClientProperty("Smart/LF.oldopaque", Boolean.TRUE);
			
				//Funktioniert via smart.xml nicht!
				jcomp.setOpaque(false);
			}
			
			//Nur wenn das Layout noch nicht installiert wurde!
			if (jcomp.getClientProperty("Smart/LF.oldlayout") == null)
			{
				jcomp.setLayout(new SmartSpinnerLayoutManager());
			}
		}
		else if (comp instanceof JPanel)
		{
			//Default Panel color
			//als ColorUIResource, damit die Prüfung auf benutzerdefinierte Farben im SmartPainter (isUserDefinedBackground)
			//funktioniert und dennoch nicht zB die ToolBar in der Hintergrundfarbe erscheint!
			comp.setBackground(new ColorUIResource(SmartTheme.COL_BACKGROUND));
		}
		
		//Defaults
		if (comp.getFont() == null)
		{
			comp.setFont(SmartTheme.FONT_DEFAULT);
		}
		
		if (comp.getForeground() == null)
		{
			comp.setForeground(SmartTheme.COL_FOREGROUND_TEXT);
		}
		
		if ((comp instanceof JTextComponent
			 || comp instanceof JSpinner
			 || comp.isOpaque()) 
			&& comp.getBackground() == null)
		{
			comp.setBackground(SmartTheme.COL_BACKGROUND_TEXT);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpaque(SynthContext pContext)
	{
		return synthStyle.isOpaque(pContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uninstallDefaults(SynthContext pContext)
	{
		JComponent comp = pContext.getComponent();
		
		if (comp instanceof JTabbedPane)
		{
			removeMouseListener(comp, TabbedPaneMouseListener.class);
		}
		else if (comp instanceof JScrollBar)
		{
			removeMouseListener(comp, ScrollBarMouseListener.class);
		}
		else if (comp instanceof JTextComponent)
		{
			comp.setOpaque(true);
		}
		else if (comp instanceof JInternalFrame)
		{
			removePropertyChangeListener(comp, InternalFramePropertyChangeListener.class, "frameIcon");
		}
		else if (comp instanceof JButton)
		{
			//bei 1.5 werden die States im Buttonmodel nicht aktualisiert. Wenn das InternalFrame
			//minimiert und dann restored wird, bleibt der restore Button selected!
			if (SmartLookAndFeel.isJava5())
			{
				removeActionListener((JButton)comp, TitlePaneActionListener.class);
			}
		}
		else if (comp instanceof JMenu)
		{
			removeMouseListener(comp, MenuMouseListener.class);
		}
		else if (comp instanceof JComboBox)
		{
			removePropertyChangeListener(comp, ComboBoxPropertyChangeListener.class, null);
			
			LayoutManager layout = (LayoutManager)comp.getClientProperty("Smart/LF.oldlayout");
			
			if (layout != null)
			{
				((JComboBox)comp).setLayout(layout);
				comp.putClientProperty("Smart/LF.oldlayout", null);
			}
			
			comp.setPreferredSize(null);
		}
		else if (comp instanceof JSpinner)
		{
			LayoutManager layout = (LayoutManager)comp.getClientProperty("Smart/LF.oldlayout");
			
			if (layout != null)
			{
				((JSpinner)comp).setLayout(layout);
				comp.putClientProperty("Smart/LF.oldlayout", null);
			}
		}
		else if (comp instanceof ComboPopup)
		{
			Border borderOld = (Border)((JComponent)comp).getClientProperty("Smart/LF.oldborder");
			
			if (borderOld != null)
			{
				((JComponent)comp).setBorder(borderOld);
				((JComponent)comp).putClientProperty("Smart/LF.oldborder", null);
			}
		}
		
		Boolean bOldOpaque = (Boolean)comp.getClientProperty("Smart/LF.oldopaque"); 
		
		if (bOldOpaque != null)
		{
			comp.putClientProperty("Smart/LF.oldopaque", null);
			comp.setOpaque(bOldOpaque.booleanValue());
		}
		
		synthStyle.uninstallDefaults(pContext);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns if a listener list contains a desired listener class.
	 * 
	 * @param pListeners the list of mapped listeners to a component
	 * @param pListener the desired listener class
	 * @return <code>true</code> if the listener list contains a listener of the desired class,
	 *         <code>false</code> otherwise
	 */
	private boolean containsListener(EventListener[] pListeners, Class<?> pListener)
	{
		for (int i = 0, anz = pListeners.length; i < anz; i++)
		{
			if (pListeners[i].getClass().equals(pListener))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Removes mouse motion listeners from a component.
	 *  
	 * @param pComponent the component with the mouse motion listener(s)
	 * @param pListener the listener class which sould be removed
	 */
	private void removeMouseListener(JComponent pComponent, Class<?> pListener)
	{
		EventListener[] listeners = pComponent.getMouseListeners();
		
		for (int i = 0, anz = listeners.length; i < anz; i++)
		{
			if (listeners[i].getClass().equals(pListener))
			{
				pComponent.removeMouseListener((MouseListener)listeners[i]);
			}
		}
		
		listeners = pComponent.getMouseMotionListeners();
		
		for (int i = 0, anz = listeners.length; i < anz; i++)
		{
			if (listeners[i].getClass().equals(pListener))
			{
				pComponent.removeMouseMotionListener((MouseMotionListener)listeners[i]);
			}
		}
	}
	
	/**
	 * Removes a property change listener from a component.
	 * 
	 * @param pComponent the component with the property change listener(s)
	 * @param pListener the listener class which should be removed
	 * @param pProperty the property name on which the listener was registered or
	 *                  <code>null</code> if the registration was not for a special 
	 *                  property
	 */
	private void removePropertyChangeListener(JComponent pComponent, Class<?> pListener, String pProperty)
	{
		EventListener[] listeners;
		
		if (pProperty != null)
		{
			listeners = pComponent.getPropertyChangeListeners(pProperty);
		}
		else
		{
			listeners = pComponent.getPropertyChangeListeners();
		}
		
		for (int i = 0, anz = listeners.length; i < anz; i++)
		{
			if (listeners[i].getClass().equals(pListener))
			{
				if (pProperty != null)
				{
					pComponent.removePropertyChangeListener(pProperty, (PropertyChangeListener)listeners[i]);
				}
				else
				{
					pComponent.removePropertyChangeListener((PropertyChangeListener)listeners[i]);
				}
			}
		}
	}	

	/**
	 * Removes action listeners from a component.
	 *  
	 * @param pButton the component with the action listener
	 * @param pListener the listener class which sould be removed
	 */
	private void removeActionListener(AbstractButton pButton, Class<?> pListener)
	{
		EventListener[] listeners = pButton.getActionListeners();
		
		for (int i = 0, anz = listeners.length; i < anz; i++)
		{
			if (listeners[i].getClass().equals(pListener))
			{
				pButton.removeActionListener((ActionListener)listeners[i]);
			}
		}
	}
	
}	// SmartStyle
