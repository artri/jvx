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
 * 17.11.2008 - [RH] - clone() added, or/and bug fixed
 * 18.11.2008 - [RH] - class simplified
 * 23.02.2013 - [JR] - toString implemented
 */
package javax.rad.model.condition;

import java.io.Serializable;
import java.util.Date;

/**
 * The <code>BaseCondition</code> is the default implementation of <code>ICondition</code>. It
 * offers the possibility to use the <code>Or</code> and <code>And</code> operator with other
 * conditions.
 * 
 * @author Roland H�rmann, Martin Handsteiner
 */
public abstract class BaseCondition implements ICondition, 
                                               Serializable, 
                                               Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
        StringBuilder result = new StringBuilder();

        toString(result, this);
        
        return result.toString();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public And and(ICondition pCondition) 
	{
		if (this instanceof And)
		{
			((And)this).add(pCondition);
			
			return (And)this;
		}
		else
		{
			And andCondition = new And(this, pCondition);
			
			return andCondition;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Or or(ICondition pCondition) 
	{
		if (this instanceof Or)
		{
			((Or)this).add(pCondition);
			
			return (Or)this;
		}
		else
		{
			Or orCondition = new Or(this, pCondition);
			
			return orCondition;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ICondition clone()
	{
		try 
		{
			return (ICondition)super.clone();
		}
		catch (CloneNotSupportedException cloneNotSupportedException)
		{
			// should not occur!
			return null;
		}
	}
		
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Faster toString when using only one <code>StringBuilder</code>.
     * 
     * @param pStringBuilder the <code>StringBuilder</code> to fill in.
     * @param pValue the value to append.
     */
    protected void valueToString(StringBuilder pStringBuilder, Object pValue)
    {
        if (pValue instanceof String)
        {
            pStringBuilder.append('\'');
            pStringBuilder.append(pValue);
            pStringBuilder.append('\'');
        }
        else if (pValue instanceof Date)
        {
            pStringBuilder.append('\'');
            pStringBuilder.append(pValue);
            pStringBuilder.append('\'');
        }
        else
        {
            pStringBuilder.append(pValue);
        }
    }
	
	
	/**
	 * Faster toString when using only one <code>StringBuilder</code>.
	 * 
	 * @param pStringBuilder the <code>StringBuilder</code> to fill in.
     * @param pCondition the <code>ICondition</code> to build.
	 */
    protected void toString(StringBuilder pStringBuilder, ICondition pCondition)
	{
        if (pCondition instanceof CompareCondition)
        {
            CompareCondition cCompare = (CompareCondition)pCondition;
            Object           oValue   = cCompare.getValue();
            
            if (oValue != null || !cCompare.isIgnoreNull())
            {
                String sColumnName = cCompare.getColumnName();
                
                boolean isLikeReverse = cCompare instanceof LikeReverse;
                boolean isLikeReverseIgnoreCase = cCompare instanceof LikeReverseIgnoreCase;
                boolean isLikeIgnoreCase = cCompare instanceof LikeIgnoreCase;
                
                if (oValue != null && (isLikeReverse || isLikeReverseIgnoreCase))
                {
                    if (isLikeReverse)
                    {
                        valueToString(pStringBuilder, oValue);
                    }
                    else // if (isLikeReverseIgnoreCase)
                    {
                        pStringBuilder.append("UPPER(");
                        valueToString(pStringBuilder, oValue);
                        pStringBuilder.append(')');
                    }                       
                }
                else if (isLikeIgnoreCase)
                {
                    pStringBuilder.append("UPPER(");
                    pStringBuilder.append(sColumnName);
                    pStringBuilder.append(')');
                }                       
                else 
                {
                    pStringBuilder.append(sColumnName);
                }                       
                
                pStringBuilder.append(' ');
                
                if (oValue == null)
                {
                    pStringBuilder.append("IS NULL");
                }
                else
                {
                    if (cCompare instanceof Equals)
                    {
                        pStringBuilder.append("= ");
                    }
                    else if (isLikeIgnoreCase || isLikeReverseIgnoreCase)
                    {
                        pStringBuilder.append("LIKE UPPER(");
                    }                       
                    else if (cCompare instanceof Like || isLikeReverse)
                    {
                        pStringBuilder.append("LIKE ");
                    }                       
                    else if (cCompare instanceof Greater)
                    {
                        pStringBuilder.append("> ");
                    }                       
                    else if (cCompare instanceof GreaterEquals)
                    {
                        pStringBuilder.append(">= ");
                    }                       
                    else if (cCompare instanceof Less)
                    {
                        pStringBuilder.append("< ");
                    }                       
                    else if (cCompare instanceof LessEquals)
                    {
                        pStringBuilder.append("<= ");
                    }       

                    if (isLikeReverse || isLikeReverseIgnoreCase)
                    {
                        pStringBuilder.append(sColumnName);                     
                    }
                    else
                    {
                        valueToString(pStringBuilder, oValue);
                    }
                    
                    if (isLikeIgnoreCase || isLikeReverseIgnoreCase)
                    {
                        pStringBuilder.append(')');
                    }                       
                }
            }
        }
        else if (pCondition instanceof OperatorCondition)
        {           
            OperatorCondition cOperator = (OperatorCondition)pCondition;
            
            int oldLength = pStringBuilder.length();
            ICondition[] caConditions = cOperator.getConditions();
            for (int i = 0; i < caConditions.length; i++)
            {
                ICondition cond = caConditions[i]; 
                if (cond != null)
                {
                    if (i > 0 && pStringBuilder.length() > oldLength)
                    {
                        if (cOperator instanceof And)
                        {
                            pStringBuilder.append(" AND ");
                        }
                        else if (cOperator instanceof Or)
                        {
                            pStringBuilder.append(" OR ");
                        }                       
                    }
                    if (cond instanceof OperatorCondition)
                    {
                        int opCount = ((OperatorCondition)cond).getConditions().length;
                        if (opCount == 1)
                        {
                            toString(pStringBuilder, cond);
                        }
                        else if (opCount > 1)
                        {
                            int curOldLength = pStringBuilder.length();
                            
                            pStringBuilder.append('(');
        
                            toString(pStringBuilder, cond);
                            
                            if (pStringBuilder.length() == curOldLength + 1)
                            {
                                pStringBuilder.setLength(curOldLength);
                            }
                            else
                            {
                                pStringBuilder.append(')');
                            }
                        }
                    }
                    else
                    {
                        toString(pStringBuilder, cond);
                    }
                }
            }
        }
        else if (pCondition instanceof Not)
        {
            ICondition cond = ((Not)pCondition).getCondition();
            if (cond != null)
            {
                int oldLength = pStringBuilder.length();
                
                pStringBuilder.append("NOT ");
                
                if (cond instanceof OperatorCondition)
                {
                    pStringBuilder.append('(');
                    
                    toString(pStringBuilder, cond);
                    
                    if (pStringBuilder.length() == oldLength + 5)
                    {
                        pStringBuilder.setLength(oldLength);
                    }
                    else
                    {
                        pStringBuilder.append(')');
                    }
                }
                else
                {
                    toString(pStringBuilder, cond);
                    
                    if (pStringBuilder.length() == oldLength + 4)
                    {
                        pStringBuilder.setLength(oldLength);
                    }
                }
            }
        }
    }
	
}	// BaseCondition
