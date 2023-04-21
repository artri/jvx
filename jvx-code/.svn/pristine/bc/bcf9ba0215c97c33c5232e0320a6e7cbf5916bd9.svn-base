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
 * 02.05.2011 - [JR] - creation
 */
package com.sibvisions.util.type;

import java.lang.ref.WeakReference;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.ICloseable;

/**
 * Tests {@link CommonUtil} methods.
 * 
 * @author René Jahn
 * @see CommonUtil
 */
public class TestCommonUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests {@link CommonUtil#equals(Object)}.
	 */
	@Test
	public void testEquals()
	{
		Assert.assertTrue(CommonUtil.equals(null, null));
		Assert.assertTrue(CommonUtil.equals("A", "A"));

		Assert.assertFalse(CommonUtil.equals(null, "A"));
		Assert.assertFalse(CommonUtil.equals("A", null));

		Assert.assertFalse(CommonUtil.equals("A", "B"));
		
		String string = new String("A");
		WeakReference<String> weakString = new WeakReference<String>(string);
		
		Assert.assertTrue(CommonUtil.equals(weakString, weakString));
		Assert.assertTrue(CommonUtil.equals(string, weakString));
		Assert.assertTrue(CommonUtil.equals(weakString, string));
	}

	/**
	 * Tests {@link CommonUtil#getFreePort(int, int, boolean)}.
	 */
	@Test
	public void testFreePort()
	{
		Assert.assertTrue(CommonUtil.getFreePort(8080, 8090, true) >= 8080);
	}
	
	/**
	 * Tests {@link CommonUtil#close(Object...)}.
	 */
	@Test
	public void testClose()
	{
	    MyHandle.cnt = 0;
	    
	    ICloseable[] cls = new ICloseable[] {new MyHandle(), new MyHandle(), new MyHandle()};
	    
	    CommonUtil.close((Object[])cls);
	    
	    Assert.assertEquals(3, MyHandle.cnt);
	    
	    MyHandle.cnt = 0;
	    
        CommonUtil.close((Object)cls);
        
        Assert.assertEquals(0, MyHandle.cnt);
	}

    //****************************************************************
    // Subclass definition
    //****************************************************************

	/**
	 * The <code>MyHandle</code> is a simple {@link ICloseable} implementation.
	 * 
	 * @author René Jahn
	 */
    public static class MyHandle implements ICloseable
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the close counter. */
        static int cnt = 0;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void close()
        {
            cnt++;
        }
        
    }   // MyHandle
	
}	// TestCommonUtil
