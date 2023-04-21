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
 * 09.04.2009 - [JR] - creation
 * 04.06.2009 - [JR] - add/removeNotify, isNotified
 * 02.07.2009 - [JR] - addToolBar: called removeToolBar instead of remove [BUGFIX]
 * 26.02.2010 - [JR] - getToolBar, removeToolBar, indexOfToolBar: used internal list instead of owner [BUGFIX]
 * 31.12.2010 - [JR] - removeToolBar: owner check instead of this [BUGFIX]
 * 04.10.2011 - [JR] - #477: beforeAddNotify handling 
 * 27.10.2011 - [JR] - set movable option for newly added toolbars   
 * 13.08.2013 - [JR] - #756: changed add order                 
 */
package javax.rad.genui.container;

import java.util.ArrayList;
import java.util.List;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIContainer;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;

/**
 * The <code>InternalToolBarPanel</code> is an {@link UIContainer} for
 * {@link IToolBar}s.
 * 
 * @author René Jahn
 */
public class InternalToolBarPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** List of toolbars. */
	private List<IToolBar> toolbars = new ArrayList<IToolBar>();
	
	/** the toolbar owner. */
	private UIContainer<? extends IToolBarPanel> owner; 
	
	/** whether the toolbar(s) are movable. */
	private boolean bMovable = true;
	
	/** the flag indicates whether the component was "before" notified. */
	private boolean bBeforeNotified = false;

	/** the flag indicates whether the component was notified. */
	private boolean bNotified = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>InternalToolBarPanel</code> with an owner.
	 * 
	 * @param pOwner the owner of the internal toolbar
	 */
	public InternalToolBarPanel(UIContainer<? extends IToolBarPanel> pOwner)
	{
		owner = pOwner;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds a toolbar to this panel at a specified index.
	 * 
	 * @param pToolBar the toolbar to be added
	 * @param pIndex the index for the toolbar              
	 */
	public void addToolBar(IToolBar pToolBar, int pIndex)
	{
		// Support both, UIComponents and IComponents
		IToolBar toolbar;
		if (pToolBar instanceof UIComponent)
		{
			toolbar = ((UIComponent<IToolBar>)pToolBar).getUIResource();
		}
		else
		{
			toolbar = pToolBar;
		}
		// Remove from UIContainer or from IContainer	
		if (pToolBar.getParent() != null)
		{
			((IToolBarPanel)pToolBar.getParent()).removeToolBar(toolbar);
		}

		if (pToolBar instanceof UIComponent)
		{
			if (isNotified() && !((UIComponent)pToolBar).isBeforeNotified())
			{
				((UIComponent)pToolBar).beforeAddNotify(owner);
			}
		}
		
		pToolBar.setMovable(bMovable);

		if (pIndex < 0)
		{
			toolbars.add(pToolBar);
		}
		else
		{
			toolbars.add(pIndex, pToolBar);
		}
		
		IContainer conOldParent = null;
		
		if (pToolBar instanceof UIComponent)
		{
			conOldParent = pToolBar.getParent();
			
			pToolBar.setParent(owner);
		}		

		try
		{
			if (pToolBar instanceof UIToolBar)
			{
				owner.getUIResource().addToolBar(((UIToolBar)pToolBar).getUIResource(), pIndex);
			}
			else
			{
				owner.getUIResource().addToolBar(pToolBar, pIndex);
			}
		}
		catch (RuntimeException re)
		{
			toolbars.remove(pToolBar);
			
			if (pToolBar instanceof UIComponent)
			{
				pToolBar.setParent(conOldParent);
			}
			
			throw re;
		}
		catch (Error e)
		{
			toolbars.remove(pToolBar);
			
			if (pToolBar instanceof UIComponent)
			{
				pToolBar.setParent(conOldParent);
			}
			
			throw e;
		}
		
		//send addNotify if the container is already added
		if (pToolBar instanceof UIComponent && isNotified() && !((UIComponent)pToolBar).isNotified())
		{
			((UIComponent)pToolBar).addNotify();
		}
	}

	/**
	 * Removes a toolbar from this panel.
	 * 
	 * @param pIndex the toolbar
	 */
	public void removeToolBar(int pIndex)
	{
		IToolBar toolBar = toolbars.get(pIndex);

		if (toolBar instanceof UIToolBar)
		{
			owner.getUIResource().removeToolBar(((UIToolBar)toolBar).getUIResource());
			
			if (((UIComponent)toolBar).isNotified())
			{
				((UIComponent)toolBar).removeNotify();
			}
		}
		else
		{
			owner.getUIResource().removeToolBar(toolBar);
		}
		
		toolbars.remove(pIndex);
		
		if (toolBar instanceof UIComponent)
		{
			toolBar.setParent(null);
		}
	}
	
	/**
	 * Removes a toolbar from this panel.
	 * 
	 * @param pToolBar the toolbar
	 */
	public void removeToolBar(IToolBar pToolBar)
	{
		if (pToolBar.getParent() == owner)
		{
			removeToolBar(indexOfToolBar(pToolBar));
		}
	}
	
	/**
	 * Gets the number of <code>IToolBar</code>s in this panel.
	 * 
	 * @return the number of toolbars
	 */
	public int getToolBarCount()
	{
		return toolbars.size();
	}
	
	/**
	 * Gets the {@link IToolBar} from a specific index.
	 *  
	 * @param pIndex the index
	 * @return the toolbar at <code>pIndex</code>
	 */
	public IToolBar getToolBar(int pIndex)
	{
		if (pIndex < 0 || pIndex >= toolbars.size())
		{
			throw new ArrayIndexOutOfBoundsException("No such child: " + pIndex);
		}
		
		return toolbars.get(pIndex);
	}
	
	/**
	 * Gets the n<sup>th</sup> position of an <code>IToolBar</code> in this panel.
	 * 
	 * @param pToolBar the <code>IToolBar</code> to search
	 * @return the n<sup>th</sup> position of <code>pToolBar</code> in this panel or
	 *         <code>-1</code> if <code>pToolBar</code> is not added
	 */
	public int indexOfToolBar(IToolBar pToolBar)
	{
		return toolbars.indexOf(pToolBar);
	}
	
	/**
	 * Sets the display area where the toolbars will be added.
	 * 
	 * @param pArea an area constant {@link IToolBarPanel#AREA_TOP}, {@link IToolBarPanel#AREA_LEFT}, 
	 *              {@link IToolBarPanel#AREA_BOTTOM}, {@link IToolBarPanel#AREA_RIGHT}
	 */
	public void setToolBarArea(int pArea)
	{
		owner.getUIResource().setToolBarArea(pArea);
	}

	/**
	 * Gets the area where the toolbar(s) are added.
	 * 
	 * @return an area constant {@link IToolBarPanel#AREA_TOP}, {@link IToolBarPanel#AREA_LEFT}, 
	 *         {@link IToolBarPanel#AREA_BOTTOM}, {@link IToolBarPanel#AREA_RIGHT}
	 */
	public int getToolBarArea()
	{
		return owner.getUIResource().getToolBarArea();
	}

	/**
	 * Sets all toolbars movable or fixed.
	 * 
	 * @param pMovable <code>true</code> to move the toolbars, <code>false</code> otherwise
	 */
	public void setToolBarMovable(boolean pMovable)
	{
		bMovable = pMovable;
		
		for (int i = 0, anz = getToolBarCount(); i < anz; i++)
		{
			getToolBar(i).setMovable(pMovable);
		}
	}
	
	/**
	 * Gets whether the toolbars are movable.
	 * 
	 * @return <code>true</code> if the toolbars are movable, <code>false</code> otherwise
	 */
	public boolean isToolBarMovable()
	{
		return bMovable;
	}
	
	/**
	 * Notification for updating the translation of all sub toolbars.
	 */	
	public void updateTranslation()
	{
		IComponent comp;
		
		//Update the translation for all sub components
		for (int i = 0, anz = toolbars.size(); i < anz; i++)
		{
			comp = toolbars.get(i);
			
			if (comp instanceof UIComponent)
			{
				((UIComponent)comp).updateTranslation();
			}
		}
	}

	/**
	 * Invoked before this component is added.
	 *  
	 * @param pParent the parent
	 */
	public void beforeAddNotify(IComponent pParent)
	{
		bBeforeNotified = true;
		
		IComponent comp;
		
		for (int i = 0, anz = toolbars.size(); i < anz; i++)
		{
			comp = toolbars.get(i);
			
			if (comp instanceof UIComponent && !((UIComponent)comp).isBeforeNotified())
			{
				((UIComponent)comp).beforeAddNotify(owner);
			}
		}
	}	
	
	/**
	 * Gets whether this component is notified before it was added.
	 *  
	 * @return <code>true</code> this component is notified, <code>false</code> otherwise
	 */
	public boolean isBeforeNotified()
	{
		return bBeforeNotified;
	}	
	
	/**
     * Notification to forward <code>addNotify</code> to all sub toolbars.
     */
	public void addNotify()
	{
		bNotified = true;
		
		IComponent comp;
		
		for (int i = 0, anz = toolbars.size(); i < anz; i++)
		{
			comp = toolbars.get(i);
			
			if (comp instanceof UIComponent && !((UIComponent)comp).isNotified())
			{
				((UIComponent)comp).addNotify();
			}
		}
	}
	
	/**
	 * Gets if the <code>InternalToolBarPanel</code> was notified about <code>addNotify</code>.
	 * 
	 * @return <code>true</code> if addNotify was called; <code>false</code> otherwise
	 */
	public boolean isNotified()
	{
		return bNotified;
	}

	/**
     * Notification to forward <code>removeNotify</code> to all sub toolbars.
     */
	public void removeNotify()
	{
		bBeforeNotified = false;
		bNotified = false;
		
		IComponent comp;
		
		for (int i = 0, anz = toolbars.size(); i < anz; i++)
		{
			comp = toolbars.get(i);
			
			if (comp instanceof UIComponent && ((UIComponent)comp).isNotified())
			{
				((UIComponent)comp).removeNotify();
			}
		}
	}
	
}	// InternalToolBarPanel
