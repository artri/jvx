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
 * 28.03.2010 - [JR] - #47: add/remove/get default choice cell editor
 * 08.01.2011 - [JR] - don't throw an IllegalArgumentException in addDefaultChoiceCellEditor (return boolean instead)
 *                   - #242: addDefaultChoiceCellEditor: return boolean instead of throw an Exception
 * 24.10.2012 - [JR] - #604: added constructor
 * 10.12.2012 - [JR] - #613: getDefaultChoiceCellEditor ignores value order
 * 02.04.2014 - [RZ] - #993: moved management of default choice cell editor to UICellEditor
 */
package javax.rad.genui.celleditor;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.celleditor.IChoiceCellEditor;

/**
 * Platform and technology independent choice editor.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIChoiceCellEditor extends UICellEditor<IChoiceCellEditor> 
                                implements IChoiceCellEditor
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIChoiceCellEditor</code>.
     *
     * @see IChoiceCellEditor
     */
	public UIChoiceCellEditor()
	{
		super(UIFactoryManager.getFactory().createChoiceCellEditor());
	}

    /**
     * Creates a new instance of <code>UIChoiceCellEditor</code> with the given
     * choice cell editor.
     * 
     * @param pEditor the choice cell editor
     * @see IChoiceCellEditor
     */
	protected UIChoiceCellEditor(IChoiceCellEditor pEditor)
	{
		super(pEditor);
	}
	
    /**
     * Creates a new instance of <code>UIChoiceCellEditor</code> with the given allowed values and image names.
     *
     * @param pAllowedValues the allowed values.
     * @param pImageNames the image names.
     * @see IChoiceCellEditor
     */
	public UIChoiceCellEditor(Object[] pAllowedValues, String[] pImageNames)
	{
		super(UIFactoryManager.getFactory().createChoiceCellEditor());
		
		getUIResource().setAllowedValues(pAllowedValues);
		getUIResource().setImageNames(pImageNames);
	}

    /**
     * Creates a new instance of <code>UIChoiceCellEditor</code> with the given allowed values and image names.
     *
     * @param pAllowedValues the allowed values.
     * @param pImageNames the image names.
     * @param pDefaultImage the default image name.
     * @see IChoiceCellEditor
     */
	public UIChoiceCellEditor(Object[] pAllowedValues, String[] pImageNames, String pDefaultImage)
	{
		super(UIFactoryManager.getFactory().createChoiceCellEditor());
		
		getUIResource().setAllowedValues(pAllowedValues);
		getUIResource().setImageNames(pImageNames);
		getUIResource().setDefaultImageName(pDefaultImage);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object[] getAllowedValues()
    {
    	return getUIResource().getAllowedValues();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setAllowedValues(Object[] pAllowedValues)
    {
		getUIResource().setAllowedValues(pAllowedValues);
    }

	/**
	 * {@inheritDoc}
	 */
	public String[] getImageNames()
    {
    	return getUIResource().getImageNames();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setImageNames(String[] pImageNames)
    {
		getUIResource().setImageNames(pImageNames);
    }

	/**
	 * {@inheritDoc}
	 */
	public String getDefaultImageName()
	{
		return getUIResource().getDefaultImageName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDefaultImageName(String pDefaultImage)
	{
		getUIResource().setDefaultImageName(pDefaultImage);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getCellRendererComponent(Object pParentComponent, 
			                               IDataPage pDataPage, 
			                               int pRowNumber, 
			                               IDataRow pDataRow, 
			                               String pColumnName, 
			                               boolean pIsSelected, 
			                               boolean pHasFocus)
	{
		return getUIResource().getCellRendererComponent(pParentComponent, pDataPage, pRowNumber, pDataRow, pColumnName, pIsSelected, pHasFocus);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Gets a choice cell editor from the defaults list, if an editor is available for specific values.
	 * 
	 * @param pValues a list of values for which a choice cell editor should be found. All values must
	 *                match to the allowed values of a registered editor.
	 * @return a choice cell editor for <code>pValue</code> or <code>null</code> if no matching editor
	 *         was found.
	 */
	public static IChoiceCellEditor getDefaultChoiceCellEditor(Object[] pValues)
    {
    	ICellEditor editor = getDefaultCellEditor(pValues);
    	if (editor instanceof IChoiceCellEditor)
    	{
    		return (IChoiceCellEditor) editor;
    	}
    	
    	return null;
    }
	
	/**
	 * Gets all currently available default choice cell editors as array.
	 * 
	 * @return an array with default choice cell editors always <code>!= null</code>
	 */
	public static IChoiceCellEditor[] getDefaultChoiceCellEditors()
	{
		ICellEditor[] editors = getDefaultCellEditors(IChoiceCellEditor.class);
		IChoiceCellEditor[] cces = new IChoiceCellEditor[editors.length];
		
		System.arraycopy(editors, 0, cces, 0, editors.length);
		
		return cces;
	}
   
	/**
	 * Adds a choice cell editor to the list of default choice cell editors. The allowed values of the
	 * cell editor will be used to check if there is already a choice cell editor present and
	 * if yes the already existing choice cell editor will be replaced.
	 * 
	 * @param pEditor the choice cell editor
	 */
    public static void addDefaultChoiceCellEditor(IChoiceCellEditor pEditor)
    {
    	addDefaultCellEditor(pEditor.getAllowedValues(), pEditor);
    }	
    
    /**
     * Removes a choice cell editor from the list of default choice cell editors.
     * 
     * @param pEditor the choice cell editor
     */
    public static void removeDefaultChoiceCellEditor(IChoiceCellEditor pEditor)
    {
    	removeDefaultCellEditor(pEditor.getAllowedValues());
    }

    /**
     * Removes all default choice cell editors.
     */
    public static void removeAllDefaultChoiceCellEditors()
    {
		removeAllDefaultCellEditors(IChoiceCellEditor.class);
    }
	
}	// UIChoiceCellEditor
