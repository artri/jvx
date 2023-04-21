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
 * 05.11.2008 - [HM] - creation
 */
package javax.rad.ui.celleditor;

import java.util.Locale;

/**
 * Platform and technology independent number editor definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface INumberCellEditor extends IInplaceCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the number format used for editing the number.
	 * 
	 * @return the number format
	 */
	public String getNumberFormat();

	/**
	 * Sets the number format used for editing the number.
	 * 
	 * @param pNumberFormat the number format
	 */
	public void setNumberFormat(String pNumberFormat);

    /**
     * Gets the locale used for editing the date, null means the default locale.
     * 
     * @return the locale
     */
    public Locale getLocale();

    /**
     * Sets the locale used for editing the date, null means the default locale.
     * 
     * @param pLocale the locale
     */
    public void setLocale(Locale pLocale);

}	// INumberCellEditor
