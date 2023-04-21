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
 * 17.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 * 22.04.2014 - [RZ] - #1014: implemented get/setDisplayReferencedColumnName
 */
package javax.rad.genui.celleditor;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ColumnView;
import javax.rad.model.condition.ICondition;
import javax.rad.model.reference.ColumnMapping;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.ui.IDimension;
import javax.rad.ui.celleditor.ILinkedCellEditor;

/**
 * Platform and technology independent linked editor.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UILinkedCellEditor extends UIComboCellEditor<ILinkedCellEditor> 
                                implements ILinkedCellEditor
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The default column view. */
    private ColumnView defaultColumnView = null;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UILinkedCellEditor</code>.
     *
     * @see ILinkedCellEditor
     */
	public UILinkedCellEditor()
	{
		super(UIFactoryManager.getFactory().createLinkedCellEditor());
	}

    /**
     * Creates a new instance of <code>UILinkedCellEditor</code> with the given 
     * linked cell editor.
     * 
     * @param pEditor the linked cell editor
     * @see ILinkedCellEditor
     */
	protected UILinkedCellEditor(ILinkedCellEditor pEditor)
	{
		super(pEditor);
	}
	
    /**
     * Creates a new instance of <code>UILinkedCellEditor</code> with LinkReference.
     *
     * @param pReferenceDefinition the LinkReference.
     * @see ILinkedCellEditor
     */
	public UILinkedCellEditor(ReferenceDefinition pReferenceDefinition)
	{
		super(UIFactoryManager.getFactory().createLinkedCellEditor());
		
		getUIResource().setLinkReference(pReferenceDefinition);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public ReferenceDefinition getLinkReference()
	{
		return getUIResource().getLinkReference();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLinkReference(ReferenceDefinition pReferenceDefinition)
	{
		getUIResource().setLinkReference(pReferenceDefinition);
	}

	/**
	 * {@inheritDoc}
	 */
	public ICondition getAdditionalCondition()
	{
		return getUIResource().getAdditionalCondition();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAdditionalCondition(ICondition pCondition)
	{
		getUIResource().setAdditionalCondition(pCondition);
	}

	/**
	 * {@inheritDoc}
	 */
	public ColumnMapping getSearchColumnMapping()
	{
		return getUIResource().getSearchColumnMapping();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSearchColumnMapping(ColumnMapping pSearchColumnNames)
	{
		getUIResource().setSearchColumnMapping(pSearchColumnNames);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isSortByColumnName()
	{
		return getUIResource().isSortByColumnName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSortByColumnName(boolean pSortByColumnName)
	{
		getUIResource().setSortByColumnName(pSortByColumnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTableReadonly()
	{
		return getUIResource().isTableReadonly();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTableReadonly(boolean pTableReadonly)
	{
		getUIResource().setTableReadonly(pTableReadonly);
	}

    /**
     * {@inheritDoc}
     */
    public boolean isAutoTableHeaderVisibility()
    {
        return getUIResource().isAutoTableHeaderVisibility();
    }

    /**
     * {@inheritDoc}
     */
	public boolean isTableHeaderVisible()
	{
		return getUIResource().isTableHeaderVisible();
	}

    /**
     * {@inheritDoc}
     */
	public void setTableHeaderVisible(boolean pTableHeaderVisible)
	{
		getUIResource().setTableHeaderVisible(pTableHeaderVisible);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValidationEnabled()
	{
		return getUIResource().isValidationEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValidationEnabled(boolean pValidationEnabled)
	{
		getUIResource().setValidationEnabled(pValidationEnabled);
	}
	
	/**
	 * {@inheritDoc}
	 */
    public ColumnView getColumnView()
    {
    	return getUIResource().getColumnView();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setColumnView(ColumnView pColumnView)
    {
        if (pColumnView == null)
        {
            getUIResource().setColumnView(defaultColumnView);
        }
        else
        {
            getUIResource().setColumnView(pColumnView);
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IDimension getPopupSize()
	{
		return getUIResource().getPopupSize();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPopupSize(IDimension pPopupSize)
	{
		getUIResource().setPopupSize(pPopupSize);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSearchTextAnywhere()
	{
		return getUIResource().isSearchTextAnywhere();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSearchTextAnywhere(boolean pSearchTextAnywhere)
	{
		getUIResource().setSearchTextAnywhere(pSearchTextAnywhere);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSearchInAllTableColumns()
	{
		return getUIResource().isSearchInAllTableColumns();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSearchInAllTableColumns(boolean pSearchInAllVisibleColumns)
	{
		getUIResource().setSearchInAllTableColumns(pSearchInAllVisibleColumns);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDisplayReferencedColumnName()
	{
		return getUIResource().getDisplayReferencedColumnName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDisplayReferencedColumnName(String pDisplayReferencedColumnName)
	{
		getUIResource().setDisplayReferencedColumnName(pDisplayReferencedColumnName);
	}
		
	/**
	 * {@inheritDoc}
	 */
	public String getDisplayConcatMask()
	{
		return getUIResource().getDisplayConcatMask();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDisplayConcatMask(String pDisplayConcatMask)
	{
		getUIResource().setDisplayConcatMask(pDisplayConcatMask);
	}
		
	/**
	 * {@inheritDoc}
	 */
	public String[] getDoNotClearColumnNames()
	{
		return getUIResource().getDoNotClearColumnNames();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDoNotClearColumnNames(String... pDoNotClearColumnNames)
	{
		getUIResource().setDoNotClearColumnNames(pDoNotClearColumnNames);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the default column view.
	 * 
	 * @return the default column view.
	 */
	public ColumnView getDefaultColumnView()
	{
	    return defaultColumnView;
	}
	
	/**
	 * Sets the default column view.
	 * 
	 * @param pDefaultColumnView the default column view
	 */
	public void setDefaultColumnView(ColumnView pDefaultColumnView)
	{
	    if (getColumnView() == defaultColumnView)
	    {
	        setColumnView(pDefaultColumnView);
	    }
	    
        defaultColumnView = pDefaultColumnView;
	}
	
}	// UILinkedCellEditor
