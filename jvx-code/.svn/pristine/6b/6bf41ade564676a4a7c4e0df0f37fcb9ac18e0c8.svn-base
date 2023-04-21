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
 * 02.03.2009 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.control;

import java.beans.Beans;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.TreePath;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.ui.control.ITree;

import com.sibvisions.util.ArrayUtil;

/**
 * Platform and technology independent Tree.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UITree extends AbstractControllable<ITree> 
                 implements ITree
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** Internal data for design mode. */
    private static IDataBook internalDesignModeData = null;

    /** The set dataBook. */
    private transient IDataBook[] dataBooks = null;

    /** whether we're in design mode. */
    private transient boolean designMode = Beans.isDesignTime();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UITree</code>.
     *
     * @see ITree
     */
	public UITree()
	{
		this(UIFactoryManager.getFactory().createTree());
	}

    /**
     * Creates a new instance of <code>UITree</code> with the given
     * tree.
     *
     * @param pTree the tree
     * @see ITree
     */
	protected UITree(ITree pTree)
	{
		super(pTree);
		
        setMaximumSize(800, 600);
        
        installTree();
	}
	
	/**
	 * Creates a new instance of {@link UITree}.
	 *
	 * @param pDataBooks the {@link IDataBook data books}.
	 * @see #setDataBooks(IDataBook...)
	 */
	public UITree(IDataBook... pDataBooks)
	{
		this();
		
		setDataBooks(pDataBooks);
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
        if (pDataBooks == null || pDataBooks.length == 0)
        {
            dataBooks = null;
        }
        else
        {
            dataBooks = pDataBooks;
        }
        
        installTree();
    }

	/**
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook()
	{
		return uiResource.getActiveDataBook();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDetectEndNode()
	{
		return uiResource.isDetectEndNode();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDetectEndNode(boolean pDetectEndNode)
	{
		uiResource.setDetectEndNode(pDetectEndNode);
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
	public INodeFormatter getNodeFormatter()
	{
		return uiResource.getNodeFormatter();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNodeFormatter(INodeFormatter pNodeFormatter)
	{
		uiResource.setNodeFormatter(pNodeFormatter);
	}
	
    /**
     * Sets the node formatter.
     *
	 * @param pNodeFormatter the node formatter.
	 * @param pMethodName the method name.
     */
	public void setNodeFormatter(Object pNodeFormatter, String pMethodName)
	{
		uiResource.setNodeFormatter(createNodeFormatter(pNodeFormatter, pMethodName));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseEventOnSelectedCell()
	{
		return uiResource.isMouseEventOnSelectedCell();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * True, if new sub should be enabled.
     *  
     * @return True, if new sub should be enabled.
     */
    @Override
    protected boolean isInsertSubEnabledIntern()
    {
        IDataBook book = getActiveSubDataBook();
        try
        {
            return book != null && book.isInsertAllowed();
        }
        catch (ModelException pEx)
        {
            return false;
        }
    }

    /**
     * Performs an insert on the sub DataBook. (next level in hierarchy)
     *  
     * @throws ModelException if insert sub is not possible
     */
    @Override
    protected void doInsertSubIntern() throws ModelException
    {
        IDataBook book = getActiveSubDataBook();
        if (book != null)
        {
            if (book.isSelfJoined())
            {
                book.setTreePath(book.getTreePath().getChildPath(book.getSelectedRow()));
            }
            
            book.insert(true);
            
            requestFocus();
        }
    }

    /**
     * Starts editing.
     */
    @Override
    protected void doEditIntern()
    {
        startEditing();
    }
    
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
		if (getDataBooks() != null && getDataBooks().length > 0)
		{
			StringBuilder name = new StringBuilder();
			name.append(createComponentNamePrefix());
			
			// Append a "r" after the "T".
			name.append("r");
			
			for (IDataBook dataBook : getDataBooks())
			{
				name.append("_");
				name.append(dataBook.getName());
			}
			
			return incrementNameIfExists(name.toString(), getExistingNames(), false);
		}
		
		return super.createComponentName();
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * The sub data book of the current active data book, or null, if it does not exist.
     * Or the active data book, if it is the last and self joined.
     * @return the sub data book of the active data book.
     */
    public IDataBook getActiveSubDataBook()
    {
        IDataBook[] books = getDataBooks();
        
        if (books != null)
        {
            IDataBook book = getActiveDataBook();
            
            int index = ArrayUtil.indexOf(books, book);
            
            if (index < books.length - 1)
            {
                return books[index + 1];
            }
            else if (books[index].isSelfJoined())
            {
                return books[index];
            }
        }
        
        return null;
    }
    
    /**
     * Installs either a dummy binding for design mode or the real binding.
     */
    protected void installTree()
    {
        if (designMode && dataBooks == null)
        {
            uiResource.setDataBooks(getInternalDesignModeData());
        }
        else
        {
            uiResource.setDataBooks(dataBooks);
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
                internalDesignModeData.getRowDefinition().addColumnDefinition(new ColumnDefinition("NAME"));
                internalDesignModeData.getRowDefinition().addColumnDefinition(new ColumnDefinition("PARENT_NAME"));
                internalDesignModeData.setMasterReference(new ReferenceDefinition(new String[] {"PARENT_NAME"}, internalDesignModeData, new String[] {"NAME"}));
                internalDesignModeData.open();
                
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"A"});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"A - 1", "A"});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"A - 2", "A"});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"B"});
                internalDesignModeData.insert(false);
                internalDesignModeData.setValues(null, new Object[] {"C"});
                internalDesignModeData.saveSelectedRow();
                internalDesignModeData.setTreePath(new TreePath(0));
            }
            catch (Exception ex)
            {
                // ignore
            }
        }
        
        return internalDesignModeData;
    }
    
}	// UITree
