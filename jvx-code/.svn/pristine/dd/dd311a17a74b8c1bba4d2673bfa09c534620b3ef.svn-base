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
 * 08.10.2012 - [CB] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.component.ITextArea;

import com.sibvisions.util.type.StringUtil;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.TextArea;

/**
 * The <code>VaadinTextArea</code> class is the vaadin implementation of {@link ITextArea}.
 * 
 * @author Benedikt Cermak
 */
public class VaadinTextArea extends AbstractVaadinTextField<TextArea>
                            implements ITextArea
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** Rows of the text area. */
    private int rows;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinTextArea</code>.
     *
     * @see javax.rad.ui.component.ITextArea
     */
	public VaadinTextArea()
	{
		super(new TextArea());
		
		resource.setWordWrap(false);
		rows = resource.getRows();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
    public void setText(String pText)
    {
        super.setText(pText);
        
        if (rows == 0)
        {
            setRows(0);
        }
        if (getColumns() == 0)
        {
            setColumns(0);
        }
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public int getRows()
    {
    	return rows;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setRows(int pRows)
    {
        if (pRows < 0)
        {
            rows = 0;
        }
        else
        {
            rows = pRows;
        }
        if (rows == 0)
        {
            resource.setRows(StringUtil.countCharacter(getText(), '\n') + 1);
        }
        else
        {
            resource.setRows(rows);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setColumns(int pColumns)
    {
        super.setColumns(pColumns);
        if (pColumns <= 0)
        {
            String text = getText();
            int maxChars = 0;
            
            if (text != null)
            {
                int lastPos = 0;
                int nextPos = text.indexOf('\n');
                
                while (nextPos >= 0)
                {
                    if (nextPos - lastPos > maxChars)
                    {
                        maxChars = nextPos - lastPos;
                    }
                    lastPos = nextPos + 1;
                    nextPos = text.indexOf('\n', lastPos);
                }
                nextPos = text.length();
                if (nextPos - lastPos > maxChars)
                {
                    maxChars = nextPos - lastPos;
                }
            }
            maxChars += 10;
            
            resource.setWidth(maxChars / 2, Unit.EM);
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	public boolean isWordWrap()
	{
		return resource.isWordWrap();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWordWrap(boolean pWordWrap)
	{
		resource.setWordWrap(pWordWrap);
	}

}	// VaadinTextArea
