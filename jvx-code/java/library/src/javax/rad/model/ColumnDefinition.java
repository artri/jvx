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
 * 01.10.2008 - [RH] - creation
 * 13.11.2008 - [RH] - getDefaultLabel; clone() optimized
 * 08.04.2009 - [RH] - interface review - base interface ColumnDefinition removed
 * 18.04.2009 - [RH] - javadoc updated, NLS removed
 * 06.03.2011 - [JR] - #115: setAllowedValues, setDefaultValue implemented
 * 31.03.2011 - [JR] - #318: invokeRepaintListeners implemented
 * 31.03.2011 - [RH] - #163: IChangeableDataRow should support isWritableColumnChanged
 *                           -> isWriteable is default == false, because it shouldn't write back to the server, if it is added on client! 
 */
package javax.rad.model;

import java.io.Serializable;

import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.StringDataType;
import javax.rad.model.ui.IControl;
import javax.rad.persist.ColumnMetaData;

import com.sibvisions.util.type.StringUtil;

/**
 * A <code>ColumnDefinition</code> is a description of the data type and other
 * attributes of a table column.
 * 
 * <br><br>Example:
 * <pre>
 * <code>
 * ColumnDefinition cdName = new ColumnDefinition("name");
 * 
 * // set properties
 * cdName.setWidth(10);
 * cdName.setLabel("Name");
 * cdName.setNull(true);
 * cdName.setReadOnly(true);
 * cdName.setWritable(false);
 * 
 * // set RowDefinition;
 * RowDefinition rdRowDefinition = new RowDefinition();
 * cdName.setRowDefinition(rdRowDefinition);  
 * </code>
 * </pre>
 *  
 * @see javax.rad.model.IRowDefinition
 * @see javax.rad.model.datatype.IDataType
 * 
 * @author Roland Hörmann
 */
