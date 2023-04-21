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
 * 07.11.2008 - [JR] - creation
 * 12.02.2009 - [JR] - get: checked InvokationTarget via Reflective.getCause
 * 15.02.2009 - [JR] - extended from Bean and rewritten invoke (only call declared methods)
 *                   - changed recursion detection
 *                   - removed already implemented methods (Bean suppport)
 * 05.03.2010 - [JR] - invoke: support superclass calls
 * 22.10.2010 - [JR] - invoke: stop on superclass Object.class
 * 25.05.2011 - [JR] - #363: implemented ILifeCycleObject
 *                   - initBeanType: created PropertyDefinition with type-class
 * 22.11.2012 - [JR] - #608: ensure call hierarchy (fallback)
 * 30.07.2013 - [JR] - #739: force close in destroy
 * 23.08.2013 - [JR] - #773: destroy: close only used properties
 * 01.07.2014 - [JR] - #1084: fixed call hierarchy
 * 24.05.2018 - [JR] - #1927: get(Object) overwritten
 * 21.02.2019 - [JR] - #1992: introduced invoker
 *                   - #1993: don't put new object in get if getXXX method was called
 */
package com.sibvisions.rad.server;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

import javax.rad.type.AbstractType;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;
import javax.rad.type.bean.PropertyDefinition;
import javax.rad.util.INamedObject;

