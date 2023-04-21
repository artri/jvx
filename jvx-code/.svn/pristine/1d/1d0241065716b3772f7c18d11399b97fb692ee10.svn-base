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
 * 01.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.impl.component;

import javax.rad.ui.component.ILabel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.sibvisions.rad.ui.swing.impl.SwingComponent;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.type.HttpUtil;

/**
 * The <code>SwingLabel</code> is the <code>ILabel</code>
 * implementation for swing. It displays an area for a short text 
 * string.<br> A <code>SwingLabel</code> does not react to input events. As a 
 * result, it cannot get the keyboard focus.
 * 
 * @author Martin Handsteiner
 * @see javax.swing.JLabel
 * @see javax.rad.ui.component.ILabel
 */
public class SwingLabel extends SwingComponent<JLabel> 
                        implements ILabel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Insets for the label to get correct size. */
	private static final Border BORDER = BorderFactory.createEmptyBorder(3, 1, 3, 1);
	
	/** The original set text. */
	private String sText;
	
	/** whether to automatically convert text to html. */
	private boolean bAutoHtml = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingLabel</code>.
	 */
	public SwingLabel()
	{
		super(new JLabel());
		
		resource.setBorder(BORDER);
		
		// set correct default values.
		super.setHorizontalAlignment(SwingFactory.getHorizontalAlignment(resource.getHorizontalAlignment()));
		super.setVerticalAlignment(SwingFactory.getVerticalAlignment(resource.getVerticalAlignment()));
		
		// Default is not opaque.
		resource.setBackground(null);
		
		sText = resource.getText();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		super.setHorizontalAlignment(pHorizontalAlignment);
		resource.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(pHorizontalAlignment));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		super.setVerticalAlignment(pVerticalAlignment);
		resource.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(pVerticalAlignment));
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
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
	    
	    if (bAutoHtml
	    	&& (pText != null 
	    	    && pText.length() > 0 
	    	    && (pText.length() < 6 || !"<html>".equalsIgnoreCase(pText.substring(0, 6).toLowerCase())) 
	            && (pText.indexOf(' ') >= 0 || pText.indexOf('\n') >= 0)))
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
	        
	        resource.setText(htmlText.toString());
	    }
	    else
	    {
	    	resource.setText(sText);
	    }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets whether to enable automatic text to html conversion.
	 * 
	 * @param pHtml <code>true</code> to convert text to html
	 */
	public void setAutomaticHtml(boolean pHtml)
	{
		bAutoHtml = pHtml;
		
		setText(sText);
	}
	
	/**
	 * Gets whether automatic text to html conversion is enabled.
	 * 
	 * @return <code>true</code> if automatic text to html conversion is enabled 
	 */
	public boolean isAutomaticHtml()
	{
		return bAutoHtml;
	}
	
}	// SwingLabel
