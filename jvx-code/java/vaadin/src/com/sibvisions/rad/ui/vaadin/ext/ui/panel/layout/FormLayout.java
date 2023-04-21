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
 * 25.06.2015- [LT] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.panel.layout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.rad.ui.layout.IFormLayout.IAnchor;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Margins;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.ui.Component;

/**
 * The {@link FormLayout} allows to use layout the components in a complex form
 * pattern.
 * 
 * @author Robert Zenz
 */
public class FormLayout extends AbstractAlignedLayout
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Gives the anchors a unique number. */
	private int anchorCounter = 0;
	
	/** The {@link WebConstraint} containing the border anchors. */
	private WebConstraint borderConstraint = null;
	
	/** All top default anchors. */
	private List<WebAnchor> bottomDefaultAnchors = new ArrayList<WebAnchor>();
	
	/** List containing the comp0onents. */
	private Map<Component, WebConstraint> componentsToConstraints = new HashMap<Component, WebConstraint>();
	
	/** All left default anchors. */
	private List<WebAnchor> leftDefaultAnchors = new ArrayList<WebAnchor>();
	
	/** The {@link WebConstraint} containing the margin anchors. */
	private WebConstraint marginConstraint = null;
	
	/** The newline count. */
	private int newlineCount = 2;
	
	/** All left default anchors. */
	private List<WebAnchor> rightDefaultAnchors = new ArrayList<WebAnchor>();
	
	/** All top default anchors. */
	private List<WebAnchor> topDefaultAnchors = new ArrayList<WebAnchor>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link FormLayout}.
	 */
	public FormLayout()
	{
		super("FormLayout");
		
		horizontalAlignment = HorizontalAlignment.Stretch;
		verticalAlignment = VerticalAlignment.Stretch;
		
		horizontalGap = 5;
		verticalGap = 5;
		
		margins.top = 10;
		margins.left = 10;
		margins.bottom = 10;
		margins.right = 10;
		
		borderConstraint = new WebConstraint(this,
				new WebAnchor(this, IAnchor.VERTICAL, "t"),
				new WebAnchor(this, IAnchor.HORIZONTAL, "l"),
				new WebAnchor(this, IAnchor.VERTICAL, "b"),
				new WebAnchor(this, IAnchor.HORIZONTAL, "r"));
		
		marginConstraint = new WebConstraint(this,
				new WebAnchor(borderConstraint.topAnchor, 10, "tm"),
				new WebAnchor(borderConstraint.leftAnchor, 10, "lm"),
				new WebAnchor(borderConstraint.bottomAnchor, -10, "bm"),
				new WebAnchor(borderConstraint.rightAnchor, -10, "rm"));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getStringConstraint(Component pComponent, Object pConstraint)
	{
		if (pConstraint instanceof WebConstraint)
		{
			componentsToConstraints.put(pComponent, (WebConstraint)pConstraint);
		}
		else if ("\n".equals(pConstraint))
		{
			componentsToConstraints.put(pComponent, getNewlineConstraint());
		}
		else
		{
			componentsToConstraints.put(pComponent, getNextConstraints());
		}
		
		return getConstraint(pComponent).getAsString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void clear(Component pComponent)
	{
		componentsToConstraints.remove(pComponent);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getData()
	{
		setAnchorsIntoData();
		
		return super.getData();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMargins(Margins pMargins)
	{
		super.setMargins(pMargins);
		
		if (pMargins == null)
		{
			getTopMarginAnchor().position = 0;
			getLeftMarginAnchor().position = 0;
			getBottomMarginAnchor().position = 0;
			getRightMarginAnchor().position = 0;
		}
		else
		{
			getTopMarginAnchor().position = pMargins.top;
			getLeftMarginAnchor().position = pMargins.left;
			getBottomMarginAnchor().position = -pMargins.bottom;
			getRightMarginAnchor().position = -pMargins.right;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link WebAnchor bottom anchor}.
	 *
	 * @return the {@link WebAnchor bottom anchor}.
	 */
	public WebAnchor getBottomAnchor()
	{
		return borderConstraint.bottomAnchor;
	}
	
	/**
	 * Gets the {@link WebAnchor bottom margin anchor}.
	 *
	 * @return the {@link WebAnchor bottom margin anchor}.
	 */
	public WebAnchor getBottomMarginAnchor()
	{
		return marginConstraint.bottomAnchor;
	}
	
	/**
	 * Gets the constraints of the component.
	 * 
	 * @param component The component which constraints are needed
	 * @return An instance of Constraints containing anchors.
	 */
	public WebConstraint getConstraint(Component component)
	{
		return componentsToConstraints.get(component);
	}
	
	/**
	 * Creates the default constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given component.
	 */
	public WebConstraint getConstraints(int pColumn, int pRow)
	{
		return getConstraints(pColumn, pRow, pColumn, pRow);
	}
	
	/**
	 * Gets the {@link WebConstraint constraints}.
	 *
	 * @param pBeginColumn the begin column.
	 * @param pBeginRow the begin row.
	 * @param pEndColumn the end column.
	 * @param pEndRow the end row.
	 * @return the {@link WebConstraint constraints}.
	 */
	public WebConstraint getConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
	{
		WebAnchor[] left = createDefaultAnchors(
				leftDefaultAnchors,
				rightDefaultAnchors,
				marginConstraint.leftAnchor,
				marginConstraint.rightAnchor,
				pBeginColumn,
				horizontalGap);
		
		WebAnchor[] right;
		
		if (pBeginColumn == pEndColumn)
		{
			right = left;
		}
		else
		{
			right = createDefaultAnchors(
					leftDefaultAnchors,
					rightDefaultAnchors,
					marginConstraint.leftAnchor,
					marginConstraint.rightAnchor,
					pEndColumn,
					horizontalGap);
		}
		
		WebAnchor[] top = createDefaultAnchors(
				topDefaultAnchors,
				bottomDefaultAnchors,
				marginConstraint.topAnchor,
				marginConstraint.bottomAnchor,
				pBeginRow, verticalGap);
		
		WebAnchor[] bottom;
		
		if (pBeginRow == pEndRow)
		{
			bottom = top;
		}
		else
		{
			bottom = createDefaultAnchors(
					topDefaultAnchors,
					bottomDefaultAnchors,
					marginConstraint.topAnchor,
					marginConstraint.bottomAnchor,
					pEndRow,
					verticalGap);
		}
		
		return new WebConstraint(
				this,
				top[0],
				left[0],
				bottom[1],
				right[1]);
	}
	
	/**
	 * Fills in the given anchor and all related anchors.
	 * 
	 * @param pAnchors the list of anchors
	 * @param pAnchor the anchor
	 */
	private void fillInAnchors(LinkedHashSet<WebAnchor> pAnchors, WebAnchor pAnchor)
	{
	    while (pAnchor != null && pAnchors.add(pAnchor))
	    {
	        pAnchor = pAnchor.getRelatedAnchor();
	    }
	}
	
	/**
	 * Gets all horizontal {@link WebAnchor}s.
	 * 
	 * @return all horizontal {@link WebAnchor}s.
	 */
	public List<WebAnchor> getHorizontalAnchors()
	{
		LinkedHashSet<WebAnchor> horizontalAnchors = new LinkedHashSet<WebAnchor>();
		
		for (WebConstraint constraint : componentsToConstraints.values())
		{
		    fillInAnchors(horizontalAnchors, constraint.getLeftAnchor());
            fillInAnchors(horizontalAnchors, constraint.getRightAnchor());
		}
		
		return new ArrayList<WebAnchor>(horizontalAnchors);
	}
	
    /**
     * Gets all vertical {@link WebAnchor}s.
     * 
     * @return all vertical {@link WebAnchor}s.
     */
    public List<WebAnchor> getVerticalAnchors()
    {
        LinkedHashSet<WebAnchor> verticalAnchors = new LinkedHashSet<WebAnchor>();
        
        for (WebConstraint constraint : componentsToConstraints.values())
        {
            fillInAnchors(verticalAnchors, constraint.getTopAnchor());
            fillInAnchors(verticalAnchors, constraint.getBottomAnchor());
        }
        
        return new ArrayList<WebAnchor>(verticalAnchors);
    }
    
	/**
	 * Gets the {@link WebAnchor left anchor}.
	 *
	 * @return the {@link WebAnchor left anchor}.
	 */
	public WebAnchor getLeftAnchor()
	{
		return borderConstraint.leftAnchor;
	}
	
	/**
	 * Gets the {@link WebAnchor left margin anchor}.
	 *
	 * @return the {@link WebAnchor left margin anchor}.
	 */
	public WebAnchor getLeftMarginAnchor()
	{
		return marginConstraint.leftAnchor;
	}
	
	/**
	 * Gets the newline count.
	 *
	 * @return the newline count.
	 */
	public int getNewlineCount()
	{
		return newlineCount;
	}
	
	/**
	 * Gets the {@link WebAnchor right anchor}.
	 *
	 * @return the {@link WebAnchor right anchor}.
	 */
	public WebAnchor getRightAnchor()
	{
		return borderConstraint.rightAnchor;
	}
	
	/**
	 * Gets the {@link WebAnchor right margin anchor}.
	 *
	 * @return the {@link WebAnchor right margin anchor}.
	 */
	public WebAnchor getRightMarginAnchor()
	{
		return marginConstraint.rightAnchor;
	}
	
	/**
	 * Gets the {@link WebAnchor top anchor}.
	 *
	 * @return the {@link WebAnchor top anchor}.
	 */
	public WebAnchor getTopAnchor()
	{
		return borderConstraint.topAnchor;
	}
	
	/**
	 * Gets the {@link WebAnchor top margin anchor}.
	 *
	 * @return the {@link WebAnchor top margin anchor}.
	 */
	public WebAnchor getTopMarginAnchor()
	{
		return marginConstraint.topAnchor;
	}
	
	/**
	 * Sets the position of a component in the layout.
	 * 
	 * @param component the component
	 * @param constraint the constraints
	 */
	public void setConstraint(Component component, WebConstraint constraint)
	{
		if (!componentsToConstraints.containsKey(component))
		{
			throw new IllegalArgumentException("Component must be a child of this layout");
		}
		
		componentsToConstraints.put(component, constraint);
		
		parent.notifyOfChanges();
	}
	
	/**
	 * Sets the newline count.
	 *
	 * @param pNewlineCount the new newline count.
	 */
	public void setNewlineCount(int pNewlineCount)
	{
		newlineCount = pNewlineCount;
		
		data.put("newlineCount", Integer.toString(newlineCount));
		
		parent.notifyOfChanges();
	}
	
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
		
		if (rightBottom)
		{
			pColumnOrRow = (-pColumnOrRow - 1) * 2;
			defaultAnchors = pRightBottomDefaultAnchors;
			anchor = pRightBottomAnchor;
			gap = -pGap;
		}
		else
		{
			pColumnOrRow *= 2;
			defaultAnchors = pLeftTopDefaultAnchors;
			anchor = pLeftTopAnchor;
			gap = pGap;
		}
		
		int size = defaultAnchors.size();
		
		while (pColumnOrRow >= size)
		{
			if (size == 0)
			{
				defaultAnchors.add(anchor);
			}
			else
			{
				defaultAnchors.add(new WebAnchor(defaultAnchors.get(size - 1), gap));
			}
			
			defaultAnchors.add(new WebAnchor(defaultAnchors.get(size)));
			size = defaultAnchors.size();
		}
		
		if (rightBottom)
		{
			return new WebAnchor[] { defaultAnchors.get(pColumnOrRow + 1), defaultAnchors.get(pColumnOrRow) };
		}
		else
		{
			return new WebAnchor[] { defaultAnchors.get(pColumnOrRow), defaultAnchors.get(pColumnOrRow + 1) };
		}
	}
	
	/**
	 * Gets the next {@link WebConstraint} based on the current layout.
	 * 
	 * @return the next {@link WebConstraint}.
	 */
	private WebConstraint getNextConstraints()
	{
		if (components.size() >= 1)
		{
			WebConstraint constraintsBefore = getPreviousConstraints();
			
			// Might happen if the client decides to add directly to the children
			// list instead of using the addChild(...) methods.
			if (constraintsBefore != null)
			{
				int column = leftDefaultAnchors.indexOf(constraintsBefore.leftAnchor) / 2 + 1;
				int row = topDefaultAnchors.indexOf(constraintsBefore.topAnchor) / 2;
				
				if (column % newlineCount == 0)
				{
					return getConstraints(0, row + 1);
				}
				else
				{
					return getConstraints(column, row);
				}
			}
		}
		
		// First children to be added. Or at least the first with constraints.
		return getConstraints(0, 0);
	}
	
	/**
	 * Gets the {@link WebConstraint} for a new line.
	 * 
	 * @return the {@link WebConstraint} for a new line.
	 */
	private WebConstraint getNewlineConstraint()
	{
		if (components.size() >= 1)
		{
			WebConstraint constraintsBefore = getPreviousConstraints();
			
			if (constraintsBefore != null)
			{
				int row = topDefaultAnchors.indexOf(constraintsBefore.topAnchor) / 2;
				return getConstraints(0, row + 1);
			}
		}
		
		return getConstraints(0, 0);
	}
	
	/**
	 * Gets the previous {@link WebConstraint} based on the current layout.
	 * 
	 * @return the previous {@link WebConstraint}.
	 */
	private WebConstraint getPreviousConstraints()
	{
		for (int index = components.size() - 1; index >= 0; index--)
		{
			WebConstraint previousConstraints = getConstraint(components.get(index));
			
			if (previousConstraints != null)
			{
				return previousConstraints;
			}
		}
		
		return null;
	}
	
	/**
	 * Sets the anchors into the state.
	 */
	private void setAnchorsIntoData()
	{
		Set<String> anchors = new HashSet<String>();
		
		setAnchorsIntoData(anchors, borderConstraint);
		setAnchorsIntoData(anchors, marginConstraint);
		
		for (Component component : components)
		{
			WebConstraint constraint = getConstraint(component);
			setAnchorsIntoData(anchors, constraint);
		}
		
		data.put("anchors", StringUtil.concat(";", anchors.toArray()));
	}
	
	/**
	 * Sets the anchors of the given constraint into the state.
	 * 
	 * @param pAnchors the list of set anchors.
	 * @param pConstraint the constraint to set.
	 */
	private void setAnchorsIntoData(Set<String> pAnchors, WebConstraint pConstraint)
	{
		for (WebAnchor anchor : new WebAnchor[] {
				pConstraint.getTopAnchor(),
				pConstraint.getLeftAnchor(),
				pConstraint.getBottomAnchor(),
				pConstraint.getRightAnchor()
		})
		{
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
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>WebAnchor</code> gives the possible horizontal and vertical
	 * positions.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class WebAnchor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Constants
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** Constant for horizontal anchors. */
		public static final int HORIZONTAL = 0;
		
		/** Constant for vertical anchors. */
		public static final int VERTICAL = 1;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** true, if this anchor should be auto sized. */
		private boolean autoSize;
		
		/** The layout. */
		private FormLayout layout;
		
		/** The anchor name. */
		private String name;
		
		/** The orientation of this anchor. */
		private int orientation;
		
		/** The position of this anchor. */
		private int position;
		
		/** The related anchor to this anchor. */
		private WebAnchor relatedAnchor;
		
		/** The second related anchor to this anchor. */
		private float relativePosition;
		
		/** The second related anchor to this anchor. */
		private WebAnchor secondRelatedAnchor;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition
		 * pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 */
		public WebAnchor(WebAnchor pRelatedAnchor)
		{
			layout = pRelatedAnchor.layout;
			name = Integer.toString(layout.anchorCounter++);
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			secondRelatedAnchor = null;
			autoSize = true;
			position = 0;
			relativePosition = 0.5f;
		}
		
		/**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition
		 * pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pPosition the position relative to the related anchor.
		 */
		public WebAnchor(WebAnchor pRelatedAnchor, int pPosition)
		{
			layout = pRelatedAnchor.layout;
			name = Integer.toString(layout.anchorCounter++);
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			secondRelatedAnchor = null;
			autoSize = false;
			position = pPosition;
			relativePosition = 0.5f;
		}
		
		/**
		 * Constructs an centered anchor between the related and second related
		 * anchor.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pSecondRelatedAnchor the second related anchor for this
		 *            anchor.
		 */
		public WebAnchor(WebAnchor pRelatedAnchor, WebAnchor pSecondRelatedAnchor)
		{
			layout = pRelatedAnchor.layout;
			name = Integer.toString(layout.anchorCounter++);
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			secondRelatedAnchor = pSecondRelatedAnchor;
			autoSize = false;
			position = 0;
		}
		
		/**
		 * Constructs a border anchor for this layout.
		 * 
		 * @param pLayout the layout.
		 * @param pOrientation the orientation.
		 * @param pName the anchor name.
		 */
		protected WebAnchor(FormLayout pLayout, int pOrientation, String pName)
		{
			layout = pLayout;
			name = pName;
			orientation = pOrientation;
			relatedAnchor = null;
			secondRelatedAnchor = null;
			autoSize = false;
			position = 0;
			relativePosition = 0.5f;
		}
		
		/**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition
		 * pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pPosition the position relative to the related anchor.
		 * @param pName the anchor name.
		 */
		protected WebAnchor(WebAnchor pRelatedAnchor, int pPosition, String pName)
		{
			layout = pRelatedAnchor.layout;
			name = pName;
			orientation = pRelatedAnchor.orientation;
			relatedAnchor = pRelatedAnchor;
			secondRelatedAnchor = null;
			autoSize = false;
			position = pPosition;
			relativePosition = 0.5f;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the absolute position.
		 *
		 * @return the absolute position.
		 */
		public int getAbsolutePosition()
		{
			if (relatedAnchor == null)
			{
				return position;
			}
			else if (secondRelatedAnchor == null)
			{
				return relatedAnchor.getAbsolutePosition() + position;
			}
			else
			{
				int pos = relatedAnchor.getAbsolutePosition();
				return pos + (int)((secondRelatedAnchor.getAbsolutePosition() - pos) * relativePosition);
			}
		}
		
		/**
		 * Gets this as {@link String}.
		 * 
		 * @return this as {@link String}.
		 */
		public String getAsString()
		{
			String separator = ",";
			
			StringBuilder data = new StringBuilder(24);
			
			data.append(name).append(separator);
			
			if (relatedAnchor != null)
			{
				data.append(relatedAnchor.name);
			}
			else
			{
				data.append("-");
			}
			data.append(",");
			
			if (secondRelatedAnchor != null)
			{
				data.append(secondRelatedAnchor.name);
			}
			else
			{
				data.append("-");
			}
			data.append(",");
			
			data.append(Boolean.toString(autoSize)).append(separator);
			data.append(BigDecimal.valueOf(position).toString()).append(separator);
			data.append(BigDecimal.valueOf(relativePosition).toString()).append(separator);
			
			if (orientation == HORIZONTAL)
			{
				data.append("H");
			}
			else
			{
				data.append("V");
			}
			
			return data.toString();
		}
		
		/**
		 * Gets the {@link WebAnchor border anchor}.
		 *
		 * @return the {@link WebAnchor border anchor}.
		 */
		public WebAnchor getBorderAnchor()
		{
			WebAnchor borderAnchor = this;
			while (borderAnchor.relatedAnchor != null)
			{
				borderAnchor = borderAnchor.relatedAnchor;
			}
			return borderAnchor;
		}
		
		/**
		 * Gets the {@link FormLayout layout}.
		 *
		 * @return the {@link FormLayout layout}.
		 */
		public FormLayout getLayout()
		{
			return layout;
		}
		
		/**
		 * Gets the orientation.
		 *
		 * @return the orientation.
		 */
		public int getOrientation()
		{
			return orientation;
		}
		
		/**
		 * Gets the position.
		 *
		 * @return the position.
		 */
		public int getPosition()
		{
			return position;
		}
		
		/**
		 * Gets the {@link WebAnchor related anchor}.
		 *
		 * @return the {@link WebAnchor related anchor}.
		 */
		public WebAnchor getRelatedAnchor()
		{
			return relatedAnchor;
		}
		
		/**
		 * true, if pRelatedAnchor has a cycle reference to this anchor.
		 *
		 * @param pRelatedAnchor the relatedAnchor to set.
		 * @return true, if pRelatedAnchor has a cycle reference to this anchor.
		 */
		private boolean hasCycleReference(WebAnchor pRelatedAnchor)
		{
			do
			{
				if (pRelatedAnchor == this)
				{
					return true;
				}
				pRelatedAnchor = pRelatedAnchor.relatedAnchor;
			}
			while (pRelatedAnchor != null);
			
			return false;
		}
		
		/**
		 * Sets the related Anchor.
		 * It is only allowed to choose an anchor with same orientation from the same layout.
		 *
		 * @param pRelatedAnchor the relatedAnchor to set.
		 */
		public void setRelatedAnchor(WebAnchor pRelatedAnchor)
		{
			if (layout != pRelatedAnchor.layout || orientation != pRelatedAnchor.orientation)
			{
				throw new IllegalArgumentException("The related anchor must have the same layout and the same orientation!");
			}
			else if (hasCycleReference(pRelatedAnchor))
			{
				throw new IllegalArgumentException("The related anchor has a cycle reference to this anchor!");
			}
			else
			{
				relatedAnchor = pRelatedAnchor;
				
				layout.notifyParent();
			}
		}
		
		/**
		 * Checks if is auto size.
		 *
		 * @return {@code true} if is auto size
		 */
		public boolean isAutoSize()
		{
			return autoSize;
		}
		
		/**
		 * Sets the auto size.
		 *
		 * @param pAutoSize the new auto size.
		 */
		public void setAutoSize(boolean pAutoSize)
		{
			autoSize = pAutoSize;
			
			layout.notifyParent();
		}
		
		/**
		 * Sets the position.
		 *
		 * @param pPosition the new position.
		 */
		public void setPosition(int pPosition)
		{
			position = pPosition;
			
			layout.notifyParent();
		}
		
	}	// WebAnchor
	
	/**
	 * The <code>WebConstraints</code> stores the top, left, bottom and right
	 * Anchor for layouting a component.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class WebConstraint
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The bottom anchor. */
		private WebAnchor bottomAnchor;
		
		/** The left anchor. */
		private WebAnchor leftAnchor;
		
		/** The right anchor. */
		private WebAnchor rightAnchor;
		
		/** The top anchor. */
		private WebAnchor topAnchor;
		
		/** The layout. */
		private FormLayout layout;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates the default constraints for the given anchors.
		 * 
		 * @param pLayout the layout.
		 * @param pTopAnchor the top anchor.
		 * @param pLeftAnchor the left anchor.
		 * @param pBottomAnchor the bottom anchor.
		 * @param pRightAnchor the right anchor.
		 */
		public WebConstraint(FormLayout pLayout, WebAnchor pTopAnchor, WebAnchor pLeftAnchor, WebAnchor pBottomAnchor, WebAnchor pRightAnchor)
		{
			layout = pLayout;
			
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
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the this as string.
		 *
		 * @return the this as string.
		 */
		public String getAsString()
		{
			return topAnchor.name + ";" + leftAnchor.name + ";" + bottomAnchor.name + ";" + rightAnchor.name;
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
		 * Returns the left anchor.
		 *
		 * @return the left anchor.
		 */
		public WebAnchor getLeftAnchor()
		{
			return leftAnchor;
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
		 * Returns the top anchor.
		 *
		 * @return the top anchor.
		 */
		public WebAnchor getTopAnchor()
		{
			return topAnchor;
		}
		
		/**
		 * Sets the bottom anchor.
		 *
		 * @param pBottomAnchor the bottom to set
		 */
		public void setBottomAnchor(WebAnchor pBottomAnchor)
		{
			if (pBottomAnchor == null && topAnchor != null)
			{
				bottomAnchor = new WebAnchor(topAnchor);
			}
			else if (pBottomAnchor.getOrientation() == WebAnchor.HORIZONTAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as bottom anchor!");
			}
			else
			{
				bottomAnchor = (WebAnchor)pBottomAnchor;
			}
			
			layout.notifyParent();
		}
		
		/**
		 * Sets the left anchor.
		 *
		 * @param pLeftAnchor left to set
		 */
		public void setLeftAnchor(WebAnchor pLeftAnchor)
		{
			if (pLeftAnchor == null && rightAnchor != null)
			{
				leftAnchor = new WebAnchor(rightAnchor);
			}
			else if (pLeftAnchor.getOrientation() == WebAnchor.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as left anchor!");
			}
			else
			{
				leftAnchor = (WebAnchor)pLeftAnchor;
			}
			
			layout.notifyParent();
		}
		
		/**
		 * Sets the right anchor.
		 *
		 * @param pRightAnchor the right anchor.
		 */
		public void setRightAnchor(WebAnchor pRightAnchor)
		{
			if (pRightAnchor == null && leftAnchor != null)
			{
				rightAnchor = new WebAnchor(leftAnchor);
			}
			else if (pRightAnchor.getOrientation() == WebAnchor.VERTICAL)
			{
				throw new IllegalArgumentException("A vertical anchor can not be used as right anchor!");
			}
			else
			{
				rightAnchor = (WebAnchor)pRightAnchor;
			}
			
			layout.notifyParent();
		}
		
		/**
		 * Sets the top anchor.
		 *
		 * @param pTopAnchor the top anchor
		 */
		public void setTopAnchor(WebAnchor pTopAnchor)
		{
			if (pTopAnchor == null && bottomAnchor != null)
			{
				topAnchor = new WebAnchor(bottomAnchor);
			}
			else if (pTopAnchor.getOrientation() == WebAnchor.HORIZONTAL)
			{
				throw new IllegalArgumentException("A horizontal anchor can not be used as top anchor!");
			}
			else
			{
				topAnchor = (WebAnchor)pTopAnchor;
			}
			
			layout.notifyParent();
		}
		
	}	// WebConstraints
	
}	// FormLayout
