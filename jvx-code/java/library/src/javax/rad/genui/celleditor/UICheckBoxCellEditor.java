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
 * 27.03.2014 - [HM] - creation
 * 02.04.2014 - [RZ] - #993 - add/remove/get default checkbox editor
 */
package javax.rad.genui.celleditor;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;

/**
 * Platform and technology independent check box editor.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UICheckBoxCellEditor extends UICellEditor<ICheckBoxCellEditor>
                                  implements ICheckBoxCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UICheckBoxCellEditor</code>.
     *
     * @see ICheckBoxCellEditor
     */
	public UICheckBoxCellEditor()
	{
		super(UIFactoryManager.getFactory().createCheckBoxCellEditor());
	}

    /**
     * Creates a new instance of <code>UICheckBoxCellEditor</code> with the given
     * choice cell editor.
     * 
     * @param pEditor the choice cell editor
     * @see ICheckBoxCellEditor
     */
	protected UICheckBoxCellEditor(ICheckBoxCellEditor pEditor)
	{
		super(pEditor);
	}
	
	/**
	 * Creates a new instance of <code>UICheckBoxCellEditor</code> with the given selected and deselected values.
	 * 
	 * @param pSelectedValue the selected value.
	 * @param pDeselectedValue the deselected value.
	 */
	public UICheckBoxCellEditor(Object pSelectedValue, Object pDeselectedValue)
	{
		super(UIFactoryManager.getFactory().createCheckBoxCellEditor());
		
		getUIResource().setSelectedValue(pSelectedValue);
		getUIResource().setDeselectedValue(pDeselectedValue);
	}

	/**
	 * Creates a new instance of <code>UICheckBoxCellEditor</code> with the given selected and deselected values.
	 * 
	 * @param pSelectedValue the selected value.
	 * @param pDeselectedValue the deselected value.
	 * @param pText the text.
	 */
	public UICheckBoxCellEditor(Object pSelectedValue, Object pDeselectedValue, String pText)
	{
		super(UIFactoryManager.getFactory().createCheckBoxCellEditor());
		
		getUIResource().setSelectedValue(pSelectedValue);
		getUIResource().setDeselectedValue(pDeselectedValue);
		getUIResource().setText(pText);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object getSelectedValue() 
	{
		return getUIResource().getSelectedValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelectedValue(Object pSelectedValue)
	{
		getUIResource().setSelectedValue(pSelectedValue);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getDeselectedValue() 
	{
		return getUIResource().getDeselectedValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDeselectedValue(Object pDeselectedValue)
	{
		getUIResource().setDeselectedValue(pDeselectedValue);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getText()
	{
		return getUIResource().getText();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setText(String pText)
	{
		getUIResource().setText(pText);
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
	 * Gets a checkbox cell editor from the defaults list, if an editor is available for specific values.
	 * 
	 * @param pValues a list of values for which a checkbox cell editor should be found. All values must
	 *                match to the allowed values of a registered editor.
	 * @return a checkbox cell editor for <code>pValue</code> or <code>null</code> if no matching editor
	 *         was found.
	 */
	public static ICheckBoxCellEditor getDefaultCheckBoxCellEditor(Object[] pValues)
    {
    	ICellEditor editor = getDefaultCellEditor(pValues);
    	if (editor instanceof ICheckBoxCellEditor)
    	{
    		return (ICheckBoxCellEditor) editor;
    	}
    	
    	return null;
    }
	
	/**
	 * Gets all currently available default checkbox cell editors as array.
	 * 
	 * @return an array with default checkbox cell editors always <code>!= null</code>
	 */
	public static ICheckBoxCellEditor[] getDefaultCheckBoxCellEditors()
	{
		ICellEditor[] editors = getDefaultCellEditors(ICheckBoxCellEditor.class);
		ICheckBoxCellEditor[] cbces = new ICheckBoxCellEditor[editors.length];
		
		System.arraycopy(editors, 0, cbces, 0, editors.length);
		
		return cbces;
	}
   
	/**
	 * Adds a checkbox cell editor to the list of default checkbox cell editors. The allowed values of the
	 * cell editor will be used to checkbox if there is already a checkbox cell editor present and
	 * if yes the already existing checkbox cell editor will be replaced.
	 * 
	 * @param pEditor the checkbox cell editor
	 */
    public static void addDefaultCheckBoxCellEditor(ICheckBoxCellEditor pEditor)
    {
    	addDefaultCellEditor(new Object[] {pEditor.getSelectedValue(), pEditor.getDeselectedValue()}, pEditor);
    }	
    
    /**
     * Removes a checkbox cell editor from the list of default checkbox cell editors.
     * 
     * @param pEditor the checkbox cell editor
     */
    public static void removeDefaultCheckBoxCellEditor(ICheckBoxCellEditor pEditor)
    {
    	removeDefaultCellEditor(new Object[] {pEditor.getSelectedValue(), pEditor.getDeselectedValue()});
    }

    /**
     * Removes all default checkbox cell editors.
     */
    public static void removeAllDefaultCheckBoxCellEditors()
    {
		removeAllDefaultCellEditors(ICheckBoxCellEditor.class);
    }
    
}	// UICheckBoxCellEditor
