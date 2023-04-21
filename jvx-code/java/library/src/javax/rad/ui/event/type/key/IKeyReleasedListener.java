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
 * 26.02.2015 - [JR] - creation
 */
package javax.rad.ui.event.type.key;

import java.io.Serializable;

import javax.rad.ui.event.UIKeyEvent;

/**
 * Platform and technology independent key released listener definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 * @see    java.awt.event.KeyListener
 */
public interface IKeyReleasedListener extends Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invoked when a key has been released.
     * See the class description for {@link java.awt.event.KeyEvent} for a definition of 
     * a key released event.
     * @param pEvent the key event.
	 * @throws Throwable if there is an error.
     */
    public void keyReleased(UIKeyEvent pEvent) throws Throwable;

}	// IKeyReleasedListener
