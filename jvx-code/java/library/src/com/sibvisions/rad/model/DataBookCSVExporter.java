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
 * 29.11.2010 - [JR] - creation
 * 08.03.2012 - [JR] - #556: list separator used
 * 25.03.2012 - [JR] - #567: writeQuoted - check null value
 * 05.02.2015 - [JR] - #1254: use default charset for CSV creation
 * 09.12.2015 - [JR] - simple writeCSV methods
 */
package com.sibvisions.rad.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.persist.ColumnMetaData;

import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>DataBookUtil</code> handles simple access to databooks. It allows export/import of
 * data.
 * 
 * @author René Jahn
 */
public final class DataBookCSVExporter
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the user-defined default encoding. */
    private static String sDefaultEncoding;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>DataBookUtil</code> is a utility
	 * class.
	 */
	private DataBookCSVExporter()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Writes all rows and values of a databook, as comma separated values to a
     * stream.
     * 
     * @param pBook the databook
     * @param pStream the output stream
     * @throws ModelException if the access to rows or values of the databook fails
     * @throws IOException if an error occurs during writing
     */
    public static void writeCSV(IDataBook pBook, OutputStream pStream) throws ModelException,
                                                                              IOException
    {
        writeCSV(pBook, pStream, null);
    }

    /**
     * Writes all rows and values of a databook, as comma separated values to a
     * stream.
     * 
     * @param pBook the databook
     * @param pStream the output stream
     * @param pEncoding the character encoding
     * @throws ModelException if the access to rows or values of the databook fails
     * @throws IOException if an error occurs during writing
     */
    public static void writeCSV(IDataBook pBook, OutputStream pStream, String pEncoding) throws ModelException,
                                                                                                IOException
	{
	    writeCSV(pBook, pStream, null, null, null, null, null, pEncoding);
	}
	
	/**
	 * Writes all rows and values of a databook, as comma separated values to a
	 * stream.
	 * 
	 * @param pBook the databook
	 * @param pStream the output stream
	 * @param pColumnNames specific column names or <code>null</code> to use the
	 *                     column names from the current {@link javax.rad.model.ColumnView}
	 * @param pLabels the preferred column labels
	 * @param pFilter the export filter
	 * @param pSort the export sort definition
	 * @throws ModelException if the access to rows or values of the databook fails
	 * @throws IOException if an error occurs during writing
	 */
	public static void writeCSV(IDataBook pBook, 
			                    OutputStream pStream, 
			                    String[] pColumnNames, 
			                    String[] pLabels, 
			                    ICondition pFilter, 
			                    SortDefinition pSort) throws ModelException, 
			                                                 IOException
	{
		writeCSV(pBook, pStream, pColumnNames, pLabels, pFilter, pSort, null);
	}

    /**
     * Writes all rows and values of a databook, as comma separated values to a stream.
     * 
     * @param pBook the databook
     * @param pStream the output stream
     * @param pColumnNames specific column names or <code>null</code> to use the column names
     *                     from the current {@link javax.rad.model.ColumnView}
     * @param pLabels the preferred column labels
     * @param pFilter the export filter
     * @param pSort the export sort definition
     * @param pSeparator the list separator 
     * @throws ModelException if the access to rows or values of the databook fails
     * @throws IOException if an error occurs during writing
     */
    public static void writeCSV(IDataBook pBook, 
                                OutputStream pStream, 
                                String[] pColumnNames, 
                                String[] pLabels, 
                                ICondition pFilter, 
                                SortDefinition pSort,
                                String pSeparator) throws ModelException,
                                                          IOException
    {
        writeCSV(pBook, pStream, pColumnNames, pLabels, pFilter, pSort, pSeparator, null);
    }                                 	
	
	/**
	 * Writes all rows and values of a databook, as comma separated values to a stream.
	 * 
	 * @param pBook the databook
	 * @param pStream the output stream
	 * @param pColumnNames specific column names or <code>null</code> to use the column names
	 *                     from the current {@link javax.rad.model.ColumnView}
	 * @param pLabels the preferred column labels
	 * @param pFilter the export filter
	 * @param pSort the export sort definition
	 * @param pSeparator the list separator 
	 * @param pEncoding the character encoding
	 * @throws ModelException if the access to rows or values of the databook fails
	 * @throws IOException if an error occurs during writing
	 */
	public static void writeCSV(IDataBook pBook, 
			                    OutputStream pStream, 
			                    String[] pColumnNames, 
			                    String[] pLabels, 
			                    ICondition pFilter, 
			                    SortDefinition pSort,
			                    String pSeparator,
			                    String pEncoding) throws ModelException,
			                                             IOException
	{
		ICondition condOld = pBook.getFilter();
		SortDefinition sortOld = pBook.getSort();
		
		int iOldSelected = pBook.getSelectedRow();

		
		String sSeparator = pSeparator;
		
		if (sSeparator == null)
		{
			sSeparator = LocaleUtil.getListSeparator();
		}
		
		OutputStreamWriter out = null;
		
		try
		{
			out = new OutputStreamWriter(pStream, pEncoding == null ? getDefaultEncoding() : pEncoding);
			
			if (pFilter != null)
			{
				pBook.setFilter(pFilter);
			}
			
			if (pSort != null)
			{
				pBook.setSort(pSort);
			}
			
			if (pColumnNames == null)
			{
				pColumnNames = pBook.getRowDefinition().getColumnView(null).getColumnNames();
			}
			
			if (pLabels == null)
			{
				pLabels = new String[pColumnNames.length];
				
				for (int i = 0; i < pColumnNames.length; i++)
				{
					pLabels[i] = pBook.getRowDefinition().getColumnDefinition(pColumnNames[i]).getLabel();
					
					if (pLabels[i] == null)
					{
						pLabels[i] = ColumnMetaData.getDefaultLabel(pColumnNames[i]);
					}
				}
			}
			
			// write column headers with the defined label
			for (int i = 0; i < pLabels.length; i++)
			{
				if (i > 0)
				{
					out.write(sSeparator);
				}
				
				out.write(StringUtil.quote(pLabels[i], '"'));
			}
			
			out.write("\n");
			
			pBook.fetchAll();
			
			Object[] oValues;
			
			IDataType[] dtColumns = new IDataType[pColumnNames.length];
			
			//write rows
			for (int i = 0, anz = pBook.getRowCount(); i < anz; i++)
			{
				pBook.setSelectedRow(i);
				oValues = pBook.getValues(pColumnNames);
				
				for (int j = 0; j < pColumnNames.length; j++)
				{
					if (j > 0)
					{
						out.write(sSeparator);
					}
					
					if (oValues[j] != null)
					{
						if (dtColumns[j] == null)
						{
							dtColumns[j] = pBook.getRowDefinition().getColumnDefinition(pColumnNames[j]).getDataType(); 
						}

						writeQuoted(out, dtColumns[j], oValues[j], sSeparator);
					}
				}
				
				out.write("\n");			
			}
			
			out.flush();
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (Exception e)
				{
					//nothing to be done
				}
			}
			
			pBook.setSort(sortOld);
			pBook.setFilter(condOld);
			
			pBook.setSelectedRow(iOldSelected);
		}
	}
	
	/**
	 * Formats a name as filename for a csv export.
	 * 
	 * @param pName a databook name or filename. Predefined extensions will be re-used.
	 * @return the filename for the export. If the <code>pName</code> is <code>null</code> then
	 *         <code>Export.csv</code> is used. 
	 */
	public static String formatCSVFileName(String pName)
	{
		if (pName == null)
		{
			return "Export.csv";
		}
		else
		{
			String sExtension = FileUtil.getExtension(pName);
			
			if (sExtension == null || "".equals(sExtension.trim()))
			{
				sExtension = "csv";
			}
		
			return StringUtil.formatInitCap(FileUtil.removeExtension(pName)) + "." + sExtension;
		}
	}
	
	/**
	 * Writes an object as quoted string. Quotes are only added, if needed.
	 *  
	 * @param pStream the output stream
	 * @param pDataType the datatype for the given value
	 * @param pValue the value
	 * @param pSeparator the column separator
	 * @throws IOException if a write error occurs
	 */
	public static void writeQuoted(OutputStreamWriter pStream, IDataType pDataType, Object pValue, String pSeparator) throws IOException
	{
		//#567
		if (pValue != null)
		{
			switch (pDataType.getTypeIdentifier())
			{
			    case StringDataType.TYPE_IDENTIFIER:
			        pStream.write(StringUtil.quote(pDataType.convertToString(pValue), '"'));
			        break;
//			    case BigDecimalDataType.TYPE_IDENTIFIER:
//			    case BooleanDataType.TYPE_IDENTIFIER:
//			    case BinaryDataType.TYPE_IDENTIFIER:
//				case TimestampDataType.TYPE_IDENTIFIER:
				default:
					String sValue = pDataType.convertToString(pValue);
					
					if (sValue.indexOf(pSeparator) >= 0)
					{
						pStream.write(StringUtil.quote(sValue, '"'));
					}
					else
					{
						pStream.write(sValue);
					}
					break;
			}
		}		
	}

	/**
	 * Sets the default encoding for CSV exports.
	 * 
	 * @param pEncoding the encoding
	 */
	public static void setDefaultEncoding(String pEncoding)
	{
	    sDefaultEncoding = pEncoding;
	}
	
	/**
	 * Get the default encoding for CSV exports. If no user-defined default encoding was set,
	 * the system property "file.encoding" will be used, if possible. The method {@link Charset#defaultCharset()} 
	 * acts as fallback.
	 * 
	 * @return the user-defined default encoding or system detected default encoding for CSV exports
	 */
	public static String getDefaultEncoding()
	{
	    if (StringUtil.isEmpty(sDefaultEncoding))
	    {
            String sEncoding;
            
            try
            {
                sEncoding = System.getProperty("file.encoding");
            }
            catch (Exception e)
            {
                try
                {
                    sEncoding = Charset.defaultCharset().name();
                }
                catch (Exception ex)
                {
                    sEncoding = "ISO-8859-15";
                }
            }
            
            return sEncoding;
	    }
	    else
	    {
	        return sDefaultEncoding;
	    }
	}
	
}	// DataBookUtil
