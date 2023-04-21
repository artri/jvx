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
 * 05.02.2011 - [JR] - #275: setMenuBar now unsets parent if new parent is null
 * 04.10.2011 - [JR] - #477: beforeAddNotify handling
 * 13.08.2013 - [JR] - #756: changed add order
 * 05.04.2014 - [JR] - #1001: don't change text if translation is disabled
 */
package javax.rad.genui.container;

import javax.rad.genui.UIImage;
import javax.rad.genui.menu.UIMenuBar;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IImage;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.menu.IMenuBar;

/**
 * Platform and technology independent frame.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * 
 * @param <C> instance of IFrame
 */
public abstract class AbstractFrame<C extends IFrame> extends AbstractWindow<C> 
                                                      implements IFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the icon image. */ 
	protected transient IImage iconImage = null;
	
	/** the menu bar. */ 
	protected transient IMenuBar menuBar = null;
	
	/** The ToolBarPanel handler. */
	private transient InternalToolBarPanel toolBarPanel;
	
	/** the frame title. */
	private transient String sTitle = null;
	
	/** the flag indicates that addNotify is active. */
	private transient boolean bAddNotify = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractFrame</code>.
     *
     * @param pFrame the IFrame.
     * @see IFrame
     */
	protected AbstractFrame(C pFrame)
	{
		super(pFrame);

		toolBarPanel = new InternalToolBarPanel(this);
		eventWindowClosing().setDefaultListener(this, "dispose");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getTitle()
    {
		return sTitle;
    }

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String pTitle)
    {
		sTitle = pTitle;
		
    	uiResource.setTitle(translate(pTitle));
    }

	/**
	 * {@inheritDoc}
	 */
	public IImage getIconImage()
    {
    	return iconImage;
    }

	/**
	 * {@inheritDoc}
	 */
	public void setIconImage(IImage pIconImage)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pIconImage instanceof UIImage)
		{
	    	uiResource.setIconImage(((UIImage)pIconImage).getUIResource());
		}
		else
		{
	    	uiResource.setIconImage(pIconImage);
		}
		iconImage = pIconImage;
    }

	/**
	 * {@inheritDoc}
	 */
    public int getState()
    {
    	return uiResource.getState();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setState(int pState)
    {
    	uiResource.setState(pState);
    }

	/**
	 * {@inheritDoc}
	 */
    public boolean isResizable()
    {
    	return uiResource.isResizable();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setResizable(boolean pResizable)
    {
    	uiResource.setResizable(pResizable);
    }

    /**
 	 * {@inheritDoc}
 	 */
    public IMenuBar getMenuBar()
    {
   		return menuBar;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void setMenuBar(IMenuBar pMenuBar)
    {
    	// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
    	if (pMenuBar instanceof UIMenuBar)
 		{
    		if (menuBar instanceof UIMenuBar && ((UIMenuBar)menuBar).isNotified())
    		{
    			((UIMenuBar)menuBar).removeNotify();
    		}
    		
    		if (menuBar != null)
    		{
    			menuBar.setParent(null);
    		}
    		
 	    	if (isNotified() && !((UIMenuBar)pMenuBar).isBeforeNotified())
 	    	{
 	    		((UIMenuBar)pMenuBar).beforeAddNotify(this);
 	    	}

 	    	IContainer conOldParent = pMenuBar.getParent();
 	    	
 	    	pMenuBar.setParent(this);

 	    	try
 	    	{
 	    		uiResource.setMenuBar(((UIMenuBar)pMenuBar).getUIResource());
 	    	}
 	    	catch (RuntimeException re)
 	    	{
 	    		pMenuBar.setParent(conOldParent);
 	    		
 	    		throw re;
 	    	}
 	    	catch (Error e)
 	    	{
 	    		pMenuBar.setParent(conOldParent);

 	    		throw e;
 	    	}
 	    	
 	    	if (isNotified() && !((UIMenuBar)pMenuBar).isNotified())
 	    	{
 	    		((UIMenuBar)pMenuBar).addNotify();
 	    	}
 		}
 		else
 		{
 			if (pMenuBar == null)
 			{
 	    		if (menuBar instanceof UIMenuBar && ((UIMenuBar)menuBar).isNotified())
 	    		{
 	    			((UIMenuBar)menuBar).removeNotify();
 	    		}
 	    		
 	    		if (menuBar != null)
 	    		{
 	    			menuBar.setParent(null);
 	    		}
 			}
 			
 	    	uiResource.setMenuBar(pMenuBar);
 		}
    	
    	menuBar = pMenuBar;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar)
	{
		addToolBar(pToolBar, -1);
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void addToolBar(IToolBar pToolBar, int pIndex)
	{
		toolBarPanel.addToolBar(pToolBar, pIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(int pIndex)
	{
		toolBarPanel.removeToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeToolBar(IToolBar pToolBar)
	{
		toolBarPanel.removeToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeAllToolBars()
	{
		int iSize;
		while ((iSize = toolBarPanel.getToolBarCount()) > 0)
		{
			removeToolBar(iSize - 1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getToolBarCount()
	{
		return toolBarPanel.getToolBarCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IToolBar getToolBar(int pIndex)
	{
		return toolBarPanel.getToolBar(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return toolBarPanel.indexOfToolBar(pToolBar);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolBarArea(int pArea)
	{
		toolBarPanel.setToolBarArea(pArea);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getToolBarArea()
	{
		return toolBarPanel.getToolBarArea();
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
		
		if (bTranslate && bChanged)
		{
			uiResource.setTitle(translate(sTitle));
		}
		
		if (bAddNotify)
		{
			return; 
		}
		
		if (menuBar instanceof UIMenuBar)
		{
			((UIMenuBar)menuBar).updateTranslation();
		}
		
		toolBarPanel.updateTranslation();
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeAddNotify(IComponent pParent)
	{
		if (bAddNotify)
		{
			return;
		}
		
		bAddNotify = true;
		
		try
		{
			super.beforeAddNotify(pParent);
		}
		finally
		{
			bAddNotify = false;
		}

		if (menuBar instanceof UIMenuBar)
		{
			((UIMenuBar)menuBar).beforeAddNotify(this);
		}
		
		toolBarPanel.beforeAddNotify(this);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		if (bAddNotify)
		{
			return;
		}
		
		bAddNotify = true;
		
		try
		{
			super.addNotify();
		}
		finally
		{
			bAddNotify = false;
		}

		if (menuBar instanceof UIMenuBar)
		{
			((UIMenuBar)menuBar).addNotify();
		}
		
		toolBarPanel.addNotify();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		
		if (menuBar instanceof UIMenuBar)
		{
			((UIMenuBar)menuBar).removeNotify();
		}
		
		toolBarPanel.removeNotify();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets all toolbars movable or fixed.
	 * 
	 * @param pMovable <code>true</code> to move the toolbars, <code>false</code> otherwise
	 */
	public void setToolBarMovable(boolean pMovable)
	{
		toolBarPanel.setToolBarMovable(pMovable);
	}
	
	/**
	 * Gets whether the toolbars are movable.
	 * 
	 * @return <code>true</code> if the toolbars are movable, <code>false</code> otherwise
	 */
	public boolean isToolBarMovable()
	{
		return toolBarPanel.isToolBarMovable();
	}
	
}	// AbstractFrame
