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
 * 01.09.2009 - [JR] - creation
 */
package javax.rad.genui.container;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIContainer;
import javax.rad.ui.IComponent;
import javax.rad.ui.container.IWindow;
import javax.rad.ui.event.WindowHandler;
import javax.rad.ui.event.type.window.IWindowActivatedListener;
import javax.rad.ui.event.type.window.IWindowClosedListener;
import javax.rad.ui.event.type.window.IWindowClosingListener;
import javax.rad.ui.event.type.window.IWindowDeactivatedListener;
import javax.rad.ui.event.type.window.IWindowDeiconifiedListener;
import javax.rad.ui.event.type.window.IWindowIconifiedListener;
import javax.rad.ui.event.type.window.IWindowOpenedListener;

/**
 * Platform and technology independent window.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * 
 * @param <C> instance of IWindow
 */
public abstract class AbstractWindow<C extends IWindow> extends UIContainer<C> 
                                                        implements IWindow
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractWindow</code>.
     *
     * @param pWindow the IWindow
     * @see IWindow
     */
	protected AbstractWindow(C pWindow)
	{
		super(pWindow);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Top level window or frame has to start addNotify, if not notified.
	 */
	protected void topLevelAddNotify()
	{
    	if (!isNotified())
    	{
	    	addNotify();
    	}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void pack()
    {
		topLevelAddNotify();
		
    	uiResource.pack();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setVisible(boolean pVisible)
    {
		if (pVisible)
		{
			topLevelAddNotify();
		}
		
    	super.setVisible(pVisible);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void dispose()
    {
    	if (isNotified())
    	{
	    	removeNotify();
    	}
	
    	uiResource.dispose();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDisposed()
	{
		return uiResource.isDisposed();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isActive()
    {
    	return uiResource.isActive();
    }

	/**
	 * {@inheritDoc}
	 */
	public void toFront()
    {
    	uiResource.toFront();
    }

	/**
	 * {@inheritDoc}
	 */
	public void toBack()
    {
    	uiResource.toBack();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void centerRelativeTo(IComponent pComponent)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pComponent instanceof UIComponent)
		{
			uiResource.centerRelativeTo((IComponent)((UIComponent)pComponent).getUIResource());
		}
		else
		{
			uiResource.centerRelativeTo(pComponent);
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public WindowHandler<IWindowOpenedListener> eventWindowOpened()
	{
		return uiResource.eventWindowOpened();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public WindowHandler<IWindowClosingListener> eventWindowClosing()
	{
		return uiResource.eventWindowClosing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public WindowHandler<IWindowClosedListener> eventWindowClosed()
	{
		return uiResource.eventWindowClosed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public WindowHandler<IWindowIconifiedListener> eventWindowIconified()
	{
		return uiResource.eventWindowIconified();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public WindowHandler<IWindowDeiconifiedListener> eventWindowDeiconified()
	{
		return uiResource.eventWindowDeiconified();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public WindowHandler<IWindowActivatedListener> eventWindowActivated()
	{
		return uiResource.eventWindowActivated();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public WindowHandler<IWindowDeactivatedListener> eventWindowDeactivated()
	{
		return uiResource.eventWindowDeactivated();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTranslation()
    {
        boolean bChanged = isTranslationChanged();

        super.updateTranslation();
        
        if (bChanged)
        {
            uiResource.setTranslation(getCurrentTranslation());
        }
    }

}	// AbstractWindow
