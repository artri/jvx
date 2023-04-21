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
 * 01.10.2008 - [HM] - creation
 * 17.11.2008 - [RH] - clone() removed
 * 18.11.2008 - [RH] - class simplified
 */
package javax.rad.model.condition;

import javax.rad.model.IDataRow;

/**
 * The <code>And</code> condition implements the AND operator.
 * 
 * @author Martin Handsteiner
 */
public class And extends OperatorCondition
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>And</code>.
	 */
    public And() 
    {
    }
	
	/**
	 * Creates a new instance of <code>And</code>. The condition concatinates
	 * two <code>ICondition</code> objects with an AND operation.
	 * 
	 * @param pConditions operands to set
	 */
    public And(ICondition... pConditions) 
    {
    	setConditions(pConditions);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public boolean isFulfilled(IDataRow pDataRow)
	{
    	ICondition[] caConditions = getConditions();
		for (int i = 0; i < caConditions.length; i++)
		{
			if (!caConditions[i].isFulfilled(pDataRow))
			{
				return false;
			}
		}
		return true;
	}

}	// And
