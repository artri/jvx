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
 * 11.10.2008 - [RH] - toString() optimized, (DataBook) checks changed to IDataBook
 * 24.11.2008 - [RH] - changed to get/setReferencedDataBook()
 * 09.04.2009 - [RH] - interface review - IReferenceDefinition removed
 * 18.04.2009 - [RH] - NLS removed
 * 12.06.2009 - [JR] - toString: used StringBuilder [PERFORMANCE]
 */
package javax.rad.model.reference;

import javax.rad.model.IDataBook;

/**
 * The <code>ReferenceDefinition</code> specifies the relation between a detail 
 * <code>DataBook</code> and the master <code>DataBook</code>. <br>
 * It also can be used as binding between a drop down list (with an 
 * list of items to choose from) and the corresponding 
 * "master" <code>DataBook</code> to write the defined item (e.g. PrimaryKey columns
 * to ForeignKey columns) back to it.
 * 
 * <br><br>Example:
 * <pre>
 * <code>
 * ReferenceDefinition bdDETAILtoTEST = new ReferenceDefinition();
 * bdDETAILtoTEST.setReferencedDataBook(dbDataBook);
 * bdDETAILtoTEST.setReferencedColumns(new String [] { "ID" });
 * bdDETAILtoTEST.setColumns(new String [] { "TEST_ID" });
 * 
 * DataBook dbDataBook = new DataBook();
 * dbDataBook.setDataSource(dba);
 * dbDataBook.setName("TEST");
 * 
 * DataBook dbDetail = new DataBook();
 * dbDetail.setDataSource(dba);
 * dbDetail.setName("DETAIL");
 * dbDetail.setMasterReference(bdDETAILtoTEST);
 * dbDetail.open();
 * 
 * dbDataBook.open();
 * </code>
 * </pre>
 *  
 * @see com.sibvisions.rad.model.remote.RemoteDataBook
 *  
 * @author Roland Hörmann
 */
public class ReferenceDefinition extends ColumnMapping
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The referenced (master) <code>IDataBook</code> of the <code>ReferenceDefinition</code>. */
	private IDataBook	drReferencedDataBook;

	/**
	 * Indicates if the source is connected, and changes are not allowed. If its
	 * connected to an open <code>IDataBook</code>, then the value is true.
	 */
	private boolean		bConnected	= false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs the <code>ReferenceDefinition</code> without parameters.
	 */
	public ReferenceDefinition()
	{ 
	}

	/**
	 * Constructs the <code>ReferenceDefinition</code> with the specified parameters.
	 * 
	 * @param pColumnNames the source column names to use in this <code>ReferenceDefinition</code>
	 * @param pReferencedDataBook the referenced <code>IDataBook</code> of the <code>ReferenceDefinition</code>
	 * @param pReferencedColumnNames the referenced column names to use in this <code>ReferenceDefinition</code>
	 */
	public ReferenceDefinition(String[] pColumnNames, IDataBook pReferencedDataBook, String[] pReferencedColumnNames)
	{
		setColumnNames(pColumnNames);
		setReferencedDataBook(pReferencedDataBook);
		setReferencedColumnNames(pReferencedColumnNames);
	}
	
	/**
	 * Constructs the <code>ReferenceDefinition</code> with the specified parameters.
	 * 
	 * @param pColumnName the source column name to use in this <code>ReferenceDefinition</code>
	 * @param pReferencedDataBook the referenced <code>IDataBook</code> of the <code>ReferenceDefinition</code>
	 * @param pReferencedColumnName the referenced column name to use in this <code>ReferenceDefinition</code>
	 */
	public ReferenceDefinition(String pColumnName, IDataBook pReferencedDataBook, String pReferencedColumnName)
	{
		if (pColumnName != null)
		{
			setColumnNames(new String[] {pColumnName});
		}
	
		setReferencedDataBook(pReferencedDataBook);
		
		if (pReferencedColumnName != null)
		{
			setReferencedColumnNames(new String[] {pReferencedColumnName});
		}
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
		StringBuilder sbResult = new StringBuilder();
		
		sbResult.append("{");
		sbResult.append(super.toString());
		sbResult.append(", referencedDataBook=");
		sbResult.append(drReferencedDataBook == null ? null : drReferencedDataBook.getName());
		sbResult.append("}");

        return sbResult.toString();
	}
	
	   /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object pObject)
    {
        if (this == pObject)
        {
            return true;
        }
        
        if (!super.equals(pObject))
        {
            return false;
        }
        
        ReferenceDefinition rdCompare = (ReferenceDefinition)pObject;
        
        if (drReferencedDataBook != rdCompare.drReferencedDataBook)
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return 11 * super.hashCode() + ((drReferencedDataBook == null) ? 0 : System.identityHashCode(drReferencedDataBook));
    }   

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the referenced <code>IDataBook</code> of the <code>ReferenceDefinition</code>.
	 * 
	 * @param pReferencedDataBook
	 *            the referenced (master) <code>IDataBook</code>
	 */
	public void setReferencedDataBook(IDataBook pReferencedDataBook)
	{
		if (bConnected)
		{
			throw new IllegalArgumentException("ReferenceDefintion is already connect to source!");
		}		
		drReferencedDataBook = pReferencedDataBook;
	}

	/**
	 * Returns the referenced (master) <code>IDataBook</code> of the <code>ReferenceDefinition</code>.
	 * 
	 * @return the referenced (master) <code>IDataBook</code> of the <code>ReferenceDefinition</code>.
	 */
	public IDataBook getReferencedDataBook()
	{
		return drReferencedDataBook;
	}
	
	/**
	 * Sets the referenced (master) column names to use in this <code>ReferenceDefinition</code>.<br>
	 * 
	 * @param pReferencedColumnNames
	 *            the referenced (master) column names to use 
	 */
	public void setReferencedColumnNames(String[] pReferencedColumnNames)
	{
		if (bConnected)
		{
			throw new IllegalArgumentException("ReferenceDefintion is already connect to source!");
		}
		super.setReferencedColumnNames(pReferencedColumnNames);
	}

	/**
	 * Sets the source (detail) <code>ColumnDefinition</code>'s to use in this 
	 * <code>ReferenceDefinition</code>.<br>
	 * 
	 * @param pColumnNames the source (detail) column names
	 */
	public void setColumnNames(String[] pColumnNames)
	{
		if (bConnected)
		{
			throw new IllegalArgumentException("ReferenceDefintion is already connect to source!");
		}		
		super.setColumnNames(pColumnNames);
	}

	/**
	 * Sets the <code>ReferenceDefinition</code> connected to the source 
	 * <code>IDataBook</code>. Internal use only!
	 */
	public void setConnected()
	{
		bConnected = true;
	}
	
    /**
     * Sets the <code>ReferenceDefinition</code> unconnected to the source 
     * <code>IDataBook</code>. Internal use only!
     */
    public void setUnconnected()
    {
        bConnected = false;
    }
    
} 	// ReferenceDefinition
