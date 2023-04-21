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
 * 23.07.2009 - [HM] - creation
 */
package javax.rad.util;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>SilentAbortException</code> is a RuntimeException, that is not reported and shown by the 
 * Exception Handler.
 * 
 * @author Martin Handsteiner
 */
public class SilentAbortException extends RuntimeException
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** 
	 * Constructs a new silent abort exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public SilentAbortException() 
    {
    	super();
    }

    /** 
     * Constructs a new silent abort exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     */
    public SilentAbortException(String message) 
    {
    	super(message);
    }

    /**
     * Constructs a new silent abort exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <code>null</code> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public SilentAbortException(String message, Throwable cause) 
    {
        super(message, cause);
    }

    /** 
     * Constructs a new silent abort exception with the specified cause and a
     * detail message of <code>(cause==null ? null : cause.toString())</code>
     * (which typically contains the class and detail message of
     * <code>cause</code>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <code>null</code> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public SilentAbortException(Throwable cause) 
    {
        super(cause);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void printStackTrace(PrintStream s) 
    {
    	Throwable thCause = getCause();
    	
    	if (thCause != null)
    	{
    		LoggerFactory.getInstance(getClass()).error(thCause);
    	}
		else
		{
			thCause = this;
		}
		s.print(": ");
		if (thCause.getMessage() == null)
		{
			s.println(thCause.getClass().getSimpleName());
		}
		else
		{
			s.println(thCause.getMessage());
		}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void printStackTrace(PrintWriter s) 
    {
    	Throwable thCause = getCause();
    	
    	if (thCause != null)
    	{
    		LoggerFactory.getInstance(getClass()).error(thCause);
    	}
		else
		{
			thCause = this;
		}
		s.print(": ");
		if (thCause.getMessage() == null)
		{
			s.println(thCause.getClass().getSimpleName());
		}
		else
		{
			s.println(thCause.getMessage());
		}
    }
	
}	// SilentAbortException
