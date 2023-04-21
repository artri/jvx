/*
 * Copyright 2015 SIB Visions GmbH
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@link ExceptionUtil} provides common functionality for working with and
 * processing exceptions.
 * 
 * @author Robert Zenz
 * @since 2.3
 */
public final class ExceptionUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invisible constructor because <code>ExceptionUtil</code> is a utility
	 * class.
	 */
	private ExceptionUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets whether the {@link Throwable} from the chain contains a
	 * {@link Throwable} that the given {@link Class} is assignable from.
	 * 
	 * @param pThrowable the {@link Throwable} from which to start.
	 * @param pClass the {@link Class} of the {@link Throwable} that you're
	 *            looking for.
	 * @return {@code true} if there is an {@link Throwable} in the chain which
	 *         matches the given type.
	 * @see #getThrowable(Throwable, Class)
	 * @see Class#isAssignableFrom(Class)
	 */
	public static boolean contains(Throwable pThrowable, Class<? extends Throwable> pClass)
	{
		return getThrowable(pThrowable, pClass) != null;
	}
	
	/**
	 * Writes the {@link Throwable} and its stacktrace into a {@link String}. If
	 * {@code pDeep} is {@code true} also all {@link Throwable#getCause()
	 * causes} will be written.
	 * 
	 * @param pCause the {@link Throwable} to dump.
	 * @param pDeep {@code true} to dump given {@link Throwable} and all
	 *            {@link Throwable#getCause() causes}, {@code false} to only
	 *            dump the given {@link Throwable}.
	 * @return the dumped {@link Throwable}.
	 */
	public static String dump(Throwable pCause, boolean pDeep)
	{
		if (pCause == null)
		{
			return "";
		}
		
		StringWriter stringWriter = new StringWriter(512);
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		if (pDeep)
		{
			Throwable cause = pCause;
			
			int depth = 0;
			
			while (cause != null)
			{
				if (depth > 0)
				{
					printWriter.print("Caused by: ");
				}
				
				printWriter.println(cause);
				
				for (StackTraceElement element : cause.getStackTrace())
				{
					printWriter.println("\tat " + element);
				}
				
				cause = cause.getCause();
				depth++;
			}
		}
		else
		{
			printWriter.println(pCause);
			
			for (StackTraceElement element : pCause.getStackTrace())
			{
				printWriter.println("\tat " + element);
			}
		}
		
		printWriter.close();
		
		return stringWriter.toString();
	}
	
	/**
	 * Gets all messages in the chain starting with {@code pThrowable}.
	 * 
	 * @param pThrowable the {@link Throwable} at which to start.
	 * @return the {@link List} of messages.
	 */
	public static List<String> getMessages(Throwable pThrowable)
	{
		if (pThrowable == null)
		{
			return Collections.emptyList();
		}
		
		List<String> messages = new ArrayList<String>();
		
		Throwable throwable = pThrowable;
		
		do
		{
			messages.add(throwable.getMessage());
			throwable = throwable.getCause();
		}
		while (throwable != null);
		
		return messages;
	}
	
	/**
	 * Gets all messages in the chain starting with {@code pThrowable}
	 * concatenated into a {@link String} with the given separator.
	 * 
	 * @param pThrowable the {@link Throwable} at which to start.
	 * @param pSeparator the separator to use.
	 * @return the concatenated messages.
	 * @see #getMessages(Throwable)
	 * @see StringUtil#concat(String, String...)
	 */
	public static String getMessages(Throwable pThrowable, String pSeparator)
	{
		List<String> messages = getMessages(pThrowable);
		
		if (messages.isEmpty())
		{
			return "";
		}
		
		return StringUtil.concat(pSeparator, messages.toArray(new String[messages.size()]));
	}
	
	/**
	 * Gets the {@link Throwable#getCause() root cause} of the given
	 * {@link Throwable}.
	 * 
	 * @param pThrowable the {@link Throwable} of which to get the
	 *            {@link Throwable#getCause() root cause}.
	 * @return the {@link Throwable#getCause() root cause} of the given
	 *         {@link Throwable}. {@code null} if {@code pThrowable} is
	 *         {@code null}. {@code pThrowable} is returned if it does not have
	 *         a cause.
	 */
	public static Throwable getRootCause(Throwable pThrowable)
	{
		if (pThrowable == null)
		{
			return null;
		}
		
		Throwable throwable = pThrowable;
		
		while (throwable.getCause() != null)
		{
			throwable = throwable.getCause();
		}
		
		return throwable;
	}
	
	/**
	 * Gets the {@link Throwable} from the chain that the given {@link Class} is
	 * assignable from.
	 * 
	 * @param <T> the type of the {@link Throwable}.
	 * @param pThrowable the {@link Throwable} from which to start.
	 * @param pClass the {@link Class} of the {@link Throwable} that you're
	 *            looking for.
	 * @return the {@link Throwable} from the chain that is assignable from the
	 *         given {@link Class}.
	 * @see #contains(Throwable, Class)
	 * @see Class#isAssignableFrom(Class)
	 */
	public static <T extends Throwable> T getThrowable(Throwable pThrowable, Class<T> pClass)
	{
		if (pThrowable == null || pClass == null)
		{
			return null;
		}
		
		Throwable throwable = pThrowable;
		
		do
		{
			if (pClass.isAssignableFrom(throwable.getClass()))
			{
				return (T)throwable;
			}
			
			throwable = throwable.getCause();
		}
		while (throwable != null);
		
		return null;
	}
	
	/**
	 * Gets a {@link List} of all {@link Throwable}s and
	 * {@link Throwable#getCause() causes} including {@code pThrowable} as first
	 * element.
	 * 
	 * @param pThrowable the {@link Throwable} of which to get all
	 *            {@link Throwable#getCause() causes}.
	 * @return a {@link List} of all {@link Throwable}s and
	 *         {@link Throwable#getCause() causes} including {@code pThrowable}
	 *         as first element.
	 */
	public static List<Throwable> getThrowables(Throwable pThrowable)
	{
		if (pThrowable == null)
		{
			return Collections.emptyList();
		}
		
		List<Throwable> causes = new ArrayList<Throwable>();
		Throwable throwable = pThrowable;
		
		do
		{
			causes.add(throwable);
			
			throwable = throwable.getCause();
		}
		while (throwable != null);
		
		return causes;
	}
	
}	// ExceptionUtil
