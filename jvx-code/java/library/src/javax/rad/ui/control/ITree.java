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
 * 02.03.2009 - [HM] - creation
 */
package javax.rad.ui.control;

import javax.rad.model.ui.ITreeControl;
import javax.rad.ui.IComponent;
import javax.rad.ui.component.IEditable;

/**
 * Platform and technology independent tree definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface ITree extends IComponent, 
                               ITreeControl,
                               IEditable,
                               ICellFormatable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets if a node should be detected to be an end node or not.
	 * Depending on the used model, the detection can cause additional client 
	 * server communication. 
	 * 
	 * @return true, if end node detection is enabled.
	 */
	public boolean isDetectEndNode();

	/**
	 * Sets if a node should be detected to be an end node or not.
	 * Depending on the used model, the detection can cause additional client 
	 * server communication. 
	 * 
	 * @param pDetectEndNode true, if end node detection is enabled.
	 */
	public void setDetectEndNode(boolean pDetectEndNode);

    /**
     * Gets the node formatter.
     *
     * @return the node formatter.
     */
	public INodeFormatter getNodeFormatter();
	
    /**
     * Sets the node formatter.
     *
	 * @param pNodeFormatter the node formatter.
     */
	public void setNodeFormatter(INodeFormatter pNodeFormatter);

	/**
	 * True, if the mouse event occured on current selected cell.
	 * 
	 * @return True, if the mouse event occured on current selected cell.
	 */
	public boolean isMouseEventOnSelectedCell();

}	// ITree
