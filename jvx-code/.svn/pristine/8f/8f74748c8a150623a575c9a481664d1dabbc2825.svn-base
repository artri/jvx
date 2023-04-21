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
 * 23.07.2015 - [JR] - auto-scroll feature
 * 18.08.2017 - [JR] - keep horizontal scroll position
 */
package com.sibvisions.rad.ui.swing.impl.component;

import java.awt.Insets;

import javax.rad.ui.component.ITextArea;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.sibvisions.rad.ui.swing.ext.JVxScrollPane;
import com.sibvisions.rad.ui.swing.ext.WrappedInsetsBorder;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>SwingTextArea</code> is the <code>ITextArea</code>
 * implementation for swing.
 *  
 * @author Martin Handsteiner
 * @see	ITextArea
 * @see	javax.swing.JTextArea
 */
public class SwingTextArea extends SwingTextComponent<JVxScrollPane, JTextArea> 
						   implements ITextArea
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   
	/** the reference text field. */
	private static JTextField tfRef = new JTextField();
	
	/** the border. */
	private static WrappedInsetsBorder border;
	
    /** whether text should autmatically scroll to the end. */
    private boolean bAutoScroll = false;
    
    /** whether to keep horizontal scroll position after autoscroll. */
    private boolean bKeepHScrollPosition = false;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
		border = new WrappedInsetsBorder(tfRef.getBorder());
		border.setPaintInsets(new Insets(0, 0, 0, 0));
    }
    
	/**
	 * Creates a new instance of <code>SwingTextArea</code>.
	 */
	public SwingTextArea()
	{
		super(new JVxScrollPane(new JTextArea()));
		
		component.setFont(new JTextField().getFont());

		component.setWrapStyleWord(true);
		
		component.setColumns(12);
		component.setRows(5);

		if (SwingFactory.isMacLaF())
		{
			component.setBorder(null);
			resource.setBorder(border);		
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
	 * {@inheritDoc}
	 */
    public int getColumns()
    {
    	return component.getColumns();
    }

    /**
	 * {@inheritDoc}
	 */
    public void setColumns(int pColumns)
    {
    	component.setColumns(pColumns);
    }

    /**
	 * {@inheritDoc}
	 */
	public int getRows()
	{
		return component.getRows();
	}

    /**
	 * {@inheritDoc}
	 */
	public void setRows(int pRows)
	{
		component.setRows(pRows);
	}

    /**
	 * {@inheritDoc}
	 */
	public boolean isWordWrap()
	{
		return component.getLineWrap();
	}

    /**
	 * {@inheritDoc}
	 */
	public void setWordWrap(boolean pWordWrap)
	{
		component.setLineWrap(pWordWrap);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String pText)
	{
	    super.setText(pText);
	    
	    autoScroll();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets automatic scrolling on text change en/disabled.
	 * 
	 * @param pAutoScroll <code>true</code> to enable auto-scrolling on text change, <code>false</code> otherwise
	 */
	public void setAutoScroll(boolean pAutoScroll)
	{
	    bAutoScroll = pAutoScroll;
	}
	
	/**
	 * Gets whether automatic scrolling on text change is en/disabled.
	 * 
	 * @return <code>true</code> if enabled, <code>false</code> otherwise
	 */
	public boolean isAutoScroll()
	{
	    return bAutoScroll;
	}
	
	/**
	 * Sets whether the horizontal position should be kept after auto scroll.
	 * 
	 * @param pKeepPosition <code>true</code> to keep the position, <code>false</code> otherwise
	 */
	public void setKeepHorizontalScrollPosition(boolean pKeepPosition)
	{
	    bKeepHScrollPosition = pKeepPosition;
	}
	
    /**
     * Gets whether the horizontal position will be kept after auto scroll.
     * 
     * @return <code>true</code> if position will be kept, <code>false</code> otherwise
     */
	public boolean isKeepHorizontalScrollPosition()
	{
	    return bKeepHScrollPosition;
	}
	
	/**
	 * Automatic scrolling to the end of the text, if enabled.
	 * 
	 * @see #setAutoScroll(boolean)
	 */
	private void autoScroll()
	{
	    if (bAutoScroll)
	    {
	        final int iValue = resource.getHorizontalScrollBar().getValue();
	        
    	    component.getCaret().setDot(component.getText().length());
    	    component.scrollRectToVisible(component.getVisibleRect());

    	    if (bKeepHScrollPosition)
    	    {
        	    SwingUtilities.invokeLater(new Runnable()
                {
        	        public void run()
        	        {
        	            JScrollBar bar = resource.getHorizontalScrollBar();
        	            
        	            if (bar.getMaximum() >= iValue)
        	            {
        	                bar.setValue(iValue);
        	            }
        	        }
                });
    	    }
	    }	    
	}

}	// SwingTextArea