public class ColumnDefinition implements Serializable, 
                                         Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The <code>RowDefinition</code>, which this <code>ColumnDefiniton</code> belong to. */
	private transient IRowDefinition	rdRowDefinition;

	/** The name of the <code>ColumnDefinition</code>. */
	private String			sName;

	/** The <code>DataType</code> of the <code>ColumnDefinition</code>. */
	private IDataType		ctDataType;

	/** The label of the <code>ColumnDefinition</code>. */
	private String			sLabel;

	/** The default label of the <code>ColumnDefinition</code>. */
    private String          sDefaultLabel;
	
	/** If this <code>ColumnDefinition</code> can be null. */
	private boolean			bNullable = true;

	/** The comment of the <code>ColumnDefinition</code>. */
	private String			sComment;
	
	/** the default value for the column. */
	private Object 			oDefault;
	
	/** the allowed values for the column. */
	private Object[]		oAllowedValues;
	
	/** If this <code>ColumnDefinition</code> is read only. */
	private boolean			bReadonly;

	/** If this <code>ColumnDefinition</code> should be stored. */
	private boolean			bWriteable	= false;

	/** If this <code>ColumnDefinition</code> should be filtered be generic filters. */
	private boolean			bFilterable	= true;
	
	/** Whether this column is required in the filter. */
	private boolean         bFilterRequired = false;

	/** The size of this <code>ColumnDefinition</code>, which is used in GUI control to display. */
	private int			    iWidth;
	
	/** Indicates if this <code>ColumnDefinition</code> can be moved. */
	private boolean			bMoveable	= true;

	/** Indicates if this <code>ColumnDefinition</code> can be resized. */
	private boolean			bResizeable	= true;

	/** Indicates if this <code>ColumnDefinition</code> can be sorted. */
	private boolean			bSortable	= false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a <code>ColumnDefinition</code>.
	 */
	public ColumnDefinition()
	{
		this(null, new StringDataType());
	}

	/**
	 * Constructs a <code>ColumnDefinition</code> with the specified name.
	 * 
	 * @param pName
	 *            is the for every <code>RowDefinition</code> unique name
	 */
	public ColumnDefinition(String pName)
	{
		this(pName, new StringDataType());
	}

	/**
	 * Constructs a <code>ColumnDefinition</code> with the specified name.
	 * 
	 * @param pName
	 *            is the for every <code>RowDefinition</code> unique name
	 * @param pDataType
	 *            is the <code>IDataType</code> of the <code>ColumnDefinition</code> 
	 */	
	public ColumnDefinition(String pName, IDataType pDataType)
	{
		sName = pName;
		ctDataType = pDataType;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("ColumnDefinition ::[");
		sb.append(sName);
		sb.append("]");
		sb.append(",Type=");
		sb.append(ctDataType);
		sb.append(",Label=");
		sb.append(sLabel);
		sb.append(",Nullable=");
		sb.append(bNullable);
		sb.append(",Readonly=");
		sb.append(bReadonly);
		sb.append(",Writeable=");
		sb.append(bWriteable);
		sb.append(",Filterable=");
		sb.append(bFilterable);
		sb.append(",FilterRequired=");
		sb.append(bFilterRequired);
		sb.append(",Width=");
		sb.append(iWidth);
		sb.append(",Moveable=");
		sb.append(bMoveable);
		sb.append(",Resizeable=");
		sb.append(bResizeable);
		sb.append(",Sortable=");
		sb.append(bSortable);
		sb.append(",Default=");
		sb.append(oDefault);
		sb.append(",Allowed=");
		sb.append(StringUtil.toString(oAllowedValues));
		
		return sb.toString();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Clone an <code>ColumnDefinition</code>.
	 * 
	 * @see java.lang.Object#clone()
	 * 
	 * @return a clone of this <code>ColumnDefinition</code>
	 */
	public ColumnDefinition clone()
	{		
		try 
		{
			ColumnDefinition cdResult = (ColumnDefinition) super.clone();
		
			cdResult.rdRowDefinition = null;
			cdResult.ctDataType = ctDataType.clone();
			
			if (oAllowedValues != null)
			{
				cdResult.oAllowedValues = oAllowedValues.clone();
			}
			
			return cdResult;
		}
		catch (Exception exception)
		{
			return null;
		}
	}
	
	/**
	 * Sets the <code>IRowDefinition</code> for this <code>ColumnDefinition</code>.
	 * One <code>ColumnDefinition</code> belongs to exact one <code>IRowDefinition</code>.
	 * 
	 * @param pRowDefinition
	 *            the <code>IRowDefinition</code> where this <code>ColumnDefinition</code>
	 *            will be added
	 * @throws ModelException
	 *             if the <code>ColumnDefinition</code> already added to an other 
	 *             <code>RowDefinition</code>
	 * @see javax.rad.model.IRowDefinition
	 */
	public void setRowDefinition(IRowDefinition pRowDefinition) throws ModelException
	{
		if (rdRowDefinition != null && pRowDefinition != null)
		{
			throw new ModelException("ColumnDefinition already added to an RowDefinition! - " + 
							         rdRowDefinition.toString());
		}
		rdRowDefinition = pRowDefinition;
	}

	/**
	 * Returns the <code>IRowDefinition</code> for this <code>ColumnDefinition</code>. 
	 * One <code>ColumnDefinition</code> belongs to exact one <code>IRowDefinition</code>.
	 * 
	 * @return the <code>IRowDefinition</code> where this <code>ColumnDefinition</code>
	 * 		   will be added.
	 * @see javax.rad.model.IRowDefinition
	 */
	public IRowDefinition getRowDefinition()
	{
		return rdRowDefinition;
	}

	/**
	 * Set the name of the column.
	 * 
	 * @param pName
	 *            column name
	 * @throws ModelException
	 * 			  if the <code>ColumnDefinition</code> already added to another 
	 * 			  <code>RowDefinition</code>	 
	 */
	public void setName(String pName) throws ModelException
	{
		if (rdRowDefinition != null)
		{
			throw new ModelException("ColumnDefinition already added to an RowDefinition! - " + 
			                         rdRowDefinition.toString());
		}
		if (pName == null)
		{
			throw new ModelException("Column name have to be != null!");
			
		}
		sName = pName;
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */
	public String getName()
	{
		return sName;
	}

	/**
	 * Sets the <code>IDataType</code> for this <code>ColumnDefinition</code>.
	 * 
	 * @param pDataType
	 *            the <code>IDataType</code> for this <code>ColumnDefinition</code>.
	 * @throws ModelException
	 * 			  if the <code>ColumnDefinition</code> already added to another 
	 * 			  <code>RowDefinition</code>	 
	 * @see IDataType
	 */
	public void setDataType(IDataType pDataType) throws ModelException
	{
		if (rdRowDefinition != null)
		{
			throw new ModelException("ColumnDefinition already added to an RowDefinition! - " + 
                                     rdRowDefinition.toString());
		}
		ctDataType = pDataType;
	}

	/**
	 * Returns the <code>IDataType</code> for this <code>ColumnDefinition</code>.
	 * 
	 * @return ctDataType the <code>IDataType</code> for this <code>ColumnDefinition</code>.
	 * @see IDataType
	 */
	public IDataType getDataType()
	{
		return ctDataType;
	}

	/**
	 * Sets whether values in this column may be null.
	 * 
	 * @param pNullable
	 *            true if values in this column may be null.
	 */
	public void setNullable(boolean pNullable)
	{
		bNullable = pNullable;
		
		invokeRepaintListeners();		
	}

	/**
	 * Returns true if values in this column may be null.
	 * 
	 * @return true if values in this column may be null.
	 */
	public boolean isNullable()
	{
		return bNullable;
	}

	/**
	 * Sets whether this column will be stored. If the column isWritable() == false,
	 * implementations of the <code>IDataBook</code> interface will not 
	 * store values for this column.
	 * 
	 * @param pStorable
	 *            true if column will be stored.
	 * @see javax.rad.model.IDataBook
	 */
	public void setWritable(boolean pStorable)
	{
		bWriteable = pStorable;

		invokeRepaintListeners();
	}

	/**
	 * Returns whether this column will be stored. If the column isWritable() == false,
	 * implementations of the <code>IDataBook</code> interface will not store
	 * values for this column.
	 * 
	 * @return true if column will be stored.
	 * @see javax.rad.model.IDataBook
	 */
	public boolean isWritable()
	{
		return bWriteable;
	}
	
	/**
	 * Sets whether this column will be filtered by generic filters.
	 * 
	 * @param pFilterable
	 *            true if column will be filtered by generic filters.
	 */
	public void setFilterable(boolean pFilterable)
	{
		bFilterable = pFilterable;

		invokeRepaintListeners();
	}

	/**
	 * true, if this column will be filtered by generic filters.
	 * 
	 * @return true
	 *            if column will be filtered by generic filters.
	 */
	public boolean isFilterable()
	{
		return bFilterable;
	}
	
	/**
	 * Sets whether this column is required in the filter or not.
	 * 
	 * @param pFilterRequired true if column is required in the filter
	 */
    public void setFilterRequired(boolean pFilterRequired)
    {
        bFilterRequired = pFilterRequired;
    }
    
    /**
     * Whether the column is required in the filter or not.
     * 
     * @return true if column is required in the filter
     */
    public boolean isFilterRequired()
    {
        return bFilterRequired;
    }

    /**
	 * Sets the label of this column. Its used as label for GUI controls.
	 * 
	 * @param pLabel
	 *            the label of the ColumnDefinition
	 */
	public void setLabel(String pLabel)
	{
		sLabel = pLabel;
		
		invokeRepaintListeners();
	}

	/**
	 * Returns the label of the column. Its used as label for GUI controls.
	 * 
	 * @return the label of the column.
	 */
	public String getLabel()
	{
		if (sLabel == null)
		{
			return getDefaultLabel();
		}
		else
		{
			return sLabel;
		}
	}

	/**
	 * Sets the comment for this column.
	 * 
	 * @param pComment
	 *            the comment for this column
	 */
	public void setComment(String pComment)
	{
		sComment = pComment;
	}

	/**
	 * Returns the comment for this column.
	 * 
	 * @return the comment for this column.
	 */
	public String getComment()
	{
		return sComment;
	}

	/**
	 * Returns the size of this column, which is used in GUI control to display. 
	 * 
	 * @return the size of this column, which is used in GUI control to display. 
	 */
	public int getWidth()
	{
		return iWidth;
	}

	/**
	 * Sets the size of this column, which is used in GUI control to display. 
	 * 
	 * @param pDisplaySize
	 *            the display size of this column in characters
	 */
	public void setWidth(int pDisplaySize)
	{
		iWidth = pDisplaySize;
	
//		TODO invokeRepaintListeners causes endless loop if more than one databook
//		invokeRepaintListeners();
	}

	/**
	 * Sets whether this column can be moved. Its used by column oriented controls
	 * like tables, ...
	 * 
	 * @param pMoveable
	 *            true if the column can be moved
	 */
	public void setMovable(boolean pMoveable)
	{
		bMoveable = pMoveable;
		
		invokeRepaintListeners();
	}

	/**
	 * Returns whether this column can be moved.
	 * 
	 * @return true if this column can be moved.
	 */
	public boolean isMovable()
	{
		return bMoveable;
	}

	/**
	 * Sets if this column can be resized. Its used by column oriented controls
	 * like tables, ...
	 * 
	 * @param pResizeable
	 *            true if the column can be resized
	 */
	public void setResizable(boolean pResizeable)
	{
		bResizeable = pResizeable;
		
		invokeRepaintListeners();
	}

	/**
	 * Returns whether this column can be resized.
	 * 
	 * @return true if the column can be resized.
	 */
	public boolean isResizable()
	{
		return bResizeable;
	}
	
	/**
	 * Sets whether this column is read only.
	 *  
	 * @param pReadOnly true if this column is read only.
	 */
	public void setReadOnly(boolean pReadOnly)
	{
		bReadonly = pReadOnly;
		
		invokeRepaintListeners();
	}

	/**
	 * Returns the read only state of this column.
	 * 
	 * @return true if this column is read only.
	 */
	public boolean isReadOnly()
	{
		return bReadonly;
	}
	
    /**
     * Sets the default label of this column.
     * 
     * @param pDefaultLabel the default label
     */
    public void setDefaultLabel(String pDefaultLabel)
    {
        sDefaultLabel = pDefaultLabel;
    }
    
	/**
	 * It creates an default column label.
	 * 
	 * @return the default column label
	 */
	public String getDefaultLabel()
	{
	    if (sDefaultLabel == null)
	    {
	        return ColumnMetaData.getDefaultLabel(sName);
	    }
	    else
	    {
	        return sDefaultLabel;
	    }
	}

	/**
	 * Sets the default value of this column.
	 * 
	 * @param pValue the default value
	 */
	public void setDefaultValue(Object pValue)
	{
		oDefault = pValue;
	}
	
	/**
	 * Gets the default value of this column.
	 * 
	 * @return the default value or <code>null</code> if the column has no default value
	 */
	public Object getDefaultValue()
	{
		return oDefault;
	}
	
	/**
	 * Sets the allowed values for this column.
	 * 
	 * @param pValues the allowed values or <code>null</code> when any value is allowed
	 */
	public void setAllowedValues(Object[] pValues)
	{
		oAllowedValues = pValues;
	}
	
	/**
	 * Gets the allowed values for this column.
	 * 
	 * @return an {@link Object}[] with values, possible for the column.
	 */
	public Object[] getAllowedValues()
	{
		return oAllowedValues;
	}

	/**
	 * Sets whether this column is sortable.
	 *  
	 * @param pSortable true if this column is sortable.
	 */
	public void setSortable(boolean pSortable)
	{
		bSortable = pSortable;
	}

	/**
	 * Returns whether this column can be sorted.
	 * 
	 * @return true if this column can be sorted.
	 */
	public boolean isSortable()
	{
		return bSortable;
	}
	
	/**
	 * Notifies all controls from the row definition that this column definition has changed. The {@link IControl#notifyRepaint()}
	 * method will be called.
	 */
	protected void invokeRepaintListeners()
	{		
		if (rdRowDefinition != null)
		{
			IControl[] controls = rdRowDefinition.getControls();
			
			for (int i = 0; i < controls.length; i++)
			{
				controls[i].notifyRepaint();
			}
		}
	}
	
}	// ColumnDefinition

