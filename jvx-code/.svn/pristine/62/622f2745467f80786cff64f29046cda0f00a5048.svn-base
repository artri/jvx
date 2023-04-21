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
 * 08.12.2008 - [JR] - used abstract modifier for class
 * 13.12.2008 - [JR] - setImage: wrong instance used for creating image [BUGFIX]
 * 20.07.2009 - [JR] - set/getMargins implemented
 * 31.07.2009 - [JR] - actionPerformed: show WAIT cursor
 * 17.10.2009 - [JR] - mouse pressed/over icon support
 * 15.09.2014 - [RZ] - the action command is now handled separately
 */
package com.sibvisions.rad.ui.swing.impl.component;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.rad.ui.IImage;
import javax.rad.ui.component.IButton;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.util.SilentAbortException;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.sibvisions.rad.ui.swing.ext.JVxButton;
import com.sibvisions.rad.ui.swing.ext.JVxToggleButton;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>SwingButton</code> is the <code>IButton</code>
 * implementation for swing.
 * 
 * @param <C> instance of AbstractButton.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.AbstractButton
 * @see javax.rad.ui.component.IButton
 */
public abstract class SwingAbstractButton<C extends AbstractButton> extends SwingAbstractFormatableButton<C> 
                                                                    implements IButton, 
                                                                               ActionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the mouse over image. */
	protected IImage imgOver = null;
	
	/** the mouse pressed image. */
	protected IImage imgPressed = null;
	
	/** EventHandler for actionPerformed. */
	private ActionHandler eventActionPerformed = null;
    
    /**
     * The action command of this button. It is extracted to circumvent
     * the behavior of Swing that it returns the button's text if
     * action command is not set, instead of returning {@code null}.
     */
    private String sActionCommand = null;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingAbstractButton</code>.
	 * 
	 * @param pAbstractButton the instance of AbstractButton.
	 */
	public SwingAbstractButton(C pAbstractButton)
	{
		this(pAbstractButton, false);
	}
	
	/**
	 * Creates a new instance of <code>SwingAbstractButton</code>.
	 * 
	 * @param pAbstractButton the button instance.
	 * @param pDummyImage <code>true</code> to initializes a dummy image
	 */
	protected SwingAbstractButton(C pAbstractButton, boolean pDummyImage)
	{
		super(pAbstractButton, pDummyImage);

		resource.addActionListener(this);
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// IButton
	
    /**
     * {@inheritDoc}
     */
    public void setVerticalTextPosition(int pVerticalPosition)
    {
        resource.setVerticalTextPosition(SwingFactory.getVerticalSwingAlignment(pVerticalPosition));
    }
    
    /**
     * {@inheritDoc}
     */
    public int getVerticalTextPosition()
    {
        return SwingFactory.getVerticalAlignment(resource.getVerticalTextPosition());
    }
    
    /**
     * {@inheritDoc}
     */
    public void setHorizontalTextPosition(int pHorizontalPosition)
    {
        resource.setHorizontalTextPosition(SwingFactory.getHorizontalSwingAlignment(pHorizontalPosition));
    }
    
    /**
     * {@inheritDoc}
     */
    public int getHorizontalTextPosition()
    {
        return SwingFactory.getHorizontalAlignment(resource.getHorizontalTextPosition());
    }
	
	/**
	 * {@inheritDoc}
	 */
    public boolean isBorderOnMouseEntered()
    {
		if (resource instanceof JVxButton)
		{
			return ((JVxButton)resource).isBorderOnMouseEntered();
		}
		else if (resource instanceof JVxToggleButton)
		{
			return ((JVxToggleButton)resource).isBorderOnMouseEntered();
		}
		else
		{
			return false;
		}
    }

	/**
	 * {@inheritDoc}
	 */
    public void setBorderOnMouseEntered(boolean pBorderOnMouseEntered)
    {
		if (resource instanceof JVxButton)
		{
			((JVxButton)resource).setBorderOnMouseEntered(pBorderOnMouseEntered);
		}
		else if (resource instanceof JVxToggleButton)
		{
			((JVxToggleButton)resource).setBorderOnMouseEntered(pBorderOnMouseEntered);
		}
    }

	/**
	 * {@inheritDoc}
	 */
    public void setAccelerator(Key pKey)
    {
    	if (resource instanceof JVxButton)
    	{
    		((JVxButton)resource).setAccelerator(SwingFactory.getKeyStroke(pKey));
    	}
    	else if (resource instanceof JVxToggleButton)
    	{
    		((JVxToggleButton)resource).setAccelerator(SwingFactory.getKeyStroke(pKey));
    	}
    	else if (resource instanceof JMenuItem)
    	{
    		((JMenuItem)resource).setAccelerator(SwingFactory.getKeyStroke(pKey));
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public Key getAccelerator()
    {
        if (resource instanceof JVxButton)
        {
            return SwingFactory.getKey(((JVxButton)resource).getAccelerator());
        }
        else if (resource instanceof JVxToggleButton)
        {
            return SwingFactory.getKey(((JVxToggleButton)resource).getAccelerator());
        }
        else if (resource instanceof JMenuItem)
        {
            return SwingFactory.getKey(((JMenuItem)resource).getAccelerator());
        }
        
        return null;
    }       
    
    /**
     * {@inheritDoc}
     */
    public void setBorderPainted(boolean pBorderPainted)
    {
    	resource.setContentAreaFilled(pBorderPainted);
    	resource.setBorderPainted(pBorderPainted);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isBorderPainted()
    {
    	return resource.isBorderPainted();
    }

    /**
     * {@inheritDoc}
     */
    public void setMouseOverImage(IImage pImage)
    {
    	if (pImage == null)
    	{
    		resource.setRolloverEnabled(false);
    		resource.setRolloverIcon(null);
    	}
    	else
    	{
    		resource.setRolloverEnabled(true);
    		resource.setRolloverIcon((ImageIcon)pImage.getResource());
    	}

    	imgOver = pImage;
    }
    
    /**
     * {@inheritDoc}
     */
    public IImage getMouseOverImage()
    {
    	return imgOver;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setPressedImage(IImage pImage)
    {
    	if (pImage == null)
    	{
    		resource.setPressedIcon(null);
    	}
    	else
    	{
    		resource.setPressedIcon((ImageIcon)pImage.getResource());
    	}

    	imgPressed = pImage;
    }
    
    /**
     * {@inheritDoc}
     */
    public IImage getPressedImage()
    {
    	return imgPressed;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setDefaultButton(boolean pDefault)
	{
		if (resource instanceof JVxButton)
		{
			((JVxButton)resource).setDefaultButton(pDefault);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDefaultButton()
	{
		if (resource instanceof JVxButton)
		{
			return ((JVxButton)resource).isDefaultButton();
		}
		else
		{
			return false;
		}
	}
    
	/**
	 * {@inheritDoc}
	 */
	public String getActionCommand()
	{
		return sActionCommand;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setActionCommand(String pActionCommand)
	{
		sActionCommand = pActionCommand;
		resource.setActionCommand(pActionCommand);
	}

	/**
	 * {@inheritDoc}
	 */
    public ActionHandler eventAction()
    {
		if (eventActionPerformed == null)
		{
			eventActionPerformed = new ActionHandler();
		}
		return eventActionPerformed;
    }
    
	// ActionListener
	
	/**
	 * {@inheritDoc}
	 */
    public void actionPerformed(ActionEvent pActionEvent)
    {
    	if (eventActionPerformed != null
    		&& eventActionPerformed.isDispatchable())
    	{
    		JVxUtil.setGlobalCursor(resource, Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			try
			{
	    		eventActionPerformed.dispatchEvent(new UIActionEvent(eventSource, 
	    															 UIActionEvent.ACTION_PERFORMED, 
	    															 pActionEvent.getWhen(), 
	    															 pActionEvent.getModifiers(), 
	    															 sActionCommand));
			}
			catch (SilentAbortException ex)
			{
    			// Prevent graphical glitches by catching the event. There are
    			// listener behind this, that has to be executed, to ensure
    			// a proper state in the gui.
				// The exception is already delegated to event handler in
    			// dispatch event. 
			}
			finally
			{
				JVxUtil.setGlobalCursor(resource, null);
			}
    	}
    }

}	// SwingAbstractButton