import com.sibvisions.rad.server.annotation.Inherit;
import com.sibvisions.rad.server.annotation.NotAccessible;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>GenericBean</code> handles the access to the values
 * of cached members and defined methods. If a member is uninitialized
 * the <code>GenericBean</code> will try to initialize it by calling
 * the init method for the member.
 * 
 * Examples for using implementing a GenericBean subclass
 * 
 * The fastest and safest way to use the GenericBean is to implement an init method 
 * for every property. If you have init methods you don't have to implement the get
 * methods, but it's good style to implement both: 
 * <pre>
 * public class Session extends GenericBean
 * {
 *     private DBAccess initDataSource() throws Exception
 *     {
 *         IConfiguration cfgSession = session.getCurrentSessionConfig();
 *         
 *         OracleDBAccess dba = new OracleDBAccess();
 *         
 *         dba.setConnection(cfgSession.getProperty("/application/securitymanager/database/url")); 
 *         dba.setUser(cfgSession.getProperty("/application/securitymanager/database/username"));
 *         dba.setPassword(cfgSession.getProperty("/application/securitymanager/database/password"));
 *         dba.open();
 *         
 *         return dba;
 *     }
 *     
 *     private DBStorage initPerson() throws Exception
 *     {
 *         DBStorage dbsPerson = new DBStorage();
 *        
 *         dbsPerson.setDBAccess(getDataSource());
 *         dbsPerson.setWriteBackTable("V_PERSON");
 *         dbsPerson.setFromClause("V_PERSON");
 *         dbsPerson.open();
 *        
 *         return dbsPerson;
 *     }
 *     
 *     public DBAccess getDataSource()
 *     {
 *         return (DBAccess)get("dataSource");
 *     }
 *     
 *     public DBStorage getPerson()
 *     {
 *         return (DBStorage)get("person");
 *     }
 * }
 * </pre>
 * It's also possible to integrate the initialization into the get method, thats recommended. The 
 * disadvantage of this implementation is that more calls will be made (That's the result of avoiding
 * recursive calls, because getPerson calls get("person") and this calls getPerson again), but you 
 * have the same flexibility as above and you have only one method where your object will be accessed:
 * <pre>
 * public class Session extends GenericBean
 * {
 *     public DBAccess getDataSource() throws Exception
 *     {
 *         OracleDBAccess dba = (OracleDBAccess)get("dataSource");
 *         
 *         if (dba == null)
 *         {
 *             IConfiguration cfgSession = session.getCurrentSessionConfig();
 *             
 *             dba = new OracleDBAccess();
 *         
 *             dba.setConnection(cfgSession.getProperty("/application/securitymanager/database/url")); 
 *             dba.setUser(cfgSession.getProperty("/application/securitymanager/database/username"));
 *             dba.setPassword(cfgSession.getProperty("/application/securitymanager/database/password"));
 *             dba.open();
 *         }
 *         
 *         return dba;
 *     }
 *     
 *     public DBStorage getPerson() throws Exception
 *     {
 *         DBStorage dbsPerson = (DBStorage)get("person");
 *        
 *         if (dbsPerson == null)
 *         {
 *             dbsPerson = new DBStorage();
 *            
 *             dbsPerson.setDBAccess(getDataSource());
 *             dbsPerson.setWriteBackTable("V_PERSON");
 *             dbsPerson.setFromClause("V_PERSON");
 *             dbsPerson.open();
 *         }
 *        
 *         return dbsPerson;
 *     }
 * }
 * </pre>
 * The EJB like implementation looks like the following:
 * <pre>
 * public class Session extends GenericBean
 * {
 *     private DBAccess dba;
 *     
 *     private DBStorage dbsPerson;
 *     
 * 
 *     public DBAccess getDataSource() throws Exception
 *     {
 *         if (dba == null)
 *         {
 *             IConfiguration cfgSession = session.getCurrentSessionConfig();
 *             
 *             dba = new OracleDBAccess();
 *         
 *             dba.setConnection(cfgSession.getProperty("/application/securitymanager/database/url")); 
 *             dba.setUser(cfgSession.getProperty("/application/securitymanager/database/username"));
 *             dba.setPassword(cfgSession.getProperty("/application/securitymanager/database/password"));
 *             dba.open();
 *             
 *             put("dataSource", dba);
 *         }
 *         
 *         return dba;
 *     }
 *     
 *     public DBStorage getPerson() throws Exception
 *     {
 *         if (dbsPerson == null)
 *         {
 *             dbsPerson = new DBStorage();
 *            
 *             dbsPerson.setDBAccess(getDataSource());
 *             dbsPerson.setWriteBackTable("V_PERSON");
 *             dbsPerson.setFromClause("V_PERSON");
 *             dbsPerson.open();
 *         }
 *        
 *         return dbsPerson;
 *     }
 * }
 * </pre>
 * The problem with above implementation is that the objects won't be managed from the expected GenericBean,
 * if use extends from another GenericBean implementation like Session. That's the case because the extended
 * class inherits all methods from the super class and all objects will be created in the inherited class if
 * you call a method. But the objects from the super class should be stored in the super class instance!
 * <b>We recommend to use the second or first implementation mechanism!</b>
 * 
 * It's also possible to ignore lazy loading and generic object access. When you call 
 * get("person") you will get another object as dba, when you didn't put the object. 
 * And with this solutions you get the exception before using the object and that's not 
 * always the right place.<br>
 * You can use one of the following mechanism:
 * <pre>
 * public class Session extends GenericBean
 * {
 *     private DBAccess dba = createDataSource();
 *     
 *     private DBStorage dbsPerson = createPerson();
 *     
 * 
 *     public Session() throws Exception
 *     {
 *         //important because the create methods throws Exceptions
 *     }
 * 
 *     //dont set the name to initDataSource, unless you put(...) the instance, because thats the name 
 *     //of an automatic called method
 *     private DBAccess createDataSource() throws Exception
 *     {
 *         IConfiguration cfgSession = session.getCurrentSessionConfig();
 *         
 *         OracleDBAccess dba = new OracleDBAccess();
 *         
 *         dba.setConnection(cfgSession.getProperty("/application/securitymanager/database/url")); 
 *         dba.setUser(cfgSession.getProperty("/application/securitymanager/database/username"));
 *         dba.setPassword(cfgSession.getProperty("/application/securitymanager/database/password"));
 *         dba.open();
 *         
 *         //with this call you can set the method name to initDataSource and you can use get("dataSource")
 *         //and getDataSource without problems
 *         put("dataSource", dba);
 *         
 *         return dba;
 *     }
 *     
 *     //dont set the name to initDataSource, unless you put(...) the instance, because thats the name 
 *     //of an automatic called method
 *     private DBStorage createPerson() throws Exception
 *     {
 *         DBStorage dbsPerson = new DBStorage();
 *        
 *         dbsPerson.setDBAccess(getDataSource());
 *         dbsPerson.setWriteBackTable("V_PERSON");
 *         dbsPerson.setFromClause("V_PERSON");
 *         dbsPerson.open();
 *        
 *         //with this call you can set the method name to initPerson and you can use get("person")
 *         //and getPerson without problems
 *         put("person", dbsPerson);
 *        
 *         return dbsPerson;
 *     }
 *     
 *     public DBAccess getDataSource()
 *     {
 *         return dba;
 *     }
 *     
 *     public DBStorage getPerson()
 *     {
 *         return dbsPerson;
 *     }
 * }
 * </pre>
 * Another way is:
 * <pre>
 * public class Session extends GenericBean
 * {
 *     private DBAccess dba;
 *     
 *     private DBStorage dbsPerson;
 *     
 * 
 *     public Session() throws Exception
 *     {
 *         IConfiguration cfgSession = session.getCurrentSessionConfig();
 *         
 *         OracleDBAccess dba = new OracleDBAccess();
 *         
 *         dba.setConnection(cfgSession.getProperty("/application/securitymanager/database/url")); 
 *         dba.setUser(cfgSession.getProperty("/application/securitymanager/database/username"));
 *         dba.setPassword(cfgSession.getProperty("/application/securitymanager/database/password"));
 *         dba.open();
 *         
 *         DBStorage dbsPerson = new DBStorage();
 *        
 *         dbsPerson.setDBAccess(getDataSource());
 *         dbsPerson.setWriteBackTable("V_PERSON");
 *         dbsPerson.setFromClause("V_PERSON");
 *         dbsPerson.open();
 *     }
 *     
 *     public DBAccess getDataSource()
 *     {
 *         return dba;
 *     }
 *     
 *     public DBStorage getPerson()
 *     {
 *         return dbsPerson;
 *     }
 * }
 * </pre>
 * 
 * @author René Jahn
 */
