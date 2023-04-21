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
 * Platform and technology independent Container definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Container
 */
public interface IContainer extends IComponent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/* 
	 * Properties for Layouting
	 */

	/** 
	 * Gets the layout manager for this container.
	 * 
	 * @return the <code>ILayout</code>
	 * @see ILayout
	 * @see #setLayout
	 */
	public ILayout getLayout();

	/** 
	 * Sets the layout manager for this container.
	 * 
	 * @param pLayout the specified layout manager
	 * @see ILayout
	 * @see #getLayout
	 */
	public void setLayout(ILayout pLayout);

	/* 
	 * Operational Properties and Functions 
	 */

	/**
	 * Adds the specified <code>IComponent</code> to the end of this container.
	 *
	 * @param pComponent the <code>IComponent</code> to be added
	 * @throws IllegalArgumentException if the component can not be added
	 * @see IComponent
	 */
	public void add(IComponent pComponent);

	/**
	 * Adds the specified <code>IComponent</code> to the end of this container.
	 * Also notifies the layout manager to add the component to 
	 * this container's layout using the specified constraints object.
	 *
	 * @param pComponent the <code>IComponent</code> to be added
	 * @param pConstraints an object expressing 
	 *        layout contraints for this component
	 * @throws IllegalArgumentException if the component can not be added
	 * @see IComponent
	 */
	public void add(IComponent pComponent, Object pConstraints);

	/**
	 * Adds the specified <code>IComponent</code> to this container at the specified index.  Also notifies the layout 
	 * manager to add the component to the this container's layout.
	 *
	 * @param pComponent the <code>IComponent</code> to be added
	 * @param pIndex the position in the container's list at which to insert
	 *        the <code>IComponent</code>; <code>-1</code> means insert at the end
	 *        component
	 * @throws IllegalArgumentException if the component can not be added
	 * @see IComponent
	 */
	public void add(IComponent pComponent, int pIndex);

	/**
	 * Adds the specified <code>IComponent</code> to this container with the specified
	 * constraints at the specified index.  Also notifies the layout 
	 * manager to add the component to the this container's layout using 
	 * the specified constraints object.
	 *
	 * @param pComponent the <code>IComponent</code> to be added
	 * @param pConstraints an object expressing layout contraints for this
	 * @param pIndex the position in the container's list at which to insert
	 *        the <code>IComponent</code>; <code>-1</code> means insert at the end
	 *        component
	 * @throws IllegalArgumentException if the component can not be added
	 * @see IComponent
	 */
	public void add(IComponent pComponent, Object pConstraints, int pIndex);

	/** 
	 * Removes the <code>IComponent</code>, specified by <code>index</code>, 
	 * from this container. 
	 * This method also notifies the layout manager to remove the
	 * component from this container's layout via the
	 * <code>removeLayoutComponent</code> method.
	 *
	 * @param pIndex the index of the <code>IComponent</code> to be removed
	 * @see #add
	 * @see #getComponentCount
	 */
	public void remove(int pIndex);

	/** 
	 * Removes the specified component from this container.
	 *
	 * @param pComponent the <code>IComponent</code> to be removed
	 * @see #add
	 * @see #remove
	 */
	public void remove(IComponent pComponent);

	/** 
	 * Removes all the components from this container.
	 * This method also notifies the layout manager to remove the
	 * components from this container's layout via the
	 * <code>removeLayoutComponent</code> method.
	 * @see #add
	 * @see #remove
	 */
	public void removeAll();

	/** 
	 * Gets the number of <code>IComponent</code>s in this panel.
	 * 
	 * @return the number of components in this panel.
	 * @see #getComponent
	 */
	public int getComponentCount();

	/** 
	 * Gets the n<sup>th</sup> <code>IComponent</code> in this container.
	 * 
	 * @param pIndex the index of the <code>IComponent</code> to get.
	 * @return the n<sup>th</sup> component in this container.
	 */
	public IComponent getComponent(int pIndex);
	
	/**
	 * Gets the n<sup>th</sup> position of an <code>IComponent</code> in this container.
	 * 
	 * @param pComponent the <code>IComponent</code> to search
	 * @return the n<sup>th</sup> position of <code>pComponent</code> in this container or
	 *         <code>-1</code> if <code>pComponent</code> is not added
	 */
	public int indexOf(IComponent pComponent);

}	// IContainer
