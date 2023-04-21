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
 * 09.10.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext.celleditor;

import java.awt.Component;
import java.awt.Dimension;

import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.swing.JComponent;

import com.sibvisions.rad.ui.celleditor.AbstractImageViewer;
import com.sibvisions.rad.ui.swing.ext.JVxIcon;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.cellrenderer.JVxIconRenderer;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>JVxImageViewer</code> provides the generation of the 
 * physical image viewer component, handles correct all events, and 
 * gives standard access to values.
 * 
 * @author Martin Handsteiner
 */
public class JVxImageViewer extends AbstractImageViewer<Component>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The cell renderer. */
	private JVxIconRenderer cellRenderer = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new JVxChoiceCellEditor.
	 */
	public JVxImageViewer()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditorHandler<JComponent> createCellEditorHandler(ICellEditorListener pCellEditorListener, 
										                          IDataRow pDataRow, String pColumnName) 
	{
		return new CellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getCellRendererComponent(Component pParentComponent,
								              IDataPage pDataPage,
									          int       pRowNumber,
									          IDataRow  pDataRow,
									          String    pColumnName,
									          boolean   pIsSelected,
									          boolean   pHasFocus)
	{
		if (cellRenderer == null)
		{
			cellRenderer = new JVxIconRenderer();
		}
		cellRenderer.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(getHorizontalAlignment()));
		cellRenderer.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(getVerticalAlignment()));
		cellRenderer.setPreserveAspectRatio(isPreserveAspectRatio());
		
		try
		{
			Object value = pDataRow.getValue(pColumnName);

			if (value instanceof String)
			{
				cellRenderer.setImage(JVxUtil.getImage((String)value));
			}
			else if (value instanceof byte[])
			{
				cellRenderer.setImage(JVxUtil.getImage(null, (byte[])value));
			}
			else
			{
				cellRenderer.setImage(JVxUtil.getImage(sDefaultImageName));
			}
		}
		catch (Exception me)
		{
			cellRenderer.setImage(JVxUtil.getImage(sDefaultImageName));
		}

		return cellRenderer;
	}

	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
     * Sets the internal changed Flag, and informs the CellEditorListener 
     * if editing is completed.
     * 
     * @author Martin Handsteiner
     */
    private static class CellEditorHandler implements ICellEditorHandler<JComponent>
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private JVxImageViewer cellEditor;
    	
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellEditorListener cellEditorListener;
    	
    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** The column name of the edited column. */
    	private String columnName;

    	/** The CellEditorListener to inform, if editing is completed. */
    	private JVxIcon cellEditorComponent;
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * Constructs a new CellEditorHandler.
    	 * 
    	 * @param pCellEditor the CellEditor that created this handler.
    	 * @param pCellEditorListener CellEditorListener to inform, if editing is started or completed.
    	 * @param pDataRow the data row that is edited.
    	 * @param pColumnName the column name of the edited column.
    	 */
    	public CellEditorHandler(JVxImageViewer pCellEditor, ICellEditorListener pCellEditorListener, 
				 				 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		columnName = pColumnName;
    		
    		cellEditorComponent = new JVxIcon();
    		cellEditorComponent.setMinimumSize(new Dimension(16, 16));
			cellEditorComponent.setImage(JVxUtil.getImage(cellEditor.sDefaultImageName));
			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
			{	// use alignment of editors, if possible.
				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
			}
			else
			{
        		cellEditorComponent.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(cellEditor.getHorizontalAlignment()));
        		cellEditorComponent.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(cellEditor.getVerticalAlignment()));
			}
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	// ICellEditorHandler
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	public void uninstallEditor()
    	{
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
    	public JComponent getCellEditorComponent()
    	{
    		return cellEditorComponent;
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
    			Object value = dataRow.getValue(columnName);

    			if (value instanceof String)
    			{
    				cellEditorComponent.setImage(JVxUtil.getImage((String)value));
    			}
    			else if (value instanceof byte[])
    			{
    				cellEditorComponent.setImage(JVxUtil.getImage(null, (byte[])value));
    			}
    			else
    			{
    				cellEditorComponent.setImage(JVxUtil.getImage(cellEditor.sDefaultImageName));
    			}
    			
    			if (dynamicAlignment != null)
	    		{
	    			int hAlign = dynamicAlignment.getHorizontalAlignment();
	    			if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
	    			{
	    				hAlign = cellEditor.getHorizontalAlignment();
	    			}
	        		cellEditorComponent.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(hAlign));
	    			int vAlign = dynamicAlignment.getVerticalAlignment();
	    			if (vAlign == IAlignmentConstants.ALIGN_DEFAULT)
	    			{
	    				vAlign = cellEditor.getVerticalAlignment();
	    			}
	        		cellEditorComponent.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(vAlign));
	    		}
    			
    			cellEditorComponent.setPreserveAspectRatio(cellEditor.isPreserveAspectRatio());
    		}
    		catch (Exception pException)
    		{
    			cellEditorComponent.setImage(JVxUtil.getImage(cellEditor.sDefaultImageName));
    			
    			throw new ModelException("Editor cannot be restored!", pException);
    		}
    	}
    }
    	
}	// JVxImageViewer
