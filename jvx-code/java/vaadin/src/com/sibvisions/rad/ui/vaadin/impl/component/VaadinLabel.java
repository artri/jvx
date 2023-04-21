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
 * 16.10.2012 - [CB] - creation
 * 08.09.2013 - [JR] - support for HTML content
 * 16.04.2013 - [DJ] - #1237: mouse listener support added
 */
package com.sibvisions.rad.ui.vaadin.impl.component;

import javax.rad.ui.component.ILabel;
import javax.rad.ui.event.MouseHandler;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.ui.event.type.mouse.IMouseClickedListener;
import javax.rad.ui.event.type.mouse.IMousePressedListener;
import javax.rad.ui.event.type.mouse.IMouseReleasedListener;

import com.sibvisions.rad.ui.vaadin.ext.ui.ClickableLabel;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.util.type.HttpUtil;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.v7.shared.ui.label.ContentMode;

/**
 * The <code>VaadinLabel</code> is the vaadin implementation of <code>ILabel</code>.
 * It displays an area for a short text string.
 * 
 * @author Benedikt Cermak
 * @see com.vaadin.ui.Label
 */
public class VaadinLabel extends VaadinComponent<ClickableLabel> 
                         implements ILabel, ClickListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the original text. */
	private String sText;
	
	/** whether the mouse listener was added. */
    protected boolean bMouseListener = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VaadinLabel</code>.
	 */
	public VaadinLabel()
	{
		super(new ClickableLabel());
	}
	
	/**
	 * Creates a new instance of <code>VaadinLabel</code> with given text.
	 * 
	 * @param pText the label text.
	 */
	public VaadinLabel(String pText)
	{
		this();
		setText(pText);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getText() 
	{
		return sText;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText) 
	{
		sText = pText;
		
		if (pText != null)
		{
			if (isHtml(pText))
			{
				resource.setContentMode(ContentMode.HTML);
			}
			else if (pText.length() > 0 && (pText.indexOf(' ') >= 0 || pText.indexOf('\n') >= 0))
			{
		        int length = pText.length();
		        StringBuilder htmlText = new StringBuilder(pText.length() + 16);
		        
	            htmlText.append("<html>");
		        int start = 0;
		        while (start < length && pText.charAt(start) == ' ')
		        {
		            htmlText.append("&nbsp;");
		            start++;
		        }
		        int end = length - 1;
		        while (end > start && pText.charAt(end) == ' ')
		        {
		            end--;
		        }
		        end++;
		        htmlText.append(HttpUtil.escapeHtml(pText.substring(start, end)));
		        while (end < length)
		        {
		            htmlText.append("&nbsp;");
		            end++;
		        }
		            
		        pText = htmlText.toString();

		        resource.setContentMode(ContentMode.HTML);
			}
			else
			{
                resource.setContentMode(ContentMode.TEXT);
			}
		}
		else
		{
			resource.setContentMode(ContentMode.TEXT);
		}
        
        resource.setValue(pText);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void click(com.vaadin.event.MouseEvents.ClickEvent pEvent)
    {
        dispatchMouseEvent(eventMousePressed, pEvent, UIMouseEvent.MOUSE_PRESSED);
        dispatchMouseEvent(eventMouseClicked, pEvent, UIMouseEvent.MOUSE_CLICKED);
        dispatchMouseEvent(eventMouseReleased, pEvent, UIMouseEvent.MOUSE_RELEASED);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * {@inheritDoc}
     */
	@Override
    public MouseHandler<IMousePressedListener> eventMousePressed()
    {
        addMouseListener();
        
        return super.eventMousePressed();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public MouseHandler<IMouseReleasedListener> eventMouseReleased()
    {
        addMouseListener();
        
        return super.eventMouseReleased();
    }
    
	/**
     * {@inheritDoc}
     */
	@Override
    public MouseHandler<IMouseClickedListener> eventMouseClicked()
    {
	    addMouseListener();
	    
        return super.eventMouseClicked();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * Adds a mouse listener for the label, if not already added.
     */
    protected void addMouseListener()
    {
        if (!bMouseListener)
        {
            bMouseListener = true;
            resource.addClickListener(this);
        }
    }
    
}	// VaadinLabel
