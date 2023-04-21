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
 * 23.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.Style;

import com.sibvisions.rad.ui.celleditor.AbstractImageViewer;
import com.sibvisions.rad.ui.vaadin.ext.INamedResource;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinIcon;
import com.sibvisions.rad.ui.vaadin.impl.control.ICellFormatterEditorListener;
import com.sibvisions.util.ArrayUtil;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontIcon;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;

/**
 * The <code>VaadinImageViewer</code> class is the vaadin implementation of {@link javax.rad.ui.celleditor.IImageViewer}.
 * 
 * @author Stefan Wurm
 */
public class VaadinImageViewer extends AbstractImageViewer<Component> 
                               implements ICellRenderer<Component>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
     * Creates a new instance of <code>VaadinImageViewer</code>.
     *
     * @see javax.rad.ui.celleditor.IImageViewer
     */	
	public VaadinImageViewer()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	public IVaadinCellEditorHandler<Component> createCellEditorHandler(ICellEditorListener pCellEditorListener, 
			                                                           IDataRow pDataRow, String pColumnName)
	{
		return new CellEditorHandler(this, (ICellFormatterEditorListener) pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Component getCellRendererComponent(Component pAvailabelComponent, 
						                              IDataPage pDataPage, 
						                              int pRowNumber, 
						                              IDataRow pDataRow, 
						                              String pColumnName, 
						                              boolean pIsSelected, 
						                              boolean pHasFocus)
	{
		// TODO isPreserveAspectRatio is not regarded here.
		// It would be ideally here if we could be using VaadinIcon, but that is
		// not easily possible because VaadinIcon is Ext/Impl rolled into one.
		// Ideally we would separate VaadinIcon first and then use the Ext
		// component here only.
		// Additionally, we can't use VaadinIcon here easily because the parent
		// does not have a fixed height set, see VaadinIcon.setFullWidth().
		SimplePanel iconPanel = null;
		
		if (pAvailabelComponent instanceof SimplePanel)
		{
			iconPanel = (SimplePanel)pAvailabelComponent;
		}
		else
		{
			iconPanel = new SimplePanel();
            iconPanel.setSizeFull();
			
            VaadinCellEditorUtil.addStyleNames(iconPanel, getStyle(), "cursor-hand", "jvximagecell", "renderer");
            
            if (pDataPage instanceof IDataBook)
            {
                iconPanel.addLayoutClickListener(new LayoutClickListener()
                {
                    public void layoutClick(LayoutClickEvent event)
                    {
                        try
                        {
                            ((IDataBook)pDataPage).setSelectedRow(pRowNumber);
                        }
                        catch (ModelException e)
                        {
                            // Ignore
                        }
                    }
                });
            }
		}
		
		Component comp = iconPanel.getContent();
		Image image;
		if (comp instanceof Image)
		{
			image = (Image)comp;
		}
		else
		{
			image = new Image();
			iconPanel.setContent(image);
		}
		
		CssExtension cssLabelExtension = VaadinUtil.getCssExtension(iconPanel);

		cssLabelExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(getHorizontalAlignment()));
		cssLabelExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(getVerticalAlignment()));

		try
		{
			Object value = pDataRow.getValue(pColumnName);
			VaadinImage vaadinImage = null;
			
			if (value instanceof String)
			{
				vaadinImage = VaadinImage.getImage((String) value); 
			}
			else if (value instanceof byte[])
			{
				vaadinImage = VaadinImage.getImage("image" + System.identityHashCode(value), (byte[]) value); 
			}
			else
			{
				vaadinImage = VaadinImage.getImage(sDefaultImageName);
			}

			if (vaadinImage != null)
			{
				INamedResource resource = vaadinImage.getResource();
	            if (resource instanceof FontIcon)
	            {
	                //works without caption!
	                image.setIcon(resource);
	                
	                //reset source, otherwise the image will be shown
	                image.setSource(null);
	            }
	            else if (resource != null)
	            {
	                //reset source, otherwise the icon/caption will be shown
	                image.setIcon(null);
	
	                //don't use setIcon because the caption will be shown!
	                image.setSource(resource);
	            }
	            else
	            {
	            	iconPanel.setContent(null);
	            }
			}
			else
			{
            	iconPanel.setContent(null);
			}
			
			return iconPanel;
		}
		catch (Exception pException)
		{
			iconPanel.setContent(null);
		}

		return iconPanel;
	}

	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
     * The <code>CellEditorHandler</code> class sets the internal changed flag, and informs the 
     * {@link ICellEditorListener} if editing is completed.
     * 
     * @author Stefan wurm
     */
    public static class CellEditorHandler implements IVaadinCellEditorHandler<Component>
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private VaadinImageViewer cellEditor;
    	
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
        /** The CellEditorListener to inform, if editing is completed. */
        private VaadinIcon cellEditorComponent;

    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
        /** the css extension. */
        private CssExtension cssExtension;
        
    	/** The attributes extension. */
    	private AttributesExtension attExtension;

    	/** The column name of the edited column. */
    	private String sColumnName;
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Creates a new instance of <code>CellEditorHandler</code>.
    	 * 
    	 * @param pCellEditor the CellEditor that created this handler.
    	 * @param pCellEditorListener CellEditorListener to inform, if editing is started or completed.
    	 * @param pDataRow the data row that is edited.
    	 * @param pColumnName the column name of the edited column.
    	 */
    	public CellEditorHandler(VaadinImageViewer pCellEditor, ICellFormatterEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		sColumnName = pColumnName;
			
    		cellEditorComponent = new VaadinIcon();
            cellEditorComponent.setImage(VaadinImage.getImage(cellEditor.getDefaultImageName()));
            
            Style style = pCellEditor.getStyle();
            String[] sStyles = style.getStyleNames();
            
            sStyles = ArrayUtil.addAll(sStyles, new String[] {"jvximagecell", "editor"});
            
            cellEditorComponent.setStyle(new Style(sStyles));

			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
			{	// use alignment of editors, if possible.
				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
            }
			else
			{
                cellEditorComponent.setHorizontalAlignment(cellEditor.getHorizontalAlignment());
                cellEditorComponent.setVerticalAlignment(cellEditor.getVerticalAlignment());
			}
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * {@inheritDoc}
    	 */
    	public void uninstallEditor()
    	{
            Style style = cellEditorComponent.getStyle();
            
            String[] sStyles = style.getStyleNames();
            
            sStyles = ArrayUtil.removeAll(sStyles, cellEditor.getStyle().getStyleNames());
            sStyles = ArrayUtil.removeAll(sStyles, new String[] {"jvximagecell", "editor"});
            
            cellEditorComponent.setStyle(new Style(sStyles));
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public ICellEditor getCellEditor()
    	{
    		return cellEditor;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public ICellEditorListener getCellEditorListener()
    	{
    		return cellEditorListener;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public IDataRow getDataRow()
    	{
    		return dataRow;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public String getColumnName()
    	{
    		return sColumnName;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public AbstractComponent getCellEditorComponent()
    	{
    		return cellEditorComponent.getResource();
    	}
    	
        /**
         * {@inheritDoc}
         */
        public CssExtension getCssExtension()
        {
            if (cssExtension == null)
            {
                cssExtension = new CssExtension();
                cssExtension.extend(getCellEditorComponent());
            }
            
            return cssExtension;
        }
        
        /**
         * {@inheritDoc}
         */
        public AttributesExtension getAttributesExtension()
        {
        	if (attExtension == null)
        	{
        		attExtension = new AttributesExtension();
        		attExtension.extend(getCellEditorComponent());
        	}
        	
        	return attExtension;
        }         

    	/**
    	 * {@inheritDoc}
    	 */
    	public void saveEditing() throws ModelException
    	{
    		// Its only a viewer!
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void cancelEditing() throws ModelException
    	{    		
    		try 
    		{
                ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(sColumnName);
                
    			// Must be set every time. jvxeditor style is set every cancel call.
    			cellEditorComponent.getIconResource().setStyleName("jvxicon"); // no jvxeditor style. use jvxicon style to set display: table
    			
    			final Object value = dataRow.getValue(sColumnName);

    			if (value instanceof String)
    			{
    				cellEditorComponent.setImage(VaadinImage.getImage((String) value)); 
    			}
    			else if (value instanceof byte[])
    			{
    		        cellEditorComponent.setImage(VaadinImage.getImage("image" + System.identityHashCode(value), (byte[]) value)); 
    			}
    			else
    			{
    				cellEditorComponent.setImage(VaadinImage.getImage(cellEditor.sDefaultImageName));
    			}

                VaadinCellEditorUtil.applyAdditionalStyles(cellEditorComponent.getResource(), columnDef);
                
                if (dynamicAlignment != null)
                {
                    int hAlign = dynamicAlignment.getHorizontalAlignment();
                    
                    if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
                    {
                        hAlign = cellEditor.getHorizontalAlignment();
                    }
                    
                    cellEditorComponent.setHorizontalAlignment(hAlign);
                    
                    int vAlign = dynamicAlignment.getVerticalAlignment();
                    
                    if (vAlign == IAlignmentConstants.ALIGN_DEFAULT)
                    {
                        vAlign = cellEditor.getVerticalAlignment();
                    }
                    
                    cellEditorComponent.setVerticalAlignment(vAlign);
                }

    			cellEditorComponent.setPreserveAspectRatio(cellEditor.isPreserveAspectRatio());
    		}
    		catch (Exception pException)
    		{
    			cellEditorComponent.setImage(VaadinImage.getImage(cellEditor.sDefaultImageName));
    			
    			throw new ModelException("Editor cannot be restored!", pException);
    		}    		
    	}
    	
		/**
		 * Sets the width for the the editor.
		 * An editor consists of Panel, Panel, Component like TextField, DateField, ....
		 * 
		 * @param pWidth the width
		 * @param pUnit the unit: PIXELS, PERCENTAGE
		 */
		public void setWidth(float pWidth, Unit pUnit)
		{
			if (pWidth >= 0 && pUnit == Unit.PIXELS)
			{
				cellEditorComponent.setWidth(pWidth);	
			}
			else if (pWidth == VaadinUtil.SIZE_UNDEFINED)
			{
				cellEditorComponent.setWidthUndefined();			
			}
			else if (pWidth == 100 && pUnit == Unit.PERCENTAGE)
			{
				cellEditorComponent.setWidthFull();					
			}
		}
		
		/**
		 * Sets the height for the the editor.
		 * An editor consists of Panel, Panel, Component like TextField, DateField, ....
		 * 
		 * @param pHeight the height
		 * @param pUnit the unit: PIXELS, PERCENTAGE
		 */
		public void setHeight(float pHeight, Unit pUnit)
		{
			if (pHeight >= 0 && pUnit == Unit.PIXELS)
			{
				cellEditorComponent.setHeight(pHeight);
			}
			else if (pHeight == VaadinUtil.SIZE_UNDEFINED)
			{
				cellEditorComponent.setHeightUndefined();	
			}
			else if (pHeight == 100 && pUnit == Unit.PERCENTAGE)
			{
				cellEditorComponent.setHeightFull();					
			}
		}	    	

    }	// CellEditorHandler	
    
} // VaadinImageViewer