public abstract class GenericBean extends Bean
                                  implements ILifeCycleObject
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the logger. */
	private ILogger logger = null;

	
	/** the parent bean. */
	private Bean parent = null;

    /** the binding between properties and classes. */
    private Hashtable<PropertyDefinition, Class<?>> htProperties = new Hashtable<PropertyDefinition, Class<?>>();
	
	/** the property access for recursive call detection. */
	private boolean[] bPropertyAccess;
	
	/** whether the superclass check was done. */
	private boolean bInitialized = false;
	
	/** whether get requests should ignore object initialization. */
	private boolean bIgnoreInitialization = false;
	
	/** whether the instance is already destroyed. */
	private boolean bDestroyed = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>GenericBean</code> without a parent.
	 */
	public GenericBean()
	{
		super();
	}
	
	/**
	 * Initalizes the bean type with the property definition of this class and all super classes
	 * until the parent class was reached.
	 */
	private void initBeanType()
	{
		checkDestroyed();
		
	    if (bInitialized)
	    {
	        return;
	    }
	    
	    synchronized (this)
	    {
            Class<?> clazz = getClass();
            
            Method[] methods;
            
            String sName;
            
            boolean bGet;
    
            while (clazz != null 
                   && clazz != GenericBean.class
                   && (parent == null || clazz != parent.getClass()))
            {
                methods = clazz.getDeclaredMethods();
    
                for (Method met : methods)
                {
                    if (checkMethod(met))
                    {
                        sName = met.getName();
                        
                        bGet = sName.startsWith("get"); 
                        
                        if ((bGet && sName.length() > 3)
                            || (sName.startsWith("init") && sName.length() > 4))
                        {
                            if (bGet)
                            {
                                sName = sName.substring(3);
                            }
                            else
                            {
                                sName = sName.substring(4);
                            }
                            
                            sName = StringUtil.formatMemberName(sName);
                            
                            if (beanType.getPropertyIndex(sName) < 0)
                            {
                                PropertyDefinition propdef = new PropertyDefinition(sName, AbstractType.getTypeFromClass(met.getReturnType()), met.getReturnType());
                                
                                beanType.addPropertyDefinition(propdef);
                                htProperties.put(propdef, clazz);
                            }
                        }
                    }
                }
                
                clazz = clazz.getSuperclass();
            }
            
            bInitialized = true;
	    }
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	public void destroy()
	{
		Object oValue;

		//only destroy "used" or mapped objects
		for (int i = 0, cnt = beanType.getPropertyCount(); i < cnt; i++)
		{
		    try
		    {
    			oValue = super.get(i);
    
    			CommonUtil.close(oValue);
    			
                //unset
                super.put(i, null);
		    }
		    catch (Throwable th)
		    {
		        //ignore
		    }
		}
		
		Field[] fields = getClass().getFields();
		
		if (fields != null)
		{
			for (int i = 0, cnt = fields.length; i < cnt; i++)
			{
				try
				{
					oValue = fields[i].get(this);

					CommonUtil.close(oValue);
				}
				catch (Throwable th)
				{
					//ignore
				}
			}
		}
		
		bDestroyed = true;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		initBeanType();
		
		sb.append("Class = ");
		sb.append(getClass().getName());
		sb.append("\nProperty count = ");
		sb.append(beanType.getPropertyCount());
		sb.append("\nParent = ");
		
		if (getParent() == null)
		{
			sb.append("null");
		}
		else
		{
			sb.append("{");
			sb.append(getParent().toString());
			sb.append("}");
		}
		
		sb.append("\ndestroyed=" + Boolean.toString(bDestroyed));
		
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public boolean equals(Object pObject)
	{
		return super.equals(pObject);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public void clear()
	{
		super.clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public Bean clone()
	{
		return super.clone();
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public Object getObject()
	{
		return super.getObject();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public void putAll(Object pObject)
	{
		super.putAll(pObject);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public void put(int pIndex, Object pValue)
	{
		super.put(pIndex, pValue);
	}
	
    /**
     * {@inheritDoc}
     */
	@NotAccessible
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet()
    {
		return super.entrySet();
    }	
	
    /**
     * {@inheritDoc}
     */
	@NotAccessible
    @Override
	public int size() 
	{
		return super.size();
	}

    /**
     * {@inheritDoc}
     */
	@NotAccessible
    @Override
	public boolean isEmpty() 
	{
		return super.isEmpty();
	}

    /**
     * {@inheritDoc}
     */
	@NotAccessible
    @Override
	public Object remove(Object pObject) 
	{
		return super.remove(pObject);
	}
	
    /**
     * {@inheritDoc}
     */
	@NotAccessible
    @Override
	public Collection<Object> values() 
	{
		return super.values();
	}
	
    /**
     * {@inheritDoc}
     */
	@NotAccessible
    @Override
	public Set<String> keySet() 
	{
		return super.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public boolean containsKey(Object pKey)
	{
		checkDestroyed();
		
		if (pKey == null)
		{
			return false;
		}
		
		initBeanType();
		
		boolean bFound = super.containsKey(pKey);
		
		if (!bFound)
		{
			if (parent != null)
			{
				return parent.containsKey(pKey);
			}
		}
		
		return bFound;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public boolean containsValue(Object pValue)
	{
		checkDestroyed();
		
		if (pValue == null)
		{
			return false;
		}

		initBeanType();
		
		boolean bFound = super.containsValue(pValue);
		
		if (!bFound)
		{
    		if (parent != null)
    		{
    			return parent.containsValue(pValue);
    		}
		}
		
		return bFound;
	}

	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public Object get(Object pName)
	{
		return get(this, pName.toString());
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public Object get(String pName)
	{
		return get(this, pName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public Object put(String pPropertyName, Object pValue)
	{
        initBeanType();

        setName(pValue, pPropertyName);
		
		//avoid object initialization -> only get existing (initialized) objects
		bIgnoreInitialization = true;
		
		try
		{
			return super.put(pPropertyName, pValue);
		}
		finally
		{
			bIgnoreInitialization = false;
		}
	}
	
	/**
	 * Gets the value for a cached member variable. If the value for the 
	 * member is not cached, it will be created with following rules:
	 * <ul>
	 *   <li>call the init&lt;membername&gt; method</li>
	 *   <li>call the get&lt;membername&gt; method</li>
	 *   <li>delegate to the parent, if available</li>
	 * </ul>
	 * 
	 * @param pIndex the index of the property from the bean type
	 * @return the cached or created value; <code>null</code> if it's not possible
	 *         to create a value
	 * @throws RuntimeException if an error occurs during object creation
	 */
	@NotAccessible
	@Override
	public Object get(int pIndex) 
	{
        initBeanType();

        if (bIgnoreInitialization)
		{
			return super.get(pIndex);
		}
		else
		{
			return getIntern(this, pIndex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@NotAccessible
	@Override
	public BeanType getBeanType()
	{
	    initBeanType();
	    
	    return super.getBeanType();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the value of a specific property.
	 * 
	 * @param pInvoker the original invoker, if a parent is set and was called
	 * @param pName the property name
	 * @return the found object or <code>null</code>
	 */
	protected Object get(GenericBean pInvoker, String pName)
	{
	    initBeanType();
	    
		int index = beanType.getPropertyIndex(pName);
		
		//It is possible that a method is available in the parent 
		//but also in the super class beantype (because of the class hierarchy)
		if (index < 0)
		{
			Object oParent;

			if (parent != null)
			{
				if (parent instanceof GenericBean && parent.getClass().isInstance(pInvoker))
				{					
					oParent = ((GenericBean)parent).get(pInvoker, pName);
				}
				else
				{
					//We can't use the invoker here, because the parent is not a super class
					oParent = parent.get(pName);
				}
				
				if (parent.containsKey(pName))
				{
					return oParent;
				}
				else
				{
				    return null;
				}
			}
			
			//maybe added manually
			return super.get(pName);
		}
		
		return getIntern(pInvoker, index);
	}
	
	/**
	 * Sets the parent bean for this bean.
	 * 
	 * @param pParent the parent bean
	 */
	@NotAccessible
	public void setParent(Bean pParent)
	{
		checkDestroyed();
		
		parent = pParent;
		
		synchronized (this)
		{
    		beanType.removeAllPropertyDefinitions();
    		bInitialized = false;
    		
    		initBeanType();
		}
	}
	
	/**
	 * Gets the parent, if set.
	 * 
	 * @return the parent or <code>null</code> if not set
	 */
	@NotAccessible
	public Bean getParent()
	{
		checkDestroyed();
		
		return parent;
	}
	
	/**
	 * Invokes a method of this object via reflective call. If the method is not declared,
	 * the invocation will be delegated to the parent, if available.
	 * 
	 * @param pMethod the method name
	 * @param pParams the parameters for the method
	 * @return the return value of the method or <code>null</code> if the method
	 *         doesn't return a value
	 * @throws Throwable if the desired method is not available or the method throws
	 *                   an error during execution
	 */
	@NotAccessible
	public Object invoke(String pMethod, Object... pParams) throws Throwable
	{
		checkDestroyed();
		
		try
		{
            //if this object has no parent -> search all methods and not only declared
		    return invoke(getClass(), parent != null, pMethod, pParams);
		}
		catch (NoSuchMethodException nsme)
		{		
			//try only, if the parent is set, because the above call searches all methods if the parent
			//is not set
			if (parent != null)
			{
				Class<?> clsSuper = getClass().getSuperclass();
				Class<?> clsParent = parent.getClass();
				
				boolean bParentFound = false;
				
				while (clsSuper != Object.class)
				{
			        bParentFound = bParentFound || clsSuper == clsParent;

				    if (!bParentFound)
				    {
	                    try
	                    {
	                        return invoke(clsSuper, true, pMethod, pParams);
	                    }
	                    catch (NoSuchMethodException nsmex)
	                    {
	                        //try the next superclass 
	                        clsSuper = clsSuper.getSuperclass();
	                    }
				    }
				    else
				    {
				        Method method = Reflective.getMethodForCall(clsSuper, true, pMethod, pParams);
				        
				        if (method != null)
				        {
				        	if (AccessHelper.isNotAccesible(method))
				            {
				                throw new SecurityException("Access to " + pMethod + " denied!"); 
				            }
				            
				            //it's possible that the method was overwritten and the super method
				            //has the inherit annotation
				            if (method.isAnnotationPresent(Inherit.class))
				            {
				                return Reflective.call(this, method, pParams);
				            }
				        }
				        
				        clsSuper = clsSuper.getSuperclass();
				    }
				}
				
				//last option: delegate to the parent
				if (parent instanceof GenericBean)
				{
					return ((GenericBean)parent).invoke(pMethod, pParams);
				}
			}
			
			throw nsme;
		}
	}
	
	/**
	 * Invokes a method from this bean.
	 * 
	 * @param pBaseClass the base class where the method should be declared 
	 * @param pOnlyDeclared <code>true</code> to search only declared methods, <code>false</code> to search any method
	 * @param pMethod the method name
	 * @param pParams the parameters
	 * @return the result from the method call
	 * @throws Throwable if an exception occurs during invocation
	 * @throws SecurityException if method isn't accessible
	 * @see com.sibvisions.rad.server.annotation.NotAccessible
	 */
    private Object invoke(Class<?> pBaseClass, boolean pOnlyDeclared, String pMethod, Object... pParams) throws Throwable
    {
    	Method method = Reflective.getMethodForCall(pBaseClass, pOnlyDeclared, pMethod, pParams);

        if (method != null)
        {
            if (AccessHelper.isNotAccesible(method))
            {
                throw new SecurityException("Access to " + pMethod + " denied!");
            }
            
            return Reflective.call(this, method, pParams);
        }
        
        throw new NoSuchMethodException(Reflective.getMethodDeclaration(pBaseClass, pMethod, pParams));
    }	
	
	/**
	 * Gets the value for a cached member variable. If the value for the 
	 * member is not cached, it will be created with following rules:
	 * <ul>
	 *   <li>call the init&lt;membername&gt; method</li>
	 *   <li>call the get&lt;membername&gt; method</li>
	 *   <li>delegate to the parent, if available</li>
	 * </ul>
	 * 
	 * @param pInvoker the invoker, if a parent is set and was called
	 * @param pIndex the index of the property from the internal bean type
	 * @return the cached or created value; <code>null</code> if it's not possible
	 *         to create a value
	 * @throws RuntimeException if an error occurs during object creation
	 */
	private Object getIntern(GenericBean pInvoker, int pIndex)
	{
	    if (pIndex < 0) // get with non existing index has to return null 
	    {
	        return null;
	    }
	    else if (bPropertyAccess == null)
		{
		    bPropertyAccess = new boolean[pIndex + 1];
		}
		else if (pIndex >= bPropertyAccess.length)
		{
			boolean[] baCopy = new boolean[Math.max(beanType.getPropertyCount(), pIndex + 1)];
			
			System.arraycopy(bPropertyAccess, 0, baCopy, 0, bPropertyAccess.length);
			
			bPropertyAccess = baCopy;
		}
		
        Object oValue = super.get(pIndex);
        
        //avoid recursive calls with the same object name 
		if (oValue != null || bPropertyAccess[pIndex])
		{
			return oValue;
		}
		
		try
		{
		    bPropertyAccess[pIndex] = true;
			
			PropertyDefinition propdef = beanType.getPropertyDefinition(pIndex); 
			
			String sPropertyName = propdef.getName();
			
			Class<?> clazz = htProperties.get(propdef);
			
			//first step: try init method
			Method method = getMethod(clazz, "init", sPropertyName);
			
			if (checkMethod(method))
			{
				GenericBean bnInvoker = this;
				
	            if (AccessHelper.isNotAccesible(method))
	            {
	                throw new SecurityException("Access to " + sPropertyName + " denied!");
	            }
				
				if (method.isAnnotationPresent(Inherit.class))
				{
					bnInvoker = pInvoker;
				}
				
				oValue = Reflective.invoke(bnInvoker, method);
				
				Object compareValue = bnInvoker.getRaw(pIndex);
				
				if (compareValue != oValue)
				{
					bnInvoker.put(sPropertyName, oValue);
				}
				else
				{
					setName(oValue, sPropertyName);
				}
				
				return oValue;
			}
			
			//second step: try get method
			method = getMethod(clazz, "get", sPropertyName);
			
			if (checkMethod(method))
			{
				GenericBean bnInvoker = this;
				
	            if (AccessHelper.isNotAccesible(method))
	            {
	                throw new SecurityException("Access to " + sPropertyName + " denied!");
	            }

	            if (method.isAnnotationPresent(Inherit.class))
				{
					bnInvoker = pInvoker;
				}

				oValue = Reflective.invoke(bnInvoker, method);
				
			    setName(oValue, sPropertyName);

			    return oValue;
			}
			
			//third step: delegate to the parent, if possible
			if (parent != null)
			{
				if (parent instanceof GenericBean)
				{
					return ((GenericBean)parent).get(pInvoker, sPropertyName);
				}
				else
				{
					return parent.get(sPropertyName);
				}
			}
			
			return null;
		}
		catch (Throwable th)
		{
			if (th instanceof InvocationTargetException)
			{
				th = ((InvocationTargetException)th).getCause();
			}
			
			if (th instanceof RuntimeException)
			{
				throw (RuntimeException)th;
			}
			else
			{
				throw new RuntimeException(th != null ? th.getMessage() : null, th);
			}
		}
		finally
		{
		    bPropertyAccess[pIndex] = false;
		}
	}
	
	/**
	 * Gets a method for a member variable.
	 * 
	 * @param pClass the class that contains the method
	 * @param pPrefix the method prefix (init, get, ...)
	 * @param pName the name of the member variable
	 * @return the method or <code>null</code> if the method is not available
	 */
	private Method getMethod(Class<?> pClass, String pPrefix, String pName)
	{
		try
		{
			Class<?> clazz = pClass;
			
			if (clazz == null)
			{
				clazz = getClass();
			}
			
			return clazz.getDeclaredMethod(StringUtil.formatMethodName(pPrefix, pName));
		}
		catch (NoSuchMethodException nsme)
		{
			return null;
		}
	}
	
	/**
	 * Checks if a method is a valid method to get/init members. A method is valid
	 * if it is not static and it has no parameters.
	 * 
	 * @param pMethod the method to check
	 * @return <code>true</code> if the method is a valid member method
	 */
	private boolean checkMethod(Method pMethod)
	{
		if (pMethod == null)
		{
			return false;
		}
		
		int iModifiers = pMethod.getModifiers();
		
		return !Modifier.isStatic(iModifiers) 
		       && (pMethod.getParameterTypes() == null || pMethod.getParameterTypes().length == 0);		
	}
	
	/**
	 * Checks if bean is already destroyed.
	 */
	private void checkDestroyed()
	{
		if (bDestroyed)
		{
			throw new IllegalStateException("Can't use an already destroyed bean");
		}
	}
	
	/**
	 * Tries to set the name of the given object, if it's an instance of {@link INamedObject}.
	 * 
	 * @param pObject the object 
	 * @param pName the name to set
	 */
	private void setName(Object pObject, String pName)
	{
        if (pObject instanceof INamedObject)
		{
			try
			{
				((INamedObject)pObject).setName(pName);
			}
			catch (Exception pException)
			{
				// Do nothing
			}
		}		
	}
	
	/**
	 * Gets the value of a property by index, by calling the super method.
	 * 
	 * @param pIndex the property index
	 * @return the found value
	 */
	private Object getRaw(int pIndex)
	{
		return super.get(pIndex);
	}

	/**
	 * Logs debug information.
	 * 
	 * @param pInfo the debug information
	 */
	protected void debug(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}
		
		logger.debug(pInfo);
	}

	/**
	 * Logs information.
	 * 
	 * @param pInfo the information
	 */
	protected void info(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}
		
		logger.info(pInfo);
	}
	
	/**
	 * Logs error information.
	 * 
	 * @param pInfo the error information
	 */
	protected void error(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}

		logger.error(pInfo);
	}	
	
}	// GenericBean
