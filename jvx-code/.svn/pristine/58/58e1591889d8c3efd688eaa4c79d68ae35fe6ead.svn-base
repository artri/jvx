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
 * 09.11.2008 - [HM] - creation
 * 24.03.2011 - [JR] - #317: cancelEditing checks parents enabled state
 */
package com.sibvisions.rad.ui.swing.ext.cellrenderer;

import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;

import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;

/**
 * The <code>JVxRendererContainer</code> is a renderer container.
 * 
 * @author Martin Handsteiner
 */
public class JVxRendererContainer extends JPanel
{

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new JVxRendererContainer.
	 */
	public JVxRendererContainer()
	{
		this(new JVxBorderLayout());
	}
	
    /**
     * Constructs a new JVxRendererContainer.
     * 
     * @param pLayout the layout
     */
    public JVxRendererContainer(LayoutManager pLayout)
    {
        super(pLayout);
        setOpaque(true);
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
        setForeground(null);
        setBackground(null);
    }
	
    /**
 	 * {@inheritDoc}
 	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	@Override
    public void setBounds(int x, int y, int width, int heigth) 
    {
		super.setBounds(x, y, width, heigth);
		
		doLayout();
    }

    /**
 	 * {@inheritDoc}
 	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	@Override
    public void validate() 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     *
     * @since 1.5
     */
	@Override
    public void invalidate() 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void revalidate() 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void repaint(long tm, int x, int y, int width, int height) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void repaint(Rectangle r) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     *
     * @since 1.5
     */
	@Override
    public void repaint() 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void firePropertyChange(String propertyName, char oldValue, char newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void firePropertyChange(String propertyName, short oldValue, short newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void firePropertyChange(String propertyName, int oldValue, int newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void firePropertyChange(String propertyName, long oldValue, long newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void firePropertyChange(String propertyName, float oldValue, float newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void firePropertyChange(String propertyName, double oldValue, double newValue) 
    {
    }

    /**
     * {@inheritDoc}
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
	@Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) 
    { 
    }
	
}	// JVxRendererContainer
