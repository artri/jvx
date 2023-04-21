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
 * 01.10.2008 - [MH] - creation
 * 17.11.2008 - [RH] - optimized
 * 18.11.2008 - [RH] - class simplified
 * 19.11.2008 - [RH] - filter redesign
 * 28.04.2009 - [RH] - setConditions added, ->POJO
 */
package javax.rad.model.condition;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>OperatorCondition</code> is the default implementation for
 * logical operators.<br>
 * That means it put its connected (glued) conditions in brackets
 * 
 * @author Martin Handsteiner
 */
public abstract class OperatorCondition extends BaseCondition
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** list of all operands. */
	private ArrayUtil<ICondition> auConditions = new ArrayUtil<ICondition>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICondition clone()
	{
		OperatorCondition ocResult = (OperatorCondition)super.clone();
		ocResult.auConditions = new ArrayUtil<ICondition>(auConditions.size());
		
		for (ICondition cond : auConditions)
		{
			ocResult.add(cond.clone());
		}
		
		if (ocResult.auConditions.size() == 0)
		{
		    return null;
		}
		else
		{
		    return ocResult;
		}
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds an operand to the operator condition.
	 * 
	 * @param pCond the operand
	 */
	public void add(ICondition pCond)
    {
	    if (pCond != null)
	    {
	        auConditions.add(pCond);
	    }
    }
	
	/**
	 * Removes an operand from the operator condition.
	 * 
	 * @param pCond the operand
	 */
	public void remove(ICondition pCond)
    {
    	auConditions.remove(pCond);
    }
	
	/**
	 * Returns all <code>IConditions</code> of this OperatorCondition.
	 * 
	 * @return all <code>IConditions</code> of this OperatorCondition.
	 */
	public ICondition[] getConditions()
	{
		return auConditions.toArray(new ICondition[auConditions.size()]);
	}
	
	/**
	 * Sets all <code>IConditions</code> of this OperatorCondition.
	 * 
	 * @param pConditions	the new conditions to set.
	 */
	public void setConditions(ICondition[] pConditions)
	{	
		auConditions.clear();
		if (pConditions != null)
		{
    		for (ICondition cond : pConditions)
    		{
    		    add(cond);
    		}
		}
	}
	
}	// OperatorCondition
