/*
 * Copyright 2014 SIB Visions GmbH
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
 * 21.11.2014 - [RZ] - creation
 */
package com.sibvisions.rad.persist.bean;

import java.math.BigDecimal;
import java.util.Date;

import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;
import javax.rad.type.bean.IBean;
import javax.rad.type.bean.IBeanType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link BeanConverter}.
 * 
 * @author Robert Zenz
 */
public class TestBeanConverter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link BeanConverter} instance used for testing. */
	private BeanConverter beanConverter;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Initializes the {@link BeanConverter} instance used for testing.
	 */
	@Before
	public void setUp()
	{
		beanConverter = new BeanConverter(new BeanType(new String[] { "ID", "NAME", "NOTE", "CREATION" }));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests the {@link BeanConverter#createArray(Object)} method with a
	 * {@link Bean} as input.
	 */
	@Test
	public void testCreateArrayFromBean()
	{
		IBean bean = new Bean();
		bean.put("ID", BigDecimal.valueOf(1));
		bean.put("NAME", "Name");
		bean.put("NOTE", "Something");
		bean.put("CREATION", new Date());
		
		Object[] array = beanConverter.createArray(bean);
		
		Assert.assertNotNull(array);
		
		Assert.assertEquals(BigDecimal.valueOf(1), array[0]);
		Assert.assertEquals("Name", array[1]);
		Assert.assertEquals("Something", array[2]);
		Assert.assertNotNull(array[3]);
	}
	
	/**
	 * Tests the {@link BeanConverter#createArray(Object)} method with a
	 * {@code null} as input.
	 */
	@Test
	public void testCreateArrayFromNull()
	{
		Object[] array = beanConverter.createArray(null);
		
		Assert.assertNotNull(array);
		
		Assert.assertNull(array[0]);
		Assert.assertNull(array[1]);
		Assert.assertNull(array[2]);
		Assert.assertNull(array[3]);
	}
	
	/**
	 * Tests the {@link BeanConverter#createArray(Object)} method with an
	 * {@link Object} as input.
	 */
	@Test
	public void testCreateArrayFromObject()
	{
		Object[] array = beanConverter.createArray(new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date()));
		
		Assert.assertNotNull(array);
		
		Assert.assertEquals(BigDecimal.valueOf(1), array[0]);
		Assert.assertEquals("Name", array[1]);
		Assert.assertEquals("Something", array[2]);
		Assert.assertNotNull(array[3]);
	}
	
	/**
	 * Tests the {@link BeanConverter#createBean(Object[])} method.
	 */
	@Test
	public void testCreateBeanFromArray()
	{
		IBean bean = beanConverter.createBean(new Object[] { BigDecimal.valueOf(1), "Name", "Something", new Date() });
		
		Assert.assertNotNull(bean);
		
		Assert.assertEquals(BigDecimal.valueOf(1), bean.get("ID"));
		Assert.assertEquals("Name", bean.get("NAME"));
		Assert.assertEquals("Something", bean.get("NOTE"));
		Assert.assertNotNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#createBean(Object[])} method with an empty
	 * array as input.
	 */
	@Test
	public void testCreateBeanFromEmptyArray()
	{
		IBean bean = beanConverter.createBean(new Object[] {});
		
		Assert.assertNotNull(bean);
		
		Assert.assertNull(bean.get("ID"));
		Assert.assertNull(bean.get("NAME"));
		Assert.assertNull(bean.get("NOTE"));
		Assert.assertNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#createBean(Object[])} method with
	 * {@code null} as input.
	 */
	@Test
	public void testCreateBeanFromNull()
	{
		IBean bean = beanConverter.createBean(null);
		
		Assert.assertNotNull(bean);
		
		Assert.assertNull(bean.get("ID"));
		Assert.assertNull(bean.get("NAME"));
		Assert.assertNull(bean.get("NOTE"));
		Assert.assertNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#createBean(Object[])} method with a
	 * partial array as input.
	 */
	@Test
	public void testCreateBeanFromPartialArray()
	{
		IBean bean = beanConverter.createBean(new Object[] { BigDecimal.valueOf(1), "Name" });
		
		Assert.assertNotNull(bean);
		
		Assert.assertEquals(BigDecimal.valueOf(1), bean.get("ID"));
		Assert.assertEquals("Name", bean.get("NAME"));
		Assert.assertNull(bean.get("NOTE"));
		Assert.assertNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#createBean(Object) method.
	 */
	@Test
	public void testCreateBeanFromPOJO()
	{
		IBean bean = beanConverter.createBean(new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date()));
		
		Assert.assertNotNull(bean);
		
		Assert.assertEquals(BigDecimal.valueOf(1), bean.get("ID"));
		Assert.assertEquals("Name", bean.get("NAME"));
		Assert.assertEquals("Something", bean.get("NOTE"));
		Assert.assertNotNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#createEmptyBean()} method.
	 */
	@Test
	public void testCreateEmptyBean()
	{
		IBean bean = beanConverter.createEmptyBean();
		
		Assert.assertNotNull(bean);
		
		Assert.assertNull(bean.get("ID"));
		Assert.assertNull(bean.get("NAME"));
		Assert.assertNull(bean.get("NOTE"));
		Assert.assertNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#createPOJO(Class, Object[])} method.
	 */
	@Test
	public void testCreatePOJOFromArray()
	{
		SimplePOJO pojo = beanConverter.createPOJO(SimplePOJO.class, new Object[] { BigDecimal.valueOf(1), "Name", "Something", new Date() });
		
		Assert.assertNotNull(pojo);
		
		Assert.assertEquals(BigDecimal.valueOf(1), pojo.getId());
		Assert.assertEquals("Name", pojo.getName());
		Assert.assertEquals("Something", pojo.getNote());
		Assert.assertNotNull(pojo.getCreation());
	}
	
	/**
	 * Tests the {@link BeanConverter#createPOJO(Class, IBean)} method.
	 */
	@Test
	public void testCreatePOJOFromBean()
	{
		IBean bean = new Bean();
		bean.put("ID", BigDecimal.valueOf(1));
		bean.put("NAME", "Name");
		bean.put("NOTE", "Something");
		bean.put("CREATION", new Date());
		
		SimplePOJO pojo = beanConverter.createPOJO(SimplePOJO.class, bean);
		
		Assert.assertNotNull(pojo);
		
		Assert.assertEquals(BigDecimal.valueOf(1), pojo.getId());
		Assert.assertEquals("Name", pojo.getName());
		Assert.assertEquals("Something", pojo.getNote());
		Assert.assertNotNull(pojo.getCreation());
	}
	
	/**
	 * Tests the {@link BeanConverter#createPOJO(Class, Object)} method.
	 */
	@Test
	public void testCreatePOJOFromPOJOJ()
	{
		SimplePOJO pojo = beanConverter.createPOJO(SimplePOJO.class, new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date()));
		
		Assert.assertNotNull(pojo);
		
		Assert.assertEquals(BigDecimal.valueOf(1), pojo.getId());
		Assert.assertEquals("Name", pojo.getName());
		Assert.assertEquals("Something", pojo.getNote());
		Assert.assertNotNull(pojo.getCreation());
	}
	
	/**
	 * Tests the {@link BeanConverter#createPOJO(Class, Object[])} method with
	 * an empty array as input.
	 */
	@Test
	public void testCreatePOJOFromEmptyArray()
	{
		SimplePOJO pojo = beanConverter.createPOJO(SimplePOJO.class, new Object[] {});
		
		Assert.assertNull(pojo);
	}
	
	/**
	 * Tests the {@link BeanConverter#createPOJO(Class, Object[])} method with
	 * an partial array as input.
	 */
	@Test
	public void testCreatePOJOFromPartialArray()
	{
		SimplePOJO pojo = beanConverter.createPOJO(SimplePOJO.class, new Object[] { BigDecimal.valueOf(1), "Name" });
		
		Assert.assertNotNull(pojo);
		
		Assert.assertEquals(BigDecimal.valueOf(1), pojo.getId());
		Assert.assertEquals("Name", pojo.getName());
		Assert.assertNull(pojo.getNote());
		Assert.assertNull(pojo.getCreation());
	}
	
	/**
	 * Tests the {@link BeanConverter#getBeanType()} method and the returned
	 * {@link BeanType}.
	 */
	@Test
	public void testGetBeanType()
	{
		IBeanType beanType = beanConverter.getBeanType();
		
		Assert.assertNotNull(beanType);
		
		Assert.assertEquals("ID", beanType.getPropertyNames()[0]);
		Assert.assertEquals("NAME", beanType.getPropertyNames()[1]);
		Assert.assertEquals("NOTE", beanType.getPropertyNames()[2]);
		Assert.assertEquals("CREATION", beanType.getPropertyNames()[3]);
	}
	
	/**
	 * Tests the {@link BeanConverter#isInitialized()} method.
	 */
	@Test
	public void testIsInitialized()
	{
		Assert.assertTrue(beanConverter.isInitialized());
	}
	
	/**
	 * Test the {@link BeanConverter#updateArray(Object[], IBean)} method.
	 */
	@Test
	public void testUpdateArrayFromBean()
	{
		Object[] array = new Object[] { BigDecimal.valueOf(2), "No Name", "Nothing", new Date() };
		
		IBean bean = new Bean();
		bean.put("ID", BigDecimal.valueOf(1));
		bean.put("NAME", "Name");
		bean.put("NOTE", "Something");
		bean.put("CREATION", new Date());
		
		beanConverter.updateArray(array, bean);
		
		Assert.assertEquals(BigDecimal.valueOf(1), array[0]);
		Assert.assertEquals("Name", array[1]);
		Assert.assertEquals("Something", array[2]);
		Assert.assertNotNull(array[3]);
	}
	
	/**
	 * Test the {@link BeanConverter#updateArray(Object[], Object)} method.
	 */
	@Test
	public void testUpdateArrayFromPOJO()
	{
		Object[] array = new Object[] { BigDecimal.valueOf(2), "No Name", "Nothing", new Date() };
		
		beanConverter.updateArray(array, new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date()));
		
		Assert.assertEquals(BigDecimal.valueOf(1), array[0]);
		Assert.assertEquals("Name", array[1]);
		Assert.assertEquals("Something", array[2]);
		Assert.assertNotNull(array[3]);
	}
	
	/**
	 * Tests the {@link BeanConverter#updateBean(IBean, Object[])} method.
	 */
	@Test
	public void testUpdateBeanFromArray()
	{
		IBean bean = beanConverter.createEmptyBean();
		bean.put("ID", BigDecimal.valueOf(2));
		bean.put("NAME", "No Name");
		bean.put("NOTE", "Nothging important");
		bean.put("CREATION", new Date());
		
		beanConverter.updateBean(bean, new Object[] { BigDecimal.valueOf(1), "Name", "Something", new Date() });
		
		Assert.assertEquals(BigDecimal.valueOf(1), bean.get("ID"));
		Assert.assertEquals("Name", bean.get("NAME"));
		Assert.assertEquals("Something", bean.get("NOTE"));
		Assert.assertNotNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#updateBean(IBean, Object[])} method with
	 * an empty array.
	 */
	@Test
	public void testUpdateBeanFromEmptyArray()
	{
		IBean bean = beanConverter.createEmptyBean();
		bean.put("ID", BigDecimal.valueOf(2));
		bean.put("NAME", "No Name");
		bean.put("NOTE", "Nothing important");
		bean.put("CREATION", new Date());
		
		beanConverter.updateBean(bean, new Object[] {});
		
		Assert.assertEquals(BigDecimal.valueOf(2), bean.get("ID"));
		Assert.assertEquals("No Name", bean.get("NAME"));
		Assert.assertEquals("Nothing important", bean.get("NOTE"));
		Assert.assertNotNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#updateBean(IBean, Object)} method.
	 */
	@Test
	public void testUpdateBeanFromObject()
	{
		IBean bean = beanConverter.createEmptyBean();
		bean.put("ID", BigDecimal.valueOf(2));
		bean.put("NAME", "No Name");
		bean.put("NOTE", "Nothing important");
		bean.put("CREATION", new Date());
		
		beanConverter.updateBean(bean, new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date()));
		
		Assert.assertEquals(BigDecimal.valueOf(1), bean.get("ID"));
		Assert.assertEquals("Name", bean.get("NAME"));
		Assert.assertEquals("Something", bean.get("NOTE"));
		Assert.assertNotNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#updateBean(IBean, Object[])} method with a
	 * partial array.
	 */
	@Test
	public void testUpdateBeanFromPartialArray()
	{
		IBean bean = beanConverter.createEmptyBean();
		bean.put("ID", BigDecimal.valueOf(2));
		bean.put("NAME", "No Name");
		bean.put("NOTE", "Nothing important");
		bean.put("CREATION", new Date());
		
		beanConverter.updateBean(bean, new Object[] { BigDecimal.valueOf(1), "Name" });
		
		Assert.assertEquals(BigDecimal.valueOf(1), bean.get("ID"));
		Assert.assertEquals("Name", bean.get("NAME"));
		Assert.assertEquals("Nothing important", bean.get("NOTE"));
		Assert.assertNotNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the {@link BeanConverter#updateBean(IBean, Object)} method.
	 */
	@Test
	public void testUpdateEmptyBeanFromObject()
	{
		IBean bean = beanConverter.createEmptyBean();
		
		beanConverter.updateBean(bean, new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date()));
		
		Assert.assertEquals(BigDecimal.valueOf(1), bean.get("ID"));
		Assert.assertEquals("Name", bean.get("NAME"));
		Assert.assertEquals("Something", bean.get("NOTE"));
		Assert.assertNotNull(bean.get("CREATION"));
	}
	
	/**
	 * Tests the various update functions with null as parameters.
	 */
	@Test
	public void testUpdateNulls()
	{
		beanConverter.updateArray(new Object[] {}, (Bean)null);
		beanConverter.updateArray(null, new Bean());
		beanConverter.updateArray(null, (Bean)null);
		beanConverter.updateArray(new Object[] {}, null);
		beanConverter.updateArray(null, new Object());
		beanConverter.updateArray(null, null);
		
		beanConverter.updateBean(new Bean(), null);
		beanConverter.updateBean(null, new Object());
		beanConverter.updateBean(null, null);
		beanConverter.updateBean(new Bean(), (Object[])null);
		beanConverter.updateBean(null, new Object[] {});
		beanConverter.updateBean(null, (Object[])null);
		
		beanConverter.updatePOJO(null, new Bean());
		beanConverter.updatePOJO(new Object(), (Bean)null);
		beanConverter.updatePOJO(null, (Bean)null);
		beanConverter.updatePOJO(null, new Object());
		beanConverter.updatePOJO(new Object(), (Object)null);
		beanConverter.updatePOJO(null, (Object)null);
		beanConverter.updatePOJO(null, new Object[] {});
		beanConverter.updatePOJO(new Object(), (Object[])null);
		beanConverter.updatePOJO(null, (Object[])null);
	}
	
	/**
	 * Test the {@link BeanConverter#updateArray(Object[], IBean)} method with
	 * an array that is too small.
	 */
	@Test
	public void testUpdatePartialArrayFromBean()
	{
		Object[] array = new Object[] { BigDecimal.valueOf(2), "No Name" };
		
		IBean bean = new Bean();
		bean.put("ID", BigDecimal.valueOf(1));
		bean.put("NAME", "Name");
		bean.put("NOTE", "Something");
		bean.put("CREATION", new Date());
		
		beanConverter.updateArray(array, bean);
		
		Assert.assertEquals(BigDecimal.valueOf(1), array[0]);
		Assert.assertEquals("Name", array[1]);
	}
	
	/**
	 * Test the {@link BeanConverter#updateArray(Object[], Object)} method with
	 * an array that is too small.
	 */
	@Test
	public void testUpdatePartialArrayFromPOJO()
	{
		Object[] array = new Object[] { BigDecimal.valueOf(2), "No Name" };
		
		beanConverter.updateArray(array, new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date()));
		
		Assert.assertEquals(BigDecimal.valueOf(1), array[0]);
		Assert.assertEquals("Name", array[1]);
	}
	
	/**
	 * Tests the {@link BeanConverter#updatePOJO(Object, Object[])} method.
	 */
	@Test
	public void testUpdatePOJOFromArray()
	{
		SimplePOJO pojo = new SimplePOJO(BigDecimal.valueOf(2), "No Name", "Nothing important", new Date());
		
		beanConverter.updatePOJO(pojo, new Object[] { BigDecimal.valueOf(1), "Name", "Something" });
		
		Assert.assertEquals(BigDecimal.valueOf(1), pojo.getId());
		Assert.assertEquals("Name", pojo.getName());
		Assert.assertEquals("Something", pojo.getNote());
		Assert.assertNotNull(pojo.getCreation());
	}
	
	/**
	 * Tests the {@link BeanConverter#updatePOJO(Object, IBean)} method.
	 */
	@Test
	public void testUpdatePOJOFromBean()
	{
		SimplePOJO pojo = new SimplePOJO(BigDecimal.valueOf(2), "No Name", "Nothing important", new Date());
		
		IBean bean = new Bean();
		bean.put("ID", BigDecimal.valueOf(1));
		bean.put("NAME", "Name");
		bean.put("NOTE", "Something");
		bean.put("CREATION", new Date());
		
		beanConverter.updatePOJO(pojo, bean);
		
		Assert.assertEquals(BigDecimal.valueOf(1), pojo.getId());
		Assert.assertEquals("Name", pojo.getName());
		Assert.assertEquals("Something", pojo.getNote());
		Assert.assertNotNull(pojo.getCreation());
	}
	
	/**
	 * Tests the {@link BeanConverter#updatePOJO(Object, Object)} method.
	 */
	@Test
	public void testUpdatePOJOFromPOJO()
	{
		SimplePOJO pojo = new SimplePOJO(BigDecimal.valueOf(2), "No Name", "Nothing important", new Date());
		
		beanConverter.updatePOJO(pojo, new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date()));
		
		Assert.assertEquals(BigDecimal.valueOf(1), pojo.getId());
		Assert.assertEquals("Name", pojo.getName());
		Assert.assertEquals("Something", pojo.getNote());
		Assert.assertNotNull(pojo.getCreation());
	}
	
 	/**
	 * Tests settings a custom bean class.
	 */
	@Test
	public void testCustomBeanClass()
	{
		beanConverter.setBeanClass(SimpleExtendedBean.class);
		
		Assert.assertEquals(SimpleExtendedBean.class, beanConverter.getBeanClass());
		
		IBean bean = beanConverter.createBean(new Object[] {BigDecimal.valueOf(1), "Name", "Something", new Date()});
		
		Assert.assertNotNull(bean);
		Assert.assertEquals(SimpleExtendedBean.class, bean.getClass());
	}
	
	/**
	 * Tests if the fallback for the custom bean class is working.
	 */
	@Test
	public void testCustomBeanClassFallback()
	{
		beanConverter.setBeanClass(SimpleExtendedBean.class);
		beanConverter.setBeanClass(null);
		
		IBean bean = beanConverter.createBean(new Object[] {BigDecimal.valueOf(1), "Name", "Something", new Date()});
		
		Assert.assertNotNull(bean);
		Assert.assertEquals(Bean.class, bean.getClass());
	}
	
	/**
	 * Tests setting a custom bean class without the required constructor.
	 * 
	 * @throws Exception when the test fails.
	 */
	@Test
	public void testCustomBeanClassMissingConstructor() throws Exception
	{
		beanConverter.setBeanClass(ConstructorMissingBean.class);
		
		try
		{
			beanConverter.createBean(new Object[] {BigDecimal.valueOf(1), "Name", "Something", new Date()});
			
			Assert.fail("Creation of the malformed Bean should have failed because the required constructor is missing.");
		}
		catch (Exception e)
		{
			//NoSuchMethodException is being expected
			if (!(e.getCause() instanceof NoSuchMethodException))
			{
				throw e;
			}
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple POJO used for testing.
	 * 
	 * @author Robert Zenz
	 */
	public static final class SimplePOJO
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The id. */
		private BigDecimal id;
		
		/** The name. */
		private String name;
		
		/** The note. */
		private String note;
		
		/** The creation. */
		private Date creation;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link SimplePOJO}.
		 */
		public SimplePOJO()
		{
		}
		
		/**
		 * Creates a new instance of {@link SimplePOJO}.
		 * 
		 * @param pId the id.
		 * @param pName the name.
		 * @param pNote the note.
		 * @param pCreation the creation.
		 */
		public SimplePOJO(BigDecimal pId, String pName, String pNote, Date pCreation)
		{
			id = pId;
			name = pName;
			note = pNote;
			creation = pCreation;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the id.
		 * 
		 * @return the id
		 */
		public BigDecimal getId()
		{
			return id;
		}
		
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName()
		{
			return name;
		}
		
		/**
		 * Gets the note.
		 * 
		 * @return the note
		 */
		public String getNote()
		{
			return note;
		}
		
		/**
		 * Gets the creation.
		 * 
		 * @return the creation
		 */
		public Date getCreation()
		{
			return creation;
		}
		
		/**
		 * Sets the id.
		 * 
		 * @param pId the new id.
		 */
		public void setId(BigDecimal pId)
		{
			id = pId;
		}
		
		/**
		 * Sets the name.
		 * 
		 * @param pName the new name.
		 */
		public void setName(String pName)
		{
			name = pName;
		}
		
		/**
		 * Sets the note.
		 * 
		 * @param pNote the new note.
		 */
		public void setNote(String pNote)
		{
			note = pNote;
		}
		
		/**
		 * Sets the creation.
		 * 
		 * @param pCreation the new creation.
		 */
		public void setCreation(Date pCreation)
		{
			creation = pCreation;
		}
		
	}	// SimplePOJO

	/**
	 * A {@link Bean} extension which is missing the required constructor for
	 * the {@link BeanConverter} to create it.
	 * 
	 * @author Robert Zenz
	 */
	private static final class ConstructorMissingBean extends Bean
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ConstructorMissingBean}.
		 */
		public ConstructorMissingBean()
		{
			super();
		}
		
	}	// ConstructorMissingBean
	
	/**
	 * A simple {@link Bean} extension for testing whether the correct class is
	 * being instantiated inside the {@link BeanConverter}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class SimpleExtendedBean extends Bean
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link SimpleExtendedBean}.
		 * 
		 * @param pBeanType the bean type
		 */
		public SimpleExtendedBean(IBeanType pBeanType)
		{
			super(pBeanType);
		}
		
	}	// SimpleExtendedBean
	
	
}	// TestBeanConverter
