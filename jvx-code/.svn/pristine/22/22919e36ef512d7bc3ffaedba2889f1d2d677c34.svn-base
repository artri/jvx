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
 * 21.10.2008 - [HM] - creation
 * 31.03.2011 - [JR] - #161: forward translation changes to date picker
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

import javax.rad.util.TranslationMap;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JFormattedTextField;

import com.sibvisions.rad.ui.swing.ext.text.DateFormatter;
import com.sibvisions.util.type.DateUtil;

/**
 * The <code>JVxDateCombo</code> handles DateEditor with CalendarPane.
 * 
 * @author Martin Handsteiner
 */
public class JVxDateCombo extends JVxComboBase
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** Reference date for size. */
    private static final Date REFERENCE_DATE = DateUtil.getTimestamp(2022, 9, 22);
    
	/** The date formatter used for formatting the date. */
	private DateFormatter dateFormatter = new DateFormatter();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a JVxDateCombo.
	 */
	public JVxDateCombo()
	{
		this(null);
	}
	
	/**
	 * Constructs a JVxDateCombo.
	 * 
	 * @param pDateFormat the date format for the editor component
	 */
	public JVxDateCombo(String pDateFormat)
	{
		setModel(new DateComboBoxModel(this));
		setEditorComponent(new JFormattedTextField(dateFormatter));
		setDateFormat(pDateFormat);
		setForceFocusOnPopup(true);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void setTranslation(TranslationMap pTranslation)
    {
    	super.setTranslation(pTranslation);
    	
    	((DateComboBoxModel)getModel()).getCalendarPane().setTranslation(pTranslation);
    }	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the date format used for editing the date.
	 * 
	 * @return the date format
	 */
	public String getDateFormat()
	{
		return dateFormatter.getDatePattern();
	}

	/**
	 * Sets the date format used for editing the date.
	 * 
	 * @param pDateFormat the date format
	 */
	public void setDateFormat(String pDateFormat)
	{
		dateFormatter.setDatePattern(pDateFormat);
		
        String pattern = dateFormatter.getDatePattern();

        try
        {
            String testDate = dateFormatter.valueToString(REFERENCE_DATE);

            JFormattedTextField textField = (JFormattedTextField)getEditorComponent();
            FontMetrics metrics = textField.getFontMetrics(textField.getFont());
            int columnWidth = metrics.charWidth('m');
            Insets m = textField.getMargin();
            
            textField.setColumns((metrics.stringWidth(testDate) + m.left + m.right + columnWidth - 1) / columnWidth);
        }
        catch (ParseException e)
        {
            // ignore
        }
        
		setButtonEnabled(pattern.contains("y") || pattern.contains("M") || pattern.contains("d") || pattern.contains("D"));
		((DateComboBoxModel)getModel()).getCalendarPane().setTimeVisible(
				pattern.contains("h") || pattern.contains("H") || pattern.contains("k") || pattern.contains("K") || pattern.contains("m") || pattern.contains("s"));
		((DateComboBoxModel)getModel()).getCalendarPane().setShowSeconds(pattern.contains("s"));
		((DateComboBoxModel)getModel()).getCalendarPane().setShowAmPm(pattern.contains("a"));
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>DateComboBoxModel</code> is a generic ComboBoxModel 
	 * implementation. 
	 * 
	 * @author Martin Handsteiner
	 */
	public static class DateComboBoxModel extends AbstractListModel 
										  implements ComboBoxModel, 
										             ActionListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The JVxComboBase that uses this Model. */
		private JVxComboBase comboBase;
		
		/** The CalendarPane shown in the Popup. */
		private JVxCalendarPane calendarPane;
		
		/** The original selected item. */
		private Object origSelectedItem;
		/** The original selected item. */
		private Object generatedSelectedItem;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Constructs an ComboBoxModel.
		 * @param pDateCombo the JVxComboBase.
		 */
		public DateComboBoxModel(JVxComboBase pDateCombo)
		{
			comboBase = pDateCombo;
			
			calendarPane = new JVxCalendarPane();
			calendarPane.addActionListener(this);
			
			comboBase.setPopupComponent(calendarPane);
			
			origSelectedItem = null;
			generatedSelectedItem = calendarPane.getDate();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		public Object getSelectedItem()
		{
			Object date = calendarPane.getDate();
			if (date.equals(generatedSelectedItem))
			{
				return origSelectedItem;
			}
			else
			{
				return calendarPane.getDate();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void setSelectedItem(Object pItem)
		{
			calendarPane.setDate((Date)pItem);
			
			origSelectedItem = pItem;
			generatedSelectedItem = calendarPane.getDate();
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getElementAt(int index)
		{
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public int getSize()
		{
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		public void actionPerformed(ActionEvent pActionEvent)
		{
			generatedSelectedItem = origSelectedItem;
			comboBase.setPopupVisible(false);
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the internal calendar pane.
		 * 
		 * @return the calendar pane
		 */
		public JVxCalendarPane getCalendarPane()
		{
			return calendarPane;
		}
		
	}	// DateComboBoxModel
	
}	// JVxDateCombo
