/*
 * Copyright 2017 SIB Visions GmbH
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
 * 06.10.2017 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.grid.databinding;

import java.sql.Timestamp;
import java.util.Map;
import java.util.WeakHashMap;

import javax.rad.genui.UIColor;
import javax.rad.genui.celleditor.UICellEditor;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.IColor;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;
import javax.rad.ui.celleditor.IChoiceCellEditor;
import javax.rad.ui.celleditor.IDateCellEditor;
import javax.rad.ui.celleditor.IImageViewer;
import javax.rad.ui.celleditor.INumberCellEditor;
import javax.rad.ui.celleditor.ITextCellEditor;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.ICellFormatter;

import com.sibvisions.rad.ui.vaadin.ext.FontResource;
import com.sibvisions.rad.ui.vaadin.ext.FontResource.StyleProperty;
import com.sibvisions.rad.ui.vaadin.ext.VaCachedResource;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFont;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinChoiceCellEditor;
import com.sibvisions.rad.ui.vaadin.impl.celleditor.VaadinLinkedCellEditor;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.DateUtil;
import com.sibvisions.util.type.NumberUtil;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Resource;

/**
 * The {@link DataBookValuePresentationProvider} is a {@link ValueProvider}
 * implementation which provides an HTML representation of the given value with
 * the {@link ICellFormatter} and {@link ICellEditor} taking into account.
 * 
 * @author Robert Zenz
 */
