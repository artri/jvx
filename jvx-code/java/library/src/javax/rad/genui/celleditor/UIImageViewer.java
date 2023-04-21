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
 * 09.10.2009 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.celleditor;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.ui.celleditor.IImageViewer;

/**
 * Platform and technology independent image viewer.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIImageViewer extends UICellEditor<IImageViewer> 
                           implements IImageViewer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIImageViewer</code>.
     *
     * @see IImageViewer
     */
	public UIImageViewer()
	{
		super(UIFactoryManager.getFactory().createImageViewer());
	}

    /**
     * Creates a new instance of <code>UIImageViewer</code> with the given
     * image viewer.
     *
     * @param pViewer the image viewer
     * @see IImageViewer
     */
	protected UIImageViewer(IImageViewer pViewer)
	{
		super(pViewer);
	}

	/**
     * Creates a new instance of <code>UIImageViewer</code>.
     *
     * @param pDefaultImage the default image.
     * @see IImageViewer
     */
	public UIImageViewer(String pDefaultImage)
	{
		super(UIFactoryManager.getFactory().createImageViewer());
		
		setDefaultImageName(pDefaultImage);
	}

    /**
     * Creates a new instance of <code>UIImageViewer</code>.
     *
     * @param pHorizontalAlignment the horizontal alignment.
     * @param pVerticalAlignment the vertical alignment.
     * @see IImageViewer
     */
	public UIImageViewer(int pHorizontalAlignment, int pVerticalAlignment)
	{
		super(UIFactoryManager.getFactory().createImageViewer());
		
		setHorizontalAlignment(pHorizontalAlignment);
		setVerticalAlignment(pVerticalAlignment);
	}

    /**
     * Creates a new instance of <code>UIImageViewer</code>.
     *
     * @param pDefaultImage the default image.
     * @param pHorizontalAlignment the horizontal alignment.
     * @param pVerticalAlignment the vertical alignment.
     * @see IImageViewer
     */
	public UIImageViewer(String pDefaultImage, int pHorizontalAlignment, int pVerticalAlignment)
	{
		super(UIFactoryManager.getFactory().createImageViewer());
		
		setDefaultImageName(pDefaultImage);
		setHorizontalAlignment(pHorizontalAlignment);
		setVerticalAlignment(pVerticalAlignment);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
	public boolean isPreserveAspectRatio()
	{
		return getUIResource().isPreserveAspectRatio();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreserveAspectRatio(boolean pPreserveAspectRatio)
	{
		getUIResource().setPreserveAspectRatio(pPreserveAspectRatio);
	}
	
}	// UIImageViewer
