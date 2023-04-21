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
 * 05.06.2009 - [JR] - set/getTranslation implemented
 * 03.04.2019 - [DJ] - #2000: table cell tooltip functionality added
 */
package com.sibvisions.rad.ui.swing.impl.control;

import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ITable;
import javax.rad.util.TranslationMap;
import javax.swing.JTable;

import com.sibvisions.rad.ui.swing.ext.JVxTable;
import com.sibvisions.rad.ui.swing.ext.format.CellFormat;
import com.sibvisions.rad.ui.swing.impl.SwingScrollComponent;

/**
 * The <code>SwingTable</code> is the <code>ITable</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JTable
 */
public class SwingTable extends SwingScrollComponent<JVxTable, JTable>
                        implements ITable, 
                                   com.sibvisions.rad.ui.swing.ext.format.ICellFormatter,
                                   com.sibvisions.rad.ui.swing.ext.tooltip.ICellToolTip
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class Members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The current Cellformatter. */
	private javax.rad.ui.control.ICellFormatter cellFormatter = null;
	
	/** The current ICellToolTip. */
	private javax.rad.ui.control.ICellToolTip cellToolTip = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingTable</code>.
	 */
	public SwingTable()
	{
		super(new JVxTable());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public IDataBook getDataBook()
	{
		return resource.getDataBook();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDataBook(IDataBook pDataBook)
	{
        IDataBook book = resource.getDataBook();
        
        if (book != null)
        {
            book.removeControl(this);
        }
        
        resource.setDataBook(pDataBook);
        
        if (pDataBook != null)
        {
            pDataBook.addControl(this);
        }
	}

	/**
	 * {@inheritDoc}
	 */
    public ColumnView getColumnView()
    {
    	return resource.getColumnView();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setColumnView(ColumnView pColumnView)
    {
    	resource.setColumnView(pColumnView);
    }
	
	/**
	 * {@inheritDoc}
	 */
    public boolean isAutoResize()
    {
    	return resource.isAutoResize();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setAutoResize(boolean pAutoResize)
    {
    	resource.setAutoResize(pAutoResize);
    }
    
	/**
	 * {@inheritDoc}
	 */
	public int getMaxRowHeight()
	{
		return resource.getMaxRowHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMinRowHeight()
	{
		return resource.getMinRowHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRowHeight()
	{
		return resource.getRowHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaxRowHeight(int pMaxRowHeight)
	{
		resource.setMaxRowHeight(pMaxRowHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMinRowHeight(int pMinRowHeight)
	{
		resource.setMinRowHeight(pMinRowHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRowHeight(int pRowHeight)
	{
		resource.setRowHeight(pRowHeight);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTableHeaderVisible()
	{
		return resource.isTableHeaderVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTableHeaderVisible(boolean pTableHeaderVisible)
	{
		resource.setTableHeaderVisible(pTableHeaderVisible);
	}

	/**
	 * {@inheritDoc}
	 */
    public boolean isSortOnHeaderEnabled()
	{
		return resource.isSortOnHeaderEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
    public void setSortOnHeaderEnabled(boolean pSortOnHeaderEnabled)
	{
    	resource.setSortOnHeaderEnabled(pSortOnHeaderEnabled);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getEnterNavigationMode()
	{
		return resource.getEnterNavigationMode();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTabNavigationMode()
	{
		return resource.getTabNavigationMode();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnterNavigationMode(int pNavigationMode)
	{
		resource.setEnterNavigationMode(pNavigationMode);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTabNavigationMode(int pNavigationMode)
	{
		resource.setTabNavigationMode(pNavigationMode);
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
	public void startEditing()
	{
		resource.startEditing();
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
	public void saveEditing() throws ModelException
	{
		resource.saveEditing();
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
	public boolean isEditable()
	{
		return resource.isEditable();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean pEditable)
	{
		resource.setEditable(pEditable);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowSelection()
	{
		return resource.isShowSelection();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowSelection(boolean pShowSelection)
	{
		resource.setShowSelection(pShowSelection);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowFocusRect()
	{
		return resource.isShowFocusRect();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowFocusRect(boolean pShowFocusRect)
	{
		resource.setShowFocusRect(pShowFocusRect);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowVerticalLines()
	{
		return resource.getJTable().getShowVerticalLines();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowVerticalLines(boolean pShowVerticalLines)
	{
		resource.getJTable().setShowVerticalLines(pShowVerticalLines);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowHorizontalLines()
	{
		return resource.getJTable().getShowHorizontalLines();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowHorizontalLines(boolean pShowHorizontalLines)
	{
		resource.getJTable().setShowHorizontalLines(pShowHorizontalLines);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseEventOnSelectedCell()
	{
		return resource.isMouseEventOnSelectedCell();
	}

	/**
	 * {@inheritDoc}
	 */
	public javax.rad.ui.control.ICellToolTip getCellToolTip()
	{
		return cellToolTip;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCellToolTip(javax.rad.ui.control.ICellToolTip pCellTooltip)
	{
		cellToolTip = pCellTooltip;
		if (cellToolTip == null)
		{
			resource.setCellToolTip(null);
		}
		else
		{
			resource.setCellToolTip(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName,
			int pRow, int pColumn)
	{
		return cellToolTip.getToolTipText(pDataBook, pDataPage, pDataRow, pColumnName, pRow, pColumn);
	}

}	// SwingTable
