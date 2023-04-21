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
 * 21.11.2008 - [JR] - creation
 * 17.02.2009 - [JR] - set/getCursor implemented
 * 20.02.2009 - [JR] - extend IMessageConstants
 * 15.04.2009 - [JR] - log methods implemented
 * 22.04.2009 - [JR] - removed log methods
 * 28.05.2009 - [JR] - don't implement getContentPane -> possible a problem with return null
 *                     let subclasses implement the method because they know what's to do!
 * 06.08.2009 - [JR] - added simple showMessage calls                     
 * 12.06.2010 - [JR] - show... now returns IContent 
 *                   - show... generic opener
 * 21.03.2013 - [JR] - #649: IApplicationSetup introduced and used       
 * 24.06.2014 - [JR] - #1078: getControllerProperty overwritten
 * 05.05.2015 - [JR] - #1379: default implementation of getContent and showMessage  
 * 24.01.2019 - [JR] - #1981: use the classloader from the application
 */
package javax.rad.application.genui;

import java.util.HashMap;
import java.util.Map;

import javax.rad.application.IApplication;
import javax.rad.application.IContent;
import javax.rad.application.ILauncher;
import javax.rad.application.genui.event.ApplicationHandler;
import javax.rad.application.genui.event.type.application.IParameterChangedListener;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.util.Parameter;

import com.sibvisions.util.Reflective;
import com.sibvisions.util.type.ResourceUtil;

/**
 * The <code>Application</code> is an abstract implementation of {@link IApplication}.
 * It only implements the methods for loading and creating classes.
 *  
 * @author René Jahn
 */
