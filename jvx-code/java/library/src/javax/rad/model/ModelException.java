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
 * 09.04.2009 - [RH] - interface review - extends now Exception instead of BaseException
 */
package javax.rad.model;

/**
 * The {@link ModelException} is thrown to indicate an error which happened in
 * the model or data storage.
 * 
 * @author Roland Hörmann
 */
public class ModelException extends Exception
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a new exception with the specified detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * {@link #initCause}.
	 *
	 * @param pErrorMessage the detail message. The detail message is saved for
	 *            later retrieval by the {@link #getMessage()} method.
	 */
	public ModelException(String pErrorMessage)
	{
		super(pErrorMessage);
	}
	
	/**
	 * Constructs a new exception with the specified cause and a detail message
	 * of <code>(cause==null ? null : cause.toString())</code> (which typically
	 * contains the class and detail message of <code>cause</code>).
	 *
	 * @param pMainCause the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <code>null</code> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public ModelException(Throwable pMainCause)
	{
		super(pMainCause);
	}
	
	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <p>
	 * Note that the detail message associated with {@code pMainCause} is
	 * <i>not</i> automatically incorporated in this exception's detail message.
	 *
	 * @param pErrorMessage the detail message (which is saved for later
	 *            retrieval by the {@link #getMessage()} method).
	 * @param pMainCause the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <code>null</code> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public ModelException(String pErrorMessage, Throwable pMainCause)
	{
		super(pErrorMessage, pMainCause);
	}
	
} 	// ModelException
