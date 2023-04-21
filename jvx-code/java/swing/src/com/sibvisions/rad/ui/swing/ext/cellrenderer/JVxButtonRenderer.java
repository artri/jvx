/*
 * Copyright 2014 SIB Visions GmbH
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
 * 05.04.2023 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext.cellrenderer;

import java.awt.Rectangle;

import com.sibvisions.rad.ui.swing.ext.JVxButton;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>JVxButtonRenderer</code> is a button renderer.
 * 
 * @author Martin Handsteiner
 */
public class JVxButtonRenderer extends JVxButton
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** focus flag. */
    private boolean hasFocus = false;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new JVxButtonRenderer.
	 */
	public JVxButtonRenderer()
	{
		setFocusable(false);
		
		if (SwingFactory.isMacLaF())
		{
			setBorder(null);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
 	 * {@inheritDoc}
	 */
	@Override
    public void updateUI() 
	{
        super.updateUI(); 
        
        if (SwingFactory.isLaFOpaque())
        {
            setForeground(null);
            setBackground(null);
        }
    }
	
    /**
 	 * {@inheritDoc}
 	 * 
 	 * Overridden for performance reasons.
	 */
	@Override
    public void validate() 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void invalidate() 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void revalidate() 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void repaint(long tm, int x, int y, int width, int height) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void repaint(Rectangle r) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void repaint() 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void firePropertyChange(String propertyName, char oldValue, char newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void firePropertyChange(String propertyName, short oldValue, short newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void firePropertyChange(String propertyName, int oldValue, int newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void firePropertyChange(String propertyName, long oldValue, long newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void firePropertyChange(String propertyName, float oldValue, float newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void firePropertyChange(String propertyName, double oldValue, double newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden for performance reasons.
     */
	@Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) 
    { 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFocus()
    {
        return hasFocus;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Sets focus for renderer.
     * 
     * @param pHasFocus the focus.
     */
	public void setHasFocus(boolean pHasFocus)
	{
	    hasFocus = pHasFocus;
	}
	
}	// JVxButtonRenderer
