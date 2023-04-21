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
 * 26.06.2015 - [RZ] - Creation
 */
package com.sibvisions.util.type;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link ExceptionUtil}.
 * 
 * @author Robert Zenz
 */
public class TestExceptionUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the {@link ExceptionUtil#contains(Throwable, Class)} method.
	 */
	@Test
	public void testContains()
	{
		Assert.assertFalse(ExceptionUtil.contains(null, null));
		Assert.assertFalse(ExceptionUtil.contains(new Exception(), null));
		Assert.assertFalse(ExceptionUtil.contains(null, Exception.class));
		
		InvalidKeyException firstException = new InvalidKeyException();
		IOException secondException = new IOException(firstException);
		Exception thirdException = new Exception(secondException);
		
		Assert.assertTrue(ExceptionUtil.contains(thirdException, InvalidKeyException.class));
		Assert.assertTrue(ExceptionUtil.contains(thirdException, IOException.class));
		Assert.assertTrue(ExceptionUtil.contains(thirdException, Exception.class));
		Assert.assertTrue(ExceptionUtil.contains(thirdException, Throwable.class));
		
		Assert.assertFalse(ExceptionUtil.contains(thirdException, OutOfMemoryError.class));
	}
	
	/**
	 * Tests the {@link ExceptionUtil#dump(Throwable, boolean)} method.
	 */
	@Test
	public void testDump()
	{
		String n = System.getProperty("line.separator");
		
		Assert.assertEquals("", ExceptionUtil.dump(null, true));
		
		Exception testException = new Exception("Test Exception");
		testException.setStackTrace(new StackTraceElement[] {});
		
		Assert.assertEquals("java.lang.Exception: Test Exception" + n, ExceptionUtil.dump(testException, true));
		
		testException.setStackTrace(new StackTraceElement[] {
				new StackTraceElement("test", "testMethod", "testFile", 42)
		});
		
		Assert.assertEquals("java.lang.Exception: Test Exception" + n
				+ "	at test.testMethod(testFile:42)" + n, ExceptionUtil.dump(testException, true));
				
		Exception wrappingException = new Exception("Test Wrapping Exception", testException);
		wrappingException.setStackTrace(new StackTraceElement[] {
				new StackTraceElement("test", "wrappedMethod", "testFile", 85)
		});
		
		Exception wrappingWrappingException = new Exception("Test Wrapping Wrapping Exception", wrappingException);
		wrappingWrappingException.setStackTrace(new StackTraceElement[] {
				new StackTraceElement("test", "yetAnotherWrappedMethod", "testFile", 125)
		});
		
		Assert.assertEquals("java.lang.Exception: Test Wrapping Wrapping Exception" + n
				+ "	at test.yetAnotherWrappedMethod(testFile:125)" + n
				+ "Caused by: java.lang.Exception: Test Wrapping Exception" + n
				+ "	at test.wrappedMethod(testFile:85)" + n
				+ "Caused by: java.lang.Exception: Test Exception" + n
				+ "	at test.testMethod(testFile:42)" + n, ExceptionUtil.dump(wrappingWrappingException, true));
				
		Assert.assertEquals("java.lang.Exception: Test Wrapping Wrapping Exception" + n
				+ "	at test.yetAnotherWrappedMethod(testFile:125)" + n, ExceptionUtil.dump(wrappingWrappingException, false));
	}
	
	/**
	 * Tests the {@link ExceptionUtil#getMessages(Throwable)} method.
	 */
	@Test
	public void testGetMessagesList()
	{
		List<String> messages = ExceptionUtil.getMessages(null);
		
		Assert.assertNotNull(messages);
		Assert.assertEquals(0, messages.size());
		
		Exception firstException = new Exception("First");
		Exception secondException = new Exception("Second", firstException);
		Exception thirdException = new Exception("Third", secondException);
		
		messages = ExceptionUtil.getMessages(thirdException);

		Assert.assertNotNull(messages);
		Assert.assertEquals(3, messages.size());
		Assert.assertEquals("Third", messages.get(0));
		Assert.assertEquals("Second", messages.get(1));
		Assert.assertEquals("First", messages.get(2));
	}
	
	/**
	 * Tests the {@link ExceptionUtil#getMessages(Throwable, String)} method.
	 */
	@Test
	public void testGetMessagesString()
	{
		Assert.assertEquals("", ExceptionUtil.getMessages(null, null));
		Assert.assertEquals("", ExceptionUtil.getMessages(null, "; "));
		
		Exception firstException = new Exception("First");
		Exception secondException = new Exception("Second", firstException);
		Exception thirdException = new Exception("Third", secondException);
		
		Assert.assertEquals("First", ExceptionUtil.getMessages(firstException, "; "));
		Assert.assertEquals("Second; First", ExceptionUtil.getMessages(secondException, "; "));
		Assert.assertEquals("Third; Second; First", ExceptionUtil.getMessages(thirdException, "; "));
	}
	
	/**
	 * Tests the {@link ExceptionUtil#getRootCause(Throwable)} method.
	 */
	@Test
	public void testGetRootCause()
	{
		Assert.assertNull(ExceptionUtil.getRootCause(null));
		
		Exception firstException = new Exception();
		
		Assert.assertSame(firstException, ExceptionUtil.getRootCause(firstException));
		
		Exception secondException = new Exception(firstException);
		Exception thirdException = new Exception(secondException);
		
		Assert.assertSame(firstException, ExceptionUtil.getRootCause(thirdException));
	}
	
	/**
	 * Tests the {@link ExceptionUtil#getThrowable(Throwable, Class)} method.
	 */
	@Test
	public void testGetThrowable()
	{
		Assert.assertNull(ExceptionUtil.getThrowable(null, null));
		Assert.assertNull(ExceptionUtil.getThrowable(new Exception(), null));
		Assert.assertNull(ExceptionUtil.getThrowable(null, Exception.class));
		
		InvalidKeyException firstException = new InvalidKeyException();
		IOException secondException = new IOException(firstException);
		Exception thirdException = new Exception(secondException);
		
		Assert.assertSame(firstException, ExceptionUtil.getThrowable(thirdException, InvalidKeyException.class));
		Assert.assertSame(secondException, ExceptionUtil.getThrowable(thirdException, IOException.class));
		Assert.assertSame(thirdException, ExceptionUtil.getThrowable(thirdException, Exception.class));
		Assert.assertSame(thirdException, ExceptionUtil.getThrowable(thirdException, Throwable.class));
		
		Assert.assertNull(ExceptionUtil.getThrowable(thirdException, OutOfMemoryError.class));
	}
	
	/**
	 * Tests the {@link ExceptionUtil#getThrowables(Throwable)} method.
	 */
	@Test
	public void testGetThrowables()
	{
		List<Throwable> causes = ExceptionUtil.getThrowables(null);
		
		Assert.assertNotNull(causes);
		Assert.assertEquals(0, causes.size());
		
		Exception firstException = new Exception();
		Exception secondException = new Exception(firstException);
		Exception thirdException = new Exception(secondException);
		
		causes = ExceptionUtil.getThrowables(firstException);
		Assert.assertEquals(1, causes.size());
		Assert.assertSame(firstException, causes.get(0));
		
		causes = ExceptionUtil.getThrowables(thirdException);
		Assert.assertEquals(3, causes.size());
		Assert.assertSame(thirdException, causes.get(0));
		Assert.assertSame(secondException, causes.get(1));
		Assert.assertSame(firstException, causes.get(2));
	}
	
}	// TestExceptionUtil
