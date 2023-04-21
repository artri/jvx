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
 */
package javax.rad.type;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link DynamicBean} and {@link com.sibvisions.util.type.BeanUtil} methods.
 * 
 * @author Martin Handsteiner
 * @see DynamicBean
 * @see com.sibvisions.util.type.BeanUtil
 */
public class TestTypes
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// START
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** 
	 * Profile.
	 *  
	 * @param pArgs arguments. 
	 */
	public static void main(String[] pArgs)
	{
/*		
		
		Timestamp[] tamps = new Timestamp[] 
		{
			Timestamp.valueOf("2009-10-01 00:00:02.0"),
			Timestamp.valueOf("2009-10-01 00:00:03.0"),
			Timestamp.valueOf("2009-10-01 00:00:04.0"),
			Timestamp.valueOf("2009-10-01 00:01:02.0"),
			Timestamp.valueOf("2009-10-02 00:00:02.0"),
			Timestamp.valueOf("2009-11-02 00:00:02.0"),
			Timestamp.valueOf("2009-05-02 00:00:02.0"),
			Timestamp.valueOf("2010-10-02 00:00:02.0"),
	    };

		for (int i = 0; i < tamps.length; i++)
		{
			int index = (int)tamps[i].getTime();
			System.out.println(index + "  " + (index % 256));
			index ^= (index >>> 20) ^ (index >>> 12);
			System.out.println(index + "  " + (index % 256));
			index =  index ^ (index >>> 7) ^ (index >>> 4);
			System.out.println(index + "  " + (index % 256));
			index &= 0xff;
			
			System.out.println(tamps[i] + "  " + index);
		}
*/		
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	/**
	 * Tests BooleanType.
	 */
	@Test
	public void testBooleanType()
	{
		BooleanType booleanType = new BooleanType();
		
		Assert.assertEquals(Boolean.TRUE,	Boolean.valueOf(true));
		Assert.assertEquals(Boolean.TRUE,	booleanType.valueOf("true"));
		Assert.assertEquals(Boolean.TRUE,	booleanType.valueOf("1"));
		Assert.assertEquals(Boolean.TRUE,	booleanType.valueOf(Integer.valueOf(5)));
		Assert.assertEquals(Boolean.FALSE,	Boolean.valueOf(false));
		Assert.assertEquals(Boolean.FALSE,	booleanType.valueOf("false"));
		Assert.assertEquals(Boolean.FALSE,	booleanType.valueOf("0"));
		Assert.assertEquals(Boolean.FALSE,	booleanType.valueOf(Integer.valueOf(0)));
	}
	
	/**
	 * Tests DecimalType.
	 */
	@Test
	public void testDecimalType()
	{
		DecimalType decimalType = new DecimalType();
		
		Assert.assertEquals(BigDecimal.class, decimalType.valueOf(Integer.valueOf(5)).getClass());
		Assert.assertEquals(BigDecimal.class, decimalType.valueOf("5").getClass());
		Assert.assertEquals(BigDecimal.class, decimalType.valueOf(BigDecimal.valueOf(5)).getClass());
		Assert.assertEquals(BigDecimal.class, decimalType.valueOf(Double.valueOf(5)).getClass());
		Assert.assertEquals(BigDecimal.class, decimalType.valueOf(Float.valueOf(5)).getClass());
		Assert.assertEquals(BigDecimal.class, decimalType.valueOf(Long.valueOf(5)).getClass());
		Assert.assertEquals(BigDecimal.class, decimalType.valueOf(Byte.valueOf((byte)5)).getClass());
		Assert.assertEquals(BigDecimal.class, decimalType.valueOf(Integer.valueOf(5)).getClass());
		
		Assert.assertTrue(decimalType.equals(new BigDecimal("5.00000"), new BigDecimal("5")));
	}
	
	/**
	 * Tests DoubleType.
	 */
	@Test
	public void testDoubleType()
	{
		DoubleType doubleType = new DoubleType();
		
		Assert.assertEquals(Double.class, doubleType.valueOf(Integer.valueOf(5)).getClass());
		Assert.assertEquals(Double.class, doubleType.valueOf("5").getClass());
		Assert.assertEquals(Double.class, doubleType.valueOf(BigDecimal.valueOf(5)).getClass());
		Assert.assertEquals(Double.class, doubleType.valueOf(Double.valueOf(5)).getClass());
		Assert.assertEquals(Double.class, doubleType.valueOf(Float.valueOf(5)).getClass());
		Assert.assertEquals(Double.class, doubleType.valueOf(Long.valueOf(5)).getClass());
		Assert.assertEquals(Double.class, doubleType.valueOf(Byte.valueOf((byte)5)).getClass());
		Assert.assertEquals(Double.class, doubleType.valueOf(Integer.valueOf(5)).getClass());
	}
	
	/**
	 * Tests FloatType.
	 */
	@Test
	public void testFloatType()
	{
		FloatType floatType = new FloatType();
		
		Assert.assertEquals(Float.class, floatType.valueOf(Integer.valueOf(5)).getClass());
		Assert.assertEquals(Float.class, floatType.valueOf("5").getClass());
		Assert.assertEquals(Float.class, floatType.valueOf(BigDecimal.valueOf(5)).getClass());
		Assert.assertEquals(Float.class, floatType.valueOf(Double.valueOf(5)).getClass());
		Assert.assertEquals(Float.class, floatType.valueOf(Float.valueOf(5)).getClass());
		Assert.assertEquals(Float.class, floatType.valueOf(Long.valueOf(5)).getClass());
		Assert.assertEquals(Float.class, floatType.valueOf(Byte.valueOf((byte)5)).getClass());
		Assert.assertEquals(Float.class, floatType.valueOf(Integer.valueOf(5)).getClass());
	}
	
	/**
	 * Tests IntegerType.
	 */
	@Test
	public void testIntegerType()
	{
		IntegerType intType = new IntegerType();
		
		Assert.assertEquals(Integer.class, intType.valueOf(Integer.valueOf(5)).getClass());
		Assert.assertEquals(Integer.class, intType.valueOf("5").getClass());
		Assert.assertEquals(Integer.class, intType.valueOf(BigDecimal.valueOf(5)).getClass());
		Assert.assertEquals(Integer.class, intType.valueOf(Double.valueOf(5)).getClass());
		Assert.assertEquals(Integer.class, intType.valueOf(Float.valueOf(5)).getClass());
		Assert.assertEquals(Integer.class, intType.valueOf(Long.valueOf(5)).getClass());
		Assert.assertEquals(Integer.class, intType.valueOf(Byte.valueOf((byte)5)).getClass());
		Assert.assertEquals(Integer.class, intType.valueOf(Integer.valueOf(5)).getClass());
	}
	
	/**
	 * Tests LongType.
	 */
	@Test
	public void testLongType()
	{
		LongType longType = new LongType();
		
		Assert.assertEquals(Long.class, longType.valueOf(Integer.valueOf(5)).getClass());
		Assert.assertEquals(Long.class, longType.valueOf("5").getClass());
		Assert.assertEquals(Long.class, longType.valueOf(BigDecimal.valueOf(5)).getClass());
		Assert.assertEquals(Long.class, longType.valueOf(Double.valueOf(5)).getClass());
		Assert.assertEquals(Long.class, longType.valueOf(Float.valueOf(5)).getClass());
		Assert.assertEquals(Long.class, longType.valueOf(Long.valueOf(5)).getClass());
		Assert.assertEquals(Long.class, longType.valueOf(Byte.valueOf((byte)5)).getClass());
		Assert.assertEquals(Long.class, longType.valueOf(Integer.valueOf(5)).getClass());
	}

	/**
	 * Tests BooleanType.
	 */
	@Test
	public void testStringType()
	{
		StringType stringType = new StringType();
		
		Assert.assertEquals(String.class, stringType.valueOf(Integer.valueOf(5)).getClass());
		Assert.assertEquals(String.class, stringType.valueOf("5").getClass());
		Assert.assertEquals(String.class, stringType.valueOf(BigDecimal.valueOf(5)).getClass());
		Assert.assertEquals(String.class, stringType.valueOf(Double.valueOf(5)).getClass());
		Assert.assertEquals(String.class, stringType.valueOf(Float.valueOf(5)).getClass());
		Assert.assertEquals(String.class, stringType.valueOf(Long.valueOf(5)).getClass());
		Assert.assertEquals(String.class, stringType.valueOf(Byte.valueOf((byte)5)).getClass());
		Assert.assertEquals(String.class, stringType.valueOf(Integer.valueOf(5)).getClass());
	}
	
	
}	// TestTypes
