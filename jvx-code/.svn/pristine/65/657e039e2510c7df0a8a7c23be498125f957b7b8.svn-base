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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.remote.PropertyException;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ExceptionUtil;

/**
 * Tests functionality of {@link MFAException}.
 * 
 * @author René Jahn
 */
public class TestMFAException 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Dumps the given exception without specific access to the stack.
	 * This method is not the same as {@link ExceptionUtil#dump(Throwable, boolean)} because
	 * of {@link Exception#getStackTrace()} access.
	 * 
	 * @param pCause the cause
	 * @return the stack trace
	 */
	public String dump(Exception pCause)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try
		{
			PrintWriter pw = new PrintWriter(baos);
	
			try
			{
				pCause.printStackTrace(pw);
			}
			finally
			{
				pw.close();
			}
		}
		finally
		{
			CommonUtil.close(baos);
		}
		
		return baos.toString();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests property hiding of {@link MFAException}.
	 */
	@Test
	public void testProperties()
	{
		MFAException mfa = new MFAException();
		mfa.put("identifier", "12345-678-987");
		mfa.put("type", "url");
		
		String sStack = dump(mfa);
		
		Assert.assertFalse(sStack.contains("at " + PropertyException.class.getName() + ".properties("));
		
		StackTraceElement[] stack = mfa.getStackTrace();
		
		Exception e = new Exception();
		e.setStackTrace(stack);
		
		sStack = dump(e);
		
		Assert.assertTrue(sStack.contains("at " + PropertyException.class.getName() + ".properties("));
		
		MFAException mfaClone = new MFAException(mfa.getMessage());
		mfaClone.setStackTrace(mfa.getStackTrace());

		sStack = dump(mfaClone);		
		
		Assert.assertFalse(sStack.contains("at " + PropertyException.class.getName() + ".properties("));

		Assert.assertEquals(mfa.get("identifier"), mfaClone.get("identifier"));
		Assert.assertEquals(mfa.get("type"), mfaClone.get("type"));
	}
	
}	// TestMFAException
