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
 * 08.04.2009 - [JR] - creation
 * 30.11.2010 - [JR] - equals implemented
 * 11.02.2011 - [JR] - getFirstCause implemented
 * 29.07.2011 - [JR] - getCauseList implemented
 * 12.09.2011 - [JR] - getFreePort implemented
 * 07.12.2012 - [JR] - isReachable implemented
 * 29.03.2013 - [JR] - containsException implemented
 * 12.09.2013 - [JR] - getFreePort with 127.0.0.1 check
 */
package com.sibvisions.util.type;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import com.sibvisions.util.ICloseable;

/**
 * The <code>CommonUtil</code> contains utility methods for handling
 * type independent operations.
 * 
 * @author René Jahn
 */
public final class CommonUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>CommonUtil</code> is a utility
	 * class.
	 */
	private CommonUtil()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Writes the stack trace of an exception/throwable object into a string.
	 * 
	 * @param pCause Exception/Throwable (StackTrace)
	 * @param pDeep <code>true</code> to dump the trace with all causes; <code>false</code> to dump
	 *        only the exception
	 * @return stack trace
	 * @deprecated since 2.3, use {@link ExceptionUtil#dump(Throwable, boolean)} instead.
	 */
	@Deprecated
	public static String dump(Throwable pCause, boolean pDeep)
	{
		return ExceptionUtil.dump(pCause, pDeep);
	}

	/**
	 * Gets the first cause.
	 * 
	 * @param pCause the origin cause
	 * @return the first cause
	 * @deprecated since 2.3, use {@link ExceptionUtil#getRootCause(Throwable)} instead.
	 */
	@Deprecated
	public static Throwable getFirstCause(Throwable pCause)
	{
		return ExceptionUtil.getRootCause(pCause);
	}
	
	/**
	 * Gets the list of all available exceptions.
	 * 
	 * @param pCause the start exception
	 * @return the list of all causes and the exception itself. The start exception is
	 *         the first entry and the cause of the start exception is the second entry
	 *         and so on.
	 * @deprecated since 2.3, use {@link ExceptionUtil#getThrowables(Throwable)} instead.
	 */
	@Deprecated
	public static List<Throwable> getCauseList(Throwable pCause)
	{
		return ExceptionUtil.getThrowables(pCause);
	}
	
	/**
	 * Gets whether an exception chain is or contains a specific error class.
	 * This methods checks whether the given class is assignable from the found
	 * exception cause.
	 * 
	 * @param pCause the exception (chain)
	 * @param pClass the expected class
	 * @return <code>true</code> if the given class or a sub class was found in the exception 
	 *         chain, <code>false</code> otherwise
	 * @see Class#isAssignableFrom(Class)
	 * @deprecated since 2.3, use {@link ExceptionUtil#contains(Throwable, Class)} instead.
	 */
	@Deprecated
	public static boolean containsException(Throwable pCause, Class<?> pClass)
	{
		return getException(pCause, pClass) != null;
	}
	
	/**
	 * Gets an exception of the given type from an exception chain.
	 * This methods checks whether the given class is assignable from the found
	 * exception cause.
	 * 
	 * @param pCause the exception (chain)
	 * @param pClass the expected class
	 * @return the found exception of <code>null</code> if no exception with the given type was found
	 * @see Class#isAssignableFrom(Class)
	 * @deprecated since 2.3, use {@link ExceptionUtil#getThrowable(Throwable, Class)} instead.
	 */
	@Deprecated
	public static Throwable getException(Throwable pCause, Class<?> pClass)
	{
		return ExceptionUtil.getThrowable(pCause, (Class<Throwable>) pClass);
	}
	
	/**
	 * Gets an alternative value for a <code>null</code> object.
	 * 
	 * @param <T> parameter type
	 * @param pValue desired value
	 * @param pNvlValue alternative value 
	 * @return <code>pValue</code> or <code>pNvlValue</code> if <code>pValue == null</code>
	 */
	public static <T> T nvl(T pValue, T pNvlValue)
	{
		if (pValue == null)
		{
			return pNvlValue;
		}
		
		return pValue;
	}
	
	/**
	 * Indicates whether two object are "equal". Two objects are equals if both are <code>null</code> or
	 * the {@link Object#equals(Object)} returns <code>true</code>.
	 * <p>
	 * If the given objects are of {@link Reference} (and not of the same reference)
	 * their {@link Reference#get() value} will be compared.
	 * 
	 * @param pFirst an object
	 * @param pSecond another object
	 * @return <code>true</code> if both objects are equal
	 */
	public static boolean equals(Object pFirst, Object pSecond)
	{
		if (pFirst == pSecond
				|| (pFirst != null && pFirst.equals(pSecond))
				|| (pSecond != null && pSecond.equals(pFirst)))
		{
			return true;
		}
		
		Object first = pFirst;
		Object second = pSecond;
		
		if (pFirst instanceof Reference<?>)
		{
			first = ((Reference)pFirst).get();
		}
		
		if (pSecond instanceof Reference<?>)
		{
			second = ((Reference)pSecond).get();
		}
		
		return first == second
			   || (first != null && first.equals(second))
			   || (second != null && second.equals(first))
			   || (first instanceof Object[] && second instanceof Object[] && Arrays.equals((Object[])first, (Object[])second));
	}

    /**
     * Searchs a free *network* port in the given range. The local interface (127.0.0.1) 
     * won't be checked.
     * 
     * @param pMin the min port
     * @param pMax the max port
     * @return -1 if no free port is available in the given range
     */
    public static int getFreePort(int pMin, int pMax)
    {
    	return getFreePort(pMin, pMax, false);
    }
	
    /**
     * Searchs a free port in the given range.
     * 
     * @param pMin the min port
     * @param pMax the max port
     * @param pCheckLocal <code>true</code> to check local (127.0.0.1) interface as well as network interface (0.0.0.0),
     *                    <code>false</code> to check only the network interface
     * @return -1 if no free port is available in the given range
     */
    public static int getFreePort(int pMin, int pMax, boolean pCheckLocal)
    {
    	ServerSocket ssok = null;
    	
    	
    	for (int i = pMin; i <= pMax; i++)
    	{
    		try
    		{
    			ssok = new ServerSocket();
    			
    			if (pCheckLocal)
    			{
    				try
    				{
    					ssok.bind(new InetSocketAddress("127.0.0.1", i));
    				}
    				finally
    				{
    					try
    					{
    						ssok.close();
    					}
    					catch (Exception e)
    					{
    						//nothing to be done
    					}
    				}

					ssok = new ServerSocket();
    			}
    			
				ssok.bind(new InetSocketAddress("0.0.0.0", i));
    			
    			return i;
    		}
    		catch (IOException ioe)
    		{
    			//try next port
    		}
    		finally
    		{
    			if (ssok != null)
    			{
    				try
    				{
    					ssok.close();
    				}
    				catch (Exception e)
    				{
    					//nothing to be done
    				}
    			}
    		}
    	}
    	
    	return -1;
    }	
    
    /**
     * Tests if the given port is reachable on the given host.
     * 
     * @param pHost the hostname or IP
     * @param pPort th port number
     * @return <code>true</code> if the port is reachable, <code>false</code> otherwise
     */
    public static boolean isReachable(String pHost, int pPort)
    {
    	Socket sok = null;
    	
		try
		{
			sok = new Socket();
	        sok.setReuseAddress(true);
	        sok.setSoTimeout(3000);
	        
	        SocketAddress sa = new InetSocketAddress(pHost, pPort);
	        sok.connect(sa, 3000);
	        
	        return true;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			if (sok != null)
			{
				try
				{
			        sok.close();
				}
				catch (Exception e)
				{
					//nothing to be done
				}
			}
		}
    }

    /**
     * Close the given object(s) if closable. An object is closable if it contains a 
     * close() method.
     * 
     * @param <T> the closable type
     * @param pClosable the object(s) to close
     * @return <code>null</code>
     */
    public static <T> T close(Object... pClosable)
    {
        if (pClosable != null)
        {
            Object obj;
            
            for (int i = 0; i < pClosable.length; i++)
            {
                obj = pClosable[i];
                
                if (obj != null)
                {
                    try
                    {
                        if (obj instanceof ICloseable)
                        {
                            ((ICloseable)obj).close();
                        }
                        else if (obj instanceof Connection)
                        {
                            ((Connection)obj).close();
                        }
                        else if (obj instanceof Statement)
                        {
                            ((Statement)obj).close();
                        }
                        else if (obj instanceof ResultSet)
                        {
                            ((ResultSet)obj).close();
                        }
                        else if (obj instanceof Closeable)
                        {
                            ((Closeable)obj).close();
                        }
                        else if (obj instanceof Clob)
                        {
                            ((Clob)obj).free();
                        }
                        else if (obj instanceof Blob)
                        {
                            ((Blob)obj).free();
                        }
                        else if (obj instanceof DatagramSocket)
                        {
                            ((DatagramSocket)obj).close();
                        }
                        else
                        {
                            //Not in any case, because we want to check above objects for documentation
                            Method met = obj.getClass().getMethod("close");
                            
                            met.invoke(obj);
                        }
                    }
                    catch (Throwable th)
                    {
                    	// We are not interested in Logspam from not being able to close
                    	// This is beeing called for example for > 10000 Blobs, if the connection is closed...
                        // LoggerFactory.getInstance(CommonUtil.class).debug(th);
                    }
                }
            }
        }
        
        return null;
    }
    
}	// CommonUtil
