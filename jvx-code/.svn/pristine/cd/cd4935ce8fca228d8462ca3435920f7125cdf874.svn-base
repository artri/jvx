/*
 * Copyright 2013 SIB Visions GmbH
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
 * 23.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IFont;
import javax.rad.ui.component.IPlaceholder;
import javax.rad.ui.control.ICellFormat;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.celleditor.AbstractDateCellEditor;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.data.SimpleResult;
import com.sibvisions.rad.ui.vaadin.ext.events.RegistrationContainer;
import com.sibvisions.rad.ui.vaadin.ext.ui.ClickableLabel;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedPopupDateField;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedPopupDateField.ExtendedBlurEvent;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedPopupDateField.ExtendedBlurListener;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFont;
import com.sibvisions.rad.ui.vaadin.impl.control.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTable;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.TimeZoneUtil;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.Result;
import com.vaadin.event.Action;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.shared.ui.datefield.LocalDateTimeFieldState;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;

/**
 * The <code>VaadinDateCellEditor</code> is the vaadin implementation of {@link javax.rad.ui.celleditor.IDateCellEditor}.
 * 
 * Be aware the java script is not handling dates correctly.
 * Every date before september 14, 1752 will have a wrong day of the week.
 * 
 * @author Stefan Wurm
 */
public class VaadinDateCellEditor extends AbstractDateCellEditor
                                  implements ICellRenderer<Component>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VaadinDateCellEditor</code>.
	 * 
	 * @see javax.rad.ui.celleditor.IDateCellEditor
	 */
	public VaadinDateCellEditor()
	{
		super();
	}	
	
	/**
	 * Creates a new instance of <code>VaadinDateCellEditor</code> with the given 
	 * date format.
	 * 
	 * @param pDateFormat the date format.
	 */
	public VaadinDateCellEditor(String pDateFormat)
	{
		super(pDateFormat);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * {@inheritDoc}
	 */
	public IVaadinCellEditorHandler<Component> createCellEditorHandler(ICellEditorListener pCellEditorListener, 
			                                                           IDataRow pDataRow, 
			                                                           String pColumnName)
	{
		return new CellEditorHandler(this, (ICellFormatterEditorListener) pCellEditorListener, pDataRow, pColumnName);
	}

	/**
	 * {@inheritDoc}
	 */
    public Component getCellRendererComponent(Component pAvailabelComponent,
								              IDataPage pDataPage,
									          int       pRowNumber,
									          IDataRow  pDataRow,
									          String    pColumnName,
									          boolean   pIsSelected,
									          boolean   pHasFocus)
	{
        ClickableLabel cellLabel = null;
        
        if (pAvailabelComponent instanceof ClickableLabel)
        {
            cellLabel = (ClickableLabel)pAvailabelComponent;
        }
        else
        {
            cellLabel = new ClickableLabel();
            cellLabel.setSizeFull();
            
            VaadinCellEditorUtil.addStyleNames(cellLabel, getStyle(), "cursor-hand", "jvxdatecell", "renderer");

            if (pDataPage instanceof IDataBook)
            {
                cellLabel.addClickListener(new ClickListener()
                {
                    public void click(ClickEvent event)
                    {
                        try
                        {
                            ((IDataBook)pDataPage).setSelectedRow(pRowNumber);
                        }
                        catch (ModelException e)
                        {
                            // Ignore
                        }
                    }
                });
            }
        }
        
		CssExtension cssLabelExtension = VaadinUtil.getCssExtension(cellLabel);

		cssLabelExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(getHorizontalAlignment()));
		cssLabelExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(getHorizontalAlignment()));

		try
		{
			cellLabel.setValue(dateUtil.format((Date)pDataRow.getValue(pColumnName)));
		}
		catch (Exception pException)
		{
			cellLabel.setValue(null);
		}
		
		return cellLabel;
	}	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
