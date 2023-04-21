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
 * 30.01.2013 - [SW] - creation
 * 21.11.2013 - [JR] - isWindowAlreadyAttached: checked if ui is null
 */
package com.sibvisions.rad.ui.vaadin.ext;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;

import com.sibvisions.rad.ui.vaadin.ext.FontResource.StyleProperty;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicCss;
import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinDateCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinNumberCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinTextCellEditor;
import com.vaadin.server.Extension;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.AlignmentInfo.Bits;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * The <code>VaadinUtil</code> is a utility class for easier Vaadin UI handling.
 * 
 * @author Stefan Wurm
 */
public final class VaadinUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the default number cell editor. */
	private static final ICellEditor NUMBER_CELL_EDITOR = new VaadinNumberCellEditor(); 
	
	/** the default number cell editor. */
	private static final ICellEditor DATE_CELL_EDITOR = new VaadinDateCellEditor(); 
	
	/** the default number cell editor. */
	private static final ICellEditor TEXT_CELL_EDITOR = new VaadinTextCellEditor(); 
	
	/** Width or height undefined.	 */
	public static final float SIZE_UNDEFINED = -1;
	
	/** The default cell editors. */
	private static Hashtable<Class<?>, ICellEditor> defaultCellEditors = new Hashtable<Class<?>, ICellEditor>();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static 
	{
		defaultCellEditors.put(Number.class, NUMBER_CELL_EDITOR);
		defaultCellEditors.put(Date.class, DATE_CELL_EDITOR);
		defaultCellEditors.put(String.class, TEXT_CELL_EDITOR);	
	}
	
	/**
	 * Invisible constructor, because the <code>VaadinUtil</code> class is a 
	 * utility class.
	 */
	protected VaadinUtil()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Gets the default <code>ICellEditor</code> for the given class. This function should always return an editor.
     *
     * @param pClass the class type to be edited
     * @return the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static ICellEditor getDefaultCellEditor(Class<?> pClass)
    {
    	if (pClass == null)
    	{
    		return TEXT_CELL_EDITOR;
    	}
    	else
    	{
    		ICellEditor cellEditor = defaultCellEditors.get(pClass);
    		
    		if (cellEditor == null)
    		{
    			return getDefaultCellEditor(pClass.getSuperclass());
    		}
    		else
    		{
    			return cellEditor;
    		}
    	}
    }
    
    /**
     * Sets the default <code>ICellEditor</code> for the given class. This function should always return an editor.
     * It should look for best matching editor with Class.isAssignableFrom. If the given ICellEditor is null, 
     * it is removed as editor for the given class.
     *
     * @param pClass the class type to be edited
     * @param pCellEditor the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static void setDefaultCellEditor(Class<?> pClass, ICellEditor pCellEditor)
    {
    	if (pCellEditor == null)
    	{
    		defaultCellEditors.remove(pClass);
    	}
    	else
    	{
    		defaultCellEditors.put(pClass, pCellEditor);
    	}
    }
    
    /**
     * Returns the {@link CssExtension} for the given component.
     * 
     * @param pComponent the component for the extension
     * @return the css extension or <code>null</code> if <code>pComponent</code> is null
     */
    public static CssExtension getCssExtension(AbstractComponent pComponent)
    {
		CssExtension cssExtension = null;
		
		if (pComponent != null)
		{	
			Iterator<Extension> iterator = pComponent.getExtensions().iterator();
			
			while (iterator.hasNext())
			{
				Extension extension = iterator.next();		
				
				if (extension instanceof CssExtension)
				{
					return (CssExtension) extension;
				}
			}
		}
		
		if (cssExtension == null && pComponent != null)
		{
			cssExtension = new CssExtension();
			cssExtension.extend(pComponent);
		}
		
		return cssExtension;
    }
    
    /**
     * Returns true if the given window is already attached to the vaadin UI.
     * 
     * @param pWindow the window
     * @return true if the window is attached to the vaadin UI
     */
    public static boolean isWindowAlreadyAttached(Window pWindow)
    {
    	UI ui = UI.getCurrent();
    	
    	if (ui != null)
    	{
	    	for (Window windowAttached : ui.getWindows())
	    	{
	    		if (windowAttached.equals(pWindow))
	    		{
	    			return true;
	    		}
	    	}
    	}
    	
    	return false;
    }
        
    /**
     * Gets whether the width of the component is 100 and the unit is PERCENTAGE.
     * 
     * @param pComponent the component
     * @return <code>true</code> if the width of the component is 100 and the unit is PERCENTAGE
     */
    public static boolean isWidthFull(Component pComponent)
    {
    	if (pComponent.getWidth() == 100 && pComponent.getWidthUnits() == Unit.PERCENTAGE)
    	{
    		return true;
    	}
    	
    	return false;   	
    }
    
    /**
     * Gets whether the height of the component is 100 and the unit is PERCENTAGE.
     * 
     * @param pComponent the component
     * @return <code>true</code> if the height of the component is 100 and the unit is PERCENTAGE.
     */
    public static boolean isHeightFull(Component pComponent)
    {
    	if (pComponent.getHeight() == 100 && pComponent.getHeightUnits() == Unit.PERCENTAGE)
    	{
    		return true;
    	}
    	
    	return false;   	
    }    
    
    /**
     * Gets whether the width and height of the component is 100 and the unit is PERCENTAGE.
     * 
     * @param pComponent the component
     * @return <code>true</code> if the width and height of the component is 100 and the unit is PERCENTAGE.
     */
    public static boolean isSizeFull(Component pComponent)
    {
    	return isWidthFull(pComponent) && isHeightFull(pComponent);
    }    
    
    /**
     * Sets the component width with the given width and unit. If the width and unit from the component 
     * is the same as the given width and unit nothing is changed.
     * 
     * @param pComponent the component
     * @param pWidth the new width
     * @param pUnit the new unit
     * @return <code>true</code> if width has changed, <code>false</code> otherwise
     */
    public static boolean setComponentWidth(Component pComponent, float pWidth, Unit pUnit)
    {
    	if (pComponent.getWidth() != pWidth
    		|| pComponent.getWidthUnits() != pUnit)
    	{
    		pComponent.setWidth(pWidth, pUnit);
    		
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Sets the component height with the given height and unit. If the height and unit 
     * from the component is the same as the given height and unit nothing is changed.
     * 
     * @param pComponent the component
     * @param pHeight the new height
     * @param pUnit the new unit
     * @return <code>true</code> if height has changed, <code>false</code> otherwise
     */
    public static boolean setComponentHeight(Component pComponent, float pHeight, Unit pUnit)
    {
    	if (pComponent.getHeight() != pHeight
    		|| pComponent.getHeightUnits() != pUnit)
    	{
    		pComponent.setHeight(pHeight, pUnit);
    		
    		return true;
    	}
    	
    	return false;
    }   
    
    /**
     * Gets whether the width is UNDEFINED or the unit is PERCENTAGE.
     * 
     * @param pComponent the component
     * @param percentageIsUndefined if percentage values should be used as undefined
     * @return true if the width is UNDEFINED or the unit is PERCENTAGE
     */
    public static boolean isWidthUndefined(Component pComponent, boolean percentageIsUndefined)
    {
		if (pComponent.getWidth() == SIZE_UNDEFINED
    		|| (percentageIsUndefined && pComponent.getWidthUnits() == Unit.PERCENTAGE))
    	{
    			return true;
    	}  
		
		return false;
    }
    
    /**
     * Gets whether the height is UNDEFINED or the unit is PERCENTAGE.
     * 
     * @param pComponent the component
     * @param percentageIsUndefined if percentage values should be used as undefined
     * @return <code>true</code> if the height is UNDEFINED or the unit is PERCENTAGE
     */
    public static boolean isHeightUndefined(Component pComponent, boolean percentageIsUndefined)
    {
		if (pComponent.getHeight() == SIZE_UNDEFINED
    		|| (percentageIsUndefined && pComponent.getHeightUnits() == Unit.PERCENTAGE))
    	{
    			return true;
    	}  
		
		return false;
    }     
    
    /**
     * Returns true if the parent height of the component is defined in pixels or 
     * defined in percentage.
     * 
     * @param pComponent the component
     * @return true if the parent height is defined. 
     */
    public static boolean isParentHeightDefined(Component pComponent)
    {
    	if (pComponent != null)
    	{
    		Component parent = pComponent.getParent();
    		
	    	if (parent != null)
	    	{
	    		if ((parent instanceof VaadinUI && VaadinUI.isServletMode()) 
	    			|| (parent.getHeightUnits() == Unit.PIXELS && parent.getHeight() >= 0) 
	    		 	|| (parent.getHeightUnits() == Unit.PERCENTAGE && parent.getHeight() == 100)
	    		 	|| (parent.getHeightUnits() == Unit.PICAS && parent.getHeight() == Float.POSITIVE_INFINITY))
	    		{
	    			return true;
	    		}
	    	}
    	}

    	return false;    	
    }
    
    /**
     * Returns true if the parent width of the component is defined in pixels or 
     * defined in percentage.
     * 
     * @param pComponent the component
     * @return true if the parent width is defined. 
     */
    public static boolean isParentWidthDefined(Component pComponent)
    {
    	if (pComponent != null)
    	{
    		Component parent = pComponent.getParent();
    		
	    	if (parent != null)
	    	{
	    		if (parent instanceof VaadinUI 
	    			|| (parent.getWidthUnits() == Unit.PIXELS && parent.getWidth() >= 0) 
	    		 	|| (parent.getWidthUnits() == Unit.PERCENTAGE && parent.getWidth() == 100))
	    		{
	    			return true;
	    		}
	    	}
    	}

    	return false;  	
    }
    
    /**
     * Returns true if the parent of the given component is null.
     * 
     * @param component the component to test.
     * @return true if the parent of the given component is null.
     */
    public static boolean isParentNull(IComponent component)
    {
		if (((Component)component.getResource()).getParent() == null)
		{
			return true;
		}
		
		return false;
    }
    
	/**
	 * Returns the alignment for the vaadin component.
	 * 
	 * @param pHorizontalAlignment the horizontal alignment.
	 * @param pVerticalAlignment the vertical alignment.
	 * @return the com.vaadin.ui.Alignment
	 */
	public static com.vaadin.ui.Alignment getVaadinAlignment(int pHorizontalAlignment, int pVerticalAlignment)
	{
		int alignment = 0;
		
		if (pVerticalAlignment == IAlignmentConstants.ALIGN_TOP)
		{
			alignment = Bits.ALIGNMENT_TOP;
		}
		else if (pVerticalAlignment == IAlignmentConstants.ALIGN_BOTTOM)
		{
			alignment = Bits.ALIGNMENT_BOTTOM;
		}
		else
		{
			alignment = Bits.ALIGNMENT_VERTICAL_CENTER;
		}
		
		
		if (pHorizontalAlignment == IAlignmentConstants.ALIGN_LEFT)
		{
			alignment |= Bits.ALIGNMENT_LEFT;
		}
		else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_RIGHT)
		{
			alignment |= Bits.ALIGNMENT_RIGHT;
		}
		else
		{
			alignment |= Bits.ALIGNMENT_HORIZONTAL_CENTER;
		}
		
		return  new com.vaadin.ui.Alignment(alignment);
	}    

	/**
	 * Removes custom styles from given resource, if it's a {@link FontResource}.
	 * 
	 * @param pCss the Css handler
	 * @param pResource the (font) resource
	 */
	public static void removeFontIconStyles(IDynamicCss pCss, Resource pResource)
	{
	    if (pResource != null && pResource instanceof FontResource)
	    {
	        FontResource fres = (FontResource)pResource;
	        
            StyleProperty[] styles = fres.getCustomStyleProperties();
            
            if (styles != null)
            {
                CssExtension cssExtension = pCss.getCssExtension();
                cssExtension.removeAllAttributes();
            }
	    }
	}
	
    /**
     * Adds custom styles to the given resource, if it's a {@link FontResource}.
     * 
     * @param pCss the Css handler
     * @param pResource the (font) resource
     * @param pStyleName the TAG element which should be used/searched 
     */
	public static void applyFontIconStyles(IDynamicCss pCss, Resource pResource, String pStyleName)
	{
        if (pResource instanceof FontResource)
        {
            FontResource fres = (FontResource)pResource;
            
            StyleProperty[] styles = fres.getCustomStyleProperties();
            
            if (styles != null)
            {
                CssExtension cssExtension = pCss.getCssExtension();
                cssExtension.removeAllAttributes();
                
                for (int i = 0; i < styles.length; i++)
                {
                    cssExtension.addAttribute(new CssExtensionAttribute(styles[i].getName(), styles[i].getValue(), pStyleName, CssExtensionAttribute.SEARCH_DOWN));
                }
            }
            
            if (pCss instanceof Component)
            {
                Component comp = (Component)pCss;
            
                if (fres.isMapped())
                {
                    comp.addStyleName("imagemapping");
                }
                else
                {
                    comp.removeStyleName("imagemapping");
                }
            }
        }
	}
	
}	// VaadinUtil
