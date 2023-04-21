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
 * 09.04.2009 - [HM] - creation
 * 15.07.2009 - [JR] - testSetInvalidProperty implemented
 * 06.03.2010 - [JR] - #71: merged test cases
 */
package javax.rad.type.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.persist.AbstractStorage.ChangeableBean;
import com.sibvisions.util.type.BeanUtil;

/**
 * Tests the {@link Bean} and {@link BeanType} methods.
 * 
 * @author Martin Handsteiner
 * @see Bean
 * @see BeanType
 */
public class TestBeans
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests {@link Bean} with POJO.
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testPojoBeans()
	{
		BeanType beanType = BeanType.getBeanType(SimplePOJO.class);

		Bean bean = new Bean(beanType);

		Assert.assertNull(bean.get("id"));
		Assert.assertNull(bean.get("name"));
		
		bean.put(0, "5");

		bean.put("name", "Hallo");
		
		Assert.assertEquals(new BigDecimal(5),	bean.get("id"));
		Assert.assertEquals("Hallo",			bean.get("name"));

		bean.put("adresse", "Hausfeldstrasse 47");
	}

	/**
	 * Tests dynamic {@link Bean} properties.
	 */
	@Test
	public void testDynamicAdd()
	{
		Bean bean = new Bean();
		
		bean.put("Martin", "Handsteiner");
		bean.put("René", "Jahn");
		bean.put("Roland", "Hörmann");
		
		Assert.assertEquals("Handsteiner", 	bean.get("Martin"));
		Assert.assertEquals("Jahn", 		bean.get("René"));
		Assert.assertEquals("Hörmann", 		bean.get("Roland"));
	}

	/**
     * Tests equals and hashCode.
     */
    @Test
    public void testEqualsAndHashCode()
    {
        Bean bean = new Bean();
        bean.put("id", BigDecimal.valueOf(14));
        bean.put("name", "Wert");
        bean.put("desc", null);

        Assert.assertEquals(bean.hashCode(), bean.hashCode());
        Assert.assertEquals(bean, bean);
        
        Bean clone = bean.clone();

        Assert.assertEquals(bean.hashCode(), clone.hashCode());
        Assert.assertEquals(bean, clone);
    }
	
	/**
	 * Tests {@link Bean#put(String, Object)} and {@link Bean#get(String)}.
	 */
	@Test
	public void testGetAndSet()
	{
		Bean bean = new Bean();
		
		bean.put("id", BigDecimal.valueOf(14));
		bean.put("name", "Wert");
		
		Assert.assertEquals("Wert", bean.get("name"));
		Assert.assertEquals(BigDecimal.valueOf(14), bean.get("id"));
		Assert.assertArrayEquals(bean.getBeanType().getPropertyNames(), new String[] {"id", "name"});
		
		BeanType bt2 = new BeanType(new String[] {"id", "name", "orders"});
		Bean bean2 = new Bean(bt2);
		
		Assert.assertTrue(bean2.get("name") == null);
		Assert.assertTrue(bean2.get("id") == null);
		Assert.assertArrayEquals(bean2.getBeanType().getPropertyNames(), new String[] {"id", "name", "orders"});
		
		SimplePOJO tp = new SimplePOJO();
		tp.setId(BigDecimal.valueOf(15));
		tp.setName("Hallo");
		
		bean2.put("orders", new Object[] {bean, tp});
		
		Assert.assertTrue(BeanUtil.get(bean2, "orders[0].name") == "Wert");
		Assert.assertTrue(BeanUtil.get(bean2, "orders[1].name") == "Hallo");
		
		Bean bean3 = new Bean();
		
		bean3.put("id", BigDecimal.valueOf(15));
		bean3.put("name", "Martin");
		
		BeanUtil.set(bean2, "orders[2]", bean3);
		
		Assert.assertTrue(((Object[])bean2.get("orders")).length == 3);
	}

	/**
	 * Tests {@link BeanType#getPropertyNames()}.
	 */
	@Test
	public void testGetPropertyNames()
	{
		SimplePOJO tp = new SimplePOJO();
		tp.setId(BigDecimal.valueOf(15));
		tp.setName("test");
		
		String[] names = BeanType.getBeanType(tp).getPropertyNames();
		
		Assert.assertArrayEquals(new String[] {"id", "name"}, names);
	}
	
	/**
	 * Tests Bean, Map get performance difference.
	 */
	@Test
	public void testPerformanceRead()
	{
		int amount = 2000000;
		
		long start;
		
		ArrayList<Object> result = new ArrayList<Object>(amount * 7);

		result.clear();
		
		Hashtable<String, Object> bean1 = new Hashtable<String, Object>();
		bean1.put("id", new BigDecimal(1));
		bean1.put("vorname", "Martin");
		bean1.put("nachname", "Handsteiner");
		bean1.put("Adresse", "Musterstrasse 1");
		bean1.put("PLZ", "2222");
		bean1.put("Ort", "Wien");

		start = System.currentTimeMillis();
		for (int i = 0; i < amount; i++)
		{
			result.add(bean1.get("id"));
			result.add(bean1.get("vorname"));
			result.add(bean1.get("nachname"));
			result.add(bean1.get("Adresse"));
			result.add(bean1.get("PLZ"));
			result.add(bean1.get("Ort"));
		}
		System.out.println("Hashtable get: " + (System.currentTimeMillis() - start));
		
		result.clear();
		
		HashMap<String, Object> bean2 = new HashMap<String, Object>();
		bean2.put("id", new BigDecimal(1));
		bean2.put("vorname", "Martin");
		bean2.put("nachname", "Handsteiner");
		bean2.put("Adresse", "Musterstrasse 1");
		bean2.put("PLZ", "2222");
		bean2.put("Ort", "Wien");

		start = System.currentTimeMillis();
		for (int i = 0; i < amount; i++)
		{
			result.add(bean2.get("id"));
			result.add(bean2.get("vorname"));
			result.add(bean2.get("nachname"));
			result.add(bean2.get("Adresse"));
			result.add(bean2.get("PLZ"));
			result.add(bean2.get("Ort"));
		}
		System.out.println("HashMap get: " + (System.currentTimeMillis() - start));
		
		result.clear();
		
		Bean bean3 = new Bean();
		bean3.put("id", new BigDecimal(1));
		bean3.put("vorname", "Martin");
		bean3.put("nachname", "Handsteiner");
		bean3.put("Adresse", "Musterstrasse 1");
		bean3.put("PLZ", "2222");
		bean3.put("Ort", "Wien");
		
		start = System.currentTimeMillis();
		for (int i = 0; i < amount; i++)
		{
			result.add(bean3.get("id"));
			result.add(bean3.get("vorname"));
			result.add(bean3.get("nachname"));
			result.add(bean3.get("Adresse"));
			result.add(bean3.get("PLZ"));
			result.add(bean3.get("Ort"));
		}
		System.out.println("Bean get: " + (System.currentTimeMillis() - start));
	}

	/**
	 * Tests Bean, Map create performance difference.
	 */
	@Test
	public void testPerformanceCreate()
	{
		int amount = 200000;
		
		long start;
		
		ArrayList<Object> result = new ArrayList<Object>(amount * 10);

		result.clear();
		
		start = System.currentTimeMillis();
		for (int i = 0; i < amount; i++)
		{
			Hashtable<String, Object> bean1 = new Hashtable<String, Object>();
			bean1.put("id", Integer.valueOf(i));
			bean1.put("vorname", "Martin");
			bean1.put("nachname", "Handsteiner");
			bean1.put("Adresse", "Musterstrasse 1");
			bean1.put("PLZ", "2222");
			bean1.put("Ort", "Wien");

			result.add(bean1);
		}
		System.out.println("Hashtable create: " + (System.currentTimeMillis() - start));
		
		result.clear();
		
		start = System.currentTimeMillis();
		for (int i = 0; i < amount; i++)
		{
			HashMap<String, Object> bean2 = new HashMap<String, Object>();
			bean2.put("id", Integer.valueOf(i));
			bean2.put("vorname", "Martin");
			bean2.put("nachname", "Handsteiner");
			bean2.put("Adresse", "Musterstrasse 1");
			bean2.put("PLZ", "2222");
			bean2.put("Ort", "Wien");

			result.add(bean2);
		}
		System.out.println("HashMap create: " + (System.currentTimeMillis() - start));
		
		result.clear();
		
		start = System.currentTimeMillis();
		for (int i = 0; i < amount; i++)
		{
			Bean bean3 = new Bean();
			bean3.put("id", Integer.valueOf(i));
			bean3.put("vorname", "Martin");
			bean3.put("nachname", "Handsteiner");
			bean3.put("Adresse", "Musterstrasse 1");
			bean3.put("PLZ", "2222");
			bean3.put("Ort", "Wien");

			result.add(bean3);
		}
		System.out.println("Bean create dynamic: " + (System.currentTimeMillis() - start));
		
		result.clear();
		
		BeanType btProperties = new BeanType(new String[] {"id", "vorname", "nachname", "Adresse", "PLZ", "Ort"});
		
		start = System.currentTimeMillis();
		for (int i = 0; i < amount; i++)
		{
			Bean bean3 = new Bean(btProperties);
			bean3.put("id", Integer.valueOf(i));
			bean3.put("vorname", "Martin");
			bean3.put("nachname", "Handsteiner");
			bean3.put("Adresse", "Musterstrasse 1");
			bean3.put("PLZ", "2222");
			bean3.put("Ort", "Wien");

			result.add(bean3);
		}
		System.out.println("Bean create fixed: " + (System.currentTimeMillis() - start));
	}
	
	/**
	 * Tests to set a property for a POJO which does not exist. 
	 */
	@Test (expected = UnsupportedOperationException.class)
	public void testSetInvalidProperty()
	{
		SimplePOJO tp = new SimplePOJO();
		
		Bean bean = new Bean(BeanType.getBeanType(tp));
		
		try
		{
			bean.put("id", "1");
			bean.put("name", "R");
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			Assert.fail("Invalid property set: " + e.getMessage());
		}
		
		bean.put("name_", "Exception");
	}	
	
	/**
	 * Tests the clone method of the {@link Bean}.
	 * 
	 * @throws CloneNotSupportedException if the test fails.
	 */
	@Test
	public void testClone() throws CloneNotSupportedException
	{
		Bean simpleBean = new Bean();
		simpleBean.put("propA", "valueA");
		simpleBean.put("propB", "valueB");
		simpleBean.put("propC", "valueC");
		
		assertClone(simpleBean);
		
		SimplePOJO object = new SimplePOJO();
		object.setId(BigDecimal.valueOf(5));
		object.setName("object");
		
		Bean objectBean = new Bean(object);
		
		assertClone(objectBean);
	}
	
	/**
	 * Tests that the {@link IBean#toString()} method returns the correct
	 * result after the {@link IBean} has been cloned.
	 */
	@Test
	public void testToStringAfterCloneTicket1502()
	{
		BeanType beanType = BeanType.getBeanType(SimplePOJO.class);
		IBean bean = new Bean(beanType);
		
		bean.put("name", "My Name");
		
		Assert.assertEquals("{id=null, name=My Name}", bean.toString());
		
		bean = (IBean)bean.clone();
		
		bean.put("id", BigDecimal.ONE);
		bean.put("name", "Other Name");
		
		Assert.assertEquals("{id=1, name=Other Name}", bean.toString());
	}
	
	/**
	 * Tests a fixed bean.
	 */
	@Test
	public void testFixedBean() throws Exception
	{
	    BeanType btProp = new BeanType("myclass", new String[] {"id", "vorname", "nachname"});
	    
	    Bean bProp = new Bean(btProp);
	    
	    try
	    {
	        bProp.put("x", "y");
	        
	        Assert.fail("Fixed beans may not be extended!");
	    }
	    catch (Exception ex)
	    {
	        // ok
	    }
	    
	    BeanType btClass = new BeanType(ExtendedBean.class.getName(), new String[] {"id", "vorname", "nachname"});
	    Bean bClass = (Bean)btClass.newInstance();

	    // Class is restored as correct class, because it has a matching constructor
        Assert.assertEquals("javax.rad.type.bean.TestBeans$ExtendedBean", bClass.getBeanType().getClassName());
        Assert.assertEquals(ExtendedBean.class, bClass.getBeanType().getTypeClass());
        Assert.assertEquals(ExtendedBean.class, bClass.getClass());
	    
	    bClass.put("x", "y"); // Still extendable
	    
	    BeanType btChange = new BeanType(ChangeableBean.class.getName(), new String[] {"id", "vorname", "nachname"});
        Bean bChange = (Bean)btChange.newInstance();

        // Class is restored as Bean, but contains the correct original class name.
        Assert.assertEquals("com.sibvisions.rad.persist.AbstractStorage$ChangeableBean", bChange.getBeanType().getClassName());
        Assert.assertEquals(Bean.class, bChange.getBeanType().getTypeClass());
        Assert.assertEquals(Bean.class, bChange.getClass());
	}
	
	/**
	 * Asserts that a clone of the given {@link Bean} does have the same
	 * properties as the original one, but not the same core object.
	 * 
	 * @param pBean the {@link Bean} to clone and test.
	 * @throws CloneNotSupportedException if the cloning is not supported.
	 */
	private void assertClone(Bean pBean) throws CloneNotSupportedException
	{
		Bean clone = (Bean) pBean.clone();
		
        Assert.assertEquals(pBean, clone);
		
		for (String propertyName : pBean.keySet())
		{
			Assert.assertEquals(pBean.get(propertyName), clone.get(propertyName));
		}
		
		if (pBean.getObject() != null && clone.getObject() != null)
		{
			Assert.assertNotSame(pBean.getObject(), clone.getObject());
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * A POJO.
	 * 
	 * @author Martin Handsteiner
	 */
	public static class SimplePOJO
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** The id. */
		private BigDecimal id;
		
		/** The name. */
		private String name;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Gets the id.
		 * @return the id.
		 */
    	public BigDecimal getId()
    	{
    		return id;
    	}
    	
    	/**
    	 * Sets the id.
    	 * @param pId the id.
    	 */
    	public void setId(BigDecimal pId)
    	{
    		id = pId;
    	}
    	
		/**
		 * Gets the name.
		 * @return the name.
		 */
    	public String getName()
    	{
    		return name;
    	}
    	
    	/**
    	 * Sets the name.
    	 * @param pName the name.
    	 */
    	public void setName(String pName)
    	{
    		name = pName;
    	}

	}	// SimplePOJO

	/**
     * A POJO.
     * 
     * @author Martin Handsteiner
     */
    public static class ExtendedBean extends Bean
    {
        /**
         * Forbidden constructor.
         */
        private ExtendedBean()
        {
        }
        
        /**
         * Allowed constructor.
         * 
         * @param pBeanType the bean type
         */
        public ExtendedBean(BeanType pBeanType)
        {
            super(pBeanType);
        }
        
    }

}	// TestBeans
