/*
 * Copyright 2012 SIB Visions GmbH
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
 * 20.11.2012 - [JR] - creation
 */
package com.sibvisions.naming;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * The <code>MemoryContext</code> is a simple {@link Context} that caches all bound objects in
 * a {@link Hashtable}.
 * 
 * @author René Jahn
 */
public class MemoryContext implements Context
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the environment settings. */
	private Hashtable<String, Object> htEnv = new Hashtable<String, Object>();
	
	/** the bound object cache. */
	private static Hashtable<String, Object> htCache = new Hashtable<String, Object>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object lookup(javax.naming.Name pName) throws NamingException
	{
		return htCache.get(pName.get(0));
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lookup(String pName) throws NamingException
	{
		return htCache.get(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void bind(javax.naming.Name pName, Object pObj) throws NamingException
	{
		if (htCache.contains(pName.get(0)))
		{
			throw new NameAlreadyBoundException(pName.get(0));
		}
		
		htCache.put(pName.get(0), pObj);
	}

	/**
	 * {@inheritDoc}
	 */
	public void bind(String pName, Object pObj) throws NamingException
	{
		if (htCache.contains(pName))
		{
			throw new NameAlreadyBoundException(pName);
		}

		htCache.put(pName, pObj);
	}

	/**
	 * {@inheritDoc}
	 */
	public void rebind(javax.naming.Name pName, Object pObj) throws NamingException
	{
		htCache.put(pName.get(0), pObj);
	}

	/**
	 * {@inheritDoc}
	 */
	public void rebind(String pName, Object pObj) throws NamingException
	{
		htCache.put(pName, pObj);
	}

	/**
	 * {@inheritDoc}
	 */
	public void unbind(javax.naming.Name pName) throws NamingException
	{
		htCache.remove(pName.get(0));		
	}

	/**
	 * {@inheritDoc}
	 */
	public void unbind(String pName) throws NamingException
	{
		htCache.remove(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void rename(javax.naming.Name pOldName, javax.naming.Name pNewName) throws NamingException
	{
		Object objOld = htCache.remove(pOldName.get(0));
		
		if (objOld == null)
		{
			throw new NameNotFoundException(pOldName.get(0));
		}
		
		htCache.put(pNewName.get(0), objOld);
	}

	/**
	 * {@inheritDoc}
	 */
	public void rename(String pOldName, String pNewName) throws NamingException
	{
		Object objOld = htCache.remove(pOldName);
		
		if (objOld == null)
		{
			throw new NameNotFoundException(pOldName);
		}
		
		htCache.put(pNewName, objOld);
	}

	/**
	 * {@inheritDoc}
	 */
	public NamingEnumeration<NameClassPair> list(javax.naming.Name name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NamingEnumeration<NameClassPair> list(String name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NamingEnumeration<Binding> listBindings(javax.naming.Name name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NamingEnumeration<Binding> listBindings(String name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroySubcontext(javax.naming.Name name) throws NamingException
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroySubcontext(String name) throws NamingException
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public Context createSubcontext(javax.naming.Name name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Context createSubcontext(String name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lookupLink(javax.naming.Name name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lookupLink(String name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NameParser getNameParser(javax.naming.Name name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public NameParser getNameParser(String name) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public javax.naming.Name composeName(javax.naming.Name name, javax.naming.Name prefix) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String composeName(String name, String prefix) throws NamingException
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addToEnvironment(String pPropName, Object pPropVal) throws NamingException
	{
		return htEnv.put(pPropName, pPropVal);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object removeFromEnvironment(String pPropName) throws NamingException
	{
		return htEnv.remove(pPropName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Hashtable<?, ?> getEnvironment() throws NamingException
	{
		return htEnv;
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws NamingException
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public String getNameInNamespace() throws NamingException
	{
		return null;
	}
	
}	// MemoryContext
