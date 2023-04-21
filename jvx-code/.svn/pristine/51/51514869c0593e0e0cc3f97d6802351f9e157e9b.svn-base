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
 */
package com.sibvisions.rad.ui.web.impl.control;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.ui.control.ITree;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.web.impl.WebComponent;

/**
 * Web server implementation of {@link ITree}.
 * 
 * @author Martin Handsteiner
 */
public class WebTree extends WebComponent
                     implements ITree,
                                Runnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the data books. */
	private IDataBook[] dataBooks = null;

	/** The translation map for internal translations. */
	private TranslationMap translation = null;
	
	/** The current CellFormatter. */
	private ICellFormatter cellFormatter = null;
	/** The current NodeFormatter. */
	private INodeFormatter nodeFormatter = null;
	
    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
    
	/** reduces notifyRepaint calls. */
    private boolean firstNotifyRepaintCall = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebTree</code>.
     *
     * @see javax.rad.ui.control.ITree
     */
	public WebTree()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public IDataBook[] getDataBooks()
    {
    	return dataBooks;
    }

	/**
	 * {@inheritDoc}
	 */
    public void setDataBooks(IDataBook... pDataBooks)
    {
        if (dataBooks != null)
        {
            for (int i = 0; i < dataBooks.length; i++)
            {
                dataBooks[i].removeControl(this);
                
                getFactory().getLauncher().register(dataBooks[i], null);
            }
        }

    	dataBooks = pDataBooks;
    	
        if (dataBooks != null)
        {
            for (int i = 0; i < dataBooks.length; i++)
            {
                dataBooks[i].addControl(this);
                
                getFactory().getLauncher().register(dataBooks[i], this);
            }
            
    		//forces sync
    		notifyRepaint();
        }
        
        setProperty("dataBooks", dataBooks, true);
    }

	/**
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDetectEndNode()
	{
		return getProperty("detectEndNode", Boolean.TRUE).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDetectEndNode(boolean pDetectEndNode)
	{
		setProperty("detectEndNode", Boolean.valueOf(pDetectEndNode));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEditable()
	{
		return getProperty("editable", Boolean.FALSE).booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean pEditable)
	{
		setProperty("enabled", Boolean.valueOf(pEditable));
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint() 
	{
		if (dataBooks != null)
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
	public void startEditing()
	{
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
	public ICellFormatter getCellFormatter()
	{
		return cellFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setCellFormatter(ICellFormatter pCellFormatter)
	{
		cellFormatter = pCellFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public INodeFormatter getNodeFormatter()
	{
		return nodeFormatter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNodeFormatter(INodeFormatter pCellFormatter)
	{
		nodeFormatter = pCellFormatter;
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
	public void run()
	{
		firstNotifyRepaintCall = true;
		
		try
		{
		    for (int i = 0; i < dataBooks.length; i++)
		    {
		    	//forces sync
		        dataBooks[i].getSelectedRow();
		    }	
		}
		catch (Exception e)
		{
			//ignore
		}
	}    

}	// WebTree
