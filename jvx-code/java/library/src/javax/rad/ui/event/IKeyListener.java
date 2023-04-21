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
 * 20.04.2008 - [HM] - creation
 * 26.02.2015 - [JR] - extends key typed/pressed/released listener
 */
package javax.rad.ui.event;

import javax.rad.ui.event.type.key.IKeyPressedListener;
import javax.rad.ui.event.type.key.IKeyReleasedListener;
import javax.rad.ui.event.type.key.IKeyTypedListener;

/**
 * Platform and technology independent key listener definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see    java.awt.event.KeyListener
 */
public interface IKeyListener extends IKeyTypedListener,
                                      IKeyPressedListener,
                                      IKeyReleasedListener
{
}	// IKeyListener
