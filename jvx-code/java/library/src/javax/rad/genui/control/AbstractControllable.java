/*
 * Copyright 2014 SIB Visions GmbH
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
 * 28.02.2014 - [HM] - creation
 * 24.06.2014 - [JR] - #1078: search controller before super addNotify call
 */
package javax.rad.genui.control;

import javax.rad.genui.UIComponent;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.ui.IControllable;
import javax.rad.model.ui.IController;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.util.EventHandler;
import javax.rad.util.ExceptionHandler;

/**
 * Base class for all genui <code>IControl</code> implementations.
 * It handles the notification of the controler on focus gained.
 * Therefore a focus listener is added to the technology dependent control.
 * All commands are deligated to seperate is[Command]Enabled and do[Command]
 * functions, to allow easy overwriting. 
 * 
 * @author Martin Handsteiner
 * 
 * @param <C> instance of IComponent
 */
public abstract class AbstractControllable<C extends IComponent> extends UIComponent<C> 
                                                                 implements IControllable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /** The cell formatter provider. */
    private static EventHandler<ICellFormatter> cellFormatterProvider = new EventHandler<ICellFormatter>(ICellFormatter.class);

    /** The node formatter provider. */
    private static EventHandler<INodeFormatter> nodeFormatterProvider = new EventHandler<INodeFormatter>(INodeFormatter.class);
    
    
	/** The controller. */
	private transient IController controller = null;
	
	/** The last active data book. */
	private transient boolean askControllableController = true;
	
	/** The internal frame between us and the application. */
	private transient IInternalFrame internalFrame = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>AbstractControllable</code>.
     *
     * @param pComponent the Component.
     * @see IComponent
     */
	protected AbstractControllable(C pComponent)
	{
		super(pComponent);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** 
	 * {@inheritDoc}
	 */
	public IController getController()
	{
		return controller;
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setController(IController pController)
	{
		controller = pController;
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean isCommandEnabled(String pCommand)
	{
		// Ask for overwritten functionality on higher level.
		if (askControllableController && controller instanceof IControllable && controller.getActiveControllable() == this)
		{
			try
			{
				askControllableController = false;
				
				return ((IControllable)controller).isCommandEnabled(pCommand);
			}
			finally
			{
				askControllableController = true;
			}
		}
		else
		{
            boolean originalAskControllableController = askControllableController;
		    try
            {
                askControllableController = false;
                
    		    if (pCommand == COMMAND_FIRST)
        		{
        			return isFirstEnabled();
        		}
        		else if (pCommand == COMMAND_LAST)
        		{
        			return isLastEnabled();
        		}
        		else if (pCommand == COMMAND_NEXT)
        		{
        			return isNextEnabled();
        		}
        		else if (pCommand == COMMAND_PREVIOUS)
        		{
        			return isPreviousEnabled();
        		}
        		else if (pCommand == COMMAND_EDIT)
        		{
        			return isEditEnabled();
        		}
        		else if (pCommand == COMMAND_INSERT)
        		{
        			return isInsertEnabled();
        		}
        		else if (pCommand == COMMAND_INSERT_SUB)
        		{
        			return isInsertSubEnabled();
        		}
        		else if (pCommand == COMMAND_RESTORE)
        		{
        			return isRestoreEnabled();
        		}
        		else if (pCommand == COMMAND_DELETE)
        		{
        			return isDeleteEnabled();
        		}
        		else if (pCommand == COMMAND_DUPLICATE)
        		{
        			return isDuplicateEnabled();
        		}
        		else if (pCommand == COMMAND_EXPORT)
        		{
        			return isExportEnabled();
        		}
        		else if (pCommand == COMMAND_SEARCH)
        		{
        			return isSearchEnabled();
        		}
        		else
        		{
        			return false;
        		}
            }
            finally
            {
                askControllableController = originalAskControllableController;
            }
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	public void doCommand(String pCommand) throws Throwable
	{
		// Ask for overwritten functionality on higher level.
		if (askControllableController && controller instanceof IControllable)
		{
			try
			{
				askControllableController = false;
				
                doNotifyController();
                
                if (controller.getActiveControllable() == this) // only if we are the active controllable, the call may be delegated to controller
                {
                    ((IControllable)controller).doCommand(pCommand);
                }
                else
                {
                    doCommand(pCommand);
                }
			}
			finally
			{
				askControllableController = true;
			}
		}
		else 
        {
		    boolean originalAskControllableController = askControllableController;
            try
            {
                askControllableController = false;
                
                if (pCommand == COMMAND_FIRST)
        		{
        			doFirst();
        		}
        		else if (pCommand == COMMAND_LAST)
        		{
        			doLast();
        		}
        		else if (pCommand == COMMAND_NEXT)
        		{
        			doNext();
        		}
        		else if (pCommand == COMMAND_PREVIOUS)
        		{
        			doPrevious();
        		}
        		else if (pCommand == COMMAND_EDIT)
        		{
        			doEdit();
        		}
        		else if (pCommand == COMMAND_INSERT)
        		{
        			doInsert();
        		}
        		else if (pCommand == COMMAND_INSERT_SUB)
        		{
        			doInsertSub();
        		}
        		else if (pCommand == COMMAND_RESTORE)
        		{
        			doRestore();
        		}
        		else if (pCommand == COMMAND_DELETE)
        		{
        			doDelete();
        		}
        		else if (pCommand == COMMAND_DUPLICATE)
        		{
        			doDuplicate();
        		}
        		else if (pCommand == COMMAND_EXPORT)
        		{
        			doExport();
        		}
        		else if (pCommand == COMMAND_SEARCH)
        		{
        			doSearch();
        		}
            }
            finally
            {
                askControllableController = originalAskControllableController;
            }
        }
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeAddNotify(IComponent pParent)
	{
	    //before, because the controller could be needed in sub components
        searchAndSetController();

        super.beforeAddNotify(pParent);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify()
    {
        if (!isBeforeNotified())
        {
            //before, because the controller could be needed in sub components
            searchAndSetController();
        }
        
        super.addNotify();
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		super.removeNotify();

		if (internalFrame != null)
		{
			internalFrame.eventWindowActivated().removeListener(this);
			
			internalFrame = null;
		}
		
		uiResource.eventFocusGained().removeListener(this, "doNotifyController");

		if (getController() != null && getController().getActiveControllable() == this)
		{
			getController().setActiveControllable(null);
		}
		setController(null);
		
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Userdefined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * True, if we should ask controller first, or really perform the command otherwise. 
	 * @return True, if we should ask controller first, or really perform the command otherwise.
	 */
	protected boolean isAskControllableController()
	{
	    return askControllableController;
	}
	
	/**
	 * Searches and sets the controller.
	 */
    private void searchAndSetController()
    {
        IContainer parent = getParent();
        
        while (parent != null && !(parent instanceof IController))
        {
            if (parent instanceof IInternalFrame)
            {
                internalFrame = (IInternalFrame)parent;
            }
                
            parent = parent.getParent();
        }

        setController((IController)parent);
        
        uiResource.eventFocusGained().addListener(this, "doNotifyController");

        if (internalFrame != null)
        {
            internalFrame.eventWindowActivated().addListener(this, "doNotifyController");
        }
    }
	
	/**
	 * Notify the controller, that focus has changed.
	 */
	public void doNotifyController()
	{
		if (controller != null 
				&& (internalFrame == null 
					|| (internalFrame instanceof UIInternalFrame && ((UIInternalFrame)internalFrame).isGlobalActive())
					|| (!(internalFrame instanceof UIInternalFrame) && internalFrame.isActive())))
		{
			controller.setActiveControllable(this);
		}
	}

	/**
	 * Same as doCommand, but Throwable is handled.
	 * 
	 * @param pCommand the command
	 * @throws ModelException if it fails
	 */
    protected void doCommandIntern(String pCommand) throws ModelException
    {
        try
        {
            doCommand(pCommand);
        }
        catch (ModelException ex)
        {
            throw ex;
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (Throwable ex)
        {
            error(ex);
        }
    }

	/**
	 * True, if first should be enabled.
	 *  
	 * @return True, if first should be enabled.
	 */
	public boolean isFirstEnabled()
	{
	    if (isAskControllableController())
	    {
	        return isCommandEnabled(COMMAND_FIRST);
	    }
	    else
	    {
    		return isFirstEnabledIntern();
	    }
	}

    /**
     * True, if first should be enabled.
     *  
     * @return True, if first should be enabled.
     */
    protected boolean isFirstEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
        	return book != null && book.getDataRow(0) != null && book.getSelectedRow() > 0;
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);

        	return false;
        }
    }
	
	/**
	 * Selects the first row in the current DataBook. (same level in hierarchy)
	 *  
	 * @throws ModelException if selecting the first row is not possible
	 */
	public void doFirst() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_FIRST);
        }
        else
        {
    		doFirstIntern();
        }
	}

    /**
     * Selects the first row in the current DataBook. (same level in hierarchy)
     *  
     * @throws ModelException if selecting the first row is not possible
     */
	protected void doFirstIntern() throws ModelException
    {
        IDataBook book = getActiveDataBook();
        
        if (book != null)
        {
        	book.setSelectedRow(0);
   
        	requestFocus();
        }
    }

	/**
	 * True, if last should be enabled.
	 *  
	 * @return True, if last should be enabled.
	 */
	public boolean isLastEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_LAST);
        }
        else
        {
    		return isLastEnabledIntern();
        }
	}

    /**
     * True, if last should be enabled.
     *  
     * @return True, if last should be enabled.
     */
	protected boolean isLastEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
        	return book != null && book.getDataRow(0) != null && (!book.isAllFetched() || book.getSelectedRow() < book.getRowCount() - 1);
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);

            return false;
        }
    }

	/**
	 * Selects the last row in the current DataBook. (same level in hierarchy)
	 *  
	 * @throws ModelException if selecting the last row is not possible
	 */
	public void doLast() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_LAST);
        }
        else
        {
    		doLastIntern();
        }
	}

    /**
     * Selects the last row in the current DataBook. (same level in hierarchy)
     *  
     * @throws ModelException if selecting the last row is not possible
     */
	protected void doLastIntern() throws ModelException
    {
        IDataBook book = getActiveDataBook();
        
        if (book != null)
        {
        	book.fetchAll();
        	book.setSelectedRow(book.getRowCount() - 1);
   
        	requestFocus();
        }
    }

	/**
	 * True, if previous should be enabled.
	 *  
	 * @return True, if previous should be enabled.
	 */
	public boolean isPreviousEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_PREVIOUS);
        }
        else
        {
    		return isPreviousEnabledIntern();
        }
	}

    /**
     * True, if previous should be enabled.
     *  
     * @return True, if previous should be enabled.
     */
    protected boolean isPreviousEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
            return book != null && book.getDataRow(0) != null && book.getSelectedRow() > 0;
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);

            return false;
        }
    }
    
	/**
	 * Selects the previous row in the current DataBook. (same level in hierarchy)
	 *  
	 * @throws ModelException if selecting the previous row is not possible
	 */
	public void doPrevious() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_PREVIOUS);
        }
        else
        {
    		doPreviousIntern();
        }
	}

    /**
     * Selects the previous row in the current DataBook. (same level in hierarchy)
     *  
     * @throws ModelException if selecting the previous row is not possible
     */
	protected void doPreviousIntern() throws ModelException
    {
        IDataBook book = getActiveDataBook();
        
        if (book != null)
        {
        	book.setSelectedRow(book.getSelectedRow() - 1);
   
        	requestFocus();
        }
    }

	/**
	 * True, if next should be enabled.
	 *  
	 * @return True, if next should be enabled.
	 */
	public boolean isNextEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_NEXT);
        }
        else
        {
    		return isNextEnabledIntern();
        }
	}

    /**
     * True, if next should be enabled.
     *  
     * @return True, if next should be enabled.
     */
	protected boolean isNextEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
        	return book != null && book.getDataRow(book.getSelectedRow() + 1) != null;
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);

            return false;
        }
    }

	/**
	 * Selects the next row in the current DataBook. (same level in hierarchy)
	 *  
	 * @throws ModelException if selecting the next is not possible
	 */
	public void doNext() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_NEXT);
        }
        else
        {
    		doNextIntern();
        }
	}

    /**
     * Selects the next row in the current DataBook. (same level in hierarchy)
     *  
     * @throws ModelException if selecting the next is not possible
     */
	protected void doNextIntern() throws ModelException
    {
        IDataBook book = getActiveDataBook();
        
        if (book != null)
        {
        	book.setSelectedRow(book.getSelectedRow() + 1);
   
        	requestFocus();
        }
    }

	/**
	 * True, if new should be enabled.
	 *  
	 * @return True, if new should be enabled.
	 */
	public boolean isInsertEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_INSERT);
        }
        else
        {
    		return isInsertEnabledIntern();
        }
	}

    /**
     * True, if new should be enabled.
     *  
     * @return True, if new should be enabled.
     */
	protected boolean isInsertEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
        	return book != null && book.isInsertAllowed();
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);

            return false;
        }
    }

	/**
	 * Performs an insert on the current DataBook. (same level in hierarchy)
	 *  
	 * @throws ModelException if insert is not possible
	 */
	public void doInsert() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_INSERT);
        }
        else
        {
    		doInsertIntern();
        }
	}

    /**
     * Performs an insert on the current DataBook. (same level in hierarchy)
     *  
     * @throws ModelException if insert is not possible
     */
	protected void doInsertIntern() throws ModelException
    {
        IDataBook book = getActiveDataBook();
        
        if (book != null)
        {
        	book.insert(false);
        	
        	requestFocus();
        }
    }

	/**
	 * True, if new sub should be enabled.
	 *  
	 * @return True, if new sub should be enabled.
	 */
	public boolean isInsertSubEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_INSERT_SUB);
        }
        else
        {
            return isInsertSubEnabledIntern();
        }
	}

    /**
     * True, if new sub should be enabled.
     *  
     * @return True, if new sub should be enabled.
     */
	protected boolean isInsertSubEnabledIntern()
    {
        return false;
    }

	/**
	 * Performs an insert on the sub DataBook. (next level in hierarchy)
	 *  
	 * @throws ModelException if insert sub is not possible
	 */
	public void doInsertSub() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_INSERT_SUB);
        }
        else
        {
            doInsertSubIntern();
        }
	}

    /**
     * Performs an insert on the sub DataBook. (next level in hierarchy)
     *  
     * @throws ModelException if insert sub is not possible
     */
	protected void doInsertSubIntern() throws ModelException
    {
    }

    /**
	 * True, if delete should be enabled.
	 *  
	 * @return True, if delete should be enabled.
	 */
	public boolean isDeleteEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_DELETE);
        }
        else
        {
    		return isDeleteEnabledIntern();
        }
	}

    /**
     * True, if delete should be enabled.
     *  
     * @return True, if delete should be enabled.
     */
	protected boolean isDeleteEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
        	return book != null && book.isDeleteAllowed() && !book.isDeleting();
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);
        	
            return false;
        }
    }

	/**
	 * Performs a delete on the current DataBook.
	 *  
	 * @throws ModelException if delete is not possible
	 */
	public void doDelete() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_DELETE);
        }
        else
        {
    		doDeleteIntern();
        }
	}

    /**
     * Performs a delete on the current DataBook.
     *  
     * @throws ModelException if delete is not possible
     */
    protected void doDeleteIntern() throws ModelException
    {
        IDataBook book = getActiveDataBook();
        
        if (book != null)
        {
        	book.delete();
        	
        	requestFocus();
        }
    }
	
	/**
	 * True, if restore should be enabled.
	 *  
	 * @return True, if restore should be enabled.
	 */
	public boolean isRestoreEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_RESTORE);
        }
        else
        {
    		return isRestoreEnabledIntern();
        }
	}

    /**
     * True, if restore should be enabled.
     *  
     * @return True, if restore should be enabled.
     */
    protected boolean isRestoreEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
        	return book != null && book.isChanged();
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);

            return false;
        }
    }

	/**
	 * Performs a restore on the current DataBook.
	 *  
	 * @throws ModelException if restore is not possible
	 */
	public void doRestore() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_RESTORE);
        }
        else
        {
    		doRestoreIntern();
        }
	}

    /**
     * Performs a restore on the current DataBook.
     *  
     * @throws ModelException if restore is not possible
     */
    protected void doRestoreIntern() throws ModelException
    {
        IDataBook book = getActiveDataBook();
        
        if (book != null)
        {
        	book.restoreSelectedRow();
        	
        	requestFocus();
        }
    }
	
	/**
	 * Gets the duplicate button visibility.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isDuplicateEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_DUPLICATE);
        }
        else
        {
    		return isDuplicateEnabledIntern();
        }
	}

    /**
     * Gets the duplicate button visibility.
     * 
     * @return <code>true</code> if visible, <code>false</code> otherwise
     */
    protected boolean isDuplicateEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
        	return book != null && book.isInsertAllowed() && book.getSelectedRow() >= 0;
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);

            return false;
        }
    }
	
	/**
	 * Sends the duplicate action to all listeners.
	 *  
	 * @throws ModelException if an duplicate error occurs
	 */
	public void doDuplicate() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_DUPLICATE);
        }
        else
        {
            doDuplicateIntern();
        }
	}

    /**
     * Sends the duplicate action to all listeners.
     *  
     * @throws ModelException if an duplicate error occurs
     */
    protected void doDuplicateIntern() throws ModelException
    {
    }
	
	/**
	 * Gets the export button visibility.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isExportEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_EXPORT);
        }
        else
        {
    		return isExportEnabledIntern();
        }
	}

    /**
     * Gets the export button visibility.
     * 
     * @return <code>true</code> if visible, <code>false</code> otherwise
     */
    protected boolean isExportEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
   
        return book != null;
    }
	
	/**
	 * Performs the CSV Export of the current DataBook.
	 *  
	 * @throws Throwable if an export error occurs
	 */
	public void doExport() throws Throwable
	{
        if (isAskControllableController())
        {
            doCommand(COMMAND_EXPORT);
        }
        else
        {
            doExportIntern();
        }
	}

	/**
     * Performs the CSV Export of the current DataBook.
     *  
     * @throws Throwable if an export error occurs
     */
	protected void doExportIntern() throws Throwable
    {
    }

	/**
	 * Gets the edit button visibility.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isEditEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_EDIT);
        }
        else
        {
    		return isEditEnabledIntern();
        }
	}

    /**
     * Gets the edit button visibility.
     * 
     * @return <code>true</code> if visible, <code>false</code> otherwise
     */
    protected boolean isEditEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
        try
        {
        	return book != null && book.getSelectedRow() >= 0 && !book.isReadOnly();
        }
        catch (ModelException pEx)
        {
            ExceptionHandler.show(pEx);

            return false;
        }
    }
	
	/**
	 * Starts editing.
	 *  
	 * @throws ModelException if an edit error occurs
	 */
	public void doEdit() throws ModelException
	{
	    if (isAskControllableController())
        {
	        doCommandIntern(COMMAND_EDIT);
        }
	    else
	    {
	        doEditIntern();
	    }
	}

	/**
     * Starts editing.
     *  
     * @throws ModelException if an edit error occurs
     */
    protected void doEditIntern() throws ModelException
    {
    }

	/**
	 * Gets the search button visibility.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isSearchEnabled()
	{
        if (isAskControllableController())
        {
            return isCommandEnabled(COMMAND_SEARCH);
        }
        else
        {
    		return isSearchEnabledIntern();
        }
	}
	
    /**
     * Gets the export button visibility.
     * 
     * @return <code>true</code> if visible, <code>false</code> otherwise
     */
    protected boolean isSearchEnabledIntern()
    {
        IDataBook book = getActiveDataBook();
   
        return book != null;
    }
    
	/**
	 * Shows the search options.
	 *  
	 * @throws ModelException if an export error occurs
	 */
	public void doSearch() throws ModelException
	{
        if (isAskControllableController())
        {
            doCommandIntern(COMMAND_SEARCH);
        }
        else
        {
            doSearchIntern();
        }
	}

	/**
     * Shows the search options.
     *  
     * @throws ModelException if an export error occurs
     */
    protected void doSearchIntern() throws ModelException
    {
    }

    /**
     * Creates a cell formatter instance with the given object and method name.
     * @param pCellFormatter the object.
     * @param pMethodName the method name.
     * @return the cell formatter.
     */
    public static ICellFormatter createCellFormatter(Object pCellFormatter, String pMethodName)
    {
        return cellFormatterProvider.createListener(pCellFormatter, pMethodName);
    }
    
    /**
     * Creates a node formatter instance with the given object and method name.
     * @param pNodeFormatter the object.
     * @param pMethodName the method name.
     * @return the cell formatter.
     */
    public static INodeFormatter createNodeFormatter(Object pNodeFormatter, String pMethodName)
    {
        return nodeFormatterProvider.createListener(pNodeFormatter, pMethodName);
    }	
	
}	// AbstractControllable
