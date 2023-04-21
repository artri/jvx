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
 * 24.10.2012 - [JR] - #604: added constructor
 * 05.04.2014 - [JR] - #1001: don't change text if translation is disabled
 */
package javax.rad.genui.component;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.control.UIEditor;
import javax.rad.model.IDataBook;
import javax.rad.ui.IComponent;
import javax.rad.ui.IFont;
import javax.rad.ui.component.ILabel;

import com.sibvisions.util.type.StringUtil;

/**
 * Platform and technology independent Label.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Label
 * @see	javax.swing.JLabel
 */
public class UILabel extends UIComponent<ILabel> 
                     implements ILabel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the text. */
	private transient String sText = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UILabel</code>.
     *
     * @see ILabel
     */
	public UILabel()
	{
		this(UIFactoryManager.getFactory().createLabel());
	}
	
    /**
     * Creates a new instance of <code>UILabel</code> with the given
     * labe.
     *
     * @param pLabel the label
     * @see ILabel
     */
	protected UILabel(ILabel pLabel)
	{
		super(pLabel);
		
		setVerticalAlignment(ALIGN_TOP);
	}
	
	/**
     * Creates a new instance of <code>UILabel</code>.
     *
     * @param pText the text.
     * @see ILabel
     */
	public UILabel(String pText)
	{
		this();
		
		setText(pText);
	}
	
    /**
     * Creates a new instance of <code>UILabel</code>.
     *
     * @param pText the text.
     * @param pFont the font.
     * @see ILabel
     */
	public UILabel(String pText, IFont pFont)
	{
		this();
		
		setText(pText);
		setFont(pFont);
	}
	
	/**
	 * Creates a new instance of {@link UILabel}.
	 *
	 * @param pText the {@link String text}.
	 * @param pFont the {@link IFont font}.
	 * @param pHorizontalAlignment the horizontal alignment.
	 * @param pVerticalAlignment the vertical alignment.
	 * @see #setFont(IFont)
	 * @see #setHorizontalAlignment(int)
	 * @see #setText(String)
	 * @see #setVerticalAlignment(int)
	 */
	public UILabel(String pText, IFont pFont, int pHorizontalAlignment, int pVerticalAlignment)
	{
		this();
		
		setText(pText);
		setFont(pFont);
		setHorizontalAlignment(pHorizontalAlignment);
		setVerticalAlignment(pVerticalAlignment);
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
		
    	uiResource.setText(translate(pText));
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
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTranslation()
	{
		boolean bChanged = isTranslationChanged();

		super.updateTranslation();
		
		if (bTranslate && bChanged)
		{
			uiResource.setText(translate(sText));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createComponentName()
	{
		if (parent != null)
		{
			int index = parent.indexOf(this);
			
			if (index >= 0 && index < parent.getComponentCount() - 1)
			{
				IComponent nextComponent = parent.getComponent(index + 1);
				
				if (nextComponent instanceof UIEditor)
				{
					UIEditor nextEditor = (UIEditor) nextComponent;
					
					if (nextEditor.getDataRow() instanceof IDataBook && !StringUtil.isEmpty(nextEditor.getColumnName()))
					{
						IDataBook dataBook = (IDataBook) nextEditor.getDataRow();
						
						String name = createComponentNamePrefix() + "_" + dataBook.getName() + "_" + nextEditor.getColumnName();
						name = incrementNameIfExists(name, getExistingNames(), false);
						
						return name;
					}
				}
			}
		}
		
		return super.createComponentName();
	}
	
}	// UILabel
