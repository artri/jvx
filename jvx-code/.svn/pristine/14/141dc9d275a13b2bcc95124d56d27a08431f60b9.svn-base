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
 * 19.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.celleditor;

import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;

import com.sibvisions.rad.ui.celleditor.AbstractImageViewer;
import com.sibvisions.rad.ui.web.impl.WebFactory;
import com.sibvisions.rad.ui.web.impl.WebImage;

/**
 * Web server implementation of {@link javax.rad.ui.celleditor.IImageViewer}.
 * 
 * @author Martin Handsteiner
 */
public class WebImageViewer extends AbstractImageViewer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>UIImageViewer</code>.
	 *
	 * @see javax.rad.ui.celleditor.IImageViewer
	 */
	public WebImageViewer()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
	{
		return new DefaultCellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
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
		return null;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the image URI for a specific image value.
	 * 
	 * @param pValue the image name or raw data
	 * @return the image URI or the default image if the image/type is
	 *         undefined/unsupported
	 */
	public String getURI(Object pValue)
	{
		if (pValue instanceof String)
		{
			return WebImage.getImageURI((String)pValue);
		}
		else if (pValue instanceof byte[])
		{
			return WebImage.getImageURI(null, (byte[])pValue);
		}
		else
		{
			return WebImage.getImageURI(getDefaultImageName());
		}
	}
	
	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName()
	{
		return WebFactory.getClassName(this);
	}	
	
}	// WebImageViewer
