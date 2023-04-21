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
 * 21.12.2009 - [HM] - creation
 */
package javax.rad.type;

/**
 * The <code>AbstractNumberType</code> is the base implementation for all numeric types.
 * 
 * @param <T> the {@link Number} type.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractNumberType<T extends Number & Comparable<T>> extends AbstractComparableType<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The minimal allowed value. */
	protected T minValue;
	
	/** The maximal allowed value. */
	protected T maxValue;
	
	/** The precision. */
	protected int precision;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public T validatedValueOf(Object pObject)
	{
		T value = valueOf(pObject);
		
		if (value != null)
		{
			if (minValue != null)
			{
				if (value.compareTo(minValue) < 0)
				{
					throw new IllegalArgumentException("The value " + toString(value) + " may not be smaller than " + toString(minValue) + "!");
				}
			}
			if (maxValue != null)
			{
				if (value.compareTo(maxValue) > 0)
				{
					throw new IllegalArgumentException("The value " + toString(value) + " may not be greater than " + toString(maxValue) + "!");
				}
			}
			
		}
		return value;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the minimal allowed value.
	 * 
	 * @return the minimal allowed value.
	 */
	public T getMinValue()
	{
		return minValue;
	}
	
	/**
	 * Sets the minimal allowed value.
	 * 
	 * @param pMinValue the minimal allowed value.
	 */
	public void setMinValue(T pMinValue)
	{
		minValue = pMinValue;
	}
	
	/**
	 * Gets the maximal allowed value.
	 * 
	 * @return the maximal allowed value.
	 */
	public T getMaxValue()
	{
		return maxValue;
	}
	
	/**
	 * Sets the maximal allowed value.
	 * 
	 * @param pMaxValue the maximal allowed value.
	 */
	public void setMaxValue(T pMaxValue)
	{
		maxValue = pMaxValue;
	}
	
	/**
	 * Gets the precision.
	 * 
	 * @return the precision.
	 */
	public int getPrecision()
	{
		return precision;
	}
	
	/**
	 * Sets the precision.
	 * 
	 * @param pPrecision the precision.
	 */
	public void setPrecision(int pPrecision)
	{
		precision = pPrecision;
	}
	
}	// AbstractNumberType
