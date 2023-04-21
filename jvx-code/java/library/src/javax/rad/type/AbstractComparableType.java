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
 * The <code>AbstractComparableType</code> is the base implementation for all comparable types.
 * 
 * @param <T> type instanceof {@link Comparable}.
 * 
 * @author Martin Handsteiner
 */
public abstract class AbstractComparableType<T extends Comparable<T>> extends AbstractType<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(T pObject1, Object pObject2)
	{
		if (pObject1 == pObject2)
		{
			return 0;
		}
		else if (pObject1 != null)
		{
			if (pObject2 == null)
			{
				return 1;
			}
			else
			{
				return pObject1.compareTo(valueOf(pObject2));
			}
		}
		else
		{
			return -1;
		}
	}

}	// AbstractComparableType
