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
 * 04.06.2009 - [JR] - add/removeNotify
 * 04.10.2011 - [JR] - #477: beforeAddNotify handling                     
 * 24.10.2012 - [JR] - #604: added constructor
 * 13.08.2013 - [JR] - #756: changed add order
 * 05.10.2013 - [JR] - #826: use add instead of addSeparator
 */
package javax.rad.genui.menu;

import java.util.ArrayList;
import java.util.List;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UILayout;
import javax.rad.genui.UIResource;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ILayout;
import javax.rad.ui.menu.IMenu;

/**
 * Platform and technology independent menu.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIMenu extends AbstractUIMenuItem<IMenu> 
                    implements IMenu
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The UIParent of this UICompoennt. */
	private transient ILayout uiLayout = null;
	
	/** List of subcomponents. */
	protected transient List<IComponent> components = new ArrayList<IComponent>(4);
	
	/** the flag indicates that addNotify is active. */
	private transient boolean bAddNotify = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
     * Creates a new instance of <code>UIMenu</code>.
     *
     * @see IMenu
     */
	public UIMenu()
	{
		super(UIFactoryManager.getFactory().createMenu());
	}

    /**
     * Creates a new instance of <code>UIMenu</code> with the given menu instance.
     *
     * @param pMenu the menu
     * @see IMenu 
     */
	protected UIMenu(IMenu pMenu)
	{
		super(pMenu);
	}

	/**
     * Creates a new instance of <code>UIMenu</code>.
     *
     *@param pText the text
     * @see IMenu
     */
	public UIMenu(String pText)
	{
		this();
		
		setText(pText);
	}

	/**
	 * Creates a new instance of {@link UIMenu}.
	 *
	 * @param pText the {@link String text}.
	 * @param pMenuItems the {@link IComponent menu items}.
	 * @see #add(IComponent)
	 * @see #setText(String)
	 */
	public UIMenu(String pText, IComponent... pMenuItems)
	{
		this();
		
		setText(pText);
		
		if (pMenuItems != null && pMenuItems.length > 0)
		{
			for (IComponent menuItem : pMenuItems)
			{
				add(menuItem);
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public ILayout getLayout()
    {
    	return uiLayout;
    }

	/**
	 * {@inheritDoc}
	 */
	public void setLayout(ILayout pLayout)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pLayout instanceof UILayout)
		{
	    	uiResource.setLayout((ILayout)((UILayout)pLayout).getUIResource());
		}
		else
		{
	    	uiResource.setLayout(pLayout);
		}
		uiLayout = pLayout;
    }

	/**
	 * {@inheritDoc}
	 */
	public void addSeparator()
    {
    	addSeparator(-1);
    }

	/**
	 * {@inheritDoc}
	 */
    public void addSeparator(int pIndex)
    {
    	UISeparator separator = new UISeparator();
    	
    	if (pIndex < 0)
    	{
        	components.add(separator);
    	}
    	else
    	{
        	components.add(pIndex, separator);
    	}
    	
    	separator.setParent(this);

    	try
    	{
    		uiResource.add(separator.getUIResource(), null, pIndex);
    	}
    	catch (RuntimeException re)
    	{
    		components.remove(separator);
    		separator.setParent(null);
    		
    		throw re;
    	}
    	catch (Error e)
    	{
    		components.remove(separator);
    		separator.setParent(null);
    		
    		throw e;
    	}
    	
    	if (isNotified() && !separator.isNotified())
    	{
    		separator.addNotify();
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent)
    {
		add(pComponent, -1);
    }

	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent, Object pConstraints)
	{
		add(pComponent, pConstraints, -1);
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(IComponent pComponent, int pIndex)
	{
		add(pComponent, null, pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void add(IComponent pComponent, Object pConstraints, int pIndex)
    {
    	if (pConstraints instanceof String)
    	{
    		IContainer parent = AbstractUIMenuItem.getMenu(this, (String)pConstraints);
        	
    		parent.add(pComponent, null, pIndex);
    	}
    	else
    	{
    		addIntern(pComponent, pConstraints, pIndex);
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void remove(int pIndex)
    {
    	uiResource.remove(pIndex);

		IComponent component = components.remove(pIndex);
		
		if (component instanceof UIComponent)
		{
			component.setParent(null);
			
			if (((UIComponent)component).isNotified())
			{
				((UIComponent)component).removeNotify();
			}
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public void remove(IComponent pComponent)
	{
		if (pComponent.getParent() == this)
		{
			remove(components.indexOf(pComponent));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAll()
	{
		while (components.size() > 0)
		{
			remove(components.size() - 1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getComponentCount()
	{
		return components.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public IComponent getComponent(int pIndex)
	{
		return components.get(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int indexOf(IComponent pComponent)
	{
		return components.indexOf(pComponent);
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
		super.updateTranslation();
		
		//updateTranslation will be called through addNotify -> don't call it more than once!
		if (bAddNotify)
		{
			return;
		}
		
		IComponent comp;
		
		//Update the translation for all sub components
		for (int i = 0, anz = components.size(); i < anz; i++)
		{
			comp = components.get(i);
			
			if (comp instanceof UIComponent)
			{
				//update all sub components with the right parent translation
				((UIComponent)comp).updateTranslation();
			}
		}
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
	
		IComponent comp;

		for (int i = 0, anz = components.size(); i < anz; i++)
		{
			comp = components.get(i);
			
			if (comp instanceof UIComponent && !((UIComponent)comp).isBeforeNotified())
			{
				((UIComponent)comp).beforeAddNotify(this);
			}
		}
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
	
		IComponent comp;

		for (int i = 0, anz = components.size(); i < anz; i++)
		{
			comp = components.get(i);
			
			if (comp instanceof UIComponent && !((UIComponent)comp).isNotified())
			{
				((UIComponent)comp).addNotify();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		super.removeNotify();

		IComponent comp;
		
		for (int i = 0, anz = components.size(); i < anz; i++)
		{
			comp = components.get(i);
			
			if (comp instanceof UIComponent && ((UIComponent)comp).isNotified())
			{
				((UIComponent)comp).removeNotify();
			}
		}
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Internal function, for adding physical component and supporting layers.
	 * 
	 * @param pComponent the <code>Component</code> to be added
	 * @param pConstraints an object expressing layout contraints for this
	 * @param pIndex the position in the container's list at which to insert
	 *        the <code>Component</code>; <code>-1</code> means insert at the end
	 *        component
	 * @see #add(IComponent, Object, int)
	 */
	public void addIntern(IComponent pComponent, Object pConstraints, int pIndex)
    {
		// Support both, UIComponents and IComponents
		IComponent component;
		if (pComponent instanceof UIComponent)
		{
			component = ((UIComponent<IComponent>)pComponent).getUIResource();
		}
		else
		{
			component = pComponent;
		}
		// Remove from UIContainer or from IContainer	
		if (pComponent.getParent() instanceof UIContainer)
		{
			((UIContainer)pComponent.getParent()).remove(pComponent);
		}
		else if (pComponent.getParent() != null)
		{
			pComponent.getParent().remove(component);
		}
		
		Object constraints;
		if (pConstraints instanceof UIResource)
		{
			constraints = ((UIResource)pConstraints).getUIResource();
		}
		else
		{
			constraints = pConstraints;
		}

		if (pComponent instanceof UIComponent)
		{
			if (isNotified() && !((UIComponent)pComponent).isBeforeNotified())
			{
				((UIComponent)pComponent).beforeAddNotify(this);
			}
		}		

		if (pIndex < 0)
		{
			components.add(pComponent);
		}
		else
		{
			components.add(pIndex, pComponent);
		}

		IContainer conOldParent = null;
		
		if (pComponent instanceof UIComponent)
		{
			conOldParent = pComponent.getParent();
			
			pComponent.setParent(this);
		}
		
		try
		{
			uiResource.add(component, constraints, pIndex);
		}
		catch (RuntimeException re)
		{
			components.remove(pComponent);
			
			if (pComponent instanceof UIComponent)
			{
				pComponent.setParent(conOldParent);
			}			
			
			throw re;
		}
		catch (Error e)
		{
			components.remove(pComponent);
			
			if (pComponent instanceof UIComponent)
			{
				pComponent.setParent(conOldParent);
			}			

			throw e;
		}
		
		if (pComponent instanceof UIComponent && isNotified() && !((UIComponent)pComponent).isNotified())
		{
			((UIComponent)pComponent).addNotify();
		}
    }

}	// UIMenu
