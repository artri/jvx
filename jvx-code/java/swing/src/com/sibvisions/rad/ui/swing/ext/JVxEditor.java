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
 * 30.10.2008 - [HM] - creation
 * 24.03.2011 - [JR] - #317: setEnabled performs cancelEditing
 * 08.06.2011 - [JR] - #385: remove dummy editor
 * 10.06.2011 - [JR] - set transparent background
 * 21.02.2013 - [HM] - avoid unnessary cancelEditing.
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.component.IPlaceholder;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;

import com.sibvisions.rad.ui.swing.ext.format.ICellFormatter;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>JVxEditor</code> implements the {@link IEditorControl} interface.
 *  
 * @author Martin Handsteiner
 */
public class JVxEditor extends JPanel 
                       implements IEditorControl,
                       			  IPlaceholder,
                                  ICellFormatterEditorListener,
                                  FocusListener,
                                  IAlignmentConstants,
                                  Runnable 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Layout focus traversal policy. */
    private static final LayoutFocusTraversalPolicy FOCUS_TRAVERSAL_POLICY = new LayoutFocusTraversalPolicy();
	
	/** the component logger. */
	private static ILogger logger = null;

	/** The DataRow to be edited. */
	private IDataRow dataRow = null;
	
	/** The column to be edited. */
	private String columnName = null;
	
	/** The CellEditor. */
	private ICellEditor cellEditor = null;
	
	/** The cellFormatListener. */
	private ICellFormatter cellFormatter = null;
	
	/** The used CellEditorHandler. */
	private ICellEditorHandler<JComponent> cellEditorHandler = null;
	
	/** The focusable component. */
	private Component focusableComponent = null;
	
	/** The translation mapping. */
	private TranslationMap translation = null;

	/** Cell Editor started editing. */
	private JTextField dummyEditor = new JTextField(10);
	
	/** the placeholder. */
	private String placeholder = null;
	
	/** dummy editor background. */
	private Color colDummyBack = null;

	/** X Alignment. */
	private int	horizontalAlignment = IAlignmentConstants.ALIGN_DEFAULT;
	
	/** Y Alignment. */
	private int	verticalAlignment = IAlignmentConstants.ALIGN_DEFAULT;

	/** Tells, if the CellEditor should save immediate. */
	private boolean savingImmediate = false;
	
	/** Tells, if notifyRepaint is called the first time. */
	private boolean firstNotifyRepaintCall = true;
	
	/** Ignore Cancel call. */
	private boolean isCancelling = false;

	/** Ignore Cancel call. */
    private boolean isReinstallOnException = false;

	/** the borders visibility. */
	private boolean borderVisible = true;
	
	/** is notified. */
	private boolean isNotified = false;
	
	/** Cell Editor started editing. */
	private boolean editingStarted = false;
	
	/** Trick out focus manager with temporary wrong showing state. */
	private boolean temporaryIsShowingState = false;

    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Constructs an editor.
	 */
	public JVxEditor()
	{
		super(new JVxBorderLayout());
		
		dummyEditor.setEditable(false);
		dummyEditor.setEnabled(false);
		
		colDummyBack = dummyEditor.getBackground();
		
		if (SwingFactory.isMacLaF())
		{
			dummyEditor.setBorder(new WrappedInsetsBorder(dummyEditor.getBorder()));
		}
		
		add(dummyEditor, JVxBorderLayout.CENTER);
		
		super.setBackground(null);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean pEnabled)
	{
		super.setEnabled(pEnabled);
		
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(Color pColor)
	{
		super.setBackground(pColor);
		
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForeground(Color pEnabled)
	{
		super.setForeground(pEnabled);
		
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFont(Font pEnabled)
	{
		super.setFont(pEnabled);
		
		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isShowing()
	{
		return temporaryIsShowingState || super.isShowing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();
		
		isNotified = true;

		if (cellEditorHandler != null)
		{
			focusableComponent = getFocusableComponent(cellEditorHandler.getCellEditorComponent());
			
			focusableComponent.addFocusListener(this);

			for (MouseListener mouseListener : getMouseListeners())
			{
				focusableComponent.addMouseListener(mouseListener);
			}
			for (KeyListener keyListener : getKeyListeners())
			{
				focusableComponent.addKeyListener(keyListener);
			}
			if (focusableComponent instanceof JComponent)
			{
				((JComponent)focusableComponent).setToolTipText(getToolTipText());
			}
		}

		if (EventQueue.isDispatchThread())
		{
			run();
		}
		else
		{
			notifyRepaint();
		}
		
//		run(); // run() instead of notifyRepaint() to ensure immediate paint at first time, necessary for designmode.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		if (focusableComponent != null)
		{
			focusableComponent.removeFocusListener(this);
	
			for (MouseListener mouseListener : getMouseListeners())
			{
				focusableComponent.removeMouseListener(mouseListener);
			}
			for (KeyListener keyListener : getKeyListeners())
			{
				focusableComponent.removeKeyListener(keyListener);
			}
			if (focusableComponent instanceof JComponent)
			{
				((JComponent)focusableComponent).setToolTipText(null);
			}
		}

		isNotified = false;

		super.removeNotify();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestFocus()
	{
		if (focusableComponent == null)
		{
			super.requestFocus();
		}
		else
		{
			focusableComponent.requestFocus();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMouseListener(MouseListener pMouseListener)
	{
		super.addMouseListener(pMouseListener);
		if (focusableComponent != null && focusableComponent != this)
		{
			focusableComponent.addMouseListener(pMouseListener);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMouseListener(MouseListener pMouseListener)
	{
		super.removeMouseListener(pMouseListener);
		if (focusableComponent != null && focusableComponent != this)
		{
			focusableComponent.removeMouseListener(pMouseListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addKeyListener(KeyListener pKeyListener)
	{
		super.addKeyListener(pKeyListener);
		if (focusableComponent != null && focusableComponent != this)
		{
			focusableComponent.addKeyListener(pKeyListener);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeKeyListener(KeyListener pKeyListener)
	{
		super.removeKeyListener(pKeyListener);
		if (focusableComponent != null && focusableComponent != this)
		{
			focusableComponent.removeKeyListener(pKeyListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(String pToolTipText)
	{
		super.setToolTipText(pToolTipText);
		if (focusableComponent instanceof JComponent && focusableComponent != this)
		{
			((JComponent)focusableComponent).setToolTipText(pToolTipText);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		horizontalAlignment = pHorizontalAlignment;
		notifyRepaint();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		verticalAlignment = pVerticalAlignment;
		notifyRepaint();
	}

    //RUNNABLE
    
	/**
	 * The run method is invoked from AWT EventQueue. 
	 * It enables events from the model again. 
	 * Due to performance reasons the events are disabled from the first call of
	 * notifyRepaint until the EventQueue calls the run method. 
	 * This minimizes the repaints of the control. 
	 */
	public void run()
	{
		try
		{
			if (!isCancelling)
			{
				isCancelling = true;
				if (isNotified)
				{
					cancelEditing();
				}
			}
		}
		finally
		{
			isCancelling = false;
			firstNotifyRepaintCall = true;
		}
	}
	
	//ICONTROL
	
	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		if (firstNotifyRepaintCall && !editingStarted)
		{
			firstNotifyRepaintCall = false;
			
			JVxUtil.invokeLater(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing()
	{
		if (editingStarted || isCancelling) // only cancel, if editing is started (in case databook calls directly cancel, or notifyRepaint (isCancelling) is called
		{
			editingStarted = false;
	
			if (cellEditorHandler != null)
			{
				try
				{
					cellEditorHandler.cancelEditing();
				}
				catch (ModelException pException)
				{
					if (logger == null)
					{
						logger = LoggerFactory.getInstance(getClass());
					}
	
					logger.debug(pException);
	
					if (isReinstallOnException)
					{
					    throw new IllegalStateException("Cancel failed after reinstall!");
					}
					else
					{
					    isReinstallOnException = true;
					    try
					    {
					        uninstallEditor();
    					
					        installEditor();
					    }
					    finally
					    {
					        isReinstallOnException = false;
					    }
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException
	{
		if (editingStarted)
		{
			// Set immediate to false to avoid recursion, if DataRowListener stores again.
			editingStarted = false;

			try
			{
				cellEditorHandler.saveEditing();
			}
			catch (Throwable e)
			{
				cellEditorHandler.cancelEditing();
				
				ExceptionHandler.raise(e);
			}

			// This should not be needed, as a saveEditing causes an notifyRepaint event.
//			cancelEditing();
			// In case of saving immediate, it not necessarily causes an event, to avoid a values changed on last key pressed.
			// so call notifyRepaint, it will not cause an additional cancelEditing, if saveEditing already caused one.
			// The event is needed, so that the cellEditorHandler can reset firstEditing flag in cancelEditing!
			notifyRepaint();
		}
	}

    /**
     * {@inheritDoc}
     */
    public ICellFormatter getCellFormatter()
    {
    	return cellFormatter;
    }

    // ITranslatable
    
    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
    	if (translation != pTranslation)
    	{
        	translation = pTranslation;
        	
        	try 
        	{
    			saveEditing();
    		} 
        	catch (Throwable e) 
        	{
    			// saveEditing cancels, if it fails
    		}

        	notifyRepaint();
    	}
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

	//ICellEditorListener
	
	/**
	 * {@inheritDoc}
	 */
	public void editingStarted() 
	{
		try
		{
			editingStarted = true; // first set editingStarted true, to prevent events on update.

			if (dataRow instanceof IDataBook)
			{
				IDataRow oldDataRow = dataRow.createDataRow(null);
				
				((IDataBook)dataRow).update();
				
				if (!oldDataRow.equals(dataRow, new String[] {columnName})) // Only if value is changed, cancel editing.
				{
					editingStarted = false;
					notifyRepaint();
				}
			}
		}
		catch (Throwable pThrowable)
		{
			editingStarted = false;
			notifyRepaint();
			
			ExceptionHandler.raise(pThrowable);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void editingComplete(String pCompleteType) 
	{
		if (pCompleteType == ICellEditorListener.ESCAPE_KEY)
		{
			cancelEditing();
		}
		else
		{
			try
			{
				saveEditing();
			}
			catch (Throwable ex)
			{
				// TODO maybe a request focus should occur, but we have to prevent tab change, ...
				try
				{
					cellEditorHandler.cancelEditing();
				}
				catch (Throwable e)
				{
					ExceptionHandler.raise(e);
				}
				
				ExceptionHandler.raise(ex);
			}
			if (pCompleteType == ICellEditorListener.ENTER_KEY)
			{
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
			else if (pCompleteType == ICellEditorListener.SHIFT_ENTER_KEY)
			{
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
			}
			else if (pCompleteType == ICellEditorListener.TAB_KEY)
			{
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
			else if (pCompleteType == ICellEditorListener.SHIFT_TAB_KEY)
			{
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSavingImmediate() 
	{
		return savingImmediate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IControl getControl()
	{
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void focusGained(FocusEvent pEvent)
	{
		processFocusEvent(pEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void focusLost(FocusEvent pEvent)
	{
		processFocusEvent(pEvent);
	}
	
	/**
	 * {@inheritDoc}
	 */
    public String getPlaceholder()
    {
    	return placeholder;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setPlaceholder(String pPlaceholder)
    {
    	placeholder = pPlaceholder;
    	
    	notifyRepaint();
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Gets the DataRow edited by this control.
     *
     * @return the DataRow.
     * @see #setDataRow
     */
	public IDataRow getDataRow()
	{
		return dataRow;
	}
	
	/**
     * Sets the DataRow edited by this control.
     *
     * @param pDataRow the DataRow.
     * @throws ModelException if the column name for the row is invalid
     * @see #getDataRow
     * @see #setColumnName(String)
     */
	public void setDataRow(IDataRow pDataRow) throws ModelException
	{
		uninstallEditor();
		
		dataRow = pDataRow;
		
		installEditor();
	}
	
	/**
	 * Gets the name of the editor {@link JComponent}.
	 * 
	 * @return the name of the editor {@link JComponent}.
	 */
	public String getEditorComponentName()
	{
		return dummyEditor.getName();
	}
	
	/**
	 * Sets the name of the editor {@link JComponent} if it is a {@link JComboBox}
	 * or of the first focusable {@link JComponent}.
	 * 
	 * @param pName the name to set.
	 */
	public void setEditorComponentName(String pName)
	{
		// Setting the name of the dummyEditor and the focusableComponent
		// (see getFocusableComponent()) allows to retrieve the Swing component
		// by name, which enables us to do automated testing of the GUI.
		dummyEditor.setName(pName);
		
		// We need to set the name of the whole combobox, as the Synth themes
		// do rely on that the textbox inside the combobox does have the name
		// "ComboBox.textField". If it doesn't have that name it will receive
		// wrong margins/borders, which will make it taller than it should be.
		if (cellEditorHandler != null && cellEditorHandler.getCellEditorComponent() instanceof JComboBox)
		{
			cellEditorHandler.getCellEditorComponent().setName(pName);
		}
		else if (focusableComponent != null)
		{
			focusableComponent.setName(pName);
		}
	}
	
	/**
     * Gets the column name edited by this control.
     *
     * @return the column name.
     * @see #setColumnName
     */
	public String getColumnName()
	{
		return columnName;
	}
	
	/**
     * Sets the column edited by this control.
     *
     * @param pColumnName the column.
     * @throws ModelException if the column name is invalid
     * @see #getColumnName
     */
	public void setColumnName(String pColumnName) throws ModelException
	{
		uninstallEditor();
		
		columnName = pColumnName;
		
		installEditor();
	}

	/**
	 * Sets whether the CellEditor should save immediate.
	 * @param pSavingImmediate true, if the CellEditor should save immediate.
	 */
	public void setSavingImmediate(boolean pSavingImmediate)
	{
		savingImmediate = pSavingImmediate;
	}

	/**
	 * Uninstalls the CellEditor and its CellEditorComponent.
	 */
	private void uninstallEditor()
	{
		if (cellEditorHandler != null)
		{
			if (focusableComponent != null)
			{
				focusableComponent.removeFocusListener(this);
				
				for (MouseListener mouseListener : getMouseListeners())
				{
					focusableComponent.removeMouseListener(mouseListener);
				}
				for (KeyListener keyListener : getKeyListeners())
				{
					focusableComponent.removeKeyListener(keyListener);
				}
			}
			
			// TODO It is unclear, if uninstalling an Editor should call cancel or save.
			// Till it is not clear and no problems occur, nothing is done, what is nearby a cancel.
			// cellEditorHandler.cancelEditing() or cellEditorHandler.saveEditing();
			cellEditorHandler.uninstallEditor();

			dataRow.removeControl(this);
			
			remove(cellEditorHandler.getCellEditorComponent());
			
			cellEditorHandler = null;
			
			editingStarted = false; // Without having any solution, if the editor should be saved or cancelled, after uninstalling, it should be not in editingStarted mode.
		}
		else
		{
			remove(dummyEditor);
		}
	}
	
    /**
     * Returns whether the the border of the text field is visible.
     * 
     * @return <code>true</code> if the border is visible, <code>false</code> if the border is invisible
     */
    public boolean isBorderVisible()
    {
		return borderVisible;
    }

	/**
     * Sets the border of the text field visible or invisible.
     * 
     * @param pVisible <code>true</code> to set the border visible or <code>false</code> to hide
     *                 the border
     */
    public void setBorderVisible(boolean pVisible)
	{
    	uninstallEditor();
    	borderVisible = pVisible;
    	installEditor();
	}
    
	/**
	 * Installs the CellEditor and its CellEditorComponent.
	 */
	private void installEditor()
	{
		dummyEditor.setBackground(colDummyBack);
		
		focusableComponent = null;

		if (dataRow != null && columnName != null)
		{
			try
			{
				IDataType dataType = dataRow.getRowDefinition().getColumnDefinition(columnName).getDataType();
				ICellEditor editor;
				if (cellEditor == null)
				{
					editor = dataType.getCellEditor();
				}
				else
				{
					editor = cellEditor;
				}
				
		    	if (editor == null)
		    	{
		    		editor = JVxUtil.getDefaultCellEditor(dataType.getTypeClass());
		    	}
				
				cellEditorHandler = editor.createCellEditorHandler(this, dataRow, columnName);
				
				Component cellEditorComponent = cellEditorHandler.getCellEditorComponent();
				
				add(cellEditorComponent, JVxBorderLayout.CENTER);
				dataRow.addControl(this);

				if (isNotified)
				{
                    focusableComponent = getFocusableComponent(cellEditorComponent);
					
					focusableComponent.addFocusListener(this);
					
					for (MouseListener mouseListener : getMouseListeners())
					{
						focusableComponent.addMouseListener(mouseListener);
					}
					for (KeyListener keyListener : getKeyListeners())
					{
						focusableComponent.addKeyListener(keyListener);
					}

					try
                    {
					    isCancelling = true;
                        cancelEditing(); // run() instead of notifyRepaint() to ensure immediate paint at first time, necessary for designmode.
                    }
                    catch (Throwable ex)
                    {
                        uninstallEditor();
                        
                        throw new ModelException("Cancel editing failed!");
                    }
					finally
					{
					    isCancelling = false;
					}
				}
			}
			catch (ModelException ex)
			{
				dummyEditor.setBackground(JVxUtil.getSystemColor(IColor.INVALID_EDITOR_BACKGROUND));
			}
		}
		
		if (cellEditorHandler == null)
		{
			add(dummyEditor, JVxBorderLayout.CENTER);
		}
		
		// #1458, swapping out cell editors in a value changed event has
		// the potential that the UI is not properly updated.
		// We have to make sure that this happens.
		JVxUtil.revalidateAllDelayed(this);
	}
	
	/**
	 * Gets the focusable sub component of the given component.
	 * @param pComponent the component.
	 * @return the focusable sub component of the given component.
	 */
	protected Component getFocusableComponent(Component pComponent)
	{
		// Trick out focus manager, to give proper focusable component, even if not visible.
		// This will only work if already notified.
		temporaryIsShowingState = true; 
		
		Component comp = null;
		
		try
		{
			comp = FOCUS_TRAVERSAL_POLICY.getFirstComponent(this);
		}
		catch (Throwable th)
		{
			// The FocusTraversalPolicy might fail with a ClassCastException(!)
			// if the component is not added. We might be able to reach this
			// point because of DataBookEvents, with the editor actually not
			// being added to a parent (or its parent not being added to
			// a parent...and so on, you get the picture.
			//
			// Now on to the ClassCastException. The Swing LayoutComparator
			// throws a ClassCastException if the component is not added to
			// a window... ... ...
		}
		
		temporaryIsShowingState = false;

		// The name is set so that the component can be retrieved by name,
		// for example for automated testing of the GUI.
		
		if (comp == null || comp == this)
		{
			pComponent.setName(dummyEditor.getName());
			return pComponent;
		}
		else if (pComponent instanceof JComboBox)
		{
			pComponent.setName(dummyEditor.getName());
		}
		else if (!StringUtil.isEmpty(dummyEditor.getName()))
		{
			comp.setName(dummyEditor.getName());
		}
		
		return comp;
	}
	
	/**
     * Gets the CellEditor that edits the given column in the given DataRow.
     * If the CellEditor is null, the editor from the columns DataType is used to edit.
     *
     * @return the CellEditor.
     * @see #setCellEditor
     */
	public ICellEditor getCellEditor()
	{
		return cellEditor;
	}
	
	/**
     * Sets the CellEditor that edits the given column in the given DataRow.
     * If the CellEditor is null, the editor from the columns DataType is used to edit.
     *
     * @param pCellEditor the CellEditor.
     * @throws ModelException if the column name of the editor is invalid
     * @see #getCellEditor
     */
	public void setCellEditor(ICellEditor pCellEditor) throws ModelException
	{
        saveEditing();

		uninstallEditor();
		
		cellEditor = pCellEditor;
		
		installEditor();
	}

	/** 
	 * The current used CellEditor for editing.
	 *  
	 * @return The current used CellEditor for editing.
	 */
	public ICellEditorHandler<JComponent> getCellEditorHandler()
	{
		return cellEditorHandler;
	}
	
    /**
     * Sets the cell formatter.
     * 
     * @param pCellFormatter the formatter
     */
    public void setCellFormatter(ICellFormatter pCellFormatter)
    {
        cellFormatter = pCellFormatter;
    }

}	// JVxEditor
