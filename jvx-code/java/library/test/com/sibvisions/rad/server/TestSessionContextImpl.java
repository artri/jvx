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
 * 29.07.2009 - [JR] - creation
 */
package com.sibvisions.rad.server;

import javax.rad.server.SessionContext;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the functionality of {@link SessionContext} and {@link SessionContextImpl}.
 * 
 * @author René Jahn
 */
public class TestSessionContextImpl
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link SessionContext#getPreviousContext()}.
	 */
	@Test
	public void testPreviousContext()
	{
		SessionContext contextFirst = new SessionContextImpl(new WrappedSession(null));
		SessionContext contextSecond = new SessionContextImpl(new WrappedSession(null));
		SessionContext contextThird = new SessionContextImpl(new WrappedSession(null));
		
		try
		{
		    try
		    {
        		Assert.assertEquals(contextSecond, contextThird.getPreviousContext());
        		Assert.assertEquals(contextFirst, contextThird.getPreviousContext().getPreviousContext());
        		Assert.assertNull("First SessionContext not found!", contextThird.getPreviousContext().getPreviousContext().getPreviousContext());
        		Assert.assertNull("First SessionContext is not the first context!", contextFirst.getPreviousContext());
		    }
		    finally
		    {
		        contextFirst.release();
		    }
		    
    		contextThird.release();
    		
    		Assert.assertNull("Second SessionContext is not the last context!", contextSecond.getNextContext());
		}
		finally
		{
		    contextSecond.release();
		}
	}
	
	/**
	 * Tests {@link SessionContext#getNextContext()}.
	 */
	@Test
	public void testNextContext()
	{
		SessionContext contextFirst = new SessionContextImpl(new WrappedSession(null));
        SessionContext contextSecond = new SessionContextImpl(new WrappedSession(null));
        SessionContext contextThird = new SessionContextImpl(new WrappedSession(null));

        try
        {
    		try
    		{
        		Assert.assertNull("Third SessionContext is not the last context!", contextThird.getNextContext());
        		Assert.assertEquals(contextSecond, contextFirst.getNextContext());
        		Assert.assertEquals(contextThird, contextFirst.getNextContext().getNextContext());
    		}
    		finally
    		{
    		    contextFirst.release();
    		}
    		contextThird.release();
		
    		Assert.assertNull("The second SessionContext is not the last context!", contextSecond.getNextContext());
        }
        finally
        {
            contextSecond.release();
        }
	}
	
}	// TestSessionContextImpl
