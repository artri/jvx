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
 * 17.11.2008 - [HM] - creation
 * 21.07.2009 - [JR] - dispose: isDisposed checked
 * 13.11.2011 - [JR] - #504: don't change z-order in tabbed mode
 * 24.10.2012 - [JR] - #604: added constructor
 * 04.04.2013 - [JR] - toFront, toBack: disable closing event (temporary)
 */
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.event.WindowHandler;

/**
 * Platform and technology independent InternalFrame.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UIInternalFrame extends AbstractFrame<IInternalFrame>
							 implements IInternalFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIInternalFrame</code>.
     *
     * @param pDesktopPanel the associated desktop for the internal frame
     * @see IInternalFrame
     * @see IDesktopPanel
     */
	public UIInternalFrame(IDesktopPanel pDesktopPanel)
	{
		this(UIFactoryManager.getFactory().createInternalFrame(getDesktopPanel(pDesktopPanel)), pDesktopPanel);
	}

    /**
     * Creates a new instance of <code>UIInternalFrame</code> with the given 
     * internal frame.
     *
     * @param pFrame the internal frame 
     * @param pDesktopPanel the associated desktop for the internal frame
     * @see IInternalFrame
     * @see IDesktopPanel
     */
	protected UIInternalFrame(IInternalFrame pFrame, IDesktopPanel pDesktopPanel)
	{
		super(pFrame);
		
		if (pDesktopPanel instanceof UIDesktopPanel)
		{
			pDesktopPanel.add(this, 0);
		}
		else
		{
			setParent(pDesktopPanel);
		}
		
		setLayout(new UIBorderLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setMaximizable(boolean pMaximizable)
    {
    	uiResource.setMaximizable(pMaximizable);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isMaximizable()
    {
    	return uiResource.isMaximizable();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setClosable(boolean pClosable)
    {
    	uiResource.setClosable(pClosable);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isClosable()
    {
    	return uiResource.isClosable();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setIconifiable(boolean pIconifiable)
    {
    	uiResource.setIconifiable(pIconifiable);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isIconifiable()
    {
    	return uiResource.isIconifiable();
    }

	/**
	 * {@inheritDoc}
	 */
	public void close()
    {
    	//TODO [JR] Nach UI Umbau sowieso anders! BUGFIX
    	//TODO [HM] Nach UI Umbau sowieso anders! BUGFIX
		IContainer parent = getParent();
		
		uiResource.close();
    	
		if (parent instanceof UIDesktopPanel)
		{
			((UIDesktopPanel)parent).remove(this);
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isClosed()
    {
    	return uiResource.isClosed();
    }
     
	/**
	 * {@inheritDoc}
	 */
    public void setModal(boolean pModal)
    {
    	uiResource.setModal(pModal);
    }
     
	/**
	 * {@inheritDoc}
	 */
    public boolean isModal()
    {
    	return uiResource.isModal();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toFront()
    {
		IContainer con = getParent();
		
		//don't change z-order in tab-mode, otherwise the tab position is changed!
		if (con instanceof UIDesktopPanel && !((UIDesktopPanel)con).isTabMode())
		{
			if (getState() == UIInternalFrame.ICONIFIED)
			{
				setState(UIInternalFrame.NORMAL);
			}
			
			WindowHandler handler = eventWindowClosing();
			
			boolean bOld = handler.isDispatchEventsEnabled();
			
			handler.setDispatchEventsEnabled(false);
			
			try
			{
				((UIContainer)getParent()).setZOrder(this, 0);
			}
			finally
			{
				handler.setDispatchEventsEnabled(bOld);
			}
		}
		
		super.toFront();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toBack()
    {
		super.toBack();

		IContainer con = getParent();
		
		//don't change z-order in tab-mode, otherwise the tab position is changed!
		if (con instanceof UIDesktopPanel && !((UIDesktopPanel)con).isTabMode() && getState() != UIInternalFrame.ICONIFIED)
		{
			WindowHandler handler = eventWindowClosing();
			
			boolean bOld = handler.isDispatchEventsEnabled();
			
			handler.setDispatchEventsEnabled(false);
			
			try
			{
				((UIContainer)getParent()).setZOrder(this, -1);
			}
			finally
			{
				handler.setDispatchEventsEnabled(bOld);
			}
		}
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void topLevelAddNotify()
    {
		//don't call addNotify!
    }    

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void dispose()
    {
    	if (!isDisposed())
    	{
	    	IContainer parent = getParent();
			
			super.dispose();
			
			if (parent instanceof UIDesktopPanel)
			{
				((UIDesktopPanel)parent).remove(this);
			}
    	}
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * Gets the UIResource of the DesktopPanel. 
	 * 
	 * @param pDesktopPanel the DesktopPanel
	 * @return the UIResource of the DesktopPanel
	 */
	private static IDesktopPanel getDesktopPanel(IDesktopPanel pDesktopPanel)
	{
		if (pDesktopPanel instanceof UIDesktopPanel)
		{
			return ((UIDesktopPanel)pDesktopPanel).getUIResource();
		}
		else
		{
			return pDesktopPanel;
		}
	}

	/**
	 * Gets the active flag of the internal frame ignoring modal frames.
	 * 
	 * @return true, if this frame is active in the desktop, ignoring modal frames.
	 */
	public boolean isGlobalActive()
    {
		if (isModal())
		{
			return isActive();
		}
		else if (isActive())
		{
			UIDesktopPanel desktop = (UIDesktopPanel)getParent();
			
			for (int i = 0, count = desktop.getComponentCount(); i < count; i++)
			{
				IComponent comp = desktop.getComponent(i);
				
				if (comp instanceof IInternalFrame && ((IInternalFrame)comp).isModal())
				{
					return false;
				}
			}
			
			return true;
		}
		else
		{
			return false;
		}
    }


}	// UIInternalFrame
