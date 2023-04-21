/*
 * Copyright 2013 SIB Visions GmbH
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
 * 29.09.2013 - [HM] - creation
 */
package javax.rad.genui.celleditor;

import javax.rad.ui.celleditor.IComboCellEditor;

/**
 * Platform and technology independent combo cell editor.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * 
 * @param <CE> an instance of IComboCellEditor.
 */
public class UIComboCellEditor<CE extends IComboCellEditor> extends UIInplaceCellEditor<CE> 
                                                            implements IComboCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Constructs a new <code>UIComboCellEditor</code>.
	 * 
	 * @param pCEResource the CellEditor resource.
	 * @see IComboCellEditor  
	 */
	protected UIComboCellEditor(CE pCEResource)
	{
		super(pCEResource);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoOpenPopup()
	{
		return getUIResource().isAutoOpenPopup();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAutoOpenPopup(boolean pAutoOpenPopup)
	{
		getUIResource().setAutoOpenPopup(pAutoOpenPopup);
	}

}	// UIComboBoxCellEditor
