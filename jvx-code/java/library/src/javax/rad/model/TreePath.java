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
 * 15.09.2009 - [MH] - creation
 */
package javax.rad.model;

import java.util.Arrays;

/**
 * A <code>TreePath</code> stores the path of node in a tree.
 * It is stored as int array representing the selected row in the corresponding level.
 * eg:
 * [] empty array means tree path for the root page.
 * [1] the detail page for the row 1 of root page.
 * [1, 2] the detail page for the row 2 of the detail page for the row 1 of the root page.  
 * 
 * @author Martin Handsteiner
 */
public class TreePath
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Constant for an empty TreePath. */
	public static final TreePath EMPTY = new TreePath();
	
	/** The tree path. */
	private int[] array;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new TreePath.
	 * 
	 * @param pPath tree path.
	 */
	public TreePath(int... pPath)
	{
		array = pPath;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		int hashCode = 1;
     	for (int i = 0; i < array.length; i++)
     	{
		    hashCode = 31 * hashCode + array[i];
		}
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean equals(Object pObject)
    {
    	if (pObject == this)
    	{
    	    return true;
    	}
    	else if (pObject == null && array.length == 0)
    	{
    		return true;
    	}
    	else if (pObject instanceof TreePath)
    	{
    		TreePath treePath = (TreePath)pObject;
    		
    		if (array.length == treePath.array.length) 
    		{
    			for (int i = 0; i < array.length; i++)
    			{
    				if (array[i] != treePath.array[i])
   					{
        	    		return false;
   					}
    			}
    			return true;
    		}
    	}
   	    return false;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public String toString()
    {
		return Arrays.toString(array);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the length of the array.
	 * 
	 * @return the length of the array.
	 */
	public int length()
	{
		return array.length;
	}
	
	/**
	 * Gets the value of given index.
	 * 
	 * @param pIndex the index.
	 * @return the value of given index.
	 */
	public int get(int pIndex)
	{
		return array[pIndex];
	}

	/**
	 * Gets the value of the last index.
	 * 
	 * @return the value of the last index.
	 */
	public int getLast()
	{
		if (array.length == 0)
		{
			return -1;
		}
		else
		{
			return array[array.length - 1];
		}
	}
	
	/**
	 * Returns the sub path from the position of the given level.
	 * 
	 * @param pLevel the start level.
	 * @return the sub path
	 */
	public TreePath getSubPath(int pLevel)
	{
		if (pLevel <= 0)
		{
			return this;
		}
		else if (array.length <= pLevel)
		{
			return EMPTY;
		}
		else
		{
			int[] result = new int[array.length - pLevel];
	
			System.arraycopy(array, pLevel, result, 0, array.length - pLevel);
			
			return new TreePath(result);
		}		
	}

	/**
	 * Sets the value of a given index.
	 * 
	 * @param pIndex the index.
	 * @param pValue the value.
	 * @return the new TreePath.
	 */
	public TreePath set(int pIndex, int pValue)
	{
		int[] result = array.clone();
		result[pIndex] = pValue;
		
		return new TreePath(result);	
	}
	
	/**
	 * Adds the given array to this <code>TreePath</code>.
	 * 
	 * @param pArray the array.
	 * @return the new TreePath.
	 */
	public TreePath getChildPath(int... pArray)
	{
		if (pArray == null || pArray.length == 0)
		{
			return this;
		}
		else
		{
			int[] result = new int[array.length + pArray.length];
	
			System.arraycopy(array, 0, result, 0, array.length);
			System.arraycopy(pArray, 0, result, array.length, pArray.length);
			
			return new TreePath(result);
		}
	}

	/**
	 * Gets the parent tree path.
	 * 
	 * @return the new TreePath.
	 */
	public TreePath getParentPath()
	{
		if (array.length == 0)
		{
			return this;
		}
		else if (array.length == 1)
		{
			return EMPTY;
		}
		else
		{
			int[] result = new int[array.length - 1];
				
			System.arraycopy(array, 0, result, 0, result.length);

			return new TreePath(result);
		}
	}

	/**
	 * Returns true, if the given tree path is a parent path of this tree path.
	 * This means, that the tree path is either equal, or the given tree is a parent tree path of this tree path.
	 *
	 * @param pTreePath the parent tree path.
	 * @return true, if the given tree path is a parent path of this tree path.
	 */
	public boolean containsAsParent(TreePath pTreePath)
	{
		if (pTreePath == null)
		{
			return true;
		}
		else
		{
			if (array.length >= pTreePath.array.length) 
			{
				for (int i = 0; i < pTreePath.array.length; i++)
				{
					if (array[i] != pTreePath.array[i])
					{
	    	    		return false;
					}
				}
				return true;
			}
		    return false;
		}
	}
	
	/**
	 * Gets a mutable array of this immutable array. 
	 * 
	 * @return a mutable array,
	 */
	public int[] toArray()
	{
		return array.clone();
	}

}	// TreePath
