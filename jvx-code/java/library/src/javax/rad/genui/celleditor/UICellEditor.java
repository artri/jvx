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
 * 17.11.2008 - [HM] - creation
 * 02.04.2014 - [RZ] - #993 - added management of default cell editors based on allowed values
 * 04.09.2014 - [RZ] - Fixed handling of default cell editors (race condition and other problems)
 */
package javax.rad.genui.celleditor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.rad.genui.AbstractUIFactoryResource;
import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.ui.IResource;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.IComboCellEditor;
import javax.rad.ui.celleditor.IInplaceCellEditor;
import javax.rad.ui.celleditor.IStyledCellEditor;
import javax.rad.ui.control.IEditor;

import com.sibvisions.util.ArrayUtil;

/**
 * Platform and technology independent editor. It is designed for use with AWT,
 * Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * 
 * @param <CE> an instance of ICellEditor.
 */
public class UICellEditor<CE extends IStyledCellEditor> extends AbstractUIFactoryResource<CE>
		                                                implements IResource, 
		                                                           IStyledCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** a map with all created celleditors. */
    private static Map<ICellEditor, Object> mpCellEditors = new WeakHashMap<ICellEditor, Object>();
    
	/** the predefined cell editors with their allowed values as pairs. */
	private static Map<AllowedValuesWrapper, ICellEditor> mpValuesEditors = new HashMap<AllowedValuesWrapper, ICellEditor>();
	
	/** The default preferred editor mode. */
	private static int defaultPreferredEditorMode = IInplaceCellEditor.DOUBLE_CLICK;
	
    /** The default preferred editor mode. */
    private static boolean defaultAutoOpenPopup = true;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new <code>UICellEditor</code>.
	 * 
	 * @param pCEResource the CellEditor resource.
	 * @see ICellEditor
	 */
	protected UICellEditor(CE pCEResource)
	{
		super(pCEResource);
		
		synchronized (mpCellEditors)
		{
		    mpCellEditors.put(this, null);
		}
		
        if (this instanceof IInplaceCellEditor)
        {
            ((IInplaceCellEditor)this).setPreferredEditorMode(defaultPreferredEditorMode);
        }
        else if (this instanceof IComboCellEditor)
        {
            ((IComboCellEditor)this).setAutoOpenPopup(defaultAutoOpenPopup);
        }
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public final Object getResource()
	{
		return getUIResource();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
	{
		return getUIResource().getHorizontalAlignment();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		getUIResource().setHorizontalAlignment(pHorizontalAlignment);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
	{
		return getUIResource().getVerticalAlignment();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		getUIResource().setVerticalAlignment(pVerticalAlignment);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener,
                                        			  IDataRow pDataRow,
                                        			  String pColumnName)
	{
		return getUIResource().createCellEditorHandler(pCellEditorListener, pDataRow, pColumnName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isDirectCellEditor()
	{
		return getUIResource().isDirectCellEditor();
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void setStyle(Style pStyle)
    {
    	getUIResource().setStyle(pStyle);
    }
    
    /**
     * {@inheritDoc}
     */
    public Style getStyle()
    {
        return getUIResource().getStyle();
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
    protected Object createCacheKey()
    {
	    //we cache the cell editors by factory because UIs should be independent of each other
        return UIFactoryManager.getFactory();        
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets the default <code>ICellEditor</code> for the given class. This
	 * function should always return an editor. It should look for best matching
	 * editor with Class.isAssignableFrom.
	 * 
	 * @param pClass the class type to be edited.
	 * @return the <code>ICellEditor</code>
	 * @see ICellEditor
	 */
	public static ICellEditor getDefaultCellEditor(Class pClass)
	{
		return UIFactoryManager.getFactory().getDefaultCellEditor(pClass);
	}
	
	/**
	 * Sets the default <code>ICellEditor</code> for the given class. This
	 * function should always return an editor. It should look for best matching
	 * editor with Class.isAssignableFrom. If the given ICellEditor is null, it
	 * is removed as editor for the given class.
	 * 
	 * @param pClass the class type to be edited.
	 * @param pCellEditor the <code>ICellEditor</code>
	 * @see ICellEditor
	 */
	public static void setDefaultCellEditor(Class pClass, ICellEditor pCellEditor)
	{
		UIFactoryManager.getFactory().setDefaultCellEditor(pClass, pCellEditor);
	}
	
    /**
     * Gets the default <code>ICellEditor</code> for <code>java.lang.String.class</code>.
     * 
     * @return the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static ICellEditor getDefaultTextCellEditor()
    {
        return UIFactoryManager.getFactory().getDefaultCellEditor(String.class);
    }
    
    /**
     * Sets the default <code>ICellEditor</code> for <code>java.lang.String.class</code>.
     * 
     * @param pCellEditor the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static void setDefaultTextCellEditor(ICellEditor pCellEditor)
    {
        UIFactoryManager.getFactory().setDefaultCellEditor(String.class, pCellEditor);
    }
    
    /**
     * Gets the default <code>ICellEditor</code> for <code>java.lang.Number.class</code>.
     * 
     * @return the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static ICellEditor getDefaultNumberCellEditor()
    {
        return UIFactoryManager.getFactory().getDefaultCellEditor(Number.class);
    }
    
    /**
     * Sets the default <code>ICellEditor</code> for <code>java.lang.Number.class</code>.
     * 
     * @param pCellEditor the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static void setDefaultNumberCellEditor(ICellEditor pCellEditor)
    {
        UIFactoryManager.getFactory().setDefaultCellEditor(Number.class, pCellEditor);
    }
    
    /**
     * Gets the default <code>ICellEditor</code> for <code>java.util.Date.class</code>.
     * 
     * @return the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static ICellEditor getDefaultDateCellEditor()
    {
        return UIFactoryManager.getFactory().getDefaultCellEditor(Date.class);
    }
    
    /**
     * Sets the default <code>ICellEditor</code> for <code>java.util.Date.class</code>.
     * 
     * @param pCellEditor the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static void setDefaultDateCellEditor(ICellEditor pCellEditor)
    {
        UIFactoryManager.getFactory().setDefaultCellEditor(Date.class, pCellEditor);
    }
    
	/**
	 * Gets the cell editor of the given column for data row.
	 * 
	 * @param <C> is instance of ICellEditor.
	 * @param pDataRow the data row.
	 * @param pColumnName the column name.
	 * @return the cell editor of the given column for data row.
	 * @throws ModelException if it fails.
	 */
	public static <C extends ICellEditor> C getCellEditor(IDataRow pDataRow, String pColumnName) throws ModelException
	{
		ICellEditor cellEditor = null;
		
		if (pDataRow != null && pColumnName != null)
		{
			IDataType dataType = pDataRow.getRowDefinition().getColumnDefinition(pColumnName).getDataType();
			
			cellEditor = dataType.getCellEditor();
			
			if (cellEditor == null)
			{
				cellEditor = UICellEditor.getDefaultCellEditor(dataType.getTypeClass());
			}
		}
		
		return (C)cellEditor;
	}
	
	/**
	 * Gets the cell editor of the given column for data row.
	 * 
	 * @param <C> is instance of ICellEditor.
	 * @param pDefinition the column definition.
	 * @return the cell editor of the given column for data row.
	 * @throws ModelException if it fails.
	 */
	public static <C extends ICellEditor> C getCellEditor(ColumnDefinition pDefinition) throws ModelException
	{
		ICellEditor cellEditor = null;
		
		if (pDefinition != null)
		{
			IDataType dataType = pDefinition.getDataType();
			
			cellEditor = dataType.getCellEditor();
			
			if (cellEditor == null)
			{
				cellEditor = UICellEditor.getDefaultCellEditor(dataType.getTypeClass());
			}
		}
		
		return (C)cellEditor;
	}
	
	/**
	 * Gets the cell editor of the editor.
	 * 
	 * @param <C> is instance of ICellEditor.
	 * @param pEditor the data row.
	 * @return the cell editor of the given column for data row.
	 * @throws ModelException if it fails.
	 */
	public static <C extends ICellEditor> C getCellEditor(IEditor pEditor) throws ModelException
	{
		ICellEditor cellEditor = pEditor.getCellEditor();

		if (cellEditor == null)
		{
		    cellEditor = getCellEditor(pEditor.getDataRow(), pEditor.getColumnName());
		}
		
		return (C)cellEditor;
	}
	
	/**
	 * Gets a cell editor from the defaults list, if an editor is available for
	 * specific values.
	 * 
	 * @param pValues a list of values for which a cell editor should be found.
	 *            All values must match to the allowed values of a registered
	 *            editor.
	 * @return a cell editor for <code>pValue</code> or <code>null</code> if no
	 *         matching editor was found.
	 */
	public static ICellEditor getDefaultCellEditor(Object[] pValues)
	{
		return mpValuesEditors.get(new AllowedValuesWrapper(pValues));
	}
	
	/**
	 * Gets all currently available default cell editors as array.
	 * 
	 * @return an array with default cell editors always <code>!= null</code>
	 */
	public static ICellEditor[] getDefaultCellEditors()
	{
		return getDefaultCellEditors(null);
	}
	
	/**
	 * Gets all currently available default cell editors of the given class.
	 * 
	 * @param pClass the class of editors to be returned
	 * @return an array with the default cell editors of the given class, always
	 *         <code>!= null</code>
	 */
	public static ICellEditor[] getDefaultCellEditors(Class pClass)
	{
		ArrayList<ICellEditor> matchingEditors = new ArrayList<ICellEditor>();
		
		for (ICellEditor editor : mpValuesEditors.values())
		{
			if (pClass == null || pClass.isInstance(editor))
			{
				matchingEditors.add(editor);
			}
		}
		
		return matchingEditors.toArray(new ICellEditor[matchingEditors.size()]);
	}
	
	/**
	 * Adds a cell editor to the list of default cell editors. The allowed
	 * values of the cell editor will be used to check if there is already a
	 * cell editor present and if yes the already existing cell editor will be
	 * replaced.
	 * 
	 * @param pAllowedValues the allowed values.
	 * @param pEditor the editor.
	 */
	public static void addDefaultCellEditor(Object[] pAllowedValues, ICellEditor pEditor)
	{
		mpValuesEditors.put(new AllowedValuesWrapper(pAllowedValues), pEditor);
	}
	
	/**
	 * Removes a cell editor from the list of default cell editors.
	 * 
	 * @param pAllowedValues the allowed values.
	 */
	public static void removeDefaultCellEditor(Object[] pAllowedValues)
	{
		mpValuesEditors.remove(new AllowedValuesWrapper(pAllowedValues));
	}
	
	/**
	 * Removes all default cell editors.
	 */
	public static void removeAllDefaultCellEditors()
	{
		removeAllDefaultCellEditors(null);
	}
	
	/**
	 * Removes all default cell editors which are of the given class.
	 * 
	 * @param pClass the class of editors to be removed
	 */
	public static void removeAllDefaultCellEditors(Class pClass)
	{
		if (pClass == null)
		{
			mpValuesEditors.clear();
			return;
		}
		
		Iterator<Map.Entry<AllowedValuesWrapper, ICellEditor>> iterator = mpValuesEditors.entrySet().iterator();
		
		while (iterator.hasNext())
		{
			ICellEditor editor = iterator.next().getValue();
			
			if (pClass.isInstance(editor))
			{
				iterator.remove();
			}
		}
	}
	
	/**
	 * Gets all instantiated cell editors.
	 * 
	 * @return all instantiated cell editors.
	 */
	public static Set<ICellEditor> getAllCellEditors()
	{
	    HashSet<ICellEditor> allCellEditors = new HashSet<ICellEditor>(Math.max(mpCellEditors.size() * 3 / 2, 16));
	
	    synchronized (mpCellEditors)
	    {
	        allCellEditors.addAll(mpCellEditors.keySet());
	    }

	    allCellEditors.add(getDefaultTextCellEditor());
	    allCellEditors.add(getDefaultNumberCellEditor());
	    allCellEditors.add(getDefaultDateCellEditor());
	    
	    return allCellEditors;
	}
	
	/**
     * Gets the default preferred Editor Mode.
     * 
     * Different Platforms are open to define own editor modes.
     * As this is meant to be extended, own modes should have constants &gt;= 100, to avoid unwanted interactions. 
     * If a platform does not support a editor mode, it should use the default.
     * 
     * @return the preferred Editor Mode.
     * @see IInplaceCellEditor#DOUBLE_CLICK
     * @see IInplaceCellEditor#SINGLE_CLICK
     */
    public static int getDefaultPreferredEditorMode() 
    {
        return defaultPreferredEditorMode;
    }

    /**
     * Sets the default preferred Editor Mode.
     * 
     * Different Platforms are open to define own editor modes.
     * As this is meant to be extended, own modes should have constants &gt;= 100, to avoid unwanted interactions. 
     * If a platform does not support a editor mode, it should use the default.
     * 
     * @param pDefaultPreferredEditorMode the preferred Editor Mode.
     * @see IInplaceCellEditor#DOUBLE_CLICK
     * @see IInplaceCellEditor#SINGLE_CLICK
     */
    public static void setDefaultPreferredEditorMode(int pDefaultPreferredEditorMode)
    {
        if (pDefaultPreferredEditorMode == IInplaceCellEditor.SINGLE_CLICK
                || pDefaultPreferredEditorMode == IInplaceCellEditor.DOUBLE_CLICK)
        {
            defaultPreferredEditorMode = pDefaultPreferredEditorMode;
        }
        else
        {
            throw new IllegalArgumentException("The default preferred editor mode has to be either SINGLE_CLICK or DOUBLE_CLICK!");
        }
        
        for (ICellEditor cellEditor : getAllCellEditors())
        {
            if (cellEditor instanceof IInplaceCellEditor)
            {
                ((IInplaceCellEditor)cellEditor).setPreferredEditorMode(defaultPreferredEditorMode);
            }
        }
    }

    /**
     * Gets true, if the popup is automatically opened.
     * On focus lost the popup should be closed again.
     * 
     * @return true, if the popup is automatically opened.
     */
    public boolean isDefaultAutoOpenPopup()
    {
        return defaultAutoOpenPopup;
    }

    /**
     * Gets true, if the popup is automatically opened.
     * On focus lost the popup should be closed again.
     * 
     * @param pDefaultAutoOpenPopup true, if the popup is automatically opened.
     */
    public void setDefaultAutoOpenPopup(boolean pDefaultAutoOpenPopup)
    {
        defaultAutoOpenPopup = pDefaultAutoOpenPopup;
        
        for (ICellEditor cellEditor : getAllCellEditors())
        {
            if (cellEditor instanceof IComboCellEditor)
            {
                ((IComboCellEditor)cellEditor).setAutoOpenPopup(defaultAutoOpenPopup);
            }
        }
    }

	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple wrapper for an {@link Object} array that provides a hashcode.
	 * 
	 * @author Robert Zenz
	 */
	private static final class AllowedValuesWrapper
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The (cached) hash code. */
		private int hashCode = 0;
		
		/** The allowed values. */
		private Object[] values;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>ValuesEditorPair</code>.
		 * 
		 * @param pValues the allowed values
		 */
		public AllowedValuesWrapper(Object[] pValues)
		{
			values = pValues;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode()
		{
			if (hashCode == 0)
			{
				hashCode = 1;
	
		        for (Object element : values)
		        {
		        	hashCode += element == null ? 0 : element.hashCode();
		        }
			}

	        return hashCode;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			
			if (obj == null)
			{
				return false;
			}
			
			if (getClass() != obj.getClass())
			{
				return false;
			}
			
			AllowedValuesWrapper other = (AllowedValuesWrapper) obj;
			
			if (!ArrayUtil.containsAll(values, other.values) || !ArrayUtil.containsAll(other.values, values))
			{
				return false;
			}
			
			return true;
		}
		
	}	// AllowedValuesWrapper 
	
}	// UICellEditor
