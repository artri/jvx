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
 * 01.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.impl.layout;

import java.awt.Component;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import javax.rad.ui.IComponent;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IFormLayout;

import com.sibvisions.rad.ui.awt.impl.AwtInsets;
import com.sibvisions.rad.ui.awt.impl.AwtResource;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>AwtFormLayout</code> is the {@link IFormLayout} implementation for AWT.
 * 
 * @author Martin Handsteiner
 * @see	JVxFormLayout
 */
public class AwtFormLayout extends AwtResource<JVxFormLayout> 
						   implements IFormLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Stores uniquie the anchor wrapper. */
	private WeakHashMap<JVxFormLayout.Anchor, WeakReference<AwtAnchor>> anchors = new WeakHashMap<JVxFormLayout.Anchor, WeakReference<AwtAnchor>>();

	/** Stores uniquie the constraint wrapper. */
	private WeakHashMap<JVxFormLayout.Constraint, WeakReference<AwtConstraints>> constraints = new WeakHashMap<JVxFormLayout.Constraint, WeakReference<AwtConstraints>>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>AwtSimpleFormLayout</code> based on an
	 * <code>IFormLayout</code> implementation.
	 * 
	 * @see IFormLayout
	 */
	public AwtFormLayout()
	{
		super(new JVxFormLayout());
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
	public int getHorizontalAlignment()
	{
		return SwingFactory.getHorizontalAlignment(resource.getHorizontalAlignment());
	}
	
    /**
     * {@inheritDoc}
     */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		resource.setHorizontalAlignment(SwingFactory.getHorizontalSwingAlignment(pHorizontalAlignment));
	}
	
    /**
     * {@inheritDoc}
     */
	public int getVerticalAlignment()
	{
		return SwingFactory.getVerticalAlignment(resource.getVerticalAlignment());
	}

    /**
     * {@inheritDoc}
     */
    public void setVerticalAlignment(int pVerticalAlignment)
    {
    	resource.setVerticalAlignment(SwingFactory.getVerticalSwingAlignment(pVerticalAlignment));
    }
    
	/**
	 * {@inheritDoc}
	 */
    public int getHorizontalGap()
    {
    	return resource.getHorizontalGap();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setHorizontalGap(int pHgap)
    {
    	resource.setHorizontalGap(pHgap);
    }

	/**
	 * {@inheritDoc}
	 */
    public int getVerticalGap()
    {
    	return resource.getVerticalGap();
    }

	/**
	 * {@inheritDoc}
	 */
    public void setVerticalGap(int pVgap)
    {
    	resource.setVerticalGap(pVgap);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public IInsets getMargins()
    {
    	return new AwtInsets(resource.getMargins());
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	if (pMargins == null)
    	{
    		resource.setMargins(null);
    	}
    	else
    	{
        	resource.setMargins((Insets)pMargins.getResource());
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public int getNewlineCount()
    {
    	return resource.getNewlineCount();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setNewlineCount(int pNewlineCount)
    {
    	resource.setNewlineCount(pNewlineCount);
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor getLeftAnchor()
    {
		return getAnchor(resource.getLeftAnchor());
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor getRightAnchor()
    {
		return getAnchor(resource.getRightAnchor());
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor getTopAnchor()
    {
		return getAnchor(resource.getTopAnchor());
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor getBottomAnchor()
    {
		return getAnchor(resource.getBottomAnchor());
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getLeftMarginAnchor()
    {
		return getAnchor(resource.getLeftMarginAnchor());
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getRightMarginAnchor()
    {
		return getAnchor(resource.getRightMarginAnchor());
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor getTopMarginAnchor()
    {
		return getAnchor(resource.getTopMarginAnchor());
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor getBottomMarginAnchor()
    {
		return getAnchor(resource.getBottomMarginAnchor());
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor[] getHorizontalAnchors()
    {
		return getAnchors(resource.getHorizontalAnchors());
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor[] getVerticalAnchors()
    {
		return getAnchors(resource.getVerticalAnchors());
    }
	
	/**
	 * {@inheritDoc}
	 */
    public IConstraints getConstraints(IComponent pComponent)
    {
    	return getConstraints(resource.getConstraint((Component)pComponent.getResource()));
    }

	/**
	 * {@inheritDoc}
	 */
    public void setConstraints(IComponent pComponent, IConstraints pConstraints)
    {
   		resource.setConstraint((Component)pComponent.getResource(), getConstraints(pConstraints));
    }
    
	/**
	 * {@inheritDoc}
	 */
	public IAnchor createAnchor(IAnchor pRelatedAnchor)
    {
    	return getAnchor(new JVxFormLayout.Anchor(getAnchor(pRelatedAnchor)));
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor createAnchor(IAnchor pRelatedAnchor, int pPosition)
    {
    	return getAnchor(new JVxFormLayout.Anchor(getAnchor(pRelatedAnchor), pPosition));
    }
	
	/**
	 * {@inheritDoc}
	 */
    public IConstraints getConstraints(int pColumn, int pRow)
    {
    	return getConstraints(resource.createConstraint(pColumn, pRow));
    }
    
	/**
	 * {@inheritDoc}
	 */
    public IConstraints getConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	return getConstraints(resource.createConstraint(pBeginColumn, pBeginRow, pEndColumn, pEndRow));
    }
    
	/**
	 * {@inheritDoc}
	 */
    public IConstraints getConstraints(IAnchor pTopAnchor, IAnchor pLeftAnchor, IAnchor pBottomAnchor, IAnchor pRightAnchor)
    {
    	return getConstraints(new JVxFormLayout.Constraint(getAnchor(pTopAnchor), getAnchor(pLeftAnchor), getAnchor(pBottomAnchor), getAnchor(pRightAnchor)));
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the JVxFormLayout.Anchor from an IAnchor.
     * @param pAnchor the IAnchor.
     * @return the JVxFormLayout.Anchor.
     */
    private JVxFormLayout.Anchor getAnchor(IAnchor pAnchor)
    {
    	if (pAnchor == null)
    	{
    		return null;
    	}
    	else
    	{
    		return (JVxFormLayout.Anchor)pAnchor.getResource();
    	}
    }

    /**
     * Gets the JVxFormLayout.Anchor from an IAnchor.
     * @param pAnchor the IAnchor.
     * @return the JVxFormLayout.Anchor.
     */
    private AwtAnchor getAnchor(JVxFormLayout.Anchor pAnchor)
    {
    	if (pAnchor == null)
    	{
    		return null;
    	}
    	else
    	{
	    	WeakReference<AwtAnchor> anchorRef = anchors.get(pAnchor);
	    	if (anchorRef != null)
	    	{
	    		AwtAnchor anchor = anchorRef.get();
	    		
	    		if (anchor != null)
	    		{
	    			return anchor;
	    		}
	    	}
	    	AwtAnchor anchor = new AwtAnchor(this, pAnchor);
	    	
	    	anchors.put(pAnchor, new WeakReference(anchor));
	    	
	    	return anchor;
    	}
    }

    /**
     * Gets the anchors for the given native anchors.
     * @param pAnchors the native anchors
     * @return the anchors
     */
    private IAnchor[] getAnchors(JVxFormLayout.Anchor[] pAnchors)
    {
		IAnchor[] result = new IAnchor[pAnchors.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = getAnchor(pAnchors[i]);
		}
    	return result;
    }
    
    /**
     * Gets the JVxFormLayout.Constraint from an IConstraints.
     * @param pConstraints the IAnchor.
     * @return the JVxFormLayout.Constraint.
     */
    private JVxFormLayout.Constraint getConstraints(IConstraints pConstraints)
    {
    	if (pConstraints == null)
    	{
    		return null;
    	}
    	else
    	{
    		return (JVxFormLayout.Constraint)pConstraints.getResource();
    	}
    }

    /**
     * Gets the JVxFormLayout.Constraint from an IConstraints.
     * @param pConstraints the JVxFormLayout.Constraint.
     * @return the AwtConstraints.
     */
    private AwtConstraints getConstraints(JVxFormLayout.Constraint pConstraints)
    {
    	if (pConstraints == null)
    	{
    		return null;
    	}
    	else
    	{
	    	WeakReference<AwtConstraints> constraintRef = constraints.get(pConstraints);
	    	if (constraintRef != null)
	    	{
	    		AwtConstraints constraint = constraintRef.get();
	    		
	    		if (constraint != null)
	    		{
	    			return constraint;
	    		}
	    	}
	    	AwtConstraints constraint = new AwtConstraints(this, pConstraints);
	    	
	    	constraints.put(pConstraints, new WeakReference(constraint));
	    	
	    	return constraint;
    	}
    }

    /**
     * Gets the constraints for the left corner, specified through anchors.
     *  
     * @param pTopAnchor the top anchor
     * @param pLeftAnchor the left anchor
     * @return the constraints for the left corner  
     */
    public IConstraints getConstraints(IAnchor pTopAnchor, IAnchor pLeftAnchor)
    {
    	return new AwtConstraints(this, new JVxFormLayout.Constraint(getAnchor(pTopAnchor), getAnchor(pLeftAnchor)));
    }
    
	//****************************************************************
	// Subinterface definition
	//****************************************************************

    /**
	 * The Anchor gives the possible horizontal and vertical positions.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class AwtAnchor extends AwtResource<JVxFormLayout.Anchor> implements IAnchor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The layout. */
		private AwtFormLayout layout;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs a new AwtAnchor.
		 * 
		 * @param pLayout the layout.
		 * @param pAnchor the anchor.
		 */
		protected AwtAnchor(AwtFormLayout pLayout, JVxFormLayout.Anchor pAnchor)
		{
			super(pAnchor);
			
			layout = pLayout;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface Implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public IFormLayout getLayout()
	    {
	    	return layout;
	    }

		/**
		 * {@inheritDoc}
		 */
		public int getOrientation()
	    {
	    	return resource.getOrientation();
	    }

		/**
		 * Returns true, if this anchor is the border anchor.
		 *
		 * @return the fixed.
		 */
		public boolean isBorderAnchor()
	    {
	    	return resource.isBorderAnchor();
	    }
		
		/**
		 * {@inheritDoc}
		 */
		public IAnchor getRelatedAnchor()
	    {
	    	return layout.getAnchor(resource.getRelatedAnchor());
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setRelatedAnchor(IAnchor pRelatedAnchor)
	    {
			resource.setRelatedAnchor(layout.getAnchor(pRelatedAnchor));
	    }
		
		/**
		 * {@inheritDoc}
		 */
		public boolean isAutoSize()
	    {
	    	return resource.isAutoSize();
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setAutoSize(boolean pAutoSize)
	    {
			resource.setAutoSize(pAutoSize);
	    }

		/**
		 * {@inheritDoc}
		 */
		public int getPosition()
	    {
	    	return resource.getPosition();
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setPosition(int pPosition)
	    {
			resource.setPosition(pPosition);
	    }
		
		/**
		 * {@inheritDoc}
		 */
		public int getAbsolutePosition()
	    {
	    	return resource.getAbsolutePosition();
	    }
		
		/**
		 * {@inheritDoc}
		 */
		public IAnchor getBorderAnchor()
	    {
	    	return layout.getAnchor(resource.getBorderAnchor());
	    }
		
	}	// AwtAnchor
	
	/**
	 * The Constraint stores the top, left, bottom and right Anchor for layouting a component.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class AwtConstraints extends AwtResource<JVxFormLayout.Constraint> implements IConstraints, Cloneable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The layout. */
		private AwtFormLayout layout; 

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs a new AwtConstraint.
		 * 
		 * @param pLayout the layout.
		 * @param pConstraints the JVxFormLayout.Constraint.
		 */
		protected AwtConstraints(AwtFormLayout pLayout, JVxFormLayout.Constraint pConstraints)
		{
			super(pConstraints);
			
			layout = pLayout;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public IAnchor getLeftAnchor()
	    {
	    	return layout.getAnchor(resource.getLeftAnchor());
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setLeftAnchor(IAnchor pLeftAnchor)
	    {
			resource.setLeftAnchor(layout.getAnchor(pLeftAnchor));
	    }

		/**
		 * {@inheritDoc}
		 */
		public IAnchor getRightAnchor()
	    {
	    	return layout.getAnchor(resource.getRightAnchor());
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setRightAnchor(IAnchor pRightAnchor)
	    {
			resource.setRightAnchor(layout.getAnchor(pRightAnchor));
	    }

		/**
		 * {@inheritDoc}
		 */
		public IAnchor getTopAnchor()
	    {
	    	return layout.getAnchor(resource.getTopAnchor());
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setTopAnchor(IAnchor pTopAnchor)
	    {
			resource.setTopAnchor(layout.getAnchor(pTopAnchor));
	    }

		/**
		 * {@inheritDoc}
		 */
		public IAnchor getBottomAnchor()
	    {
	    	return layout.getAnchor(resource.getBottomAnchor());
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setBottomAnchor(IAnchor pBottomAnchor)
	    {
			resource.setBottomAnchor(layout.getAnchor(pBottomAnchor));
	    }
		
	}	// AwtConstraint

}	// AwtFormLayout
