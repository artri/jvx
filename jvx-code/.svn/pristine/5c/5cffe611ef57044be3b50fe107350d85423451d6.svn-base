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
 * 23.01.2013 - [SW] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.celleditor;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IEditorControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IFont;
import javax.rad.ui.component.IPlaceholder;
import javax.rad.ui.control.ICellFormat;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.rad.ui.celleditor.AbstractTextCellEditor;
import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.events.RegistrationContainer;
import com.sibvisions.rad.ui.vaadin.ext.ui.ClickableLabel;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponentBase;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFont;
import com.sibvisions.rad.ui.vaadin.impl.control.ICellFormatterEditorListener;
import com.sibvisions.rad.ui.vaadin.impl.control.VaadinEditor;
import com.sibvisions.rad.ui.vaadin.impl.feature.IAutoCompleteFeature;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Registration;
import com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.v7.shared.ui.label.ContentMode;

/**
 * The <code>VaadinTextCellEditor</code> class is the vaadin implementation of {@link javax.rad.ui.celleditor.ITextCellEditor}.
 * 
 * @author Stefan Wurm
 */
public class VaadinTextCellEditor extends AbstractTextCellEditor 
                                  implements ICellRenderer<Component>,
                                             IAutoCompleteFeature
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Content type for using a multi line line editor. */
	public static final String TEXT_HTML 	= "text/html";
	
	/** Whether the autocomplete feature is enabled. */
	private Boolean bAutoComplete;
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
     * Creates a new instance of <code>VaadinStyledCellEditor</code>.
     *
     * @see javax.rad.ui.celleditor.ITextCellEditor
     */	
	public VaadinTextCellEditor()
	{
		this(null);
	}
	
	/**
	 * Creates a new instance of <code>VaadinTextCellEditor</code> with the given content type.
	 * 
	 * @param pContentType the content type.
	 */
	public VaadinTextCellEditor(String pContentType)
	{
		setContentType(pContentType);
		setVerticalAlignment(ALIGN_CENTER);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	public IVaadinCellEditorHandler<Component> createCellEditorHandler(ICellEditorListener pCellEditorListener, 
			                                                           IDataRow pDataRow, String pColumnName)
	{
		return new CellEditorHandler(this, (ICellFormatterEditorListener)pCellEditorListener, pDataRow, pColumnName);
	}	
	
	/**
	 * {@inheritDoc}
	 */
    public Component getCellRendererComponent(Component pAvailabelComponent, IDataPage pDataPage, 
													  int pRowNumber, IDataRow pDataRow, String pColumnName, boolean pIsSelected, boolean pHasFocus)
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
	        
	        VaadinCellEditorUtil.addStyleNames(cellLabel, getStyle(), "cursor-hand", "jvxtextcell", "renderer");
	        
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
		cssLabelExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(getVerticalAlignment()));
		
		try
		{
			String value = pDataRow.getValueAsString(pColumnName);
			
			ContentMode contentMode = ContentMode.TEXT;
			if (value != null)
			{
    			if (TEXT_PLAIN_PASSWORD.equals(contentType))
    			{
    			    value = maskPassword(value);
    			}
    			else if (value.startsWith("<html>"))
    			{
    			    contentMode = ContentMode.HTML;
    			}
			}
			
            cellLabel.setContentMode(contentMode);
			cellLabel.setValue(value);
		}
		catch (Exception pException)
		{
			cellLabel.setValue(null);
		}

		return cellLabel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAutoComplete(boolean pAutoComplete)
	{
		bAutoComplete = Boolean.valueOf(pAutoComplete);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoComplete()
	{
		return bAutoComplete == null || bAutoComplete.booleanValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoCompleteSet()
	{
		return bAutoComplete != null;
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
                                                     BlurListener,
                                                     ValueChangeListener
              
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** The CellEditor, that created this handler. */
    	private VaadinTextCellEditor cellEditor;
    
    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellFormatterEditorListener cellEditorListener;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** The physical text editor. */
    	private AbstractField cellEditorComponent;    	
    	
    	/** Dynamic alignment. */
    	private IAlignmentConstants dynamicAlignment = null;
    	
    	/** To be able to set styles to this component. */
    	private CssExtension cssCellEditorComponentExtension = new CssExtension();
    	
    	/** The attributes extension. */
    	private AttributesExtension attExtension;
    	
    	/** The {@link RegistrationContainer} that holds all registrations. */
    	private RegistrationContainer registrations = new RegistrationContainer();

    	/** The column name of the edited column. */
    	private String sColumnName;
 
    	/** Maximum character length. */
    	private int iMaxLength;

    	/** For checking the html tag. */
    	private boolean checkHtmlTag = false;
    	
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
    	public CellEditorHandler(VaadinTextCellEditor pCellEditor, ICellFormatterEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		sColumnName = pColumnName;

    		String contentType = cellEditor.getContentType();
    		if (TEXT_PLAIN_SINGLELINE.equals(contentType))
    		{
    			cellEditorComponent = new TextComponent();
    			cssCellEditorComponentExtension.extend(cellEditorComponent);
    		}
    		else if (TEXT_PLAIN_PASSWORD.equals(contentType))
    		{
    			cellEditorComponent = new PasswordComponent();
    			cssCellEditorComponentExtension.extend(cellEditorComponent);
    		}
    		else if (TEXT_PLAIN_MULTILINE.equals(contentType) || TEXT_PLAIN_WRAPPEDMULTILINE.equals(contentType))
    		{
    			cellEditorComponent = new TextAreaComponent();
    			((TextAreaComponent)cellEditorComponent).setWordWrap(true);

    			cssCellEditorComponentExtension.extend(cellEditorComponent);
    			cssCellEditorComponentExtension.removeAttribute("font-weight");
    			cssCellEditorComponentExtension.removeAttribute("font-style");
    			cssCellEditorComponentExtension.removeAttribute("font-family");
    			cssCellEditorComponentExtension.removeAttribute("font-size");
    		}
    		else if (TEXT_HTML.equals(contentType))
    		{
    			cellEditorComponent = new RichTextAreaComponent();
    			
    			checkHtmlTag = true;
    			
    			cssCellEditorComponentExtension.extend(cellEditorComponent);
    			cssCellEditorComponentExtension.removeAttribute("font-weight");
    			cssCellEditorComponentExtension.removeAttribute("font-style");
    			cssCellEditorComponentExtension.removeAttribute("font-family");
    			cssCellEditorComponentExtension.removeAttribute("font-size");    			
    		}
    		
			if (cellEditorListener.getControl() instanceof IAlignmentConstants && cellEditorListener.getControl() instanceof IEditorControl)
			{	// use alignment of editors, if possible.
				dynamicAlignment = (IAlignmentConstants)cellEditorListener.getControl();
			}
			else
			{
    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getHorizontalAlignCssAttribute(cellEditor.getHorizontalAlignment()));
    			cssCellEditorComponentExtension.addAttribute(VaadinCellEditorUtil.getVerticalAlignCssAttribute(cellEditor.getVerticalAlignment()));
			}
			
			VaadinCellEditorUtil.addStyleNames(cellEditorComponent, pCellEditor.getStyle(), "jvxtextcell", "editor");
			
    		registrations.add(cellEditorComponent.addValueChangeListener(this));

            try
    		{
    			iMaxLength = ((StringDataType)dataRow.getRowDefinition().getColumnDefinition(sColumnName).getDataType()).getSize();
    		}
    		catch (Exception ex)
    		{
    			iMaxLength = Integer.MAX_VALUE;
    		}

    		if (cellEditorComponent instanceof AbstractTextField)
    		{
        		((AbstractTextField)cellEditorComponent).setMaxLength(iMaxLength);
    			
    			registrations.add(((AbstractTextField)cellEditorComponent).addBlurListener(this));
    		}
    		else if (cellEditorComponent instanceof RichTextAreaComponent)
    		{
    			registrations.add(((RichTextAreaComponent)cellEditorComponent).addBlurListener(this));
    		}
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * {@inheritDoc}
    	 */
    	public void uninstallEditor()
    	{
    		VaadinCellEditorUtil.removeStyleNames(cellEditorComponent, cellEditor.getStyle(), "jvxtextcell", "editor");
    		
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
    			IDataType dataType = dataRow.getRowDefinition().getColumnDefinition(sColumnName).getDataType();
    			
	    		Object oldValue = dataRow.getRawValue(sColumnName);
	    		Object newValue = checkHtmlTag(dataType.convertToTypeClass(cellEditorComponent.getValue()));
	    		
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
				IColor background = null;
    			
	    		bIgnoreEvent = true;
	    		
	    		VaadinCellEditorUtil.applyFeature(this);
	
	    		try
	    		{
	    			ColumnDefinition columnDef = dataRow.getRowDefinition().getColumnDefinition(sColumnName);

	    			boolean bParentReadOnly = !(cellEditorListener.getControl() instanceof IComponent) || !((IComponent)cellEditorListener.getControl()).isEnabled();
					boolean readonly;
		    		if (dataRow instanceof IDataBook)
		    		{
		    			IDataBook dataBook = (IDataBook)dataRow;
		    			
		    			readonly = bParentReadOnly
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
		    		}
					else
					{
					    readonly = bParentReadOnly || columnDef.isReadOnly();
					}

		    		String value = columnDef.getDataType().convertToString(readonly ? dataRow.getValue(sColumnName) : dataRow.getRawValue(sColumnName));
                    
                    cellEditorComponent.setReadOnly(false);
                    if (value == null) 
                    {           
                        cellEditorComponent.setValue("");
                    }
                    else
                    {
                        cellEditorComponent.setValue(value);        
                    }
                    cellEditorComponent.setReadOnly(readonly);

                    VaadinCellEditorUtil.applyAdditionalStyles(cellEditorComponent, columnDef);

                    ICellFormat cellFormat = null;

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
						cssCellEditorComponentExtension.addAttributes(((VaadinFont) font.getResource()).getStyleAttributes(null, CssExtensionAttribute.SELF));
					}
					else
					{
						cssCellEditorComponentExtension.removeAttribute("font-weight");
						cssCellEditorComponentExtension.removeAttribute("font-style");
						cssCellEditorComponentExtension.removeAttribute("font-family");
						cssCellEditorComponentExtension.removeAttribute("font-size");
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
						cssCellEditorComponentExtension.addAttribute("color", ((VaadinColor) foreground.getResource()).getStyleValueRGB());
					}
					else
					{
						cssCellEditorComponentExtension.removeAttribute("color");
					}

					if (background != null)
					{
						cssCellEditorComponentExtension.addAttribute("background-color", ((VaadinColor) background.getResource()).getStyleValueRGB());
						cssCellEditorComponentExtension.addAttribute("background-image", "none");
					}
					else
					{
						cssCellEditorComponentExtension.removeAttribute("background-color");
						cssCellEditorComponentExtension.removeAttribute("background-image");
					}
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
		    		
					if (cellEditorListener.getControl() instanceof IPlaceholder && cellEditorComponent instanceof AbstractTextField)
					{
						((AbstractTextField)cellEditorComponent).setPlaceholder(cellEditor.getPlaceholderText((IPlaceholder)cellEditorListener.getControl()));
					}
	    		}
	    		catch (Exception pException)
	    		{
	    			cellEditorComponent.setReadOnly(false);
	    			cellEditorComponent.setValue("");
	    			cellEditorComponent.setReadOnly(true);
	    			
	    			background = ((VaadinComponentBase<?, ?>) cellEditorListener.getControl()).getFactory().getSystemColor(IColor.CONTROL_READ_ONLY_BACKGROUND);
	    			
	    			cssCellEditorComponentExtension.addAttribute("background-color", ((VaadinColor) background.getResource()).getStyleValueRGB());
	    			cssCellEditorComponentExtension.addAttribute("background-image", "none");
	    				
	    			throw new ModelException("Editor cannot be restored!", pException);
	    		}
	    		finally
	    		{
                    if (cellEditorListener.getControl() instanceof VaadinEditor)
                    {
                        CssExtensionAttribute attrBorder = new CssExtensionAttribute("border", "none");
                        CssExtensionAttribute attrBackImage = new CssExtensionAttribute("background-image", "none");
                        
                        
                        if (!((VaadinEditor)cellEditorListener.getControl()).isBorderVisible())
                        {
                            cellEditorComponent.addStyleName("noborder");

                            cssCellEditorComponentExtension.addAttribute(attrBorder);
                            
                            if (background != null)
                            {
                            	cssCellEditorComponentExtension.addAttribute(attrBackImage);
                            }
                        }
                        else
                        {
                            cellEditorComponent.removeStyleName("noborder");
                            
                            cssCellEditorComponentExtension.removeAttribute(attrBorder);
                            
                            if (background == null)
                            {
                            	cssCellEditorComponentExtension.removeAttribute(attrBackImage);
                            }
                        }
                    }
	    			
	    			bFirstEditingStarted = true;
	    			bIgnoreEvent = false;
	    		}
    		}
    	}
    	
    	// FocusListener
  
    	/**
    	 * {@inheritDoc}
    	 */	
		public void blur(BlurEvent pEvent)
		{			
			fireEditingComplete(ICellEditorListener.FOCUS_LOST);
		}	
		
		// ValueChangeListener
		
		@Override
		public void valueChange(ValueChangeEvent pEvent)
		{
			if (!bIgnoreEvent)
			{
				if (cellEditorComponent instanceof AbstractTextField
					|| cellEditorComponent instanceof RichTextArea)
				{
					fireEditingStarted();

					if (cellEditorListener.isSavingImmediate())
		   			{
		       			try
		       			{
		       				if (!cellEditorComponent.isReadOnly() && cellEditorComponent.isEnabled())
		       		    	{
				    			dataRow.setValue(sColumnName, checkHtmlTag(pEvent.getValue()));
				    		}
		       			}
		       			catch (Exception pException)
		       			{
		       				ExceptionHandler.raise(pException);
		       			}
		   			}
				}
			}
		}
		
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Checks, if the html tags have to be added.
		 * @param pText the original text
		 * @return the text with html tag.
		 */
		private Object checkHtmlTag(Object pText)
		{
			if (checkHtmlTag && pText instanceof String)
			{
				String text = ((String)pText).toLowerCase();
				StringBuilder result = null;
				if (!text.startsWith("<html>"))
				{
					result = new StringBuilder(text.length() + 13);
					result.append("<html>");
					result.append(pText);
				}
				if (!text.endsWith("</html>"))
				{
					if (result == null)
					{
						result = new StringBuilder(text.length() + 7);
						result.append(pText);
					}
					result.append("</html>");
				}
				if (result != null)
				{
					return result.toString();
				}
			}
			
			return pText;
		}
		
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
		
	    //****************************************************************
	    // Subclass definition
	    //****************************************************************		

		/**
		 * The <code>TextComponent</code> class is a {@link TextField}. It is needed for the {@link ShortcutHandler}, 
		 * to fire editing started and editing complete.
		 * 
		 * @author Stefan Wurm
		 */
		class TextComponent extends TextField 
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
		     	if (pAction == ShortcutHandler.ACTION_ESCAPE) 
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
		    	} 
		     	else if (pAction == ShortcutHandler.ACTION_ENTER
		     			 || pAction == ShortcutHandler.ACTION_ALT_ENTER
		     			 || pAction == ShortcutHandler.ACTION_CTRL_ENTER
		     			 || pAction == ShortcutHandler.ACTION_META_ENTER) 
		    	{
		     		CellEditorHandler.this.fireEditingStarted();
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
			
			/**
	    	 * {@inheritDoc}
	    	 */
			public void focus()
			{
				if (!bIgnoreEvent)
				{
					super.focus();
				
					if (bFirstEditingStarted)
					{
						bIgnoreEvent = true;
						selectAll();
						bIgnoreEvent = false;
					}
				}
			}
			
		}	// TextComponent
		
		/**
		 * The <code>PasswordComponent</code> class is a {@link PasswordField}. It is needed for the {@link ShortcutHandler}, 
		 * to fire editing started and editing complete.
		 * 
		 * @author Stefan Wurm
		 */
		class PasswordComponent extends PasswordField 
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
			}
		
			/**
	    	 * {@inheritDoc}
	    	 */
			public void focus()
			{
				if (!bIgnoreEvent)
				{
					super.focus();
				
					if (bFirstEditingStarted)
					{
						bIgnoreEvent = true;
						selectAll();
						bIgnoreEvent = false;
					}
				}
			}
			
		} 	// PasswordComponent 
		
		/**
		 * The <code>TextAreaComponent</code> class is a {@link TextArea}. It is needed for the {@link ShortcutHandler}, 
		 * to fire editing started and editing complete.
		 * 
		 * @author Stefan Wurm
		 */
		class TextAreaComponent extends TextArea 
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
		     	if (pAction == ShortcutHandler.ACTION_ESCAPE) 
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
		    	} 
		     	else if (pAction == ShortcutHandler.ACTION_ENTER) 
		    	{
	     			String value = (String) cellEditorComponent.getValue();

	     			int cursorPosition = ((AbstractTextField) cellEditorComponent).getCursorPosition();

	     			value = new StringBuilder(value).insert(cursorPosition, "\n").toString();

	     			if (value == null || value.length() <= 0)
	     			{
	     				value = "\n";
	     			}
	     			
	     			setValue(value);
	     			
	     			((AbstractTextField) cellEditorComponent).setCursorPosition(cursorPosition + 1);
	     			
	     			cellEditorComponent.focus();	
		    	}
		     	else if (pAction == ShortcutHandler.ACTION_ALT_ENTER
		     			 || pAction == ShortcutHandler.ACTION_CTRL_ENTER
		     			 || pAction == ShortcutHandler.ACTION_META_ENTER)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ENTER_KEY);
		     	}
		     	else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
		     	}		
			}
			
		} 	// TextAreaComponent
		
		/**
		 * The <code>RichTextAreaComponent</code> class is a {@link RichTextArea}. It is needed for the {@link ShortcutHandler}, 
		 * to fire editing started and editing complete.
		 * 
		 * @author Stefan Wurm
		 */
		class RichTextAreaComponent extends RichTextArea 
		                            implements IEditorComponent
		{
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Initialization
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

			/**
			 * Createa a new instanceof <code>RichTextAreaComponent</code> and registers the focus handling RPC.
			 */
			public RichTextAreaComponent()
			{
				registerRpc(new RichTextFocusAndBlurRpcImpl());
			}
		    
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Interface implementation
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	    	/**
	    	 * {@inheritDoc}
	    	 */
			public void handleAction(Action pAction)
			{
		     	if (pAction == ShortcutHandler.ACTION_ESCAPE) 
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ESCAPE_KEY);
		    	} 
		     	else if (pAction == ShortcutHandler.ACTION_ENTER) 
		    	{
		     		// Nothing to do.
		    	}
		     	else if (pAction == ShortcutHandler.ACTION_ALT_ENTER
		     			 || pAction == ShortcutHandler.ACTION_CTRL_ENTER
		     			 || pAction == ShortcutHandler.ACTION_META_ENTER)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.ENTER_KEY);
		     	}
		     	else if (pAction == ShortcutHandler.ACTION_SHIFT_ENTER)
		     	{
		     		CellEditorHandler.this.fireEditingComplete(ICellEditorListener.SHIFT_ENTER_KEY);
		     	}		
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// User-defined methods
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
			
			/**
			 * Adds a blur listener.
			 * 
			 * @param listener the listener
			 * @return the listener registration
			 */
		    public Registration addBlurListener(BlurListener listener) 
		    {
		        return addListener(BlurEvent.EVENT_ID, BlurEvent.class, listener, BlurListener.blurMethod);
		    }
			
		    /**
		     * Adds a focus listener.
		     * 
		     * @param listener the listener
		     * @return the listener registration
		     */
		    public Registration addFocusListener(FocusListener listener) 
		    {
		        return addListener(FocusEvent.EVENT_ID, FocusEvent.class, listener, FocusListener.focusMethod);
		    }

			//****************************************************************
			// Subclass definition
			//****************************************************************

		    /**
		     * The <code>RichTextFocusAndBlurRpcImpl</code> is the focus and blur implementation for RPC.
		     * 
		     * @author René Jahn
		     */
		    private final class RichTextFocusAndBlurRpcImpl implements FocusAndBlurServerRpc 
		    {
				//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				// Interface implementation
				//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

		    	/**
		    	 * {@inheritDoc}
		    	 */
		    	@Override
		        public void blur() 
		        {
		            fireEvent(new BlurEvent(RichTextAreaComponent.this));
		        }
		
		    	/**
		    	 * {@inheritDoc}
		    	 */
		        @Override
		        public void focus() 
		        {
		            fireEvent(new FocusEvent(RichTextAreaComponent.this));
		        }
		        
		    }	// RichTextFocusAndBlurRpcImpl			
			
		} 	// TextAreaComponent
		
    }	// CellEditorHandler	
	
} 	// VaadinTextCellEditor
