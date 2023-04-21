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
 * 27.11.2008 - [HM] - creation
 */
package javax.rad.ui.container;

import javax.rad.ui.IContainer;
import javax.rad.ui.component.ILabel;

/**
 * Platform and technology independent GroupPanel definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Panel
 * @see	javax.swing.JPanel
 */
public interface IGroupPanel extends IContainer, ILabel
{
}	// IGroupPanel
