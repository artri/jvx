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
 * 16.11.2008 - [HM] - creation
 * 04.06.2009 - [JR] - text translation
 * 08.06.2009 - [JR] - setEditable, setEnable -> translation handling 
 * 11.06.2009 - [JR] - setText: changed expression [BUGFIX]
 * 21.07.2009 - [JR] - updateTranslation: wrong expression [BUGFIX]
 * 18.03.2011 - [JR] - #314: border visibility support for TextField/Area
 * 15.04.2013 - [JR] - don't update translation if text is empty
 * 05.04.2014 - [JR] - #1001: don't change text if translation is disabled
 */
package javax.rad.genui.component;

import javax.rad.genui.UIComponent;
import javax.rad.ui.component.ITextField;

import com.sibvisions.util.type.StringUtil;

/**
 * Platform and technology independent text field.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @param <C> instance of ITextField
 * 
 * @author Martin Handsteiner
 * @see	java.awt.TextField
 * @see	javax.swing.JTextField
 */
public abstract class AbstractUITextField<C extends ITextField> extends UIComponent<C> 
                                                    implements ITextField
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the text. */
	private transient String sText = "";

	/** the placeholder. */
    private transient String sPlaceholder = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UITextField</code>.
     *
     * @param pTextField the ITextField.
     * @see ITextField
     */
	protected AbstractUITextField(C pTextField)
	{
		super(pTextField);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getText()
    {
		if (isEditable() && isEnabled())
		{
			return uiResource.getText();
		}
		else
		{
			return sText;
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
    {
		if (isEditable() && isEnabled())
		{
			sText = null;
			
	    	uiResource.setText(pText);
		}
		else
		{
			sText = pText;
			
	    	uiResource.setText(translate(pText));
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
    {
    	return uiResource.getHorizontalAlignment();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
    {
    	uiResource.setHorizontalAlignment(pHorizontalAlignment);
    }

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
    {
    	return uiResource.getVerticalAlignment();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
    {
    	uiResource.setVerticalAlignment(pVerticalAlignment);
    }
	
	/**
	 * {@inheritDoc}
	 */
    public int getColumns()
    {
    	return uiResource.getColumns();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setColumns(int pColumns)
    {
    	uiResource.setColumns(pColumns);
    }

	/**
	 * {@inheritDoc}
	 */
    public void setEditable(boolean pEditable)
    {
    	if (pEditable != isEditable())
    	{
	    	uiResource.setEditable(pEditable);
	
	    	if (bTranslate)
	    	{
    	    	if (isEditable() && isEnabled())
    	    	{
    	    		uiResource.setText(sText);
    	    	}
    	    	else
    	    	{
    	    		sText = uiResource.getText();
    	    		
        		    uiResource.setText(translate(sText));
    	    	}
	    	}
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isEditable()
    {
    	return uiResource.isEditable();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setBorderVisible(boolean pVisible)
    {
    	uiResource.setBorderVisible(pVisible);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public boolean isBorderVisible()
    {
    	return uiResource.isBorderVisible();
    }

	/**
	 * {@inheritDoc}
	 */
    public void selectAll()
    {
    	uiResource.selectAll();
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
        
    	uiResource.setPlaceholder(translate(pPlaceholder));
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void setEnabled(boolean pEnabled)
    {
    	if (pEnabled != isEnabled())
    	{
	    	super.setEnabled(pEnabled);
	
	    	if (bTranslate)
	    	{
    	    	if (isEditable() && isEnabled())
    	    	{
    	    		uiResource.setText(sText);
    	    	}
    	    	else
    	    	{
    	    		sText = uiResource.getText();
    	    		
    	    		uiResource.setText(translate(sText));
    	    	}
	    	}
    	}
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTranslation()
	{
		boolean bChanged = isTranslationChanged();

		super.updateTranslation();
		
		if (bTranslate && bChanged && (!isEditable() || !isEnabled()) && !StringUtil.isEmpty(sText))
		{			
			uiResource.setText(translate(sText));
		}
	}

}	// AbstractUITestField
