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
 * 01.10.2008 - [HM] - creation
 * 31.03.2011 - [JR] - #161: ITranslatable implemented
 * 25.08.2011 - [JR] - #465: install actions
 * 07.11.2018 - [JR] - setTextInputEnabled implemented
 * 26.06.2019 - [JR] - #2034: workaround for empty border
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.ParseException;

import javax.rad.model.ui.ITranslatable;
import javax.rad.util.TranslationMap;
import javax.swing.ActionMap;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;

import com.sibvisions.rad.ui.swing.ext.celleditor.PromptSupport;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * <code>JVxComboBase</code> is a generic component for displaying 
 * any editor component combined with any popup component. The combo 
 * base has always an editor component, to copy the text out of it.
 * 
 * @author Martin Handsteiner
 */
public class JVxComboBase extends JComboBox 
						  implements PopupMenuListener, 
						             MouseListener, 
						             KeyListener,
						             WindowListener,
						             ITranslatable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The Layout policy to findout, which component should get the focus. */
	private static final LayoutFocusTraversalPolicy LAYOUT_POLICY = new LayoutFocusTraversalPolicy();

	/** the internal popup menu. */
	private JVxPopupMenu internalPopup;
	
	/** the internal popup menu. */
	private boolean ignorePopupMenuEvents = false;
	
	/** the internal popup menu. */
	private JButton internalButton;
	
	/** the windowAncestor of this component. */
	private Window windowAncestor = null;

    /** the popup component. */
	private JComponent popupComponent = null;

	/** the editor component. */
	private JTextComponent editorComponent = null;

	/** the popup size. */
	private Dimension popupSize = null;

	/** The translation mapping. */
	private TranslationMap translation = null;
	
    /** selectedItem. */
    private Object selectedItem = null;

    /** Determains if the popup was just closed, to prevent it to be opened again immediatly. */
	private boolean justClosed = false;

	/** Determains if the popup is canceled. */
	private boolean popupCanceled = false;

	/** Forces the Focus on the popup. */
	private boolean forceFocusOnPopup = false;

    /** Determines wether the windowDeactivated is permanent. */
    private boolean isPermanentDeactivated;
        
    /** true if the button should be enabled. */
    private boolean buttonEnabled = true;
    
    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
    
    /** wheter the LaF is a Mac LaF. */
    private boolean bMacLaF;

    /** whethert the editor is editable. */
    private boolean bEditable = true;
    
    /** whether text input is enabled. */
    private boolean bTextInputEnabled = true;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>JVxComboBase</code> with a default editor.
	 */
	public JVxComboBase()
	{
		setEditor(new GenericComboBoxEditor());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// MOUSELISTENER
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(MouseEvent pMouseEvent)
	{
		Object source = pMouseEvent.getSource();
		if (source == editorComponent)
		{
			if (!justClosed && !isEditorEditable() && !isPopupVisible())
			{
				setPopupVisible(true);
			}
		}
		else if (internalButton == null && source == this && pMouseEvent.getX() > editorComponent.getX() + editorComponent.getWidth())
		{
			setPopupVisible(!isPopupVisible());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(MouseEvent pMouseEvent)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent pMouseEvent)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent pMouseEvent)
	{
		Object source = pMouseEvent.getSource();
		if (source == internalButton)
		{
			if (!isPopupVisible())
			{
	         	Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
	         	if (focusOwner != null && focusOwner != editorComponent)
	         	{
	    			KeyboardFocusManager.getCurrentKeyboardFocusManager().
	    				dispatchEvent(new FocusEvent(focusOwner, FocusEvent.FOCUS_LOST));
	         	}
	         	if (editorComponent.isFocusable())
				{
					editorComponent.requestFocus();
	         	}
	         	else
	         	{
	       			requestFocus();
	         	}
			}
			setPopupVisible(!isPopupVisible());
		}
		else if (source == editorComponent)
		{
			justClosed = false;
			if (!isEditorEditable() && isPopupVisible())
			{
				setPopupVisible(false);
				justClosed = true;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent pMouseEvent)
	{
	}

	// KeyListener

	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(KeyEvent pKeyEvent)
	{
		if (!pKeyEvent.isConsumed() && pKeyEvent.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (!isPopupVisible())
			{
				pKeyEvent.consume();
				setPopupVisible(true);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(KeyEvent pKeyEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(KeyEvent pKeyEvent)
	{
	}

	// WINDOWLISTENER
	
	/**
	 * {@inheritDoc}
	 */
	public void windowActivated(WindowEvent pWindowEvent)
	{
		isPermanentDeactivated = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowClosed(WindowEvent pWindowEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowClosing(WindowEvent pWindowEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowDeactivated(WindowEvent pWindowEvent)
	{
		isPermanentDeactivated = true;
        new Thread(
            	new Runnable()
            	{
            	  	public void run()
            	  	{
            	  		try
            	  		{
	            	  		Thread.sleep(50);
	            	  		if (isPermanentDeactivated)
	            	  		{
		            	  		SwingUtilities.invokeLater(
		            	            	new Runnable()
		            	            	{
		            	            	  	public void run()
		            	            	  	{
		            	                        internalPopup.putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.TRUE);
		            	                        MenuSelectionManager.defaultManager().clearSelectedPath();
		            	            	    }
		            	            	});
	            	  		}
            	  		}
            	  		catch (InterruptedException ex)
            	  		{
            	  			// not possible
            	  		}
            	    }
            	}).start();
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowDeiconified(WindowEvent pWindowEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowIconified(WindowEvent pWindowEvent)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowOpened(WindowEvent pWindowEvent)
	{
	}

	// FOCUSLISTENER
	
	/**
	 * {@inheritDoc}
	 */
/*	public void focusGained(FocusEvent pFocusEvent)
	{
	}*/
	
	/**
	 * {@inheritDoc}
	 */
/*	public void focusLost(FocusEvent pFocusEvent)
	{
		if (!pFocusEvent.isTemporary() && !isPopupFocusEvent(pFocusEvent))
		{
			setPopupVisible(false);
			setSelectedItem(getEditor().getItem());
			pFocusEvent.getOppositeComponent().requestFocus();
		}
	}*/

    /**
  	 * {@inheritDoc}
  	 */
    public void actionPerformed(ActionEvent pActionEvent) 
    {
    	AWTEvent awtEvent = EventQueue.getCurrentEvent();
    	if (!(awtEvent instanceof FocusEvent) || !isPopupFocusEvent((FocusEvent)awtEvent))
    	{
    		setPopupVisible(false);
        	setSelectedItem(getEditor().getItem());
    	}
    }

	// POPUPMENULISTENER
	
	/**
	 * {@inheritDoc}
	 */
	public void popupMenuWillBecomeVisible(PopupMenuEvent pPopupMenuEvent)
	{
		if (!ignorePopupMenuEvents)
		{
			popupCanceled = false;
			firePopupMenuWillBecomeVisible();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void popupMenuWillBecomeInvisible(PopupMenuEvent pPopupMenuEvent)
	{
		if (windowAncestor != null)
		{
			windowAncestor.removeWindowListener(this);
		}
		windowAncestor = null;

		if (!ignorePopupMenuEvents)
		{
			// The focus should not be passed directly to the editor, as it does not work anymore. The Caret blinking Thread is broken, and
			// transferFocusToNext in KeyBoardManager is not working
//			if (isChild(internalPopup, KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()))
//			{
//				KeyboardFocusManager.getCurrentKeyboardFocusManager().
//					dispatchEvent(new FocusEvent(editorComponent, FocusEvent.FOCUS_GAINED));
//			}
			Object item = getModel().getSelectedItem();
			if (!popupCanceled)
			{
				setSelectedItem(item);
			}
			firePopupMenuWillBecomeInvisible();
		}
	}
	
	/**
	 * True, if popup is canceled, or popup is set as canceled.
	 * @return true, if popup is canceled, or popup is set as canceled.
	 */
	public boolean isPopupCanceled()
	{
		return popupCanceled;
	}
	
	/**
	 * True, if popup should close in canceled mode.
	 * @param pPopupCanceled True, if popup should close in canceled mode.
	 */
	public void setPopupCanceled(boolean pPopupCanceled)
	{
		popupCanceled = pPopupCanceled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void popupMenuCanceled(PopupMenuEvent pPopupMenuEvent)
	{
		if (!ignorePopupMenuEvents)
		{
			popupCanceled = true;
			firePopupMenuCanceled();
		}
	}
	
	//ITranslatable
	
	/**
	 * {@inheritDoc}
	 */
    public void setTranslation(TranslationMap pTranslation)
    {
    	if (translation != pTranslation)
    	{
	    	translation = pTranslation;
	    	
	    	if (popupComponent instanceof ITranslatable)
	    	{
	    		((ITranslatable)popupComponent).setTranslation(translation);
	    	}
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public TranslationMap getTranslation()
    {
    	return translation;
    }

    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        bTranslationEnabled = pEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return bTranslationEnabled;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    @Override
    public Object getSelectedItem() 
    {
    	return selectedItem;
    }
  	
	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setSelectedItem(Object pSelectedItem) 
    {
    	if ((selectedItem == null && pSelectedItem != null) || (selectedItem != null && !selectedItem.equals(pSelectedItem)))
   		{
    	    fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, selectedItem, ItemEvent.DESELECTED));

        	selectedItem = pSelectedItem;
    		getEditor().setItem(pSelectedItem);

    	    fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, selectedItem, ItemEvent.SELECTED));
   		}
    	else
    	{
    		getEditor().setItem(selectedItem);
    	}
    	fireActionEvent();
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void selectedItemChanged() 
    {
    }
    
	/**
	 * {@inheritDoc}
	 */
    @Override
    public void contentsChanged(ListDataEvent pListDataEvent) 
    {
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void intervalAdded(ListDataEvent pListDataEvent) 
    {
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void intervalRemoved(ListDataEvent pListDataEvent) 
    {
    }
        
	/**
	 * {@inheritDoc}
	 */
    @Override
    public int getSelectedIndex() 
    {
    	return -1;
    }
  	
	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setSelectedIndex(int pIndex) 
    {
    	// Not supported in JVxComboBase
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public int getItemCount() 
    {
        return 0;
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public Object getItemAt(int index) 
    {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public void doLayout()
    {
    	super.doLayout();

    	if (bMacLaF)
    	{
    		if (editorComponent != null && internalButton != null)
    		{
    			if (editorComponent.getHeight() != internalButton.getHeight())
    			{
		    		Rectangle rect = editorComponent.getBounds();
		    		
		    		editorComponent.setBounds(rect.x, rect.y, rect.width - 3, rect.height - 2);
    			}
    		}
    	}
    }
	
    /**
	 * {@inheritDoc}
	 */
	@Override
	public void setUI(ComboBoxUI pComboBoxUI)
	{
		setEditable(true);
		super.setUI(pComboBoxUI);

		bMacLaF = pComboBoxUI.getClass().getSimpleName().contains("Aqua");
		
		JPopupMenu comboPopup = (JPopupMenu)getUI().getAccessibleChild(this, 0);
		//needed to init the border!
		comboPopup.updateUI();

		Border border = comboPopup.getBorder();
		
		if (internalPopup == null)
		{
			internalPopup = new JVxPopupMenu()
			{
				private boolean ignore = false;
				
			    public void setVisible(boolean pVisible) 
			    {
			    	if (!ignore)
			    	{
			    		try
			    		{
			    			ignore = true;
			    			
			    			super.setVisible(pVisible);
			    		}
			    		finally
			    		{
			    			ignore = false;
			    		}
			    	}
			    }
			    
			    @Override
			    public Insets getInsets()
			    {
			    	if (getClientProperty("#customBorder#") != null)
			    	{
			    		return new Insets(2, 1, 1, 1);
			    	}
			    	else
			    	{
			    		return super.getInsets();
			    	}
			    }
			};
			internalPopup.setLayout(new BorderLayout());
			internalPopup.addPopupMenuListener(this);
		}
		//it's a good idea to give the popup a name (maybe useful for LaF)
		internalPopup.setName(comboPopup.getName());
		
		//#2034
		if (SwingFactory.isWindows() && pComboBoxUI instanceof BasicComboBoxUI)
		{
			//e.g. openJDK12
			if (border instanceof EmptyBorder)
			{
				border = new LineBorder(Color.BLACK, 1);
				internalPopup.putClientProperty("#customBorder#", Boolean.TRUE);
			}
			else
			{
				internalPopup.putClientProperty("#customBorder#", null);
			}
		}

		internalPopup.setBorder(border);
		
		//even this popup should look a bit better!
		SwingUtilities.updateComponentTreeUI(internalPopup);
		
		for (int i = getComponentCount() - 1; i >= 0; i--)
		{
			Component comp = getComponent(i);
			if (comp instanceof JButton)
			{
				internalButton = (JButton)comp;
			}
			else if (comp != editorComponent)
			{
				remove(comp);
			}
		}
		if (internalButton != null)
		{
			removeComboListeners(internalButton);
			internalButton.removeMouseListener(this);
			internalButton.addMouseListener(this);
		}
		
		removeComboListeners(this);
		removeComboActionMap();
		
		if (internalButton == null)
		{
			addMouseListener(this);
		}

		if (editorComponent != null)
		{
            SwingUtilities.invokeLater(
            	new Runnable()
            	{
            	  	public void run()
            	  	{
            	  		updateEditorComponent();
            	    }
            	});
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEditable(boolean pEditable)
	{
		if (!pEditable)
		{
			throw new IllegalArgumentException("JVxComboBase is always editable");
		}
		
		super.setEditable(pEditable);
	}
	
 	/**
 	 * Sets the visibility of the choice component.
 	 *
 	 * @param pPopupVisible the visibility of the choice component
 	 */
	@Override
 	public void setPopupVisible(boolean pPopupVisible)
 	{
	 	if (pPopupVisible && isShowing() && (isEditorEditable() || !isTextInputEnabled()) && buttonEnabled)
     	{
	 		ignorePopupMenuEvents = isPopupVisible();

	 		Dimension size = getSize();

	 		internalPopup.setPreferredSize(null);
	 		
			getModel().setSelectedItem(getEditor().getItem());
	 		popupComponent.addNotify(); // Force correct preferred size calculation with simulating a packed state.

	 		if (isShowing()) // selection/ model activity can cause the removal of the component.
	 		{
				Dimension prefSize = internalPopup.getPreferredSize();
				if (popupSize != null)
				{
			 		if (popupSize.width > 0 && prefSize.width > popupSize.width)
			 		{
			 			prefSize.width = popupSize.width;
			 		}
			 		if (popupSize.height > 0 && prefSize.height > popupSize.height)
			 		{
			 			prefSize.height = popupSize.height;
			 		}
				}
		 		if (prefSize.width < size.width)
		 		{
		 			prefSize.width = size.width;
		 		}
		 		
		 		//#2017
		 		//especially for MacOS, otherwise the popup won't resize
		 		prefSize.height = Math.max(1, prefSize.height);
		 		
		 		internalPopup.setPreferredSize(prefSize);
		 		internalPopup.setSize(prefSize);
	    	 	internalPopup.validate();
	
	    	 	if (ignorePopupMenuEvents)
	    	 	{
	    	 		Container cont = internalPopup;
	    	 		while (!(cont instanceof Window) && cont.getParent() != null)
	    	 		{
	    	 			cont = cont.getParent();
	    	 		}
	    	 		if (cont instanceof Window && !(cont instanceof Frame))
	    	 		{
	    	 			((Window)cont).pack();
	    	 		}
	    	 		else
	    	 		{
	    	 			internalPopup.getParent().setSize(prefSize);
	    	 			internalPopup.validate();
	    	 		}
	    	 	}
	    	 	else
	    	 	{
		    	 	internalPopup.show(this, 0, size.height);
		         	
		         	windowAncestor = SwingUtilities.getWindowAncestor(this);
		         	if (windowAncestor != null)
		         	{
		         		WindowListener[] listeners = windowAncestor.getWindowListeners();
		         		for (int i = 0; i < listeners.length; i++)
		         		{
		         			if (listeners[i].getClass().getName().contains("MouseGrabber"))
		         			{
		         				windowAncestor.removeWindowListener(listeners[i]);
		         			}
		         		}
		         		windowAncestor.addWindowListener(this);
		         	}
					Component comp = LAYOUT_POLICY.getFirstComponent(popupComponent);
		        	AWTEvent awtEvent = EventQueue.getCurrentEvent();
					if (comp != null && (forceFocusOnPopup || (awtEvent instanceof KeyEvent && ((KeyEvent)awtEvent).getKeyCode() == KeyEvent.VK_DOWN)))
					{
						comp.requestFocus();
					}
					else if (editorComponent.isFocusable())
					{
						editorComponent.requestFocus();
		         	}
		     		else
		     		{
		       			requestFocus();
		     		}
	    	 	}
	 		}
			ignorePopupMenuEvents = false;
     	}
     	else if (!pPopupVisible && internalPopup.isShowing())
     	{
     		try
     		{
     			internalPopup.setVisible(false);
     		}
     		catch (Exception ex)
     		{
     			// Try to close anyway!
     		}
     	}
 	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEditor(ComboBoxEditor pEditor)
	{
		if (editorComponent != null)
		{
			editorComponent.removeMouseListener(this);
			editorComponent.removeKeyListener(this);
//			editorComponent.removeFocusListener(this);
		}
		
        JTextField tf = null;
        int iCols = -1;

        if (pEditor != null)
        {
            Component compEditor = pEditor.getEditorComponent();
		
    		if (compEditor instanceof JTextField)
    		{
    		    tf = ((JTextField)compEditor);
    		    
    		    iCols = tf.getColumns();
    		}
        }
		
		super.setEditor(pEditor);
		
		if (tf != null)
		{
		    tf.setColumns(iCols);
		}
		
		if (pEditor == null)
		{
			editorComponent = null;
		}
		else
		{
			editorComponent = (JTextComponent)pEditor.getEditorComponent();
			updateEditorComponent();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean pVisible)
	{
		super.setVisible(pVisible);
		
		if (!pVisible && isPopupVisible())
		{
			setPopupVisible(false);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		
		if (isPopupVisible())
		{
			setPopupVisible(false);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
        Dimension dim = super.getPreferredSize();

        if (editorComponent != null && !isPreferredSizeSet())
	    {
	        return new Dimension(editorComponent.getPreferredSize().width + dim.height, dim.height);
	    }
        else
        {
            return dim;
        }
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets true, if the editor is editable.
	 * @return true, if the editor is editable.
	 */
	public boolean isEditorEditable()
	{
		if (editorComponent == null)
		{
			return true;
		}
		else
		{
			return editorComponent.isEditable();
		}
	}
	
	/**
	 * Sets true, if the editor is editable.
	 * @param pEditable true, if the editor is editable.
	 */
	public void setEditorEditable(boolean pEditable)
	{
		bEditable = pEditable;
		
		if (editorComponent != null)
		{
			editorComponent.setEditable(bEditable && bTextInputEnabled);
			if (internalButton != null)
			{
				internalButton.setEnabled((bEditable || !bTextInputEnabled) && buttonEnabled);
			}
			updateEditorComponent();
		}
	}
	
	/**
	 * Sets whether it's allowed to enter text.
	 * 
	 * @param pTextInput <code>true</code> to enable entering text, <code>false</code> to deny entering text
	 */
	public void setTextInputEnabled(boolean pTextInput)
	{
		bTextInputEnabled = pTextInput;
		
		setEditorEditable(pTextInput);
	}
	
	/**
	 * Gets whether it's allowed to enter text.
	 * 
	 * @return <code>true</code> if it's allowed, <code>false</code> otherwise
	 */
	public boolean isTextInputEnabled()
	{
		return bTextInputEnabled;
	}
	
	/**
	 * Sets the Button enabled or not.
	 * @param pButtonEnabled the Button enabled or not.
	 */
	public void setButtonEnabled(boolean pButtonEnabled)
	{
		buttonEnabled = pButtonEnabled;
		if (editorComponent == null || (editorComponent.isEditable() != buttonEnabled))
		{
			if (internalButton != null)
			{
				internalButton.setEnabled(pButtonEnabled);
			}
		}
	}
	
    /**
     * Gets the size of the Popup window using a <code>Dimension</code> object.
     * Null means, that the Popup window opens with preferredSize.
     *
     * @return the Popup size of.
     */
	public Dimension getPopupSize()
	{
		return popupSize;
	}
	
    /**
     * Sets the size of the Popup window using a <code>Dimension</code> object.
     * Null means, that the Popup window opens with preferredSize.
     *
     * @param pPopupSize the Popup size of.
     */
	public void setPopupSize(Dimension pPopupSize)
	{
		popupSize = pPopupSize;
	}
	
    /**
     * Gets if the focus is forced to be given on the popup.
     *
     * @return the focus is forced to be given on the popup.
     */
	public boolean isForceFocusOnPopup()
	{
		return forceFocusOnPopup;
	}
	
    /**
     * Gets if the focus is forced to be given on the popup.
     *
     * @param pForceFocusOnPopup the focus is forced to be given on the popup.
     */
	public void setForceFocusOnPopup(boolean pForceFocusOnPopup)
	{
		forceFocusOnPopup = pForceFocusOnPopup;
	}
	
	/**
	 * Removes all relevant combo listeners from the components.
	 * 
	 * @param pComponent the component.
	 */
	private void removeComboListeners(Component pComponent)
	{
		MouseListener[] mouseListener = pComponent.getMouseListeners();
		for (int i = 0; i < mouseListener.length; i++)
		{
			if (mouseListener[i].getClass().getName().indexOf("Combo") >= 0)
			{
				pComponent.removeMouseListener(mouseListener[i]);
			}
		}
		MouseMotionListener[] mouseMotionListener = pComponent.getMouseMotionListeners();
		for (int i = 0; i < mouseMotionListener.length; i++)
		{
			if (mouseMotionListener[i].getClass().getName().indexOf("Combo") >= 0)
			{
				pComponent.removeMouseMotionListener(mouseMotionListener[i]);
			}
		}
		KeyListener[] keyListener = pComponent.getKeyListeners();
		for (int i = 0; i < keyListener.length; i++)
		{
			if (keyListener[i].getClass().getName().indexOf("Combo") >= 0)
			{
				pComponent.removeKeyListener(keyListener[i]);
			}
		}
		FocusListener[] focusListener = pComponent.getFocusListeners();
		for (int i = 0; i < focusListener.length; i++)
		{
			if (focusListener[i].getClass().getName().indexOf("Combo") >= 0)
			{
				pComponent.removeFocusListener(focusListener[i]);
			}
		}
	}
	
	/**
	 * Removes the action map of this combobox added by look and feel.
	 */
	private void removeComboActionMap()
	{
		ActionMap map = getActionMap();
		while (map.getParent() != null)
		{
			if (map.getParent().getParent() == null)
			{
				map.setParent(null);
			}
			else
			{
				map = map.getParent();
			}
		}
	}
	
	/**
	 * Updates the editor component.
	 */
	private void updateEditorComponent()
	{
        
		JComboBox combo = new JComboBox();
    	combo.setEditable(true);
        JTextComponent def = (JTextComponent)combo.getEditor().getEditorComponent();
        def.setEditable(isEditorEditable());
        def.addNotify();
        def.updateUI();

        if (editorComponent != null)
        {
        	if (bMacLaF)
        	{
        		editorComponent.setBorder(new WrappedInsetsBorder(def.getBorder()));
        	}
        	else
        	{
        		editorComponent.setBorder(def.getBorder());
        	}
	        // Only set Background, if it is not set by the editor itself.
	        if (editorComponent.getBackground() instanceof UIResource) 
	        {
	        	editorComponent.setBackground(def.getBackground());
	        }

	        editorComponent.removeMouseListener(this);
			editorComponent.addMouseListener(this);
	        editorComponent.removeKeyListener(this);
			editorComponent.addKeyListener(this);
        }
	}
	
	/**
	 * Returns the visibility of the choice component.
	 *
	 * @return the visibility of the choice component
	 */
	public boolean isPopupVisible()
	{
		return internalPopup.isVisible();
	}

	
 	/**
 	 * True, if FocusEvent is caused from the Popup.
 	 * 
 	 * @param pFocusEvent the focus event.
 	 * @return True, if FocusEvent is caused from the Popup.
 	 */
 	public boolean isPopupFocusEvent(FocusEvent pFocusEvent)
 	{
 		return isChild(internalPopup, pFocusEvent.getOppositeComponent()) || isChild(this, pFocusEvent.getOppositeComponent());
 	}
 	
 	/**
 	 * True, if pComp is a child of pParent.
 	 * 
 	 * @param pParent the parent.
 	 * @param pComp the child.
 	 * @return True, if pComp is a child of pParent.
 	 */
 	private boolean isChild(Component pParent, Component pComp)
 	{
 		while (pComp != null)
 		{
 			if (pComp == pParent)
 			{
 				return true;
 			}
 			pComp = pComp.getParent();
 		}
 		return false;
 	}
 	
	/**
	 * Returns the editor component.
	 * 
	 * @return the editor component.
	 */
	public JTextComponent getEditorComponent()
	{
		return editorComponent;
	}

	/**
	 * Sets the editor component.
	 *
	 * @param pEditorComponent the editor component
	 */
	public void setEditorComponent(JTextComponent pEditorComponent)
	{
		setEditor(new GenericComboBoxEditor(pEditorComponent));
	}

	/**
	 * Returns the popup component.
	 * 
	 * @return the popup component.
	 */
	public JComponent getPopupComponent()
	{
		return popupComponent;
	}

	/**
	 * Sets the popup component.
	 *
	 * @param pPopupComponent the popup component
	 */
	public void setPopupComponent(JComponent pPopupComponent)
	{
		if (popupComponent != null)
		{
			internalPopup.remove(popupComponent);
		}
		popupComponent = pPopupComponent;
		if (popupComponent != null)
		{
			if (popupComponent instanceof ITranslatable)
			{
				((ITranslatable)popupComponent).setTranslation(translation);
			}
			
			internalPopup.add(popupComponent, BorderLayout.CENTER);
			internalPopup.validate();
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * Generic Editor that allows the changing the EditorComponent. 
	 * 
	 * @author Martin Handsteiner
	 */
	public static class GenericComboBoxEditor implements ComboBoxEditor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The editor component. */
		private JTextComponent editorComponent;
		
		/** the no focus component. */
		private static JTextField tfNoFocus;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		static
		{
	    	tfNoFocus = new JTextField();
	    	tfNoFocus.setFocusable(false);
		}
		
		/**
		 * Generates a <code>GenericComboBoxEditor</code> with {@link JTextField}.
		 */
	    public GenericComboBoxEditor() 
	    {
	        this(null);
	    }

		/**
		 * Generates a <code>GenericComboBoxEditor</code> with the given editor.
		 * 
		 * @param pEditorComponent the editor component.
		 */
	    public GenericComboBoxEditor(JTextComponent pEditorComponent) 
	    {
	    	if (pEditorComponent == null)
	    	{
		    	editorComponent = new JTextField("", 10);
	    	}
	    	else
	    	{
		    	editorComponent = pEditorComponent;
	    	}
	    	
	    	// Setting this name to the editor component is important and needed
	    	// for the Synth LaF engine. Not settings (or overriding) will cause
	    	// that the editor component will have wrong margins/borders.
	    	editorComponent.setName("ComboBox.textField");
	    	
	    	JVxUtil.installActions(editorComponent);
	    }

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * {@inheritDoc}
	     */
	    public Component getEditorComponent() 
	    {   
	    	if (SwingFactory.isMacLaF())
	    	{
		    	StackTraceElement[] ste = new Exception().getStackTrace();
		    	
		    	if (!editorComponent.isEditable() 
		    		&& ste[1].getClassName().endsWith(".AquaComboBoxButton") 
		    		&& ste[1].getMethodName().equals("paintComponent"))
		    	{
		    		return tfNoFocus;
		    	}
	    	}
	    		
	    	return editorComponent;
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
	    public Object getItem() 
	    {
	    	if (editorComponent instanceof JFormattedTextField)
	    	{
	    		try
	    		{
	    			((JFormattedTextField)editorComponent).commitEdit();
	    		}
	    		catch (ParseException pEx)
	    		{
	    			// Beep on invalid edit.
	    			UIManager.getLookAndFeel().provideErrorFeedback(editorComponent);
	    		}
	    		return ((JFormattedTextField)editorComponent).getValue();
	    	}
	    	else
	    	{
	    		String text = editorComponent.getText();
	    		if (text == null || text.length() == 0)
	    		{
	    			return null;
	    		}
	    		else
	    		{
	    			return text;
	    		}
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void setItem(Object pObject) 
	    {
	    	AbstractDocument document = (AbstractDocument)editorComponent.getDocument();
	    	DocumentListener[] listeners = document.getDocumentListeners();
	    	for (DocumentListener listener : listeners)
	    	{
	    	    if (!(listener instanceof PromptSupport))
	    	    {
	    	        document.removeDocumentListener(listener);
	    	    }
	    	}
	    	
	    	if (editorComponent instanceof JFormattedTextField)
	    	{
	    		((JFormattedTextField)editorComponent).setValue(pObject);
	    	}
	    	else
	    	{
	    		if (pObject == null)
	    		{
		    		editorComponent.setText("");
	    		}
	    		else
	    		{
		    		editorComponent.setText(String.valueOf(pObject));
	    		}
	    	}
    		editorComponent.repaint();
            for (DocumentListener listener : document.getDocumentListeners())
            {
                document.removeDocumentListener(listener);
            }
	    	for (DocumentListener listener : listeners)
	    	{
	    		document.addDocumentListener(listener);
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void selectAll() 
	    {
	    	editorComponent.selectAll();
	    	editorComponent.requestFocus();
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void addActionListener(ActionListener pActionListener) 
	    {
	    	if (editorComponent instanceof JTextField)
	    	{
	    		((JTextField)editorComponent).addActionListener(pActionListener);
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public void removeActionListener(ActionListener pActionListener) 
	    {
	    	if (editorComponent instanceof JTextField)
	    	{
	    		((JTextField)editorComponent).removeActionListener(pActionListener);
	    	}
	    }

	}	// GenericComboBoxEditor

}  // JVxComboBase
