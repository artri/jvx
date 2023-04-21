/*
 * Copyright 2022 SIB Visions GmbH
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
 * 25.03.2022 - [JR] - creation
 */
package com.sibvisions.rad.remote.mfa;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.sibvisions.rad.remote.PropertyException;

/**
 * The <code>MFAException</code> is a base Multi-Factor exception.
 * 
 * @author René Jahn
 */
public class MFAException extends PropertyException
                          implements IMFAConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>MFAException</code>.
	 */
	public MFAException()
	{
	}
	
	/**
	 * Creates a new instance of <code>MFAException</code> with given message.
	 * 
	 * @param pMessage the message
	 */
	public MFAException(String pMessage)
	{
		super(pMessage);
	}
	
	/**
	 * Creates a new instance of <code>MFAException</code> with given cause.
	 * 
	 * @param pCause the wrapped cause
	 */
	public MFAException(Throwable pCause)
	{
		super(pCause);
	}

	/**
	 * Creates a new instance of <code>MFAException</code> with given cause and message.
	 * 
	 * @param pMessage the message
	 * @param pCause the wrapped cause
	 */
	public MFAException(String pMessage, Throwable pCause)
	{
		super(pMessage, pCause);
	}	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Entry<String, Object>> properties()
	{
		HashMap<String, Object> hmpCopy = new HashMap<String, Object>(hmpProperties);
		
		//remove "fields" because properties are additional to "fields"
		hmpCopy.remove("token");
		hmpCopy.remove("timeout");
		hmpCopy.remove("state");
		hmpCopy.remove("type");
		
		return hmpCopy.entrySet();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void put(String pName, Object pValue)
	{
		//not allowed to change internal properties 
		if (pName != null 
			&& !"token".equals(pName)
			&& !"timeout".equals(pName)
			&& !"state".equals(pName)
			&& !"type".equals(pName))
		{
			super.put(pName, pValue);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the token.
	 * 
	 * @param pToken the token
	 */
	public void setToken(Object pToken)
	{
		super.put("token", pToken);
	}
	
	/**
	 * Gets the token.
	 * 
	 * @return the token
	 */
	public Object getToken()
	{
		return get("token");
	}
	
	/**
	 * Sets the timeout.
	 * 
	 * @param pTimeout the timeout in millis or <code>-1</code> to disable/unset timeout
	 */
	public void setTimeout(long pTimeout)
	{
		super.put("timeout", Long.valueOf(pTimeout));
	}
	
	/**
	 * Gets the timeout.
	 * 
	 * @return the timeout in millis or <code>-1</code> if no timeout is set
	 */
	public long getTimeout()
	{
		Object oType = get("timeout");
		
		if (oType == null)
		{
			return -1;
		}
		else if (oType instanceof Number)
		{
			return ((Number)oType).longValue();
		}
		
		return -1;
	}
	
	/**
	 * Sets the type.
	 * 
	 * @param pType the type
	 * @see #TYPE_NONE
	 * @see #TYPE_WAIT
	 * @see #TYPE_TEXTINPUT
	 * @see #TYPE_URL
	 */
	public void setType(int pType)
	{
		super.put("type", Integer.valueOf(pType));
	}
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 * @see #setType(int)
	 */
	public int getType()
	{
		Object oType = get("type");
		
		if (oType == null)
		{
			return TYPE_NONE;
		}
		else if (oType instanceof Number)
		{
			return ((Number)oType).intValue();
		}
		
		return TYPE_NONE;
	}
	
	/**
	 * Sets the state.
	 * 
	 * @param pState the state
	 * @see #STATE_NONE
	 * @see #STATE_RETRY
	 */
	public void setState(int pState)
	{
		super.put("state", Integer.valueOf(pState));
	}
	
	/**
	 * Gets the state.
	 * 
	 * @return the state
	 * @see #setState(int)
	 */
	public int getState()
	{
		Object oType = get("state");
		
		if (oType == null)
		{
			return STATE_NONE;
		}
		else if (oType instanceof Number)
		{
			return ((Number)oType).intValue();
		}
		
		return STATE_NONE;
	}
	
}	// MFAException
