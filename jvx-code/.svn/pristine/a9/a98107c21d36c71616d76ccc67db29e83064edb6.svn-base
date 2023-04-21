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
 * 26.11.2009 - [HM] - creation
 */
package com.sibvisions.rad.ui.web.impl.layout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IInsets;
import javax.rad.ui.layout.IFormLayout;

import com.sibvisions.rad.ui.web.impl.IWebContainer;
import com.sibvisions.rad.ui.web.impl.WebInsets;
import com.sibvisions.rad.ui.web.impl.WebLayout;
import com.sibvisions.rad.ui.web.impl.WebResource;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Web server implementation of {@link IFormLayout}.
 * 
 * @author Martin Handsteiner
 */
public class WebFormLayout extends WebLayout<IFormLayout.IConstraints>
						   implements IFormLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Gives the anchors a unique number. */
	private int anchorCounter = 0;
	
	/** the horizontal alignment. */
	private int horizontalAlignment = IAlignmentConstants.ALIGN_STRETCH;
	/** the vertical alignment. */
	private int verticalAlignment = IAlignmentConstants.ALIGN_STRETCH;
	
	/** the new line count. */
	private int newlineCount = 4;
	
	/** The left border anchor. */
	private WebAnchor leftAnchor = new WebAnchor(this, IAnchor.HORIZONTAL, "l");
	/** The left border anchor. */
	private WebAnchor rightAnchor = new WebAnchor(this, IAnchor.HORIZONTAL, "r");
	/** The left border anchor. */
	private WebAnchor topAnchor = new WebAnchor(this, IAnchor.VERTICAL, "t");
	/** The left border anchor. */
	private WebAnchor bottomAnchor = new WebAnchor(this, IAnchor.VERTICAL, "b");
	
	/** The left margin border anchor. */
	private WebAnchor leftMarginAnchor = new WebAnchor(leftAnchor, 10, "lm");
	/** The left margin border anchor. */
	private WebAnchor rightMarginAnchor = new WebAnchor(rightAnchor, -10, "rm");
	/** The left margin border anchor. */
	private WebAnchor topMarginAnchor = new WebAnchor(topAnchor, 10, "tm");
	/** The left margin border anchor. */
	private WebAnchor bottomMarginAnchor = new WebAnchor(bottomAnchor, -10, "bm");
	
	/** All left default anchors. */
	private List<WebAnchor> leftDefaultAnchors = new ArrayList<WebAnchor>();
	/** All top default anchors. */
	private List<WebAnchor> topDefaultAnchors = new ArrayList<WebAnchor>();
	/** All left default anchors. */
	private List<WebAnchor> rightDefaultAnchors = new ArrayList<WebAnchor>();
	/** All top default anchors. */
	private List<WebAnchor> bottomDefaultAnchors = new ArrayList<WebAnchor>();
	
	/** the margins. */
	private IInsets margins;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIFormLayout</code>.
     *
     * @see IFormLayout
     */
	public WebFormLayout()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getData(IWebContainer pContainer)
	{
		Set<String> anchors = new HashSet<String>();
		
		analyzeAnchor(anchors, topAnchor);
		analyzeAnchor(anchors, leftAnchor);
		analyzeAnchor(anchors, bottomAnchor);
		analyzeAnchor(anchors, rightAnchor);
		
		analyzeAnchor(anchors, topMarginAnchor);
		analyzeAnchor(anchors, leftMarginAnchor);
		analyzeAnchor(anchors, bottomMarginAnchor);
		analyzeAnchor(anchors, rightMarginAnchor);

		WebConstraints cons;
		
		for (int i = 0, cnt = pContainer.getComponentCount(); i < cnt; i++)
		{
			cons = (WebConstraints)getConstraints(pContainer.getComponent(i));
			
			analyzeAnchor(anchors, cons.topAnchor);
			analyzeAnchor(anchors, cons.leftAnchor);
			analyzeAnchor(anchors, cons.bottomAnchor);
			analyzeAnchor(anchors, cons.rightAnchor);
		}
		
		return StringUtil.concat(";", anchors.toArray());
	}
	
	/**
	 * Detects all (related)anchors starting with a specific anchor.
	 * 
	 * @param pAnchors the list of detected anchors
	 * @param pAnchor the anchor to analyze
	 */
	private void analyzeAnchor(Set<String> pAnchors, WebAnchor pAnchor)
	{
		WebAnchor anchor = pAnchor;
		
		while (anchor != null)
		{
			if (pAnchors.add(anchor.getAsString()))
			{
				anchor = anchor.relatedAnchor;
			}
			else
			{
				anchor = null;
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
	
    /**
     * {@inheritDoc}
     */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		int old = horizontalAlignment;
		
		horizontalAlignment = pHorizontalAlignment;
		
		if (old != horizontalAlignment)
		{
			markChanged();
		}
	}
	
    /**
     * {@inheritDoc}
     */
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}

    /**
     * {@inheritDoc}
     */
    public void setVerticalAlignment(int pVerticalAlignment)
    {
    	int old = verticalAlignment;
    	
    	verticalAlignment = pVerticalAlignment;
    	
		if (old != verticalAlignment)
		{
			markChanged();
		}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public WebInsets getMargins()
    {
    	return new WebInsets(topMarginAnchor.position, leftMarginAnchor.position, -bottomMarginAnchor.position, -rightMarginAnchor.position);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setMargins(IInsets pMargins)
    {
    	IInsets old = margins;
    	
    	margins = pMargins;
    	
    	if (pMargins == null)
    	{
    		topMarginAnchor.position = 0;
    		leftMarginAnchor.position = 0; 
			bottomMarginAnchor.position = 0;
			rightMarginAnchor.position = 0;
    	}
    	else
    	{
    		topMarginAnchor.position = pMargins.getTop();
    		leftMarginAnchor.position = pMargins.getLeft(); 
			bottomMarginAnchor.position = -pMargins.getBottom();
			rightMarginAnchor.position = -pMargins.getRight();
    	}
    	
		if (!CommonUtil.equals(old, margins))
		{
			markChanged();
		}
    }

	/**
	 * {@inheritDoc}
	 */
    public int getNewlineCount()
    {
    	return newlineCount;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setNewlineCount(int pNewlineCount)
    {
    	int old = newlineCount;
    	
    	newlineCount = pNewlineCount;
    	
		if (old != newlineCount)
		{
			markChanged();
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor getLeftAnchor()
    {
    	return leftAnchor;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getRightAnchor()
    {
    	return rightAnchor;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getTopAnchor()
    {
    	return topAnchor;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getBottomAnchor()
    {
    	return bottomAnchor;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getLeftMarginAnchor()
    {
    	return leftMarginAnchor;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getRightMarginAnchor()
    {
    	return rightMarginAnchor;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getTopMarginAnchor()
    {
    	return topMarginAnchor;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getBottomMarginAnchor()
    {
    	return bottomMarginAnchor;
    }

    /**
     * Fills in the given anchor and all related anchors.
     * 
     * @param pAnchors the list of anchors
     * @param pAnchor the anchor
     */
    private void fillInAnchors(LinkedHashSet<IAnchor> pAnchors, IAnchor pAnchor)
    {
        while (pAnchor != null && pAnchors.add(pAnchor))
        {
            pAnchor = pAnchor.getRelatedAnchor();
        }
    }
    
	/**
	 * {@inheritDoc}
	 */
	public IAnchor[] getHorizontalAnchors()
	{
        LinkedHashSet<IAnchor> horizontalAnchors = new LinkedHashSet<IAnchor>();
        
        IWebContainer container = getContainer();
        for (int i = 0, count = container.getComponentCount(); i < count; i++)
        {
            IConstraints constraint = getConstraints(container.getComponent(i));
            fillInAnchors(horizontalAnchors, constraint.getLeftAnchor());
            fillInAnchors(horizontalAnchors, constraint.getRightAnchor());
        }
        
        return horizontalAnchors.toArray(new IAnchor[horizontalAnchors.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor[] getVerticalAnchors()
	{
        LinkedHashSet<IAnchor> verticalAnchors = new LinkedHashSet<IAnchor>();
        
        IWebContainer container = getContainer();
        for (int i = 0, count = container.getComponentCount(); i < count; i++)
        {
            IConstraints constraint = getConstraints(container.getComponent(i));
            fillInAnchors(verticalAnchors, constraint.getTopAnchor());
            fillInAnchors(verticalAnchors, constraint.getBottomAnchor());
        }
        
        return verticalAnchors.toArray(new IAnchor[verticalAnchors.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public WebAnchor createAnchor(IAnchor pRelatedAnchor)
    {
    	return new WebAnchor((WebAnchor)pRelatedAnchor);
    }

	/**
	 * {@inheritDoc}
	 */
	public WebAnchor createAnchor(IAnchor pRelatedAnchor, int pPosition)
    {
    	return new WebAnchor((WebAnchor)pRelatedAnchor, pPosition);
    }

	/**
	 * {@inheritDoc}
	 */
    public WebConstraints getConstraints(int pColumn, int pRow)
    {
    	return getConstraints(pColumn, pRow, pColumn, pRow);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public WebConstraints getConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	WebAnchor[] left = createDefaultAnchors(leftDefaultAnchors, rightDefaultAnchors, leftMarginAnchor, rightMarginAnchor, pBeginColumn, getHorizontalGap());
    	WebAnchor[] right;
    	if (pBeginColumn == pEndColumn)
    	{
    		right = left;
    	}
    	else
    	{
    		right = createDefaultAnchors(leftDefaultAnchors, rightDefaultAnchors, leftMarginAnchor, rightMarginAnchor, pEndColumn, getHorizontalGap());
    	}
    	
    	WebAnchor[] top = createDefaultAnchors(topDefaultAnchors, bottomDefaultAnchors, topMarginAnchor, bottomMarginAnchor, pBeginRow, getVerticalGap());
    	WebAnchor[] bottom;
    	if (pBeginRow == pEndRow)
    	{
    		bottom = top;
    	}
    	else
    	{
    		bottom = createDefaultAnchors(topDefaultAnchors, bottomDefaultAnchors, topMarginAnchor, bottomMarginAnchor, pEndRow, getVerticalGap());
    	}
    	return new WebConstraints(top[0], 
    						     left[0], 
    						     bottom[1], 
    						     right[1]);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public WebConstraints getConstraints(IAnchor pTopAnchor, IAnchor pLeftAnchor, IAnchor pBottomAnchor, IAnchor pRightAnchor)
    {
    	return new WebConstraints((WebAnchor)pTopAnchor, (WebAnchor)pLeftAnchor, (WebAnchor)pBottomAnchor, (WebAnchor)pRightAnchor);
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    @Override
	public void setComponentConstraints(IComponent pComponent, Object pConstraints)
    {
    	WebConstraints constraint;

    	if (pConstraints instanceof WebConstraints)
    	{
    		constraint = (WebConstraints)pConstraints;
    	}
    	else
    	{
    		constraint = null;
    		
    		IContainer parent = pComponent.getParent();
    		int zOrder = parent.indexOf(pComponent);
		  
    		if (zOrder == parent.getComponentCount() - 1)
    		{
    			WebConstraints consBefore;
    			if (zOrder == 0)
    			{
    				consBefore = null;
    			}
    			else
    			{
    				consBefore = (WebConstraints)getConstraints(parent.getComponent(zOrder - 1));
    			}
				if (consBefore == null)
				{
					constraint = getConstraints(0, 0);
				}
				else
				{
					int col = leftDefaultAnchors.indexOf(consBefore.leftAnchor) / 2 + 1;
					int row = topDefaultAnchors.indexOf(consBefore.topAnchor) / 2;
					
	    			if (pConstraints == NEWLINE || (pConstraints == null && col % newlineCount == 0))
	    			{
   						constraint = getConstraints(0, row + 1);
	    			}
	    			else if (pConstraints == null)
	    			{
	    				constraint = getConstraints(col, row);
	    			}
				}
    		}
    	}
	
    	if (constraint == null)
    	{
    		throw new IllegalArgumentException("Constraint " + pConstraints + " is not allowed!");
    	}
    	else
    	{
    		setConstraints(pComponent, constraint);
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    public String getAsString()
    {
    	return super.getAsString() + "," + horizontalAlignment + "," + verticalAlignment;
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates the default anchors.
     * 
     * @param pLeftTopDefaultAnchors the vector to store the anchors.
     * @param pRightBottomDefaultAnchors the vector to store the anchors.
     * @param pLeftTopAnchor the left or top margin anchor.
     * @param pRightBottomAnchor the right or bottom margin anchor.
     * @param pColumnOrRow the column or the row.
     * @param pGap the horizontal or vertical gap.
     * @return the leftTop and rightBottom Anchors.
     */
    private WebAnchor[] createDefaultAnchors(List<WebAnchor> pLeftTopDefaultAnchors, 
    										 List<WebAnchor> pRightBottomDefaultAnchors, 
    										 WebAnchor pLeftTopAnchor, 
    										 WebAnchor pRightBottomAnchor, 
    										 int pColumnOrRow,
    										 int pGap)
    {
    	List<WebAnchor> defaultAnchors;
    	WebAnchor anchor;
    	
    	int gap;
    	boolean rightBottom = pColumnOrRow < 0;
    	String ltName;
    	String rbName;
    	
    	if (pLeftTopAnchor == leftMarginAnchor)
    	{
    		ltName = "l";
    		rbName = "r";
    	}
    	else
    	{
    		ltName = "t";
    		rbName = "b";
    	}
    	
    	int size;
    	int index;
    	
    	if (rightBottom)
    	{
        	pColumnOrRow = (-pColumnOrRow - 1) * 2;
        	defaultAnchors = pRightBottomDefaultAnchors;
        	anchor = pRightBottomAnchor;
        	gap = -pGap;
        	size = defaultAnchors.size();
        	index = -size / 2 - 1; 
    	}
    	else
    	{
        	pColumnOrRow *= 2;
        	defaultAnchors = pLeftTopDefaultAnchors;
        	anchor = pLeftTopAnchor;
        	gap = pGap;
        	size = defaultAnchors.size();
        	index = size / 2;
    	}
    	
    	while (pColumnOrRow >= size)
    	{
    		if (size == 0)
    		{
    			defaultAnchors.add(anchor);
    		}
    		else
    		{
    			defaultAnchors.add(new WebAnchor(defaultAnchors.get(size - 1), gap, rightBottom ? rbName + index : ltName + index));
    		}
    		
    		defaultAnchors.add(new WebAnchor(defaultAnchors.get(size), rightBottom ? ltName + index : rbName + index));
    		
    		size = defaultAnchors.size();
    		
    		if (rightBottom)
    		{
    			index--;
    		}
    		else
    		{
    			index++;
    		}
    	}
    	
    	if (rightBottom)
    	{
        	return new WebAnchor[] {defaultAnchors.get(pColumnOrRow + 1), defaultAnchors.get(pColumnOrRow)}; 
    	}
    	else
    	{
        	return new WebAnchor[] {defaultAnchors.get(pColumnOrRow), defaultAnchors.get(pColumnOrRow + 1)}; 
    	}
    }
    
	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
	 * The <code>WebAnchor</code> gives the possible horizontal and vertical positions.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class WebAnchor extends WebResource
	                      		  implements IAnchor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The layout. */
		private WebFormLayout layout;
		
		/** The anchor name. */
		private String name;

		/** The orientation of this anchor. */
		private int       orientation;
		/** The related anchor to this anchor. */
		private WebAnchor relatedAnchor;
		/** true, if this anchor should be auto sized. */
		private boolean   autoSize;
		/** The position of this anchor. */
		private int       position;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs a border anchor for this layout.
		 * 
		 * @param pLayout the layout.
		 * @param pOrientation the orientation.
		 * @param pName the anchor name.
		 */
		protected WebAnchor(WebFormLayout pLayout, int pOrientation, String pName)
		{
			layout = pLayout;
			name = pName;
			orientation = pOrientation;
			relatedAnchor = null;
			autoSize = false;
			position = 0;
		}

		/**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 */
		public WebAnchor(WebAnchor pRelatedAnchor)
		{
			this(pRelatedAnchor, null);
		}

		/**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pName the anchor name
		 */
		public WebAnchor(WebAnchor pRelatedAnchor, String pName)
		{
			layout = pRelatedAnchor.layout;
			name = pName;
			
			if (name == null)
			{
				name = orientationPrefix(pRelatedAnchor.orientation) + "" + layout.anchorCounter++;
			}
			
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			autoSize = true;
			position = 0;
		}

		/**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pPosition the position relative to the related anchor.
		 */
		public WebAnchor(WebAnchor pRelatedAnchor, int pPosition)
		{
			this(pRelatedAnchor, pPosition, null);
		}

		/**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pPosition the position relative to the related anchor.
		 * @param pName the anchor name
		 */
		public WebAnchor(WebAnchor pRelatedAnchor, int pPosition, String pName)
		{
			layout = pRelatedAnchor.layout;
			name = pName;
			
			if (name == null)
			{
				name = orientationPrefix(pRelatedAnchor.orientation) + "" + layout.anchorCounter++;
			}
			
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			autoSize = false;
			position = pPosition;
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Abstract methods implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
		 * {@inheritDoc}
		 */
	    @Override
	    public String getAsString()
	    {
	    	return name + "," + (relatedAnchor == null ? "-" : relatedAnchor.name)
	    	            + "," + "-"
	    	            + "," + (autoSize ? "a" : String.valueOf(position))
	    	            + "," + String.valueOf(position);
	    }

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public WebFormLayout getLayout()
	    {
	    	return layout;
	    }

		/**
		 * {@inheritDoc}
		 */
		public int getOrientation()
	    {
	    	return orientation;
	    }

		/**
		 * {@inheritDoc}
		 */
		public WebAnchor getRelatedAnchor()
	    {
	    	return relatedAnchor;
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setRelatedAnchor(IAnchor pAnchor)
	    {
	    	relatedAnchor = (WebAnchor)pAnchor;
	    }

		/**
		 * {@inheritDoc}
		 */
		public boolean isAutoSize()
	    {
	    	return autoSize;
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setAutoSize(boolean pAutoSize)
	    {
			autoSize = pAutoSize;
	    }

		/**
		 * {@inheritDoc}
		 */
		public int getPosition()
	    {
	    	return position;
	    }

		/**
		 * {@inheritDoc}
		 */
		public void setPosition(int pPosition)
	    {
			position = pPosition;
	    }

		/**
		 * {@inheritDoc}
		 */
		public IAnchor getBorderAnchor()
		{
			WebAnchor borderAnchor = this;
			
			while (borderAnchor.relatedAnchor != null)
			{
				borderAnchor = borderAnchor.relatedAnchor;
			}
			
			return borderAnchor;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int getAbsolutePosition()
		{
			if (relatedAnchor == null)
			{
				return position;
			}
			else
			{
				return relatedAnchor.getAbsolutePosition() + position;
			}
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the prefix for an orientation.
		 * 
		 * @param pOrientation the orientation
		 * @return the prefix
		 */
		private static String orientationPrefix(int pOrientation)
		{
			if (pOrientation == IAnchor.HORIZONTAL)
			{
				return "h";
			}
			else
			{
				return "v";
			}
		}
	    
	}	// WebAnchor
	
	/**
	 * The <code>WebConstraints</code> stores the top, left, bottom and right Anchor for layouting a component.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class WebConstraints extends WebResource
	                                   implements IFormLayout.IConstraints
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The top anchor. */
		private WebAnchor topAnchor;
		/** The left anchor. */
		private WebAnchor leftAnchor;
		/** The bottom anchor. */
		private WebAnchor bottomAnchor;
		/** The right anchor. */
		private WebAnchor rightAnchor;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates the default constraints for the given anchors.
		 * 
		 * @param pTopAnchor the top anchor.
		 * @param pLeftAnchor the left anchor.
		 * @param pBottomAnchor the bottom anchor.
		 * @param pRightAnchor the right anchor.
		 */
	    public WebConstraints(WebAnchor pTopAnchor, WebAnchor pLeftAnchor, WebAnchor pBottomAnchor, WebAnchor pRightAnchor)
	    {
			if (pLeftAnchor == null && pRightAnchor != null)
			{
				pLeftAnchor = new WebAnchor(pRightAnchor);
			}
			else if (pRightAnchor == null && pLeftAnchor != null)
			{
				pRightAnchor = new WebAnchor(pLeftAnchor);
			}
			if (pTopAnchor == null && pBottomAnchor != null)
			{
				pTopAnchor = new WebAnchor(pBottomAnchor);
			}
			else if (pBottomAnchor == null && pTopAnchor != null)
			{
				pBottomAnchor = new WebAnchor(pTopAnchor);
			}
			setLeftAnchor(pLeftAnchor);
			setRightAnchor(pRightAnchor);
			setTopAnchor(pTopAnchor);
			setBottomAnchor(pBottomAnchor);
	    }
	    
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Returns the left anchor.
		 *
		 * @return the left anchor.
		 */
		public WebAnchor getLeftAnchor()
		{
			return leftAnchor;
		}

		/**
		 * Sets the left anchor.
		 *
		 * @param pLeftAnchor left to set
		 */
		public void setLeftAnchor(IAnchor pLeftAnchor)
		{
			if (pLeftAnchor == null && rightAnchor != null)
			{
				leftAnchor = new WebAnchor(rightAnchor);
			}
			else if (pLeftAnchor.getOrientation() == IAnchor.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as left anchor!");
			}
			else
			{
				leftAnchor = (WebAnchor)pLeftAnchor;
			}
		}

		/**
		 * Returns the right anchor.
		 *
		 * @return the right anchor.
		 */
		public WebAnchor getRightAnchor()
		{
			return rightAnchor;
		}

		/**
		 * Sets the right anchor.
		 *
		 * @param pRightAnchor the right anchor.
		 */
		public void setRightAnchor(IAnchor pRightAnchor)
		{
			if (pRightAnchor == null && leftAnchor != null)
			{
				rightAnchor = new WebAnchor(leftAnchor);
			}
			else if (pRightAnchor.getOrientation() == IAnchor.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as right anchor!");
			}
			else
			{
				rightAnchor = (WebAnchor)pRightAnchor;
			}
		}

		/**
		 * Returns the top anchor.
		 *
		 * @return the top anchor.
		 */
		public WebAnchor getTopAnchor()
		{
			return topAnchor;
		}

		/**
		 * Sets the top anchor.
		 *
		 * @param pTopAnchor the top anchor
		 */
		public void setTopAnchor(IAnchor pTopAnchor)
		{
			if (pTopAnchor == null && bottomAnchor != null)
			{
				topAnchor = new WebAnchor(bottomAnchor);
			}
			else if (pTopAnchor.getOrientation() == IAnchor.HORIZONTAL)
			{
				throw new IllegalArgumentException("A horizontal anchor can not be used as top anchor!");
			}
			else
			{
				topAnchor = (WebAnchor)pTopAnchor;
			}
		}

		/**
		 * Returns the bottom anchor.
		 *
		 * @return the bottom anchor.
		 */
		public WebAnchor getBottomAnchor()
		{
			return bottomAnchor;
		}

		/**
		 * Sets the bottom anchor.
		 *
		 * @param pBottomAnchor the bottom to set
		 */
		public void setBottomAnchor(IAnchor pBottomAnchor)
		{
			if (pBottomAnchor == null && topAnchor != null)
			{
				bottomAnchor = new WebAnchor(topAnchor);
			}
			else if (pBottomAnchor.getOrientation() == IAnchor.HORIZONTAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as bottom anchor!");
			}
			else
			{
				bottomAnchor = (WebAnchor)pBottomAnchor;
			}
		}

		/**
		 * {@inheritDoc}
		 */
	    public String getAsString()
	    {
	    	return topAnchor.name + ";" + leftAnchor.name + ";" + bottomAnchor.name + ";" + rightAnchor.name;
	    }
	    
	}	// WebConstraints
    
}	// WebFormLayout
