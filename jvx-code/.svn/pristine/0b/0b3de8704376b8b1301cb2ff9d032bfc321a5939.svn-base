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
 * 06.03.2014 - [LT] - erstellt
 */
package javax.rad.genui.layout;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UILayout;
import javax.rad.genui.UIResource;
import javax.rad.ui.IFactory;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IGridLayout;

/**
 * Platform and technology independent grid oriented layout. It is designed for
 * use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Thomas Lehner
 */
public class UIGridLayout extends UILayout<IGridLayout, IGridLayout.IGridConstraints> 
						  implements IGridLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>UIGridLayout</code> with the given
	 * layout.
	 * 
	 * @param pLayout the layout
	 * @see IGridLayout
	 */
	protected UIGridLayout(IGridLayout pLayout)
	{
		super(pLayout);
	}

	/**
	 * Creates a new instance of <code>UIGridLayout</code>.
	 * 
	 * @see IGridLayout
	 */
	public UIGridLayout()
	{
		this(10, 10);
	}

	/**
	 * Creates a new instance of <code>UIGridLayout</code>.
	 * 
	 * @param pColumns the number of columns
	 * @param pRows the number of rows
	 * @see IGridLayout
	 */
	public UIGridLayout(int pColumns, int pRows)
	{
		this(((IFactory)UIFactoryManager.getFactory()).createGridLayout(pColumns, pRows));
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void setColumns(int pColumns)
	{
		uiResource.setColumns(pColumns);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getColumns()
	{
		return uiResource.getColumns();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRows(int pRows)
	{
		uiResource.setRows(pRows);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRows()
	{
		return uiResource.getRows();
	}

	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows)
	{
		return uiResource.getConstraints(pColumns, pRows);
	}

	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight)
	{
		return uiResource.getConstraints(pColumns, pRows, pWidth, pHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public IGridConstraints getConstraints(int pColumns, int pRows, int pWidth, int pHeight, IInsets pInsets)
	{
		if (pInsets instanceof UIResource)
		{
			pInsets = ((UIResource<IInsets>)pInsets).getUIResource();
		}
		return uiResource.getConstraints(pColumns, pRows, pWidth, pHeight, pInsets);
	}

}	// UIGridLayout
