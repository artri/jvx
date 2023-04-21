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
 * 20.11.2009 - [HM] - creation
 * 17.01.2013 - [JR] - removed unused code, constructor comment added
 * 20.02.2013 - [JR] - dispose calls setVisible(false)
 * 17.09.2013 - [JR] - removed Memory.gc
 */
package com.sibvisions.rad.ui.web.impl.container;

import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IInternalFrame;

/**
 * Web server implementation of {@link IInternalFrame}.
 * 
 * @author Martin Handsteiner
 */
public class WebInternalFrame extends AbstractWebFrame
						      implements IInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** whether the frame is disposed. */
	private boolean bDisposed = false;
	
	/** whether the frame is closed. */
	private boolean bClosed = false;
	
	/** whether visibility was set for the first time. */
	private boolean bFirstVisible = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebInternalFrame</code>.
     *
     * @param pDesktopPanel the associated desktop for the internal frame
     * @see javax.rad.ui.container.IInternalFrame
     * @see javax.rad.ui.container.IDesktopPanel
     */
	public WebInternalFrame(IDesktopPanel pDesktopPanel)
	{
		//don't use the desktop, because genUI adds this frame in its constructor!!!
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isMaximizable()
    {
    	return getProperty("maximizable", Boolean.TRUE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setMaximizable(boolean pMaximizable)
    {
		setProperty("maximizable", Boolean.valueOf(pMaximizable));
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isClosable()
    {
    	return getProperty("closable", Boolean.TRUE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setClosable(boolean pClosable)
    {
		setProperty("closable", Boolean.valueOf(pClosable));
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isIconifiable()
    {
    	return getProperty("iconifiable", Boolean.TRUE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setIconifiable(boolean pIconifiable)
    {
		setProperty("iconifiable", Boolean.valueOf(pIconifiable));
    }
	
	/**
	 * {@inheritDoc}
	 */
    public boolean isModal()
    {
    	return getProperty("modal", Boolean.FALSE).booleanValue();
    }
	
	/**
	 * {@inheritDoc}
	 */
    public void setModal(boolean pModal)
    {
		setProperty("modal", Boolean.valueOf(pModal));
    }
     
	/**
	 * {@inheritDoc}
	 */
	public boolean isVisible()
    {
		return getProperty("visible", Boolean.FALSE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setVisible(boolean pVisible)
    {
		if (isVisible() != pVisible)
		{
			setProperty("visible", Boolean.valueOf(pVisible));
			
			if (pVisible)
			{
				if (bFirstVisible)
				{
					windowOpened();
					
					bFirstVisible = false;
				}
			}
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isClosed()
    {
    	return bClosed;
    }
     
	/**
	 * {@inheritDoc}
	 */
	public void close()
    {
		windowClosing();
		
		bClosed = true;
		
		setProperty("close", Boolean.TRUE);
		
		dispose();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void dispose()
    {
		if (!isDisposed())
		{
			setVisible(false);
			
			bDisposed = true;
			
			setProperty("dispose", Boolean.TRUE);
			
			windowClosed();
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDisposed()
	{
		return bDisposed;
	}

}	// WebInternalFrame
