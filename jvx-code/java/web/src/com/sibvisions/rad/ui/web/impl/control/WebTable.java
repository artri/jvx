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
 * 23.07.2010 - [JR] - #149: setDataBook: RESTORE, RELOAD events set
 * 18.01.2011 - [JR] - setReload: force implemented (support for updates during value changed)
 * 10.04.2013 - [JR] - calculatePreferredSize: check dataBook != null [BUGFIX]
 */
package com.sibvisions.rad.ui.web.impl.control;

import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ITableControl;
import javax.rad.ui.control.ICellToolTip;
import javax.rad.ui.control.ITable;
import javax.rad.util.TranslationMap;

/**
 * Web server implementation of {@link ITable}.
 * 
 * @author Martin Handsteiner
 */
public class WebTable extends AbstractCellFormatable
                      implements ITable,
                                 Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the data book. */
	private IDataBook dataBook = null;
	
	/** The column view. */
	private ColumnView columnView = null;
	
	/** The translation map for internal translations. */
	private TranslationMap translation = null;
	
    /** current CellToolTip. */
    private ICellToolTip cellToolTip;

    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
    
	/** reduces notifyRepaint calls. */
    private boolean firstNotifyRepaintCall = true;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebTable</code>.
     *
     * @see javax.rad.ui.control.ITable
     */
	public WebTable()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public IDataBook getDataBook()
    {
    	return dataBook;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setDataBook(IDataBook pDataBook)
    {
    	if (dataBook != null)
    	{
    		dataBook.removeControl(this);
    		
    		if (columnView != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.removeRowDefinition(dataBook.getRowDefinition());
    		}
    		
    		getFactory().getLauncher().register(dataBook, null);
    	}
    	
    	dataBook = pDataBook;
    	
    	setProperty("dataBook", dataBook, true);
    	
    	if (dataBook != null)
    	{
    		dataBook.addControl(this);
    		
    		getFactory().getLauncher().register(dataBook, this);
    		
    		//forces sync
    		notifyRepaint();
    		
    		if (columnView != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.addRowDefinition(dataBook.getRowDefinition());
    		}
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
    public ColumnView getColumnView()
    {
		if (columnView == null && dataBook != null)
		{
			return dataBook.getRowDefinition().getColumnView(ITableControl.class);
		}
		else
		{
			return columnView;
		}
    }

	/**
	 * {@inheritDoc}
	 */
    public void setColumnView(ColumnView pColumnView)
    {
    	if (columnView != pColumnView)
    	{
    		if (columnView != null && dataBook != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.removeRowDefinition(dataBook.getRowDefinition());
    		}
    		
        	columnView = pColumnView;
    		
    		if (columnView != null && dataBook != null && columnView != dataBook.getRowDefinition().getColumnView(ITableControl.class))
    		{
    			columnView.addRowDefinition(dataBook.getRowDefinition());
    		}
    		
    		notifyRepaint();
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
    public boolean isAutoResize()
    {
    	return ((Boolean)getProperty("autoResize", Boolean.TRUE)).booleanValue();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setAutoResize(boolean pAutoResize)
    {
    	setProperty("autoResize", Boolean.valueOf(pAutoResize));
    }
    
	/**
	 * {@inheritDoc}
	 */
	public int getMaxRowHeight()
	{
		return ((Integer)getProperty("maxRowHeight", Integer.valueOf(120))).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMinRowHeight()
	{
		return ((Integer)getProperty("minRowHeight", Integer.valueOf(20))).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRowHeight()
	{
		return ((Integer)getProperty("rowHeight", Integer.valueOf(20))).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaxRowHeight(int pMaxRowHeight)
	{
		setProperty("maxRowHeight", Integer.valueOf(pMaxRowHeight));
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMinRowHeight(int pMinRowHeight)
	{
		setProperty("minRowHeight", Integer.valueOf(pMinRowHeight));
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRowHeight(int pRowHeight)
	{
		setProperty("rowHeight", Integer.valueOf(pRowHeight));
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTableHeaderVisible()
	{
		return ((Boolean)getProperty("tableHeaderVisible", Boolean.TRUE)).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTableHeaderVisible(boolean pTableHeaderVisible)
	{
		setProperty("tableHeaderVisible", Boolean.valueOf(pTableHeaderVisible));
	}

	/**
	 * {@inheritDoc}
	 */
    public boolean isSortOnHeaderEnabled()
	{
		return ((Boolean)getProperty("sortOnHeaderEnabled", Boolean.TRUE)).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
    public void setSortOnHeaderEnabled(boolean pSortOnHeaderEnabled)
	{
		setProperty("sortOnHeaderEnabled", Boolean.valueOf(pSortOnHeaderEnabled));
	}

	/**
     * Gets the ENTER navigation mode.
     *
     * @return the ENTER navigation mode.
     */
    public int getEnterNavigationMode()
	{
		return ((Integer)getProperty("enterNavigationMode", Integer.valueOf(NAVIGATION_CELL_AND_FOCUS))).intValue();
	}

	/**
     * Sets the ENTER navigation mode.
     *
     * @param pNavigationMode the ENTER navigation mode.
     */
    public void setEnterNavigationMode(int pNavigationMode)
	{
		setProperty("enterNavigationMode", Integer.valueOf(pNavigationMode));
	}

	/**
     * Gets the ENTER navigation mode.
     *
     * @return the ENTER navigation mode.
     */
    public int getTabNavigationMode()
	{
		return ((Integer)getProperty("tabNavigationMode", Integer.valueOf(NAVIGATION_CELL_AND_FOCUS))).intValue();
	}

	/**
     * Sets the ENTER navigation mode.
     *
     * @param pNavigationMode the ENTER navigation mode.
     */
    public void setTabNavigationMode(int pNavigationMode)
	{
		setProperty("tabNavigationMode", Integer.valueOf(pNavigationMode));
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint() 
	{
		if (dataBook != null)
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
	public void startEditing()
	{
		setCommandProperty("startEditing", Boolean.TRUE);
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
	public boolean isEditable()
	{
		return getProperty("editable", Boolean.TRUE).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean pEditable)
	{
		setProperty("editable", Boolean.valueOf(pEditable));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowSelection()
	{
		return ((Boolean)getProperty("showSelection", Boolean.TRUE)).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowSelection(boolean pShowSelection)
	{
		setProperty("showSelection", Boolean.valueOf(pShowSelection));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowFocusRect()
	{
		return ((Boolean)getProperty("showFocusRect", Boolean.TRUE)).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowFocusRect(boolean pShowFocusRect)
	{
		setProperty("showFocusRect", Boolean.valueOf(pShowFocusRect));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowVerticalLines()
	{
		return ((Boolean)getProperty("showVerticalLines", Boolean.TRUE)).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowVerticalLines(boolean pShowVerticalLines)
	{
		setProperty("showVerticalLines", Boolean.valueOf(pShowVerticalLines));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowHorizontalLines()
	{
		return ((Boolean)getProperty("showHorizontalLines", Boolean.TRUE)).booleanValue();		
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowHorizontalLines(boolean pShowHorizontalLines)
	{
		setProperty("showHorizontalLines", Boolean.valueOf(pShowHorizontalLines));
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
	public boolean isMouseEventOnSelectedCell()
	{
		return true;
	}
	
	/**
     * {@inheritDoc}
     */
    public ICellToolTip getCellToolTip()
    {
        return cellToolTip;
    }

    /**
     * {@inheritDoc}
     */
    public void setCellToolTip(ICellToolTip pCellTooltip)
    {
        cellToolTip = pCellTooltip;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
		firstNotifyRepaintCall = true;
		
		try
		{
			//forces sync
			dataBook.getSelectedRow();
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
     * Sets whether word wrap is enabled.
     * 
     * @param pEnabled <code>true</code> to enable word wrap, <code>false</code> otherwise
     */
    public void setWordWrapEnabled(boolean pEnabled)
    {
    	setProperty("wordWrapEnabled", Boolean.valueOf(pEnabled));
    }
    
    /**
     * Gets whether word wrap is enabled.
     * 
     * @return <code>true</code> if word wrap is enabled, <code>false</code> otherwise
     */
    public boolean isWordWrapEnabled()
    {
    	return ((Boolean)getProperty("wordWrapEnabled", Boolean.FALSE)).booleanValue();
    }
    
    /**
     * Sets whether delete is enabled.
     * 
     * @param pEnabled <code>true</code> to enable delete, <code>false</code> otherwise
     */
    public void setDeleteEnabled(boolean pEnabled)
    {
        setProperty("deleteEnabled", Boolean.valueOf(pEnabled));
    }
    
    /**
     * Gets whether delete is enabled.
     * 
     * @return <code>true</code> if delete is enabled, <code>false</code> otherwise
     */
    public boolean isDeleteEnabled()
    {
        return ((Boolean)getProperty("deleteEnabled", Boolean.FALSE)).booleanValue();
    }
    
}	// WebTable
