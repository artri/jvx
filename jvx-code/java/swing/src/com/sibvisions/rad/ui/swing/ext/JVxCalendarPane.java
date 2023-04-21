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
 * 10.10.2008 - [HM] - creation
 * 31.03.2011 - [JR] - #161: change locale when translation is changed
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.rad.model.ui.ITranslatable;
import javax.rad.util.TranslationMap;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.PanelUI;

import com.sibvisions.rad.ui.swing.ext.layout.JVxGridLayout;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>JVxCalendarPane</code> provides Calendar functionality.
 * 
 * @author Martin Handsteiner
 */
public class JVxCalendarPane extends JPanel 
                             implements ChangeListener, 
                                        PropertyChangeListener,
                                        KeyListener,
                                        MouseListener,
                                        FocusListener,
                                        Runnable,
                                        ITranslatable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Spinner for changing years. */
	private JSpinner years = new JSpinner(new SpinnerDateModel()); //t, null, null, Calendar.MONTH));
	/** Spinner for changing months. */
	private JSpinner months = new JSpinner(new SpinnerDateModel()); //t, null, null, Calendar.MONTH));
	/** Spinner for changing time. */
	private JSpinner currentTime;
	/** Spinner for changing time. */
	private JSpinner timeMinutes = new JSpinner(new SpinnerDateModel()); //t, null, null, Calendar.MONTH));
	/** Spinner for changing time. */
	private JSpinner timeSeconds = new JSpinner(new SpinnerDateModel()); //t, null, null, Calendar.MONTH));
	/** Spinner for changing time. */
	private JSpinner timeMinutes12 = new JSpinner(new SpinnerDateModel()); //t, null, null, Calendar.MONTH));
	/** Spinner for changing time. */
	private JSpinner timeSeconds12 = new JSpinner(new SpinnerDateModel()); //t, null, null, Calendar.MONTH));
	
	/** Spacer left top. */
    private JLabel spacer = new JLabel();
	/** Labels for week days. */
	private JLabel[] weekDays = new JLabel[7];
	/** Labels for weeks. */
	private JLabel[] weeks    = new JLabel[6];
	/** Labels for days. */
	private JToggleButton[] days = new JToggleButton[42];
	
	/** GregorianCalendar for date selection. */
	private GregorianCalendar calendar;

    /** The current selected date and time. */
    private Date date = null;
    
    /** The current actionCommand. */
    private String actionCommand = null;

    /** The translation mapping. */
    private TranslationMap translation = null;

    /** The selected Day. */
	private int selectedIndex = -1;
	
	/** true, if the date changes occurs setting internal state. */
	private boolean ignoreEvent = false;
	
    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * Creates a new CalendarPane with no date selected.
	 */
	public JVxCalendarPane()
	{
		JVxGridLayout northLayout = new JVxGridLayout(3, 1);
		
		JPanel north = new JPanel(northLayout);
		north.setBackground(SystemColor.control);
		north.add(months, northLayout.getConstraints(0, 0));
		north.add(years, northLayout.getConstraints(1, 0));
		north.add(timeSeconds, northLayout.getConstraints(2, 0));
		north.add(timeMinutes, northLayout.getConstraints(2, 0));
		north.add(timeSeconds12, northLayout.getConstraints(2, 0));
		north.add(timeMinutes12, northLayout.getConstraints(2, 0));

		JPanel center = new JPanel(new GridLayout(7, 8));
//		center.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.gray));
		center.setBackground(SystemColor.window);
		
        spacer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.gray));
        spacer.setOpaque(true);
        spacer.setBackground(SystemColor.control);
        spacer.setHorizontalAlignment(SwingConstants.CENTER);
        center.add(spacer);
		for (int i = 0; i < 7; i++)
		{
	        JLabel weekDay = new JLabel();
	        weekDays[i] = weekDay;
	        weekDay.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
	        weekDay.setOpaque(true);
	        weekDay.setBackground(SystemColor.control);
	        weekDay.setHorizontalAlignment(SwingConstants.CENTER);
	        if (i % 7 >= 5) 
	        {
	        	weekDay.setForeground(Color.red);
	        }
	        center.add(weekDay);
		}

	    for (int i = 0; i < 42; i++) 
	    {
	      if (i % 7 == 0) 
	      {
	        JLabel week = new JLabel();
	        weeks[i / 7] = week;
	        week.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.gray));
	        week.setOpaque(true);
	        week.setBackground(SystemColor.control);
	        week.setHorizontalAlignment(SwingConstants.CENTER);
	        center.add(week);
	      }
	      	      
	      JVxToggleButton day = new JVxToggleButton();
	      
	      if (SwingFactory.isMacLaF())
	      {
	    	  //MacOS needs an image to recognize the margins. Otherwise the button is large
	    	  day.setIcon(JVxUtil.getIcon("/com/sibvisions/rad/ui/swing/ext/images/1x1.png"));
		      day.setIconTextGap(0);
	      }
	      
	      day.setMargin(new Insets(1, 4, 1, 4));
	      day.setHorizontalAlignment(SwingConstants.CENTER);
	      day.setBorderOnMouseEntered(true);
	      day.setFocusable(false);
	      day.addMouseListener(this);
	      day.addKeyListener(this);
	      center.add(day);

	      days[i] = day;
	    }

	    setFocusable(true);
	    setLayout(new BorderLayout());
		add(north, BorderLayout.NORTH);
		add(center,  BorderLayout.CENTER);
		
		years.setFont(spacer.getFont());
		months.setFont(spacer.getFont());
		timeSeconds.setFont(spacer.getFont());
		timeMinutes.setFont(spacer.getFont());
		timeSeconds12.setFont(spacer.getFont());
		timeMinutes12.setFont(spacer.getFont());
		
		createDateEditor(months, "MMMM");
		createDateEditor(years, "yyyy");
		createDateEditor(timeSeconds, "HH:mm:ss");
		createDateEditor(timeMinutes, "HH:mm");
		createDateEditor(timeSeconds12, "hh:mm:ss a");
		createDateEditor(timeMinutes12, "hh:mm a");
		
		years.addChangeListener(this);
		months.addChangeListener(this);
		timeSeconds.addChangeListener(this);
		timeMinutes.addChangeListener(this);
		timeSeconds12.addChangeListener(this);
		timeMinutes12.addChangeListener(this);
		
		currentTime = timeMinutes;
		setTimeVisible(true);
		
		setLocaleIntern();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void run()
	{
		for (int i = 0; i < days.length; i++)
		{
			days[i].removeMouseListener(this);
			days[i].addMouseListener(this);
		}
		years.setFont(spacer.getFont());
		months.setFont(spacer.getFont());
		timeSeconds.setFont(spacer.getFont());
		timeMinutes.setFont(spacer.getFont());
		timeSeconds12.setFont(spacer.getFont());
		timeMinutes12.setFont(spacer.getFont());
	}

	/**
	 * {@inheritDoc}
	 */
	public void stateChanged(ChangeEvent pChangeEvent)
	{
		if (!ignoreEvent)
		{
			JSpinner spinner = (JSpinner)pChangeEvent.getSource();
			setDate((Date)spinner.getValue());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void propertyChange(PropertyChangeEvent pPropertyChangeEvent)
	{
		if (!ignoreEvent && "value" == pPropertyChangeEvent.getPropertyName())
		{
			calendar.setTime((Date)pPropertyChangeEvent.getNewValue());
			Object source = pPropertyChangeEvent.getSource();
			if (source == ((JSpinner.DateEditor)currentTime.getEditor()).getTextField())
			{
				int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); 
				int minute = calendar.get(Calendar.MINUTE); 
				int second = calendar.get(Calendar.SECOND); 
				int millisecond = calendar.get(Calendar.MILLISECOND); 
				calendar.setTime(date);
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.SECOND, second);
				calendar.set(Calendar.MILLISECOND, millisecond);
			}
			else if (source == ((JSpinner.DateEditor)months.getEditor()).getTextField())
			{
				int month = calendar.get(Calendar.MONTH); 
				calendar.setTime(date);
				calendar.set(Calendar.MONTH, month);
			}
			else
			{
				int year = calendar.get(Calendar.YEAR); 
				calendar.setTime(date);
				calendar.set(Calendar.YEAR, year);
			}
			setDate(calendar.getTime());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(KeyEvent pKeyEvent)
	{
		if (pKeyEvent.getSource() instanceof JVxToggleButton)
		{
			switch (pKeyEvent.getKeyCode())
			{
				case KeyEvent.VK_PAGE_UP:
					calendar.setTime(date);
					calendar.add(Calendar.MONTH, -1);
					setDate(calendar.getTime());
					days[selectedIndex].requestFocus();
					break;
				case KeyEvent.VK_PAGE_DOWN:
					calendar.setTime(date);
					calendar.add(Calendar.MONTH, 1);
					setDate(calendar.getTime());	
					days[selectedIndex].requestFocus();
					break;
				case KeyEvent.VK_UP:
					calendar.setTime(date);
					calendar.add(Calendar.DATE, -7);
					setDate(calendar.getTime());	
					days[selectedIndex].requestFocus();
					break;
				case KeyEvent.VK_DOWN:
					calendar.setTime(date);
					calendar.add(Calendar.DATE, 7);
					setDate(calendar.getTime());	
					days[selectedIndex].requestFocus();
					break;
				case KeyEvent.VK_LEFT:
					calendar.setTime(date);
					calendar.add(Calendar.DATE, -1);
					setDate(calendar.getTime());				
					days[selectedIndex].requestFocus();
					break;
				case KeyEvent.VK_RIGHT:
					calendar.setTime(date);
					calendar.add(Calendar.DATE, 1);
					setDate(calendar.getTime());				
					days[selectedIndex].requestFocus();
					break;
				default:
					// do nothing
			}
		}
		switch (pKeyEvent.getKeyCode())
		{
			case KeyEvent.VK_ESCAPE:
				Container parent = getParent();
				while (parent != null && !(parent instanceof JPopupMenu))
				{
					parent = parent.getParent();
				}
				if (parent instanceof JPopupMenu)
				{
					JPopupMenu popupMenu = (JPopupMenu)parent;
					
					popupMenu.putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.TRUE);
					popupMenu.setVisible(false);
				}
			    break;
			default:
				// do nothing
		}
		
//		validate();
		repaint();
	}
	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(KeyEvent pKeyEvent)
	{
	}
	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(KeyEvent pKeyEvent)
	{
		switch (pKeyEvent.getKeyChar())
		{
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_SPACE:
				processActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand(), pKeyEvent.getWhen(), pKeyEvent.getModifiers()));
			    break;
			default:
				// do nothing
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(MouseEvent pMouseEvent)
	{
	}
	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(MouseEvent pMouseEvent)
	{
	}
	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent pMouseEvent)
	{
	}
	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent pMouseEvent)
	{
	}
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent pMouseEvent)
	{
		int index = ArrayUtil.indexOf(days, pMouseEvent.getSource());
		if (selectedIndex == index)
		{
			days[index].setSelected(true);
			processActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand(), pMouseEvent.getWhen(), pMouseEvent.getModifiers()));
		}
		else
		{
			calendar.setTime(date);
			calendar.add(Calendar.DATE, index - selectedIndex);
			setDate(calendar.getTime());
			days[selectedIndex].requestFocus();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void focusGained(final FocusEvent pFocusEvent)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				((JFormattedTextField)pFocusEvent.getSource()).selectAll();
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void focusLost(FocusEvent pFocusEvent)
	{
	}
	
	// ITranslatable
	
    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
    	if (translation != pTranslation)
    	{
	    	translation = pTranslation;
	
	    	setLocaleIntern();
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public TranslationMap getTranslation()
    {
    	return translation;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        bTranslationEnabled = pEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return bTranslationEnabled;
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUI(PanelUI pPanelUI)
	{
		super.setUI(pPanelUI);
		if (days != null)
		{
			SwingUtilities.invokeLater(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocale(Locale pLocale)
	{
		super.setLocale(pLocale);
		setLocaleIntern();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestFocus()
	{
		if (selectedIndex >= 0)
		{
			days[selectedIndex].requestFocus();
		}
		else
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
		}
	    setFocusable(false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		super.addNotify();
		setFocusable(true);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a proper date editor.
	 * @param pSpinner the spinner.
	 * @param pFormat the format
	 */
	private void createDateEditor(JSpinner pSpinner, String pFormat)
	{
		JSpinner.DateEditor dateEdit = new JSpinner.DateEditor(pSpinner, pFormat);
		pSpinner.setEditor(dateEdit);
		dateEdit.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
		dateEdit.getTextField().removePropertyChangeListener(dateEdit);
		dateEdit.getTextField().addPropertyChangeListener(this);
		dateEdit.getTextField().addKeyListener(this);
		dateEdit.getTextField().addFocusListener(this);
	}
	
	/**
	 * Sets the locale corrected Editors.
	 */
	private void setLocaleIntern()
	{
		ignoreEvent = true;
		
		Locale locale;
		
		if (translation != null && translation.getLanguage() != null)
		{
			locale = new Locale(translation.getLanguage());
		}
		else
		{
			locale = getLocale();
		}
		
		months.setLocale(locale);
		years.setLocale(locale);
		timeSeconds.setLocale(locale);
		timeMinutes.setLocale(locale);
		timeSeconds12.setLocale(locale);
		timeMinutes12.setLocale(locale);

		createDateEditor(months, "MMMM");
		createDateEditor(years, "yyyy");
		createDateEditor(timeSeconds, "HH:mm:ss");
		createDateEditor(timeMinutes, "HH:mm");
		createDateEditor(timeSeconds12, "hh:mm:ss a");
		createDateEditor(timeMinutes12, "hh:mm a");
		
		calendar = new GregorianCalendar(locale);

	    String[] week = new DateFormatSymbols(locale).getShortWeekdays();
	    
	    for (int i = 0; i < 7; i++) 
	    {
	    	weekDays[i].setText(week[(i + 1) % 7 + 1]);
	    }
		
		setDateIntern();
	}
	
	/**
	 * True, if the time editor is visible.
	 * @return True, if the time editor is visible.
	 */
	public boolean isTimeVisible()
	{
		return currentTime.isVisible();
	}
	
	/**
	 * True, if the time editor is visible.
	 * @param pTimeVisible True, if the time editor is visible.
	 */
	public void setTimeVisible(boolean pTimeVisible)
	{
		timeSeconds.setVisible(false);
		timeMinutes.setVisible(false);
		timeSeconds12.setVisible(false);
		timeMinutes12.setVisible(false);
		currentTime.setVisible(pTimeVisible);
		
		JVxGridLayout northLayout = ((JVxGridLayout)years.getParent().getLayout());
		
		if (pTimeVisible)
		{
			northLayout.setConstraints(months, northLayout.getConstraints(0, 0));
			northLayout.setConstraints(years, northLayout.getConstraints(1, 0));
		}
		else
		{
			northLayout.setConstraints(months, northLayout.getConstraints(0, 0, 2, 1));
			northLayout.setConstraints(years, northLayout.getConstraints(2, 0));
		}
	}
	
	/**
	 * True, if seconds should be shown.
	 * @return True, if seconds should be shown.
	 */
	public boolean isShowSeconds()
	{
		return currentTime == timeSeconds || currentTime == timeSeconds12;
	}
	
	/**
	 * True, if seconds should be shown.
	 * @param pShowSeconds True, if seconds should be shown.
	 */
	public void setShowSeconds(boolean pShowSeconds)
	{
		boolean visible = isTimeVisible();
		setTimeVisible(false);
		setCurrentTime(pShowSeconds, isShowAmPm());
		setTimeVisible(visible);
	}
	
	/**
	 * True, if am pm should be shown.
	 * @return True, if am pm should be shown.
	 */
	public boolean isShowAmPm()
	{
		return currentTime == timeSeconds12 || currentTime == timeMinutes12;
	}
	
	/**
	 * True, if am pm should be shown.
	 * @param pShowAmPm True, if am pm should be shown.
	 */
	public void setShowAmPm(boolean pShowAmPm)
	{
		boolean visible = isTimeVisible();
		setTimeVisible(false);
		setCurrentTime(isShowSeconds(), pShowAmPm);
		setTimeVisible(visible);
	}
	
	/**
	 * sets the correct current time editor.
	 * @param pShowSeconds show seconds
	 * @param pShowAmPm show am pm
	 */
	private void setCurrentTime(boolean pShowSeconds, boolean pShowAmPm)
	{
		if (pShowSeconds && pShowAmPm)
		{
			currentTime = timeSeconds12;
		}
		else if (!pShowSeconds && pShowAmPm)
		{
			currentTime = timeMinutes12;
		}
		else if (pShowSeconds && !pShowAmPm)
		{
			currentTime = timeSeconds;
		}
		else // if (!pShowSeconds && pShowAmPm)
		{
			currentTime = timeMinutes;
		}
	}
	
	/**
	 * Sets the current selected date.
	 */
	private void setDateIntern()
	{
	    ignoreEvent = true;

	    if (date == null) 
	    {
	    	calendar.setTime(new Date());
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        date = calendar.getTime();
	    }
	    else 
	    {
	    	calendar.setTime(date);
	    }
	    
	    years.setValue(calendar.getTime());
	    months.setValue(calendar.getTime());
	    currentTime.setValue(calendar.getTime());
	    
	    int iMonth = calendar.get(Calendar.MONTH);
	    int iDay = calendar.get(Calendar.DAY_OF_MONTH);

	    calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_MONTH) + 1);

	    int dayOfWeekInMonth = calendar.get(Calendar.DAY_OF_WEEK);
	    if (dayOfWeekInMonth <= Calendar.MONDAY) 
	    {
	    	dayOfWeekInMonth += 7;
	    }
	    calendar.add(Calendar.DATE, -dayOfWeekInMonth + Calendar.MONDAY);
	    
	    GregorianCalendar loop = (GregorianCalendar)calendar.clone();
	    for (int i = 0; i < 42; i++) 
	    {
	    	if (i % 7 == 0) 
	    	{
	    		weeks[i / 7].setText(String.valueOf(loop.get(Calendar.WEEK_OF_YEAR)));
	    	}
	    	days[i].setText(String.valueOf(loop.get(Calendar.DAY_OF_MONTH)));
	    	days[i].setEnabled(loop.get(Calendar.MONTH) == iMonth);
	    	if (loop.get(Calendar.MONTH) == iMonth)
	    	{
		    	if (i % 7 >= 5) 
		    	{
		    		days[i].setForeground(Color.red);
		    	}
		    	else
		    	{
		    		days[i].setForeground(Color.black);
		    	}
	    	}
	    	else
	    	{
		    	if (i % 7 >= 5) 
		    	{
		    		days[i].setForeground(Color.red.darker());
		    	}
		    	else
		    	{
		    		days[i].setForeground(Color.lightGray);
		    	}
	    	}
	    	if (loop.get(Calendar.MONTH) == iMonth && loop.get(Calendar.DATE) == iDay)
    		{
	    		selectedIndex = i;
    			if (!days[i].isSelected()) 
    			{
    				days[i].setSelected(true);
    			}
				days[i].setFocusable(true);
    		}
    		else
    		{
		        if (days[i].isSelected()) 
		        {
		        	days[i].setSelected(false);
    				days[i].setFocusable(false);
		        }
    		}
	    	loop.add(Calendar.DATE, 1);
	    }
	    ignoreEvent = false;
	}
	
	/**
	 * Returns the current selected date and time.
	 *
	 * @return the selected date.
	 */
	public Date getDate()
	{
		ignoreEvent = true;
		try
		{
			((JSpinner.DateEditor)currentTime.getEditor()).getTextField().commitEdit();
		}
		catch (ParseException ex)
		{
			// try to store
		}
		try
		{
			((JSpinner.DateEditor)months.getEditor()).getTextField().commitEdit();
		}
		catch (ParseException ex)
		{
			// try to store
		}
		try
		{
			((JSpinner.DateEditor)years.getEditor()).getTextField().commitEdit();
		}
		catch (ParseException ex)
		{
			// try to store
		}
		ignoreEvent = false;
		return date;
	}

	/**
	 * Sets the current selected date and time.
	 *
	 * @param pDate the selected date to set.
	 */
	public void setDate(Date pDate)
	{
		date = pDate;
		setDateIntern();
	}

    /**
     * Adds an <code>ActionListener</code> to the button.
     * @param pActionListener the <code>ActionListener</code> to be added
     */
    public void addActionListener(ActionListener pActionListener) 
    {
        listenerList.add(ActionListener.class, pActionListener);
    }
    
    /**
     * Removes an <code>ActionListener</code> from the button.
     * If the listener is the currently set <code>Action</code>
     * for the button, then the <code>Action</code>
     * is set to <code>null</code>.
     *
     * @param pActionListener the listener to be removed
     */
    public void removeActionListener(ActionListener pActionListener) 
    {
		if (pActionListener != null) 
		{
		    listenerList.remove(ActionListener.class, pActionListener);
		}
    }
    
    /**
     * Sets the action command for this button. 
     * @param pActionCommand the action command for this button
     */
    public void setActionCommand(String pActionCommand) 
    {
        actionCommand = pActionCommand;
    }
    
    /**
     * Returns the action command for this button. 
     * @return the action command for this button
     */
    public String getActionCommand() 
    {
    	return actionCommand;
    }
    
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the <code>event</code> 
     * parameter.
     *
     * @param pActionEvent  the <code>ActionEvent</code> object
     * @see ActionListener
     */
    protected void processActionEvent(ActionEvent pActionEvent) 
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) 
        {
            if (listeners[i] == ActionListener.class) 
            {
                ((ActionListener)listeners[i + 1]).actionPerformed(e);
            }          
        }
    }
    
    /**
     * Translates the <code>pText</code> with the mapped translations.
     * 
     * @param pText the text to translate
     * @return the translation for <code>pText</code> based on the translation mapping or 
     *         <code>pText</code> if there is no translation available
     * @see #setTranslation(TranslationMap)
     */
    protected String translate(String pText)
    {
    	if (bTranslationEnabled && translation != null)
    	{
            return translation.translate(pText);
    	}
    	else
    	{
            return pText;
    	}
    }

}	// JVxCalenderPane
