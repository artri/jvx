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

/**
 * The <code>BaseCondition</code> is the default implementation of <code>ICondition</code>. It
 * offers the possibility to use the <code>Or</code> and <code>And</code> operator with other
 * conditions.
 * 
 * @author Roland Hörmann, Martin Handsteiner
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
        
        if (this instanceof CompareCondition)
        {
            CompareCondition cCompare = (CompareCondition)this;
            Object           oValue   = cCompare.getValue();
            
            if (!cCompare.isIgnoreNull() || oValue != null)
            {
                String sColumnName = cCompare.getColumnName();
                
                if (oValue instanceof String)
                {
                    oValue = "'" + oValue + "'";
                }
                
                if ((cCompare instanceof LikeReverse || cCompare instanceof LikeReverseIgnoreCase)
                    && oValue != null)
                {
                    if (cCompare instanceof LikeReverse)
                    {
                        result.append(oValue);
                    }
                    else if (cCompare instanceof LikeReverseIgnoreCase)
                    {
                        result.append("UPPER(");
                        result.append(oValue);
                        result.append(")");
                    }                       
                }
                else if (cCompare instanceof LikeIgnoreCase)
                {
                    result.append("UPPER(");
                    result.append(sColumnName);
                    result.append(")");
                }                       
                else 
                {
                    result.append(sColumnName);
                }                       
                
                result.append(' ');
                
                if (oValue == null)
                {
                    result.append("IS NULL");
                }
                else
                {
                    if (cCompare instanceof Equals)
                    {
                        result.append("= ");
                    }
                    else if (cCompare instanceof LikeIgnoreCase || cCompare instanceof LikeReverseIgnoreCase)
                    {
                        result.append("LIKE UPPER(");
                    }                       
                    else if (cCompare instanceof Like
                            || cCompare instanceof LikeReverse)
                    {
                        result.append("LIKE ");
                    }                       
                    else if (cCompare instanceof Greater)
                    {
                        result.append("> ");
                    }                       
                    else if (cCompare instanceof GreaterEquals)
                    {
                        result.append(">= ");
                    }                       
                    else if (cCompare instanceof Less)
                    {
                        result.append("< ");
                    }                       
                    else if (cCompare instanceof LessEquals)
                    {
                        result.append("<= ");
                    }       
                    else
                    {
                        result.append(' ');
                    }

                    if (cCompare instanceof LikeReverse || cCompare instanceof LikeReverseIgnoreCase)
                    {
                        result.append(sColumnName);                     
                    }
                    else
                    {
                        result.append(oValue);
                    }
                    
                    if (cCompare instanceof LikeIgnoreCase || cCompare instanceof LikeReverseIgnoreCase)
                    {
                        result.append(")");
                    }                       
                }
            }
        }
        else if (this instanceof OperatorCondition)
        {           
            OperatorCondition cOperator = (OperatorCondition)this;
                        
            ICondition[] caConditions = cOperator.getConditions();
            for (int i = 0; i < caConditions.length; i++)
            {
                String sTempSQL = caConditions[i].toString();
                            
                if (sTempSQL != null && sTempSQL.length() > 0)
                {
                    if (i > 0 && result.length() > 0)
                    {
                        if (cOperator instanceof And)
                        {
                            result.append(" AND ");
                        }
                        else if (cOperator instanceof Or)
                        {
                            result.append(" OR ");
                        }                       
                    }
                    if (caConditions[i] instanceof OperatorCondition)
                    {
                        result.append("(");
                        result.append(sTempSQL);
                        result.append(")");
                    }
                    else
                    {
                        result.append(sTempSQL);
                    }
                }
            }
        }
        else if (this instanceof Not)
        {
            ICondition cCond = ((Not)this).getCondition();
            if (cCond != null)
            {
                String sTempSQL  = cCond.toString();
                        
                result.append("NOT ");          
                if (cCond instanceof OperatorCondition)
                {
                    result.append("(");
                    result.append(sTempSQL);
                    result.append(")");
                }
                else
                {
                    result.append(sTempSQL);
                }
            }
        }
        
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
		
}	// BaseCondition
