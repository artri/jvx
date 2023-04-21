/*
 * Copyright 2012 SIB Visions GmbH
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
 * 26.03.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import javax.rad.model.ui.ICellEditorHandler;

import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicAttributes;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicCss;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;

/**
 * The <code>IVaadinCellEditorHandler</code> is an interface for cell editor handlers. It adds sizing options
 * for editor components.
 * 
 * @author Stefan Wurm
 *
 * @param <C> the component type
 */
public interface IVaadinCellEditorHandler<C extends Component> extends ICellEditorHandler<C>,
                                                                       IDynamicCss,
                                                                       IDynamicAttributes
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Sets the width for the the editor.
	 * An editor consists of Panel, Panel, Component like TextField, DateField, ....
	 * 
	 * @param width the width
	 * @param unit the unit: PIXELS, PERCENTAGE
	 */
	public void setWidth(float width, Unit unit);
	
	/**
	 * Sets the height for the the editor.
	 * An editor consists of Panel, Panel, Component like TextField, DateField, ....
	 * 
	 * @param height the height
	 * @param unit the unit: PIXELS, PERCENTAGE
	 */
	public void setHeight(float height, Unit unit);
	
}	// IVaadinCellEditorHandler