//	Jvx and TimeZoneUtil is 16 compatibel, when rebased to java 1.8 this can be removed. 
	/**
	 * Gets a <code>LocalDateTime</code> which matches exact the <code>Date</code> or <code>Timestamp</code> including time
	 * in the default time zone of TimeZoneUtil.
	 * 
	 * @param pDate <code>Date</code> or <code>Timestamp</code> instance
	 * @return a <code>LocalDateTime</code>
	 */
	public static LocalDateTime getLocalDateTime(Date pDate)
	{
		Calendar cal = TimeZoneUtil.getDefaultCalendar();
		cal.setTime(pDate);
		
		if (pDate instanceof Timestamp)
		{
			return LocalDateTime.of(cal.get(Calendar.YEAR),
    				cal.get(Calendar.MONTH) + 1,
    				cal.get(Calendar.DAY_OF_MONTH),
    				cal.get(Calendar.HOUR_OF_DAY),
    				cal.get(Calendar.MINUTE),
    				cal.get(Calendar.SECOND),
                    ((Timestamp)pDate).getNanos());
		}
		else
		{
			return LocalDateTime.of(cal.get(Calendar.YEAR),
    				cal.get(Calendar.MONTH) + 1,
    				cal.get(Calendar.DAY_OF_MONTH),
    				cal.get(Calendar.HOUR_OF_DAY),
    				cal.get(Calendar.MINUTE),
    				cal.get(Calendar.SECOND),
    				cal.get(Calendar.MILLISECOND) * 1000000);
		}
	}

	/**
	 * Gets a <code>Timestamp</code> which matches exact the <code>LocalDateTime</code> including time
	 * in the default time zone of TimeZoneUtil.
	 * 
	 * @param pLocalDateTime the <code>LocalDateTime</code> 
	 * @return a <code>Timestamp</code>
	 */
	public static Timestamp getTimestamp(LocalDateTime pLocalDateTime)
	{
		Calendar cal = TimeZoneUtil.getDefaultCalendar();
		cal.set(pLocalDateTime.getYear(), 
				pLocalDateTime.getMonthValue() - 1, 
				pLocalDateTime.getDayOfMonth(), 
				pLocalDateTime.getHour(), 
				pLocalDateTime.getMinute(), 
				pLocalDateTime.getSecond());
		
		Timestamp result = new Timestamp(cal.getTimeInMillis());
		result.setNanos(pLocalDateTime.getNano());
		
		return result;
	}

	
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
     * The <code>CellEditorHandler</code> class sets the internal changed flag, and informs the 
     * {@link ICellEditorListener} if editing is completed.
     * 
     * @author Stefan wurm
     */
    public static class CellEditorHandler implements IVaadinCellEditorHandler<Component>,
    												 ExtendedBlurListener,
                                                     ValueChangeListener
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private VaadinDateCellEditor cellEditor;
    	
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** The physical text editor. */
    	private DateComponent cellEditorComponent;
    	
    	/** The data type to convert and check the dates. */
    	private IDataType dataType;
    	
    	/** To be able to set styles to this component. */
    	private CssExtension cssCellEditorComponentExtension = new CssExtension();    	

    	/** The attributes extension. */
    	private AttributesExtension attExtension;

    	/** the listener registration container. */
    	private RegistrationContainer registrations = new RegistrationContainer();

    	/** The column name of the edited column. */
    	private String sColumnName;

    	/** True, the Event should be ignored. */
    	private boolean bIgnoreEvent;
    	
    	/** True, if it's the first editing started event. */
    	private boolean bFirstEditingStarted = true;
    	    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Creates a new instance of <code>CellEditorHandler</code>.
    	 * 
    	 * @param pCellEditor the CellEditor that created this handler.
    	 * @param pCellEditorListener CellEditorListener to inform, if editing is started or completed.
    	 * @param pDataRow the data row that is edited.
    	 * @param pColumnName the column name of the edited column.
    	 */
    	public CellEditorHandler(VaadinDateCellEditor pCellEditor, ICellFormatterEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		sColumnName = pColumnName;
            try
            {
                dataType = pDataRow.getRowDefinition().getColumnDefinition(pColumnName).getDataType();
                
                if (!Date.class.isAssignableFrom(dataType.getTypeClass()))
                {
                    LoggerFactory.getInstance(AbstractDateCellEditor.class).error("DateCellEditor is used for a column, that does not store dates!");
                }
            }
            catch (ModelException me)
            {
                //nothing to be done
            }

    		cellEditorComponent = new DateComponent();
    		
    		VaadinCellEditorUtil.addStyleNames(cellEditorComponent, pCellEditor.getStyle(), "jvxdatecell", "editor");

    		cssCellEditorComponentExtension.extend(cellEditorComponent);
    		
    		cellEditorComponent.addExtendedBlurListener(this);
    		registrations.add(cellEditorComponent.addValueChangeListener(this));
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	// IVaadinCellEditorHandler
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	public void uninstallEditor()
    	{
    		VaadinCellEditorUtil.removeStyleNames(cellEditorComponent, cellEditor.getStyle(), "jvxdatecell", "editor");
    		
    		registrations.removeAll();
    		cellEditorComponent.removeExtendedBlurListener(this);
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public ICellEditor getCellEditor()
    	{
    		return cellEditor;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public ICellEditorListener getCellEditorListener()
    	{
    		return cellEditorListener;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public IDataRow getDataRow()
    	{
    		return dataRow;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public String getColumnName()
    	{
    		return sColumnName;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public AbstractComponent getCellEditorComponent()
    	{
    		return cellEditorComponent;
    	}
    	
        /**
         * {@inheritDoc}
         */
        public CssExtension getCssExtension()
        {
            return cssCellEditorComponentExtension;
        }
        
        /**
         * {@inheritDoc}
         */
        public AttributesExtension getAttributesExtension()
        {
        	if (attExtension == null)
        	{
        		attExtension = new AttributesExtension();
        		attExtension.extend(getCellEditorComponent());
        	}
        	
        	return attExtension;
        }         

    	/**
    	 * {@inheritDoc}
    	 */
    	public void saveEditing() throws ModelException
    	{
    		if (!cellEditorComponent.isReadOnly()
    			&& cellEditorComponent.isEnabled())
    		{
	    		Object oldValue = dataRow.getValue(sColumnName);
	    		Object newValue = cellEditorComponent.getValue();
	    		
	    		if (newValue instanceof LocalDateTime)
	    		{
//	    			newValue = new Timestamp(((LocalDateTime)newValue).atZone(TimeZoneUtil.getDefaultZoneId()).toInstant().toEpochMilli());
	    			newValue = getTimestamp((LocalDateTime)newValue);
	    		}
	    		else
	    		{
	    			newValue = dataType.convertToTypeClass(newValue);
	    		}
	    		
	    		if (dataType.compareTo(oldValue, newValue) != 0)
	    		{
	    			dataRow.setValue(sColumnName, newValue);
	    		}
    		}
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void cancelEditing() throws ModelException
    	{
    		if (!bIgnoreEvent)
    		{
	    		bIgnoreEvent = true;
	
	    		try
	    		{
	    			ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(sColumnName);
		    		
	        		TranslationMap translation = cellEditorListener.getControl().getTranslation();
	        		
	        		if (translation != null)
	        		{
	        			Locale locNew;
	        			
	        			String sLang = translation.getLanguage();
	        			
	        			if (sLang != null)
	        			{
	        				locNew = new Locale(translation.getLanguage());
	        			}
	        			else
	        			{
	        				locNew = LocaleUtil.getThreadDefault();
	        			}
	        			
	        			cellEditorComponent.setLocale(locNew);
	        			
	        			if (cellEditorListener.getControl().isTranslationEnabled())
	        			{
	        				String sText = translation.translate(LocalDateTimeFieldState.DESCRIPTION_FOR_ASSISTIVE_DEVICES);	        					
	        					
	        				if (!sText.equals(cellEditorComponent.getState(false).descriptionForAssistiveDevices))
	        				{
	        					cellEditorComponent.setAssistiveText(sText);
	        				}
	        			}
	        		}
	        		
		    		cellEditorComponent.setDateFormat(cellEditor.getDateFormat());
		    		
		    		setTimeSelectionMode();		    		
		    		
		    		cellEditorComponent.setReadOnly(false);
					setValue((Timestamp) dataRow.getValue(sColumnName));
		
					ICellFormat cellFormat = null;

					boolean bParentReadOnly = !(cellEditorListener.getControl() instanceof IComponent) || !((IComponent)cellEditorListener.getControl()).isEnabled();
					
		    		if (dataRow instanceof IDataBook)
		    		{
		    			IDataBook dataBook = (IDataBook)dataRow;
		    			
		    			boolean readonly = bParentReadOnly
		    				    || !dataBook.isUpdateAllowed()
				                || columnDef.isReadOnly();
		    			if (!readonly && dataBook.getReadOnlyChecker() != null)
		    			{
		    				try
							{
		    					readonly = dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook, sColumnName, dataBook.getSelectedRow(), -1);
							}
							catch (Throwable pTh)
							{
								// Ignore
							}
		    			}
		    			cellEditorComponent.setReadOnly(readonly);
		    		}
					else
					{
						cellEditorComponent.setReadOnly(bParentReadOnly || columnDef.isReadOnly());
					}

		    		VaadinCellEditorUtil.applyAdditionalStyles(cellEditorComponent, columnDef);
                    
					if (cellEditorListener.getCellFormatter() != null)
					{
						IDataBook curDataBook = null;
						IDataPage curDataPage = null;
						int curSelectedRow = -1;
						
						if (dataRow instanceof IDataBook)
						{
							curDataBook = (IDataBook)dataRow;
							curDataPage = curDataBook.getDataPage();
							curSelectedRow = curDataBook.getSelectedRow();
						}
						else if (dataRow instanceof IChangeableDataRow)
						{
							curDataPage = ((IChangeableDataRow)dataRow).getDataPage();
							curSelectedRow = ((IChangeableDataRow)dataRow).getRowIndex();
							if (curDataPage != null)
							{
								curDataBook = curDataPage.getDataBook();
							}
						}
						
						try
						{
							cellFormat = cellEditorListener.getCellFormatter().getCellFormat(
									curDataBook, curDataPage, dataRow, sColumnName, curSelectedRow, -1);
						}
						catch (Throwable pThrowable)
						{
							// Do nothing
						}
					}
					
					IColor background;
					IColor foreground;
					IFont  font;
					
					if (cellFormat == null)
					{
						background = null;
						foreground = null;
						font = null;
					}
					else
					{
						background = cellFormat.getBackground();
						foreground = cellFormat.getForeground();
						font = cellFormat.getFont();
					}
					if (font == null)
					{
						font = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFont();
					}
					if (foreground == null && ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).isForegroundSet())
					{
						foreground = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getForeground();
					}
					if (background == null && ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).isBackgroundSet())
					{
						background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getBackground();
					}
					
					if (font != null)
					{
						cssCellEditorComponentExtension.addAttributes(((VaadinFont) font).getStyleAttributes("v-datefield-textfield", 
                                																			CssExtensionAttribute.SEARCH_DOWN));
					}
					else
					{
						CssExtensionAttribute attrib1 = new CssExtensionAttribute("font-weight", 
														    					  null, 
												                                  "v-datefield-textfield", 
												                                  CssExtensionAttribute.SEARCH_DOWN);
						CssExtensionAttribute attrib2 = new CssExtensionAttribute("font-style", 
														    					  null, 
												                                  "v-datefield-textfield", 
												                                  CssExtensionAttribute.SEARCH_DOWN);
						CssExtensionAttribute attrib3 = new CssExtensionAttribute("font-family", 
														    					  null, 
												                                  "v-datefield-textfield", 
												                                  CssExtensionAttribute.SEARCH_DOWN);
						
						CssExtensionAttribute attrib4 = new CssExtensionAttribute("font-size", 
														    					  null, 
												                                  "v-datefield-textfield", 
												                                  CssExtensionAttribute.SEARCH_DOWN);
																		
						cssCellEditorComponentExtension.removeAttribute(attrib1);
						cssCellEditorComponentExtension.removeAttribute(attrib2);
						cssCellEditorComponentExtension.removeAttribute(attrib3);
						cssCellEditorComponentExtension.removeAttribute(attrib4);
					}					
					
		    		if (!cellEditorComponent.isReadOnly())
		    		{
		    			if (background == null)
		    			{
			    			if (columnDef.isNullable())
			    			{
		    					background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_BACKGROUND);
			    			}
			    			else
			    			{
			    				background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_MANDATORY_BACKGROUND);
			    			}
		    			}
		    		}
		    		else if (background == null)
		    		{
		    			background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
		    		}
		    		
					if (foreground != null)
					{
		    			CssExtensionAttribute attrib = new CssExtensionAttribute("color", 
														    					((VaadinColor) foreground.getResource()).getStyleValueRGB(), 
												                                "v-datefield-textfield", 
												                                CssExtensionAttribute.SEARCH_DOWN);
						
						cssCellEditorComponentExtension.addAttribute(attrib);
					}
					else
					{
		    			CssExtensionAttribute attrib = new CssExtensionAttribute("color", 
														    					null, 
												                                "v-datefield-textfield", 
												                                CssExtensionAttribute.SEARCH_DOWN);

						cssCellEditorComponentExtension.removeAttribute(attrib);
					}
					
					if (background != null)
					{
		    			CssExtensionAttribute attribCol = new CssExtensionAttribute("background-color", 
		    																	    ((VaadinColor) background.getResource()).getStyleValueRGB(), 
		    																	    "v-datefield-textfield", 
		    																	    CssExtensionAttribute.SEARCH_DOWN);						

		    			CssExtensionAttribute attribImg = new CssExtensionAttribute("background-image", "none", 
													                                "v-datefield-textfield", 
													                                CssExtensionAttribute.SEARCH_DOWN);						
						
						cssCellEditorComponentExtension.addAttribute(attribCol);
						cssCellEditorComponentExtension.addAttribute(attribImg);
					}
					else
					{
		    			CssExtensionAttribute attribCol = new CssExtensionAttribute("background-color", 
		    																		null, 
		    																		"v-datefield-textfield", 
		    																		CssExtensionAttribute.SEARCH_DOWN);						
		    			CssExtensionAttribute attribImg = new CssExtensionAttribute("background-image", 
																					null, 
																					"v-datefield-textfield", 
																					CssExtensionAttribute.SEARCH_DOWN);						
						
						cssCellEditorComponentExtension.removeAttribute(attribCol);
						cssCellEditorComponentExtension.removeAttribute(attribImg);
					}
					
					if (cellEditorListener.getControl() instanceof IPlaceholder && cellEditorComponent instanceof DateTimeField)
					{
						((DateTimeField)cellEditorComponent).setPlaceholder(cellEditor.getPlaceholderText((IPlaceholder)cellEditorListener.getControl()));
					}
	    		}
	    		catch (Exception pException)
	    		{
	    			pException.printStackTrace();
	    			cellEditorComponent.setReadOnly(false);
	    			cellEditorComponent.setValue(null);
	    			cellEditorComponent.setReadOnly(true);
	    			    			
	    			IColor background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
	    			
	    			cssCellEditorComponentExtension.addAttribute("background-color", ((VaadinColor) background.getResource()).getStyleValueRGB());
	    			cssCellEditorComponentExtension.addAttribute("background-image", "none");
	    				
	    			throw new ModelException("Editor cannot be restored!", pException);
	    		}
	    		finally
	    		{
	    			bFirstEditingStarted = true;
	    			bIgnoreEvent = false;
	    		}
    		}
    	}
    	
    	// FocusListener

		@Override
		public void extendedBlur(ExtendedBlurEvent event)
		{
			fireEditingComplete(event.getCompleteType());
		}

		//ValueChangeListener

    	/**
    	 * {@inheritDoc}
    	 */	
		public void valueChange(ValueChangeEvent pEvent)
		{
			fireEditingStarted();
		}
        
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
    	
    	/**
    	 * Sets the time selection mode. If it is possible to select seconds, minutes, hours or only days.
    	 */
    	private void setTimeSelectionMode()
    	{
    		String dateFormat = cellEditor.getDateFormat();
    		
    		if (dateFormat != null)
    		{
    			cellEditorComponent.removeStyleName("v-timeselection");
    			
    			// If the cell editor does not contain any formats for anything
    			// related to a date, we can make the time only selectable.
    			if (!dateFormat.contains("G")
    					&& !dateFormat.contains("u")
    					&& !dateFormat.contains("y")
    					&& !dateFormat.contains("D")
    					&& !dateFormat.contains("M")
    					&& !dateFormat.contains("L")
    					&& !dateFormat.contains("d")
    					&& !dateFormat.contains("Q")
    					&& !dateFormat.contains("q")
    					&& !dateFormat.contains("Y")
    					&& !dateFormat.contains("w")
    					&& !dateFormat.contains("W")
    					&& !dateFormat.contains("E")
    					&& !dateFormat.contains("e")
    					&& !dateFormat.contains("E")
    					&& !dateFormat.contains("F"))
    			{
    				cellEditorComponent.setStyleName("v-timeselection");
    			}
    			
    			if (dateFormat.contains("s"))
    			{
    				cellEditorComponent.setResolution(DateTimeResolution.SECOND);
    			}
    			else if (dateFormat.contains("m")) 
    			{
    				cellEditorComponent.setResolution(DateTimeResolution.MINUTE);
    			}
    			else if (dateFormat.toUpperCase().contains("H") || dateFormat.toUpperCase().contains("K")) 
    			{
    				cellEditorComponent.setResolution(DateTimeResolution.HOUR);
    			}
    			else
    			{
    				cellEditorComponent.setResolution(DateTimeResolution.DAY);
    			}
    		}
    	}

		/**
		 * Delegates the event to the {@link ICellEditorListener}.
		 * It takes care, that the event occurs only one time.
		 */
		protected void fireEditingStarted()
		{
        	if (bFirstEditingStarted
        		&& !bIgnoreEvent
        		&& cellEditorListener != null
        		&& !cellEditorComponent.isReadOnly()
        		&& cellEditorComponent.isEnabled())
        	{
           		bFirstEditingStarted = false;
           		cellEditorListener.editingStarted();
        	}
		}
		
		/**
		 * Delegates the event to the {@link ICellEditorListener}.
		 * It takes care, that editing started will be called before,
		 * if it is not called until jet.
		 * 
		 * @param pCompleteType the editing complete type.
		 */
		protected void fireEditingComplete(String pCompleteType)
		{
			if (!bIgnoreEvent
				&& cellEditorListener != null
				&& !cellEditorComponent.isReadOnly()
				&& cellEditorComponent.isEnabled())
			{
				cellEditorListener.editingComplete(pCompleteType);
			}
		}
		
		/**
		 * Sets the width for the the editor.
		 * An editor consists of Panel, Panel, Component like TextField, DateField, ....
		 * 
		 * @param pWidth the width
		 * @param pUnit the unit: PIXELS, PERCENTAGE
		 */
		public void setWidth(float pWidth, Unit pUnit)
		{
			if (cellEditorComponent != null)
			{
				cellEditorComponent.setWidth(pWidth, pUnit);
			}	
		}
		
		/**
		 * Sets the height for the the editor.
		 * An editor consists of Panel, Panel, Component like TextField, DateField, ....
		 * 
		 * @param pHeight the height
		 * @param pUnit the unit: PIXELS, PERCENTAGE
		 */
		public void setHeight(float pHeight, Unit pUnit)
		{
			if (cellEditorComponent != null)
			{
				cellEditorComponent.setHeight(pHeight, pUnit);
			}	
		}
		
    	/**
    	 * If the value is different from the actualValue, the value is set on the editor.
    	 * 
    	 * @param pValue the new value.
    	 */
    	private void setValue(Timestamp pValue)
    	{
	    	if (pValue != null)
	    	{
	    	    cellEditorComponent.setValue(null); // we have to force a valueChange, else otherwise the client does not send wrong data again, if it is the same...
	    		cellEditorComponent.setValue(
//    	    				LocalDateTime.ofInstant(((Timestamp)pValue).toInstant(), TimeZoneUtil.getDefaultZoneId())); // Calculates wrong <1894 and >2038
	    	    		getLocalDateTime((Timestamp)pValue));
	    	}
	    	else
	    	{
	    		cellEditorComponent.setValue(null);
	    	}
    	}
		
	    //****************************************************************
	    // Subclass definition
	    //****************************************************************
		
		/**
		 * The <code>DateComponent</code> class is a {@link ExtendedPopupDateField}. It is needed for the 
		 * ShortcutHandler, to fire editing started and editing complete.
		 * 
		 * @author Stefan Wurm
		 */
		class DateComponent extends ExtendedPopupDateField 
		                    implements IEditorComponent
		{
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Interface implementation
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
			
			@Override
		    protected Result<LocalDateTime> handleUnparsableDateString(String pDateString)
			{
			    fireEditingStarted();
				try
				{
					return new SimpleResult(
						// Calculates wrong <1894 and >2038
//						LocalDateTime.ofInstant(cellEditor.dateUtil.parse(pDateString).toInstant(), TimeZoneUtil.getDefaultZoneId()));
						// We have to deliver the correct parsed timestamp directly as LocalDateTime
						new Timestamp(cellEditor.dateUtil.parse(pDateString).getTime()).toLocalDateTime());
				}
				catch (Exception e)
				{
					return new SimpleResult(getValue());
				}
			}			
			
	    	/**
	    	 * {@inheritDoc}
	    	 */
			public void handleAction(Action pAction)
			{
		     	if (pAction == ShortcutHandler.ACTION_ESCAPE) 
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
		    	} 
		     	else if (pAction == ShortcutHandler.ACTION_ENTER
		     			 || pAction == ShortcutHandler.ACTION_ALT_ENTER
		     			 || pAction == ShortcutHandler.ACTION_CTRL_ENTER
		     			 || pAction == ShortcutHandler.ACTION_META_ENTER) 
		    	{
		     		// This is just for VaadinTable! We have to force loosing the focus!
		     		// ExtendedBlur delivers all necessary information about how the focus was lost (ENTER, Shift ENTER)
		     		if (cellEditorListener instanceof VaadinTable)
		     		{
		     			((VaadinTable)cellEditorListener).requestFocus();
		     		}
		     		else
		     		{
		     			CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ENTER_KEY);
		     		}
		    	}
		     	else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
		     	{
		     		// This is just for VaadinTable! We have to force loosing the focus!
		     		// ExtendedBlur delivers all necessary information about how the focus was lost (ENTER, Shift ENTER)
		     		if (cellEditorListener instanceof VaadinTable)
		     		{
		     			((VaadinTable)cellEditorListener).requestFocus();
		     		}
		     		else
		     		{
		     			CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
		     		}
		     	}		
		     	else if (pAction == ShortcutHandler.ACTION_TAB)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.TAB_KEY);
		     	}
		     	else if (pAction == ShortcutHandler.ACTION_SHIFT_TAB)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_TAB_KEY);
		     	}		     	
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Overwritten methods
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

			@Override
		    public LocalDateTimeFieldState getState(boolean markAsDirty) 
		    {
		        return (LocalDateTimeFieldState)super.getState(markAsDirty);
		    }	
			
		}	// DateComponent	

    }	// CellEditorHandler		
    
} // VaadinDateCellEditor
