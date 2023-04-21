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
 * 01.10.2008 - [JR] - creation
 */
package com.sibvisions.rad.server;

import java.util.HashMap;
import java.util.List;

import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnection;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.ArrayUtil;

/**
 * Tests the <code>GenericBean</code> functionality.
 * 
 * @author René Jahn
 * @see GenericBean
 */
public class TestGenericBean
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test the getProperties method.
	 */
	@Test
	public void testGetProperties()
	{
		Global global = new Global();
		
		String[] sProps = global.getBeanType().getPropertyNames();
		
		List<String> liProps = new ArrayUtil<String>(sProps);

		Assert.assertEquals(5, sProps.length);
		Assert.assertTrue("'address' property is missing!", liProps.contains("address"));
		Assert.assertTrue("'email' property is missing!", liProps.contains("email"));
		Assert.assertTrue("'principalAddress' property is missing!", liProps.contains("principalAddress"));
		Assert.assertTrue("'adressString' property is missing!", liProps.contains("adressString"));
		Assert.assertTrue("'notAccessibleAddress' property is missing!", liProps.contains("notAccessibleAddress"));
	}
	
	/**
	 * Tests the get method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGet() throws Throwable
	{
		//GLOBAL Level
		
		Global global = new Global();

		Address adr = global.getPrincipalAddress();
		
		Assert.assertNotNull("PrincipalAddress not created", adr);
		Assert.assertEquals("Nikolausgasse 25", adr.getAddress());
		
		adr = (Address)global.get("notAccessibleAddress");
		
		Assert.assertNotNull("Accessible address created", adr);
		
		//APPLICATION Level (reuse Global object)
		
		Application application = new Application();
		application.setParent(global);
		
		Assert.assertEquals("Nikolausgasse 25", application.getApplicationAddress().getAddress());
		
		//APPLICATION Level (new Global object)
		
		global = new Global();
		
		application = new Application();
		application.setParent(global);
		
		Assert.assertEquals("Nikolausgasse 25", application.getApplicationAddress().getAddress());
		
		//APPLICATION-Global Level (reuse Global object)

		ApplicationGlobal applGlobal = new ApplicationGlobal();
		applGlobal.setParent(global);
		
		Assert.assertEquals("Nikolausgasse 25", applGlobal.getApplicationAddress().getAddress());
		
		//APPLICATION-Global Level (new Global object)

		global = new Global();
		
		applGlobal = new ApplicationGlobal();
		applGlobal.setParent(global);
		
		Assert.assertEquals("Nikolausgasse 25", applGlobal.getApplicationAddress().getAddress());
	}
	
	/**
	 * Tests the invoke method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testInvoke() throws Throwable
	{
		Global global = new Global();
		
		Application application = new Application();
		application.setParent(global);		
		
		//Invoke a method defined in the parent bean
		Assert.assertEquals("The email method", application.invoke("getEmail"));
	}
	
	/**
	 * Tests a recursive object call.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testRecursiveGet() throws Throwable
	{
		ApplicationGlobal application = new ApplicationGlobal();
		
		Assert.assertNull(application.get("recursion"));
	}
	
	/**
	 * Tests method invocation with and without parent inheritance.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDeclaredMethodInvocation() throws Throwable
	{
		Global global = new Global();
		
		Application app = new Application();
		app.setParent(global);
		
		SubScreen screen = new SubScreen();
		screen.setParent(app);
		
		Assert.assertEquals("Topic", screen.invoke("getTopic"));
		
		Assert.assertNotNull("Method invocation not possible!", screen.invoke("getApplicationAddress"));
		
		screen.setParent(null);
		
		//to call inherited methods, without parent!
		screen.invoke("getApplicationAddress");
	}
	
	/**
	 * Tests invocation of methods with superclass fallback support.
	 * 
	 * @throws Throwable if test fails.
	 */
	@Test
	public void testSuperClassAccess608() throws Throwable
	{
		DirectServerConnection con = new DirectServerConnection();

		
		MasterConnection macon = null;

		try
		{
			macon = new MasterConnection(con);
			macon.setLifeCycleName("demo.special.Address");
			macon.setApplicationName("demo");
			macon.setUserName("rene");
			macon.setPassword("rene");
			macon.open();
	
			//method is defined in address
			macon.call("address", "getMetaData");
			
			//method is defined in a superclass
			macon.call("data", "getBytes", "UTF-8");
			
			//method is defined in parent
			macon.call("application", "getAbsolutePath");
		}
		finally
		{
			macon.close();
		}
	}	
	
	/**
	 * Tests method calls with check of declared method hierarchy.
	 */
	@Test
	public void testDeclaredMethodCall()
	{
	    MySession session = new MySession();
	    
	    ScreenLCO lco = new ScreenLCO();
	    lco.setParent(session);
	    
	    //a call of our declared methods
	    Assert.assertEquals("ScreenLCO.getMemory", lco.get("memory"));
	    Assert.assertEquals("ScreenLCO.DBAccess", lco.get("dBAccess"));
	    
	    //not explicitely declared -> must be a call of MySession
	    Assert.assertEquals("MySession.MasterDBAccess", lco.get("masterDBAccess"));
	}
	
	/**
	 * Tests call recursion.
	 */
	@Test
	public void testRecursion()
	{
        MySession session = new MySession();
        
        ScreenLCO lco = new ScreenLCO();
        lco.setParent(session);

        Assert.assertNull(lco.get("recursive"));
        Assert.assertNull(lco.get("method1"));
        Assert.assertNull(lco.get("method2"));
	}
	
	/**
	 * Tests call of put twice on get.
	 */
	@Test
	public void testDoublePutOnGet()
	{
		TestPutGenericBean test = new TestPutGenericBean();
		
		test.getHallo();
		
		Assert.assertEquals(1, test.getCallCount("hallo"));
		
		test.get("hallo2");
		
		Assert.assertEquals(1, test.getCallCount("hallo2"));
		
		test.get("hallo3");
		
		Assert.assertEquals(1, test.getCallCount("hallo3"));

		test.get("hallo4");
		
		Assert.assertEquals(1, test.getCallCount("hallo4"));
	}
	
	/**
	 * Tests toString call.
	 * 
	 * @throws Throwable if test fails
	 */
	@Test
	public void testToStringCall() throws Throwable
	{
		Throwable thError = null;
		
		IConnection con = new DirectServerConnection();
		
		ConnectionInfo coninfo = new ConnectionInfo();
		
		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "xmlusers");
		coninfo.getProperties().put(IConnectionConstants.USERNAME, "admin");
		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "admin");

		try
		{
			con.open(coninfo);
			
			String sValue = (String)con.call(coninfo, new String[] {null}, new String[] {"toString"}, null, null)[0];
			
			Assert.assertTrue(sValue != null && sValue.startsWith("Class = demo.Application"));
			
			try
			{
				con.call(coninfo, new String[] {null}, new String[] {"destroy"}, null, null);
				
				Assert.fail("Inaccessible method called!");
			}
			catch (SecurityException se)
			{
				Assert.assertEquals("Access to destroy denied!", se.getMessage());
			}
		}
		catch (Throwable th)
		{
			thError = th;
		}
		finally
		{
			try
			{
				con.close(coninfo);
			}
			catch (Throwable th)
			{
				if (thError == null)
				{
					thError = th;
				}
			}
			
			if (thError != null)
			{
				throw thError;
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>Global</code> class is a simple test implementation of
	 * GenericBean.
	 * 
	 * @author René Jahn
	 */
	public static class Global extends GenericBean
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates the principalAddress object.
		 * 
		 * @return the principal address object
		 * @throws RuntimeException if an error occurs during initialization
		 */
		@SuppressWarnings("unused")
		private Object initPrincipalAddress()
		{
			return new PrincipalAddress("Nikolausgasse 25");
		}
		
		/**
		 * Creates an object for a member without get method.
		 * 
		 * @return an address object
		 * @throws RuntimeException if an error occurs during initialization
		 */
		@SuppressWarnings("unused")
		private Object initNotAccessibleAddress()
		{
			return new PrincipalAddress("Not accessible");
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets address.
		 * 
		 * @return <code>"Adress"</code>
		 */
		public Object getAdressString()
		{
			return "AddressString";
		}
		
		/**
		 * Returns the cached address object.
		 * 
		 * @return the address object
		 * @throws RuntimeException if an error occurs during object creation
		 */
		public Address getAddress()
		{
			return (Address)get("address");
		}
		
		/**
		 * Returns the cached principal address object.
		 * 
		 * @return the principal address object
		 * @throws RuntimeException if an error occurs during object creation
		 */
		public PrincipalAddress getPrincipalAddress()
		{
			return (PrincipalAddress)get("principalAddress");
		}
		
		/**
		 * Default email action.
		 * 
		 * @return the hardcoded value: <code>"The email method"</code>
		 */
		public String getEmail()
		{
			return "The email method";
		}
		
	}	// Global
	
	/**
	 * The <code>ApplicationGlobal</code> class is a simple test for an inherited
	 * Global object. It's possible to use the methods of the Global object.
	 * 
	 * @author René Jahn
	 */
	public static class ApplicationGlobal extends Global
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Initializes the applications address by using {@link #getPrincipalAddress()}
		 * from the "super" class.
		 * 
		 * @return the address from the "super" class
		 * @throws RuntimeException if an error occurs during object initialization
		 */
		@SuppressWarnings("unused")
		private Object initApplicationAddress()
		{
			Address adr = new Address();
			
			adr.setAddress(getPrincipalAddress().getAddress());
			
			return adr;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the applications address.
		 * 
		 * @return the application address
		 * @throws RuntimeException if an error occurs during object creation
		 */
		public Address getApplicationAddress()
		{
			return (Address)get("applicationAddress");
		}
		
		/**
		 * Tests recursion call and invokes {@link #getRecursionSecond()}.
		 * 
		 * @return doesn't return anything because it's a recursion
		 */
		public Address getRecursion()
		{
			return (Address)get("recursionSecond");
		}
		
		/**
		 * Tests recursion call and invokes {@link #getRecursion()}.
		 * 
		 * @return doesn't return anything because it's a recursion
		 */
		public Address getRecursionSecond()
		{
			return (Address)get("recursion");
		}
		
	}	// ApplicationGlobal
	
	/**
	 * The <code>Application</code> class is a simple test for a <code>GenericBean</code>
	 * with a <code>Global</code> object as parent.
	 * 
	 * @author René Jahn
	 */
	public static class Application extends GenericBean
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Initializes the applications address by using {@link #getAddress()}
		 * from the parent object.
		 * 
		 * @return the address from the parent object
		 * @throws RuntimeException if an error occurs during object initialization
		 */
		@SuppressWarnings("unused")
		private Object initApplicationAddress()
		{
			Address adr = new Address();
			
			Address adrPrincipal = (Address)get("principalAddress");
			
			if (adrPrincipal != null)
			{
			    adr.setAddress(adrPrincipal.getAddress());
			}
			
			return adr;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the applications address.
		 * 
		 * @return the application address
		 * @throws RuntimeException if an error occurs during object creation
		 */
		public Address getApplicationAddress()
		{
			return (Address)get("applicationAddress");
		}
		
	}	// Application
	
	/**
	 * Simple Screen lifecycle object.
	 * 
	 * @author René Jahn
	 */
	public class SubScreen extends Application
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the topic name.
		 * 
		 * @return "Topic"
		 */
		public String getTopic()
		{
			return "Topic";
		}
	}
	
	/**
	 * Simple bean for an address. The default constructor can be
	 * used.
	 *  
	 * @author René Jahn
	 */
	public static class Address
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the adress. */
		private String sAddress;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Sets the address. 
		 * 
		 * @param pAddress the adress
		 */
		public void setAddress(String pAddress)
		{
			sAddress = pAddress;
		}
		
		/**
		 * Gets the address.
		 * 
		 * @return the address
		 */
		public String getAddress()
		{
			return sAddress;
		}
		
	}	// Address
	
	/**
	 * Simple bean for a principal address. A userdefined
	 * constructor is implemented.
	 * 
	 * @author René Jahn
	 */
	public static class PrincipalAddress extends Address
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>PrincipalAddress</code> with
		 * for a desired address.
		 * 
		 * @param pAddress the principal address
		 */
		public PrincipalAddress(String pAddress)
		{
			setAddress(pAddress);
		}
		
	}	//PrincipalAddress

	/**
	 * The <code>MySession</code> is a simple sessin LCO .
	 * 
	 * @author René Jahn
	 */
	public static class MySession extends GenericBean
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * Gets the master database access.
	     * 
	     * @return the name
	     */
	    public String getMasterDBAccess()
	    {
	        return getClass().getSimpleName() + ".MasterDBAccess";
	    }
	    
        /**
         * Gets the database access.
         * 
         * @return the name
         */
	    public String getDBAccess()
	    {
	        return getClass().getSimpleName() + ".DBAccess";
	    }
	    
	}  // MySession
	
	/**
	 * The <code>SharedScreenLCO</code> is a LCO for shared object access.
	 * 
	 * @author René Jahn
	 */
	public static class SharedScreenLCO extends MySession
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * {@inheritDoc}
	     */
        @Override
        public String getDBAccess()
        {
            return getClass().getSimpleName() + ".DBAccess";
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
        /**
         * Gets the memory info.
         * 
         * @return the info
         */
	    public String getMemory()
	    {
	        return getClass().getSimpleName() + ".getMemory";
	    }
	    
	}  // SharedScreenLCO
	
	/**
	 * The <code>ScreenLCO</code> is a standard LCO for a screen.
	 * 
	 * @author René Jahn
	 */
	public static class ScreenLCO extends SharedScreenLCO
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    /**
	     * Recursive call.
	     * 
	     * @return <code>null</code>
	     */
	    public String getRecursive()
	    {
	        return (String)get("recursive");
	    }
	    
        /**
         * Calls {@link #getMethod2()}.
         * 
         * @return <code>null</code>
         */
	    public String getMethod1()
	    {
	        return (String)get("method2");
	    }

	    /**
	     * calls {@link #getMethod1()}.
	     * 
	     * @return <code>null</code>
	     */
        public String getMethod2()
        {
            return (String)get("method1");
        }
	    
	}  // ScreenLCO

	/**
	 * The <code>ScreenLCO</code> is a standard LCO for a screen.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class TestPutGenericBean extends GenericBean
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The map to store the call count. */
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * The hallo3 property initialization.
		 * 
		 * @return the value.
		 */
		public String initHallo3()
		{
			String result = (String)get("hallo3");
			
			if (result == null)
			{
				result = "Martin3";
				
				put("hallo3", result);
			}
			
			return result;
		}
		
		/**
		 * The hallo4 property initialization.
		 * 
		 * @return the value.
		 */
		public String initHallo4()
		{
			return "Martin4";
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object put(String pPropertyName, Object pValue)
		{
			map.put(pPropertyName, Integer.valueOf(getCallCount(pPropertyName) + 1));
			
			return super.put(pPropertyName, pValue);
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the hallo property.
		 * 
		 * @return <code>Martin</code>
		 */
		public String getHallo()
		{
			String result = (String)get("hallo");
			if (result == null)
			{
				result = "Martin";
				
				put("hallo", result);
			}
			
			return result;
		}
		
		/**
		 * Gets the hallo2 property.
		 * 
		 * @return <code>Martin2</code>.
		 */
		public String getHallo2()
		{
			String result = (String)get("hallo2");
			if (result == null)
			{
				result = "Martin2";
				
				put("hallo2", result);
			}
			
			return result;
		}

		/**
		 * Gets the call count of put.
		 * 
		 * @param pPropertyName the property name.
		 * @return the call count.
		 */
		public int getCallCount(String pPropertyName)
		{
			Integer value = map.get(pPropertyName);
			
			if (value == null)
			{
				return 0;
			}
			else
			{
				return value.intValue();
			}
		}
		
	}	// TestPutGenericBean
	
}	// TestGenericBean
