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
 * 03.03.2014 - [HM] - creation
 * 24.06.2014 - [JR] - #1078: search controller before super addNotify call                  
 */
package javax.rad.application.genui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rad.application.IApplication;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.IControllable;
import javax.rad.model.ui.IController;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.component.IActionComponent;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.util.SilentAbortException;

import com.sibvisions.util.KeyValueList;

/**
 * The <code>ControllerContent</code> is the default {@link IController} implementation and extends
 * an {@link Content}.
 *  
 * @author Martin Handsteiner
 */
public class ControllerContent extends Content 
					           implements IController, 
					                      IControllable, 
					                      IControl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The controller. */
	private transient IController controller = null;
	
	/** The last active data book. */
	private transient boolean askControllableController = true;

	/** The active controllable. */
	private transient IControllable activeControllable = null;
	
	/** The active controllable control. */
	private transient IControllable activeControl = null;
	
	/** The active data book. */
	private transient IDataBook activeDataBook = null;
	
	/** The internal frame between us and the application. */
	private transient IInternalFrame internalFrame = null;

    /** the controller properties. */
    private transient HashMap<String, Object> hmpProperties;

    /** repaint later indicator. */
    private transient KeyValueList<String, IActionComponent> commandButtons = new KeyValueList<String, IActionComponent>();

    /** repaint later indicator. */
	private transient boolean	bFirstNotifyRepaintCall = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ControllerContent</code>.
	 */
	public ControllerContent()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public IControllable getActiveControllable()
	{
		return activeControllable;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setActiveControllable(IControllable pActiveControllable)
	{
		boolean activeControllableChanged = pActiveControllable != activeControllable;
		
		activeControllable = pActiveControllable;
		
		IControllable newActiveControllableControl;
		IDataBook newActiveDataBook;
		if (activeControllable instanceof ControllerContent)
		{
			ControllerContent controllerContent = (ControllerContent)activeControllable;
			
			newActiveControllableControl = controllerContent.getActiveControl();
			newActiveDataBook = controllerContent.getActiveDataBook();
		}
		else
		{
			newActiveControllableControl = activeControllable;
			while (newActiveControllableControl instanceof IController 
					&& ((IController)newActiveControllableControl).getActiveControllable() != newActiveControllableControl 
					&& ((IController)newActiveControllableControl).getActiveControllable() != null)
			{
				newActiveControllableControl = ((IController)newActiveControllableControl).getActiveControllable();
			}
			
			if (newActiveControllableControl == null)
			{
				newActiveDataBook = null;
			}
			else
			{
				newActiveDataBook = newActiveControllableControl.getActiveDataBook();
			}
			
		}

		boolean activeControllableControlChanged = newActiveControllableControl != activeControl;

		IDataBook oldActiveDataBook = getActiveDataBook();
		
		activeControl = newActiveControllableControl;
		activeDataBook = newActiveDataBook;

		newActiveDataBook = getActiveDataBook();
		
		boolean activeDataBookChanged = newActiveDataBook != oldActiveDataBook;

		doNotifyController();

        if (activeDataBookChanged) // first register on databook, if setActiveControllable is called in 
        {
            if (oldActiveDataBook != null)
            {
                oldActiveDataBook.removeControl(this);
            }
            if (newActiveDataBook != null)
            {
                newActiveDataBook.addControl(this);
            }

            bFirstNotifyRepaintCall = true;
        }

        if (activeControllableChanged || activeControllableControlChanged || activeDataBookChanged)
        {
    		try
    		{
                doConfigureCommandButtons();
            }
            catch (Throwable ex)
            {
                error(ex);
            }
        }

        try
        {
            if (activeControllableChanged)
            {
				doActiveControllableChanged();
            }
            if (activeControllableControlChanged)
            {
                doActiveControlChanged();
            }
            if (activeDataBookChanged)
            {
                doActiveDataBookChanged();
            }
        }
        catch (SilentAbortException ex)
        {
            debug(ex);
        }
        catch (Throwable ex)
        {
            error(ex);
        }
	}

    /**
     * {@inheritDoc}
     */
    public void setControllerProperty(String pName, Object pValue)
    {
        if (pValue != null)
        {
            if (hmpProperties == null)
            {
                hmpProperties = new HashMap<String, Object>();
            }

            hmpProperties.put(pName, pValue);
        }
        else if (hmpProperties != null)
        {
            hmpProperties.remove(pName);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Object getControllerProperty(String pName)
    {
        if (hmpProperties != null && hmpProperties.containsKey(pName))
        {
            return hmpProperties.get(pName);
        }

        //property was not set in this object, ask the "parent" controller
        IController cont = getController();
        
        if (cont != null)
        {
            return cont.getControllerProperty(pName);
        }
        
        return null;
    }
	
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
	public IDataBook getActiveDataBook()
	{
		return activeDataBook;
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
		else if (pCommand == COMMAND_FIRST)
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
		else if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(pCommand);
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
		else if (pCommand == COMMAND_FIRST)
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
		else if (activeControllable != null)
		{
			activeControllable.doCommand(pCommand);
		}
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
	public void notifyRepaint()
	{
		if (bFirstNotifyRepaintCall)
		{
			bFirstNotifyRepaintCall = false;
			invokeLater(new Runnable()
			{
				public void run()
				{
					try
					{
						doConfigureCommandButtons();
					}
					catch (Throwable pEx)
					{
						error(pEx);
					}
					finally
					{
						bFirstNotifyRepaintCall = true;
					}
				}
			});
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
	protected boolean isRootContainer()
	{
	    return getClass() != ControllerContent.class;
	}

	
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

		try
		{
			doConfigureCommandButtons(); // correct initial state.
		}
		catch (Throwable e)
		{
			error(e);
		}
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
        
        if (internalFrame != null)
        {
            internalFrame.eventWindowActivated().addListener(this, "doNotifyController");
        }
        else if (parent instanceof IApplication) // Notify only applications that there is a new controllable added.   
        {
            doNotifyController();
        }
	}
	
	/**
	 * Registers a command button.
	 * The registered command buttons are default enabled or disabled in <code>doConfigureCommandButtons</code>.
	 * 
	 * @param pCommand the command
	 * @param pButton the button
	 */
	public void registerCommandButton(String pCommand, IActionComponent pButton)
	{
		commandButtons.put(pCommand, pButton);
	}
	
	/**
	 * Unregisters a command button.
	 * The registered command buttons are default enabled or disabled in <code>doConfigureCommandButtons</code>.
	 * 
	 * @param pCommand the command
	 * @param pButton the button
	 */
	public void unregisterCommandButton(String pCommand, IActionComponent pButton)
	{
		commandButtons.remove(pCommand, pButton);
	}
	
	/**
	 * Unregisters a command button.
	 * The registered command buttons are default enabled or disabled in <code>doConfigureCommandButtons</code>.
	 * 
	 * @param pCommand the command
	 */
	public void unregisterCommandButton(String pCommand)
	{
		commandButtons.remove(pCommand);
	}
	
	/**
	 * Unregisters all command buttons.
	 * The registered command buttons are default enabled or disabled in <code>doConfigureCommandButtons</code>.
	 */
	public void unregisterAllCommandButtons()
	{
		commandButtons.clear();
	}
	
	/**
	 * Gets all registered commands.
	 * The registered command buttons are default enabled or disabled in <code>doConfigureCommandButtons</code>.
	 * 
	 * @return all registered commands.
	 */
	public String[] getRegisteredCommands()
	{
		return commandButtons.keySet().toArray(new String[commandButtons.size()]);
	}
	
	/**
	 * Gets the registered button for the given command, or null otherwise.
	 * The registered command buttons are default enabled or disabled in <code>doConfigureCommandButtons</code>.
	 * 
	 * @param pCommand the command
	 * @return the button
	 */
	public List<IActionComponent> getCommandButtons(String pCommand)
	{
		return commandButtons.get(pCommand);
	}
	
	/**
	 * Gets the real control that caused the focus event.
	 * @return the real control that caused the focus event.
	 */
	public IControllable getActiveControl()
	{
		return activeControl;
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
		    if (getEventSource() instanceof IControllable)
		    {
		        controller.setActiveControllable((IControllable)getEventSource());
		    }
		    else
		    {
		        controller.setActiveControllable(this);
		    }
		}
	}

	/**
	 * This Method is invoked, if the active controllable has changed.
	 * 
	 * @throws Throwable if it fails
	 */
	public void doActiveControllableChanged() throws Throwable
	{
		// configuration for changed active controllable.
	}

	/**
	 * This Method is invoked, if the active controllable control has changed.
	 * 
	 * @throws Throwable if it fails
	 */
	public void doActiveControlChanged() throws Throwable
	{
		// configuration for changed active controllable control.
		//System.out.println(getClass().getSimpleName() + " doActiveControlChanged " + (getActiveControllable() == null ? null : getActiveControl().getClass().getSimpleName()));
	}

	/**
	 * This Method is invoked, if the active data book has changed.
	 * 
	 * @throws Throwable if it fails
	 */
	public void doActiveDataBookChanged() throws Throwable
	{
		// configuration for changed active data book.
		//System.out.println(getClass().getSimpleName() + " doActiveDataBookChanged " + (getActiveDataBook() == null ? null : getActiveDataBook().getName()));
	}

	/**
	 * This Method is invoked, when the data book has been changed, and the buttons have to be configured.
	 * 
	 * @throws Throwable if it fails
	 */
	public void doConfigureCommandButtons() throws Throwable
	{
		// configuration for changes of the current active data book.
		// System.out.println(getClass().getSimpleName() + " doConfigureButtons " + (getActiveDataBook() == null ? null : getActiveDataBook().getName()));

		for (Map.Entry<String, List<IActionComponent>> entry : commandButtons.entrySet())
		{
			for (IActionComponent actionComponent : entry.getValue())
			{
				actionComponent.setEnabled(isCommandEnabled(entry.getKey()));
			}
		}
	}
	
	/**
	 * True, if first should be enabled.
	 *  
	 * @return True, if first should be enabled.
	 */
	public boolean isFirstEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_FIRST);
		}
	}

	/**
	 * Selects the first row in the current DataBook. (same level in hierarchy)
	 *  
	 * @throws Throwable if the row cannot be selected
	 */
	public void doFirst() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_FIRST);
		}
	}

	/**
	 * True, if last should be enabled.
	 *  
	 * @return True, if last should be enabled.
	 */
	public boolean isLastEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_LAST);
		}
	}

	/**
	 * Selects the last row in the current DataBook. (same level in hierarchy)
	 *  
	 * @throws Throwable if the last row cannot be selected
	 */
	public void doLast() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_LAST);
		}
	}

	/**
	 * True, if previous should be enabled.
	 *  
	 * @return True, if first should be enabled.
	 */
	public boolean isPreviousEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_PREVIOUS);
		}
	}

	/**
	 * Selects the previous row in the current DataBook. (same level in hierarchy)
	 *  
	 * @throws Throwable if the previous row cannot be selected
	 */
	public void doPrevious() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_PREVIOUS);
		}
	}

	/**
	 * True, if next should be enabled.
	 *  
	 * @return True, if next should be enabled.
	 */
	public boolean isNextEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_NEXT);
		}
	}

	/**
	 * Selects the next row in the current DataBook. (same level in hierarchy)
	 *  
	 * @throws Throwable if the next row cannot be selected
	 */
	public void doNext() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_NEXT);
		}
	}

	/**
	 * True, if new should be enabled.
	 *  
	 * @return True, if new should be enabled.
	 */
	public boolean isInsertEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_INSERT);
		}
	}

	/**
	 * Performs an insert on the current DataBook. (same level in hierarchy)
	 *  
	 * @throws Throwable if insert is not possible
	 */
	public void doInsert() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_INSERT);
		}
	}

	/**
	 * True, if new sub should be enabled.
	 *  
	 * @return True, if new sub should be enabled.
	 */
	public boolean isInsertSubEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_INSERT_SUB);
		}
	}

	/**
	 * Performs an insert on the sub DataBook. (next level in hierarchy)
	 *  
	 * @throws Throwable if insert in the sub is not possible
	 */
	public void doInsertSub() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_INSERT_SUB);
		}
	}

	/**
	 * True, if delete should be enabled.
	 *  
	 * @return True, if delete should be enabled.
	 */
	public boolean isDeleteEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_DELETE);
		}
	}

	/**
	 * Performs a delete on the current DataBook.
	 *  
	 * @throws Throwable if delete is not possible
	 */
	public void doDelete() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_DELETE);
		}
	}
	
	/**
	 * True, if restore should be enabled.
	 *  
	 * @return True, if restore should be enabled.
	 */
	public boolean isRestoreEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_RESTORE);
		}
	}

	/**
	 * Performs a restore on the current DataBook.
	 *  
	 * @throws Throwable if restore is not possible
	 */
	public void doRestore() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_RESTORE);
		}
	}
	
	/**
	 * Gets the duplicate button visibility.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isDuplicateEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_DUPLICATE);
		}
	}
	
	/**
	 * Sends the duplicate action to all listeners.
	 *  
	 * @throws Throwable if an export error occurs
	 */
	public void doDuplicate() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_DUPLICATE);
		}
	}

	/**
	 * Gets the export button visibility.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isExportEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_EXPORT);
		}
	}
	
	/**
	 * Performs the CSV Export of the current DataBook.
	 *  
	 * @throws Throwable if an export error occurs
	 */
	public void doExport() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_EXPORT);
		}
	}
	
	/**
	 * Gets the edit button visibility.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isEditEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_EDIT);
		}
	}
	
	/**
	 * Starts editing.
	 *  
	 * @throws Throwable if an export error occurs
	 */
	public void doEdit() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_EDIT);
		}
	}
	
	/**
	 * Gets the search button visibility.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isSearchEnabled()
	{
		if (activeControllable == null)
		{
			return false;
		}
		else
		{
			return activeControllable.isCommandEnabled(COMMAND_SEARCH);
		}
	}
	
	/**
	 * Shows the search options.
	 *  
	 * @throws Throwable if an export error occurs
	 */
	public void doSearch() throws Throwable
	{
		if (activeControllable != null)
		{
			activeControllable.doCommand(COMMAND_SEARCH);
		}
	}

}	// ControllerContent