public abstract class Application extends ControllerContent 
								  implements IApplication
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the parameter name for the setup class. */
	public static final String PARAM_SETUP_CLASS = "Application.setup.classname";

	/** the launcher implementation. */
	private transient UILauncher launcher;
	
	/** the specific application setup. */
	private transient IApplicationSetup setup;
	
    /** The map which maps parameter names to {@link ApplicationHandler}s. */
    private transient Map<String, ApplicationHandler> hmpParameterChangedHandler;
    
	/** the parameter mapping. */
	private transient Map<String, Object> mapParameter;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Application</code> with a desired 
	 * launcher.
	 * 
	 * @param pLauncher the launcher of this application
	 */
	public Application(UILauncher pLauncher)
	{
		setLauncher(pLauncher);
		
		try
		{
			String sClass = pLauncher.getParameter(PARAM_SETUP_CLASS);
			
			if (sClass != null)
			{
				//#1981
				IApplicationSetup stpApp = (IApplicationSetup)Reflective.construct(ResourceUtil.getResourceClassLoader(this), sClass);
				
				applySetup(stpApp);
				
				setup = stpApp;
				
				debug("Apply setup: ", sClass);
			}
		}
		catch (Throwable th)
		{
			//no specific setup
			
			debug(th);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public UILauncher getLauncher()
	{
		return launcher;
	}

    /**
     * {@inheritDoc}
     */
    public <OP> IContent showMessage(OP pOpener, 
                                     int pIconType, 
                                     int pButtonType, 
                                     String pMessage, 
                                     String pOkAction, 
                                     String pCancelAction) throws Throwable
    {
        getLauncher().handleException(new Exception(pMessage));
        
        return null;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IContainer getApplicationPane()
	{
		return this;
	}

    /**
     * {@inheritDoc}
     */
	public IContainer getContentPane()
	{
	    return getApplicationPane();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object setParameter(String pName, Object pValue)
	{
		if (mapParameter == null)
		{
			mapParameter = new HashMap<String, Object>();
		}
		
		Object old = mapParameter.put(pName, pValue);
		
		fireParameterChanged(pName, old, pValue);
		
		return old;
	}
	
	/**
	 * Gets the value of an additional parameter from the application. If parameter is not set in
	 * the application, the launcher parameter will be returned.
	 * 
	 * @param pName the parameter name
	 * @return the parameter
	 * @see ILauncher#getParameter(String)
	 */
	public Object getParameter(String pName)
	{
		if (mapParameter != null && mapParameter.containsKey(pName))
		{
			return mapParameter.get(pName);
		}
		else if (launcher != null)
		{
			return launcher.getParameter(pName);
		}
		else
		{
		    return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isParameterSet(String pName)
	{
		if (mapParameter == null)
		{
			return false;
		}
		
		return mapParameter.containsKey(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object removeParameter(String pName)
	{
		if (mapParameter == null)
		{
			return null;
		}
		
        Object old = mapParameter.remove(pName);

        fireParameterChanged(pName, old, null);
		
		return old;
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the cursor of the launcher.
	 * 
	 * @param pCursor the cursor
	 */
	@Override
	public void setCursor(ICursor pCursor)
	{
		launcher.setCursor(pCursor);
	}
	
	/**
	 * Gets the cursor from the launcher.
	 * 
	 * @return the cursor of the launcher
	 */
	@Override
	public ICursor getCursor()
	{
		return launcher.getCursor();
	}
	
	/**
	 * Cancels all pending launcher threads.
	 */
	@Override
	public void notifyDestroy()
	{
	    if (launcher != null)
	    {
	        launcher.cancelPendingThreads();
	    }
	    
	    super.notifyDestroy();
	}
	
	/**
	 * Gets the current value of the given controller property. If the property was not set,
	 * the launcher parameter will be checked for a name with the prefix <code>Controller.</code>, e.g.
	 * Controller.name.
	 * 
	 * @param pName the name of the property
	 * @return the value or <code>null</code> if the property was not found
	 */
	@Override
    public Object getControllerProperty(String pName)
    {
	    Object oValue = super.getControllerProperty(pName);
	    
	    if (oValue == null)
	    {
	        return launcher.getParameter("Controller." + pName);
	    }
	    
	    return oValue;
    }	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the given parameter.
     * 
     * @param pParameter the parameter
     */
    @Deprecated
    public void setParameter(Parameter pParameter)
    {
        addParameter(pParameter);
    }
    
    /**
     * Adds the given parameter.
     * 
     * @param pParameter the parameter
     */
    public void addParameter(Parameter... pParameter)
    {
        if (pParameter != null)
        {
            for (Parameter param : pParameter)
            {
                setParameter(param.getName(), param.getValue());
            }
        }
    }
	
	/**
	 * Removes the given parameter.
	 * 
	 * @param pParameter the parameter
	 */
	public void removeParameter(Parameter... pParameter)
	{
		if (pParameter != null)
		{
            for (Parameter param : pParameter)
            {
                removeParameter(param.getName());
            }
		}
	}
	
	/**
	 * Gets whether the parameter is set.
	 * 
	 * @param pParameter the parameter
	 * @return <code>true</code> if parameter is set, <code>false</code> otherwise
	 */
	public boolean isParameterSet(Parameter pParameter)
	{
		if (pParameter != null)
		{
			return isParameterSet(pParameter.getName());
		}
		
		return false;
	}
	
	/**
	 * Shows a message with the {@link javax.rad.application.IMessageConstants#MESSAGE_ICON_INFO} icon and the
	 * {@link javax.rad.application.IMessageConstants#MESSAGE_BUTTON_OK} button.
	 * 
	 * @param <OP> the opener type
	 * @param pOpener the opener of the information
	 * @param pMessage the message/information to show
	 * @return the message content or <code>null</code> if the message has no content
	 * @throws Throwable if the message could not be initialized 
	 */
	public <OP> IContent showInformation(OP pOpener, String pMessage) throws Throwable
	{
		return showMessage(pOpener, MESSAGE_ICON_INFO, MESSAGE_BUTTON_OK, pMessage, null, null);
	}

	/**
	 * Shows a message with the {@link javax.rad.application.IMessageConstants#MESSAGE_ICON_INFO} icon and the
	 * {@link javax.rad.application.IMessageConstants#MESSAGE_BUTTON_OK} button.
	 * 
	 * @param <OP> the opener type
	 * @param pOpener the opener of the information
	 * @param pMessage the message/information to show
	 * @param pOkAction the action to call when OK was pressed
	 * @return the message content or <code>null</code> if the message has no content
	 * @throws Throwable if the message could not be initialized 
	 */
	public <OP> IContent showInformation(OP pOpener, String pMessage, String pOkAction) throws Throwable
	{
		return showMessage(pOpener, MESSAGE_ICON_INFO, MESSAGE_BUTTON_OK, pMessage, pOkAction, null);
	}

	/**
	 * Shows a message with the {@link javax.rad.application.IMessageConstants#MESSAGE_ICON_ERROR} icon and the
	 * {@link javax.rad.application.IMessageConstants#MESSAGE_BUTTON_OK} button.
	 * 
	 * @param <OP> the opener type
	 * @param pOpener the opener of the information
	 * @param pMessage the message/error to show
	 * @return the message content or <code>null</code> if the message has no content
	 * @throws Throwable if the message could not be initialized 
	 */
	public <OP> IContent showError(OP pOpener, String pMessage) throws Throwable
	{
		return showMessage(pOpener, MESSAGE_ICON_ERROR, MESSAGE_BUTTON_OK, pMessage, null, null);
	}
	
	/**
	 * Shows a message with the {@link javax.rad.application.IMessageConstants#MESSAGE_ICON_ERROR} icon and the
	 * {@link javax.rad.application.IMessageConstants#MESSAGE_BUTTON_OK} button.
	 * 
	 * @param <OP> the opener type
	 * @param pOpener the opener of the information
	 * @param pMessage the message/error to show
	 * @param pOkAction the action to call when OK was pressed
	 * @return the message content or <code>null</code> if the message has no content
	 * @throws Throwable if the message could not be initialized 
	 */
	public <OP> IContent showError(OP pOpener, String pMessage, String pOkAction) throws Throwable
	{
		return showMessage(pOpener, MESSAGE_ICON_ERROR, MESSAGE_BUTTON_OK, pMessage, pOkAction, null);
	}
	
	/**
	 * Shows a message with the {@link javax.rad.application.IMessageConstants#MESSAGE_ICON_QUESTION} icon and the
	 * {@link javax.rad.application.IMessageConstants#MESSAGE_BUTTON_YES_NO} buttons.
	 * 
	 * @param <OP> the opener type
	 * @param pOpener the opener of the information
	 * @param pMessage the message/question to show
	 * @param pOkAction the action to call when yes was pressed
	 * @return the message content or <code>null</code> if the message has no content
	 * @throws Throwable if the message could not be initialized 
	 */
	public <OP> IContent showQuestion(OP pOpener, String pMessage, String pOkAction) throws Throwable
	{
		return showMessage(pOpener, MESSAGE_ICON_QUESTION, MESSAGE_BUTTON_YES_NO, pMessage, pOkAction, null);
	}

	/**
	 * Shows a message with the {@link javax.rad.application.IMessageConstants#MESSAGE_ICON_QUESTION} icon and the
	 * {@link javax.rad.application.IMessageConstants#MESSAGE_BUTTON_YES_NO} buttons.
	 * 
	 * @param <OP> the opener type
	 * @param pOpener the opener of the information
	 * @param pMessage the message/question to show
	 * @param pOkAction the action to call when yes was pressed
	 * @param pCancelAction the action to call when no/x was pressed
	 * @return the message content or <code>null</code> if the message has no content
	 * @throws Throwable if the message could not be initialized 
	 */
	public <OP> IContent showQuestion(OP pOpener, String pMessage, String pOkAction, String pCancelAction) throws Throwable
	{
		return showMessage(pOpener, MESSAGE_ICON_QUESTION, MESSAGE_BUTTON_YES_NO, pMessage, pOkAction, pCancelAction);
	}

    /**
     * Shows a warning with the {@link javax.rad.application.IMessageConstants#MESSAGE_ICON_WARNING} icon and the
     * {@link javax.rad.application.IMessageConstants#MESSAGE_BUTTON_OK} button.
     * 
     * @param <OP> the opener type
     * @param pOpener the opener of the warning
     * @param pMessage the message/warning to show
     * @return the message content or <code>null</code> if the message has no content
     * @throws Throwable if the message could not be initialized 
     */
    public <OP> IContent showWarning(OP pOpener, String pMessage) throws Throwable
    {
        return showMessage(pOpener, MESSAGE_ICON_WARNING, MESSAGE_BUTTON_OK, pMessage, null, null);
    }	
	
    /**
     * Shows a warning with the {@link javax.rad.application.IMessageConstants#MESSAGE_ICON_WARNING} icon and the
     * {@link javax.rad.application.IMessageConstants#MESSAGE_BUTTON_OK} buttons.
     * 
     * @param <OP> the opener type
     * @param pOpener the opener of the warning
     * @param pMessage the message/warning to show
     * @param pOkAction the action to call when yes was pressed
     * @return the message content or <code>null</code> if the warning has no content
     * @throws Throwable if the message could not be initialized 
     */
    public <OP> IContent showWarning(OP pOpener, String pMessage, String pOkAction) throws Throwable
    {
        return showMessage(pOpener, MESSAGE_ICON_WARNING, MESSAGE_BUTTON_OK, pMessage, pOkAction, null);
    }
	
	/**
	 * Sets the launcher. Be careful with this method because it changes the behavior of
	 * the application if you use the wrong launcher.
	 * 
	 * @param pLauncher the launcher
	 */
	protected final void setLauncher(UILauncher pLauncher)
	{
	    if (launcher != null && pLauncher == null)
	    {
	        launcher.cancelPendingThreads();
	    }
	    
		launcher = pLauncher;
	}
	
	/**
	 * Gets the application setup, if used.
	 * 
	 * @return the application setup or <code>null</code> if no specific setup was used
	 */
	protected IApplicationSetup getSetup()
	{
		return setup;
	}

	/**
	 * Applies the given application setup.
	 * 
	 * @param pSetup the setup
	 */
	protected void applySetup(IApplicationSetup pSetup)
	{
	    pSetup.apply(this);
	}
	
    /**
     * Gets the event handler for the parameter changed event.
     * 
     * @return the event handler
     */
    public ApplicationHandler<IParameterChangedListener> eventParameterChanged()
    {
        return eventParameterChanged(null);
    }
    
    /**
     * Gets the event handler for the changed event of a specific parameter.
     * 
     * @param pName the connected parameter name
     * @return the event handler
     */
    public ApplicationHandler<IParameterChangedListener> eventParameterChanged(String pName)
    {
        if (hmpParameterChangedHandler == null)
        {
            hmpParameterChangedHandler = new HashMap<String, ApplicationHandler>();
        }
        
        ApplicationHandler handler = hmpParameterChangedHandler.get(pName);
        
        if (handler == null)
        {
            handler = new ApplicationHandler<IParameterChangedListener>(IParameterChangedListener.class);
            
            hmpParameterChangedHandler.put(pName, handler);
        }
        
        return handler;        
    }
    
    /**
     * Notifies all parameter changed listeners about a change.
     * 
     * @param pName the parameter name
     * @param pOld the old value
     * @param pNew the new value
     */
    protected void fireParameterChanged(String pName, Object pOld, Object pNew)
    {
        if (hmpParameterChangedHandler != null)
        {
            if (hmpParameterChangedHandler.containsKey(null))
            {
                hmpParameterChangedHandler.get(null).dispatchEvent(this, pName, pOld, pNew);
            }
            
            if (hmpParameterChangedHandler.containsKey(pName))
            {
                hmpParameterChangedHandler.get(pName).dispatchEvent(this, pName, pOld, pNew);
            }
        }
    }
	
}	// Application
