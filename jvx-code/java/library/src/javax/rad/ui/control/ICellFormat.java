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
 * 25.01.2009 - [HM] - creation
 * 31.05.2016 - [JR] - #1564: style property support
 */
package javax.rad.ui.control;

import javax.rad.ui.IColor;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.IResource;
import javax.rad.ui.Style;

/**
 * Platform and technology independent CellFormat definition. It is designed for
 * use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface ICellFormat extends IResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the background {@link IColor}.
	 * 
	 * @return the background {@link IColor}.
	 */
	public IColor getBackground();
	
	/**
	 * Gets the foreground {@link IColor}.
	 * 
	 * @return the foreground {@link IColor}.
	 */
	public IColor getForeground();
	
	/**
	 * Gets the {@link IFont}.
	 * 
	 * @return the {@link IFont}.
	 */
	public IFont getFont();
	
	/**
	 * Gets the {@link IImage}.
	 * 
	 * @return the {@link IImage}.
	 */
	public IImage getImage();
	
	/**
	 * Gets the {@link Style style definition}.
	 * 
	 * @return the {@link Style style definition}.
	 */
	public Style getStyle();
	
	/**
	 * Gets the left indentation value.
	 * 
	 * @return the left indentation value.
	 */
	public int getLeftIndent();
	
}	// ICellFormat
