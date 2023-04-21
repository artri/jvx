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
 * 24.10.2012 - [JR] - #604:  added constructor
 * 28.04.2014 - [JR] - #966:  added constructors
 * 03.04.2019 - [DJ] - #2000: cell tooltip added
 */
package javax.rad.genui.control;

import java.beans.Beans;
import java.math.BigDecimal;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.ICellToolTip;
import javax.rad.ui.control.ITable;

/**
 * Platform and technology independent Table.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JTable
 */
public class UITable extends AbstractControllable<ITable> 
                     implements ITable
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** Internal data for design mode. */
    private static IDataBook internalDesignModeData = null;

    /** The set dataBook. */
    private transient IDataBook dataBook = null;

    /** whether we're in design mode. */
    private transient boolean designMode = Beans.isDesignTime();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UITable</code>.
     *
     * @see ITable
     */
	public UITable()
	{
	    this(UIFactoryManager.getFactory().createTable(), null, null);
	}

    /**
     * Creates a new instance of <code>UITable</code> with the given
     * data book.
     * 
     * @param pDataBook the data book.
     * @see #setDataBook(IDataBook)
     */
    public UITable(IDataBook pDataBook)
    {
        this(UIFactoryManager.getFactory().createTable(), pDataBook, null);
    }
    
    /**
     * Creates a new instance of <code>UITable</code> with the given
     * data book and column view.
     * 
     * @param pDataBook the data book.
     * @param pColumnView the column view.
     * @see #setDataBook(IDataBook)
     * @see #setColumnView(ColumnView)
     */
    public UITable(IDataBook pDataBook, ColumnView pColumnView)
    {
        this(UIFactoryManager.getFactory().createTable(), pDataBook, pColumnView);
    }	
	
    /**
     * Creates a new instance of <code>UITable</code> with the given
     * table.
     *
     * @param pTable the table
     * @see ITable
     */
    protected UITable(ITable pTable)
    {
        this(pTable, null, null);
    }
    
    /**
     * Creates a new instance of <code>UITable</code> with the given
     * table, data book and column view.
     * 
     * @param pTable the table. 
     * @param pDataBook the data book.
     * @param pColumnView the column view.
     * @see ITable
     * @see #setDataBook(IDataBook)
     * @see #setColumnView(ColumnView)
     */
    protected UITable(ITable pTable, IDataBook pDataBook, ColumnView pColumnView)
    {
        super(pTable);

        if (pDataBook != null)
        {
            setDataBook(pDataBook);
        }
        
        if (pColumnView != null)
        {
            setColumnView(pColumnView);
        }
        
        setMaximumSize(800, 600);
        
        installTable();
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
        dataBook = pDataBook;
        
        installTable();
    }
	
	/** 
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook() 
	{
		return getDataBook();
	}

	/**
	 * {@inheritDoc}
	 */
    public ColumnView getColumnView()
    {
    	return uiResource.getColumnView();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setColumnView(ColumnView pColumnView)
    {
    	uiResource.setColumnView(pColumnView);
    }
	
	/**
	 * {@inheritDoc}
	 */
    public boolean isAutoResize()
    {
    	return uiResource.isAutoResize();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setAutoResize(boolean pAutoResize)
    {
    	uiResource.setAutoResize(pAutoResize);
    }
    
	/**
	 * {@inheritDoc}
	 */
	public int getMaxRowHeight()
	{
		return uiResource.getMaxRowHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getMinRowHeight()
	{
		return uiResource.getMinRowHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRowHeight()
	{
		return uiResource.getRowHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaxRowHeight(int pMaxRowHeight)
	{
		uiResource.setMaxRowHeight(pMaxRowHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMinRowHeight(int pMinRowHeight)
	{
		uiResource.setMinRowHeight(pMinRowHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRowHeight(int pRowHeight)
	{
		uiResource.setRowHeight(pRowHeight);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTableHeaderVisible()
	{
		return uiResource.isTableHeaderVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTableHeaderVisible(boolean pTableHeaderVisible)
	{
		uiResource.setTableHeaderVisible(pTableHeaderVisible);
	}

	/**
	 * {@inheritDoc}
	 */
    public boolean isSortOnHeaderEnabled()
	{
		return uiResource.isSortOnHeaderEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
    public void setSortOnHeaderEnabled(boolean pSortOnHeaderEnabled)
	{
		uiResource.setSortOnHeaderEnabled(pSortOnHeaderEnabled);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getEnterNavigationMode()
	{
		return uiResource.getEnterNavigationMode();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTabNavigationMode()
	{
		return uiResource.getTabNavigationMode();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnterNavigationMode(int pNavigationMode)
	{
		uiResource.setEnterNavigationMode(pNavigationMode);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTabNavigationMode(int pNavigationMode)
	{
		uiResource.setTabNavigationMode(pNavigationMode);
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint() 
	{
    	uiResource.notifyRepaint();
	}

	/**
	 * {@inheritDoc}
	 */
	public void startEditing() 
	{
    	uiResource.startEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException 
	{
    	uiResource.saveEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing() 
	{
    	uiResource.cancelEditing();
	}

	/**
	 * {@inheritDoc}
	 */
	public ICellFormatter getCellFormatter()
	{
		return uiResource.getCellFormatter();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setCellFormatter(ICellFormatter pCellFormatter)
	{
		uiResource.setCellFormatter(pCellFormatter);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEditable()
	{
		return uiResource.isEditable();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean pEditable)
	{
		uiResource.setEditable(pEditable);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowSelection()
	{
		return uiResource.isShowSelection();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowSelection(boolean pShowSelection)
	{
		uiResource.setShowSelection(pShowSelection);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowFocusRect()
	{
		return uiResource.isShowFocusRect();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowFocusRect(boolean pShowFocusRect)
	{
		uiResource.setShowFocusRect(pShowFocusRect);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowVerticalLines()
	{
		return uiResource.isShowVerticalLines();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowVerticalLines(boolean pShowVerticalLines)
	{
		uiResource.setShowVerticalLines(pShowVerticalLines);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowHorizontalLines()
	{
		return uiResource.isShowHorizontalLines();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowHorizontalLines(boolean pShowHorizontalLines)
	{
		uiResource.setShowHorizontalLines(pShowHorizontalLines);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseEventOnSelectedCell()
	{
		return uiResource.isMouseEventOnSelectedCell();
	}
	
	/**
     * {@inheritDoc}
     */
    public ICellToolTip getCellToolTip()
    {
        return uiResource.getCellToolTip();
    }

    /**
     * {@inheritDoc}
     */
    public void setCellToolTip(ICellToolTip pCellTooltip)
    {
        uiResource.setCellToolTip(pCellTooltip);
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
		
		if (bChanged)
		{
			uiResource.setTranslation(getCurrentTranslation());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createComponentName()
	{
		if (getDataBook() != null)
		{
			String name = createComponentNamePrefix() + "_" + getDataBook().getName();
			name = incrementNameIfExists(name, getExistingNames(), false);
			
			return name;
		}
		
		return super.createComponentName();
	}

	/**
	 * Starts editing.
	 */
	@Override
	protected void doEditIntern()
	{
	    startEditing();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * Sets the cell formatter.
     *
     * @param pCellFormatter the cell formatter.
     * @param pMethodName the method name.
     */
    public void setCellFormatter(Object pCellFormatter, String pMethodName)
    {
        uiResource.setCellFormatter(createCellFormatter(pCellFormatter, pMethodName));
    }
    
	/**
     * Gets if the horizontal lines or vertical lines are shown.
     * @return true, the horizontal lines or vertical lines are shown.
     */
    public boolean isShowGridLines()
    {
        return uiResource.isShowHorizontalLines() || uiResource.isShowVerticalLines();
    }

    /**
     * Sets if the horizontal lines or vertical lines are shown.
     * @param pShowVerticalLines true, the horizontal lines or vertical lines are shown.
     */
    public void setShowGridLines(boolean pShowVerticalLines)
    {
        uiResource.setShowHorizontalLines(pShowVerticalLines);
        uiResource.setShowVerticalLines(pShowVerticalLines);
    }

    /**
     * Installs either a dummy binding for design mode or the real binding.
     */
    protected void installTable()
    {
        if (designMode && dataBook == null)
        {
            uiResource.setDataBook(getInternalDesignModeData());
        }
        else
        {
            uiResource.setDataBook(dataBook);
        }
    }
    
    /**
     * Gets internal data for design mode.
     * 
     * @return internal data for design mode.
     */
    protected static IDataBook getInternalDesignModeData()
    {
        if (internalDesignModeData == null)
        {
            try
            {
                internalDesignModeData = (IDataBook)Class.forName("com.sibvisions.rad.model.mem.MemDataBook").newInstance();
                internalDesignModeData.setName("internalDesignModeData");
                internalDesignModeData.getRowDefinition().addColumnDefinition(new ColumnDefinition("A"));
                internalDesignModeData.getRowDefinition().addColumnDefinition(new ColumnDefinition("B", new BigDecimalDataType()));
                internalDesignModeData.getRowDefinition().addColumnDefinition(new ColumnDefinition("C", new BigDecimalDataType()));
                internalDesignModeData.open();
                
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"Test 1", BigDecimal.valueOf(4), BigDecimal.valueOf(1)});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"Test 2", BigDecimal.valueOf(3), BigDecimal.valueOf(2)});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"Test 3", BigDecimal.valueOf(2), BigDecimal.valueOf(3)});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"Test 4", BigDecimal.valueOf(1), BigDecimal.valueOf(4)});
                internalDesignModeData.saveSelectedRow();
                internalDesignModeData.setSelectedRow(0);
            }
            catch (Exception ex)
            {
                // ignore
            }
        }
        
        return internalDesignModeData;
    }
    
}	// UITable
