/*
 * Copyright 2012 SIB Visions GmbH
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
 * 25.04.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.control;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.TreePath;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.IControl;
import javax.rad.ui.control.ICellFormatter;
import javax.rad.ui.control.INodeFormatter;
import javax.rad.ui.control.ITree;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedTreeTable;
import com.sibvisions.rad.ui.vaadin.ext.ui.table.JVxHierarchicalContainer;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.IEditorComponent;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.ShortcutHandler;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTree.TreeTableComponent;
import com.vaadin.event.Action;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.Table.ColumnHeaderMode;

/**
 * The <code>VaadinTree</code> is the <code>ITree</code>
 * implementation for vaadin.
 *  
 * @author Stefan Wurm
 */
@SuppressWarnings("deprecation")
public class VaadinTree extends VaadinComponent<TreeTableComponent> 
                        implements ITree, 
                                   ItemClickListener,
						           Runnable, 
						           ICellFormatterEditorListener,
						           ValueChangeListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/** The container for the vaadin tree with the data books. **/
	private JVxHierarchicalContainer jvxContainer = null;
	
	/** The IDataBook to be shown. */
	private IDataBook[] dataBooks = null;
	
	/** The selfjoined databooks. */
	private IDataBook selfJoinedDataBook = null;
	
	/** The selected data book. **/
	private IDataBook currentDataBook = null;
	
	/** The selected tree path. **/
	private TreePath selectedTreePath = null;
	
	/** The maximum length of not selfjoined databooks. */
	private int maxLength = 0;
	
	/** The cellFormatListener. */
	private ICellFormatter cellFormatter = null;
	
	/** The Node Formatter. **/
	private INodeFormatter nodeFormatter = null;

	/** The translation mapping. */
	private TranslationMap translation = null;

	/** Tells, if notifyRepaint is called the first time. */
	private boolean bFirstNotifyRepaintCall = true;

	/** Cell Editor started editing. */
	private boolean bEditingStarted = false;
	
	/** Ignoring Events. */
	private boolean bIgnoreEvent = false;

	/** If the table is editable. **/
	private boolean editable = false;	
	
    /** whether a node should be detected to be an end node or not. */
    private boolean bDetectEndNode = true;

    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * New instance of <code>VaadinTree</code>.
	 */
	public VaadinTree()
	{
		super(new TreeTableComponent());
		
		((TreeTableComponent)resource).tree = this;
		resource.setPageLength(16);
		
		jvxContainer = new JVxHierarchicalContainer(this);
		
		resource.setSelectable(true);
		resource.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		resource.setStyleName("jvxtree");
		resource.setNullSelectionAllowed(false);
		
		resource.addItemClickListener(this);
		resource.addValueChangeListener(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */	
	public void run()
	{
		bFirstNotifyRepaintCall = true;
		
    	if (!bIgnoreEvent && dataBooks != null)
    	{
    		try
    		{
    			bIgnoreEvent = true;

    			jvxContainer.initialize();
    		}
			finally
			{
				bIgnoreEvent = false;
			}	
    	}
	}		
	
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
			}
		}
		
		if (pDataBooks == null || pDataBooks.length == 0)
		{
			dataBooks = null;
		}
		else
		{
			dataBooks = pDataBooks;
		}
		
		if (dataBooks != null)
		{
			for (int i = 0; i < dataBooks.length; i++)
			{
				dataBooks[i].addControl(this);
			}
			
			IDataBook dataBook = dataBooks[dataBooks.length - 1];
			
			if (dataBook.isSelfJoined())
			{
				selfJoinedDataBook = dataBook;
				maxLength = dataBooks.length - 1;
			}
			else
			{
				maxLength = dataBooks.length;
			}
		}
		
		jvxContainer.setDataBooks(dataBooks);
		jvxContainer.addItemSetChangeListener(resource);
		
		resource.setContainerDataSource(jvxContainer);

		if (jvxContainer.getContainerPropertyIds().size() > 0)
		{
			resource.setVisibleColumns(new Object [] {jvxContainer.getContainerPropertyIds().iterator().next()});
			resource.setItemIconPropertyId(JVxHierarchicalContainer.ITEM_ICON_PROPERTY_ID);
		}

		notifyRepaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IDataBook getActiveDataBook()
	{
		return currentDataBook;
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyRepaint()
	{
		if (bFirstNotifyRepaintCall && !bIgnoreEvent && !bEditingStarted) // Check additionally if editing is started, to prevent immediate closing editor
		{
			bFirstNotifyRepaintCall = false;
			
			getFactory().invokeLater(this);
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */	
	public void editingStarted()
	{
		bEditingStarted = true; // first set bEditingStarted true, to prevent events on update.
	}

	/**
	 * {@inheritDoc}
	 */	
	public void editingComplete(String pCompleteType)
	{
		if (pCompleteType == ICellEditorListener.ESCAPE_KEY)
		{
			if (jvxContainer.isEditMode())
			{
				if (jvxContainer.getCellEditorHandler() != null)
				{
					try
					{
						jvxContainer.getCellEditorHandler().cancelEditing();
						cancelEditing();
					}
					catch (ModelException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		else
		{
			try
			{
				saveEditing();
			}
			catch (ModelException ex)
			{
				cancelEditing();
				ExceptionHandler.raise(ex);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */	
	public boolean isSavingImmediate()
	{
		return false;
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
	public void startEditing()
	{
		if (isEditable())
		{
			jvxContainer.setEditMode(true);
			jvxContainer.initialize();
			
			resource.select(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */	
	public void saveEditing() throws ModelException
	{
		if (bEditingStarted)
		{
        	if (jvxContainer.getCellEditorHandler() != null)
        	{
        		bEditingStarted = false;
        		
        		jvxContainer.getCellEditorHandler().saveEditing();
        	}

			cancelEditing();
		}
	}

	/**
	 * {@inheritDoc}
	 */	
	public void cancelEditing()
	{
		if (isEditable() && jvxContainer.isEditMode())
		{
			jvxContainer.setEditMode(false);
			bEditingStarted = false;
			jvxContainer.initialize();

			resource.select(selectedTreePath);
		}	
	}

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
	    	catch (ModelException e) 
	    	{
				cancelEditing();
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
	public boolean isDetectEndNode()
	{
		return bDetectEndNode;
	}

	/**
	 * {@inheritDoc}
	 */	
	public void setDetectEndNode(boolean pDetectEndNode)
	{
		bDetectEndNode = pDetectEndNode;
	}

	/**
	 * {@inheritDoc}
	 */	
	public boolean isEditable()
	{
		return editable;
	}

	/**
	 * {@inheritDoc}
	 */	
	public void setEditable(boolean pEditable)
	{
		editable = pEditable;
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
	public void setNodeFormatter(INodeFormatter pNodeFormatter)
	{
		nodeFormatter = pNodeFormatter;
	}
	
	// Click Listener
	
	/**
	 * {@inheritDoc}
	 */	
	public void itemClick(ItemClickEvent pEvent)
	{
		if (bIgnoreEvent)
		{
			return;
		}
		
		try
		{
			selectedTreePath = (TreePath) pEvent.getItemId();
			
			bIgnoreEvent = true;
			
			if (editable)
			{		
				if (!pEvent.isDoubleClick())
				{
					saveEditing();		
				}	
				
				selectPathInDataBooks();		

				resource.select(selectedTreePath);

				if (pEvent.isDoubleClick())
				{
					startEditing();
				}
			}
			else
			{
				selectPathInDataBooks();	
				
				resource.select(selectedTreePath);
			}

			dispatchMouseClickedEvent(pEvent);			
		
		}
		catch (Throwable thr)
		{
			bIgnoreEvent = false;
			notifyRepaint();
			ExceptionHandler.raise(thr);
		}
		finally
		{
			bIgnoreEvent = false;
		}
	}
	
	//Value Changed Listener.
	
	/**
	 * {@inheritDoc}
	 */
	public void valueChange(ValueChangeEvent pEvent)
	{	
		if (bIgnoreEvent)
		{
			return;
		}
		
		try
		{			
			bIgnoreEvent = true;
			
			selectPathInDataBooks();	
			
			resource.select(selectedTreePath);

		}
		catch (Throwable thr)
		{
			bIgnoreEvent = false;
			notifyRepaint();
			ExceptionHandler.raise(thr);
		}
		finally
		{
			bIgnoreEvent = false;
		}
	}		

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWidthFull()
	{
		if (VaadinUtil.isParentWidthDefined(resource))
		{
			VaadinUtil.setComponentWidth(resource, 100, Unit.PERCENTAGE);
		}
		else
		{
			setWidthUndefined();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeightFull()
	{
		if (VaadinUtil.isParentHeightDefined(resource))
		{
			VaadinUtil.setComponentHeight(resource, 100, Unit.PERCENTAGE);
		}
		else
		{
			setHeightUndefined();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWidthUndefined()
	{
		VaadinUtil.setComponentWidth(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeightUndefined()
	{
		VaadinUtil.setComponentHeight(resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
	}	

	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseEventOnSelectedCell()
	{
		return true;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Select the path in the databooks.
	 * 
	 * @throws ModelException 
	 */ 
	private void selectPathInDataBooks() throws ModelException
	{	
		if (selectedTreePath != null)
		{
			int selLength = Math.min(maxLength, selectedTreePath.length());
			
			for (int i = 0; i < selLength; i++)
			{
				dataBooks[i].setSelectedDataPageRow(selectedTreePath.get(i));
				currentDataBook = dataBooks[i];
			}
			
			for (int i = selLength; i < maxLength; i++)
			{
				dataBooks[i].setSelectedRow(-1);
			}
			
			if (selfJoinedDataBook != null)
			{
				int pathLen = selectedTreePath.length() - dataBooks.length;
				
				TreePath treePath = new TreePath();
				int selRow = -1;
				if (pathLen >= 0)
				{
					for (int i = 0; i < pathLen; i++)
					{
						treePath = treePath.getChildPath(selectedTreePath.get(dataBooks.length - 1 + i));
					}
					
					selRow = selectedTreePath.getLast();
				}
				selfJoinedDataBook.setTreePath(treePath);
				selfJoinedDataBook.setSelectedDataPageRow(selRow);
				currentDataBook = selfJoinedDataBook;
			}
		}
	}
	
	/**
	 * Returns the selected <code>TreePath</code>.
	 * 
	 * @return the selected <code>TreePath</code>. 
	 */
	public TreePath getSelectedTreePath()
	{
		return selectedTreePath;
	}

	
	/**
	 * Sends the mouse click event.
	 * 
	 * @param pEvent the item click event.
	 */
	private void dispatchMouseClickedEvent(ItemClickEvent pEvent)
	{
		dispatchMouseEvent(eventMousePressed, pEvent, UIMouseEvent.MOUSE_PRESSED);
		dispatchMouseEvent(eventMouseClicked, pEvent, UIMouseEvent.MOUSE_CLICKED);
		dispatchMouseEvent(eventMouseReleased, pEvent, UIMouseEvent.MOUSE_RELEASED);
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************	
	
	/**
	 * The <code>TreeTableComponent</code> class handles all actions.
	 * 
	 * @author Stefan Wurm
	 */
	static class TreeTableComponent extends ExtendedTreeTable 
	                                implements IEditorComponent, 
	                                           CellStyleGenerator
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the connected tree. */
		private VaadinTree tree;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>TreeTableComponent</code>.
		 */
		public TreeTableComponent()
		{
			setCellStyleGenerator(this);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Handles the key actions.
		 * 
		 * @param pAction the key action.
		 */
		public void handleAction(Action pAction)
		{
			if (tree.isEditable())
			{
				if (pAction == ShortcutHandler.ACTION_TAB)
				{
					tree.editingComplete(ICellEditorListener.TAB_KEY);	
				}
				else if (pAction == ShortcutHandler.ACTION_SHIFT_TAB)
				{
					tree.editingComplete(ICellEditorListener.SHIFT_TAB_KEY);
				}
				else if (pAction == ShortcutHandler.ACTION_ENTER
						 || pAction == ShortcutHandler.ACTION_ALT_ENTER
		     			 || pAction == ShortcutHandler.ACTION_CTRL_ENTER
		     			 || pAction == ShortcutHandler.ACTION_META_ENTER)
				{
					tree.editingComplete(ICellEditorListener.ENTER_KEY);	
				}
				else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
				{
					tree.editingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
				}
				else if (pAction == ShortcutHandler.ACTION_ESCAPE)
				{
					tree.editingComplete(ICellEditorListener.ESCAPE_KEY);
				}
				else if (pAction == ShortcutHandler.ACTION_EDIT)
				{
					tree.startEditing();
				}
			}		
		}

		/**
		 * Returns the style Name for every level.
		 * 
		 * @param pItemId the itemId
		 * @param pPropertyId the propertyId
		 * @param pSource the table.
		 * 
		 * @return the style name
		 */
		public String getStyle(Table pSource, Object pItemId, Object pPropertyId)
		{
			if (pItemId instanceof TreePath)
			{
				return "level-" + ((TreePath)pItemId).length();
			}
			
			return "";
		}
		
	}	// TreeTableComponent
	
}	// VaadinTree
