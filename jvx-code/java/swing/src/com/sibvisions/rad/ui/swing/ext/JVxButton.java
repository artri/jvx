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
 * 08.10.2008 - [HM] - creation
 * 08.12.2008 - [JR] - setDefaultButton implemented
 * 11.10.2009 - [JR] - setBorderOnMouseEntered: set opaque property
 * 21.02.2011 - [JR] - #292: mouseEntered checks if button is enabled 
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.WeakHashMap;

import javax.rad.ui.celleditor.IStyledCellEditor;
import javax.rad.util.SilentAbortException;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sibvisions.rad.ui.swing.ext.event.IMenuActionListener;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>JVxButton</code> is a <code>JButton</code>
 * extension.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JButton
 */
public class JVxButton extends JButton
                       implements MouseListener,
                                  MouseMotionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Client property key for button accelerator. */
    private static final String BUTTON_ACCELERATOR = "buttonAccelerator";
	/** Client property key for border on mouse entered. */
	private static final String BORDER_ON_MOUSE_ENTERED = "borderOnMouseEntered";

    /** The Accelerator Action for pressed event. */
    private static final AcceleratorAction ACCELERATOR_ACTION_PRESSED = new AcceleratorAction(AcceleratorAction.PRESSED);
    /** The Accelerator Action for released event. */
    private static final AcceleratorAction ACCELERATOR_ACTION_RELEASED = new AcceleratorAction(AcceleratorAction.RELEASED);
    
	/** Border removed time. */
	private static WeakHashMap<AbstractButton, Long> borderRemovedTime = new WeakHashMap<AbstractButton, Long>();
    
	
    /** the margins without split area. */
    private Insets insOrig;
    
    /** the buffered arrow image. */
    private Image imgArrow;
    
    /** the buffered separator image. */
    private Image imgSeparator;

    /** the buffered bounds. */
    private Rectangle rectCachedBounds;
    
    /** the split separator.. */
    private JSeparator separator;

    /** the menu. */
    private JPopupMenu menu;
    
    /** the split area with the arrow. */
    private Rectangle rectSplitArea;
    
    /** the default action if not listener is set. */
    private JMenuItem miDefault;
    
    /** the list of internal action listeners (only for menu counting). */
    private ArrayUtil<ActionListener> auInternalListener;
    
    /** the default split area width. */
    private int iSplitWidth = SwingFactory.isMacLaF() ? 35 : 20;
    
    /** the default arrow width. */
    private int iArrowWidth = 8;
    
    /** whether the mouse cursor is in/over the split area. */
    private boolean bIsOverSplitArea = false;
    
    /** the mark whether this button is the default button for a root pane. */
    private boolean bDefault = false;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a button with no set text or icon.
     */
    public JVxButton() 
    {
        super(null, null);
    }
    
    /**
     * Creates a button with an icon.
     *
     * @param pIcon  the Icon image to display on the button
     */
    public JVxButton(Icon pIcon) 
    {
        super(null, pIcon);
    }
    
    /**
     * Creates a button with text.
     *
     * @param pText  the text of the button
     */
    public JVxButton(String pText) 
    {
        super(pText, null);
    }
    
    /**
     * Creates a button where properties are taken from the 
     * <code>Action</code> supplied.
     *
     * @param pAction the <code>Action</code> used to specify the new button
     */
    public JVxButton(Action pAction) 
    {
        super(null, null);
        
        setAction(pAction);
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param pText  the text of the button
     * @param pIcon  the Icon image to display on the button
     */
    public JVxButton(String pText, Icon pIcon) 
    {
        super(pText, pIcon);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processMouseEvent(MouseEvent pMouseEvent)
    {
        IStyledCellEditor cellEditor = (IStyledCellEditor)getClientProperty("cellEditor");
        if (cellEditor != null)
        {
            Rectangle bounds = getBounds();
            Dimension prefSize = getPreferredSize();
            
            int horizontalAlignment = SwingFactory.getHorizontalSwingAlignment(cellEditor.getHorizontalAlignment());
            if (getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT)
            {
                if (horizontalAlignment == JVxConstants.LEADING)
                {
                    horizontalAlignment = JVxConstants.RIGHT;
                }
                else if (horizontalAlignment == JVxConstants.TRAILING)
                {
                    horizontalAlignment = JVxConstants.LEFT;
                }
            }
            else
            {
                if (horizontalAlignment == JVxConstants.LEADING)
                {
                    horizontalAlignment = JVxConstants.LEFT;
                }
                else if (horizontalAlignment == JVxConstants.TRAILING)
                {
                    horizontalAlignment = JVxConstants.RIGHT;
                }
            }
            
            switch (horizontalAlignment)
            {
                case JVxConstants.LEFT:
                    bounds.x = 0;
                    bounds.width = prefSize.width;
                    break;
                case JVxConstants.CENTER:
                    bounds.x = (bounds.width - prefSize.width) / 2;
                    bounds.width = prefSize.width;
                    break;
                case JVxConstants.RIGHT:
                    bounds.x = bounds.width - prefSize.width;
                    bounds.width = prefSize.width;
                    break;
                default:
                    bounds.x = 0;
            }
            switch (SwingFactory.getHorizontalSwingAlignment(cellEditor.getVerticalAlignment()))
            {
                case JVxConstants.TOP:
                    bounds.y = 0;
                    bounds.height = prefSize.height;
                    break;
                case JVxConstants.CENTER:
                    bounds.y = (bounds.height - prefSize.height) / 2;
                    bounds.height = prefSize.height;
                    break;
                case JVxConstants.RIGHT:
                    bounds.y = bounds.height - prefSize.height;
                    bounds.height = prefSize.height;
                    break;
                default:
                    bounds.y = 0;
            }
            
            if (bounds.contains(pMouseEvent.getX(), pMouseEvent.getY()))
            {
                super.processMouseEvent(pMouseEvent);
            }
        }
        else
        {
            super.processMouseEvent(pMouseEvent);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) 
    {
        // Stop animation thread, if component is not showing anymore.
        return isShowing() && super.imageUpdate(img, infoflags, x, y, w, h);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public boolean isShowing()
	{
        if (getClientProperty("jvx.isSuperShowing") != null)
        {
            return super.isShowing();
        }
        else
        {
            if (super.isShowing())
        	{
        		if (!isContentAreaFilled())
        		{
        	    	Long time = borderRemovedTime.get(this);
        			
        			if (time != null)
        			{
        				long dif = System.currentTimeMillis() - time.longValue();
        				if (dif < 210)
        				{
        					return dif < 100;
        				}
        				else
        				{
        					borderRemovedTime.remove(this);
        				}
        			}
        		}
        		return true;
        	}
        	else
        	{
        		return false;
        	}
        }
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
	public boolean isDefaultButton()
	{
		return bDefault || super.isDefaultButton();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
	public void addNotify()
	{        
        super.addNotify();

        if (bDefault)
        {
	        JRootPane root = SwingUtilities.getRootPane(this);
	        
	        if (root != null) 
	        {
	        	//unset the "old" default button
		        JButton butOldDefault = root.getDefaultButton();
		        
		        if (butOldDefault != this)
		        {
			        if (butOldDefault instanceof JVxButton)
			        {
			        	((JVxButton)butOldDefault).setDefaultButton(false);
			        }
	
			        //set the new default button
			        root.setDefaultButton(this);
		        }
	        }
        }
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeActionListener(ActionListener pListener)
    {
        super.removeActionListener(pListener);
        
        if (auInternalListener != null)
        {
            auInternalListener.remove(pListener);
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setMargin(Insets pInsets)
    {
        insOrig = pInsets;

        if (menu != null)
        {
        	int iGap = 0;
        	
        	if (SwingFactory.isMacLaF())
        	{
        		iGap = 5;
        	}
        	
        	putClientProperty("#insets#", pInsets);
        	
            super.setMargin(new Insets(insOrig.top, insOrig.left, insOrig.bottom, insOrig.right + iSplitWidth - iGap));
        }
        else
        {
            super.setMargin(pInsets);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        if (menu != null)
        {
            Dimension dimSize = getSize();
            
            Rectangle rectCurrentBounds = getBounds();
            
            if (!rectCurrentBounds.equals(rectCachedBounds))
            {
                imgSeparator = null;
            }
            
            rectSplitArea = new Rectangle(dimSize.width - iSplitWidth, 0, iSplitWidth, dimSize.height);
            
            //g.drawRect(rectSplitArea.x, rectSplitArea.y, rectSplitArea.width, rectSplitArea.height);
            
            //Separator

            if (isBorderPainted() && isEnabled())
            {
                if (imgSeparator == null)
                {
                    BufferedImage img = new BufferedImage(4, dimSize.height, BufferedImage.TYPE_INT_ARGB);
                    
                    Graphics gr = img.getGraphics();
                    
                    if (separator == null)
                    {
                        separator = new JSeparator(JSeparator.VERTICAL);
                        separator.setEnabled(isEnabled());
                    }
                    
                    if (SwingFactory.isMacLaF())
                    {
                    	gr.setColor(separator.getForeground());
                    	gr.fillRect(2, 4, 1, getSize().height - 16);
                    }
                    else
                    {
                    	separator.setBounds(4, 4, 4, dimSize.height - 8);
                        separator.paint(gr);
                    }
                    
                    gr.dispose();
                    
                    imgSeparator = img;
                }
                
                g.drawImage(imgSeparator, dimSize.width - iSplitWidth, 4, this);
            }
            
            // Arrow
            
            if (imgArrow == null)
            {
                rectCachedBounds = rectCurrentBounds;
                
                BufferedImage img = new BufferedImage(iArrowWidth, iArrowWidth, BufferedImage.TYPE_INT_ARGB);
                
                Graphics gr = img.getGraphics();
                
                if (isEnabled())
                {
                    gr.setColor(Color.black);
                }
                else
                {
                    gr.setColor(new Color(153, 153, 153));
                }
                
                gr.fillPolygon(new int[]{0, iArrowWidth, iArrowWidth / 2}, 
                               new int[] {0, 0, (iArrowWidth / 2)}, 
                               3);
                gr.dispose();
                
                imgArrow = img;
            }
            
            g.drawImage(imgArrow, dimSize.width - iSplitWidth - 1 + ((iSplitWidth - iArrowWidth) / 2), 
                        (dimSize.height - iArrowWidth / 2) / 2 + 1, this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean pEnabled)
    {
        if (pEnabled != isEnabled())
        {
            //we need a new image
            imgArrow = null;
        }
        
        if (separator != null)
        {
            separator.setEnabled(pEnabled);
        }

        super.setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fireActionPerformed(ActionEvent pEvent) 
    {
        if (menu != null)
        {
            if (bIsOverSplitArea)
            {
                try
                {
                    fireMenuActionPerformed(pEvent);
                
                    setMenuVisible(true);
                }
                catch (SilentAbortException se)
                {
                    //don't show menu
                }
            }
            else
            {
                ActionListener[] listener = getActionListeners();
                
                int iCount = listener.length;
                
                if (auInternalListener != null)
                {
                    for (int i = 0; i < listener.length; i++)
                    {
                        if (auInternalListener.indexOf(listener[i]) >= 0)
                        {
                            iCount--;
                        }
                    }
                }
                
                if (iCount == 0)
                {
                    if (miDefault != null)
                    {
                        miDefault.doClick(0);
                    }
                    else
                    {
                        try
                        {
                            fireMenuActionPerformed(pEvent);
                        
                            setMenuVisible(true);
                        }
                        catch (SilentAbortException se)
                        {
                            //don't show menu
                        }
                    }
                }
                else
                {
                    super.fireActionPerformed(pEvent);
                }
            }
        }
        else
        {
            super.fireActionPerformed(pEvent);
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent pEvent)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent pEvent)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent pEvent)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent pEvent)
    {
    	checkSplitAreaPosition(pEvent);
    }

    /**
     * {@inheritDoc}
     */
    public void mouseExited(MouseEvent pEvent)
    {
    	checkSplitAreaPosition(null);
    }

    /**
     * {@inheritDoc}
     */
    public void mouseMoved(MouseEvent pEvent)
    {
    	checkSplitAreaPosition(pEvent);
    }
    
    /**
     * {@inheritDoc}
     */
    public void mouseDragged(MouseEvent pEvent)
    {
    	checkSplitAreaPosition(pEvent);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Checks if the given mouse position is over the split area.
     * 
     * @param pEvent the mouse event
     */
    protected void checkSplitAreaPosition(MouseEvent pEvent)
    {
        if (rectSplitArea != null)
        {
            bIsOverSplitArea = pEvent != null && rectSplitArea.contains(pEvent.getPoint());
            
            repaint(rectSplitArea);
        }
        else
        {
        	bIsOverSplitArea = false;
        }
    }
    
	/**
	 * Sets this button as default button for the "parent" root pane. If
	 * another button is defined as default button, then the other button
	 * will unset as default button.
	 * 
	 * @param pDefault <code>true</code> to set this button as default
	 */
	public void setDefaultButton(boolean pDefault)
	{
		bDefault = pDefault;
	}

    /**
     * Returns the <code>KeyStroke</code> which serves as an accelerator 
     * for the menu item.
     * @return a <code>KeyStroke</code> object identifying the
     *		accelerator key
     */
    public KeyStroke getAccelerator() 
    {
        return JVxButton.getAccelerator(this);
    }
    
    /**
     * Sets the key combination which invokes the menu item's
     * action listeners without navigating the menu hierarchy. It is the
     * UI's responsibility to install the correct action.  Note that 
     * when the keyboard accelerator is typed, it will work whether or
     * not the menu is currently displayed.
     *
     * @param pKeyStroke the <code>KeyStroke</code> which will
     *		serve as an accelerator 
     */
    public void setAccelerator(KeyStroke pKeyStroke) 
    {
    	JVxButton.setAccelerator(this, pKeyStroke);
    }

    /**
     * Gets, if the border should only be shown on mouse entered.
     *
     * @return true, if the border should only be shown on mouse entered.
     */
    public boolean isBorderOnMouseEntered()
    {
        return JVxButton.isBorderOnMouseEntered(this);
    }

    /**
     * Sets, if the border should only be shown on mouse entered.
     *
     * @param pBorderOnMouseEntered true, if the border should only be shown on mouse entered.
     */
    public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
    {
    	JVxButton.setBorderOnMouseEntered(this, pBorderOnMouseEntered);
    }
    
    /**
     * Creates a new KeyStroke with the given OnKeyRelease behaviour.
     * @param pKeyStroke the original KeyStroke.
     * @param pOnKeyRelease the OnKeyRelease behaviour.
     * @return a new KeyStroke with the given OnKeyRelease behaviour. 
     */
    private static KeyStroke getKeyStroke(KeyStroke pKeyStroke, boolean pOnKeyRelease)
    {
    	int keyCode = pKeyStroke.getKeyCode();
    	if (keyCode == KeyEvent.VK_UNDEFINED)
    	{
    		keyCode = Character.toUpperCase(pKeyStroke.getKeyChar());
    	}
		return KeyStroke.getKeyStroke(pKeyStroke.getKeyCode(), pKeyStroke.getModifiers(), pOnKeyRelease);
    }
    
    /**
     * Returns the <code>KeyStroke</code> which serves as an accelerator 
     * for the menu item.
     * @param pAbstractButton the AbstractButton to install the accelerator.
     * @return a <code>KeyStroke</code> object identifying the
     *		accelerator key
     */
    public static KeyStroke getAccelerator(AbstractButton pAbstractButton)
    {
    	return (KeyStroke)pAbstractButton.getClientProperty(BUTTON_ACCELERATOR);
    }
    
    /**
     * Sets the key combination which invokes the menu item's
     * action listeners without navigating the menu hierarchy. It is the
     * UI's responsibility to install the correct action.  Note that 
     * when the keyboard accelerator is typed, it will work whether or
     * not the menu is currently displayed.
     *
     * @param pAbstractButton the AbstractButton to install the accelerator.
     * @param pKeyStroke the <code>KeyStroke</code> which will
     *		serve as an accelerator 
     */
    public static void setAccelerator(AbstractButton pAbstractButton, KeyStroke pKeyStroke)
    {
    	KeyStroke oldKeyStroke = getAccelerator(pAbstractButton);
    	if (oldKeyStroke != null)
    	{
        	pAbstractButton.getActionMap().remove(AcceleratorAction.PRESSED);
        	pAbstractButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(getKeyStroke(oldKeyStroke, false));
        	pAbstractButton.getActionMap().remove(AcceleratorAction.RELEASED);
        	pAbstractButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(getKeyStroke(oldKeyStroke, true));
    	}
    	pAbstractButton.putClientProperty(BUTTON_ACCELERATOR, pKeyStroke);
    	if (pKeyStroke != null)
    	{
        	pAbstractButton.getActionMap().put(AcceleratorAction.PRESSED, ACCELERATOR_ACTION_PRESSED);
        	pAbstractButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(getKeyStroke(pKeyStroke, false), AcceleratorAction.PRESSED);
        	pAbstractButton.getActionMap().put(AcceleratorAction.RELEASED, ACCELERATOR_ACTION_RELEASED);
        	pAbstractButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(getKeyStroke(pKeyStroke, true), AcceleratorAction.RELEASED);
    	}
    }
	
    /**
     * Gets, if the border should only be shown on mouse entered.
     *
	 * @param pAbstractButton the AbstractButton.
     * @return true, if the border should only be shown on mouse entered.
     */
	public static boolean isBorderOnMouseEntered(AbstractButton pAbstractButton)
	{
		return pAbstractButton.getClientProperty(BORDER_ON_MOUSE_ENTERED) != null;
	}
	
    /**
     * Sets, if the border should only be shown on mouse entered.
     *
	 * @param pAbstractButton the AbstractButton.
     * @param pBorderOnMouseEntered true, if the border should only be shown on mouse entered.
     */
	public static void setBorderOnMouseEntered(AbstractButton pAbstractButton, boolean pBorderOnMouseEntered)
	{
		BorderOnMouseEnteredListener listener = (BorderOnMouseEnteredListener)pAbstractButton.getClientProperty(BORDER_ON_MOUSE_ENTERED);
		
		if (pBorderOnMouseEntered)
		{
			if (listener == null)
			{
				listener = new BorderOnMouseEnteredListener(pAbstractButton);
				
				pAbstractButton.putClientProperty(BORDER_ON_MOUSE_ENTERED, listener);
				
				pAbstractButton.addMouseListener(listener);
				pAbstractButton.addPropertyChangeListener("ancestor", listener);
				pAbstractButton.getModel().addChangeListener(listener);
				
				pAbstractButton.putClientProperty("jvx.opaque", Boolean.valueOf(pAbstractButton.isOpaque()));
				pAbstractButton.setOpaque(false);

				listener.setBorderVisible(pAbstractButton.getModel().isPressed() || pAbstractButton.getModel().isSelected());
			}
		}
		else
		{
			if (listener != null)
			{
				pAbstractButton.putClientProperty(BORDER_ON_MOUSE_ENTERED, null);

				pAbstractButton.removeMouseListener(listener);
                pAbstractButton.removePropertyChangeListener("ancestor", listener);
				pAbstractButton.getModel().removeChangeListener(listener);
				
				Boolean bOpaque = (Boolean)pAbstractButton.getClientProperty("jvx.opaque");
				
				if (bOpaque != null)
				{
					pAbstractButton.putClientProperty("jvx.opaque", null);
					
					pAbstractButton.setOpaque(bOpaque.booleanValue());
				}
				
				listener.setBorderVisible(true);
			}
		}
	}
	
	/**
	 * Notifies all menu action listeners.
	 * 
	 * @param pEvent the action event
	 */
    protected void fireMenuActionPerformed(ActionEvent pEvent)
    {
        Object[] listeners = listenerList.getListenerList();
        
        ActionEvent aev = null;
        
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == IMenuActionListener.class)
            {
                if (aev == null)
                {
                    String actionCommand = pEvent.getActionCommand();
                    
                    if (actionCommand == null)
                    {
                        actionCommand = getActionCommand();
                    }
                    
                    aev = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand, pEvent.getWhen(), pEvent.getModifiers());
                }
                
                ((IMenuActionListener)listeners[i + 1]).menuActionPerformed(aev);
            }
        }
    }
    
    /**
     * Sets the visibility of the popup menu.
     * 
     * @param pVisible <code>true</code> to show, <code>false</code> to hide the menu
     */
    protected void setMenuVisible(boolean pVisible)
    {
        if (menu != null)
        {
            if (pVisible)
            {
                //otherwise pref-size will be the old popup size
                menu.setPopupSize(null);
                
                Dimension dimPref = menu.getPreferredSize();
                
                int iWidth = getWidth();
                
                if (iWidth < dimPref.width)
                {
                    iWidth = dimPref.width;
                }

                int iBorderGap = 0;
                int iBorderBottom = 0;
                
                if (SwingFactory.isMacLaF())
                {
                	String sName = getBorder().getClass().getName();
                	
                	if (sName.indexOf("$Dynamic") >= 0)
                	{
                		//dynamic is a button without image
                    	iBorderGap = 7 - (insOrig != null ? insOrig.left : 0);
                    	iBorderBottom = getBorder().getBorderInsets(this).bottom - (insOrig != null ? insOrig.bottom  + insOrig.top : 0);
                    	
                        iWidth -= iBorderGap * 2;
                	}
                	else if (sName.indexOf("$Toggle") >= 0)
                	{
                		//a button with an image
                		
                    	iBorderGap = 3;
                    	iBorderBottom = 3;
                    	
                        iWidth -= iBorderGap * 2;
                	}
                	
                	//$Named is a button in a toolbar 
                }
                
                menu.setPopupSize(iWidth, dimPref.height);
                
                //looks better with border
                //
                //setBorderPainted(false);
                //setContentAreaFilled(false);
                
                //repaint();
                
                menu.show(this, 0 + iBorderGap, getHeight() - iBorderBottom);
            }
            else
            {
                menu.setVisible(false);            
            }
        }
    }
    
    /**
     * Sets the popup menu.
     * 
     * @param pMenu the menu
     */
    public void setPopupMenu(JPopupMenu pMenu)
    {
        menu = pMenu;
        
        if (menu == null)
        {
            removeMouseMotionListener(this);
            removeMouseListener(this);

            rectSplitArea = null;
        }
        else
        {
            addMouseMotionListener(this);
            addMouseListener(this);
        }
        
        setMargin(insOrig);
    }
    
    /**
     * Gets the popup menu.
     * 
     * @return the menu
     */
    public JPopupMenu getPopupMenu()
    {
        return menu;
    }
    
    /**
     * Sets the default menu item for actions. This item should be a sub element of the configured 
     * menu but it's not necessary. If not custom action event was set, this item will be used as
     * action receiver.
     *  
     * @param pMenuItem the default menu item
     */
    public void setDefaultItem(JMenuItem pMenuItem)
    {
        miDefault = pMenuItem;
    }
    
    /**
     * Gets the default menu item.
     * 
     * @return the default menu item
     * @see #setDefaultItem(JMenuItem)
     */
    public JMenuItem getDefaultItem()
    {
        return miDefault;
    }
    
    /**
     * Adds a menu action listener.
     * 
     * @param pListener the listener
     */
    public void addMenuActionListener(IMenuActionListener pListener)
    {
        listenerList.add(IMenuActionListener.class, pListener);
    }
    
    /**
     * Removes a menu action listener.
     * 
     * @param pListener the listener
     */
    public void removeMenuActionListener(IMenuActionListener pListener)
    {
        listenerList.remove(IMenuActionListener.class, pListener);
    }    
    
    /**
     * Adds an event listener to the list of ignored listeners for menu handling.
     *  
     * @param pListener the listener
     */
    public void addIgnoreActionListener(ActionListener pListener)
    {
        if (auInternalListener == null)
        {
            auInternalListener = new ArrayUtil<ActionListener>();
        }
        
        if (auInternalListener.indexOf(pListener) < 0)
        {
            auInternalListener.add(pListener);
        }
    }
    
    /**
     * Sets the split width.
     * 
     * @param pWidth the width
     */
    public void setSplitWidth(int pWidth)
    {
    	iSplitWidth = pWidth;
    	
    	Insets ins = (Insets)getClientProperty("#insets#"); 
    			
    	if (ins != null)
    	{
    		setMargin(ins);
    	}
    }
    
    /**
     * Gets the split width.
     * 
     * @return the width
     */
    public int getSplitWidth()
    {
    	return iSplitWidth;
    }
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
     * Actions for Buttons. Two type of action are supported:
     * pressed: Moves the button to a pressed state
     * released: Disarms the button.
     * 
     * @author Martin Handsteiner
     */
	private static class BorderOnMouseEnteredListener implements ChangeListener, 
																 PropertyChangeListener,
	                                                             MouseListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The AbstractButton for this listener. */
		private AbstractButton abstractButton;
		
		/** Mouse entered. */
		private boolean entered = false;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
         * Constructs a border change listener.
         * 
         * @param pAbstractButton the AbstractButton.
         */
        BorderOnMouseEnteredListener(AbstractButton pAbstractButton)
		{
        	abstractButton = pAbstractButton;
		}
		
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
		public void stateChanged(ChangeEvent pChangeEvent)
		{
			if (!entered)
			{
				setBorderVisible(abstractButton.getModel().isPressed() || abstractButton.getModel().isSelected());
			}
		}

        /**
         * {@inheritDoc}
         */
		public void mouseClicked(MouseEvent e)
		{
		}

        /**
         * {@inheritDoc}
         */
		public void mouseEntered(MouseEvent e)
		{
			if (abstractButton.isEnabled() 
				&& (e.getModifiers() & (MouseEvent.BUTTON1_MASK | MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK)) == 0)
			{
				entered = true;
				setBorderVisible(true);
			}
		}

        /**
         * {@inheritDoc}
         */
		public void mouseExited(MouseEvent e)
		{
			if (entered)
			{
				entered = false;

				setBorderVisible(abstractButton.getModel().isPressed() || abstractButton.getModel().isSelected());
			}
		}

        /**
         * {@inheritDoc}
         */
		public void mousePressed(MouseEvent e)
		{
		}

        /**
         * {@inheritDoc}
         */
		public void mouseReleased(MouseEvent e)
		{
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
	     * Internal function which shows or hides.
	     * 
	     * @param pBorderVisible shows or hides the button border.
	     */
	    protected void setBorderVisible(boolean pBorderVisible)
	    {
	    	if (abstractButton.isBorderPainted() != pBorderVisible)
	    	{
    			boolean opaque = abstractButton.isOpaque();

		    	if (!pBorderVisible)
		    	{
	    			if (abstractButton.isShowing())
		    		{
		    			try
		    			{
		    				abstractButton.getUI().update(abstractButton.getGraphics(), abstractButton);
		    			}
		    			catch (Exception ex)
		    			{
		    				// Ignore it, if it fails.
		    			}
		    			finally
		    			{
		    				borderRemovedTime.put(abstractButton, Long.valueOf(System.currentTimeMillis()));
		    			}
		    		}
    			}
	    	
		    	abstractButton.setBorderPainted(pBorderVisible);		        
		    	abstractButton.setContentAreaFilled(pBorderVisible);
		    	
		        if (opaque != abstractButton.isOpaque())
		        {
		        	abstractButton.setOpaque(opaque);
		        }
		        
		        //without this hack, the border will be shown
		        //this is only a problem with transparent background colors
		        if (!pBorderVisible)
		        {
		        	Component comp = abstractButton.getParent();
		        	
			        if (comp != null)
			        {
			        	if (comp.isBackgroundSet() && comp.getBackground().getAlpha() != 0xFF)
			        	{
			        		comp = abstractButton.getParent().getParent();
			        		
			        		if (comp != null)
			        		{
			        			comp.repaint();
			        		}
			        	}
			        }
		        }
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
		public void propertyChange(PropertyChangeEvent pPropertyChangeEvent)
		{
			if (entered && pPropertyChangeEvent.getNewValue() == null)
			{
				entered = false;
			}

			setBorderVisible(abstractButton.getModel().isPressed() || abstractButton.getModel().isSelected());
		}

	}	// BorderOnMouseEnteredListener
	
    /**
     * Actions for Buttons. Two type of action are supported:
     * pressed: Moves the button to a pressed state
     * released: Disarms the button.
     * 
     * @author Martin Handsteiner
     */
	private static class AcceleratorAction extends AbstractAction
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** Constant for pressed state. */
		private static final String PRESSED = "pressed";
		/** Constant for released state. */
        private static final String RELEASED = "released";

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Constructs an Action with name.
         * 
         * @param pName the name
         */
        AcceleratorAction(String pName)
		{
			super(pName);
		}
		
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Overwritten methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * {@inheritDoc}
         */
		public void actionPerformed(ActionEvent pActionEvent) 
		{
            AbstractButton button = (AbstractButton)pActionEvent.getSource();
            Object key = getValue(Action.NAME);
            
            if (key == PRESSED) 
            {
                ButtonModel model = button.getModel();
                model.setArmed(true);
                model.setPressed(true);
                if (!button.hasFocus()) 
                {
                	button.requestFocus();
                }
            }
            else if (key == RELEASED) 
            {
                ButtonModel model = button.getModel();
                model.setPressed(false);
                model.setArmed(false);
            }
        }

	}	//AcceleratorAction
	
}	// JVxButton
