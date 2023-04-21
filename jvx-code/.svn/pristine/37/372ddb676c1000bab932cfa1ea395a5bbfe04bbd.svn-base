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
 * 24.11.2014 - [RZ] - creation
 */
package com.sibvisions.rad.persist.event;

import java.math.BigDecimal;
import java.util.Date;

import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;
import javax.rad.type.bean.IBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.rad.persist.bean.BeanConverter;

/**
 * Tests the {@link StorageEvent}.
 * 
 * @author Robert Zenz
 */
public class TestStorageEvent
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
	 * Tests the conversion to other types from an array.
	 */
	@Test
	public void testAutomaticConversionFromArray()
	{
		Object[] oldArray = new Object[] { BigDecimal.valueOf(1), "Name", "Something", new Date() };
		Object[] newArray = new Object[] { BigDecimal.valueOf(1), "Name", "Something", new Date() };
		
		StorageEvent event = new StorageEvent(null, beanConverter, null, oldArray, newArray);
		
		// Old
		
		IBean oldBean = event.getOld();
		oldBean.put("ID", BigDecimal.valueOf(2));
		oldBean.put("NAME", "Changed");
		
		// Call to update the array.
		Assert.assertSame(oldArray, event.getOldAsArray());
		
		Assert.assertEquals(BigDecimal.valueOf(2), oldArray[0]);
		Assert.assertEquals("Changed", oldArray[1]);
		
		SimplePOJO oldPojo = event.getOld(SimplePOJO.class);
		oldPojo.setId(BigDecimal.valueOf(3));
		oldPojo.setName("Second");
		
		// Call to update the array.
		Assert.assertSame(oldArray, event.getOldAsArray());
		
		Assert.assertEquals(BigDecimal.valueOf(3), oldArray[0]);
		Assert.assertEquals("Second", oldArray[1]);
		
		// New
		
		IBean newBean = event.getNew();
		newBean.put("ID", BigDecimal.valueOf(2));
		newBean.put("NAME", "Changed");
		
		// Call to update the array.
		Assert.assertSame(newArray, event.getNewAsArray());
		
		Assert.assertEquals(BigDecimal.valueOf(2), newArray[0]);
		Assert.assertEquals("Changed", newArray[1]);
		
		SimplePOJO newPojo = event.getNew(SimplePOJO.class);
		newPojo.setId(BigDecimal.valueOf(3));
		newPojo.setName("Second");
		
		// Call to update the array.
		Assert.assertSame(newArray, event.getNewAsArray());
		
		Assert.assertEquals(BigDecimal.valueOf(3), newArray[0]);
		Assert.assertEquals("Second", newArray[1]);
	}
	
	/**
	 * Tests the conversion to other types from a bean.
	 */
	@Test
	public void testAutomaticConversionFromBean()
	{
		IBean oldBean = new Bean();
		oldBean.put("ID", BigDecimal.valueOf(1));
		oldBean.put("NAME", "Name");
		oldBean.put("NOTE", "Something");
		oldBean.put("CREATION", new Date());
		
		IBean newBean = new Bean();
		newBean.put("ID", BigDecimal.valueOf(1));
		newBean.put("NAME", "Name");
		newBean.put("NOTE", "Something");
		newBean.put("CREATION", new Date());
		
		StorageEvent event = new StorageEvent(null, beanConverter, null, oldBean, newBean);
		
		// Old
		
		Object[] oldArray = event.getOldAsArray();
		oldArray[0] = BigDecimal.valueOf(2);
		oldArray[1] = "Changed";
		
		// Call to update the bean.
		Assert.assertSame(oldBean, event.getOld());
		
		Assert.assertEquals(BigDecimal.valueOf(2), oldBean.get("ID"));
		Assert.assertEquals("Changed", oldBean.get("NAME"));
		
		SimplePOJO oldPojo = event.getOld(SimplePOJO.class);
		oldPojo.setId(BigDecimal.valueOf(3));
		oldPojo.setName("Second");
		
		// Call to update the bean.
		Assert.assertSame(oldBean, event.getOld());
		
		Assert.assertEquals(BigDecimal.valueOf(3), oldBean.get("ID"));
		Assert.assertEquals("Second", oldBean.get("NAME"));
		
		// New
		
		Object[] newArray = event.getNewAsArray();
		newArray[0] = BigDecimal.valueOf(2);
		newArray[1] = "Changed";
		
		// Call to update the bean.
		Assert.assertSame(newBean, event.getNew());
		
		Assert.assertEquals(BigDecimal.valueOf(2), newBean.get("ID"));
		Assert.assertEquals("Changed", newBean.get("NAME"));
		
		SimplePOJO newPojo = event.getNew(SimplePOJO.class);
		newPojo.setId(BigDecimal.valueOf(3));
		newPojo.setName("Second");
		
		// Call to update the bean.
		Assert.assertSame(newBean, event.getNew());
		
		Assert.assertEquals(BigDecimal.valueOf(3), newBean.get("ID"));
		Assert.assertEquals("Second", newBean.get("NAME"));
	}
	
	/**
	 * Tests the conversion to other types from a POJO.
	 */
	@Test
	public void testAutomaticConversionFromPOJO()
	{
		SimplePOJO oldPojo = new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date());
		SimplePOJO newPojo = new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date());
		
		StorageEvent event = new StorageEvent(null, beanConverter, null, oldPojo, newPojo);
		
		// Old
		
		IBean oldBean = event.getOld();
		oldBean.put("ID", BigDecimal.valueOf(2));
		oldBean.put("NAME", "Changed");
		
		// Call to update the array.
		Assert.assertSame(oldPojo, event.getOld(SimplePOJO.class));
		
		Assert.assertEquals(BigDecimal.valueOf(2), oldPojo.getId());
		Assert.assertEquals("Changed", oldPojo.getName());
		
		Object[] oldArray = event.getOldAsArray();
		oldArray[0] = BigDecimal.valueOf(3);
		oldArray[1] = "Second";
		
		Assert.assertSame(oldPojo, event.getOld(SimplePOJO.class));
		
		Assert.assertEquals(BigDecimal.valueOf(3), oldPojo.getId());
		Assert.assertEquals("Second", oldPojo.getName());
		
		// New
		
		IBean newBean = event.getNew();
		newBean.put("ID", BigDecimal.valueOf(2));
		newBean.put("NAME", "Changed");
		
		// Call to update the array.
		Assert.assertSame(newPojo, event.getNew(SimplePOJO.class));
		
		Assert.assertEquals(BigDecimal.valueOf(2), newPojo.getId());
		Assert.assertEquals("Changed", newPojo.getName());
		
		Object[] newArray = event.getNewAsArray();
		newArray[0] = BigDecimal.valueOf(3);
		newArray[1] = "Second";
		
		Assert.assertSame(newPojo, event.getNew(SimplePOJO.class));
		
		Assert.assertEquals(BigDecimal.valueOf(3), newPojo.getId());
		Assert.assertEquals("Second", newPojo.getName());
	}
	
	/**
	 * Tests if the POJO instance is kept in tact, even though e different one
	 * had to be created.
	 */
	@Test
	public void testKeepsSamePOJOInstance()
	{
		SimplePOJO oldPojo = new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date());
		SimplePOJO newPojo = new SimplePOJO(BigDecimal.valueOf(1), "Name", "Something", new Date());
		
		StorageEvent event = new StorageEvent(null, beanConverter, null, oldPojo, newPojo);
		
		// Old
		
		ExtendedPOJO oldExtendedPojo = event.getOld(ExtendedPOJO.class);
		Assert.assertNotNull(oldExtendedPojo);
		Assert.assertNotSame(oldPojo, oldExtendedPojo);
		Assert.assertEquals(oldPojo.getId(), oldExtendedPojo.getId());
		Assert.assertEquals(oldPojo.getName(), oldExtendedPojo.getName());
		Assert.assertEquals(oldPojo.getNote(), oldExtendedPojo.getNote());
		Assert.assertEquals(oldPojo.getCreation(), oldExtendedPojo.getCreation());
		
		oldExtendedPojo.setName("Changed");
		
		Assert.assertSame(oldPojo, event.getOld(SimplePOJO.class));
		Assert.assertEquals("Changed", oldPojo.getName());
		
		// New
		
		ExtendedPOJO newExtendedPojo = event.getNew(ExtendedPOJO.class);
		Assert.assertNotNull(newExtendedPojo);
		Assert.assertNotSame(newPojo, newExtendedPojo);
		Assert.assertEquals(newPojo.getId(), newExtendedPojo.getId());
		Assert.assertEquals(newPojo.getName(), newExtendedPojo.getName());
		Assert.assertEquals(newPojo.getNote(), newExtendedPojo.getNote());
		Assert.assertEquals(newPojo.getCreation(), newExtendedPojo.getCreation());
		
		newExtendedPojo.setName("Changed");
		
		Assert.assertSame(newPojo, event.getNew(SimplePOJO.class));
		Assert.assertEquals("Changed", newPojo.getName());
	}
	
	/**
	 * Tests if fetching multiple POJOs with different classes behaves
	 * correctly.
	 */
	@Test
	public void testMultiplePOJOClasses()
	{
		Object[] oldArray = new Object[] { BigDecimal.valueOf(1), "Name", "Something", new Date() };
		Object[] newArray = new Object[] { BigDecimal.valueOf(1), "Name", "Something", new Date() };
		
		StorageEvent event = new StorageEvent(null, beanConverter, null, oldArray, newArray);
		
		// Old
		
		SimplePOJO oldSimplePojo = event.getOld(SimplePOJO.class);
		// Sanity check
		Assert.assertNotNull(oldSimplePojo);
		Assert.assertEquals(oldArray[0], oldSimplePojo.getId());
		Assert.assertEquals(oldArray[1], oldSimplePojo.getName());
		Assert.assertEquals(oldArray[2], oldSimplePojo.getNote());
		Assert.assertEquals(oldArray[3], oldSimplePojo.getCreation());
		
		// Should create a new instance.
		ExtendedPOJO oldExtendedPojo = event.getOld(ExtendedPOJO.class);
		Assert.assertNotNull(oldExtendedPojo);
		Assert.assertNotSame(oldSimplePojo, oldExtendedPojo);
		Assert.assertEquals(oldArray[0], oldExtendedPojo.getId());
		Assert.assertEquals(oldArray[1], oldExtendedPojo.getName());
		Assert.assertEquals(oldArray[2], oldExtendedPojo.getNote());
		Assert.assertEquals(oldArray[3], oldExtendedPojo.getCreation());
		Assert.assertNull(oldExtendedPojo.getExtendedState());
		
		// Should return the same as the previous one.
		SimplePOJO oldSecondSimplePojo = event.getOld(SimplePOJO.class);
		Assert.assertNotNull(oldSecondSimplePojo);
		Assert.assertSame(oldExtendedPojo, oldSecondSimplePojo);
		
		// New
		
		SimplePOJO newSimplePojo = event.getNew(SimplePOJO.class);
		// Sanity check
		Assert.assertNotNull(newSimplePojo);
		Assert.assertEquals(newArray[0], newSimplePojo.getId());
		Assert.assertEquals(newArray[1], newSimplePojo.getName());
		Assert.assertEquals(newArray[2], newSimplePojo.getNote());
		Assert.assertEquals(newArray[3], newSimplePojo.getCreation());
		
		// Should create a new instance.
		ExtendedPOJO newExtendedPojo = event.getNew(ExtendedPOJO.class);
		Assert.assertNotNull(newExtendedPojo);
		Assert.assertNotSame(newSimplePojo, newExtendedPojo);
		Assert.assertEquals(newArray[0], newExtendedPojo.getId());
		Assert.assertEquals(newArray[1], newExtendedPojo.getName());
		Assert.assertEquals(newArray[2], newExtendedPojo.getNote());
		Assert.assertEquals(newArray[3], newExtendedPojo.getCreation());
		Assert.assertNull(newExtendedPojo.getExtendedState());
		
		// Should return the same as the previous one.
		SimplePOJO newSecondSimplePojo = event.getNew(SimplePOJO.class);
		Assert.assertNotNull(newSecondSimplePojo);
		Assert.assertSame(newExtendedPojo, newSecondSimplePojo);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple POJO used for testing.
	 * 
	 * @author Robert Zenz
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
	 * An extension of {2link SimplePOJO} with an additional field.
	 * 
	 * @author Robert Zenz
	 */
	public static final class ExtendedPOJO extends SimplePOJO
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The extended state. */
		private String extendedState;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ExtendedPOJO}.
		 */
		public ExtendedPOJO()
		{
			super();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link ExtendedPOJO}.
		 * 
		 * @param pId the id.
		 * @param pName the name.
		 * @param pNote the note.
		 * @param pCreation the creation.
		 * @param pExtendedState the extended state.
		 */
		public ExtendedPOJO(BigDecimal pId, String pName, String pNote, Date pCreation, String pExtendedState)
		{
			super(pId, pName, pNote, pCreation);
			
			extendedState = pExtendedState;
		}
		
		/**
		 * Gets the extended state.
		 * 
		 * @return the extended state
		 */
		public String getExtendedState()
		{
			return extendedState;
		}
		
		/**
		 * Sets the extended state.
		 * 
		 * @param pExtendedState the new extended state.
		 */
		public void setExtendedState(String pExtendedState)
		{
			extendedState = pExtendedState;
		}
		
	}	// ExtendedPOJO
	
}	// TestStorageEvent
