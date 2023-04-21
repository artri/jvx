/*
 * Copyright 2017 SIB Visions GmbH
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
 * 27.07.2017 - [JR] - creation
 */
package javax.rad.util;

/**
 * The <code>Parameter</code> is a general key/value implementation.
 * A parameter is identified by name and its value.
 * 
 * @author René Jahn
 */
public class Parameter
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the name of the parameter. */
    private String sName;
    
    /** the value of the parameter. */
    private Object oValue;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>Parameter</code>.
     */
    public Parameter()
    {
    }
    
    /**
     * Creates a new instance of <code>Parameter</code> with predefined
     * name and value.
     * 
     * @param pName the name of the parameter
     * @param pValue the value of the parameter
     */
    public Parameter(String pName, Object pValue)
    {
        sName = pName;
        oValue = pValue;
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
        int result = 31 + (oValue == null ? 0 : oValue.hashCode());
        result = 31 * result + (sName == null ? 0 : sName.hashCode());
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        
        if (obj == null)
        {
            return false;
        }
        
        if (getClass() != obj.getClass())
        {
            return false;
        }
        
        Parameter other = (Parameter)obj;
        
        if (oValue == null)
        {
            if (other.oValue != null)
            {
                return false;
            }
        }
        else if (!oValue.equals(other.oValue))
        {
            return false;
        }
        
        if (sName == null)
        {
            if (other.sName != null)
            {
                return false;
            }
        }
        else if (!sName.equals(other.sName))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return sName + " = " + oValue; 
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName()
    {
        return sName;
    }

    /**
     * Sets the name.
     * 
     * @param pName the name
     */
    public void setName(String pName)
    {
        sName = pName;
    }
    
    /**
     * Gets the value.
     * 
     * @return the value
     */
    public Object getValue()
    {
        return oValue;
    }
    
    /**
     * Sets the value.
     * 
     * @param pValue the value
     */
    public void setValue(Object pValue)
    {
        oValue = pValue;
    }
    
}   // Parameter
