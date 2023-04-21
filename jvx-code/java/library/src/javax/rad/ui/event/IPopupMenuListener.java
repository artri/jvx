/*
 * Copyright 2014 SIB Visions GmbH
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
 * 17.07.2014 - [HM] - creation
 */
package javax.rad.ui.event;

import javax.rad.ui.event.type.popupmenu.IPopupMenuCanceledListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeInvisibleListener;
import javax.rad.ui.event.type.popupmenu.IPopupMenuWillBecomeVisibleListener;

/**
 * Platform and technology independent popup menu listener definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IPopupMenuListener extends IPopupMenuWillBecomeVisibleListener,
                                            IPopupMenuWillBecomeInvisibleListener,
                                            IPopupMenuCanceledListener
{
}	// IPopupMenuListener
