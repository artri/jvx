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
package javax.rad.ui;

/**
 * Platform and technology independent layout definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @param <CO> type of the constraints.
 * 
 * @author Martin Handsteiner
 */
public interface ILayout<CO> extends IResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the constraints for the specified <code>IComponent</code>.
	 *
	 * @param pComp the <code>IComponent</code> to be queried
	 * @return the constraint for the specified <code>IComponent</code>,
	 *         or null if component is null or is not present
	 *         in this layout
	 */
	public CO getConstraints(IComponent pComp);

	/**
	 * Sets the constraints for the specified <code>IComponent</code>.
	 *
	 * @param pComp the <code>IComponent</code>
	 * @param pConstraints the new constraints for the specified <code>IComponent</code>.
	 */
	public void setConstraints(IComponent pComp, CO pConstraints);

    /**
     * Gets the margins.
     * 
     * @return the margins.
     */
    public IInsets getMargins(); 
    
    /**
     * Sets the margins.
     * 
     * @param pMargins the margins.
     */
    public void setMargins(IInsets pMargins); 
	
    /**
     * Returns the horizontal gap between components.
     * 
     * @return returns the horizontal gap between components.
     */
    public int getHorizontalGap();

    /**
     * Sets the horizontal gap between components.
     * 
     * @param pHorizontalGap the horizontal gap between components.
     */
    public void setHorizontalGap(int pHorizontalGap);

    /**
     * Returns the vertical gap between components.
     * 
     * @return returns the vertical gap between components.
     */
    public int getVerticalGap();

    /**
     * Sets the vertical gap between components.
     * 
     * @param pVerticalGap the vertical gap between components
     */
    public void setVerticalGap(int pVerticalGap);
    
}	// ILayout
