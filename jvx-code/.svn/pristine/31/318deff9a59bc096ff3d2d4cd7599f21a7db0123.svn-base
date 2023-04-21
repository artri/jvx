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
 * 19.10.2009 - [JR] - creation
 * 05.04.2014 - [JR] - #1001: don't change text if translation is disabled
 */
package javax.rad.genui.component;

import java.util.regex.Pattern;

import javax.rad.ui.component.IActionComponent;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.Key;
import javax.rad.util.EventHandler;

import com.sibvisions.util.type.StringUtil;

/**
 * Platform and technology independent action component.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * 
 * @param <C> instance of IActionComponent
 */
public abstract class AbstractUIActionComponent<C extends IActionComponent> extends AbstractUILabeledIcon<C> 
                                                                            implements IActionComponent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * The pattern used for testing if an action command could be a fully
	 * qualified classname.
	 * <p/>
	 * <code><pre>
	 * ^                At the start of the string
	 * [a-zA-Z]+        One or more letters
	 * (\\.[a-zA-Z]+)+  Followed by at least one group of a leading dot
	 *                  and one or more letters.
	 * $                Until the string ends.
	 * </pre></code>
	 */
	private static final Pattern FULLY_QUALIFIED_CLASSNAME_PATTERN = Pattern.compile("^[a-zA-Z]+(\\.[a-zA-Z]+)+$");
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractUIActionComponent</code>.
     *
     * @param pActionComponent the {@link IActionComponent}.
     * @see IActionComponent
     */
	protected AbstractUIActionComponent(C pActionComponent)
	{
		super(pActionComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public void setAccelerator(Key pKey)
    {
    	uiResource.setAccelerator(pKey);
    }

	/**
	 * {@inheritDoc}
	 */
    public Key getAccelerator()
    {
    	return uiResource.getAccelerator();
    }
    
	/**
	 * {@inheritDoc}
	 */
	public String getActionCommand()
    {
    	return uiResource.getActionCommand();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setActionCommand(String pActionCommand)
    {
    	uiResource.setActionCommand(pActionCommand);
    }

	/**
	 * {@inheritDoc}
	 */
	public ActionHandler eventAction()
	{
		return uiResource.eventAction();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createComponentName()
	{
		String name = getNameFromActionListeners();
		
		String actionCommand = getActionCommand();
		
		if (!StringUtil.isEmpty(actionCommand))
		{
			if (FULLY_QUALIFIED_CLASSNAME_PATTERN.matcher(actionCommand).matches())
			{
				actionCommand = StringUtil.getShortenedWords(actionCommand, 3);
				actionCommand = actionCommand.replace('.', '-');
			}
			
			if (!StringUtil.isEmpty(name))
			{
				name = name + "_" + actionCommand;
			}
			else
			{
				name = actionCommand;
			}
		}
		
		if (!StringUtil.isEmpty(name))
		{
			String rootPrefix = "";
			
			if (getRootName() != null)
			{
				rootPrefix = getRootName() + "_";
			}
			
			return incrementNameIfExists(rootPrefix + createSimplifiedClassName() + "_" + name.toUpperCase(), getExistingNames(), false);
		}
		else
		{
			return super.createComponentName();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the name of the method from the registered action listeners. Returns
	 * {@code null} if there is no suitable listener registered.
	 * 
	 * @return the name of method. {@code null} if there is no suitable listener registered.
	 */
	private String getNameFromActionListeners()
	{
	    IActionListener[] listeners = eventAction().getListeners();
	    
		for (int i = listeners.length - 1; i >= 0; i--)
		{
			String listenerMethod = EventHandler.extractInvokedMethodName(listeners[i]);

			if (listenerMethod != null)
			{
			    return listenerMethod;
			}
		}
		
		return null;
	}

}	// AbstractUIActionComponent
