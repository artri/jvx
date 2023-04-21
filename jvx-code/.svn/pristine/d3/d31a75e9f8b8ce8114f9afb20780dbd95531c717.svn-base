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
 * 01.12.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.layout;

import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UILayout;
import javax.rad.ui.layout.IFlowLayout;

/**
 * Platform and technology independent FlowLayout.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UIFlowLayout extends UILayout<IFlowLayout, Object> 
						  implements IFlowLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIFlowLayout</code>.
     *
     * @see IFlowLayout
     */
	public UIFlowLayout()
	{
		this(UIFactoryManager.getFactory().createFlowLayout());
	}

    /**
     * Creates a new instance of <code>UIFlowLayout</code> with the given
     * layout.
     *
     * @param pLayout the layout
     * @see IFlowLayout
     */
	protected UIFlowLayout(IFlowLayout pLayout)
	{
		super(pLayout);
	}
	
    /**
     * Creates a new instance of <code>UIFlowLayout</code>.
     * 
     * @param pOrientation the orientation.
     * @see IFlowLayout
     */
	public UIFlowLayout(int pOrientation)
	{
		this();
		
		uiResource.setOrientation(pOrientation);
	}
	
    /**
     * Creates a new instance of <code>UIFlowLayout</code> with a given orientation and default
     * gaps.
     * 
     * @param pOrientation the orientation.
     * @param pHorizontalGap the horizontal gap.
     * @param pVerticalGap the vertical gap.
     * @see IFlowLayout
     */
	public UIFlowLayout(int pOrientation, int pHorizontalGap, int pVerticalGap)
	{
		this();
		
		uiResource.setOrientation(pOrientation);
		uiResource.setHorizontalGap(pHorizontalGap);
		uiResource.setVerticalGap(pVerticalGap);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
	public int getHorizontalAlignment()
	{
		return uiResource.getHorizontalAlignment();
	}
	
    /**
     * {@inheritDoc}
     */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		uiResource.setHorizontalAlignment(pHorizontalAlignment);
	}
	
    /**
     * {@inheritDoc}
     */
	public int getVerticalAlignment()
	{
		return uiResource.getVerticalAlignment();
	}

    /**
     * {@inheritDoc}
     */
    public void setVerticalAlignment(int pVerticalAlignment)
    {
    	uiResource.setVerticalAlignment(pVerticalAlignment);
    }
    
    /**
	 * {@inheritDoc}
	 */
	public int getOrientation()
	{
		return uiResource.getOrientation();
	}

    /**
	 * {@inheritDoc}
	 */
	public void setOrientation(int pOrientation)
	{
		uiResource.setOrientation(pOrientation);
	}

    /**
	 * {@inheritDoc}
	 */
	public int getComponentAlignment()
	{
		return uiResource.getComponentAlignment();
	}

    /**
	 * {@inheritDoc}
	 */
	public void setComponentAlignment(int pComponentAlignment)
	{
		uiResource.setComponentAlignment(pComponentAlignment);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoWrap()
	{
		return uiResource.isAutoWrap();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAutoWrap(boolean pAutoWrap)
	{
		uiResource.setAutoWrap(pAutoWrap);
	}

}	// UIFlowLayout
