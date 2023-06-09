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
 * 03.10.2008 - [HM] - creation
 */
package javax.rad.ui.layout;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.ILayout;
import javax.rad.ui.IResource;

/**
 * Platform and technology independent form oriented layout definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public interface IFormLayout extends ILayout<IFormLayout.IConstraints>, 
                                     IAlignmentConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Constraint for starting a new row for the given component. */
	public static final IConstraints NEWLINE = new IConstraints()
    {
        public Object getResource()
        {
            return "\n";
        }
        
        public void setTopAnchor(IAnchor pTopAnchor)
        {
        }
        
        public void setRightAnchor(IAnchor pRightAnchor)
        {
        }
        
        public void setLeftAnchor(IAnchor pLeftAnchor)
        {
        }
        
        public void setBottomAnchor(IAnchor pBottomAnchor)
        {
        }
        
        public IAnchor getTopAnchor()
        {
            return null;
        }
        
        public IAnchor getRightAnchor()
        {
            return null;
        }
        
        public IAnchor getLeftAnchor()
        {
            return null;
        }
        
        public IAnchor getBottomAnchor()
        {
            return null;
        }
    };
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the new line count.
     * 
     * @return the new line count.
     */
    public int getNewlineCount();
    
    /**
     * Sets the new line count.
     * 
     * @param pNewlineCount the new line count.
     */
    public void setNewlineCount(int pNewlineCount); 

	/**
	 * Constructs an anchor relative to pRelatedAnchor auto sized.
	 * 
	 * @param pRelatedAnchor the related anchor for this anchor.
	 * @return the anchor.
	 */
	public IAnchor createAnchor(IAnchor pRelatedAnchor);

	/**
	 * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
	 * 
	 * @param pRelatedAnchor the related anchor for this anchor.
	 * @param pPosition the position relative to the related anchor.
	 * @return the anchor.
	 */
	public IAnchor createAnchor(IAnchor pRelatedAnchor, int pPosition);
	
	/**
	 * Returns the left border anchor.
	 *
	 * @return the left border anchor.
	 */
	public IAnchor getLeftAnchor();
	
	/**
	 * Returns the right border anchor.
	 *
	 * @return the right border anchor.
	 */
	public IAnchor getRightAnchor();
	
	/**
	 * Returns the top border anchor.
	 *
	 * @return the top border anchor.
	 */
	public IAnchor getTopAnchor();
	
	/**
	 * Returns the bottom border anchor.
	 *
	 * @return the bottom border anchor.
	 */
	public IAnchor getBottomAnchor();
	
	/**
	 * Returns the left margin border anchor.
	 *
	 * @return the left margin border anchor.
	 */
	public IAnchor getLeftMarginAnchor();
	
	/**
	 * Returns the right margin border anchor.
	 *
	 * @return the right margin border anchor.
	 */
	public IAnchor getRightMarginAnchor();
	
	/**
	 * Returns the top margin border anchor.
	 *
	 * @return the top margin border anchor.
	 */
	public IAnchor getTopMarginAnchor();
	
	/**
	 * Returns the bottom margin border anchor.
	 *
	 * @return the bottom margin border anchor.
	 */
	public IAnchor getBottomMarginAnchor();

	/**
	 * Returns all horizontal anchors used by this layout.
	 *
	 * @return all horizontal anchors used by this layout.
	 */
	public IAnchor[] getHorizontalAnchors();
	
	/**
	 * Returns all vertical anchors used by this layout.
	 *
	 * @return all vertical anchors used by this layout.
	 */
	public IAnchor[] getVerticalAnchors();
	
	/**
	 * Creates the default constraints for the given column and row.
	 * Negative columns or rows counts the columns from the right or bottom border.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getConstraints(int pColumn, int pRow);
    
	/**
	 * Creates the default constraints for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the column count.
	 * @param pEndRow the row count.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow);
    
	/**
	 * Creates the default constraints for the given anchors.
	 * 
	 * @param pTopAnchor the top anchor.
	 * @param pLeftAnchor the left anchor.
	 * @param pBottomAnchor the bottom anchor.
	 * @param pRightAnchor the right anchor.
	 * @return the constraints for the given Anchors.
	 */
    public IConstraints getConstraints(IAnchor pTopAnchor, IAnchor pLeftAnchor, IAnchor pBottomAnchor, IAnchor pRightAnchor);
    
	//****************************************************************
	// Subinterface definition
	//****************************************************************

    /**
	 * The Anchor gives the possible horizontal and vertical positions.
	 * 
	 * @author Martin Handsteiner
	 */
	public static interface IAnchor extends IResource
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Constants
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** Constant for horizontal anchors. */
		public static final int HORIZONTAL = 0;
		/** Constant for vertical anchors. */
		public static final int VERTICAL   = 1;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Method definitions
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Returns the layout to which this anchor belongs.
		 *
		 * @return the orientation.
		 */
		public IFormLayout getLayout();

		/**
		 * Returns whether the orientation of this Anchor is <code>HORIZONTAL</code> or <code>VERTICAL</code>.
		 *
		 * @return the orientation.
		 */
		public int getOrientation();

		/**
		 * Returns the related Anchor.
		 *
		 * @return the relatedAnchor.
		 */
		public IAnchor getRelatedAnchor();

		/**
		 * Sets the related Anchor.
		 *
		 * @param pRelatedAnchor the relatedAnchor.
		 */
		public void setRelatedAnchor(IAnchor pRelatedAnchor);

		/**
		 * Gets the border anchor.
		 * 
		 * @return the border anchor.
		 */
		public IAnchor getBorderAnchor();

		/**
		 * Returns true, if the position of this anchor is calculated automatically.
		 *
		 * @return the fixed.
		 */
		public boolean isAutoSize();

		/**
		 * Sets, if the position of this anchor is calculated automatically.
		 *
		 * @param pAutoSize the fixed to set.
		 */
		public void setAutoSize(boolean pAutoSize);

		/**
		 * Returns the position of this Anchor.
		 * The position is only correct if the layout is valid.
		 *
		 * @return the position.
		 */
		public int getPosition();

		/**
		 * Sets the position of this Anchor.
		 * It is not allowed to set the position of a border anchor.
		 *
		 * @param pPosition the position to set
		 */
		public void setPosition(int pPosition);

		/**
		 * Returns the absolute position of this Anchor in this FormLayout.
		 * The position is only correct if the layout is valid.
		 *
		 * @return the absolute position.
		 */
		public int getAbsolutePosition();
		
	}	// IAnchor
	
	/**
	 * The Constraint stores the top, left, bottom and right Anchor for layouting a component.
	 * 
	 * @author Martin Handsteiner
	 */
	public static interface IConstraints extends IResource
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Method definitions
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Returns the left anchor.
		 *
		 * @return the left anchor.
		 */
		public IAnchor getLeftAnchor();

		/**
		 * Sets the left anchor.
		 *
		 * @param pLeftAnchor left to set
		 */
		public void setLeftAnchor(IAnchor pLeftAnchor);

		/**
		 * Returns the right anchor.
		 *
		 * @return the right anchor.
		 */
		public IAnchor getRightAnchor();

		/**
		 * Sets the right anchor.
		 *
		 * @param pRightAnchor the right anchor.
		 */
		public void setRightAnchor(IAnchor pRightAnchor);

		/**
		 * Returns the top anchor.
		 *
		 * @return the top anchor.
		 */
		public IAnchor getTopAnchor();

		/**
		 * Sets the top anchor.
		 *
		 * @param pTopAnchor the top anchor
		 */
		public void setTopAnchor(IAnchor pTopAnchor);

		/**
		 * Returns the bottom anchor.
		 *
		 * @return the bottom anchor.
		 */
		public IAnchor getBottomAnchor();

		/**
		 * Sets the bottom anchor.
		 *
		 * @param pBottomAnchor the bottom to set
		 */
		public void setBottomAnchor(IAnchor pBottomAnchor);
	
	}	// IConstraints
    
}	// IFormLayout
