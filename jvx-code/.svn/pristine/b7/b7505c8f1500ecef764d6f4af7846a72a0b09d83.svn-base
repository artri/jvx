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
 * 17.11.2009 - [RH] - creation
 * 23.02.2013 - [JR] - equals and hashCode implemented
 */
package javax.rad.model.reference;


/**
 * The <code>StorageReferenceDefinition</code> specifies the relation between a detail 
 * <code>IStorage</code> and the master <code>IStorage</code>. <br>
 * It also can be used as "server side" binding between a drop down list (with an 
 * list of items to choose from) and the corresponding 
 * "master" <code>DataBook</code> to write the defined item (e.g. PrimaryKey columns
 * to ForeignKey columns) back to it.
 * 
 * <br><br>Example:
 * <pre>
 * <code>
 * StorageReferenceDefinition bdDETAILtoTEST = new StorageReferenceDefinition();
 * bdDETAILtoTEST.setReferencedStorage("test");
 * bdDETAILtoTEST.setReferencedColumns(new String [] { "ID" });
 * bdDETAILtoTEST.setColumns(new String [] { "TEST_ID" });
 * </code>
 * </pre>
 *  
 * @author Roland Hörmann
 */
public class StorageReferenceDefinition extends ColumnMapping
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The "name" of referenced (master) <code>IStorage</code> of the <code>StorageReferenceDefinition</code>. */
	private String sReferencedStorage;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs the <code>StorageReferenceDefinition</code> without parameters.
	 */
	public StorageReferenceDefinition()
	{ 
	}

	/**
	 * Constructs the <code>StorageReferenceDefinition</code> with the specified parameters.
	 * 
     * @param pColumnNames the source column names to use in this <code>StorageReferenceDefinition</code>
     * @param pReferencedStorage the "name" of referenced <code>IStorage</code> of the <code>StorageReferenceDefinition</code>
     * @param pReferencedColumnNames the referenced column names to use in this <code>StorageReferenceDefinition</code>
	 */
	public StorageReferenceDefinition(String[] pColumnNames, String pReferencedStorage, String[] pReferencedColumnNames)
	{
		setColumnNames(pColumnNames);
		setReferencedStorage(pReferencedStorage);
		setReferencedColumnNames(pReferencedColumnNames);
	}

    /**
     * Constructs the <code>StorageReferenceDefinition</code> with the specified parameters.
     * 
     * @param pColumnName the source column names to use in this <code>StorageReferenceDefinition</code>
     * @param pReferencedStorage the "name" of referenced <code>IStorage</code> of the <code>StorageReferenceDefinition</code>
     * @param pReferencedColumnName the referenced column names to use in this <code>StorageReferenceDefinition</code>
     */
    public StorageReferenceDefinition(String pColumnName, String pReferencedStorage, String pReferencedColumnName)
    {
        if (pColumnName != null)
        {
            setColumnNames(new String[] {pColumnName});
        }
    
        setReferencedStorage(pReferencedStorage);
        
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
		sbResult.append(", referencedStorage=");
		sbResult.append(sReferencedStorage);
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
		
		StorageReferenceDefinition srdCompare = (StorageReferenceDefinition)pObject;
		
		if (sReferencedStorage == null)
		{
			if (srdCompare.sReferencedStorage != null)
			{
				return false;
			}
		}
		else if (!sReferencedStorage.equals(srdCompare.sReferencedStorage))
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
		return 11 * super.hashCode() + ((sReferencedStorage == null) ? 0 : sReferencedStorage.hashCode());
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the "name" of referenced <code>IStorage</code> of the <code>StorageReferenceDefinition</code>.
	 * 
	 * @param pReferencedStorage
	 *            the "name" of the referenced (master) <code>IStorage</code>
	 */
	public void setReferencedStorage(String pReferencedStorage)
	{
		sReferencedStorage = pReferencedStorage;
	}

	/**
	 * Returns the "name" of the referenced (master) <code>IStorage</code> of the <code>StorageReferenceDefinition</code>.
	 * 
	 * @return the "name" of the referenced (master) <code>IStorage</code> of the <code>StorageReferenceDefinition</code>.
	 */
	public String getReferencedStorage()
	{
		return sReferencedStorage;
	}

} 	// StorageReferenceDefinition
