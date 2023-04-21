/*
 * Copyright 2015 SIB Visions GmbH
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
 * 11.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.celleditor;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.celleditor.IImageViewer;

/**
 * The {@link AbstractImageViewer} is an {@link IImageViewer} implementation,
 * which provides a base implementation.
 * 
 * @author Robert Zenz
 * @param <C> the type of the content.
 */
public abstract class AbstractImageViewer<C> extends AbstractStyledCellEditor 
                                             implements IImageViewer<C>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The default image name. */
	protected transient String sDefaultImageName = null;
	
	/** whether to preserve aspect ration. */
	private transient boolean bPreserveAspectRatio = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractImageViewer}.
	 */
	protected AbstractImageViewer()
	{
        setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getDefaultImageName()
	{
		return sDefaultImageName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDefaultImageName(String pDefaultImageName)
	{
		sDefaultImageName = pDefaultImageName;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPreserveAspectRatio()
	{
		return bPreserveAspectRatio;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreserveAspectRatio(boolean pPreserveAspectRatio)
	{
		bPreserveAspectRatio = pPreserveAspectRatio;
	}
	
}	// AbstractImageViewer
