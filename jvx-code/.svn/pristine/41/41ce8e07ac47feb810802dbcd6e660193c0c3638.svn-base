/*
 * Copyright 2014 SIB Visions GmbH
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
 * 18.12.2014 - [JR] - creation
 */
package javax.rad.remote;

/**
 * The <code>SessionInvalidatedException</code> is a {@link SessionExpiredException} for 
 * the notification that a session is expired because of inactivity.
 * 
 * @author René Jahn
 */
public class SessionInvalidatedException extends SessionExpiredException
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Constructs a new <code>SessionInvalidatedException</code> with <code>null</code> as its
     * detail message. The cause is not initialized, and may subsequently be initialized by 
     * a call to {@link #initCause}.
     */
	public SessionInvalidatedException()
	{
		super();
	}

	/** 
	 * Constructs a new <code>SessionInvalidatedException</code> with the specified cause and a
     * detail message of <code>(cause==null ? null : cause.toString())</code>
     * (which typically contains the class and detail message of
     * <code>cause</code>).
     *
     * @param pCause the cause (which is saved for later retrieval by the
     *               {@link #getCause()} method).  (A <code>null</code> value is
     *               permitted, and indicates that the cause is nonexistent or
     *               unknown.)
     */
	public SessionInvalidatedException(Throwable pCause)
	{
		super(pCause);
	}
	
    /** 
     * Constructs a new <code>SessionInvalidatedException</code> with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to 
     * {@link #initCause}.
     *
     * @param pMessage the detail message. The detail message is saved for 
     *                 later retrieval by the {@link #getMessage()} method.
     */
	public SessionInvalidatedException(String pMessage)
	{
		super(pMessage);
	}
	
	
    /**
     * Constructs a new <code>SessionInvalidatedException</code> with the specified detail message and
     * cause. <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param pMessage the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method).
     * @param pCause the cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A <code>null</code> value is
     *        permitted, and indicates that the cause is nonexistent or
     *        unknown.)
     */
	public SessionInvalidatedException(String pMessage, Throwable pCause)
	{
		super(pMessage, pCause);
	}	
	
}	// SessionInvalidatedException
