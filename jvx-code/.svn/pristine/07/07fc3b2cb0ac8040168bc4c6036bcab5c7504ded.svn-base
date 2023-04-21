/*
 * Copyright 2012 SIB Visions GmbH
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
 * 17.10.2012 - [CB] - creation
 * 19.11.2013 - [JR] - #847: check null values and set empty string instead of null
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.component.ITextField;

import com.sibvisions.rad.ui.vaadin.impl.VaadinFeaturedComponent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractTextField;

/**
 * The <code>AbstractVaadinTextField</code> is the base class for all vaadin implementation of {@link ITextField}.
 * 
 * @author Benedikt Cermak
 * @param <C> an instance of {@link AbstractTextField}
 */
public abstract class AbstractVaadinTextField<C extends AbstractTextField> extends VaadinFeaturedComponent<C>
                                                                           implements ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the columns. */
    private int iColumns = 0;

    /** whether the border around the text field is visible. **/
	private boolean borderVisible = true;
	
	/** the placeholder. */
	private String sPlaceholder;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractVaadinTextField</code>.
     *
     * @param pComponent an instance of {@link AbstractTextField}
     * @see ITextField
     */
	public AbstractVaadinTextField(C pComponent)
	{
		super(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getText() 
	{
		return resource.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText) 
	{
		String sText;
		
		if (pText == null)
		{
			sText = "";
		}
		else
		{
			sText = pText;
		}
		
		// If resource is readonly there is a ReadOnlyException when setText!
		if (resource.isReadOnly())
		{
			resource.setReadOnly(false);
			
			try
			{
				resource.setValue(sText);
			}
			finally
			{
				resource.setReadOnly(true);
			}
		}
		else
		{
			resource.setValue(sText);
		}
		
		if (iColumns == 0)
		{
		    setColumns(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getColumns() 
	{
	    return iColumns;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setColumns(int pColumns) 
	{
	    //DON'T set the columns to the resource -> would damage the layout (e.g. About, Login)
	    if (pColumns <= 0)
	    {
	        iColumns = 0;

            String text = getText();
            int maxChars = 0;
            
            if (text != null)
            {
                maxChars = text.length();
            }
            maxChars += 10;
            
            resource.setWidth(maxChars / 2, Unit.EM);
	    }
	    else
	    {
	        iColumns = pColumns;
	        
	        resource.setWidth(pColumns, Unit.EM);
	    }
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean pEditable) 
	{
		resource.setReadOnly(!pEditable);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEditable() 
	{
		return !resource.isReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBorderVisible(boolean pVisible) 
	{
		if (pVisible)  
		{
			removeInternStyleName("v-textfield-noborder");
		} 
		else 
		{
			addInternStyleName("v-textfield-noborder");			
		}
		
		borderVisible = pVisible;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderVisible() 
	{
		return borderVisible;
	}

	/**
	 * {@inheritDoc}
	 */
	public void selectAll() 
	{
		resource.selectAll();
	}
	
	/**
	 * {@inheritDoc}
	 */
    public String getPlaceholder()
    {
    	return sPlaceholder;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setPlaceholder(String pPlaceholder)
    {
    	sPlaceholder = pPlaceholder;
    	
    	resource.setPlaceholder(sPlaceholder);
    } 	

}	// AbstractVaadinTextField