public class DataBookValuePresentationProvider implements ValueProvider<IChangeableDataRow, String>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The character replacement token for a password. */
	private static final String PASSWORD_TOKEN = "&#x2022;";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The base style. */
	private String baseStyle = "";
	
	/** The (cached) {@link ICellEditor} associated with the column. */
	private ICellEditor cellEditor;
	
	/** The cache for {@link ICellFormat}s {@link StyleInformation}. */
	private Map<ICellFormat, StyleInformation> cellFormatCache = new WeakHashMap<ICellFormat, StyleInformation>();
	
	/** The {@link ICellFormatter} that is used. */
	private ICellFormatter cellFormatter;
	
	/** The class derived from the {@link #columnName}. */
	private String className;
	
	/** The index of the column. */
	private int columnIndex = -1;
	
	/** If the column is mandatory. */
	private boolean columnIsMandatory;
	
	/** If the column is readonly. */
	private boolean columnIsReadOnly;
	
	/** If the column is writeable. */
	private boolean columnIsWriteable;
	
	/** The name of the column. */
	private String columnName = null;
	
	/** The {@link IDataBook}. */
	private IDataBook dataBook = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link DataBookValuePresentationProvider}.
	 *
	 * @param pDataBook the {@link IDataBook}.
	 * @param pColumnName the {@link String column name}.
	 * @param pCellFormatter the {@link ICellFormatter}.
	 */
	public DataBookValuePresentationProvider(IDataBook pDataBook, String pColumnName, ICellFormatter pCellFormatter)
	{
		super();
		
		dataBook = pDataBook;
		columnName = pColumnName;
		cellFormatter = pCellFormatter;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(IChangeableDataRow pDataRow)
	{
		try
		{
			if (cellEditor == null)
			{
				updateCellEditor();
				updateColumnInformation();
				updateBaseStyle();
			}
			
			if (className == null)
			{
				className = "c-" + StringUtil.getText(columnName, StringUtil.TextType.AZLetters).toLowerCase();
			}
			
			String value = getValue(pDataRow);
			
			StringBuilder classes = new StringBuilder("v-" + getStylePrefix() + "-cell-content " + className);
			
			ColumnDefinition columnDefinition = dataBook.getRowDefinition().getColumnDefinition(columnName);
			
			// Watch the space!
			
			if (columnDefinition.isNullable())
			{
				classes.append(" v-" + getStylePrefix() + "-cell-nullable");
			}
			else
			{
				classes.append(" v-" + getStylePrefix() + "-cell-not-nullable");
			}
			
			boolean readOnly = columnDefinition.isReadOnly();
			if (!readOnly && dataBook.getReadOnlyChecker() != null)
			{
				try
				{
					readOnly = dataBook.getReadOnlyChecker().isReadOnly(
							dataBook,
							pDataRow.getDataPage(),
							pDataRow,
							columnName,
							pDataRow.getRowIndex(),
							columnIndex);
				}
				catch (Throwable ex)
				{
					// Ignore
				}
			}
			
			if (readOnly)
			{
				classes.append(" v-" + getStylePrefix() + "-cell-readonly");
			}
			else
			{
				classes.append(" v-" + getStylePrefix() + "-cell-not-readonly");
			}
			
			StyleInformation style = getStyleInformation(pDataRow);
			
			String imageContainer = "";
			
			if (style.getImage() != null)
			{
				imageContainer = "<span class=\"contentcontainer image\">" + toHtml(style.getImage()) + "</span>";
			}
			
			String valueContainer = "<span class=\"contentcontainer text\">" + value + "</span>";
			
			return "<div"
					+ " class=\"" + classes + " " + style.getClasses() + "\""
					+ " style=\"" + baseStyle + ";" + style.getStyles() + "\">"
					+ imageContainer
					+ valueContainer
					+ "</div>";
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Converts the given {@link Resource} to a HTML string, or {@code null} if
	 * it could not be converted.
	 * 
	 * @param pResource the {@link Resource} to convert.
	 * @return the HTML string, or {@code ""} if it could not be converted.
	 */
	private static String toHtml(Resource pResource)
	{
		if (pResource instanceof VaCachedResource)
		{
			return "<img src=\"" + ((VaCachedResource)pResource).getURL() + "\" style=\"vertical-align: middle;\">";
		}
		else if (pResource instanceof FontResource)
		{
			FontResource fontResource = (FontResource)pResource;
			
			StringBuilder container = new StringBuilder();
			container.append("<i ");
			container.append("class=\"fa\" ");
			container.append("style=\"font-style: normal;");
			
			if (fontResource.getCustomStyleProperties() != null)
			{
				for (StyleProperty styleProperty : fontResource.getCustomStyleProperties())
				{
					container.append(styleProperty.getName());
					container.append(":");
					container.append(styleProperty.getValue());
					container.append(";");
				}
			}
			
			container.append("\">");
			container.append("&#" + Integer.toString(fontResource.getCodepoint()) + ";");
			container.append("</i>");
			
			return container.toString();
		}
		
		return "";
	}
	
	/**
	 * Converts the given {@link IColor} to an RGBA "command" usable in CSS.
	 * 
	 * @param pColor the {@link IColor}.
	 * @param pAlpha the alpha value.
	 * @return the RGBA "command" for CSS.
	 */
	private static String toRGBA(IColor pColor, float pAlpha)
	{
		return "rgba("
				+ pColor.getRed() + ","
				+ pColor.getGreen() + ","
				+ pColor.getBlue() + ","
				+ pAlpha + ")";
	}
	
	/**
	 * Clears all cached values.
	 */
	public void clearCache()
	{
		baseStyle = "";
		cellEditor = null;
		cellFormatCache.clear();
	}
	
	/**
	 * Sets the column name.
	 * 
	 * @param pColumnName the new column name.
	 */
	public void setColumnName(String pColumnName)
	{
		columnName = pColumnName;
		
		clearCache();
		columnIndex = -1;
	}
	
	/**
	 * Sets the {@link IDataBook}.
	 * 
	 * @param pDataBook the {@link IDataBook}.
	 */
	public void setDataBook(IDataBook pDataBook)
	{
		dataBook = pDataBook;
		
		clearCache();
		columnIndex = -1;
	}
	
	/**
	 * Gets the prefix for the styles.
	 * 
	 * @return the prefix for the styles.
	 */
	protected String getStylePrefix()
	{
		return "grid";
	}
	
	/**
	 * Gets the style for the cached {@link IChangeableDataRow}.
	 * 
	 * @param pDataRow the data representation
	 * @return the style for the cached {@link IChangeableDataRow}. Callers can
	 *         safely assume that this function never returns {@code null}.
	 */
	private StyleInformation getStyleInformation(IChangeableDataRow pDataRow)
	{
		ICellFormat cellFormat = null;
		
		try
		{
			cellFormat = cellFormatter.getCellFormat(
					dataBook,
					pDataRow.getDataPage(),
					pDataRow,
					columnName,
					pDataRow.getRowIndex(),
					columnIndex);
		}
		catch (Throwable ex)
		{
			// Ignore any exceptions. If an exception happens here,
			// the whole grid will be broken anyway.
		}
		
		if (cellFormat != null)
		{
			boolean isSelectedRow = false;
			
			try
			{
				isSelectedRow = (dataBook.getSelectedRow() == pDataRow.getRowIndex());
			}
			catch (ModelException e)
			{
				throw new RuntimeException(e);
			}
			
			if (!cellFormatCache.containsKey(cellFormat) || isSelectedRow)
			{
				StringBuilder style = new StringBuilder();
				
				if (cellFormat.getBackground() != null)
				{
					style.append("background-color:");
					
					if (isSelectedRow)
					{
						style.append(toRGBA(cellFormat.getBackground(), 0.5f));
					}
					else
					{
						style.append(UIColor.toHex(cellFormat.getBackground()));
					}
					
					style.append(";");
				}
				
				if (cellFormat.getFont() != null)
				{
					VaadinFont font = (VaadinFont)cellFormat.getFont().getResource();
					
					for (Map.Entry<String, String> entry : font.getStyleAttributes(false).entrySet())
					{
						style.append(entry.getKey());
						style.append(":");
						style.append(entry.getValue());
						style.append(";");
					}
				}
				
				if (cellFormat.getForeground() != null)
				{
					style.append("color:");
					style.append(UIColor.toHex(cellFormat.getForeground()));
					style.append(";");
				}
				
				style.append("padding-left:");
				style.append(cellFormat.getLeftIndent());
				style.append("px;");
				
				Style styleCell = cellFormat.getStyle();
				
				String classes;
				
				if (styleCell != null)
				{
					classes = StringUtil.concat(" ", styleCell.getStyleNames());
				}
				else
				{
					classes = null;
				}
				
				Resource image = null;
				
				if (cellFormat.getImage() != null)
				{
					image = (Resource)cellFormat.getImage().getResource();
				}
				
				if (isSelectedRow)
				{
					// Do not cache the style of the selected row.
					return new StyleInformation(classes, style.toString(), image);
				}
				
				cellFormatCache.put(cellFormat, new StyleInformation(classes, style.toString(), image));
			}
			
			return cellFormatCache.get(cellFormat);
		}
		
		return StyleInformation.EMPTY;
	}
	
	/**
	 * Gets the value from the {@link IDataRow} as HTML string, prepared to be
	 * displayed.
	 * 
	 * @param pDataRow the {@link IDataRow}.
	 * @return the value as HTML string.
	 * @throws ModelException if accessing the {@link IDataRow} fails.
	 */
	private String getValue(IChangeableDataRow pDataRow) throws ModelException
	{
		if (pDataRow == null)
		{
			return "";
		}
		
		if (cellEditor instanceof VaadinLinkedCellEditor)
		{
			String displayValue = ((VaadinLinkedCellEditor)cellEditor).getDisplayValue(pDataRow, columnName);
			
			if (displayValue != null)
			{
				return displayValue;
			}
			else
			{
				return "";
			}
		}
		
		Object value = pDataRow.getValue(columnName);
		
		if (cellEditor instanceof ICheckBoxCellEditor<?>)
		{
			boolean isChecked = CommonUtil.equals(value, ((ICheckBoxCellEditor<?>)cellEditor).getSelectedValue());
			String checked = "";
			
			if (isChecked)
			{
				checked = "checked=\"checked\"";
			}
			
			return "<input type=\"checkbox\" " + checked + "\" style=\"vertical-align: middle;\"></input>";
		}
		else if (cellEditor instanceof VaadinChoiceCellEditor)
		{
			VaadinChoiceCellEditor choiceCellEditor = (VaadinChoiceCellEditor)cellEditor;
			
			int index = ArrayUtil.indexOf(choiceCellEditor.getAllowedValues(), value);
			
			if (index >= 0 && choiceCellEditor.getImages() != null && index < choiceCellEditor.getImages().length)
			{
				return toHtml(choiceCellEditor.getImages()[index]);
			}
			
			return toHtml(choiceCellEditor.getDefaultImage());
		}
		
		if (value == null)
		{
			return "";
		}
		
		if (cellEditor instanceof IDateCellEditor)
		{
			return DateUtil.format(((Timestamp)value).getTime(), ((IDateCellEditor)cellEditor).getDateFormat());
		}
		else if (cellEditor instanceof IImageViewer<?>)
		{
			if (value instanceof Resource)
			{
				return toHtml((Resource)value);
			}
			else if (value instanceof byte[])
			{
				VaadinImage image = VaadinImage.getImage(null, (byte[])value);
				
				if (image != null)
				{
					return toHtml((Resource)image.getResource());
				}
			}
			else if (value instanceof String)
			{
				VaadinImage image = VaadinImage.getImage((String)value);
				
				if (image != null)
				{
					return toHtml((Resource)image.getResource());
				}
			}
		}
		else if (cellEditor instanceof INumberCellEditor)
		{
			return NumberUtil.format((Number)value, ((INumberCellEditor)cellEditor).getNumberFormat());
		}
		else if (cellEditor instanceof ITextCellEditor)
		{
			ITextCellEditor textCellEditor = (ITextCellEditor)cellEditor;
			
			if (textCellEditor.getContentType() == ITextCellEditor.TEXT_PLAIN_PASSWORD)
			{
				// Value is guaranteed to be not null at this point because of
				// a check above.
				int passwordLength = pDataRow.getValueAsString(columnName).length();
				
				StringBuilder passwordBuilder = new StringBuilder(passwordLength * PASSWORD_TOKEN.length());
				
				for (int counter = 0; counter < passwordLength; counter++)
				{
					passwordBuilder.append(PASSWORD_TOKEN);
				}
				
				return passwordBuilder.toString();
			}
		}
		
		return pDataRow.getValueAsString(columnName);
	}
	
	/**
	 * Updates the base style that is used.
	 */
	private void updateBaseStyle()
	{
		StringBuilder style = new StringBuilder();
		
		style.append("text-align:");
		
		if (cellEditor instanceof INumberCellEditor)
		{
			style.append("right");
		}
		else if (cellEditor instanceof ICheckBoxCellEditor<?>
				|| cellEditor instanceof IChoiceCellEditor<?>
				|| cellEditor instanceof IImageViewer<?>)
		{
			style.append("center");
		}
		else
		{
			style.append("left");
		}
		
		style.append(";");
		
		if (columnIsMandatory)
		{
			style.append("background-color:");
			style.append(toRGBA(UIColor.controlMandatoryBackground, 0.5f));
			style.append(";");
		}
		
		if (columnIsReadOnly)
		{
			style.append("background-color:");
			style.append(toRGBA(UIColor.controlReadOnlyBackground, 0.5f));
			style.append(";");
		}
		
		if (!columnIsWriteable)
		{
			// TODO What background to use?
		}
		
		baseStyle = style.toString();
	}
	
	/**
	 * Updates the {@link #cellEditor} from the {@link IDataRow}.
	 * 
	 * @throws ModelException if accessing the column information fails.
	 */
	private void updateCellEditor() throws ModelException
	{
		cellEditor = dataBook.getRowDefinition().getColumnDefinition(columnName).getDataType().getCellEditor();
		
		if (cellEditor instanceof UICellEditor<?>)
		{
			// Get the resource directly to bypass the UIEnumCellEditor.
			cellEditor = (ICellEditor)((UICellEditor)cellEditor).getResource();
		}
	}
	
	/**
	 * Updates the column information.
	 * 
	 * @throws ModelException if accessing the column information failed.
	 */
	private void updateColumnInformation() throws ModelException
	{
		columnIndex = dataBook.getRowDefinition().getColumnDefinitionIndex(columnName);
		
		ColumnDefinition column = dataBook.getRowDefinition().getColumnDefinition(columnIndex);
		
		columnIsMandatory = !column.isNullable();
		columnIsReadOnly = column.isReadOnly() || dataBook.isReadOnly() || !dataBook.isUpdateEnabled();
		columnIsWriteable = column.isWritable() && !dataBook.isReadOnly() && dataBook.isUpdateEnabled();
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * {@link StyleInformation} is an immutable container for classes and
	 * styles.
	 * 
	 * @author Robert Zenz
	 */
	private static class StyleInformation
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Constants
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** An empty instance. */
		public static final StyleInformation EMPTY = new StyleInformation("", "", null);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The classes. */
		private String classes = null;
		
		/** The {@link Resource} to use as image. */
		private Resource image = null;
		
		/** The styles. */
		private String styles = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link StyleInformation}.
		 *
		 * @param pClasses the {@link String classes}.
		 * @param pStyles the {@link String styles}.
		 * @param pImage the {@link Resource image}.
		 */
		public StyleInformation(String pClasses, String pStyles, Resource pImage)
		{
			super();
			
			if (pClasses != null)
			{
				classes = pClasses;
			}
			else
			{
				classes = "";
			}
			
			if (pStyles != null)
			{
				styles = pStyles;
			}
			else
			{
				styles = "";
			}
			
			image = pImage;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the {@link String classes}.
		 *
		 * @return the {@link String classes}. Callers can safely assume that
		 *         this function never returns {@code null}.
		 */
		public String getClasses()
		{
			return classes;
		}
		
		/**
		 * Gets the {@link Resource image}.
		 *
		 * @return the {@link Resource image}.
		 */
		public Resource getImage()
		{
			return image;
		}
		
		/**
		 * Gets the {@link String styles}.
		 *
		 * @return the {@link String styles}. Callers can safely assume that
		 *         this function never returns {@code null}.
		 */
		public String getStyles()
		{
			return styles;
		}
		
	}	// StyleInformation
	
}	// DataBookValuePresentationProvider
