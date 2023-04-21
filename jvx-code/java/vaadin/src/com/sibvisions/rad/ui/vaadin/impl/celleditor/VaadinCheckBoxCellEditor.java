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
 * 28.03.2014 - [HM] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.Style;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.rad.ui.celleditor.AbstractCheckBoxCellEditor;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.events.RegistrationContainer;
import com.sibvisions.rad.ui.vaadin.ext.ui.RadioButton;
import com.sibvisions.rad.ui.vaadin.ext.ui.SimplePanel;
import com.sibvisions.rad.ui.vaadin.ext.ui.Switch;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.container.FocusForwardingSimplePanel;
import com.sibvisions.rad.ui.vaadin.impl.control.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinTable;
import com.sibvisions.util.type.CommonUtil;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;


/**
 * The <code>VaadinCheckBoxCellEditor</code> class is the vaadin implementation of {@link javax.rad.ui.celleditor.ICheckBoxCellEditor}.
 * 
 * @author Martin Handsteiner
 */
public class VaadinCheckBoxCellEditor extends AbstractCheckBoxCellEditor<Component>
                                      implements ICellRenderer<Component>
{
	/** true, if events should be ignored. */
	private boolean ignoreEvents = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
     * Creates a new instance of <code>VaadinCheckBoxCellEditor</code>.
     *
     * @see javax.rad.ui.celleditor.ICheckBoxCellEditor
     */
	public VaadinCheckBoxCellEditor()
	{
		this(null, null);
	}

	/**
	 * Creates a new instance of <code>VaadinCheckBoxCellEditor</code> with the given selected and deselected value.
	 * 
	 * @param pSelectedValue the selected value.
	 * @param pDeselectedValue the deselected value.
	 */
	public VaadinCheckBoxCellEditor(Object pSelectedValue, Object pDeselectedValue)
	{
		super(pSelectedValue, pDeselectedValue);
		
		setHorizontalAlignment(ALIGN_CENTER);
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
	public Component getCellRendererComponent(final Component pAvailabelComponent,
											  final IDataPage pDataPage, 
											  final int pRowNumber,
											  final IDataRow pDataRow,
											  final String pColumnName,
											  boolean pIsEditable,
											  boolean pHasFocus)
	{
		SimplePanel panel = null;
		
		try
		{
		    AbstractField checkBoxField;
		    
			if (pAvailabelComponent instanceof SimplePanel)
			{
				panel = (SimplePanel)pAvailabelComponent;

				checkBoxField = (AbstractField)panel.getContent();
			}
			else
			{
				panel = new SimplePanel();

				Style style = getStyle();
	            if (style.containsStyleName(STYLE_RADIOBUTTON))
	            {
                    checkBoxField = new RadioButton();
	            }
	            else if (style.containsStyleName(STYLE_SWITCH))
	            {
                    checkBoxField = new Switch();
	            }
	            else if (style.containsStyleName(STYLE_TOGGLEBUTTON))
	            {
                    checkBoxField = new CheckBox();
	            }
	            else if (style.containsStyleName(STYLE_BUTTON))
	            {
                    checkBoxField = new CheckBox();
	            }
	            else
	            {
	                checkBoxField = new CheckBox();
	            }				
                checkBoxField.addValueChangeListener(new CheckBoxFieldClickListener(this, pDataPage, pRowNumber, pColumnName));
                
	            VaadinCellEditorUtil.addStyleNames(checkBoxField, getStyle(), "jvxcheckcell", "renderer");

                VaadinUtil.getCssExtension(checkBoxField).addAttribute(new CssExtensionAttribute("display", "inline"));

                panel.setContent(checkBoxField);
				
                if (pDataPage instanceof IDataBook)
                {
    				panel.addLayoutClickListener(new LayoutClickListener() 
    				{
    					public void layoutClick(LayoutClickEvent pEvent)
    					{
    						if (pEvent.getClickedComponent() == null)
    						{
    							try
    							{
    							    ((IDataBook)pDataPage).setSelectedRow(pRowNumber);
    							}
    							catch (ModelException e)
    							{
    								//Nothing todo
    							}
		                    }
						}
                    });
                }
			}
	
			ignoreEvents = true;
			
			Boolean value = Boolean.valueOf(selectedValue != null && selectedValue.equals(pDataRow.getValue(pColumnName)));
			if (!CommonUtil.equals(checkBoxField.getValue(), value))
			{
				if (checkBoxField.isReadOnly())
				{
					checkBoxField.setReadOnly(false);
				}
				checkBoxField.setValue(value);
			}

			boolean readonly = !pIsEditable || pDataPage.getDataBook().isReadOnly() || !pDataPage.getDataBook().isUpdateEnabled()
       			    || pDataRow.getRowDefinition().getColumnDefinition(pColumnName).isReadOnly();
			if (!readonly && pDataPage.getDataBook().getReadOnlyChecker() != null)
			{
				try
				{
					readonly = pDataPage.getDataBook().getReadOnlyChecker().isReadOnly(pDataPage.getDataBook(), pDataPage, pDataRow, pColumnName, pRowNumber, -1);
				}
				catch (Throwable pTh)
				{
					// Ignore
				}
			}
			if (checkBoxField.isReadOnly() != readonly)
			{
				checkBoxField.setReadOnly(readonly);
			}

			CssExtension cssPanelExtension = VaadinUtil.getCssExtension(panel);
			cssPanelExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(getHorizontalAlignment()));
			cssPanelExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(getVerticalAlignment()));
		
		}
		catch (ModelException e)
		{
			panel = null;
		}
		finally
		{
			ignoreEvents = false;
		}
		
		return panel;
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
    												 ValueChangeListener
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private VaadinCheckBoxCellEditor cellEditor;
    	
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** To be able to set styles to this component. */
    	private CssExtension cssCellEditorComponentExtension = new CssExtension(); 
    	
    	/** The attributes extension. */
    	private AttributesExtension attExtension;

    	/** A wrapper for the choice component. **/
    	private SimplePanel simplePanel;
    	
    	/** The physical text editor. */
    	private AbstractField cellEditorComponent;    	

    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
    	/** The {@link RegistrationContainer} that holds all registrations. */
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
    	public CellEditorHandler(VaadinCheckBoxCellEditor pCellEditor, ICellFormatterEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		sColumnName = pColumnName;
    		
            Style style = cellEditor.getStyle();
            if (style.containsStyleName(STYLE_RADIOBUTTON))
            {
                cellEditorComponent = new RadioButtonComponent();
            }
            else if (style.containsStyleName(STYLE_SWITCH))
            {
                cellEditorComponent = new SwitchComponent();
            }
            else if (style.containsStyleName(STYLE_TOGGLEBUTTON))
            {
                cellEditorComponent = new CheckBoxComponent();
            }
            else if (style.containsStyleName(STYLE_BUTTON))
            {
                cellEditorComponent = new CheckBoxComponent();
            }
            else
            {
                cellEditorComponent = new CheckBoxComponent();
            }
    		cellEditorComponent.setSizeUndefined();
    		
    		VaadinCellEditorUtil.addStyleNames(cellEditorComponent, pCellEditor.getStyle(), "jvxcheckcell", "editor");
    		
    		VaadinUtil.getCssExtension(cellEditorComponent).addAttribute(new CssExtensionAttribute("display", "inline"));
    		
    		simplePanel = new FocusForwardingSimplePanel();
    		simplePanel.setWidth(100, Unit.PERCENTAGE);
    		simplePanel.setContent(cellEditorComponent);
    		simplePanel.addStyleNames("jvxcheckcellwrapper");
    		
    		cssCellEditorComponentExtension.extend(simplePanel);
    		
			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
			{	// use alignment of editors, if possible.
				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
			}
			else
			{
    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(cellEditor.getHorizontalAlignment()));
    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(cellEditor.getVerticalAlignment()));
			}
			
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
    		VaadinCellEditorUtil.removeStyleNames(cellEditorComponent, cellEditor.getStyle(), "jvxcheckcell", "editor");
    		
    		registrations.removeAll();
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
    		return simplePanel;
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
				if (Boolean.TRUE.equals(cellEditorComponent.getValue()))
				{
					dataRow.setValue(sColumnName, cellEditor.selectedValue);
				}
				else
				{
					dataRow.setValue(sColumnName, cellEditor.deselectedValue);
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
	    			
	    			cellEditorComponent.setValue(Boolean.valueOf(cellEditor.selectedValue != null && cellEditor.selectedValue.equals(dataRow.getValue(sColumnName))));
	    			if (cellEditorListener.getControl() instanceof IEditorControl)
	    			{
	    				IEditorControl control = (IEditorControl)cellEditorListener.getControl();

		    			if (cellEditor.text == null)
		    			{
		    				cellEditorComponent.setCaption(control.translate(dataRow.getRowDefinition().getColumnDefinition(sColumnName).getLabel()));
		    			}
		    			else
		    			{
		    				cellEditorComponent.setCaption(control.translate(cellEditor.text));
		    			}
	    			}
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
                    
		    		if (dynamicAlignment != null)
		    		{
		    			int hAlign = dynamicAlignment.getHorizontalAlignment();
		    			if (hAlign == IAlignmentConstants.ALIGN_DEFAULT)
		    			{
		    				hAlign = cellEditor.getHorizontalAlignment();
		    			}
		    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(hAlign));
		    			int vAlign = dynamicAlignment.getVerticalAlignment();
		    			if (vAlign == IAlignmentConstants.ALIGN_DEFAULT)
		    			{
		    				vAlign = cellEditor.getVerticalAlignment();
		    			}
		    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(vAlign));
		    		}
	    		}
	    		catch (Exception pException)
	    		{
	    			cellEditorComponent.setReadOnly(false);
	    			cellEditorComponent.setValue(null);
	    			cellEditorComponent.setReadOnly(true);
	   
	    			throw new ModelException("Editor cannot be restored!", pException);
	    		}
	    		finally
	    		{
	    			bFirstEditingStarted = true;
	    			bIgnoreEvent = false;
	    		}
    		}
    	}

    	//Click Listener
				
    	/**
    	 * {@inheritDoc}
    	 */
    	public void valueChange(ValueChangeEvent pEvent)
		{
    		if (!bIgnoreEvent)
    		{
				fireEditingStarted();
	
				fireEditingComplete(ICellEditorListener.FOCUS_LOST);
	
				cellEditorComponent.focus();
    		}
		}
		
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Delegates the event to the {@link ICellEditorListener}.
		 * It takes care, that the event occurs only one time.
		 */
		public void fireEditingStarted()
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
		public void fireEditingComplete(String pCompleteType)
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
				//Always size undefined
				cellEditorComponent.setWidth(VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
				simplePanel.setWidth(pWidth, pUnit);
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
				//Always size undefined
				cellEditorComponent.setHeight(VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
				simplePanel.setHeight(pHeight, pUnit);
			}	
		}
		
	    //****************************************************************
	    // Subclass definition
	    //****************************************************************
		
		/**
		 * The <code>CheckBoxComponent</code> is a {@link CheckBox}. It is needed for the ShortcutHandler, 
		 * to fire editing started and editing complete.
		 * 
		 * @author Martin Handsteiner
		 */
		class CheckBoxComponent extends CheckBox 
		                      implements IEditorComponent
		{
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Interface implementation
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

			/**
	    	 * {@inheritDoc}
	    	 */
			public void handleAction(Action pAction)
			{
				bIgnoreEvent = false;
			
		     	if (pAction == ShortcutHandler.ACTION_ESCAPE) 
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
		    	} 
		     	else if (pAction == ShortcutHandler.ACTION_ENTER
		     			 || pAction == ShortcutHandler.ACTION_ALT_ENTER
		     			 || pAction == ShortcutHandler.ACTION_CTRL_ENTER
		     			 || pAction == ShortcutHandler.ACTION_META_ENTER) 
		    	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ENTER_KEY);
		    	}
		     	else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
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
			
		} 	// CheckBoxComponent

        /**
         * The <code>RadioButtonComponent</code> is a {@link RadioButton}. It is needed for the ShortcutHandler, 
         * to fire editing started and editing complete.
         * 
         * @author Martin Handsteiner
         */
        class RadioButtonComponent extends RadioButton 
                              implements IEditorComponent
        {
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Interface implementation
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  

            /**
             * {@inheritDoc}
             */
            public void handleAction(Action pAction)
            {
                bIgnoreEvent = false;
            
                if (pAction == ShortcutHandler.ACTION_ESCAPE) 
                {
                    CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
                } 
                else if (pAction == ShortcutHandler.ACTION_ENTER
                         || pAction == ShortcutHandler.ACTION_ALT_ENTER
                         || pAction == ShortcutHandler.ACTION_CTRL_ENTER
                         || pAction == ShortcutHandler.ACTION_META_ENTER) 
                {
                    CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ENTER_KEY);
                }
                else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
                {
                    CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
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
            
        }   // RadioButtonComponent

        /**
         * The <code>SwitchComponent</code> is a {@link Switch}. It is needed for the ShortcutHandler, 
         * to fire editing started and editing complete.
         * 
         * @author Martin Handsteiner
         */
        class SwitchComponent extends Switch 
                              implements IEditorComponent
        {
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Interface implementation
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  

            /**
             * {@inheritDoc}
             */
            public void handleAction(Action pAction)
            {
                bIgnoreEvent = false;
            
                if (pAction == ShortcutHandler.ACTION_ESCAPE) 
                {
                    CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
                } 
                else if (pAction == ShortcutHandler.ACTION_ENTER
                         || pAction == ShortcutHandler.ACTION_ALT_ENTER
                         || pAction == ShortcutHandler.ACTION_CTRL_ENTER
                         || pAction == ShortcutHandler.ACTION_META_ENTER) 
                {
                    CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ENTER_KEY);
                }
                else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
                {
                    CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
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
            
        }   // SwitchComponent

    }	// CellEditorHandler
    
	/**
	 * The <code>CheckBoxFieldClickListener</code> is a {@link ValueChangeListener}. 
	 * 
	 * @author Martin Handsteiner
	 */
    public static class CheckBoxFieldClickListener implements ValueChangeListener
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/** The data page. */
    	private IDataPage dataPage;
    	
    	/** The row number. */
    	private int rowNumber;
    	
    	/** The column name. */
    	private String columnName;
    	
    	/** The column name. */
    	private VaadinCheckBoxCellEditor cellEditor;
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Constructs a <code>CheckBoxFieldClickListener</code>.
    	 * @param pEditor editor.
    	 * @param pDataPage data page.
    	 * @param pRowNumber row number.
    	 * @param pColumnName column name.
    	 */
    	public CheckBoxFieldClickListener(VaadinCheckBoxCellEditor pEditor, IDataPage pDataPage, int pRowNumber, String pColumnName)
    	{
    		dataPage = pDataPage;
    		rowNumber = pRowNumber;
    		columnName = pColumnName;
    		cellEditor = pEditor;
    	}
    	
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
    	
    	/**
    	 * {@inheritDoc}
    	 */
        @SuppressWarnings("deprecation")
        public void valueChange(ValueChangeEvent pEvent)
		{
    		if (cellEditor.ignoreEvents)
    		{
    			return;
    		}
			try
			{
				cellEditor.ignoreEvents = true;
				
				CheckBox checkBox = (CheckBox)pEvent.getComponent();
				
				IDataBook dataBook = (IDataBook)dataPage;
				
				HasComponents parent = checkBox.getParent().getParent();

				// First select Row and column, then route click to table.
				// This way no initializeOnlyVisibleItems occurs in table click.
				// This initializeOnlyVisibleItems would reset the current choiceField, and restore the item value.
				// If in this place a problem occurs, maybe try to get the choiceField value first, and then use the old click mechanism. 
				dataBook.setSelectedRow(rowNumber);
				dataBook.setSelectedColumn(columnName);
				
				if (parent instanceof VaadinTable.TableComponent)
				{
					((VaadinTable.TableComponent)parent).getVaadinTable().
						itemClick(new com.vaadin.v7.event.ItemClickEvent(parent, null, Integer.valueOf(rowNumber), columnName, new MouseEventDetails()));
				}

				IDataRow dataRow = dataBook.getDataRow(rowNumber);
				
				boolean readOnly = checkBox.isReadOnly();
				
				if (readOnly || dataBook.getSelectedRow() != rowNumber)
				{
					Boolean value = Boolean.valueOf(cellEditor.selectedValue != null && cellEditor.selectedValue.equals(dataRow.getValue(columnName)));
					if (!CommonUtil.equals(checkBox.getValue(), value))
					{
						if (readOnly)
						{
							checkBox.setReadOnly(false);
						}
						checkBox.setValue(value);
						if (readOnly)
						{
							checkBox.setReadOnly(true);
						}
					}
				}
				else
				{
					Object value;
					if (Boolean.TRUE.equals(checkBox.getValue()))
					{
						value = cellEditor.selectedValue;
					}
					else
					{
						value = cellEditor.deselectedValue;
					}
					if (!CommonUtil.equals(dataBook.getValue(columnName), value))
					{
						try
						{
							dataBook.setValue(columnName, value);
						}
						finally
						{
							if (parent instanceof VaadinTable.TableComponent)
							{
								((VaadinTable.TableComponent)parent).getVaadinTable().refreshDataOnly();
							}
						}
					}
				}
			}
			catch (ModelException pEx)
			{
				ExceptionHandler.raise(pEx);
			}
			finally
			{
				cellEditor.ignoreEvents = false;
			}
		}

    }   // CheckBoxFieldClickListener

}	// VaadinCheckBoxCellEditor
