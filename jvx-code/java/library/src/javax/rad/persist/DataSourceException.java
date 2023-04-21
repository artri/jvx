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
 * 01.10.2008 - [RH] - creation
 * 08.04.2009 - [RH] - interface review - extends now ModelException
 */
package javax.rad.persist;

import javax.rad.model.ModelException;

/**
 * The <code>DataSourceException</code> is used to throw storage depending 
 * <code>Exception</code>'s. 
 * 
 * @author Roland Hörmann
 */
public class DataSourceException extends ModelException
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs the <code>DataSourceException</code> with the specified error message.
	 * 
	 * @param pErrorMessage the error message of the <code>Exception</code>
	 */
	public DataSourceException(String pErrorMessage)
	{
		super(pErrorMessage);
	}

    /**
     * Constructs the <code>DataSourceException</code> with the specified error message.
     * 
     * @param pMainCause the <code>Exception</code>, which followed to this <code>Exception</code>.
     */
    public DataSourceException(Throwable pMainCause)
    {
        super(pMainCause);
    }

    /**
	 * Constructs the <code>DataSourceException</code> with the specified error message 
	 * and main cause.
	 * 
	 * @param pErrorMessage the error message of the <code>Exception</code>
	 * @param pMainCause the <code>Exception</code>, which followed to this <code>Exception</code>.
	 */
	public DataSourceException(String pErrorMessage, Throwable pMainCause)
	{
		super(pErrorMessage, pMainCause);
	}

} 	// DataSourceException
