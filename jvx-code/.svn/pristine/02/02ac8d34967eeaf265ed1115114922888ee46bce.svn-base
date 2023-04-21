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
 * 13.10.2008 - [HM] - creation
 * 07.04.2009 - [JR] - setColumnName, setDataRow, setCellEditor now throws a ModelException
 */
package com.sibvisions.rad.ui.swing.impl.control;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.component.IPlaceholder;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.IEditor;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.swing.ext.JVxEditor;
import com.sibvisions.rad.ui.swing.ext.format.CellFormat;
import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The <code>SwingEditor</code> is the <code>IEditor</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	com.sibvisions.rad.ui.swing.ext.JVxEditor
 */
public class SwingEditor extends SwingComponent<JVxEditor>
					  	 implements IEditor, 
					  	 			IPlaceholder,
					  	 			com.sibvisions.rad.ui.swing.ext.format.ICellFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class Members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The current Cellformatter. */
	private javax.rad.ui.control.ICellFormatter cellFormatter = null;
	
	/** the column name. */
	private String sColumnName;
	
	/** the row. */
	private IDataRow dataRow;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingEditor</code>.
	 */
	public SwingEditor()
	{
		super(new JVxEditor());
		
		super.setHorizontalAlignment(resource.getHorizontalAlignment());
		super.setVerticalAlignment(resource.getVerticalAlignment());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public IDataRow getDataRow()
	{
		return resource.getDataRow();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDataRow(IDataRow pDataRow) throws ModelException
	{
	    uninstallEditor();
	    
	    dataRow = pDataRow;
		resource.setDataRow(pDataRow);
		
		installEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getColumnName()
	{
		return resource.getColumnName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setColumnName(String pColumnName) throws ModelException
	{
	    uninstallEditor();

	    sColumnName = pColumnName;
		resource.setColumnName(pColumnName);
		
		installEditor();
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellEditor getCellEditor()
	{
		return resource.getCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setCellEditor(ICellEditor pCellEditor) throws ModelException
	{
		resource.setCellEditor(pCellEditor);
	}
	
	/**
	 * {@inheritDoc}
	 */
    public boolean isSavingImmediate()
    {
    	return resource.isSavingImmediate();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setSavingImmediate(boolean pSavingImmediate)
    {
    	resource.setSavingImmediate(pSavingImmediate);
    }

	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing()
	{
		resource.cancelEditing();
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		resource.notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException
	{
		resource.saveEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public javax.rad.ui.control.ICellFormatter getCellFormatter()
	{
		return cellFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setCellFormatter(javax.rad.ui.control.ICellFormatter pCellFormatter)
	{
		cellFormatter = pCellFormatter;
		if (cellFormatter == null)
		{
			resource.setCellFormatter(null);
		}
		else
		{
			resource.setCellFormatter(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public CellFormat getCellFormat(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, int pColumn) throws Throwable
	{
		ICellFormat cellFormat = cellFormatter.getCellFormat(pDataBook, pDataPage, pDataRow, pColumnName, pRow, pColumn);
		if (cellFormat == null)
		{
	    	return null;
		}
		else
		{
			return (CellFormat)cellFormat.getResource();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setTranslation(TranslationMap pTranslation)
	{
		resource.setTranslation(pTranslation);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TranslationMap getTranslation()
	{
		return resource.getTranslation();
	}

    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        resource.setTranslationEnabled(pEnabled);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return resource.isTranslationEnabled();
    }
	
    /**
     * {@inheritDoc}
     */
    public String translate(String pText)
    {
        return resource.translate(pText);
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderVisible() 
	{
		return resource.isBorderVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBorderVisible(boolean pVisible) 
	{
		resource.setBorderVisible(pVisible);
	}

	/**
	 * {@inheritDoc}
	 */
    public String getPlaceholder()
    {
		return resource.getPlaceholder();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setPlaceholder(String pPlaceholder)
    {
    	resource.setPlaceholder(pPlaceholder);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		super.setHorizontalAlignment(pHorizontalAlignment);
		resource.setHorizontalAlignment(pHorizontalAlignment);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		super.setVerticalAlignment(pVerticalAlignment);
		resource.setVerticalAlignment(pVerticalAlignment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return resource.getEditorComponentName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String pName)
	{
		resource.setEditorComponentName(pName);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Removes this editor as control from the data row.
     */
    private void uninstallEditor()
    {
        if (sColumnName != null && dataRow != null)
        {
            dataRow.removeControl(this);
        }
    }
    
    /**
     * Sets the <code>cellEditor</code> property based on the databook and column name and adds this editor
     * as control of the data row.
     */
    private void installEditor()
    {
        if (sColumnName != null && dataRow != null)
        {
            dataRow.addControl(this);
        }   
    }
	
}	// SwingEditor
