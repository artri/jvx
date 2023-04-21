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
 * 12.08.2013 - [JR] - used VaadinImage for resource creation
 * 02.11.2015 - [JR] - lazy initialization of images
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.rad.ui.celleditor.AbstractChoiceCellEditor;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.ChoiceField;
import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.rad.ui.vaadin.impl.container.FocusForwardingSimplePanel;
import com.sibvisions.rad.ui.vaadin.impl.control.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTable;
import com.sibvisions.util.type.CommonUtil;
import com.vaadin.event.Action;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

/**
 * The <code>VaadinChoiceCellEditor</code> class is the vaadin implementation of {@link javax.rad.ui.celleditor.IChoiceCellEditor}.
 * 
 * @author Stefan Wurm
 */
public class VaadinChoiceCellEditor extends AbstractChoiceCellEditor<Component>
                                    implements ICellRenderer<Component>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The default image. */
	private Resource defaultImage;
	
	/** The images list. **/
	private Resource[] images;
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
     * Creates a new instance of <code>VaadinChoiceCellEditor</code>.
     *
     * @see javax.rad.ui.celleditor.IChoiceCellEditor
     */
	public VaadinChoiceCellEditor()
	{
		this(null, null);
	}

	/**
	 * Creates a new instance of <code>VaadinChoiceCellEditor</code> with the given allowed values and image names.
	 * 
	 * @param pAllowedValues the allowed values.
	 * @param pImageNames the image names.
	 */
	public VaadinChoiceCellEditor(Object[] pAllowedValues, String[] pImageNames)
	{
		super(pAllowedValues, pImageNames);
		
		setHorizontalAlignment(ALIGN_CENTER);
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * {@inheritDoc}
	 */
	public IVaadinCellEditorHandler<Component> createCellEditorHandler(ICellEditorListener pCellEditorListener, 
			                                                           IDataRow pDataRow, 
			                                                           String pColumnName)
	{
		return new CellEditorHandler(this, (ICellFormatterEditorListener) pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Component getCellRendererComponent(final Component pAvailabelComponent,
											  final IDataPage pDataPage, 
											  final int pRowNumber,
											  final IDataRow pDataRow,
											  final String pColumnName,
											  boolean pIsEditable,
											  boolean pHasFocus)
	{
		SimplePanel panel = null;
		
		try
		{
		    ChoiceField choiceField;
		    
			if (pAvailabelComponent instanceof SimplePanel)
			{
				panel = (SimplePanel)pAvailabelComponent;
				
				choiceField = (ChoiceField)panel.getContent();
			}
			else
			{
				panel = new SimplePanel();
				
                choiceField = new ChoiceField();
                choiceField.setTabIndex(-2);
                choiceField.addClickListener(new ChoiceFieldClickListener(pDataPage, pRowNumber, pColumnName));

                VaadinCellEditorUtil.addStyleNames(choiceField, getStyle(), "jvxchoicecell", "renderer");

                panel.setContent(choiceField);

                if (pDataPage instanceof IDataBook)
                {
                    panel.addLayoutClickListener(new LayoutClickListener() 
                    {
                        public void layoutClick(LayoutClickEvent pEvent)
                        {
                            if (pEvent.getChildComponent() == null || !((ChoiceField)pEvent.getChildComponent()).isEnabled())
                            {
                                try
                                {
                                    ((IDataBook)pDataPage).setSelectedRow(pRowNumber);
                                }
                                catch (ModelException e)
                                {
                                    //Nothing todo
                                }
                            }
                        }
                    });
                }
			}
			
			choiceField.setAllowedValues(getAllowedValues());
			choiceField.setImages(getImages());
			choiceField.setDefaultImage(getDefaultImage());
			Object value = pDataRow.getValue(pColumnName);
			if (!CommonUtil.equals(choiceField.getSelectedItem(), value))
			{
				choiceField.setSelectedItem(value);
			}

			boolean readonly = !pIsEditable || pDataPage.getDataBook().isReadOnly() || !pDataPage.getDataBook().isUpdateEnabled()
       			    || pDataRow.getRowDefinition().getColumnDefinition(pColumnName).isReadOnly();
			if (!readonly && pDataPage.getDataBook().getReadOnlyChecker() != null)
			{
				try
				{
					readonly = pDataPage.getDataBook().getReadOnlyChecker().isReadOnly(pDataPage.getDataBook(), pDataPage, pDataRow, pColumnName, pRowNumber, -1);
				}
				catch (Throwable pTh)
				{
					// Ignore
				}
			}
			
			if (choiceField.isEnabled() == readonly)
			{
				choiceField.setEnabled(!readonly);
			}
	
			CssExtension cssPanelExtension = VaadinUtil.getCssExtension(panel);
			cssPanelExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(getHorizontalAlignment()));
			cssPanelExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(getVerticalAlignment()));
		}
		catch (ModelException e)
		{
			panel = null;
		}
		
		return panel;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * {@inheritDoc}
     */
	@Override
    public void setImageNames(String[] pImageNames)
    {
	    super.setImageNames(pImageNames);
        
	    images = null;
    }

	/**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultImageName(String pDefaultImageName)
    {
        defaultImageName = pDefaultImageName;
        
        defaultImage = null;
    }
    

	//****************************************************************
	// User definition methods
	//****************************************************************	
	
	/**
	 * Returns the Images in an Array.
	 * 
	 * @return the Images.
	 */
	public Resource[] getImages()
	{
	    if (images == null)
	    {
            if (imageNames != null)
            {
                images = new Resource[imageNames.length];
    
                for (int i = 0; i < imageNames.length; i++)
                {
                    images[i] = VaadinImage.createResource(imageNames[i]);
                }
            }
            else
    		{
    		    images = new Resource[0];
    		}
	    }
        
		return images;
	}
	
	/**
	 * Returns the default image as resource.
	 * 
	 * @return the default image as resource.
	 */
	public Resource getDefaultImage() 
	{
	    if (defaultImage == null && defaultImageName != null)
        {
            defaultImage = VaadinImage.createResource(defaultImageName);
	    }
	    
		return defaultImage;
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
    public static class CellEditorHandler implements IVaadinCellEditorHandler<Component>, 
                                                     ClickListener
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private VaadinChoiceCellEditor cellEditor;
    	
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** To be able to set styles to this component. */
    	private CssExtension cssCellEditorComponentExtension = new CssExtension(); 
    	
    	/** The attributes extension. */
    	private AttributesExtension attExtension;

    	/** A wrapper for the choice component. **/
    	private SimplePanel simplePanel;
    	
    	/** The physical text editor. */
    	private ChoiceComponent cellEditorComponent;    	

    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
    	/** The column name of the edited column. */
    	private String sColumnName;

    	/** True, the Event should be ignored. */
    	private boolean bIgnoreEvent;
    	
    	/** True, if it's the first editing started event. */
    	private boolean bFirstEditingStarted = true;
    	
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
    	public CellEditorHandler(VaadinChoiceCellEditor pCellEditor, ICellFormatterEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		sColumnName = pColumnName;
    		
    		cellEditorComponent = new ChoiceComponent();
    		cellEditorComponent.setSizeUndefined();
    		cellEditorComponent.setAllowedValues(convertAllowedValues(cellEditor.getAllowedValues()));
    		cellEditorComponent.setImages(cellEditor.getImages());
    		cellEditorComponent.setDefaultImage(cellEditor.getDefaultImage());
    		cellEditorComponent.setSelectedItem(null);

    		VaadinCellEditorUtil.addStyleNames(cellEditorComponent, pCellEditor.getStyle(), "jvxchoicecell", "editor");

    		
    		simplePanel = new FocusForwardingSimplePanel();
    		simplePanel.setWidth(100, Unit.PERCENTAGE);
    		simplePanel.setContent(cellEditorComponent);
    		simplePanel.addStyleName("jvxeditor-choicefield");
    		
    		cssCellEditorComponentExtension.extend(simplePanel);
    		
			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
			{	// use alignment of editors, if possible.
				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
			}
			else
			{
    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(cellEditor.getHorizontalAlignment()));
    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(cellEditor.getVerticalAlignment()));
			}
			
    		cellEditorComponent.addClickListener(this);
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	// IVaadinCellEditorHandler
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@SuppressWarnings("deprecation")
        public void uninstallEditor()
    	{
    		VaadinCellEditorUtil.removeStyleNames(cellEditorComponent, cellEditor.getStyle(), "jvxchoicecell", "editor");
    		
    		cellEditorComponent.removeClickListener(this);
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
    		return simplePanel;
    	}
    	
        /**
         * {@inheritDoc}
         */
        public CssExtension getCssExtension()
        {
            return cssCellEditorComponentExtension;
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
    		if (cellEditorComponent.isEnabled())
    		{
    			dataRow.setValue(sColumnName, cellEditorComponent.getSelectedItem());
    		}
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void cancelEditing() throws ModelException
    	{
    		if (!bIgnoreEvent)
    		{
	    		bIgnoreEvent = true;
	
	    		try
	    		{
	    			ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(sColumnName);
	    			
	    			if (!CommonUtil.equals(cellEditorComponent.getSelectedItem(), dataRow.getValue(sColumnName)))
	    			{
	    				cellEditorComponent.setSelectedItem(dataRow.getValue(sColumnName));
	    			}
		    		
					boolean bParentReadOnly = !(cellEditorListener.getControl() instanceof IComponent) || !((IComponent)cellEditorListener.getControl()).isEnabled();
					
		    		if (dataRow instanceof IDataBook)
		    		{
		    			IDataBook dataBook = (IDataBook)dataRow;
		    			boolean readonly = bParentReadOnly
		    				    || !dataBook.isUpdateAllowed()
				                || columnDef.isReadOnly();
		    			if (!readonly && dataBook.getReadOnlyChecker() != null)
		    			{
		    				try
							{
		    					readonly = dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook, sColumnName, dataBook.getSelectedRow(), -1);
							}
							catch (Throwable pTh)
							{
								// Ignore
							}
		    			}
		    			cellEditorComponent.setEnabled(!readonly);
		    		}
					else
					{
						cellEditorComponent.setEnabled(!bParentReadOnly && !columnDef.isReadOnly());
					}
		    		
                    VaadinCellEditorUtil.applyAdditionalStyles(cellEditorComponent, columnDef);
                    
		    		if (dynamicAlignment != null)
		    		{
		    			int hAlign = dynamicAlignment.getHorizontalAlignment();
		    			if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
		    			{
		    				hAlign = cellEditor.getHorizontalAlignment();
		    			}
		    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(hAlign));
		    			int vAlign = dynamicAlignment.getVerticalAlignment();
		    			if (vAlign == IAlignmentConstants.ALIGN_DEFAULT)
		    			{
		    				vAlign = cellEditor.getVerticalAlignment();
		    			}
		    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(vAlign));
		    		}
	    		}
	    		catch (Exception pException)
	    		{
	    			cellEditorComponent.setSelectedItem(null);
	    			cellEditorComponent.setEnabled(false);
	   
	    			throw new ModelException("Editor cannot be restored!", pException);
	    		}
	    		finally
	    		{
	    			bFirstEditingStarted = true;
	    			bIgnoreEvent = false;
	    		}
    		}
    	}

    	//Click Listener
				
    	/**
    	 * {@inheritDoc}
    	 */		
		public void buttonClick(ClickEvent pEvent)
		{
			if (!bIgnoreEvent)
    		{
				fireEditingStarted();
	
				fireEditingComplete(ICellEditorListener.FOCUS_LOST);
	
	   			cellEditorComponent.focus();
    		}
		}
		
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Convert allowed values to destination type.
		 * @param pAllowedValues the allowed values
		 * @return the converted values
		 */
		private Object[] convertAllowedValues(Object[] pAllowedValues)
		{
			try
			{
	    		IDataType dataType = dataRow.getRowDefinition().getColumnDefinition(sColumnName).getDataType();

				Object[] result = new Object[pAllowedValues.length];
				
				for (int i = 0; i < result.length; i++)
				{
					try
					{
						result[i] = dataType.convertToTypeClass(pAllowedValues[i]);
					}
					catch (ModelException e)
					{
						result[i] = pAllowedValues[i];
					}
				}
				
				return result;
			}
			catch (Exception ex)
			{
				return pAllowedValues;
			}
		}		

		/**
		 * Delegates the event to the {@link ICellEditorListener}.
		 * It takes care, that the event occurs only one time.
		 */
		public void fireEditingStarted()
		{
        	if (bFirstEditingStarted
        		&& !bIgnoreEvent
        		&& cellEditorListener != null
        		&& cellEditorComponent.isEnabled())
        	{
           		bFirstEditingStarted = false;
           		cellEditorListener.editingStarted();
        	}
		}
		
		/**
		 * Delegates the event to the {@link ICellEditorListener}.
		 * It takes care, that editing started will be called before,
		 * if it is not called until jet.
		 * 
		 * @param pCompleteType the editing complete type.
		 */
		public void fireEditingComplete(String pCompleteType)
		{
			if (!bIgnoreEvent
				&& cellEditorListener != null
				&& cellEditorComponent.isEnabled())
			{
				cellEditorListener.editingComplete(pCompleteType);
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
			if (cellEditorComponent != null)
			{
				//Always size undefined
				cellEditorComponent.setWidth(VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
				simplePanel.setWidth(pWidth, pUnit);
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
			if (cellEditorComponent != null)
			{
				//Always size undefined
				cellEditorComponent.setHeight(VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
				simplePanel.setHeight(pHeight, pUnit);
			}	
		}
		
	    //****************************************************************
	    // Subclass definition
	    //****************************************************************
		
		/**
		 * The <code>ChoiceComponent</code> is a {@link ChoiceField}. It is needed for the ShortcutHandler, 
		 * to fire editing started and editing complete.
		 * 
		 * @author Stefan Wurm
		 */
		private class ChoiceComponent extends ChoiceField 
		                              implements IEditorComponent
		{
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Interface implementation
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

			/**
	    	 * {@inheritDoc}
	    	 */
			public void handleAction(Action pAction)
			{
				bIgnoreEvent = false;
			
		     	if (pAction == ShortcutHandler.ACTION_ESCAPE) 
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
		    	} 
		     	else if (pAction == ShortcutHandler.ACTION_ENTER
		     			 || pAction == ShortcutHandler.ACTION_ALT_ENTER
		     			 || pAction == ShortcutHandler.ACTION_CTRL_ENTER
		     			 || pAction == ShortcutHandler.ACTION_META_ENTER) 
		    	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ENTER_KEY);
		    	}
		     	else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
		     	}
		     	else if (pAction == ShortcutHandler.ACTION_TAB)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.TAB_KEY);
		     	}
		     	else if (pAction == ShortcutHandler.ACTION_SHIFT_TAB)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_TAB_KEY);
		     	}		     	
			}
			
		} 	// ChoiceComponent					

    }	// CellEditorHandler
    
	/**
	 * The <code>ChoiceFieldClickListener</code> is a {@link ClickListener}. 
	 * 
	 * @author Stefan Wurm
	 */
    public static class ChoiceFieldClickListener implements ClickListener
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/** The data page. */
    	private IDataPage dataPage;
    	
    	/** The row number. */
    	private int rowNumber;
    	
    	/** The column name. */
    	private String columnName;
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Constructs a <code>ChoiceFieldClickListener</code>.
    	 * @param pDataPage data page.
    	 * @param pRowNumber row number.
    	 * @param pColumnName column name.
    	 */
    	public ChoiceFieldClickListener(IDataPage pDataPage, int pRowNumber, String pColumnName)
    	{
    		dataPage = pDataPage;
    		rowNumber = pRowNumber;
    		columnName = pColumnName;
    	}
    	
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
    	
		/**
    	 * {@inheritDoc}
    	 */
		@SuppressWarnings("deprecation")
        public void buttonClick(ClickEvent pEvent)
		{
			try
			{
				ChoiceField choiceField = (ChoiceField)pEvent.getSource();

				IDataBook dataBook = (IDataBook)dataPage;
				
				HasComponents parent = choiceField.getParent().getParent();

				// First select Row and column, then route click to table.
				// This way no initializeOnlyVisibleItems occurs in table click.
				// This initializeOnlyVisibleItems would reset the current choiceField, and restore the item value.
				// If in this place a problem occurs, maybe try to get the choiceField value first, and then use the old click mechanism. 
				dataBook.setSelectedRow(rowNumber);
				dataBook.setSelectedColumn(columnName);

				if (parent instanceof VaadinTable.TableComponent)
				{
					((VaadinTable.TableComponent)parent).getVaadinTable().
						itemClick(new com.vaadin.v7.event.ItemClickEvent(parent, null, Integer.valueOf(rowNumber), columnName, new MouseEventDetails()));
				}
				
				IDataRow dataRow = dataBook.getDataRow(rowNumber);
				
				if (!choiceField.isEnabled() || dataBook.getSelectedRow() != rowNumber)
				{
					Object value = dataRow.getValue(columnName);
					if (!CommonUtil.equals(choiceField.getSelectedItem(), value))
					{
						choiceField.setSelectedItem(value);
					}
				}
				else
				{
					try
					{
						dataBook.setValue(columnName, choiceField.getSelectedItem());
					}
					finally
					{
						if (parent instanceof VaadinTable.TableComponent)
						{
							((VaadinTable.TableComponent)parent).getVaadinTable().refreshDataOnly();
						}
					}
				}
			}
			catch (ModelException pEx)
			{
				ExceptionHandler.raise(pEx);
			}
		}
    	
    } // ChoiceFieldClickListener

}	// VaadinChoiceCellEditor
