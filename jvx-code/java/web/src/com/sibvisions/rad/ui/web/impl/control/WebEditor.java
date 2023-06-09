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
 * 20.11.2009 - [HM] - creation
 * 19.01.2011 - [JR] - used readonly of row(databook)
 * 24.03.2011 - [JR] - #317 
 *                     * invoke cancelEditing when enabled state changed
 *                     * updateProperties checks enabled state 
 */
package com.sibvisions.rad.ui.web.impl.control;

import java.util.List;
import java.util.Map;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.celleditor.IStyledCellEditor;
import javax.rad.ui.component.IPlaceholder;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.IEditor;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.ui.celleditor.AbstractLinkedCellEditor;
import com.sibvisions.rad.ui.celleditor.AbstractStyledCellEditor;
import com.sibvisions.rad.ui.web.impl.IWebFieldConstants;
import com.sibvisions.rad.ui.web.impl.WebColor;
import com.sibvisions.rad.ui.web.impl.WebFactory;
import com.sibvisions.rad.ui.web.impl.celleditor.WebTextCellEditor;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * Web server implementation of {@link IEditor}.
 * 
 * @author Martin Handsteiner
 */
public class WebEditor extends AbstractCellFormatable
                       implements IEditor, 
                                  IPlaceholder,
                                  ICellEditorListener,
                                  Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** the component logger. */
    private static ILogger logger = null;

    /** the data row. */
	private IDataRow dataRow = null;
	
	/** the column name. */
	private String columnName = null;
	
	/** the cell editor. */
	private ICellEditor cellEditor = null;
	
	/** the translation map for internal translations. */
	private TranslationMap translation = null;

	/** the background color. */
	private IColor colBackground = null;
	
	/** the cell editor handler. */
	private ICellEditorHandler handler;
	
	/** the placeholder. */
	private String sPlaceholder;
	
    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
    
    /** whether this editor is enabled. */
    private boolean bEnabled = true;
    
	/** reduces notifyRepaint calls. */
    private boolean firstNotifyRepaintCall = true;
	
    /** The dummy cell editor. */
    private WebTextCellEditor dummyCellEditor = new WebTextCellEditor();
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebEditor</code>.
     *
     * @see javax.rad.ui.control.IEditor
     */
	public WebEditor()
	{
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
		bEnabled = pEnabled;
		
		super.setEnabled(pEnabled);
		
		cancelEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(IColor pColor) 
	{
		colBackground = pColor;
		
		super.setBackground(pColor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Map.Entry<String, Object>> getChangedProperties()
	{
		applyStyleProperties();
		
		return super.getChangedProperties();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(String pToolTipText)
	{
		super.setToolTipText(pToolTipText);
		
		updateAccessibility();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
    {
    	return getProperty("horizontalAlignment", Integer.valueOf(IAlignmentConstants.ALIGN_DEFAULT)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
    {
		setProperty("horizontalAlignment", Integer.valueOf(pHorizontalAlignment));
    }

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
    {
    	return getProperty("verticalAlignment", Integer.valueOf(IAlignmentConstants.ALIGN_DEFAULT)).intValue();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
    {
		setProperty("verticalAlignment", Integer.valueOf(pVerticalAlignment));
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderVisible() 
	{
    	return ((Boolean)getProperty("borderVisible", Boolean.TRUE)).booleanValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setBorderVisible(boolean pVisible) 
	{
    	setProperty("borderVisible", Boolean.valueOf(pVisible));
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
    	
    	setProperty("placeholder", sPlaceholder);
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
    public void setDataRow(IDataRow pDataRow) throws ModelException
    {
    	uninstallEditor();
    	
    	dataRow = pDataRow;
    	
    	installEditor();
    	
    	updateAccessibility();
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
    public void setColumnName(String pColumnName) throws ModelException
    {
    	uninstallEditor();
    	
    	columnName = pColumnName;
    	
		setProperty("columnName", columnName, true);
    	
    	installEditor();
    	
    	updateAccessibility();
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
	public void setCellEditor(ICellEditor pCellEditor) throws ModelException
	{
		uninstallEditor();
		
		cellEditor = pCellEditor;
		
    	installEditor();
    	
    	updateAccessibility();
	}
	
	/**
	 * {@inheritDoc}
	 */
    public boolean isSavingImmediate()
    {
    	return getProperty("savingImmediate", Boolean.FALSE).booleanValue();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setSavingImmediate(boolean pSavingImmediate)
    {
    	setProperty("savingImmediate", Boolean.valueOf(pSavingImmediate));
    }

    /**
	 * {@inheritDoc}
	 */
	public void notifyRepaint() 
	{
		if (handler != null)
		{
			if (firstNotifyRepaintCall)
			{
				firstNotifyRepaintCall = false;
				
				getFactory().invokeLater(this);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException 
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing() 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public TranslationMap getTranslation()
	{
		return translation;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTranslation(TranslationMap pTranslation)
	{
		translation = pTranslation;
	}
	
    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        bTranslationEnabled = pEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return bTranslationEnabled;
    }
	
    /**
     * {@inheritDoc}
     */
	public String translate(String pText)
	{
		if (bTranslationEnabled && translation != null)
		{
			return translation.translate(pText);
		}
		else
		{
			return pText;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void editingStarted() 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void editingComplete(String pCompleteType) 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public IControl getControl() 
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
		firstNotifyRepaintCall = true;
		
		try
		{
			handler.cancelEditing();
		}
		catch (Exception e)
		{
			//ignore
		}
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Removes this editor as control from the data row.
	 */
	private void uninstallEditor()
	{
		if (dataRow != null)
		{
		    handler = null;
		    setProperty("cellEditor", dummyCellEditor, true);

			dataRow.removeControl(this);
			
			getFactory().getLauncher().register(dataRow, null);
		}
	}
	
	/**
	 * Sets the <code>cellEditor</code> property based on the databook and column name and adds this editor
	 * as control of the data row.
	 */
	private void installEditor()
	{
		ICellEditor ced = cellEditor;

		ColumnDefinition coldef = null;
		
		setProperty("dataRow", dataRow, true);
		
		if (columnName != null && dataRow != null)
		{			
			try
			{
				coldef = dataRow.getRowDefinition().getColumnDefinition(columnName);
				
				IDataType dataType = coldef.getDataType();

				if (ced == null)
				{
					ced = dataType.getCellEditor();
					
					if (ced == null)
					{
						ced = WebFactory.getCellEditor(dataType.getTypeClass());
					}
				}

				if (ced != null)
				{
					handler = ced.createCellEditorHandler(this, dataRow, columnName);
					
					if (handler != null)
					{
						ICellEditorHandler chComponent = ((ICellEditorHandler)handler.getCellEditorComponent());
						
						IDataRow rowNew = chComponent.getDataRow();

						if (dataRow != rowNew)
						{
							setProperty("dataRow", rowNew, true);
							
							if (rowNew instanceof DataRow)
							{
								//mark a dynamic datarow -> name is important for the client
								((DataRow)rowNew).putObject(IWebFieldConstants.NAME, "" + System.identityHashCode(rowNew));
							}	
						}

						setProperty("columnName", chComponent.getColumnName());
					}
				}
				
	            dataRow.addControl(this);
	            
	            getFactory().getLauncher().register(dataRow, this);
	            
	            notifyRepaint();            

				super.setEnabled(bEnabled);
                super.setBackground(colBackground);
			}
			catch (Exception ex)
			{
                if (logger == null)
                {
                    logger = LoggerFactory.getInstance(getClass());
                }

                logger.error(ex);

                handler = null;
                ced = dummyCellEditor;
			    
                super.setEnabled(false);
				super.setBackground(WebColor.getSystemColor(WebColor.INVALID_EDITOR_BACKGROUND));
			}
		}
		
		setProperty("cellEditor", handler != null ? ((ICellEditorHandler)handler.getCellEditorComponent()).getCellEditor() : WebFactory.getWebResource(ced), true);
	}
	
	/**
	 * Gets the value of the internal cell editor property.
	 * 
	 * @return the internal cell editor
	 */
	public ICellEditor propertyCellEditor()
	{
		 return (ICellEditor)getProperty("cellEditor", null);
	}
	
	/**
	 * Applies style properties from the used cell editor.
	 */
	private void applyStyleProperties()
	{
		try
		{
			ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(columnName);
	
			boolean editable;
			
			if (dataRow instanceof IDataBook)
			{
				IDataBook dataBook = (IDataBook)dataRow;
				
				editable = isEnabled()
						   && dataBook.isUpdateAllowed() 
						   && !columnDef.isReadOnly();
						   
				if (editable && dataBook.getReadOnlyChecker() != null)
				{
					try
					{
						editable = !dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook, columnName, dataBook.getSelectedRow(), -1);
					}
					catch (Throwable pTh)
					{
						// Ignore
					}
				}
			}
			else
			{
				editable = isEnabled() && !columnDef.isReadOnly();
			}
			
			setProperty("cellEditor_editable_", Boolean.valueOf(editable));
			
			styleEditor(columnDef, editable);
			
			ICellEditor ced = (ICellEditor)propertyCellEditor();
			
			int hAlign = getHorizontalAlignment();
			int vAlign = getVerticalAlignment();

			if (ced instanceof AbstractStyledCellEditor)
			{
				AbstractStyledCellEditor aced = ((AbstractStyledCellEditor)ced);
				
				String placeholder = aced.getPlaceholderText(this);
				
				setProperty("cellEditor_placeholder_", placeholder);
				
				if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
				{
					hAlign = aced.getHorizontalAlignment();
					
					if (hAlign == IAlignmentConstants.ALIGN_DEFAULT && ced instanceof AbstractLinkedCellEditor)
					{
					    hAlign = ((AbstractLinkedCellEditor)aced).getDefaultHorizontalAlignment(dataRow, columnName);
					}
				}
				
				if (vAlign == IAlignmentConstants.ALIGN_DEFAULT)
				{
					vAlign = aced.getVerticalAlignment();
				}
			}
			
			setProperty("cellEditor_horizontalAlignment_", Integer.valueOf(hAlign));
			setProperty("cellEditor_verticalAlignment_", Integer.valueOf(vAlign));
			
			if (ced instanceof IStyledCellEditor)
			{
				IStyledCellEditor sced = (IStyledCellEditor)ced;

				String[] sEditorStyles = getStyle().getStyleNames();
				String[] sCedStyles = sced.getStyle().getStyleNames();
				
				String[] sStyles = ArrayUtil.merge(sEditorStyles, sCedStyles);
				
				if (sStyles.length > 0)
				{
					setProperty("cellEditor_style_", StringUtil.concat(",", sStyles));
				}
			}
		}
		catch (Exception pException)
		{
			setProperty("cellEditor_editable_", Boolean.FALSE);
			setProperty("cellEditor_background_", WebColor.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND));
	
			debug(pException);
		}
	}
	
	/**
	 * Styles the editor based on the model.
	 * 
	 * @param pColumnDef the column definition
	 * @param pEditable whether the editor should be editable
	 * @throws ModelException if access to column definition fails
	 */
	private void styleEditor(ColumnDefinition pColumnDef, boolean pEditable) throws ModelException
	{
		ICellFormat cellFormat = null;
		
		if (getCellFormatter() != null)
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
				cellFormat = getCellFormatter().getCellFormat(curDataBook, curDataPage, dataRow, columnName, curSelectedRow, -1);
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
			font = getFont();
		}
		if (foreground == null && isForegroundSet())
		{
			foreground = getForeground();
		}
		if (background == null && isBackgroundSet())
		{
			background = getBackground();
		}
		
		setProperty("cellEditor_font_", font);
		
		if (pEditable)
		{
			if (background == null)
			{
				if (pColumnDef.isNullable())
				{
					background = WebColor.getSystemColor(IColor.CONTROL_BACKGROUND);
				}
				else
				{
					background = WebColor.getSystemColor(IColor.CONTROL_MANDATORY_BACKGROUND);
				}
			}
		}
		else if (background == null)
		{
			background = WebColor.getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
		}
		
		setProperty("cellEditor_background_", background);
		setProperty("cellEditor_foreground_", foreground);
	}
	
	/**
	 * Updates the accessibility attributes of this editor.
	 */
	private void updateAccessibility()
	{
		if (dataRow != null
			&& !StringUtil.isEmpty(columnName))
		{
			try
			{
				ColumnDefinition columnDefinition = dataRow.getRowDefinition().getColumnDefinition(columnName);
				
				setProperty("ariaLabel", getAriaLabel(getToolTipText(), columnDefinition.getComment(), 
						                               columnDefinition.getLabel(), columnDefinition.getName()));
			}
			catch (ModelException e)
			{
				error(e);
			}
		}
		else
		{
			setProperty("ariaLabel", null);
		}
	}
	
}	// WebEditor
