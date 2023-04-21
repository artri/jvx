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
 * 22.04.2014 - [RZ] - #1014: implemented displayReferencedColumnName
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IDimension;
import javax.rad.ui.IFont;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.ui.component.IPlaceholder;
import javax.rad.ui.control.ICellFormat;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.celleditor.AbstractLinkedCellEditor;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.data.AbstractDataBookDataProvider;
import com.sibvisions.rad.ui.vaadin.ext.events.RegistrationContainer;
import com.sibvisions.rad.ui.vaadin.ext.ui.ClickableLabel;
import com.sibvisions.rad.ui.vaadin.ext.ui.LazyComboBox;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinDimension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFont;
import com.sibvisions.rad.ui.vaadin.impl.control.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.vaadin.impl.feature.IAutoCompleteFeature;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CommonUtil;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComboBox.NewItemHandler;
import com.vaadin.ui.Component;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.v7.shared.ui.label.ContentMode;

/**
 * The <code>VaadinLinkedCellEditor</code> class is the vaadin implementation of {@link ILinkedCellEditor}.
 * 
 * @author Stefan Wurm
 */
public class VaadinLinkedCellEditor extends AbstractLinkedCellEditor 
                                    implements ICellRenderer<Component>,
                                               IAutoCompleteFeature
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Whether the autocomplete feature is enabled. */
	private Boolean bAutoComplete;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Creates a new instance of <code>VaadinLinkedCellEditor</code>.
     *
     * @see ILinkedCellEditor
     */	
	public VaadinLinkedCellEditor()
	{
		horizontalAlignment = ALIGN_DEFAULT;
		setPopupSize(new VaadinDimension(-1, -1));
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
	public Component getCellRendererComponent(Component pAvailabelComponent, IDataPage pDataPage, 
											  int pRowNumber, IDataRow pDataRow, String pColumnName, boolean pIsSelected, boolean pHasFocus)
	{
        ClickableLabel cellLabel = null;
        
        if (pAvailabelComponent instanceof ClickableLabel)
        {
            cellLabel = (ClickableLabel)pAvailabelComponent;
        }
        else
        {
            cellLabel = new ClickableLabel();
            cellLabel.setSizeFull();
            
            VaadinCellEditorUtil.addStyleNames(cellLabel, getStyle(), "cursor-hand", "jvxlinkedcell", "renderer");
            
            if (pDataPage instanceof IDataBook)
            {
                cellLabel.addClickListener(new ClickListener()
                {
                    public void click(ClickEvent event)
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
        
		CssExtension cssLabelExtension = VaadinUtil.getCssExtension(cellLabel);

		cssLabelExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(getDefaultHorizontalAlignment(pDataRow, pColumnName)));
		cssLabelExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(getVerticalAlignment()));

		try
		{
            String value = getDisplayValue(pDataRow, pColumnName);

            ContentMode contentMode = ContentMode.TEXT;
            if (value != null && value.startsWith("<html>"))
            {
                    contentMode = ContentMode.HTML;
            }
	            
            cellLabel.setContentMode(contentMode);
            cellLabel.setValue(value);
		}
		catch (Exception pException)
		{
			cellLabel.setValue(null);
		}

		return cellLabel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAutoComplete(boolean pAutoComplete)
	{
		bAutoComplete = Boolean.valueOf(pAutoComplete);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoComplete()
	{
		return bAutoComplete == null || bAutoComplete.booleanValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoCompleteSet()
	{
		return bAutoComplete != null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayValue(IDataRow pDataRow, String pColumnName) throws ModelException
	{
	    //made public
	    return super.getDisplayValue(pDataRow, pColumnName);
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
    												 FocusListener,
    												 BlurListener,
    												 NewItemHandler,
                                                     ValueChangeListener
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private VaadinLinkedCellEditor cellEditor;

    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** The DataBook referenced by the edited column. */
    	private IDataBook referencedDataBook;
    	
    	/** The physical text editor. */
    	private ComboBoxComponent cellEditorComponent;  	

    	/** To be able to set styles to this component. */
    	private CssExtension cssCellEditorComponentExtension = new CssExtension();
    	
    	/** The attributes extension. */
    	private AttributesExtension attExtension;
    	
    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
    	/** The {@link RegistrationContainer} that holds all registrations. */
    	private RegistrationContainer registrations = new RegistrationContainer();

    	/** The column name of the edited column. */
    	private String columnName;

    	/** The column names. */
    	private String[] columnNames;
    	
    	/** The column names referenced. */
    	private String[] referencedColumnNames;
    	
    	/** The column name referenced by the edited column. */
    	private String referencedColumnName;
    	
    	/** The clear Columns. */
    	private String[] clearColumns;

    	/** The additional clear Columns. */
    	private String[] additionalClearColumns;
        /** The columns and additional Columns. */
    	private String[] allColumns;
    	
    	/** The current search string. */
    	private String selectedItemCaption = null;
    	
    	/** The current search string. */
    	private String currentSearch;
    	
    	/** Focus Lost event is before accept. */
    	private String acceptReason = ICellEditorListener.ENTER_KEY;
    	
    	/** True, the Event should be ignored. */
    	private boolean ignoreEvent = false;
    	
    	/** True, if it's the first editing started event. */
    	private boolean bFirstEditingStarted = true;

    	/** Whether the editor has the focus. */
    	private boolean hasFocus = false;
    	
    	/** The current translation map. */
    	private TranslationMap currentTranslation;

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Creates a new instanceof <code>CellEditorHandler</code>.
    	 * 
    	 * @param pCellEditor the CellEditor that created this handler.
    	 * @param pCellEditorListener CellEditorListener to inform, if editing is started or completed.
    	 * @param pDataRow the data row that is edited.
    	 * @param pColumnName the column name of the edited column.
    	 */
    	public CellEditorHandler(VaadinLinkedCellEditor pCellEditor, ICellFormatterEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		columnName = pColumnName;
    		
    		columnNames = cellEditor.linkReference.getColumnNames();
    		referencedColumnNames = cellEditor.linkReference.getReferencedColumnNames();
    		if (columnNames.length == 0 && referencedColumnNames.length == 1)
    		{
    			columnNames = new String[] {pColumnName};
    		}

    		int columnIndex = ArrayUtil.indexOf(columnNames, columnName);
    		if (columnIndex < 0)
    		{
    			throw new IllegalArgumentException("The edited column " + columnName + " has to be part of in column names list in LinkReference!");
    		}
    		else
    		{
        		referencedColumnName = referencedColumnNames[columnIndex];
    		}
    		
       		referencedDataBook = cellEditor.linkReference.getReferencedDataBook();
    		referencedDataBook.setSelectionMode(IDataBook.SelectionMode.DESELECTED);
    		
    		currentTranslation = cellEditorListener.getControl().getTranslation();
    		try
			{
    			clearColumns = cellEditor.getClearColumns(dataRow, columnName);
    			additionalClearColumns = cellEditor.getAdditionalClearColumns(dataRow);

    			referencedDataBook.setReadOnly(cellEditor.isTableReadonly());
			}
			catch (ModelException pModelException)
			{
				// Do nothing, it only tries to set data book readonly.
			}
    		if (additionalClearColumns != null && additionalClearColumns.length > 0)
    		{
    		    allColumns = ArrayUtil.addAll(columnNames, additionalClearColumns);
    		}

    		String sPrefix;
    		
    		if (dataRow instanceof IDataBook)
    		{
    			sPrefix = ((IDataBook)dataRow).getName();
    			
    			if (sPrefix != null)
    			{
    				sPrefix = sPrefix.toLowerCase() + "_";
    			}
    		}
    		else
    		{
    			sPrefix = "row_";
    		}
    		
    		cellEditorComponent = new ComboBoxComponent();
    		cssCellEditorComponentExtension.extend(cellEditorComponent);
    		
    		VaadinCellEditorUtil.addStyleNames(cellEditorComponent, pCellEditor.getStyle(), sPrefix + columnName.toLowerCase(), "jvxlinkedcell", "editor");
    		
    		IDimension dim = pCellEditor.getPopupSize();
    		
    		if (dim != null && dim.getWidth() > 0)
    		{
    			//fixed width
    			cellEditorComponent.setPopupWidth("" + dim.getWidth() + "px");
    		}
    		else
    		{
                //at least the content-width
                cellEditorComponent.setPopupWidth(null);
    		}
    		if (dim != null && dim.getHeight() >= 0)
    		{
    		    cellEditorComponent.setPageLength(dim.getHeight() / 20);
    		}
    		else
    		{
    		    cellEditorComponent.setPageLength(15);
    		}
    		
			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
			{	// use alignment of editors, if possible.
				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
			}
			else
			{
    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(
    					cellEditor.getDefaultHorizontalAlignment(dataRow, columnName)));
    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(cellEditor.getVerticalAlignment()));
			}

			cellEditorComponent.setItemCaptionGenerator(new DataRowCaptionGenerator());
			
			DataBookDataRowDataProvider dataProvider =  new DataBookDataRowDataProvider();
			// We will disable the provider here, it will be re-enabled upon
			// the first focus gain of the component. This is to disable
			// the initial fetch of items.
			dataProvider.setEnabled(false);
			cellEditorComponent.setDataProvider(dataProvider);
    		
    		registrations.add(cellEditorComponent.addFocusListener(this));
    		registrations.add(cellEditorComponent.addBlurListener(this));
    		registrations.add(cellEditorComponent.addValueChangeListener(this));
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * {@inheritDoc}
    	 */
    	public void uninstallEditor()
    	{
    		VaadinCellEditorUtil.removeStyleNames(cellEditorComponent, cellEditor.getStyle(), "jvxlinkedcell", "editor");
    		
    		registrations.removeAll();
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
    		return columnName;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public AbstractComponent getCellEditorComponent()
    	{
    		return cellEditorComponent;
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
    	 * Sets the values and clears the additionalClearColumns, if values are changed.
    	 * @param pNewValues the new values.
    	 * @param pNotValidatedValue the value of this field, if it is not validated.
    	 * @throws ModelException if it fails.
    	 */
    	private void setValuesAndClearIfNecessary(Object[] pNewValues, Object pNotValidatedValue) throws ModelException
    	{
    		IDataRow oldRow = dataRow.createDataRow(null);
    		
    		IDataRow newRow = dataRow.createDataRow(null);
    		if (pNewValues == null)
    		{
    			// Use a temp row, to have one setValues with one values changed event on dataRow.
    		    newRow.setValues(clearColumns, null);
    		    newRow.setValue(columnName, pNotValidatedValue);
    		}
    		else
    		{
    		    newRow.setValues(columnNames, pNewValues);
    		}

    		if (additionalClearColumns != null && additionalClearColumns.length > 0 && !newRow.equals(oldRow, columnNames))
            {
    		    newRow.setValues(additionalClearColumns, null);
                dataRow.setValues(allColumns, newRow.getValues(allColumns));
            }
    		else
    		{
    		    dataRow.setValues(columnNames, newRow.getValues(columnNames));
    		}
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	public void saveEditing() throws ModelException
    	{
    		if (referencedDataBook.getSelectedRow() >= 0)
			{
    			setValuesAndClearIfNecessary(referencedDataBook.getValues(referencedColumnNames), null);
			}
			else
			{
				Object item = cellEditorComponent.getValue();
				
	    		if (item == null || "".equals(item))
	    		{
					setValuesAndClearIfNecessary(null, null);
	    		}
	    		else
	    		{
		    		referencedDataBook.setFilter(cellEditor.getSearchCondition(dataRow, cellEditor.getItemSearchCondition(false, getRelevantSearchColumnName(), item)));
		    		if (referencedDataBook.getDataRow(0) != null && referencedDataBook.getDataRow(1) == null)
		    		{
		    			setValuesAndClearIfNecessary(referencedDataBook.getDataRow(0).getValues(referencedColumnNames), null);
		    		}
		    		else
		    		{
	    				if (cellEditor.isValidationEnabled())
	    				{
			        		referencedDataBook.setFilter(cellEditor.getSearchCondition(dataRow, 
			        				cellEditor.getItemSearchCondition(true, getRelevantSearchColumnName(), item)));
			        		if (referencedDataBook.getDataRow(0) != null && referencedDataBook.getDataRow(1) == null)
			        		{
				    			setValuesAndClearIfNecessary(referencedDataBook.getDataRow(0).getValues(referencedColumnNames), null);
			    			}
			    			else
			    			{
				    			setValuesAndClearIfNecessary(dataRow.getValues(columnNames), null);
		    				}
		    			}
	    				else
	    				{
	    					try
	    					{
	    						item = dataRow.getRowDefinition().getColumnDefinition(columnName).getDataType().convertAndCheckToTypeClass(item);
	    					}
	    					catch (Exception ex)
	    					{
				    			setValuesAndClearIfNecessary(dataRow.getValues(columnNames), null);
				    			return;
	    					}
    						setValuesAndClearIfNecessary(null, item);
	    				}
		    		}
	    		}
			}
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void cancelEditing() throws ModelException
    	{
            if (!ignoreEvent)
    		{
	    		ignoreEvent = true;
	    		
	    		VaadinCellEditorUtil.applyFeature(this);
	    		
	    		try
	    		{
	    			ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(columnName);

	    			if (cellEditorComponent.isReadOnly())
	    			{
	    			    cellEditorComponent.setReadOnly(false);
	    			}
	    			
	        		cellEditorComponent.setEmptySelectionAllowed(columnDef.isNullable());

	        		if (currentTranslation != cellEditorListener.getControl().getTranslation())
	        		{
	        		    currentTranslation = cellEditorListener.getControl().getTranslation();
	        		    
	                    cellEditorComponent.setItemCaptionGenerator(cellEditorComponent.getItemCaptionGenerator());
	        		}
	        		
	                cellEditorComponent.setValue(dataRow.getValueAsString(columnName));
	                selectedItemCaption = cellEditorComponent.getSelectedItemCaption();
	                
					ICellFormat cellFormat = null;

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
		    					readonly = dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook, columnName, dataBook.getSelectedRow(), -1);
							}
							catch (Throwable pTh)
							{
								// Ignore
							}
		    			}
		    			cellEditorComponent.setReadOnly(readonly);
		    		}
					else
					{
						cellEditorComponent.setReadOnly(bParentReadOnly || columnDef.isReadOnly());
					}
		    		
		    		if (cellEditor.isValidationEnabled() || cellEditorComponent.isReadOnly())
		    		{
		    			cellEditorComponent.setNewItemHandler(null);
		    		}
		    		else
		    		{
		    			cellEditorComponent.setNewItemHandler(this);
		    		}
		    		
                    VaadinCellEditorUtil.applyAdditionalStyles(cellEditorComponent, columnDef);
                    
					if (cellEditorListener.getCellFormatter() != null)
					{
						IDataBook curDataBook = null;
						IDataPage curDataPage = null;
						int curSelectedRow = -1;
						
						if (dataRow instanceof IDataBook)
						{
							curDataBook = (IDataBook)dataRow;
							curDataPage = curDataBook.getDataPage();
							curSelectedRow = curDataBook.getSelectedRow();
						}
						else if (dataRow instanceof IChangeableDataRow)
						{
							curDataPage = ((IChangeableDataRow)dataRow).getDataPage();
							curSelectedRow = ((IChangeableDataRow)dataRow).getRowIndex();
							if (curDataPage != null)
							{
								curDataBook = curDataPage.getDataBook();
							}
						}
						
						try
						{
							cellFormat = cellEditorListener.getCellFormatter().getCellFormat(
									curDataBook, curDataPage, dataRow, columnName, curSelectedRow, -1);
						}
						catch (Throwable pThrowable)
						{
							// Do nothing
						}
					}
					
					IColor background;
					IColor foreground;
					IFont  font;
					
					if (cellFormat == null)
					{
						background = null;
						foreground = null;
						font = null;
					}
					else
					{
						background = cellFormat.getBackground();
						foreground = cellFormat.getForeground();
						font = cellFormat.getFont();
					}
					if (font == null)
					{
						font = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFont();
					}
					if (foreground == null && ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).isForegroundSet())
					{
						foreground = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getForeground();
					}
					if (background == null && ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).isBackgroundSet())
					{
						background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getBackground();
					}

					if (dynamicAlignment != null)
		    		{
		    			int hAlign = dynamicAlignment.getHorizontalAlignment();
		    			if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
		    			{
		    				hAlign = cellEditor.getDefaultHorizontalAlignment(dataRow, columnName);
		    			}
		    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(hAlign));
		    			int vAlign = dynamicAlignment.getVerticalAlignment();
		    			if (vAlign == IAlignmentConstants.ALIGN_DEFAULT)
		    			{
		    				vAlign = cellEditor.getVerticalAlignment();
		    			}
		    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(vAlign));
		    		}
					
					if (font != null)
					{
						cssCellEditorComponentExtension.addAttributes(
								((VaadinFont) font.getResource()).getStyleAttributes("v-filterselect-input", CssExtensionAttribute.SEARCH_DOWN));
					}
					else
					{
						CssExtensionAttribute attrib1 = new CssExtensionAttribute("font-weight", 
														    					null, 
														    					"v-filterselect-input", 
												                                CssExtensionAttribute.SEARCH_DOWN);
						
						CssExtensionAttribute attrib2 = new CssExtensionAttribute("font-style", 
														    					null, 
														    					"v-filterselect-input", 
												                                CssExtensionAttribute.SEARCH_DOWN);
						
						CssExtensionAttribute attrib3 = new CssExtensionAttribute("font-family", 
														    					null, 
														    					"v-filterselect-input", 
												                                CssExtensionAttribute.SEARCH_DOWN);
						
						CssExtensionAttribute attrib4 = new CssExtensionAttribute("font-size", 
														    					null, 
														    					"v-filterselect-input", 
												                                CssExtensionAttribute.SEARCH_DOWN);
												
						cssCellEditorComponentExtension.removeAttribute(attrib1);
						cssCellEditorComponentExtension.removeAttribute(attrib2);
						cssCellEditorComponentExtension.removeAttribute(attrib3);
						cssCellEditorComponentExtension.removeAttribute(attrib4);
					}
					
		    		if (!cellEditorComponent.isReadOnly())
		    		{
		    			if (background == null)
		    			{
			    			if (columnDef.isNullable())
			    			{
		    					background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_BACKGROUND);
			    			}
			    			else
			    			{
			    				background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_MANDATORY_BACKGROUND);
			    			}
		    			}
		    		}
		    		else if (background == null)
		    		{
		    			background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
		    		}
	
					if (foreground != null)
					{
		    			CssExtensionAttribute attrib = new CssExtensionAttribute("color", 
		    					                                                 ((VaadinColor) foreground.getResource()).getStyleValueRGB(), 
												                                 "v-filterselect-input", 
												                                 CssExtensionAttribute.SEARCH_DOWN);

						
						cssCellEditorComponentExtension.addAttribute(attrib);
					}
					else
					{
		    			CssExtensionAttribute attrib = new CssExtensionAttribute("color", 
                                												null, 
                                												"v-filterselect-input", 
                                												CssExtensionAttribute.SEARCH_DOWN);
						
						cssCellEditorComponentExtension.removeAttribute(attrib);
					}
					
					if (background != null)
					{
		    			CssExtensionAttribute attribCol = new CssExtensionAttribute("background-color", 
                                                                                 ((VaadinColor)background.getResource()).getStyleValueRGB(), 
                                                                                 "v-filterselect-input", 
                                                                                 CssExtensionAttribute.SEARCH_DOWN);
		    			
		    			CssExtensionAttribute attribImg = new CssExtensionAttribute("background-image", "none", 
													                                "v-filterselect-input", 
													                                CssExtensionAttribute.SEARCH_DOWN);
						
						cssCellEditorComponentExtension.addAttribute(attribCol);
						cssCellEditorComponentExtension.addAttribute(attribImg);
					}
					else 
					{
		    			CssExtensionAttribute attribCol = new CssExtensionAttribute("background-color", 
                                												null, 
                                												"v-filterselect-input", 
                                												CssExtensionAttribute.SEARCH_DOWN);
		    			CssExtensionAttribute attribImg = new CssExtensionAttribute("background-image", 
																					null, 
																					"v-filterselect-input", 
																					CssExtensionAttribute.SEARCH_DOWN);
						
						cssCellEditorComponentExtension.removeAttribute(attribCol);
						cssCellEditorComponentExtension.removeAttribute(attribImg);
					}
					
					if (cellEditorListener.getControl() instanceof IPlaceholder && cellEditorComponent instanceof ComboBox)
					{
						((ComboBox)cellEditorComponent).setPlaceholder(cellEditor.getPlaceholderText((IPlaceholder)cellEditorListener.getControl()));
					}
	    		}
	    		catch (Exception ex)
	    		{
	    		    boolean bReadOnly = cellEditorComponent.isReadOnly();
	    		    
	    		    if (bReadOnly)
	    		    {
	    		        cellEditorComponent.setReadOnly(false);
	    		    }

	                cellEditorComponent.setValue(null);
	    			
	    			if (bReadOnly)
	    			{
	    			    cellEditorComponent.setReadOnly(true);
	    			}
	    			cellEditorComponent.setNewItemHandler(null);

	    			IColor background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
	    			
	    			CssExtensionAttribute attribCol = new CssExtensionAttribute("background-color", 
	    					                                                 	((VaadinColor)background.getResource()).getStyleValueRGB(), 
	    					                                                 	"v-filterselect-input", 
	    					                                                 	CssExtensionAttribute.SEARCH_DOWN);
	    			CssExtensionAttribute attribImg = new CssExtensionAttribute("background-image", "none",  
	    																		"v-filterselect-input", 
	    																		CssExtensionAttribute.SEARCH_DOWN);
	    			
	    			cssCellEditorComponentExtension.addAttribute(attribCol);
	    			cssCellEditorComponentExtension.addAttribute(attribImg);

	    			throw new ModelException("Editor cannot be restored!", ex);
	    		}
	    		finally
	    		{
	    			bFirstEditingStarted = true;
	    			ignoreEvent = false;
	    		}
    		}
    	}
    	
    	// FocusListener
    	  
    	/**
    	 * {@inheritDoc}
    	 */
		public void focus(com.vaadin.event.FieldEvents.FocusEvent pEvent)
		{
		    acceptReason = ICellEditorListener.ENTER_KEY;
		    hasFocus = true;
		    currentSearch = null;
		    
		    ((DataBookDataRowDataProvider)cellEditorComponent.getDataProvider()).forceSetFilter();
		}	
		
    	/**
    	 * {@inheritDoc}
    	 */	
		public void blur(BlurEvent pEvent)
		{
			if (currentSearch != null && !currentSearch.equals(cellEditorComponent.getValue()))
			{
			    acceptReason = ICellEditorListener.FOCUS_LOST;

				cellEditorComponent.setValue(currentSearch);
			}
			
		    hasFocus = false;
		}	

		
    	//NewItemHandler
		
    	/**
    	 * {@inheritDoc}
    	 */
		public void accept(String pNewItemCaption)
		{
		    cellEditorComponent.setValue(pNewItemCaption); // saveEditing handles validation enabled and value search.
		}

    	//ItemChangeListener
		
    	/**
    	 * {@inheritDoc}
    	 */
		public void valueChange(ValueChangeEvent pEvent)
		{
			if (!ignoreEvent && hasFocus)
			{
				try 
				{
					String value = (String)pEvent.getValue();
				
					int rowNum;
					if (value == null || "".equals(value)) 
					{
						if (currentSearch != null && currentSearch.length() > 0)
						{
							cancelEditing();
							return;
						}
						rowNum = -1;
					}
					else
					{
						rowNum = referencedDataBook.searchNext(new Equals(getRelevantSearchColumnName(), value));
					}
					fireEditingStarted();
		
					referencedDataBook.setSelectedRow(rowNum);
							
					fireEditingComplete(acceptReason);
				}
				catch (ModelException ex) 
				{
					// Do Nothing
				}
			}
		}

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Returns the relevant search column. If a {@code displayColumnName} is set,
		 * then that one will be returned, otherwise it will return {@code referencedColumnName}.
		 * 
		 * @return the relevant search column name.
		 */
		private String getRelevantSearchColumnName()
		{
			if (cellEditor.displayReferencedColumnName != null)
			{
				return cellEditor.displayReferencedColumnName;
			}
			else
			{
				return referencedColumnName;
			}
		}
    	
		/**
		 * Delegates the event to the {@link ICellEditorListener}.
		 * It takes care, that the event occurs only one time.
		 */
		public void fireEditingStarted()
		{
        	if (bFirstEditingStarted
        		&& !ignoreEvent
        		&& cellEditorListener != null
        		&& !cellEditorComponent.isReadOnly()
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
		protected void fireEditingComplete(String pCompleteType)
		{
			if (!ignoreEvent
				&& cellEditorListener != null
				&& !cellEditorComponent.isReadOnly()
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
				cellEditorComponent.setWidth(pWidth, pUnit);
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
				// Nothing todo. ComboBox has no height.
			}
			else 
			{
				if (cellEditorComponent != null)
				{
					cellEditorComponent.setHeight(pHeight, pUnit);
				}	
			}
		}
		
		//****************************************************************
	    // Subclass definition
	    //****************************************************************
		
		/**
		 * The <code>ComboBoxComponent</code> is a {@link ComboBox}. It is needed for the {@link ShortcutHandler}, 
		 * to fire editing started and editing complete.
		 * 
		 * @author Stefan Wurm
		 */
		private class ComboBoxComponent extends LazyComboBox 
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
		     	    //temp-focus check won't work because vaadin moves to next focusable component
		     	    CellEditorHandler.this.fireEditingComplete(ICellEditorListener.TAB_KEY);
		     	}
		     	else if (pAction == ShortcutHandler.ACTION_SHIFT_TAB)
		     	{
                    //temp-focus check won't work because vaadin moves to next previous component
                    CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_TAB_KEY);
		     	}
			}

			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// User-defined methods
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			/**
			 * Gets the current selected item caption.
			 * @return the current selected item caption.
			 */
			public String getSelectedItemCaption()
			{
				return getState(false).selectedItemCaption;
			}
			
		}	// ComboBoxComponent	

		/**
		 * The {@link DataBookDataRowDataProvider} is an {@link AbstractDataBookDataProvider}
		 * which provides {@link IDataRow}s.
		 * 
		 * @author Robert Zenz
		 */
	    private class DataBookDataRowDataProvider extends AbstractDataBookDataProvider<String>
	                                              implements LazyComboBox.IToggleableDataProvider<String, Object> // Implicite
	    {
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	// Initialization
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	
	    	/**
			 * Creates a new instance of {@link DataBookDataRowDataProvider}.
			 */
	    	protected DataBookDataRowDataProvider()
			{
				super(referencedDataBook);
			}
			
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	// Abstract methods implementation
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	
			/**
	    	 * {@inheritDoc}
	    	 */
	    	@Override
	    	protected Stream<String> getItems(Query<String, Object> pQuery, int pStartRowIndex, int pEndRowIndex) throws ModelException
	    	{
	    		List<String> indexes = new ArrayList<String>(pEndRowIndex - pStartRowIndex);
	    		
	    		IDataRow dr = dataBook.getDataRow(pStartRowIndex);
	    		while (dr != null && pStartRowIndex < pEndRowIndex)
	    		{
	    			indexes.add(CommonUtil.nvl(dr.getValueAsString(getRelevantSearchColumnName()), ""));
	    			pStartRowIndex++;
	    			dr = dataBook.getDataRow(pStartRowIndex);
	    		}
	    		
	    		return indexes.stream();
	    	}
	    	
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	// Overwritten methods
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	
            /**
             * {@inheritDoc}
             */
            @Override
	        protected void updateSort(List<QuerySortOrder> pSort) throws ModelException
	        {
	            if (cellEditor.isSortByColumnName())
	            {
	                SortDefinition currentSort = dataBook.getSort();
	             
	                if (currentSort == null 
	                        || currentSort.getColumns().length != 1 || !columnName.equals(currentSort.getColumns()[0])
	                        || !currentSort.isAscending()[0])
	                {
	                    dataBook.setSort(new SortDefinition(columnName));	                    
	                }
	            }
	        }
	    	
			/**
	    	 * {@inheritDoc}
	    	 */
			@Override
			protected ICondition createFilter(Object pValue) throws ModelException
			{
				// The filter event is used, to detect an editor change by keyboard.
				// If the search value is not null, the editor has the focus, and the value does not equal the current editor value,
				// the user must have typed a new value with keyboard.
				// In this case editing started is fired.
				if (pValue != null && hasFocus && !pValue.equals(selectedItemCaption))
				{
					fireEditingStarted();
				}
				
				// As long as the user is not editing, all values have to be shown.
				if (pValue == null || bFirstEditingStarted) 
	    		{
                    if (pValue != null)
                    {
                        // The editor was invisible and set to visible, the client sends the last filter, we have to ignore it.
                        selectedItemCaption = pValue.toString();
                    }
					currentSearch = null;
					return cellEditor.getSearchCondition(dataRow, null);
				}
				else
				{
					currentSearch = (String)pValue;
					return cellEditor.getSearchCondition(dataRow, cellEditor.getItemSearchCondition(true, getRelevantSearchColumnName(), pValue));
	    		}
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected void updateFilter(Optional<Object> pFilter) throws ModelException
			{
				super.updateFilter(pFilter);

				if (!bFirstEditingStarted)
				{
					cellEditor.updateCurrentCachedPage();
				}
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected int getItemsCount(Query<String, Object> pQuery) throws ModelException
			{
				// Because we might not know how many lines there are, we will tell the
				// system that are "inifinitely" more.
			    if (cellEditorComponent.getPageLength() == 0)
			    {
			        dataBook.fetchAll();
			    }
			    
				if (!dataBook.isAllFetched())
				{
					return Integer.MAX_VALUE;
				}
				else
				{
					return dataBook.getRowCount();
				}
			}
	    	
	    }	// DataBookDataRowDataProvider
		
	    /**
		 * The {@link DataRowCaptionGenerator} is an {@link ItemCaptionGenerator} implementation
		 * which returns the value from an {@link IDataRow}.
		 */
    	private class DataRowCaptionGenerator implements ItemCaptionGenerator<String>
	    {
    		/** Helper row to get the display value efficiently. */
    		IDataRow currentRow = null;
    		
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	// Initialization
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	
	    	/**
			 * Creates a new instance of {@link DataRowCaptionGenerator}.
			 */
	    	public DataRowCaptionGenerator()
	    	{
	    		super();
	    	}

	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	// Interface implementation
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    	
	    	/**
	    	 * {@inheritDoc}
	    	 */
	    	@Override
	    	public String apply(String pItem)
	    	{
	    		try
	    		{
		    		if (currentRow == null)
		    		{
		    			currentRow = dataRow.createEmptyDataRow(null);
		    		}
		    		currentRow.setValue(columnName, pItem);

	    			return cellEditor.getDisplayValue(currentRow, columnName);
	    		}
	    		catch (ModelException ex)
	    		{
	    			return pItem;
	    		}
	    	}
	    	
	    }	// DataRowCaptionGenerator
	    
    }	// CellEditorHandler
    
} 	// VaadinLinkedCellEditor
