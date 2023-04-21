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
 * 18.03.2011 - [JR] - #314: border visibility support for TextField/Area
 * 25.08.2011 - [JR] - #465: access clipboard service and content 
 */
package com.sibvisions.rad.ui.swing.impl.component;

import java.awt.Color;

import javax.rad.ui.IColor;
import javax.rad.ui.component.ITextField;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import com.sibvisions.rad.ui.awt.impl.AwtColor;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.celleditor.PromptSupport;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.rad.ui.swing.impl.SwingScrollComponent;

/**
 * Platform and technology independent TextField definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @param <C> the added resource
 * @param <TC> the text component
 * 
 * @author Martin Handsteiner
 * @see	java.awt.TextField
 * @see	javax.swing.JTextField
 */
public abstract class SwingTextComponent<C extends JComponent, TC extends JTextComponent> extends SwingScrollComponent<C, TC> 
																						  implements ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the original component border. */
	private Border borComp = null;

	/** the original resource border. */
	private Border borRes = null;

	/** the placeholder. */
	private String sPlaceholder;
	
	/** original background. */
	private Color originalBackground;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingTextField</code>.
	 * 
	 * @param pComponent instance of the component
	 */
	protected SwingTextComponent(C pComponent)
	{
		super(pComponent);

		JVxUtil.installActions(component);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getText()
	{
		return component.getText();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
	{
		component.setText(pText);
		component.select(0, 0);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBackground(IColor pBackground)
    {
        super.setBackground(pBackground);
        
        if (!component.isEditable())
        {
            if (component.isBackgroundSet())
            {
                originalBackground = component.getBackground();
            }
            else
            {
                originalBackground = null;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IColor getBackground()
    {
        if (component.isEditable())
        {
            return super.getBackground();
        }
        else 
        {
            Color color = originalBackground;
            if (color == null && component.getParent() != null)
            {
                color = component.getParent().getBackground();
            }
            if (color == null)
            {
                return null;
            }
            else
            {
                return new AwtColor(originalBackground);
            }
        }
    }
    
	/**
     * {@inheritDoc}
     */
	@Override
    public boolean isBackgroundSet()
    {
        if (component.isEditable())
        {
            return super.isBackgroundSet();
        }
        else
        {
            return originalBackground != null;
        }
    }
	
    /**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean pEditable)
	{
	    if (component.isEditable())
	    {
	        if (component.isBackgroundSet())
	        {
	            originalBackground = component.getBackground();
	        }
	        else
	        {
                originalBackground = null;
	        }
	    }
	    
		component.setEditable(pEditable);
		
		if (pEditable)
		{
		    component.setBackground(originalBackground);
		}
		else if (originalBackground instanceof javax.swing.plaf.UIResource)
		{
		    Color readOnlyBackground = JVxUtil.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
		    
            if (SwingFactory.isMacLaF())
            {
                if (!(readOnlyBackground instanceof javax.swing.plaf.UIResource))
                {
	                if (resource != component)
	                {
                        resource.setBackground(readOnlyBackground);
                    }
	                
	                component.setBackground(readOnlyBackground);
                }
            }
            else
            {
                if (readOnlyBackground instanceof javax.swing.plaf.UIResource)
                {
                    readOnlyBackground = new Color(readOnlyBackground.getRed(), readOnlyBackground.getGreen(), readOnlyBackground.getBlue(), readOnlyBackground.getAlpha());
                    
                }
                
                component.setBackground(readOnlyBackground);
            }
            
		}
	}
	
    /**
	 * {@inheritDoc}
	 */
	public boolean isEditable()
	{
		return component.isEditable();
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void setBorderVisible(boolean pVisible)
    {
    	if (pVisible != isBorderVisible())
    	{
	    	if (pVisible)
	    	{
	    		resource.setBorder(borRes);
	    		component.setBorder(borComp);
	
	    		borRes  = null;
	    		borComp = null;
	    	}
	    	else
	    	{
	    		borRes  = resource.getBorder();
	    		borComp = component.getBorder();
	    		
	    		resource.setBorder(BorderFactory.createEmptyBorder());
	    		component.setBorder(BorderFactory.createEmptyBorder());
	    	}
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isBorderVisible()
    {
    	return borComp == null;
    }		

	/**
	 * {@inheritDoc}
	 */
    public void selectAll()
    {
    	component.selectAll();
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
    	
    	PromptSupport.setPrompt(component, sPlaceholder);
    } 

}	// SwingTextComponent
