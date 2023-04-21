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
import java.util.TimeZone;

/**
 * Platform and technology independent date editor definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IDateCellEditor extends IComboCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the date format used for editing the date, null means the default date format.
	 * 
	 * @return the date format
	 */
	public String getDateFormat();

	/**
	 * Sets the date format used for editing the date, null means the default date format.
	 * 
	 * @param pDateFormat the date format
	 */
	public void setDateFormat(String pDateFormat);

    /**
     * Gets the time zone used for editing the date, null means the default time zone.
     * 
     * @return the time zone
     */
    public TimeZone getTimeZone();

    /**
     * Sets the time zone used for editing the date, null means the default time zone.
     * 
     * @param pTimeZone the time zone
     */
    public void setTimeZone(TimeZone pTimeZone);

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

}	// IDateCellEditor
