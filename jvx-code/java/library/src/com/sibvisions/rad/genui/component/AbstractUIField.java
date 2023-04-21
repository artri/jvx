/*
 * Copyright 2013 SIB Visions GmbH
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
 * 28.10.2022 - [HM] - creation
 */
package com.sibvisions.rad.genui.component;

import javax.rad.genui.UIComponent;
import javax.rad.genui.control.UIEditor;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.ui.control.IEditor;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.rad.model.mem.DataRow;

/**
 * The {@link AbstractUIField} is the base for stand-alone editor which acts like a
 * {@link UIEditor} but does not need to be bound against an
 * {@link javax.rad.model.IDataRow}.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractUIField extends UIComponent<IEditor>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The name of the one and only column. */
	private static final String COLUMN_NAME = "VALUE";
	
	/** The {@link IDataRow} that is used for the {@link UIEditor}. */
	private transient IDataRow dataRow = new DataRow();
	
	/** The action event handler. */
	private transient ActionHandler eventAction = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractUIField}.
	 */
	public AbstractUIField()
	{
		super(new UIEditor());
		
		dataRow = new DataRow();
		
		try
		{
			dataRow.getRowDefinition().addColumnDefinition(new ColumnDefinition(COLUMN_NAME, createDataType()));
			
			dataRow.eventValuesChanged().addListener(this, "dispatchActionEvent");
			
			getUIResource().setDataRow(dataRow);
			getUIResource().setColumnName(COLUMN_NAME);
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates the data type to use.
	 * 
	 * @return the data type.
	 */
	protected abstract IDataType createDataType();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the value.
	 * 
	 * @return the value.
	 */
	public Object getValue()
	{
		try
		{
		    dataRow.saveEditingControls();

		    return dataRow.getValue(COLUMN_NAME);
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
		}
		
		return null;
	}
	
    /**
     * Gets the value as string.
     * 
     * @return the value as string.
     */
    public String getValueAsString()
    {
        try
        {
            dataRow.saveEditingControls();

            return dataRow.getValueAsString(COLUMN_NAME);
        }
        catch (ModelException e)
        {
            ExceptionHandler.raise(e);
        }
        
        return null;
    }
    
	/**
	 * Sets the value.
	 * 
	 * @param pValue the new value.
	 */
	public void setValue(Object pValue)
	{
	    boolean dispatchEnabled = false;
	    if (eventAction != null && eventAction.isDispatchEventsEnabled())
	    {
	        dispatchEnabled = true;
	        eventAction.setDispatchEventsEnabled(false);
	    }
		try
		{
		    dataRow.cancelEditingControls();
		    
			dataRow.setValue(COLUMN_NAME, pValue);
		}
		catch (ModelException e)
		{
			ExceptionHandler.raise(e);
		}
		finally
		{
	        if (dispatchEnabled)
	        {
	            eventAction.setDispatchEventsEnabled(true);
	        }
		}
	}

	/**
	 * The action event is fired, if a value is changed in editor.
	 * Calling setValue will not trigger an action event.
	 * 
	 * @return the action event handler.
	 */
	public ActionHandler eventAction()
	{
	    if (eventAction == null)
	    {
	        eventAction = new ActionHandler();
	    }
	    
	    return eventAction;
	}

	/**
	 * Dispatches an action event.
	 */
	public void dispatchActionEvent()
	{
	    if (eventAction != null && eventAction.isDispatchable())
	    {
	        eventAction.dispatchEvent(new UIActionEvent(this, UIActionEvent.ACTION_PERFORMED, System.currentTimeMillis(), 0, null));
	    }
	}

}	// AbstractUIField
