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
 * 27.02.2015 - [JR] - creation
 */
package javax.rad.ui.event.type.window;

import java.io.Serializable;

import javax.rad.ui.event.UIWindowEvent;

/**
 * Platform and technology independent window closed listener definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * @see    java.awt.event.WindowListener
 */
public interface IWindowClosedListener extends Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     * 
     * @param pEvent the window event.
	 * @throws Throwable if there is an error.
     */
    public void windowClosed(UIWindowEvent pEvent) throws Throwable;

}	// IWindowClosedListener
