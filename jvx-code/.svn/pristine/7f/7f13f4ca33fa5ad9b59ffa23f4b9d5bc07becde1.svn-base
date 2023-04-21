/*
 * Copyright 2016 SIB Visions GmbH
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
 * 07.03.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.StyleConstants;

/**
 * The {@link VLayoutedPanel} is the main implementation of the new panel.
 * 
 * @author Robert Zenz
 */
public class VLayoutedPanel extends ComplexPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The classname of this panel. */
	public static final String CLASSNAME = "v-jvx-panel";
	
	/** The canvas {@link Element} which hosts all children. */
	private Element canvas = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link VLayoutedPanel}.
	 */
	public VLayoutedPanel()
	{
		setElement(Document.get().createDivElement());
		
		setStyleName(CLASSNAME);
		addStyleName(StyleConstants.UI_LAYOUT);
		
		Style style = getElement().getStyle();
		style.setDisplay(Display.BLOCK);
		
		canvas = Document.get().createDivElement();
		canvas.setClassName(CLASSNAME + "-canvas");
		
		getElement().appendChild(canvas);
		
		Style canvasStyle = canvas.getStyle();
		
		canvasStyle.setPosition(Position.RELATIVE);
		canvasStyle.setProperty("minWidth", "100%");
		canvasStyle.setProperty("minHeight", "100%");
		canvasStyle.setProperty("boxSizing", "border-box");
		canvasStyle.setOverflow(Overflow.HIDDEN);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link Element canvas}.
	 *
	 * @return the {@link Element canvas}.
	 */
	public Element getCanvas()
	{
		return canvas;
	}
	
    /**
     * For internal use only. May be removed or replaced in the future.
     * @param pChild the child
     * @param pIndex the index
     */
    public void addOrMove(Widget pChild, int pIndex) 
    {
        if (pChild.getParent() != this || pIndex != getWidgetIndex(pChild)) 
        {
			Style style = pChild.getElement().getStyle();
			style.setPosition(Position.ABSOLUTE);
			style.setProperty("boxSizing", "border-box");

			if (pIndex == getWidgetCount()) 
        	{
	            // optimized path for appending components - faster especially for
	            // initial rendering
	        	add(pChild, canvas);
        	}
			else
			{
		        insert(pChild, canvas, pIndex, true);
			}
        }
    }

}	// VLayoutedPanel
