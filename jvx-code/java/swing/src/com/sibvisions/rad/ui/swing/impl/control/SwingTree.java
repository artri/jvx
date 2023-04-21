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
 * 08.05.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.impl.control;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.ui.IImage;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ITree;
import javax.rad.util.TranslationMap;
import javax.swing.Icon;
import javax.swing.JTree;

import com.sibvisions.rad.ui.swing.ext.JVxTree;
import com.sibvisions.rad.ui.swing.ext.format.CellFormat;
import com.sibvisions.rad.ui.swing.impl.SwingScrollComponent;

/**
 * The <code>SwingTree</code> is the <code>ITree</code>
 * implementation for swing.
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JTree
 */
public class SwingTree extends SwingScrollComponent<JVxTree, JTree>
                        implements ITree, 
                                   com.sibvisions.rad.ui.swing.ext.format.ICellFormatter,
                                   com.sibvisions.rad.ui.swing.ext.format.INodeFormatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class Members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The current CellFormatter. */
	private javax.rad.ui.control.ICellFormatter cellFormatter = null;
	
	/** The current NodeFormatter. */
	private javax.rad.ui.control.INodeFormatter nodeFormatter = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingTable</code>.
	 */
	public SwingTree()
	{
		super(new JVxTree());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public IDataBook[] getDataBooks()
	{
		return resource.getDataBooks();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDataBooks(IDataBook... pDataBooks)
	{
	    IDataBook[] books = resource.getDataBooks();
	    
        if (books != null)
        {
            for (int i = 0; i < books.length; i++)
            {
                books[i].removeControl(this);
            }
        }

        resource.setDataBooks(pDataBooks);
        
        if (pDataBooks != null)
        {
            for (int i = 0; i < pDataBooks.length; i++)
            {
                pDataBooks[i].addControl(this);
            }
        }
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook()
	{
		return resource.getActiveDataBook();
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
	public boolean isDetectEndNode()
	{
		return resource.isDetectEndNode();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDetectEndNode(boolean pDetectEndNode)
	{
		resource.setDetectEndNode(pDetectEndNode);
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
	public javax.rad.ui.control.INodeFormatter getNodeFormatter()
	{
		return nodeFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNodeFormatter(javax.rad.ui.control.INodeFormatter pNodeFormatter)
	{
		nodeFormatter = pNodeFormatter;
		if (nodeFormatter == null)
		{
			resource.setNodeFormatter(null);
		}
		else
		{
			resource.setNodeFormatter(this);
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
	public Icon getNodeImage(IDataBook pDataBook, IDataPage pDataPage, IDataRow pDataRow, String pColumnName, int pRow, boolean pExpanded, boolean pLeaf)
	{
		IImage image = nodeFormatter.getNodeImage(pDataBook, pDataPage, pDataRow, pColumnName, pRow, pExpanded, pLeaf);
		if (image == null)
		{
	    	return null;
		}
		else
		{
			return (Icon)image.getResource();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseEventOnSelectedCell()
	{
		return resource.isMouseEventOnSelectedCell();
	}

}	// SwingTree
