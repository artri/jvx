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
 * 20.01.2020 - [HM] - creation
 */
package com.sibvisions.rad.genui.celleditor;

import java.util.HashMap;
import java.util.Map;

import javax.rad.genui.UIImage;
import javax.rad.genui.celleditor.UICellEditor;
import javax.rad.genui.celleditor.UICheckBoxCellEditor;
import javax.rad.genui.celleditor.UIChoiceCellEditor;
import javax.rad.genui.celleditor.UIDateCellEditor;
import javax.rad.genui.celleditor.UILinkedCellEditor;
import javax.rad.genui.celleditor.UINumberCellEditor;
import javax.rad.genui.celleditor.UITextCellEditor;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IChangeableDataRow;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataPage;
import javax.rad.model.IDataRow;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BigDecimalDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.model.event.DataRowEvent;
import javax.rad.model.event.IDataRowListener;
import javax.rad.model.reference.ColumnMapping;
import javax.rad.model.reference.ReferenceDefinition;
import javax.rad.model.ui.ICellEditor;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;
import javax.rad.model.ui.ICellRenderer;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.IEditorControl;
import javax.rad.model.ui.ITableControl;

import com.sibvisions.rad.model.mem.DataRow;
import com.sibvisions.rad.model.remote.RemoteDataBook;
import com.sibvisions.rad.model.remote.RemoteDataSource;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@code UIDynamicCellEditor} is a special editor that offers a row specific editor and renderer, depending on a type column.
 * <p>
 * The type column has to have the name of the edited column with the postfix "_TYPE".<p>
 * e.g. 
 * if the column "VALUE" has a dynamic cell editor, there should be a column "VALUE_TYPE" which specifies the type of the editor.
 * <p>
 * Supported types are:<ul>
 * <li>NUMBER[(precision,scale)][;java-number-format]</li> 
 * <li>VARCHAR[(size)][;content-type]</li>
 * <li>DATE[;java-date-format]</li>
 * <li>ENUM;comma-separated-allowed-values[;comma-separated-display-values]</li>
 * <li>CHECKBOX[;yes-value,no-value] default is Y, N</li>
 * <li>CHOICE[;comma-separated-allowed-values[;comma-separated-image-names[;default-image-name]]]<br>
 *     default is Y, N;UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL; UIImage.CHECK_SMALL.<br>
 *     An empty value means the value null</li>
 * <li>COMBOBOX;storage-name[;referenced-column-name[;referenced-display-column-name]][:comma-separated-search-columns[;comma-separated-referenced-search-columns]]</li>
 * <li>&lt;keyname&gt; for preconfigured types</li>
 * </ul><p>  
 * examples:
 * <p>
 * NUMBER<br>
 * NUMBER(10,2)<br>
 * NUMBER(10,2);#,##0.0<br>
 * <br>
 * VARCHAR<br>
 * VARCHAR(4)<br>
 * <br>
 * DATE<br>
 * DATE;dd.MM.yyyy HH:mm<br>
 * <br>
 * ENUM;Martin,Roland,Rene<br>
 * ENUM;1,2,3;Martin,Roland,Rene<br>
 * <br>
 * CHECKBOX<br>
 * CHECKBOX;1,0<br>
 * <br>
 * CHOICE;1,0,<br>
 * CHOICE;1,0,;/javax/rad/genui/images/16x16/check_yes.png,/javax/rad/genui/images/16x16/check.png,/javax/rad/genui/images/16x16/check_no.png<br>
 * There is an empty value at the end of the allowed values list, the allowed values are therefore 1, 0 and null.<br>
 * <br>
 * COMBOBOX;contacts.subStorages.salutations;ID;SALUTATION<br>
 * COMBOBOX;listvalues;ID;TEXT:GROUP_ID<br>
 * <br>
 * Preconfigure types on UIDynamicCellEditor:<br>
 * <code>
 * UIDynamicCellEditor dynEditor = new UIDynamicCellEditor();<br>
 * dynEditor.addType("a", "NUMBER(10,2)");<br>
 * dynEditor.addType("b", "VARCHAR(4)");<br>
 * dynEditor.addType("c", "ENUM;Martin,Roland,Rene");<br>
 * </code>
 * Usage:<br>
 * a<br>
 * b<br>
 * c<br>
 * <br>
 * 
 * @author Martin Handsteiner
 */
