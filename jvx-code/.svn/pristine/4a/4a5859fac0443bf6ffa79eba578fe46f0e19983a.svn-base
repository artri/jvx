/*
 * Copyright 2011 SIB Visions GmbH
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
 * 11.12.2011 - [JR] - creation
 * 18.06.2015 - [JR] - #1413: IFileHandle support
 * 21.06.2018 - [JR] - recording implemented
 */
package com.sibvisions.rad.server.http.rest.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.rad.server.ISession;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

import com.sibvisions.rad.server.DirectServerSession;
import com.sibvisions.rad.server.http.rest.JSONUtil;
import com.sibvisions.rad.server.http.rest.RESTAdapter;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>AdminService</code> is a special administrative service.
 * 
 * @author René Jahn
 */
public abstract class AbstractCustomService extends AbstractService
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the DoS cache. */
	private static HashMap<String, Object[]> hmpDoSUser = new HashMap<String, Object[]>();
	
	/** the service get delegates. */
	private static Hashtable<String, ICustomServiceDelegate> htDelegates;
	
	/** the opened sessions per thread. */
	private ThreadLocal<List<DirectServerSession>> thlSessions = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Delegates the get request to custom services.
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the application name
	 * @param pAction the action
	 * @param pParameter the parameter
	 * @return the response
	 * @throws Throwable if delegation fails
	 */
	protected Representation delegateGet(Class pServiceClass, String pApplicationName, String pAction, String pParameter) throws Throwable
	{
		ICustomServiceGetDelegate delegate = (ICustomServiceGetDelegate)getGetService(pServiceClass, pApplicationName, pAction);
		
		if (delegate != null)
		{
			Object result = delegate.call(this, pApplicationName, pAction, pParameter);
			
			if (result == null)
			{							
				setStatus(Status.SUCCESS_NO_CONTENT);
				
			    return toInternalRepresentation(null);
			}
			else
			{
				setStatus(Status.SUCCESS_OK);
				
			    return toInternalRepresentation(result);
			}
		}	
		
		setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		
		return null;
	}
	
	/**
	 * Delegates the post request to custom services.
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the application name
	 * @param pAction the action
	 * @param pParameter the parameter
	 * @param pInput the input parameter
	 * @return the response
	 * @throws Throwable if delegation fails
	 */
	protected Representation delegatePost(Class pServiceClass, String pApplicationName, String pAction, String pParameter, Map<String, Object> pInput) throws Throwable
	{
		ICustomServicePostDelegate delegate = (ICustomServicePostDelegate)getPostService(pServiceClass, pApplicationName, pAction);
		
		if (delegate != null)
		{
			Object result = delegate.call(this, pApplicationName, pAction, pParameter, pInput);
			
			if (result == null)
			{							
				setStatus(Status.SUCCESS_NO_CONTENT);
				
			    return toInternalRepresentation(null);
			}
			else
			{
				setStatus(Status.SUCCESS_OK);
				
			    return toInternalRepresentation(result);
			}
		}
		
		setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		
		return null;
	}
	
	/**
	 * Checks whether a user is able to use the service. A user is unable to use it, if 3 connect attempts failed.
	 * The user will be able to try it again after a timeout of 3 minutes.
	 * 
	 * @param pUserName the user name
	 */
	protected void checkDoS(String pUserName)
	{
		//cleanup
		Set<Entry<String, Object[]>> set = hmpDoSUser.entrySet();
		
		long lNow = System.currentTimeMillis();
		
		for (Iterator<Entry<String, Object[]>> it = set.iterator(); it.hasNext();)
		{
			Entry<String, Object[]> entry = it.next();
			
			Object[] oValue = entry.getValue();
			
			//remove after 3 minutes "inactivity"
			if (((Long)oValue[0]).longValue() + 180000 < lNow)
			{
				set.remove(entry);
			}
		}
		
		//check
		Object[] oUser = (Object[])hmpDoSUser.get(pUserName);
		
		
		Long lTime = Long.valueOf(System.currentTimeMillis());
		
		int iCount;
		
		if (oUser != null)
		{
			iCount = ((Integer)oUser[1]).intValue() + 1;
		}
		else
		{
			iCount = 0;
		}			
	
		if (iCount >= 3)
		{
			//update time
			hmpDoSUser.put(pUserName, new Object[] {lTime, Integer.valueOf(2)});
			
			setStatus(Status.CLIENT_ERROR_TOO_MANY_REQUESTS);
			throw new SecurityException();
		}
		else
		{
			hmpDoSUser.put(pUserName, new Object[] {lTime, Integer.valueOf(iCount)});
		}
	}
	
	/**
	 * Removes the user from the DoS cache.
	 * 
	 * @param pUserName the user name
	 */
	private void clearDoS(String pUserName)
	{
		hmpDoSUser.remove(pUserName);
	}
	
	/**
	 * Registers a service delegation instance for an application and a custom action. This method doesn't
	 * override existing registrations. If an action has a delegate instance, it will kept.
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @param pDelegate the delegation instance
	 * @return <code>true</code> if registration was successful, <code>false</code> if action already contains
	 *         a delegation instance
	 */
	static boolean register(Class pServiceClass, String pApplicationName, String pAction, ICustomServiceDelegate pDelegate)
	{
		if (pApplicationName == null)
		{
			throw new SecurityException("Can't register a service delegate instance without application name!");
		}
		
		if (pAction == null)
		{
			throw new SecurityException("Can't register a service delegate without action name!");
		}

		if (pDelegate == null)
		{
			throw new SecurityException("Can't register a service delegate without instance!");
		}

		boolean bPut = false;
		boolean bFound = false;
		
		if (pDelegate instanceof ICustomServiceGetDelegate)
		{
			bPut |= putDelegate(pServiceClass.getName() + pApplicationName + "/@GET/" + pAction, pDelegate);
			
			bFound = true;
		}
		
		if (pDelegate instanceof ICustomServicePostDelegate)
		{
			bPut |= putDelegate(pServiceClass.getName() + pApplicationName + "/@POST/" + pAction, pDelegate);
			
			bFound = true;
		}
		
		if (!bFound)
		{
			throw new SecurityException("Unsupported delegate: " + pDelegate.getClass().getName());
		}
		
		return bPut;
	}
	
	/**
	 * Puts a service delegate to the cache.
	 * 
	 * @param pKey the cache key
	 * @param pDelegate the delegate
	 * @return <code>true</code> if added to the cache
	 */
	private static boolean putDelegate(String pKey, ICustomServiceDelegate pDelegate)
	{
		if (htDelegates != null)
		{
			if (htDelegates.containsKey(pKey))
			{
				return false;
			}
		}
		
		if (htDelegates == null)
		{
			htDelegates = new Hashtable<String, ICustomServiceDelegate>();
		}
		
		htDelegates.put(pKey, pDelegate);
		
		return true;
	}
	
	/**
	 * Unregisters a service delegation (GET and/or POST).
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @param pDelegate the delegation instance
	 * @param <D> the delegation type
	 * @return <code>true</code> if an existing delegation instance was removed, <code>false</code> if no delegation instance
	 *         was found
	 */
	static <D extends ICustomServiceDelegate> boolean unregister(Class pServiceClass, String pApplicationName, String pAction, D pDelegate)
	{
		if (pApplicationName == null)
		{
			throw new SecurityException("Can't unregister a service delegate instance without application name!");
		}
		
		if (pAction == null)
		{
			throw new SecurityException("Can't unregister a service delegate without action name!");
		}

		boolean bRemoved = false;
		
		
		if (pDelegate instanceof ICustomServiceGetDelegate && htDelegates != null)
		{
			synchronized(htDelegates)
			{
				String sKey = pServiceClass.getName() + pApplicationName + "/@GET/" + pAction;
				
				ICustomServiceDelegate removed = (ICustomServiceDelegate)htDelegates.get(sKey);
			
				if (removed == pDelegate)
				{
					removed = htDelegates.remove(sKey);
					
					bRemoved = true;
				}
			}
			
			if (htDelegates.isEmpty())
			{
				htDelegates = null;
			}
		}

		if (pDelegate instanceof ICustomServicePostDelegate && htDelegates != null)
		{
			synchronized(htDelegates)
			{
				String sKey = pServiceClass.getName() + pApplicationName + "/@POST/" + pAction;
				
				ICustomServiceDelegate removed = (ICustomServiceDelegate)htDelegates.get(sKey);
			
				if (removed == pDelegate)
				{
					removed = htDelegates.remove(sKey);
					
					bRemoved = true;
				}
			}
			
			if (htDelegates.isEmpty())
			{
				htDelegates = null;
			}
		}

		return bRemoved;
	}
	
	/**
	 * Unregisters a service delegation (GET and POST).
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @return <code>true</code> if an existing delegation instance was removed, <code>false</code> if no delegation instance
	 *         was found
	 */
	static boolean unregister(Class pServiceClass, String pApplicationName, String pAction)
	{
		return unregister(pServiceClass, pApplicationName, pAction, (Class<? extends ICustomServiceDelegate>)null);
	}	
	
	/**
	 * Unregisters a service delegation (GET and/or POST).
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @param pClass the delegate class type to remove
	 * @return <code>true</code> if an existing delegation instance was removed, <code>false</code> if no delegation instance
	 *         was found
	 */
	static boolean unregister(Class pServiceClass, String pApplicationName, String pAction, Class<? extends ICustomServiceDelegate> pClass)
	{
		if (pApplicationName == null)
		{
			throw new SecurityException("Can't unregister a service delegate instance without application name!");
		}
		
		if (pAction == null)
		{
			throw new SecurityException("Can't unregister a service delegate without action name!");
		}

		boolean bRemoved = false;
		
		
		if (ICustomServiceDelegate.class.equals(pClass) 
			|| ICustomServiceGetDelegate.class.isAssignableFrom(pClass) 
			|| htDelegates != null)
		{
			ICustomServiceGetDelegate removed = (ICustomServiceGetDelegate)htDelegates.remove(pServiceClass.getName() + pApplicationName + "/@GET/" + pAction);
			
			if (htDelegates.isEmpty())
			{
				htDelegates = null;
			}

			bRemoved |= removed != null;
		}
		
		if (ICustomServiceDelegate.class.equals(pClass) 
			|| ICustomServicePostDelegate.class.isAssignableFrom(pClass) 
			|| htDelegates != null)
		{
			ICustomServicePostDelegate removed = (ICustomServicePostDelegate)htDelegates.remove(pServiceClass.getName() + pApplicationName + "/@POST/" + pAction);
			
			if (htDelegates.isEmpty())
			{
				htDelegates = null;
			}

			bRemoved |= removed != null;
		}

		return bRemoved;
	}
	
	/**
	 * Checks if a custom service delegate is already registered.
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the name of the application
	 * @param pAction the action name
	 * @param pClass the delegate type class to check
	 * @return <code>true</code> if a delegate instance is registered, <code>false</code> otherwise
	 */
	static boolean isRegistered(Class pServiceClass, String pApplicationName, String pAction, Class<? extends ICustomServiceDelegate> pClass)
	{
		if (pApplicationName == null)
		{
			throw new SecurityException("Can't check a service delegate instance without application name!");
		}
		
		if (pAction == null)
		{
			throw new SecurityException("Can't check a service delegate without action name!");
		}

		boolean bRegistered = false;
		
		if (ICustomServiceDelegate.class.equals(pClass) 
			|| ICustomServiceGetDelegate.class.isAssignableFrom(pClass) 
			|| htDelegates != null)
		{
			bRegistered |= htDelegates.containsKey(pServiceClass.getName() + pApplicationName + "/@GET/" + pAction);
		}
		
		if (ICustomServiceDelegate.class.equals(pClass) 
			|| ICustomServicePostDelegate.class.isAssignableFrom(pClass) 
			|| htDelegates != null)
		{
			bRegistered |= htDelegates.containsKey(pServiceClass.getName() + pApplicationName + "/@POST/" + pAction);
		}
		
		return bRegistered;
	}	
	
	/**
	 * Gets a registered GET delegation service.
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the application name
	 * @param pAction the action
	 * @return the service or <code>null</code> if no service was found
	 */
	static ICustomServiceGetDelegate getGetService(Class pServiceClass, String pApplicationName, String pAction)
	{
		if (htDelegates == null) 
		{
			return null;
		}
		
		return (ICustomServiceGetDelegate)htDelegates.get(pServiceClass.getName() + pApplicationName + "/@GET/" + pAction);
	}
	
	/**
	 * Gets a registered POST delegation service.
	 * 
	 * @param pServiceClass the service class
	 * @param pApplicationName the application name
	 * @param pAction the action
	 * @return the service or <code>null</code> if no service was found
	 */
	static ICustomServicePostDelegate getPostService(Class pServiceClass, String pApplicationName, String pAction)
	{
		if (htDelegates == null) 
		{
			return null;
		}
		
		return (ICustomServicePostDelegate)htDelegates.get(pServiceClass.getName() + pApplicationName + "/@POST/" + pAction);
	}
	
	/**
	 * Creates a new {@link DirectServerSession} with given parameters.
	 * 
	 * @param pApplicationName the application name
	 * @param pUserName the user name
	 * @param pPassword the password
	 * @return the session
	 * @throws Throwable if session creation fails
	 */
	public ISession createSession(String pApplicationName, String pUserName, String pPassword) throws Throwable
	{
		return createSession(pApplicationName, pUserName, pPassword, null);
	}
	
	/**
	 * Creates a new {@link DirectServerSession} with given parameters.
	 * 
	 * @param pApplicationName the application name
	 * @param pUserName the user name
	 * @param pPassword the password
	 * @param pParams the session parameters (optional)
	 * @return the session
	 * @throws Throwable if session creation fails
	 */
	public ISession createSession(String pApplicationName, String pUserName, String pPassword, HashMap<String, Object> pParams) throws Throwable
	{
		HashMap<String, Object> hmpParams = RESTAdapter.createConnectionProperties(((RESTAdapter)getApplication()), getRequest());
		
		if (pParams != null)
		{
			hmpParams.putAll(pParams);
		}

		checkDoS(pUserName);
		
		DirectServerSession session = DirectServerSession.createMasterSession(pApplicationName, pUserName, pPassword, hmpParams);
		
		if (thlSessions == null)
		{
			thlSessions = new ThreadLocal<List<DirectServerSession>>();
			thlSessions.set(new ArrayUtil<DirectServerSession>());
		}
		
		List<DirectServerSession> liSessions = thlSessions.get();
		liSessions.add(session);
		
		return session;
	}
	
	/**
	 * Destroys all current sessions.
	 */
	protected void destroySessions()
	{
		if (thlSessions != null)
		{
			List<DirectServerSession> liSessions = thlSessions.get();
			
			if (liSessions != null)
			{
				for (DirectServerSession session : liSessions)
				{
					try 
					{
						session.destroy();
					}
					catch (Throwable th)
					{
						//ignore
					}
					finally
					{
						clearDoS(session.getUserName());
					}
				}
			}
		}
	}
	
	/**
	 * Applies session information to given record.
	 * 
	 * @param pRecord the record
	 */
	protected void applySession(Record pRecord)
	{
		if (pRecord != null)
		{
			List<DirectServerSession> liSessions = thlSessions.get();
			
			if (liSessions != null)
			{
				//only use first session
				if (liSessions.size() > 0)
				{
					pRecord.addIdentifier(liSessions.get(0).getId());
				}
			}
		}
	}
	
	/**
	 * Gets the input from the given representation as {@link HashMap}.
	 * 
	 * @param pParameter the representation from the request
	 * @return the read input parameter as {@link HashMap}
	 * @throws IOException if reading input from the representation fails
	 */
	protected HashMap<String, Object> getInput(Representation pParameter) throws IOException
	{
		HashMap<String, Object> hmpInput = null;
		
		if (MediaType.APPLICATION_OCTET_STREAM.equals(pParameter.getMediaType()))
		{
			hmpInput = new HashMap<String, Object>();
			
			try
			{
				hmpInput.put(ICustomServicePostDelegate.INPUT_STREAM, pParameter.getStream());
			}
			catch (Exception e)
			{
				error(e);
			}
		}
		else if (MediaType.MULTIPART_FORM_DATA.equals(pParameter.getMediaType(), true)
			     || MediaType.MULTIPART_ALL.equals(pParameter.getMediaType(), true))
		{
			//do nothing because touching the stream or request will consume it
		}
		else
		{
			hmpInput = JSONUtil.getObject(pParameter, HashMap.class);
		}
		
		if (hmpInput == null)
		{
			hmpInput = new HashMap<String, Object>();
		}
		
		return hmpInput;
	}
	
}	// AbstractCustomService
