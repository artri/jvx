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
 * The <code>Not</code> condition implements the NOT operator.
 * 
 * @author Martin Handsteiner
 */
public class Not extends BaseCondition
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** <code>ICondition</code> on which the not operator should be applied. */
	private ICondition condition;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Not</code>.
	 */
	public Not() 
    {
    }
    
	/**
	 * Creates a new instance of <code>Not</code>. The condition negates another
	 * <code>ICondition</code>.
	 * 
	 * @param pCondition condition to negate
	 */
	public Not(ICondition pCondition) 
    {
    	condition = pCondition;
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public boolean isFulfilled(IDataRow pDataRow)
	{
		return condition == null || !condition.isFulfilled(pDataRow);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICondition clone()
	{
	    if (condition == null)
	    {
	        return null;
	    }
	    else
	    {
    		Not iResult = (Not)super.clone();
    		
    		iResult.condition = condition.clone();
    
    		return iResult;
	    }
	}	

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the ICondition to negate.
	 * 
	 * @return the ICondition to negate.
	 */
	public ICondition getCondition()
    {
		return condition;
    }
	
	/**
	 * Sets the ICondition to negate.
	 * 
	 * @param pCondition the ICondition to negate.
	 */
	public void setCondition(ICondition pCondition)
    {
		condition = pCondition;
    }
	
}	// Not
