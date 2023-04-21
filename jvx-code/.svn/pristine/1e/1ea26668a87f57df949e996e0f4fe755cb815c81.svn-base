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
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.ITreeControl;
import javax.rad.ui.component.IEditable;
import javax.rad.ui.control.ITree;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.sibvisions.rad.ui.swing.ext.format.ICellFormatter;
import com.sibvisions.rad.ui.swing.ext.format.INodeFormatter;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>JVxTree</code> is a scrollable JTree that implements {@link ITreeControl} interface.
 *  
 * @author Martin Handsteiner
 */
public class JVxTree extends JVxScrollPane 
                     implements ITreeControl,
                                IEditable,
                                ICellFormatterEditorListener,
                                Runnable,
                                TreeSelectionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The {@link ILogger} for the {@link JVxTree} class. */
	private static final ILogger LOGGER = LoggerFactory.getInstance(JVxTree.class);
	
	/** The JTree that shows the IDataBooks. */
	private JTree tree = new JTree();
	
	/** The IDataBook to be shown. */
	private IDataBook[] dataBooks = null;

	/** The selfjoined databooks. */
	private IDataBook selfJoinedDataBook = null;

	/** The selfjoined databooks. */
	private IDataBook activeDataBook = null;

	/** The cellFormatListener. */
	private ICellFormatter cellFormatter = null;
	
	/** The cellFormatListener. */
	private INodeFormatter nodeFormatter = null;
	
	/** The translation mapping. */
	private TranslationMap translation = null;
	
    /** The maximum length of not selfjoined databooks. */
    private int maxLength = 0;

    /** True, if the editable is shown. */
	private boolean editable = false;
	
	/** True, if the end node should be detected is shown. */
	private boolean detectEndNode = true;
	
	/** Tells, if notifyRepaint is called the first time. */
	private boolean firstNotifyRepaintCall = true;

	/** True, if the end node should be detected is shown. */
	private boolean doNotScrollToSelectedNode = false;
	
	/** Ignoring Events. */
	private boolean ignoreEvent = false;
	
	/** is notified. */
	private boolean isNotified = false;
	
	/** true, if the mouse event is on selected cell. */
	private boolean mouseEventOnSelectedCell = false;

    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Constructs a JVxTable.
	 */
	public JVxTree()
	{
		setViewportView(tree);
		installMouseListenerToGainFocus();

		DataRowTreeCellRenderer renderer = new DataRowTreeCellRenderer();
		tree.setCellRenderer(renderer);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellEditor(new DataRowTreeCellEditor(tree, renderer));
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.setRowHeight(0);
		tree.addMouseListener(this);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();
		ignoreEvent = true;
		tree.scrollRectToVisible(new Rectangle());
		tree.getSelectionModel().clearSelection();
		ignoreEvent = false;

		isNotified = true;

		notifyRepaint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNotify()
	{
		isNotified = false;

		super.removeNotify();
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
	public void mousePressed(MouseEvent pMouseEvent)
	{
		TreePath path = tree.getPathForLocation(pMouseEvent.getX(), pMouseEvent.getY());
		
		mouseEventOnSelectedCell = path != null;

		if (SwingUtilities.isRightMouseButton(pMouseEvent) && mouseEventOnSelectedCell)
		{
			tree.setSelectionPath(path);
		}
		super.mousePressed(pMouseEvent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent pMouseEvent)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				mouseEventOnSelectedCell = false;
			}
		});
	}
	
    /**
     * {@inheritDoc}
     */
	public void valueChanged(TreeSelectionEvent pTreeSelectionEvent)
	{
		if (!ignoreEvent)
		{
			ignoreEvent = true;
			try 
			{
				TreePath currentPath = pTreeSelectionEvent.getPath();
				
				Object[] path = currentPath.getPath();
				
				int selLength = Math.min(maxLength, path.length - 1);
				
				for (int i = 0; i < selLength; i++)
				{
					dataBooks[i].setSelectedDataPageRow(((DataPageNode)path[i + 1]).rowIndex);
				}
				for (int i = selLength; i < maxLength; i++)
				{
					dataBooks[i].setSelectedRow(-1);
				}
				if (selfJoinedDataBook != null)
				{
					int pathLen = path.length - 1 - dataBooks.length;
					
					javax.rad.model.TreePath treePath = new javax.rad.model.TreePath();
					int selRow = -1;
					if (pathLen >= 0)
					{
						for (int i = 0; i < pathLen; i++)
						{
							treePath = treePath.getChildPath(((DataPageNode)path[dataBooks.length + i]).rowIndex);
						}
						selRow = ((DataPageNode)path[path.length - 1]).rowIndex;
						selLength++;
					}
					selfJoinedDataBook.setTreePath(treePath);
					selfJoinedDataBook.setSelectedDataPageRow(selRow);
				}
				ignoreEvent = false;
				notifyRepaint();
			}
			catch (Throwable pThrowable)
			{
				ignoreEvent = false;
				notifyRepaint();
				
				ExceptionHandler.raise(pThrowable);
			}
		}
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
	public void notifyRepaint()
	{
	    if (firstNotifyRepaintCall && !ignoreEvent)
		{
			firstNotifyRepaintCall = false;
			
			JVxUtil.invokeLater(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void startEditing() 
	{
	    JVxUtil.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                TreePath lead = tree.getSelectionPath();

                if (lead != null) 
                {
                    tree.startEditingAtPath(lead);
                }
            }
        });
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void saveEditing() throws ModelException 
	{
    	tree.stopEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void cancelEditing() 
	{
    	tree.cancelEditing();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void editingComplete(String pCompleteType)
	{
    	// TODO [HM]
	}

	/**
	 * {@inheritDoc}
	 */
	public void editingStarted()
	{
    	// TODO [HM]
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSavingImmediate()
	{
    	// TODO [HM]
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public IControl getControl()
	{
		return this;
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

    //Runnable

    /**
     * {@inheritDoc}
     */
    public void run()
    {
        firstNotifyRepaintCall = true;

        if (isNotified)
        {
            doRepaint();
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the DataBooks displayed by this control.
     *
     * @return the DataBooks.
     * @see #setDataBooks
     */
    public IDataBook[] getDataBooks()
    {
    	return dataBooks;
    }

    /**
     * Sets the DataBooks displayed by this control.
     * 
	 * @param pDataBooks the DataBooks
     * @see #getDataBooks
     */
    public void setDataBooks(IDataBook... pDataBooks)
    {
		if (dataBooks != null)
		{
			tree.getSelectionModel().removeTreeSelectionListener(this);
			tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));

			for (int i = 0; i < dataBooks.length; i++)
			{
				dataBooks[i].removeControl(this);
			}
			selfJoinedDataBook = null;
			activeDataBook = null;
			maxLength = 0;
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
			tree.setModel(new DefaultTreeModel(new DataPageNode(this, dataBooks)));
			tree.getSelectionModel().addTreeSelectionListener(this);
			
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
    }	

    /**
     * Repaints the table and updates the selection.
     */
    private void doRepaint()
    {
        if (!ignoreEvent && dataBooks != null)
        {
            for (int i = 0; i < dataBooks.length; i++)
            {
                IDataBook dataBook = dataBooks[i];
                if (dataBook == null || !dataBook.isOpen())
                {
                    return;
                }
            }

            ignoreEvent = true;
            try
            {
                int x = getHorizontalScrollBar().getValue();
                int y = getVerticalScrollBar().getValue();
                
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                
                DataPageNode node = (DataPageNode)model.getRoot();
                node.initDataPage(); // if root row is outside the tree, the tree has to be rerooted.
//              if (dataBooks[0].isSelfJoined())
//              {
//                  dataBooks[0].getSelectedRow(); // sync
//                  node.dataPage = dataBooks[0].getDataPage(new javax.rad.model.TreePath());
//              }
//              else
//              {
//                  node.dataPage = dataBooks[0].getDataPage();
//              }
                
                TreePath path = new TreePath(node);
                
                Enumeration<TreePath> expanded = tree.getExpandedDescendants(path);
                model.reload(node);
                if (expanded != null)
                {
                    while (expanded.hasMoreElements())
                    {
                        TreePath expPath = expanded.nextElement();
                        if (isAllValid(expPath.getPath()))
                        {
                            tree.expandPath(expPath);
                        }
                    }
                }
                
                boolean found = true;
                int selLength = -1;
                
                for (int i = 0; i < maxLength; i++)
                {
                    int selRow = dataBooks[i].getSelectedDataPageRow();
                    if (found)
                    {
                        found = selRow >= 0;
                        if (found)
                        {
                            selLength = i;
                            node = (DataPageNode)node.getChildAt(selRow);
                            path = path.pathByAddingChild(node);
                        }
                    }
                    else
                    {
                        dataBooks[i].setSelectedRow(-1);
                    }
                }
                if (selfJoinedDataBook != null)
                {
                    if (found)
                    {
                        javax.rad.model.TreePath treePath = selfJoinedDataBook.getTreePath();
                        
                        for (int i = 0; i < treePath.length(); i++)
                        {
                            node = (DataPageNode)node.getChildAt(treePath.get(i));
                            path = path.pathByAddingChild(node);
                        }
                        if (selfJoinedDataBook.getSelectedDataPageRow() < 0)
                        {
                            if (treePath.length() > 0)
                            {
                                selfJoinedDataBook.setTreePath(treePath.getParentPath());
                                selfJoinedDataBook.setSelectedDataPageRow(treePath.getLast());
                                selLength++;
                            }
                        }
                        else
                        {
                            node = (DataPageNode)node.getChildAt(selfJoinedDataBook.getSelectedDataPageRow());
                            path = path.pathByAddingChild(node);
                            selLength++;
                        }
                    }
                    else
                    {
                        selfJoinedDataBook.setTreePath(null);
                        selfJoinedDataBook.setSelectedRow(-1);
                    }
                }

                tree.setEditable(editable && selLength >= 0 && !dataBooks[selLength].isReadOnly());

                tree.setSelectionPath(path);
                
                getHorizontalScrollBar().setValue(x);
                getVerticalScrollBar().setValue(y);
                if (!doNotScrollToSelectedNode)
                {
                    tree.scrollPathToVisible(path);
                }

                IDataBook newActiveDataBook;
                if (selLength < 0)
                {
                    newActiveDataBook = null;
                }
                else
                {
                    newActiveDataBook = dataBooks[selLength];
                }
                if (newActiveDataBook != activeDataBook)
                {
                    activeDataBook = newActiveDataBook;

                    treeProcessFocusGained();
                }

                ignoreEvent = false;
            }
            catch (Exception pException)
            {
                ignoreEvent = false;

                JVxTree.LOGGER.error(pException);
            }
        }
        doNotScrollToSelectedNode = false;
    }

    /**
     * Inspects, if the given tree path is valid.
     * 
     * @param pDataNodes the tree nodes.
     * @return if the given tree path is valid.
     */
    private static boolean isAllValid(Object[] pDataNodes)
    {
        for (int i = 0; i < pDataNodes.length; i++)
        {
            if (!(((DataPageNode)pDataNodes[i]).isValidNode()))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Sends focus gained event to the tree listeners.
     */
    private void treeProcessFocusGained()
    {
        if (tree.hasFocus())
        {
            FocusListener[] listeners = tree.getFocusListeners();
        
            FocusEvent focusGainedEvent = new FocusEvent(tree, FocusEvent.FOCUS_GAINED);
            
            for (int i = 0; i < listeners.length; i++)
            {
                listeners[i].focusGained(focusGainedEvent);
            }
        }
    }
    
    /**
     * Gets the node formatter.
     * 
     * @return the node formatter
     */
    public INodeFormatter getNodeFormatter()
    {
        return nodeFormatter;
    }
    
    /**
     * Gets whether end node detection is enabled.
     * 
     * @return <code>true</code> if end node detection is enabled, <code>false</code> otherwise
     */
    public boolean isDetectEndNode()
    {
        return detectEndNode;
    }

    /**
     * Sets end node detection en- or disabled.
     * 
     * @param pDetectEndNode <code>true</code> to enable end node detection, <code>false</code> to disable
     */
    public void setDetectEndNode(boolean pDetectEndNode)
    {
        detectEndNode = pDetectEndNode;
        
        if (isNotified)
        {
            notifyRepaint();
        }
    }
    
    /**
     * Gets whether this tree is editable.
     * 
     * @return <code>true</code> if editable, <code>false</code> otherwise
     */
    public boolean isEditable()
    {
        return editable;
    }

    /**
     * Sets this tree editable or not editable.
     * 
     * @param pEditable <code>true</code> to enable editing, <code>false</code> otherwise
     */
    public void setEditable(boolean pEditable)
    {
        editable = pEditable;
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
    
    /**
     * Sets the node formatter.
     * 
     * @param pNodeFormatter the node formatter
     */
    public void setNodeFormatter(INodeFormatter pNodeFormatter)
    {
        nodeFormatter = pNodeFormatter;
    }
    
    /**
     * Gets whether mouse event on selected cell is enabled.
     * 
     * @return <code>true</code> if mouse event is enabled, <code>false</code> if event is disabled
     */
    public boolean isMouseEventOnSelectedCell()
    {
        return mouseEventOnSelectedCell;
    }
    
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>DataPageNode</code> implements MutableTreeNode.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class DataPageNode implements MutableTreeNode
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The tree. */
		private JVxTree jvxTree;
		/** The tree for this model. */
		private IDataBook[] dataBooks;
		/** The parent DataPageNode. */
		private DataPageNode parent;
		/** The IDataPage for sub nodes. */
		private IDataPage dataPage;
		/** The IDataPage for sub nodes. */
		private IDataRow primaryKey;
		
        /** The parent DataPageNode. */
        private int level;
        /** The rowNumber inside the parent. */
        private int rowIndex;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs a new root DataPageNode.
		 * @param pTree the Tree.
		 * @param pDataBooks the IDataBook's.
		 */
		public DataPageNode(JVxTree pTree, IDataBook[] pDataBooks)
		{
			this(pTree, pDataBooks, 0, null, 0);
		}
		
		/**
		 * Constructs a new DataPageNode.
		 * @param pTree the Tree.
		 * @param pDataBooks the IDataBook's.
		 * @param pLevel the tree level.
		 * @param pParent the parent DataPageNode.
		 * @param pRowIndex the row number.
		 */
		private DataPageNode(JVxTree pTree, IDataBook[] pDataBooks, int pLevel, DataPageNode pParent, int pRowIndex)
		{
			jvxTree = pTree;
			dataBooks = pDataBooks;
			level = pLevel;
			parent = pParent;
			rowIndex = pRowIndex;
			
			initDataPage();
		}
		
		/**
		 * Initializes the DataPage.
		 */
		private void initDataPage() 
		{
			try
			{
				if (parent == null)
				{
					if (dataBooks[0].isSelfJoined())
					{
						dataBooks[0].getSelectedDataPageRow(); // sync
						dataPage = dataBooks[0].getDataPage(javax.rad.model.TreePath.EMPTY);
					}
					else
					{
						dataPage = dataBooks[0].getDataPage();
					}
					primaryKey = null;
				}
				else
				{
					IDataBook dataBook = getDataBook(level);
					
					primaryKey = getDataRow();

					if (dataBook == null || primaryKey == null)
					{
						dataPage = null;
					}
					else if (dataBook.isSelfJoined() && dataBook != getDataBook(level - 1))
					{
						dataPage = dataBook.getDataPageWithRootRow(primaryKey);
					}
					else
					{
						dataPage = dataBook.getDataPage(primaryKey);
					}
					primaryKey = getPrimaryKey(primaryKey);
				}
			}
			catch (Exception pException)
			{
				JVxTree.LOGGER.error(pException);
				dataPage = null;
				primaryKey = null;
			}
		}
		
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            if (parent == null)
            {
                return null;
            }
            else
            {
                try
                {
                    Object value = getDataRow().getValue(parent.dataPage.getDataBook().getRowDefinition().getColumnView(ITree.class).getColumnName(0));
                    if (value == null)
                    {
                        return null;
                    }
                    else
                    {
                        return value.toString();
                    }
                }
                catch (Exception pException)
                {
                    return pException.getMessage();
                }
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object pObject)
        {
            if (pObject == this)
            {
                return true;
            }
            else if (pObject instanceof DataPageNode) 
            {
                DataPageNode node = (DataPageNode)pObject;

                return level == node.level && (primaryKey == node.primaryKey || (primaryKey != null && primaryKey.equals(node.primaryKey)));
            }
            else
            {
                return false;
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode()
        {
            int hash = (level + 13) * 17;
            if (primaryKey != null)
            {
                hash += primaryKey.hashCode();
            }
            return hash;
        }
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * {@inheritDoc}
	     */
	    public TreeNode getChildAt(int pChildIndex)
	    {
	    	return new DataPageNode(jvxTree, dataBooks, level + 1, this, pChildIndex);
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public int getChildCount()
	    {
			if (dataPage != null)
			{
				try
				{
	    			if (!dataPage.isAllFetched())
	    			{
						int lastVisibleRowIndex;
						if (jvxTree.tree.getParent() instanceof JViewport)
						{
							Rectangle viewRect = ((JViewport)jvxTree.tree.getParent()).getViewRect();
		
							lastVisibleRowIndex = (viewRect.y + viewRect.height) * 10 / Math.max(16, jvxTree.tree.getRowHeight()) / 9 + 1;
						}
						else
						{
							Rectangle viewRect = jvxTree.tree.getBounds();
							
							lastVisibleRowIndex = viewRect.height * 10 / Math.max(16, jvxTree.tree.getRowHeight()) / 9 + 1;
						}
						if (lastVisibleRowIndex >= dataPage.getRowCount())
						{
							boolean resetCursor = JVxUtil.getGlobalCursor(jvxTree).getType() != Cursor.WAIT_CURSOR;
							JVxUtil.setGlobalCursor(jvxTree, Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							try
							{
								dataPage.getDataRow(lastVisibleRowIndex);
								jvxTree.notifyRepaint();
								jvxTree.doNotScrollToSelectedNode = true;
							}
							finally
							{
								if (resetCursor)
								{
									JVxUtil.setGlobalCursor(jvxTree, null);
								}
							}
	    				}
					}
					
					return dataPage.getRowCount();
				}
				catch (Throwable me)
				{
					ExceptionHandler.raise(me);
				}
			}
			return 0;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public TreeNode getParent()
	    {
	    	return parent;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public int getIndex(TreeNode pNode)
	    {
	    	return ((DataPageNode)pNode).rowIndex;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public boolean getAllowsChildren()
	    {
	    	return dataPage != null;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public boolean isLeaf()
	    {
	    	try
	    	{
	    		if (jvxTree.detectEndNode && !dataPage.isAllFetched() && dataPage.getRowCount() == 0)
	    		{
	    			dataPage.getDataRow(0);
	    		}

	    		IChangeableDataRow dataRow = getDataRow();
		    	return (dataRow != null && dataRow.isInserting()) || dataPage == null || (dataPage.isAllFetched() && dataPage.getRowCount() == 0);
	    	}
	    	catch (Exception ex)
	    	{
		    	return true;
	    	}
	    }

	    /**
	     * {@inheritDoc}
	     */
	    public Enumeration children()
	    {
	    	return new Enumeration()
	    	{
	    		private int index = 0;

				public boolean hasMoreElements()
				{
					return index < getChildCount();
				}

				public Object nextElement()
				{
					return getChildAt(index++);
				}
	    	};
	    }

	    /**
	     * {@inheritDoc}
	     */
		public void insert(MutableTreeNode pChild, int pIndex)
		{
			// Not in use
		}

	    /**
	     * {@inheritDoc}
	     */
		public void remove(int pIndex)
		{
			// Not in use
		}

	    /**
	     * {@inheritDoc}
	     */
		public void remove(MutableTreeNode pNode)
		{
			// Not in use
		}

	    /**
	     * {@inheritDoc}
	     */
		public void removeFromParent()
		{
			// Not in use
		}

	    /**
	     * {@inheritDoc}
	     */
		public void setParent(MutableTreeNode pNewParent)
		{
			// Not in use
		}

	    /**
	     * {@inheritDoc}
	     */
		public void setUserObject(Object pObject)
		{
			try
			{
				IDataBook dataBook = parent.dataPage.getDataBook();

				parent.dataPage = dataBook.getDataPage();
					
				dataBook.setValue(dataBook.getRowDefinition().getColumnView(ITree.class).getColumnName(0), pObject);
			}
			catch (Exception pException)
			{
				ExceptionHandler.raise(pException);
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Gets the current IDataRow.
         * @return the current IDataRow.
         */
        public IChangeableDataRow getDataRow()
        {
        	if (parent != null)
        	{
	            try
	            {
	                if (parent.dataPage.getDataBook().isOpen())
	                {
	                    return parent.dataPage.getDataRow(rowIndex);
	                }
	            }
	            catch (Throwable pException)
	            {
	                // Do Nothing
	            }
        	}
            return null;
        }
        
        /**
         * Gets the primary key part of the given datarow. 
         * @param pDataRow the given datarow. 
         * @return the primary key part of the given datarow. 
         */
        public IDataRow getPrimaryKey(IDataRow pDataRow)
        {
            try
            {
                return pDataRow.createDataRow(pDataRow.getRowDefinition().getPrimaryKeyColumnNames());
            }
            catch (Exception pException)
            {
                return null;
            }
        }
        
        /**
         * Checks, if this node is still valid.
         * 
         * @return if this node is still valid.
         */
        public boolean isValidNode()
        {
            if (parent == null)
            {
                return true;
            }
            else if (primaryKey == null)
            {
                return false;
            }
            else if (!parent.isDataPageValid())
            {
            	return false;
            }
            else
            {
                return primaryKey.equals(getPrimaryKey(getDataRow()));
            }
        }

        /**
		 * Gets the IDataBook for the given level.
		 * 
		 * @param pLevel the level.
		 * @return the IDataBook.
		 */
		private IDataBook getDataBook(int pLevel)
		{
			if (pLevel < dataBooks.length)
			{
				return dataBooks[pLevel];
			}
			else
			{
		    	IDataBook dataBook = dataBooks[dataBooks.length - 1];

		    	if (dataBook.isSelfJoined())
		    	{
			    	return dataBook;
		    	}
		    	else
		    	{
		    		return null;
		    	}
			}
		}


		/**
		 * Checks if the {@link IDataPage} of this {@link DataPageNode} is still
		 * valid.
		 * <p/>
		 * Which means that it checks if the parent {@link IDataBook} still has
		 * this page.
		 * 
		 * @return {@code true} if the {@link IDataPage} is still valid.
		 */
		private boolean isDataPageValid()
		{
			if (dataPage != null)
			{
				try
				{
					IDataRow masterDataRow = dataPage.getMasterDataRow();
					IDataBook dataBook = dataPage.getDataBook();
					return dataBook.hasDataPage(masterDataRow) && dataBook.getDataPage(masterDataRow) == dataPage;
				}
				catch (Throwable e)
				{
					JVxTree.LOGGER.error(e);
				}
			}
			return false;
		}
		
	}	// DataPageNode

	/**
	 * The <code>DataRowTreeCellRenderer</code> renders the tree columns.
	 * 
	 * @author Martin Handsteiner
	 */
	public class DataRowTreeCellRenderer extends DefaultTreeCellRenderer
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component getTreeCellRendererComponent(JTree pTree, 
				                                      Object pValue, 
				                                      boolean pSelected, 
				                                      boolean pExpanded, 
				                                      boolean pLeaf, 
				                                      int pRow, 
				                                      boolean pHasFocus) 
		{
			DataRowTreeCellRenderer result = (DataRowTreeCellRenderer)super.getTreeCellRendererComponent(pTree, pValue, pSelected, pExpanded, pLeaf, pRow, pHasFocus);
			
			if (nodeFormatter != null)
			{
				try
				{
					DataPageNode node = (DataPageNode)pValue;
					if (node.parent != null)
					{
						IDataPage dataPage = node.parent.dataPage;
						IDataBook dataBook = dataPage.getDataBook();
						IDataRow dataRow = dataPage.getDataRow(node.rowIndex);
						Icon icon = nodeFormatter.getNodeImage(dataBook, dataPage, dataRow, 
								dataBook.getRowDefinition().getColumnView(ITree.class).getColumnName(0), node.rowIndex, pExpanded, pLeaf);
						
						if (icon != null)
						{
							result.setIcon(icon);   
						}
					}
				}
				catch (Exception e)
				{
					// Do Nothing
				}
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Dimension getPreferredSize()
		{
			Dimension result = super.getPreferredSize();
			
			if (result.height < 20)
			{
				result.height = 20;
			}
			return result;
		}
		
	}	// DataRowTreeCellRenderer

	/**
	 * The <code>DataRowTreeCellRenderer</code> handles editing.
	 * 
	 * @author Martin Handsteiner
	 */
	public class DataRowTreeCellEditor extends DefaultTreeCellEditor
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * Constructs a <code>DefaultTreeCellEditor</code>
	     * object for a JTree using the specified renderer and
	     * a default editor. (Use this constructor for normal editing.)
	     *
	     * @param pTree      a <code>JTree</code> object
	     * @param pRenderer  a <code>DefaultTreeCellRenderer</code> object
	     */
	    public DataRowTreeCellEditor(JTree pTree, DefaultTreeCellRenderer pRenderer) 
	    {
	    	this(pTree, pRenderer, null);
	    }

	    /**
	     * Constructs a <code>DefaultTreeCellEditor</code>
	     * object for a <code>JTree</code> using the
	     * specified renderer and the specified editor. (Use this constructor
	     * for specialized editing.)
	     *
	     * @param pTree      a <code>JTree</code> object
	     * @param pRenderer  a <code>DefaultTreeCellRenderer</code> object
	     * @param pEditor    a <code>TreeCellEditor</code> object
	     */
	    public DataRowTreeCellEditor(JTree pTree, DefaultTreeCellRenderer pRenderer, TreeCellEditor pEditor) 
	    {
	    	super(pTree, pRenderer, pEditor);
	    }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected void determineOffset(JTree pTree, Object pValue, boolean pSelected, boolean pExpanded, boolean pLeaf, int pRow) 
	    {
	    	if (renderer != null) 
	    	{
				DataRowTreeCellRenderer result = (DataRowTreeCellRenderer)renderer.getTreeCellRendererComponent(pTree, pValue, pSelected, pExpanded, pLeaf, pRow, true);

				if (nodeFormatter != null)
				{
					try
					{
						DataPageNode node = (DataPageNode)pValue;
						if (node.parent != null)
						{
							IDataPage dataPage = node.parent.dataPage;
							IDataBook dataBook = dataPage.getDataBook();
							IDataRow dataRow = dataPage.getDataRow(node.rowIndex);
							Icon icon = nodeFormatter.getNodeImage(dataBook, dataPage, dataRow, 
									                               dataBook.getRowDefinition().getColumnView(ITree.class).getColumnName(0), 
									                               node.rowIndex, pExpanded, pLeaf);
							
							if (icon != null)
							{
								result.setIcon(icon);   
							}
						}
					}
					catch (Exception e)
					{
						// Do Nothing
					}
				}
				editingIcon = result.getIcon();
				
			    if (editingIcon != null)
			    {
					offset = renderer.getIconTextGap() + editingIcon.getIconWidth();
			    }
				else
				{
					offset = renderer.getIconTextGap();
				}
	    	}
	    	else 
	    	{
	    		editingIcon = null;
	    		offset = 0;
	    	}
	    }
	    
	}	// DataRowTreeCellEditor

}	// JVxTree
