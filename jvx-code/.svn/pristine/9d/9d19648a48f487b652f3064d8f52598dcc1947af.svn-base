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
 * 19.02.2009 - [JR] - keep blocking removed
 */
package javax.rad.ui.container;


/**
 * Platform and technology independent InternalFrame definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	javax.swing.JInternalFrame
 */
public interface IInternalFrame extends IFrame
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Sets whether this internal frame can be maximized by some user action.
     * 
     * @param pMaximizable a boolean value, where <code>true</code> means 
     *                     this internal frame can be maximized 
	 */
	public void setMaximizable(boolean pMaximizable);
	
	/**
	 * Returns whether this internal frame can be maximized by some user action.
	 * 
	 * @return <code>true</code> if this internal frame can be maximized
	 */
	public boolean isMaximizable();

	/**
     * Sets whether this internal frame can be closed by some user action.
     * 
     * @param pClosable a boolean value, where <code>true</code> means 
     *                  this internal frame can be closed 
	 */
	public void setClosable(boolean pClosable);
	
    /** 
     * Returns whether this internal frame can be closed by some user action.
     * 
     * @return <code>true</code> if this internal frame can be closed
     */
	public boolean isClosable();

	/**
     * Sets whether this internal frame can be iconified by some user action.
     * 
     * @param pIconifiable a boolean value, where <code>true</code> means 
     *                     this internal frame can be iconified 
	 */
	public void setIconifiable(boolean pIconifiable);
	
    /** 
     * Returns whether this internal frame can be iconified by some user action.
     * 
     * @return <code>true</code> if this internal frame can be iconified
     */
	public boolean isIconifiable();

    /** 
     * Closes this internal frame. If the internal frame is already closed,
     * this method does nothing and returns immediately. 
     * Otherwise, this method begins by firing an <code>windowClosing</code> event.
     * This method finishes by making the internal frame invisible and unselected, 
     * and then firing an <code>windowClosed</code> event.
     * 
     * @see #isClosed()
     * @see #dispose
     */
	public void close();
	
    /** 
     * Returns whether this <code>IInternalFrame</code> is currently closed.
     *  
     * @return <code>true</code> if this internal frame is closed, <code>false</code> otherwise
     */
	public boolean isClosed();
     
    /**
     * Sets the frame as modal frame. When a modal frame is visible
     * it's not possible to use the underlaying components.
     * 
     * @param pModal <code>true</code> to set this internal frame modal, 
     *               otherwise <code>false</code>
     */
    public void setModal(boolean pModal);
     
    /**
     * Returns the modal option of the internal frame.
     * 
     * @return <code>true</code> if the frame is modal otherwise <code>false</code>
     */
    public boolean isModal();
	
}	// IInternalFrame