public class UIDynamicCellEditor implements ICellEditor, ICellRenderer<Object> 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/** Constant for number type. */
	public static final String NUMBER    = "NUMBER";
	/** Constant for varchar type. */
	public static final String VARCHAR   = "VARCHAR";
	/** Constant for date type. */
	public static final String DATE      = "DATE";
	/** Constant for enum type. */
	public static final String ENUM      = "ENUM";
	/** Constant for checkbox type. */
	public static final String CHECKBOX  = "CHECKBOX";
	/** Constant for choice type. */
	public static final String CHOICE    = "CHOICE";
    /** Constant for combobox type. */
    public static final String COMBOBOX  = "COMBOBOX";

	/** The default text cell editor. */
	private static final UICellEditor DEFAULT_EDITOR = new UITextCellEditor();

	/** The cached cell editors for a specific type. */
	private static Map<String, UICellEditor> cachedCellEditors = new HashMap<String, UICellEditor>();
	/** The cached data types for a specific type. */
	private static Map<String, IDataType> cachedDataTypes = new HashMap<String, IDataType>();
	/** The cached data rows for a specific type. */
	private static Map<String, Map<String, DataRow>> cachedRenderingDataRows = new HashMap<String, Map<String, DataRow>>();

	/** 
	 * The last rendered cell editor.
	 * This is newly implemented in all implementations, that <code>getCellRendererComponent</code>
	 * is called before calling <code>isDirectCellEditor</code>, to be able to have different results per row.
	 */
	private transient UICellEditor lastCellEditor = null;
	
	/** Preconfigured types. */
	private transient HashMap<String, String> types = new HashMap<String, String>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link UIDynamicCellEditor}.
	 */
	public UIDynamicCellEditor()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener,
													  IDataRow pDataRow,
													  String pColumnName)
	{
		return new CellEditorHandler(this, pCellEditorListener, pDataRow, pColumnName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDirectCellEditor() 
	{
		if (lastCellEditor != null)
		{
			return lastCellEditor.isDirectCellEditor();
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getCellRendererComponent(Object pParentComponent, IDataPage pDataPage, int pRowNumber,
			IDataRow pDataRow, String pColumnName, boolean pIsSelected, boolean pHasFocus) 
	{
		String type = null;
		try
		{
			type = getType(pDataRow.getValueAsString(pColumnName + "_TYPE"));
		}
		catch (Exception ex)
		{
			// Ignore
		}
		lastCellEditor = getCellEditor(type, pDataPage.getDataBook());
		
		DataRow dataRow = getDataRow(type, pColumnName);
		
		try 
		{
			dataRow.setValue(pColumnName, pDataRow.getValue(pColumnName));
		}
		catch (ModelException e) 
		{
	        try 
	        {
	            dataRow.setValue(pColumnName, null);
	        }
	        catch (ModelException ex) 
	        {
	            // Ignore
	        }
		}
		
		return ((ICellRenderer<Object>)lastCellEditor.getUIResource()).getCellRendererComponent(pParentComponent, pDataPage, pRowNumber,
				dataRow, pColumnName, pIsSelected, pHasFocus);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a preconfigured type with the given key name.
	 * @param pKeyName the key name
	 * @param pType the type
	 */
	public void addType(String pKeyName, String pType)
	{
	    types.put(pKeyName, pType);
	}
	
    /**
     * Removes a preconfigured type with the given key name.
     * @param pKeyName the key name
     */
    public void removeType(String pKeyName)
    {
        types.remove(pKeyName);
    }
    
    /**
     * Removes all preconfigured types.
     */
    public void removeAllTypes()
    {
        types.clear();
    }
    
    /**
     * Gets all added key names.
     * @return the key names.
     */
    public String[] getTypeNames()
    {
        return types.keySet().toArray(new String[types.size()]);
    }
    
    /**
     * Gets the type for the given key name.
     * if there is no type configured for the given key name, the key name is returned.
     * 
     * @param pKeyName the key name
     * @return the type.
     */
    public String getType(String pKeyName)
    {
        String type = types.get(pKeyName);
        
        if (type == null)
        {
            return pKeyName;
        }
        else
        {
            return type;
        }
    }
    
    /**
     * Gets the dynamic editor for a column.
     * @param pDataRow the data row
     * @param pColumnName the column name
     * @return the dynamic editor
     * @throws ModelException if it fails.
     */
    public static UIDynamicCellEditor getDynamicCellEditor(IDataRow pDataRow, String pColumnName) throws ModelException
    {
        ICellEditor cellEditor = UICellEditor.getCellEditor(pDataRow, pColumnName);
        
        if (cellEditor instanceof UIDynamicCellEditor)
        {
            return (UIDynamicCellEditor)cellEditor;
        }
        else
        {
            IControl[] controls = pDataRow.getControls();
            for (IControl control : controls)
            {
                if (control instanceof IEditorControl)
                {
                    IEditorControl editorControl = (IEditorControl)control;
                    
                    if (pColumnName.equals(editorControl.getColumnName()))
                    {
                        cellEditor = editorControl.getCellEditor();
                        
                        if (cellEditor instanceof UIDynamicCellEditor)
                        {
                            return (UIDynamicCellEditor)cellEditor;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
	/**
	 * Gets the value in correct type class of a column with a dynamic editor.
	 * 
	 * @param pDataRow the data row
	 * @param pColumnName the column name
	 * @return the value in correct type class of a column with a dynamic editor.
	 * @throws ModelException if it fails.
	 */
	public static Object getValue(IDataRow pDataRow, String pColumnName) throws ModelException
	{
	    String type = null;
	    try
	    {
	        type = pDataRow.getValueAsString(pColumnName + "_TYPE");
	        
	        UIDynamicCellEditor cellEditor = getDynamicCellEditor(pDataRow, pColumnName);
	        if (cellEditor != null)
	        {
	            type = cellEditor.getType(type);
	        }
	    }
	    catch (Exception ex)
	    {
	        // Ignore
	    }
        IDataBook dataBook = null;
        if (pDataRow instanceof IDataBook)
        {
            dataBook = (IDataBook)pDataRow;
        }
        else if (pDataRow instanceof IChangeableDataRow && ((IChangeableDataRow)pDataRow).getDataPage() != null)
        {
            dataBook = ((IChangeableDataRow)pDataRow).getDataPage().getDataBook();
        }
	    getCellEditor(type, dataBook);
	        
	    return getDataType(type).convertToTypeClass(pDataRow.getValue(pColumnName));
	}
	
    /**
     * Sets the correct unified value on a column with dynamic editor.
     * 
     * @param pDataRow the data row
     * @param pColumnName the column name
     * @param pValue the value to set.
     * @throws ModelException if it fails.
     */
    public static void setValue(IDataRow pDataRow, String pColumnName, Object pValue) throws ModelException
    {
        String type = null;
        try
        {
            type = pDataRow.getValueAsString(pColumnName + "_TYPE");

            UIDynamicCellEditor cellEditor = getDynamicCellEditor(pDataRow, pColumnName);
            if (cellEditor != null)
            {
                type = cellEditor.getType(type);
            }
        }
        catch (Exception ex)
        {
            // Ignore
        }
        IDataBook dataBook = null;
        if (pDataRow instanceof IDataBook)
        {
            dataBook = (IDataBook)pDataRow;
        }
        else if (pDataRow instanceof IChangeableDataRow && ((IChangeableDataRow)pDataRow).getDataPage() != null)
        {
            dataBook = ((IChangeableDataRow)pDataRow).getDataPage().getDataBook();
        }
        getCellEditor(type, dataBook);

        pDataRow.setValue(pColumnName, getDataType(type).convertToUnifiedString(pValue));
    }
    
	/**
	 * Gets the cached cell editor for the given type.<br>
	 * Preconfigured key names are not allowed here, as the configured dynamic cell editor cannot be determined.
	 * 
	 * @param pType the type string
	 * @param pDataRow the edited data row
	 * @return the cached cell editor
	 */
	public static UICellEditor getCellEditor(String pType, IDataRow pDataRow)
	{
		UICellEditor cellEditor = cachedCellEditors.get(pType);
		
		if (cellEditor instanceof UILinkedCellEditor)
		{
		    UILinkedCellEditor linkedCellEditor = (UILinkedCellEditor)cellEditor;
		    
		    if (linkedCellEditor.getLinkReference() == null 
		            || linkedCellEditor.getLinkReference().getReferencedDataBook() == null 
		            || !linkedCellEditor.getLinkReference().getReferencedDataBook().isOpen())
		    {
		        cellEditor = null;
		        cachedCellEditors.put(pType, null);
		    }
		}		
		if (cellEditor == null)
		{
		   IDataType dataType = null;
		   
		   if (pType != null)
		   {
			   int paramIndex = pType.indexOf(';');
			   
			   String typeBase = paramIndex < 0 ? pType : pType.substring(0, paramIndex);
			   String typeParam = paramIndex < 0 ? "" : pType.substring(paramIndex + 1).trim();

			   int dataTypeIndex = typeBase.indexOf('(');
			   
			   String dataTypeDetails = dataTypeIndex < 0 ? "" : typeBase.substring(dataTypeIndex + 1).trim();
			   if (dataTypeDetails.endsWith(")"))
			   {
				   dataTypeDetails = dataTypeDetails.substring(0, dataTypeDetails.length() - 1).trim();
			   }
			   typeBase = dataTypeIndex < 0 ? typeBase.trim().toUpperCase() : typeBase.substring(0, dataTypeIndex).trim().toUpperCase();

			   if (NUMBER.equals(typeBase))
			   {
				   if (StringUtil.isEmpty(typeParam))
				   {
					   cellEditor = new UINumberCellEditor();
				   }
				   else
				   {
					   cellEditor = new UINumberCellEditor(typeParam);
				   }
				   
				   dataType = new BigDecimalDataType();
				   if (!StringUtil.isEmpty(dataTypeDetails))
				   {
    				   String[] precScal = dataTypeDetails.split(",");
    				   try
    				   {
    					   ((BigDecimalDataType)dataType).setPrecision(Integer.parseInt(precScal[0]));
    				   }
    				   catch (Exception ex)
    				   {
    					   // Ignore
    				   }
    				   try
    				   {
    					   ((BigDecimalDataType)dataType).setScale(Integer.parseInt(precScal[1]));
    				   }
    				   catch (Exception ex)
    				   {
    				       ((BigDecimalDataType)dataType).setScale(0);
    				   }
				   }
			   }
			   else if (DATE.equals(typeBase))
			   {
				   if (StringUtil.isEmpty(typeParam))
				   {
					   cellEditor = new UIDateCellEditor();
				   }
				   else
				   {
					   cellEditor = new UIDateCellEditor(typeParam);
				   }
				   
				   dataType = new TimestampDataType();
			   }
			   else if (VARCHAR.equals(typeBase))
			   {
                   if (!StringUtil.isEmpty(typeParam))
                   {
                       cellEditor = new UITextCellEditor(typeParam.intern());
                   }
                   
				   dataType = new StringDataType();
				   try
				   {
					   ((StringDataType)dataType).setSize(Integer.parseInt(dataTypeDetails));
				   }
				   catch (Exception ex)
				   {
					   // Ignore
				   }
			   }
			   else if (ENUM.equals(typeBase))
			   {
                   String[] params = typeParam.split(";");
				   
				   String[] allowedValues = params[0].split(",", -1);
				   String[] displayValues = params.length > 1 ? params[1].split(",", -1) : null;
				   
				   for (int i = 0; i < allowedValues.length; i++)
				   {
					   allowedValues[i] = allowedValues[i].trim();
					   if ("".equals(allowedValues[i]))
					   {
						   allowedValues[i] = null;
					   }
				   }
				   if (displayValues != null)
				   {
					   for (int i = 0; i < displayValues.length; i++)
					   {
						   displayValues[i] = displayValues[i].trim();
						   if ("".equals(displayValues[i]))
						   {
							   displayValues[i] = null;
						   }
					   }
				   }

				   cellEditor = new UIEnumCellEditor(allowedValues, displayValues);
			   }
			   else if (CHECKBOX.equals(typeBase))
			   {
				   String[] params = typeParam.split(";");
				   
				   String[] allowedValues = params[0].split(",", -1);
				   
				   Object selectedValue = "Y";
				   if (allowedValues.length > 0)
				   {
					   selectedValue = allowedValues[0].trim();
					   if ("".equals(selectedValue))
					   {
						   selectedValue = null;
					   }
				   }
				   Object deselectedValue = "N";
				   if (allowedValues.length > 1)
				   {
					   deselectedValue = allowedValues[1].trim();
					   if ("".equals(deselectedValue))
					   {
						   deselectedValue = null;
					   }
				   }

				   cellEditor = new UICheckBoxCellEditor(selectedValue, deselectedValue);
			   }
			   else if (CHOICE.equals(typeBase))
			   {
				   String[] params = typeParam.split(";");
				   
				   String[] allowedValues = params[0].split(",", -1);
				   String[] imageNames = params.length > 1 ? params[1].split(",", -1) : null;
				   String defaultImageName = params.length > 2 ? params[2].trim() : null;
				   
				   if (defaultImageName == null)
				   {
					   defaultImageName = UIImage.CHECK_SMALL;
				   }

				   if (StringUtil.isEmpty(params[0]))
				   {
					   allowedValues = new String[] {"Y", "N"};
				   }
				   else
				   {
					   for (int i = 0; i < allowedValues.length; i++)
					   {
						   allowedValues[i] = allowedValues[i].trim();
						   if ("".equals(allowedValues[i]))
						   {
							   allowedValues[i] = null;
						   }
					   }
				   }
				   
				   if (imageNames == null || imageNames.length == 0)
				   {
					   imageNames = new String[] {UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL, UIImage.CHECK_NO_SMALL};
				   }
				   else
				   {
					   for (int i = 0; i < imageNames.length; i++)
					   {
						   imageNames[i] = imageNames[i].trim();
						   if ("".equals(imageNames[i]))
						   {
							   imageNames[i] = null;
						   }
					   }
				   }
				   
				   cellEditor = new UIChoiceCellEditor(allowedValues, imageNames, defaultImageName);
			   }
			   else if (COMBOBOX.equals(typeBase))
               {
                   String[] allParams = typeParam.split(":");
                   String[] params = allParams[0].split(";");
                   
                   IDataBook dataBook = null;
                   if (pDataRow instanceof IDataBook)
                   {
                       dataBook = (IDataBook)pDataRow;
                   }
                   else if (pDataRow instanceof IChangeableDataRow && ((IChangeableDataRow)pDataRow).getDataPage() != null)
                   {
                       dataBook = ((IChangeableDataRow)pDataRow).getDataPage().getDataBook();
                   }
                   
                   if (params.length > 0 && !StringUtil.isEmpty(params[0]) && dataBook != null && dataBook.getDataSource() instanceof RemoteDataSource)
                   {
                       try
                       {
                           RemoteDataBook remoteDataBook = new RemoteDataBook();
                           remoteDataBook.setDataSource((RemoteDataSource)dataBook.getDataSource());
                           remoteDataBook.setName(params[0].trim());
                           remoteDataBook.open();
                           
                           String mappedColumn;
                           if (params.length > 1)
                           {
                               mappedColumn = params[1].trim();
                           }
                           else
                           {
                               mappedColumn = remoteDataBook.getRowDefinition().getColumnView(ITableControl.class).getColumnName(0);
                           }
                           String displayColumn;
                           if (params.length > 2)
                           {
                               displayColumn = params[2].trim();
                           }
                           else
                           {
                               displayColumn = null;
                           }
                           ReferenceDefinition linkReference = new ReferenceDefinition();
                           linkReference.setReferencedDataBook(remoteDataBook);
                           linkReference.setReferencedColumnNames(new String[] {mappedColumn});
                           
                           UILinkedCellEditor linkedCellEditor = new UILinkedCellEditor();
                           linkedCellEditor.setLinkReference(linkReference);
                           linkedCellEditor.setColumnView(new ColumnView(displayColumn == null ? mappedColumn : displayColumn));
                           linkedCellEditor.setDisplayReferencedColumnName(displayColumn);
                           
                           if (allParams.length > 1)
                           {
                               String[] searchColumnMapping = allParams[1].split(";");
                               
                               if (!StringUtil.isEmpty(searchColumnMapping[0]))
                               {
                                   String[] columns = searchColumnMapping[0].split(",");
                                   for (int i = 0; i < columns.length; i++)
                                   {
                                       columns[i] = columns[i].trim();
                                   }
                                   String[] referencedColumns = null;
                                   if (searchColumnMapping.length > 1 && !StringUtil.isEmpty(searchColumnMapping[1]))
                                   {
                                       referencedColumns = searchColumnMapping[0].split(",");
                                       for (int i = 0; i < referencedColumns.length; i++)
                                       {
                                           referencedColumns[i] = referencedColumns[i].trim();
                                       }
                                   }
                                   
                                   linkedCellEditor.setSearchColumnMapping(new ColumnMapping(columns, referencedColumns));
                               }
                           }
                           
                           cellEditor = linkedCellEditor;
                       }
                       catch (Throwable e)
                       {
                           // Ignore, combobox will not be available due to wrong configuration
                       }
                   }
			       
               }
		   }

		   if (cellEditor == null)
		   {
			   cellEditor = DEFAULT_EDITOR;
		   }
		   if (dataType == null)
		   {
			   dataType = new StringDataType();
		   }

		   cachedCellEditors.put(pType, cellEditor);
		   cachedDataTypes.put(pType, dataType);
		   cachedRenderingDataRows.put(pType, new HashMap<String, DataRow>());
		}
		
		return cellEditor;
	}

	/**
	 * Gets the datatype for the specific type definition.
	 * This method has to be called after <code>getCellEditor</code>.
     * Preconfigured key names are not allowed here, as the configured dynamic cell editor cannot be determined.
	 * 
	 * @param pType the type definition
	 * @return the data type
	 */
	public static IDataType getDataType(String pType)
	{
		return cachedDataTypes.get(pType);
	}

	/**
	 * Gets the data row for the specific type definition and column name.
	 * This method has to be called after <code>getCellEditor</code>.
     * Preconfigured key names are not allowed here, as the configured dynamic cell editor cannot be determined.
	 * 
	 * @param pType the type definition
	 * @param pColumnName the column name
	 * @return the data row
	 */
	public static DataRow getDataRow(String pType, String pColumnName)
	{
		Map<String, DataRow> dataRows = cachedRenderingDataRows.get(pType);
		
		DataRow dataRow = dataRows.get(pColumnName);
		if (dataRow == null)
		{
			dataRow = new DataRow();
			try 
			{
				dataRow.getRowDefinition().addColumnDefinition(new ColumnDefinition(pColumnName, cachedDataTypes.get(pType)));
				
				ICellEditor cellEditor = cachedCellEditors.get(pType);
				if (cellEditor instanceof UILinkedCellEditor)
				{
				    UILinkedCellEditor linkedCellEditor = (UILinkedCellEditor)cellEditor;
				    ColumnMapping columnMapping = linkedCellEditor.getSearchColumnMapping();
				    
				    if (columnMapping != null)
				    {
				        IRowDefinition rowDef = linkedCellEditor.getLinkReference().getReferencedDataBook().getRowDefinition(); 
				        
				        String[] columnNames = columnMapping.getColumnNames();
                        String[] referencedColumnNames = columnMapping.getColumnNames();
				        
				        for (int i = 0; i < columnNames.length; i++)
				        {
				            dataRow.getRowDefinition().addColumnDefinition(
				                    new ColumnDefinition(columnNames[i], rowDef.getColumnDefinition(referencedColumnNames[i]).getDataType()));
				        }
				    }
				}
				
			} 
			catch (ModelException e) 
			{
				e.printStackTrace();
			}
			
			dataRows.put(pColumnName, dataRow);
		}
		
		return dataRow;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
     * Own CellEditorHandler to forward the correct editor component.
     * 
     * @author Martin Handsteiner
     */
    public static class CellEditorHandler implements ICellEditorHandler<Object>, IDataRowListener
    {
    	/** The CellEditor, that created this handler. */
    	private UIDynamicCellEditor cellEditor;

    	/** The CellEditorListener to inform, if editing is started or completed. */
    	private ICellEditorListener cellEditorListener;
    	
    	/** The data row that is edited. */
    	private IDataRow dataRow;
    	
    	/** The column name of the edited column. */
    	private String columnName;
    	
        /** The column names to be copied in cancel editing. */
        private String[] columnNames;
        
    	/** The internal type. */
    	private String internalType;
    	
    	/** The internal cell editor. */
    	private UICellEditor internalCellEditor;
    	
    	/** The internal datarow. */
    	private IDataRow internalDataRow;

    	/** The internal cellEditorHandler. */
    	private ICellEditorHandler<Object> internalCellEditorHandler;

    	/** Ignore saving. */
    	private boolean ignoreSave = false;
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Constructs a new CellEditorHandler.
    	 * 
    	 * @param pCellEditor the CellEditor that created this handler.
    	 * @param pCellEditorListener CellEditorListener to inform, if editing is started or completed.
    	 * @param pDataRow the data row that is edited.
    	 * @param pColumnName the column name of the edited column.
    	 */
    	public CellEditorHandler(UIDynamicCellEditor pCellEditor, ICellEditorListener pCellEditorListener,
    			                 IDataRow pDataRow, String pColumnName)
    	{
    		cellEditor = pCellEditor;
    		cellEditorListener = pCellEditorListener;
    		dataRow = pDataRow;
    		columnName = pColumnName;
    		
    		try
    		{
    			internalType = cellEditor.getType(pDataRow.getValueAsString(pColumnName + "_TYPE"));
    		}
    		catch (Exception ex)
    		{
    			// Ignore
    		}
    		
    		internalCellEditor = UIDynamicCellEditor.getCellEditor(internalType, dataRow);
    		
    		try 
    		{
				internalDataRow = UIDynamicCellEditor.getDataRow(internalType, columnName).createDataRow(null);
				
				internalDataRow.eventValuesChanged().addListener(this);
				
				columnNames = internalDataRow.getRowDefinition().getColumnNames();
			}
    		catch (ModelException e) 
    		{
				// Ignore
			}
    		
    		internalCellEditorHandler = internalCellEditor.createCellEditorHandler(pCellEditorListener, internalDataRow, columnName);
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
		@Override
		public void uninstallEditor() 
		{
			internalCellEditorHandler.uninstallEditor();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ICellEditor getCellEditor() 
		{
			return cellEditor;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ICellEditorListener getCellEditorListener() 
		{
			return cellEditorListener;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IDataRow getDataRow() 
		{
			return dataRow;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getColumnName() 
		{
			return columnName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getCellEditorComponent() 
		{
			return internalCellEditorHandler.getCellEditorComponent();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveEditing() throws ModelException 
		{
		    if (!ignoreSave)
		    {
    		    ignoreSave = true;
    		    try
    		    {
    		        internalCellEditorHandler.saveEditing();
    		    }
    		    finally
    		    {
    		        ignoreSave = false;
    		    }
    			
    			IDataType dataType = internalDataRow.getRowDefinition().getColumnDefinition(columnName).getDataType();
    			
    			dataRow.setValue(columnName, dataType.convertToUnifiedString(internalDataRow.getValue(columnName)));
		    }
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void cancelEditing() throws ModelException 
		{
			String type = null;
			try
    		{
				type = cellEditor.getType(dataRow.getValueAsString(columnName + "_TYPE"));
    		}
    		catch (Exception ex)
    		{
    			// Ignore
    		}

			if (!CommonUtil.equals(type, internalType) && cellEditorListener.getControl() instanceof IEditorControl)
			{
				((IEditorControl)cellEditorListener.getControl()).setDataRow(dataRow);
			}
			else
			{
				ColumnDefinition colDefDataRow = dataRow.getRowDefinition().getColumnDefinition(columnName);
				ColumnDefinition colDefInternalDataRow = internalDataRow.getRowDefinition().getColumnDefinition(columnName);
				
				colDefInternalDataRow.setNullable(colDefDataRow.isNullable());

				boolean editable = !colDefDataRow.isReadOnly();
				if (editable && dataRow instanceof IDataBook)
                {
                    IDataBook dataBook = (IDataBook)dataRow;
                    editable = dataBook.isUpdateAllowed();
                    if (editable && dataBook.getReadOnlyChecker() != null)
                    {
                        try
                        {
                            editable = !dataBook.getReadOnlyChecker().isReadOnly(dataBook, dataBook.getDataPage(), dataBook, columnName, dataBook.getSelectedRow(), -1);
                        }
                        catch (Throwable pTh)
                        {
                            // Ignore
                        }
                    }
                }
				colDefInternalDataRow.setReadOnly(!editable);

				ignoreSave = true;
				try
				{
				    internalDataRow.setValues(columnNames, dataRow.getValues(columnNames));
				}
				catch (Exception ex)
				{
                    internalDataRow.setValues(columnNames, null);
				}
				finally
				{
	                ignoreSave = false;
				}
                
				internalCellEditorHandler.cancelEditing();
			}
		}

		/**
		 * We have to check changed values, as the mobile client directly writes into the datarow. 
		 */
        @Override
        public void valuesChanged(DataRowEvent pDataRowEvent) throws Throwable
        {
            saveEditing();
        }
        
    }

}	// UIDynamicCellEditor
