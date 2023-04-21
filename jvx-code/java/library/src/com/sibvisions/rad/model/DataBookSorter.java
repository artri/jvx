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
 * 10.10.2013 - [PK] - creation
 */
package com.sibvisions.rad.model;

import javax.rad.model.IDataRow;
import javax.rad.model.SortDefinition;

/**
 * The <code>DataBookSorter</code> is a helper class for sorting {@link javax.rad.model.IDataRow}s.
 * 
 * @author Peter Kofler
 */
public class DataBookSorter
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the used {@link SortDefinition}. */
	private final SortDefinition	sort;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DataBookSorter</code> with a specific
	 * {@link SortDefinition}.
	 * 
	 * @param pSort the {@link SortDefinition}
	 */
	public DataBookSorter(SortDefinition pSort)
	{
		sort = pSort;
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sorts an array of {@link IDataRow}s.
	 * 
	 * @param pDataRows the {@link IDataRow}s
	 * @return the sorted indexes
	 */
	public int[] sort(IDataRow[] pDataRows)
	{
		int[] iaIndexes = createArrayWithAscendingIndexes(pDataRows.length);
		
		quickSort(iaIndexes, pDataRows, 0, iaIndexes.length - 1);
		insertionSort(iaIndexes, pDataRows, 0, iaIndexes.length - 1);
		
		return iaIndexes;
	}

	/**
	 * Creates an array with ascending indexes.
	 * 
	 * @param pLength the length of the array.
	 * @return the array with ascending indexes.
	 */
	private int[] createArrayWithAscendingIndexes(int pLength)
	{
		int[] result = new int[pLength];
		
		for (int i = 0; i < result.length; i++)
		{
			result[i] = i;
		}
		return result;
	}
	
	/**
	 * Swap the source and target index in the result in[].
	 * 
	 * @param pResult the result int[] with the row indexes.
	 * @param pSourceIndex the source index.
	 * @param pTargetIndex the target index.
	 */
	private void swap(int[] pResult, int pSourceIndex, int pTargetIndex)
	{
		int iTemp = pResult[pSourceIndex];
		
		pResult[pSourceIndex] = pResult[pTargetIndex];
		pResult[pTargetIndex] = iTemp;
	}

	/**
	 * QuickSort implementation.
	 * 
	 * @param pResult the result int[] with the row indexes.
	 * @param pDataRows the {@link IDataRow}s  
	 * @param pFromIndex the from index to sort.
	 * @param pToIndex the to index to sort.
	 */
	private void quickSort(int[] pResult, IDataRow[] pDataRows, int pFromIndex, int pToIndex) 
	{
		if (pToIndex - pFromIndex > 4)
		{
			int i = (pToIndex + pFromIndex) / 2;
			
			if (pDataRows[pResult[pFromIndex]].compareTo(pDataRows[pResult[i]], sort) > 0)
			{
				swap(pResult, pFromIndex, i);
			}
			if (pDataRows[pResult[pFromIndex]].compareTo(pDataRows[pResult[pToIndex]], sort) > 0)
			{
				swap(pResult, pFromIndex, pToIndex);
			}
			if (pDataRows[pResult[i]].compareTo(pDataRows[pResult[pToIndex]], sort) > 0)
			{
				swap(pResult, i, pToIndex);
			}

			int j = pToIndex - 1;
			
			swap(pResult, i, j);
			i = pFromIndex;
			
			IDataRow v = pDataRows[pResult[j]];
			
			while (true)
			{
				while (pDataRows[pResult[++i]].compareTo(v, sort) < 0) 
				{ /* . */ }
				
				while (pDataRows[pResult[--j]].compareTo(v, sort) > 0)
				{ /* . */ }
				
				if (j < i)
				{
					break;
				}
				swap(pResult, i, j);
			}
			
			swap(pResult, i, pToIndex - 1);
			quickSort(pResult, pDataRows, pFromIndex, j);
			quickSort(pResult, pDataRows, i + 1, pToIndex);
		}
	}

	/**
	 * Insertion sort implementation.
	 * 
	 * @param pResult the result int[] with the row indexes.
	 * @param pDataRows the {@link IDataRow}s
	 * @param pFromIndex the from index to sort
	 * @param pToIndex the to index to sort
	 */
	private void insertionSort(int[] pResult, IDataRow[] pDataRows, int pFromIndex, int pToIndex) 
	{
		for (int i = pFromIndex + 1; i <= pToIndex; i++)
		{
			int vIndex = pResult[i];
			IDataRow vValue = pDataRows[vIndex];
			
			int j = i;
			while (j > pFromIndex && pDataRows[pResult[j - 1]].compareTo(vValue, sort) > 0)
			{
				pResult[j] = pResult[j - 1];
				j--;
			}
			
			pResult[j] = vIndex;
		}
	}	
	
}   //DataBookSorter
