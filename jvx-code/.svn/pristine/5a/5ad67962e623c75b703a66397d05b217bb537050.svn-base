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
 * 17.11.2008 - [HM] - creation
 * 24.10.2012 - [JR] - #604: added constructor
 */
package javax.rad.genui.celleditor;

import java.util.Locale;
import java.util.TimeZone;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.celleditor.IDateCellEditor;

/**
 * Platform and technology independent date editor.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public class UIDateCellEditor extends UIComboCellEditor<IDateCellEditor> 
                              implements IDateCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIDateCellEditor</code>.
     *
     * @see IDateCellEditor
     */
	public UIDateCellEditor()
	{
		super(UIFactoryManager.getFactory().createDateCellEditor());
	}

    /**
     * Creates a new instance of <code>UIDateCellEditor</code> with the given
     * date cell editor.
     *
     * @param pEditor the date cell editor
     * @see IDateCellEditor
     */
	protected UIDateCellEditor(IDateCellEditor pEditor)
	{
		super(pEditor);
	}
	
    /**
     * Creates a new instance of <code>UIDateCellEditor</code> with the given format.
     *
     * @param pDateFormat then DateFormat.
     * @see IDateCellEditor
     */
	public UIDateCellEditor(String pDateFormat)
	{
		super(UIFactoryManager.getFactory().createDateCellEditor());
		
		getUIResource().setDateFormat(pDateFormat);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getDateFormat()
	{
		return getUIResource().getDateFormat();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDateFormat(String pDateFormat)
	{
		getUIResource().setDateFormat(pDateFormat);
	}

    /**
     * {@inheritDoc}
     */
    public TimeZone getTimeZone()
    {
        return getUIResource().getTimeZone();
    }

    /**
     * {@inheritDoc}
     */
    public void setTimeZone(TimeZone pTimeZone)
    {
        getUIResource().setTimeZone(pTimeZone);
    }

    /**
     * {@inheritDoc}
     */
    public Locale getLocale()
    {
        return getUIResource().getLocale();
    }

    /**
     * {@inheritDoc}
     */
    public void setLocale(Locale pLocale)
    {
        getUIResource().setLocale(pLocale);
    }

}	// UIDateCellEditor
