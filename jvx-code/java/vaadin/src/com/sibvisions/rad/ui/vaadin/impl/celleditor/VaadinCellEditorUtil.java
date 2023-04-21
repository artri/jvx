/*
 * Copyright 2015 SIB Visions GmbH
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
 * 13.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.Style;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.impl.feature.FeatureUtil;
import com.sibvisions.rad.ui.vaadin.impl.feature.IFeature;
import com.vaadin.ui.Component;

/**
 * The {@link VaadinCellEditorUtil} encapsulates common functionality used for
 * the cell editors.
 * 
 * @author Robert Zenz
 */
public final class VaadinCellEditorUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** Style for editor not nullable. */
	public static final String STYLE_EDITOR_NOT_NULLABLE = "editor-not-nullable";

    /**
	 * Invisible constructor because <code>VaadinCellEditorUtil</code> is a utility class.
	 */
	private VaadinCellEditorUtil()
	{
		// No instance required.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds style names to the given component. The style definition is used after style names.
	 * 
	 * @param pComponent the component
	 * @param pStyle the style definition
	 * @param pStyleNames the style names
	 */
	public static void addStyleNames(Component pComponent, Style pStyle, String... pStyleNames)
	{
		if (pComponent != null)
		{
			if (pStyleNames != null && pStyleNames.length > 0)
			{	
				pComponent.addStyleNames(pStyleNames);
			}
			
			if (pStyle != null)
			{
				String[] sStyles = pStyle.getStyleNames();
				
				if (sStyles != null && sStyles.length > 0)
				{
					pComponent.addStyleNames(sStyles);
				}
			}
		}
	}
	
	/**
	 * Removes style names to the given component.
	 * 
	 * @param pComponent the component
	 * @param pStyle the style definition
	 * @param pStyleNames the style names
	 */
	public static void removeStyleNames(Component pComponent, Style pStyle, String...pStyleNames)
	{
		if (pComponent != null)
		{
			if (pStyleNames != null && pStyleNames.length > 0)
			{	
				pComponent.removeStyleNames(pStyleNames);
			}
			
			if (pStyle != null)
			{
				String[] sStyles = pStyle.getStyleNames();
				
				if (sStyles != null && sStyles.length > 0)
				{
					pComponent.removeStyleNames(sStyles);
				}
			}
		}
	}
	
	/**
	 * Adds additional styles to the given component.
	 * 
	 * @param pComponent the component
	 * @param pColumnDefinition the column definition
	 */
	public static void applyAdditionalStyles(Component pComponent, ColumnDefinition pColumnDefinition)
	{
	    String styles = pComponent.getStyleName();

	    if (pColumnDefinition.isNullable())
		{
		    if (styles.contains(STYLE_EDITOR_NOT_NULLABLE))
		    {
		        pComponent.removeStyleName(STYLE_EDITOR_NOT_NULLABLE);
		    }
		}
		else if (!styles.contains(STYLE_EDITOR_NOT_NULLABLE))
		{
		    pComponent.addStyleName(STYLE_EDITOR_NOT_NULLABLE);
		}
	}
	
	/**
	 * Apply Features of given cell editor handler. The method takes care of used control. The control
	 * overrules the used cell editor.
	 * 
	 * @param pHandler the cell editor handler
	 */
	public static void applyFeature(IVaadinCellEditorHandler pHandler)
	{
		ICellEditor ced = pHandler.getCellEditor();
		ICellEditorListener listener = pHandler.getCellEditorListener();

		if (ced instanceof IFeature)
		{
			IControl ctrl = listener.getControl();

			if (ctrl instanceof IFeature)
			{
				FeatureUtil.apply(pHandler, (IFeature)ctrl, (IFeature)ced);
			}
			else
			{
				FeatureUtil.apply(pHandler, (IFeature)ced);
			}
		}		
	}
	
	/**
	 * Returns the horizontal css style.
	 * 
	 * @param pHorizontalAlignment the horizontal alignment
	 * @return CssExtensnioAttribute
	 */
	public static CssExtensionAttribute getHorizontalAlignCssAttribute(int pHorizontalAlignment)
	{
		CssExtensionAttribute styleAttribute = new CssExtensionAttribute();
		styleAttribute.setAttribute("text-align");
		
		if (pHorizontalAlignment == IAlignmentConstants.ALIGN_RIGHT)
		{
			styleAttribute.setValue("right");
		}
		else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_LEFT)
		{
			styleAttribute.setValue("left");
		}
		else
		{
			styleAttribute.setValue("center");
		}
		
		return styleAttribute;
	}
	
	/**
	 * Returns the vertical css style.
	 * 
	 * @param pVerticalAlignment the vertical alignment
	 * @return CssExtensnioAttribute
	 */
	public static CssExtensionAttribute getVerticalAlignCssAttribute(int pVerticalAlignment)
	{
		CssExtensionAttribute styleAttribute = new CssExtensionAttribute();
		styleAttribute.setAttribute("vertical-align");
		
		if (pVerticalAlignment == IAlignmentConstants.ALIGN_TOP)
		{
			styleAttribute.setValue("top");
		}
		else if (pVerticalAlignment == IAlignmentConstants.ALIGN_BOTTOM)
		{
			styleAttribute.setValue("bottom");
		}
		else
		{
			styleAttribute.setValue("middle");
		}
		
		return styleAttribute;
	}
	
}	// VaadinCellEditorUtil
