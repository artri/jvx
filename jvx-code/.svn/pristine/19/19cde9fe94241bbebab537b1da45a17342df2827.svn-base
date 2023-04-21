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
package com.sibvisions.util;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.rad.model.ColumnView;
import javax.rad.model.SortDefinition;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.IDBAccess;
import com.sibvisions.rad.persist.jdbc.OracleDBAccess;
import com.sibvisions.rad.server.annotation.Accessible;
import com.sibvisions.rad.server.annotation.NotAccessible;

/**
 * Tests the {@link Reflective} methods.
 * 
 * @author René Jahn
 * @see Reflective
 */
public class TestReflective
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the {@link Reflective#construct(String, Object[])} method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testConstruct() throws Throwable
	{
		//Defaultkonstruktor
		Demo demo = (Demo)Reflective.construct("com.sibvisions.util.Demo");
		
		Assert.assertEquals("Demo()", demo.getCallQueue());


		//Erstzugriff testen
		demo = (Demo)Reflective.construct("com.sibvisions.util.Demo", new Object[] {"TestName"});
		
		Assert.assertEquals("Demo(java.lang.String)", demo.getCallQueue());

		//Cache testen
		demo = (Demo)Reflective.construct("com.sibvisions.util.Demo", new Object[] {"TestName"});
		
		Assert.assertEquals("Demo(java.lang.String)", demo.getCallQueue());

		//Invalid Konstruktor testen
		try
		{
			demo = (Demo)Reflective.construct("com.sibvisions.util.Demo", new Object[] {new File("")});
			
			Assert.fail("Invalid constructor found!");
		}
		catch (NoSuchMethodException nsme)
		{
			//passt
		}
		
		//Primitive-Type Parameter
		demo = (Demo)Reflective.construct("com.sibvisions.util.Demo", new Object[] {Integer.valueOf(1)});
		
		Assert.assertEquals("Demo(int)", demo.getCallQueue());
	}
	
	/**
	 * Tests the {@link Reflective#call(Object, String, Object[])} method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testCall() throws Throwable
	{
		Demo demo = new Demo();
		
		String sCall;
		
		
		//Der Default-Konstruktor setzt den counter nicht!
		Assert.assertEquals(Integer.valueOf(-1), Reflective.call(demo, "getCount"));		

		//Counter verändern (cache wird durch getCount auch getestet)
		Assert.assertNull("Unexpected result!", Reflective.call(demo, "setCount", new Object[] {Integer.valueOf(10)}));		
		Assert.assertEquals(Integer.valueOf(10), Reflective.call(demo, "getCount"));		
		
		
		sCall = (String)Reflective.call(demo, "getCallQueue");
		
		Assert.assertEquals("Demo()\ngetCount()\nsetCount(int)\ngetCount()", sCall);
		
		//Invalid Konstruktor testen
		try
		{
			Reflective.call(demo, "unknown");
			
			Assert.fail("Unknown method found!");
		}
		catch (NoSuchMethodException nsme)
		{
			//passt
		}
	}
	
	/**
	 * Tests the {@link Reflective#getMetohd(Class, String, Class[])} method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetMethod() throws Throwable
	{
		Assert.assertEquals("public void com.sibvisions.util.Demo.setCount(int)", 
				Reflective.getMethod(Demo.class, "setCount", int.class).toString());

		Assert.assertEquals("public void com.sibvisions.util.Demo.setCount(int)", 
				Reflective.getMethod(Demo.class, "setCount", Integer.class).toString());
		
		// We should explicitely not find a Method with non primitive Parameter by an primitive parameter.
//		Assert.assertEquals("public void com.sibvisions.util.Demo.setCountNonPrimitive(java.lang.Integer)", 
//				Reflective.getMethod(Demo.class, "setCountNonPrimitive", int.class).toString());

		Assert.assertEquals("public void com.sibvisions.util.Demo.setCountNonPrimitive(java.lang.Integer)", 
				Reflective.getMethod(Demo.class, "setCountNonPrimitive", Integer.class).toString());
		
		Assert.assertEquals("public void com.sibvisions.util.Demo.doAction(java.math.BigDecimal)", 
				Reflective.getMethod(Demo.class, "doAction", BigDecimal.class).toString());
	}

	/**
	 * Tests the {@link Reflective#getMetohd(Class, String, Class[])} method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetCompatibleMethod() throws Throwable
	{
		Assert.assertEquals("public void com.sibvisions.util.Demo.doAction(java.math.BigDecimal)", 
				Reflective.getCompatibleMethod(Demo.class, "doAction", Number.class).toString());
	}

	/**
	 * Tests the {@link Reflective#setValue(Object, java.lang.reflect.Field, Object)} method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testSetField() throws Throwable
	{	
		Demo demo = new Demo();
		
		
		Assert.assertEquals(-1, demo.getCount());
		
		Reflective.setValue(demo, "iCount", Integer.valueOf(20));
		
		Assert.assertEquals(20, demo.getCount());
		
		Assert.assertEquals("Demo()\ngetCount()\ngetCount()", demo.getCallQueue());
	}
	
	/**
	 * Tests the {@link Reflective#getValue(Object, java.lang.reflect.Field)} method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testGetField() throws Throwable
	{	
		Demo demo = new Demo();
		
		
		Assert.assertEquals(Integer.valueOf(-1), Reflective.getValue(demo, "iCount"));
		
		demo.setCount(20);
		
		Assert.assertEquals(20, demo.getCount());
		
		Assert.assertEquals("Demo()\nsetCount(int)\ngetCount()", demo.getCallQueue());
	}	
	
	/**
	 * Tests the {@link Reflective#invoke(Object, java.lang.reflect.Method, Object[])} method.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testInvoke() throws Throwable
	{
		Demo demo = new Demo();
		
		Method method = demo.getClass().getDeclaredMethod("validate");

		//protected Methode
		Reflective.invoke(demo, method);
		
		Assert.assertEquals("Demo()\nvalidate()", demo.getCallQueue());
		
		//Public Methode
		method = demo.getClass().getDeclaredMethod("getCount");

		Reflective.invoke(demo, method);
		
		Assert.assertEquals("Demo()\nvalidate()\ngetCount()", demo.getCallQueue());
	}
	
	/**
	 * Tests a method call with Object[] as single parameter.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testObjectArrayCall() throws Throwable
	{
		ArrayCheck ac = new ArrayCheck();
		
		Object[] oArray = new Object[] {"FIRST"};
		
		Reflective.call(ac, "get", (Object[])new Integer[] {Integer.valueOf(1), Integer.valueOf(2)});
		Reflective.call(ac, "get", oArray);
		Reflective.call(ac, "get", "Test", "Test2");
		
		VarargCheck vac = new VarargCheck();
		
		Reflective.call(vac, "get", (Object[])new Integer[] {Integer.valueOf(1), Integer.valueOf(2)});
		Reflective.call(vac, "get", oArray);
		Reflective.call(vac, "get", "Test", "Test2");
	}
	
	/**
	 * Tests a method call with Object[] as single parameter.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testExtendedArrayCall() throws Throwable
	{
		ArrayCheck ac = new ArrayCheck();
		
		Assert.assertNull((Object[])Reflective.call(ac, "get", null));
		Assert.assertArrayEquals(new Object[1], (Object[])(Reflective.call(ac, "get", (Object)null)));
		Assert.assertArrayEquals(new Object[0], (Object[])Reflective.call(ac, "get", new Object[] {}));
//		Assert.assertNull((Object[])Reflective.call(ac, "get")); // It is not detectable if above or this method is called.
									 // possibly override Call with no parameter
		
		VarargCheck vac = new VarargCheck();
		
		Assert.assertArrayEquals(new Object[1], (Object[])Reflective.call(vac, "get", null));
		Assert.assertArrayEquals(new Object[1], (Object[])(Reflective.call(vac, "get", (Object)null)));
		Assert.assertArrayEquals(new Object[0], (Object[])Reflective.call(vac, "get", new Object[] {}));
		Assert.assertArrayEquals(new Object[0], (Object[])Reflective.call(vac, "get"));
	}

	/**
	 * getBytes() and getBytes(String) are called depending on the order of getMethods in the jvm.
	 * We should think about it, what would be best for calling with Object.
	 * 
	 * @throws Throwable if it fails
	 */
//	@Test
//	public void testString() throws Throwable
//	{
//		System.out.println(new String((byte[])Reflective.call("Über", "getBytes", "UTF-8")));
//		
//		toWas()
//		
//		call("toWas");
//		call("toWas", null); -> error
//
//		toWas2(String pP)
//		
//		call("toWas2"); -> error
//		call("toWas2", null);
//		call("toWas2", getObject()); 
//		call("toWas2", getObjectArray()); 
//		call("toWas2", "irgendwas"); 
//	}

	
	/**
	 * Tests a method call with Object[] as single parameter.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testExtended42ArrayCall() throws Throwable
	{
		ArrayCheck ac = new ArrayCheck();
		
		Assert.assertArrayEquals(new Object[0], (Object[])Reflective.call(ac, "getMore", "Hallo", new Object[] {}));
		Assert.assertNull((Object[])Reflective.call(ac, "getMore", "Hallo", null));
		try
		{
			Reflective.call(ac, "getMore", "Hallo");
			
			Assert.fail("Method not found should occur!");
		}
		catch (Exception ex)
		{
			// Exception is ok
		}

		VarargCheck vac = new VarargCheck();
		
		Assert.assertArrayEquals(new Object[0], (Object[])Reflective.call(vac, "getMore", "Hallo", new Object[] {}));
		Assert.assertArrayEquals(new Object[1], (Object[])Reflective.call(vac, "getMore", "Hallo", null));
		Assert.assertArrayEquals(new Object[0], (Object[])Reflective.call(vac, "getMore", "Hallo"));
	}
	
	/**
	 * Tests object instantiation with Object[] as single parameter.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testObjectArrayConstruct() throws Throwable
	{
		Object[] oArray = new Object[] {"FIRST"};

		Reflective.construct(VarargCheck.class, oArray);
		Reflective.construct(ArrayCheck.class, oArray);
		
		Reflective.construct("com.sibvisions.util.VarargCheck", "Test", "Test2");
		Reflective.construct("com.sibvisions.util.VarargCheck", new Object[] {"TestName"});
		
		Reflective.construct("com.sibvisions.util.ArrayCheck", "Test", "Test2");
		Reflective.construct("com.sibvisions.util.ArrayCheck", new Object[] {"TestName"});
	}

	/**
	 * Tests VarArgs as Parameter.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testVarArgs() throws Throwable
	{
		Reflective.construct(SortDefinition.class, (Object[])new String[] {"VORNAME", "NACHNAME"});
		
		Reflective.construct(SortDefinition.class, (Object)new String[] {"VORNAME", "NACHNAME"});
		
		Reflective.construct(ColumnView.class, null, "NACHNAME");
		
		Object columnView = Reflective.construct(ColumnView.class, "VORNAME", "NACHNAME");
		
		Reflective.call(columnView, "addColumnNames", "ADRESSE", "PLZ", "ORT");
		
		Object columnNames = Reflective.call(columnView, "getColumnNames");
		
		Assert.assertArrayEquals(new String[] {"VORNAME", "NACHNAME", "ADRESSE", "PLZ", "ORT"}, (String[])columnNames);
	}

	/**
	 * Tests {@link Reflective#getMethodsByReturnValue(Class, Class, boolean)}.
	 */
	@Test
	public void testGetMethodsByReturnValue()
	{
	    Object oLco = new LifeCycleObject();
	    
	    Method[] met = Reflective.getMethodsByReturnValue(oLco.getClass(), DBAccess.class, true);
	    
	    ArrayUtil<String> auMethods = new ArrayUtil<String>();
	    
	    for (int i = 0; i < met.length; i++)
	    {
	        auMethods.add(met[i].getName());
	    }
	    
	    Assert.assertEquals(2, auMethods.size());
	    Assert.assertTrue(ArrayUtil.containsAll(auMethods.toArray(new String[auMethods.size()]), new String[] {"getDBAccess", "getOracleDBAccess"}));
	}
	
	/**
	 * Tests {@link Reflective#isParamTypeListValid(Class[], Class[], boolean)}.
	 */
	@Test
	public void testIsParamTypeListValid()
	{
		// Test empty lists first
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] {}, new Class[] {}, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] {}, new Class[] {}, true));
		
		// Test non matching.
		Assert.assertFalse(Reflective.isParamTypeListValid(new Class<?>[] { }, new Class[] { Object.class }, false));
		Assert.assertFalse(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { }, false));
		
		// Test if an object is an object.
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { Object.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { Object.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { Object.class, Object.class, Object.class }, true));
		
		// Test assignment to a different type.
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { String.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { Date.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { String.class, Date.class, String.class }, true));
		
		// Automatic conversion to varargs because of Object[].
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { String.class, Date.class, String.class }, false));
				
		// Multiple parameters are not an array.
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { String[].class }, new Class[] { String.class, String.class, String.class }, false));
		
		// Test primitive classes.
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { boolean.class }, new Class[] { boolean.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { boolean[].class }, new Class[] { boolean[].class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { boolean[].class }, new Class[] { boolean.class, boolean.class, boolean.class }, true));
		
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { boolean.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { byte.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { char.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { short.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { int.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { long.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { float.class }, false));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object.class }, new Class[] { double.class }, false));

		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { boolean.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { byte.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { char.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { short.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { int.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { long.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { float.class }, true));
		Assert.assertTrue(Reflective.isParamTypeListValid(new Class<?>[] { Object[].class }, new Class[] { double.class }, true));
	}
	
	/**
	 * Tests {@link Reflective#getAnnotation(Method, Class)}.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testGetAnnotation() throws Exception
	{
		List<Accessible> liAcc = Reflective.getAnnotation(LifeCycleObject.class.getMethod("access"), Accessible.class);
		
		Assert.assertEquals(1, liAcc.size());
		
		List<NotAccessible> liNotAcc = Reflective.getAnnotation(LifeCycleObject.class.getMethod("access"), NotAccessible.class);

		Assert.assertEquals(0, liNotAcc.size());
	}
	
} 	// TestReflective

/**
 * The <code>LifeCycleObject</code> is a simple LCO.
 * 
 * @author René Jahn
 */
class LifeCycleObject
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Simple get method.
     * 
     * @return null
     */
    public DBAccess getDBAccess()
    {
        return null;
    }
    
    /**
     * Simple get method.
     * 
     * @return null
     */
    public IDBAccess getIDBAccess()
    {
        return null;
    }
    
    /**
     * Simple get method.
     * 
     * @return null
     */
    public OracleDBAccess getOracleDBAccess()
    {
        return null;
    }
    
    /**
     * Simple get method.
     * 
     * @param pValid will be ignored
     * @return null
     */
    public DBAccess getDBAccessWithParam(boolean pValid)
    {
        return null;
    }
    
    /**
     * Simple get method.
     * 
     * @return null
     */
    public String getValue()
    {
        return null;
    }
    
    /**
     * Simple get method.
     *
     * @param pParam will be ignored
     * @return null
     */
    public String getValue(String pParam)
    {
        return null;
    }
    
    /**
     * Simple action method.
     */
    public void doCheck()
    {
    }
    
    /**
     * Simple action method.
     * 
     * @param pForce will be ignored
     */
    public void doCheck(boolean pForce)
    {
    }

    /**
     * Annotated method.
     */
    @Accessible(environment = "WEB:rest,DESKTOP")
    public void access()
    {
    }
    
}   // LifeCycleObject

/**
 * The <code>Demo</code> class has different constructors and methods for testing
 * the Reflective class.
 * 
 * @author René Jahn
 */
class Demo
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** simple list of constructor and method calls. */
	private StringBuilder sbCallQueue = new StringBuilder();
	
	/** the object name. */
	private String sName = null;
	
	/** the internal object counter. */
	private int iCount = -1;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Simple default constructor.
	 */
	public Demo()
	{
		addCall("Demo()");
	}
	
	/**
	 * Constructor with string as parameter.
	 * 
	 * @param pName any string
	 */
	public Demo(String pName)
	{
		this.sName = pName;
		
		addCall("Demo(java.lang.String)");
	}
	
	/**
	 * Constructor with a primitive type parameter.
	 * 
	 * @param pCount any number
	 */
	public Demo(int pCount)
	{
		this.iCount = pCount;
		
		addCall("Demo(int)");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a method or constructor call to the queue.
	 * 
	 * @param pCall the call
	 */
	private void addCall(String pCall)
	{
		if (sbCallQueue.length() > 0)
		{
			sbCallQueue.append("\n");
		}
		
		sbCallQueue.append(pCall);
	}

	/**
	 * Returns the current call queue.
	 * 
	 * @return the call identifiers
	 */
	public String getCallQueue()
	{
		return sbCallQueue.toString();
	}

	/**
	 * Gets the object name.
	 * 
	 * @return the object name
	 */
	public String getName()
	{
		return sName;
	}
	
	/**
	 * Sets the counter to a specified value.
	 * 
	 * @param pCount the new counter value
	 */
	public void setCount(int pCount)
	{
		this.iCount = pCount;
		
		addCall("setCount(int)");
	}
	
	/**
	 * Sets the counter to a specified value.
	 * 
	 * @param pCount the new counter value
	 */
	public void setCountNonPrimitive(Integer pCount)
	{
		if (pCount == null)
		{
			this.iCount = 0;
		}
		else
		{
			this.iCount = pCount.intValue();
		}
		
		addCall("setCountNonPrimitive(Integer)");
	}
	
	/**
	 * Gets the current value of the counter.
	 * 
	 * @return the counter value
	 */
	public int getCount()
	{
		addCall("getCount()");
		
		return iCount;
	}
	
	/**
	 * Action with number parameter.
	 * @param pIdentifier a test identifier
	 */
	public void doAction(BigDecimal pIdentifier)
	{
		addCall("doAction(Number)");
	}
	
	/**
	 * A simple protected method.
	 */
	protected void validate()
	{
		addCall("validate()");
	}
	
}	// Demo

/**
 * The <code>ArrayCheck</code> will be used for testing array handling instead of varargs.
 * 
 * @author René Jahn
 */
class ArrayCheck
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ArrayCheck</code>.
	 */
	public ArrayCheck()
	{
	}

	/**
	 * Simple constructor with an object array as parameter.
	 * 
	 * @param pArray an object array
	 */
	public ArrayCheck(Object[] pArray)
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Simple method call with an object array as parameter.
	 * 
	 * @param pArray an object array
	 * @return the <code>pArray</code>
	 */
	public Object[] get(Object[] pArray)
	{
		return pArray;
	}
	
	/**
	 * Simple method call with an object array as parameter.
	 * 
	 * @param pFirst the first parameter
	 * @param pArray an object array
	 * @return the <code>pArray</code>
	 */
	public Object[] getMore(String pFirst, Object[] pArray)
	{
		return pArray;
	}
	
}	// ArrayCheck

/**
 * The <code>VarargCheck</code> will be used for testing vararg handling instead of object arrays.
 * 
 * @author René Jahn
 */
class VarargCheck
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VarargCheck</code>.
	 */
	public VarargCheck()
	{
	}
	
	/**
	 * Simple constructor with vararg.
	 * 
	 * @param pArray a vararg
	 */
	public VarargCheck(Object... pArray)
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Simple method call with an vararg as parameter.
	 * 
	 * @param pArray a vararg
	 * @return the <code>pArray</code>
	 */
	public Object[] get(Object... pArray)
	{
		return pArray;
	}
	
	/**
	 * Simple method call with an object array as parameter.
	 * 
	 * @param pFirst the first parameter
	 * @param pArray an object array
	 * @return the <code>pArray</code>
	 */
	public Object[] getMore(String pFirst, Object... pArray)
	{
		return pArray;
	}
	
}	// TestReflective
