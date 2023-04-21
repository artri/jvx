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
 * 07.04.2009 - [JR] - setColumnName, setDataRow, setCellEditor now throws a ModelException
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.control;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.celleditor.UICellEditor;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.component.IEditable;
import javax.rad.ui.component.IPlaceholder;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.IEditor;

import com.sibvisions.util.type.StringUtil;

/**
 * Platform and technology independent Editor. It is designed for use with AWT,
 * Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIEditor extends AbstractControllable<IEditor>
		              implements IEditor, 
		                         IPlaceholder,
		                         IEditable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>UIEditor</code>.
	 * 
	 * @see IEditor
	 */
	public UIEditor()
	{
		this(UIFactoryManager.getFactory().createEditor());
	}
	
	/**
	 * Creates a new instance of <code>UIEditor</code> with the given editor.
	 * 
	 * @param pEditor the editor
	 * @see IEditor
	 */
	protected UIEditor(IEditor pEditor)
	{
		super(pEditor);
		
		setMaximumSize(500, 350);
	}
	
	/**
	 * Creates a new instance of <code>UIEditor</code>.
	 * 
	 * @param pDataRow the IDataRow.
	 * @param pColumnName the column name.
	 * @throws ModelException if the editor can't be initialized.
	 * @see IEditor
	 */
	public UIEditor(IDataRow pDataRow, String pColumnName) throws ModelException
	{
		this();
		
		setDataRow(pDataRow);
		setColumnName(pColumnName);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public IDataRow getDataRow()
	{
		return uiResource.getDataRow();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDataRow(IDataRow pDataRow) throws ModelException
	{
		uiResource.setDataRow(pDataRow);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook()
	{
		IDataRow dataRow = getDataRow();
		if (dataRow instanceof IDataBook)
		{
			return (IDataBook) dataRow;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getColumnName()
	{
		return uiResource.getColumnName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setColumnName(String pColumnName) throws ModelException
	{
		uiResource.setColumnName(pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditor getCellEditor()
	{
		return uiResource.getCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setCellEditor(ICellEditor pCellEditor) throws ModelException
	{
		uiResource.setCellEditor(pCellEditor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isSavingImmediate()
	{
		return uiResource.isSavingImmediate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSavingImmediate(boolean pSavingImmediate)
	{
		uiResource.setSavingImmediate(pSavingImmediate);
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
	public void setBorderVisible(boolean pVisible)
	{
		uiResource.setBorderVisible(pVisible);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isBorderVisible()
	{
		return uiResource.isBorderVisible();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
	{
		return uiResource.getHorizontalAlignment();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		uiResource.setHorizontalAlignment(pHorizontalAlignment);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
	{
		return uiResource.getVerticalAlignment();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		uiResource.setVerticalAlignment(pVerticalAlignment);
	}
	
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
	 * {@inheritDoc}
	 */
    public String getPlaceholder()
    {
    	if (uiResource instanceof IPlaceholder)
    	{
    		return ((IPlaceholder)uiResource).getPlaceholder();
    	}
    	else
    	{
    		return null;
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setPlaceholder(String pPlaceholder)
    {
    	if (uiResource instanceof IPlaceholder)
    	{
    		((IPlaceholder)uiResource).setPlaceholder(pPlaceholder);
    	}
    }

    /**
     * {@inheritDoc}
     */
    public void setEditable(boolean pEditable)
    {
        setEnabled(pEditable);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isEditable()
    {
        return isEnabled();
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
		String sColName = getColumnName();
		
		if (!StringUtil.isEmpty(sColName))
		{
			String name = createComponentNamePrefix();
			
			IDataRow row = getDataRow();
			
			if (row instanceof IDataBook)
			{
				IDataBook dataBook = (IDataBook)row;
				
				name = name + "_" + dataBook.getName();
			}
			
			name = name + "_" + sColName;
			name = incrementNameIfExists(name, getExistingNames(), false);
			
			return name;
		}
		
		return super.createComponentName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doEditIntern() throws ModelException
	{
		requestFocus();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the current used cell editor of this editor.
	 * 
	 * @param <C> is instance of ICellEditor
	 * @return the current used cell editor.
	 * @throws ModelException if it fails.
	 */
	public <C extends ICellEditor> C getCurrentCellEditor() throws ModelException
	{
		ICellEditor cellEditor = uiResource.getCellEditor();
		
		if (cellEditor == null)
		{
			cellEditor = UICellEditor.getCellEditor(uiResource.getDataRow(), uiResource.getColumnName());
		}

		return (C)cellEditor;
	}
	
}	// UIEditor
