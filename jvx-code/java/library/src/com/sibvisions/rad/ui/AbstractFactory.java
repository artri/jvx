/*
 * Copyright 2015 SIB Visions GmbH
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
 * 06.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import javax.rad.ui.IComponent;
import javax.rad.ui.IFactory;
import javax.rad.ui.IFactoryComponent;
import javax.rad.ui.IResource;
import javax.rad.ui.component.IButton;
import javax.rad.ui.component.ICheckBox;
import javax.rad.ui.component.IIcon;
import javax.rad.ui.component.ILabel;
import javax.rad.ui.component.IMap;
import javax.rad.ui.component.IPasswordField;
import javax.rad.ui.component.IPopupMenuButton;
import javax.rad.ui.component.IRadioButton;
import javax.rad.ui.component.ITextArea;
import javax.rad.ui.component.ITextField;
import javax.rad.ui.component.IToggleButton;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IGroupPanel;
import javax.rad.ui.container.IPanel;
import javax.rad.ui.container.IScrollPanel;
import javax.rad.ui.container.ISplitPanel;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.control.IChart;
import javax.rad.ui.control.IEditor;
import javax.rad.ui.control.IGauge;
import javax.rad.ui.control.ITable;
import javax.rad.ui.control.ITree;
import javax.rad.ui.menu.ICheckBoxMenuItem;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;
import javax.rad.ui.menu.ISeparator;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ResourceUtil;

/**
 * The {@link AbstractFactory} is an abstract implementation of {@link IFactory}
 * , which provides common functionality.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractFactory implements IFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The registered resource classes for all technologies. */
    private static Map<Class<IComponent>, Map<Class<?>, Class<?>>> registeredResourceClasses = 
            new HashMap<Class<IComponent>, Map<Class<?>, Class<?>>>();

    /** The factory logger. */
    private transient ILogger logger = null;

	/** The {@link Map} which holds the properties. */
	private Map<String, Object> properties = new HashMap<String, Object>();
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
	public <C extends IComponent, D extends C> void registerComponent(Class<C> pComponentInterface, Class<D> pComponentImplementation)
    {
        if (!IComponent.class.isAssignableFrom(pComponentInterface))
        {
            throw new IllegalArgumentException("The interface " + pComponentInterface.getName());
        }
       
        registerResource(pComponentInterface, pComponentImplementation);
    }

    /**
     * {@inheritDoc}
     */
    public <C extends IComponent> C createComponent(Class<C> pComponentInterface)
    {
        Class<C> pComponentImplementation = (Class<C>)getRegisteredResources().get(pComponentInterface);
        
        if (pComponentImplementation != null)
        {
            try
            {
                C componentInstance = pComponentImplementation.newInstance();
                
                if (componentInstance instanceof IFactoryComponent)
                {
                    ((IFactoryComponent)componentInstance).setFactory(this);
                }
                
                return componentInstance;
            }
            catch (Exception ex)
            {
                error(ex);
                throw new IllegalArgumentException("Creating a component for " + pComponentInterface.getName() + " failed in " + getClass().getSimpleName() + "!");
            }
        }
        
        Iterator<C> componentImplementations = ServiceLoader.load(pComponentInterface, ResourceUtil.getDefaultClassLoader()).iterator();
        
        while (componentImplementations.hasNext()) 
        {
            try
            {
                C componentInstance = componentImplementations.next();
            
                if (getComponentBaseClass().isInstance(componentInstance))
                {
                    if (componentInstance instanceof IFactoryComponent)
                    {
                        ((IFactoryComponent)componentInstance).setFactory(this);
                    }
                    
                    // For performance, register the component implementation class, for next time.
                    registerResource(pComponentInterface, (Class<C>)componentInstance.getClass());
                    
                    return componentInstance;
                }
            }
            catch (Throwable ex)
            {
                debug(ex);
            }
        }
        
        throw new IllegalArgumentException("There is no component registered for " + pComponentInterface.getName() + " in " + getClass().getSimpleName() + "!");
    }
    
	/**
	 * {@inheritDoc}
	 */
	public Object getProperty(String pName)
	{
		return properties.get(pName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setProperty(String pName, Object pValue)
	{
	    if (pValue == null)
	    {
	        properties.remove(pName);
	    }
	    else
	    {
	        properties.put(pName, pValue);
	    }
	}
	
    /**
     * {@inheritDoc}
     */
    public ILabel createLabel() 
    {
        return createComponent(ILabel.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public ITextField createTextField() 
    {
        return createComponent(ITextField.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public IPasswordField createPasswordField()
    {
        return createComponent(IPasswordField.class);
    }

    /**
     * {@inheritDoc}
     */
    public ITextArea createTextArea()
    {
        return createComponent(ITextArea.class);
    }       
    
    /**
     * {@inheritDoc}
     */
    public IIcon createIcon() 
    {
        return createComponent(IIcon.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public IButton createButton()
    {
        return createComponent(IButton.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public IToggleButton createToggleButton()
    {
        return createComponent(IToggleButton.class);
    }

    /**
     * {@inheritDoc}
     */
    public IPopupMenuButton createPopupMenuButton()
    {
        return createComponent(IPopupMenuButton.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public ICheckBox createCheckBox()
    {
        return createComponent(ICheckBox.class);
    }
   
    /**
     * {@inheritDoc}
     */
    public IRadioButton createRadioButton()
    {
        return createComponent(IRadioButton.class);
    }
   
    /**
     * {@inheritDoc}
     */
    public IPanel createPanel()
    {
        return createComponent(IPanel.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public IToolBarPanel createToolBarPanel()
    {
        return createComponent(IToolBarPanel.class);
    }

    /**
     * {@inheritDoc}
     */
    public IGroupPanel createGroupPanel()
    {
        return createComponent(IGroupPanel.class);
    }

    /**
     * {@inheritDoc}
     */
    public IScrollPanel createScrollPanel()
    {
        return createComponent(IScrollPanel.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public ISplitPanel createSplitPanel()
    {
        return createComponent(ISplitPanel.class);
    }

    /**
     * {@inheritDoc}
     */
    public ITabsetPanel createTabsetPanel()
    {
        return createComponent(ITabsetPanel.class);
    }

    /**
     * {@inheritDoc}
     */
    public IToolBar createToolBar()
    {
        return createComponent(IToolBar.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public IDesktopPanel createDesktopPanel()
    {
        return createComponent(IDesktopPanel.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public IMenuItem createMenuItem()
    {
        return createComponent(IMenuItem.class);
    }
   
    /**
     * {@inheritDoc}
     */
    public ICheckBoxMenuItem createCheckBoxMenuItem()
    {
        return createComponent(ICheckBoxMenuItem.class);
    }
   
    /**
     * {@inheritDoc}
     */
    public IMenu createMenu()
    {
        return createComponent(IMenu.class);
    }
   
    /**
     * {@inheritDoc}
     */
    public IMenuBar createMenuBar()
    {
        return createComponent(IMenuBar.class);
    }
   
    /**
     * {@inheritDoc}
     */
    public IPopupMenu createPopupMenu()
    {
        return createComponent(IPopupMenu.class);
    }

    /**
     * {@inheritDoc}
     */
    public ISeparator createSeparator()
    {
        return createComponent(ISeparator.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public IEditor createEditor()
    {
        return createComponent(IEditor.class);
    }
   
    /**
     * {@inheritDoc}
     */
    public ITable createTable()
    {
        return createComponent(ITable.class);
    }

    /**
     * {@inheritDoc}
     */
    public ITree createTree()
    {
        return createComponent(ITree.class);
    }

    /**
     * {@inheritDoc}
     */
    public IChart createChart()
    {
        return createComponent(IChart.class);
    }

    /**
     * {@inheritDoc}
     */
    public IGauge createGauge()
    {
        return createComponent(IGauge.class);
    }
	
    /**
     * {@inheritDoc}
     */
    public IMap createMap()
    {
        return createComponent(IMap.class);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Initializes this factory.
     * In this method all standard components has to be registered. 
     * This method can be overwritten in own implementations, and specific component replaced.
     */
    protected abstract void initFactory();

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the registered resources for the component base class of this factory.
     * 
     * @param <C> the resource class. 
     * @return the registered resources for the component base class of this factory.
     */
    protected <C extends IResource> Map<Class<?>, Class<?>> getRegisteredResources()
    {
        synchronized (registeredResourceClasses)
        {
            Map<Class<?>, Class<?>> resourceClasses = registeredResourceClasses.get(getComponentBaseClass());
            
            if (resourceClasses == null)
            {
                resourceClasses = new HashMap<Class<?>, Class<?>>();
                registeredResourceClasses.put(getComponentBaseClass(), resourceClasses);
                
                initFactory();
            }
            
            return resourceClasses;
        }
    }
    
    /**
     * Registers a resource implementation.
     * This is the base method, that is able to store all resource that can be instanciated with default constructor.
     *  
     * @param <C> the resource interface. 
     * @param <D> the resource implementation class. 
     * @param pResourceInterface the resource interface
     * @param pResourceImplementation the resource implementation
     */
    protected <C extends IComponent, D extends C> void registerResource(Class<C> pResourceInterface, Class<D> pResourceImplementation)
    {
        getRegisteredResources().put(pResourceInterface, pResourceImplementation);
    }
    
	/**
	 * Gets whether the property with the given name is enabled. A property is enabled
	 * if it's set to <code>true</code>, as {@link String} or {@link Boolean}.
	 * 
	 * @param pName the property name
	 * @return <code>true</code> if property is available and set to <code>true</code>, <code>false</code> otherwise
	 */
	protected boolean isPropertyEnabled(String pName)
	{
		Object oValue = properties.get(pName);
		
		if (oValue != null)
		{
			if (oValue instanceof Boolean)
			{
				return ((Boolean)oValue).booleanValue();
			}
			else
			{
				return Boolean.parseBoolean(oValue.toString());
			}
		}
		
		return false;
	}	
	
    /**
     * Logs debug information.
     * 
     * @param pInfo the debug information
     */
    public void debug(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(getClass());
        }
        
        logger.debug(pInfo);
    }

    /**
     * Logs information.
     * 
     * @param pInfo the information
     */
    public void info(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(getClass());
        }
        
        logger.info(pInfo);
    }
    
    /**
     * Logs error information.
     * 
     * @param pInfo the error information
     */
    public void error(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(getClass());
        }

        logger.error(pInfo);
    }
    
}	// AbstractFactory
