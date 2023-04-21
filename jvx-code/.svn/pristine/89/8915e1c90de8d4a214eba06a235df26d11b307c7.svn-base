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
package com.sibvisions.rad.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.rad.persist.DataSourceException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.ExceptionUtil;

/**
 * Tests the serialization and deserialization of objects with the <code>ByteSerializer</code> class.
 * 
 * @author René Jahn
 */
@SuppressWarnings("deprecation")
public class TestByteSerializer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** byte array stream for serialization. */
	private ByteArrayOutputStream bos = null;

	/** serialized stream. */
	private DataOutputStream dos = null;
	
	/** the serializer. */
    private ByteSerializer serializer = new ByteSerializer();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Initialization of the OutputStream. 
	 */
	@Before
	public void beforeTest()
	{
		bos = new ByteArrayOutputStream();
		dos = new DataOutputStream(bos);
		
		serializer.setObjectStreamEnabled(true);
	}
	
	/**
	 * Shows serialized content. 
	 */
	@After
	public void afterTest()
	{
		dumpStream();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Write the content of the serialized stream to stdout.
	 */
	private void dumpStream()
	{
		byte[] byContent = bos.toByteArray();
		
		for (int i = 0, anz = byContent.length; i < anz; i++)
		{
			if (i > 0)
			{
				System.out.print(" ");
			}
			
			System.out.print(byContent[i] & 255);
		}
		
		System.out.println();
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test null object.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testNull() throws Exception
	{
		DataInputStream dis;
		
		serializer.write(dos, null);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertNull(serializer.read(dis));
	}
	
	/**
	 * Test Byte class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testByte() throws Exception
	{
		DataInputStream dis;
		
		Byte byValue = new Byte((byte)178);
		
		
		serializer.write(dos, byValue);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(byValue.intValue(), ((Byte)serializer.read(dis)).intValue());
	}

	/**
	 * Test Character class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testCharacter() throws Exception
	{
		DataInputStream dis;
		
		Character chValue = new Character('J');
		
		
		serializer.write(dos, chValue);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(chValue.charValue(), ((Character)serializer.read(dis)).charValue());
	}

	/**
	 * Test Boolean class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testBoolean() throws Exception
	{
		DataInputStream dis;
		
		Boolean boTrue = new Boolean(true);
		Boolean boFalse = new Boolean(false);
		
		
		serializer.write(dos, boTrue);
		serializer.write(dos, boFalse);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertTrue(((Boolean)serializer.read(dis)).booleanValue());
		Assert.assertFalse(((Boolean)serializer.read(dis)).booleanValue());
	}
	
	/**
	 * Test Float class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testFloat() throws Exception
	{
		DataInputStream dis;
		
		Float fZero = new Float(0);
		Float fMaxValue = new Float(Float.MAX_VALUE);
		
		
		serializer.write(dos, fZero);
		serializer.write(dos, fMaxValue);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(fZero, ((Float)serializer.read(dis)));
		Assert.assertEquals(fMaxValue, ((Float)serializer.read(dis)));
	}

	/**
	 * Test Double class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testDouble() throws Exception
	{
		DataInputStream dis;
		
		Double dZero = new Double(0);
		Double dMaxValue = new Double(Double.MAX_VALUE);
		
		
		serializer.write(dos, dZero);
		serializer.write(dos, dMaxValue);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(dZero, ((Double)serializer.read(dis)));
		Assert.assertEquals(dMaxValue, ((Double)serializer.read(dis)));
	}
	
	/**
	 * Test Short class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testShort() throws Exception
	{
		DataInputStream dis;
		
		Short shZero = new Short((short)0);
		Short shMaxValue = new Short(Short.MAX_VALUE);
		
		
		serializer.write(dos, shZero);
		serializer.write(dos, shMaxValue);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(shZero, ((Short)serializer.read(dis)));
		Assert.assertEquals(shMaxValue, ((Short)serializer.read(dis)));
	}

	/**
	 * Test Integer class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testInteger() throws Exception
	{
		DataInputStream dis;
		
		Integer iZero = new Integer(0);
		Integer iMaxValue = new Integer(Integer.MAX_VALUE);
		
		
		serializer.write(dos, iZero);
		serializer.write(dos, iMaxValue);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(iZero, ((Integer)serializer.read(dis)));
		Assert.assertEquals(iMaxValue, ((Integer)serializer.read(dis)));
	}
	
	/**
	 * Test Long class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testLong() throws Exception
	{
		DataInputStream dis;
		
		Long lZero = new Long(0);
		Long lMaxValue = new Long(Long.MAX_VALUE);
		
		
		serializer.write(dos, lZero);
		serializer.write(dos, lMaxValue);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(lZero, ((Long)serializer.read(dis)));
		Assert.assertEquals(lMaxValue, ((Long)serializer.read(dis)));
	}
	
	/**
	 * Test java.util.Date class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testUtilDate() throws Exception
	{
		DataInputStream dis;
		
		Calendar cal = Calendar.getInstance();

		java.util.Date date = new java.util.Date(cal.getTimeInMillis());
		
		
		serializer.write(dos, date);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(date.toString(), ((java.util.Date)serializer.read(dis)).toString());
	}
	
	/**
	 * Test java.sql.Date class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSqlDate() throws Exception
	{
		DataInputStream dis;
		
		Calendar cal = Calendar.getInstance();

		java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
		

		serializer.write(dos, date);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(date.toString(), ((java.sql.Date)serializer.read(dis)).toString());
	}
	
	/**
	 * Test java.sql.Time class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSqlTime() throws Exception
	{
		DataInputStream dis;
		
		Calendar cal = Calendar.getInstance();

		java.sql.Time date = new java.sql.Time(cal.getTimeInMillis());
		
		
		serializer.write(dos, date);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(date.toString(), ((java.sql.Time)serializer.read(dis)).toString());
	}
	
	/**
	 * Test java.sql.Timestamp class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSqlTimestamp() throws Exception
	{
		DataInputStream dis;
		
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DATE, 2);
		cal.set(Calendar.MONTH, 4);
		cal.set(Calendar.YEAR, 540);

		java.sql.Timestamp date = new java.sql.Timestamp(cal.getTimeInMillis());
		
		
		serializer.write(dos, date);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(date.toString(), ((java.sql.Timestamp)serializer.read(dis)).toString());

		//ohne Nanos
		date.setNanos(0);
		
		serializer.write(dos, date);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		serializer.read(dis);
		Assert.assertEquals(date.toString(), ((java.sql.Timestamp)serializer.read(dis)).toString());
	}

	/**
	 * Test String class and the calculated type length.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testString() throws Exception
	{
		DataInputStream dis;
		
		String sPlain = "serialize me ....................................................." +
		                " i don't know what happens to me ?äöü_#+´^`éáÄÖÜ.................." +
		                " much characters";
		
		String sPlain2 = sPlain + sPlain + sPlain;
		String sPlain3 = sPlain2 + sPlain2 + sPlain2 + sPlain2 + sPlain2 + sPlain2 + sPlain2;
		
		sPlain3 += sPlain3 + sPlain3 + sPlain3 + sPlain3 + sPlain3 + sPlain3 + sPlain3;
		sPlain3 += sPlain3 + sPlain3 + sPlain3 + sPlain3 + sPlain3 + sPlain3 + sPlain3;

		
		//Testen der berechneten Type-Länge!
		serializer.write(dos, sPlain);
		serializer.write(dos, sPlain2);
		serializer.write(dos, sPlain3);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(sPlain, serializer.read(dis));
		Assert.assertEquals(sPlain2, serializer.read(dis));
		Assert.assertEquals(sPlain3, serializer.read(dis));
	}
	
	/**
	 * Test StringBuffer class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testStringBuffer() throws Exception
	{
		DataInputStream dis;
		
		StringBuffer sbfPlain = new StringBuffer("serialize me");

		
		serializer.write(dos, sbfPlain);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(sbfPlain.toString(), serializer.read(dis).toString());
	}

	/**
	 * Test StringBuilder class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testStringBuilder() throws Exception
	{
		DataInputStream dis;
		
		StringBuilder sblPlain = new StringBuilder("serialize me again much characters");

		
		serializer.write(dos, sblPlain);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(sblPlain.toString(), serializer.read(dis).toString());
	}
	
	/**
	 * Tests read an empty stream.
	 * 
	 * @throws Exception if test fails
	 */
	@Test (expected = Exception.class)
	public void testEmptyStream() throws Exception
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		serializer.read(dis);
	}
	
	/**
	 * Test java.math.BigInteger class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testBigInteger() throws Exception
	{
		DataInputStream dis;
		
		BigInteger biValue = new BigInteger("124982761512");
		
		
		serializer.write(dos, biValue);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(biValue.longValue(), ((BigInteger)serializer.read(dis)).longValue());
	}
	
	/**
	 * Test java.math.BigDecimal class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testBigDecimal() throws Exception
	{
		DataInputStream dis;
		
		BigDecimal bdValue = new BigDecimal("124982761512");
		BigDecimal bdValueScale = new BigDecimal("124982761512.9876543");
		
		
		serializer.write(dos, bdValue);
		serializer.write(dos, bdValueScale);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(bdValue.floatValue(), ((BigDecimal)serializer.read(dis)).floatValue(), 0);
		Assert.assertEquals(bdValueScale.floatValue(), ((BigDecimal)serializer.read(dis)).floatValue(), 0);
	}

	/**
	 * Test userdefined class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSerializableObject() throws Exception
	{
		DataInputStream dis;
		
		SerializableClass serc = new SerializableClass("Hugo Boss");
		
		
		serializer.write(dos, serc);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(serc.toString(), ((SerializableClass)serializer.read(dis)).toString());
	}
	
	/**
	 * Test java.text.MessageFormat class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testMessageFormat() throws Exception
	{
		DataInputStream dis;
		
		MessageFormat mesf = new MessageFormat("{0}, {0}, {0}");
		
		
		serializer.write(dos, mesf);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(mesf.toString(), ((MessageFormat)serializer.read(dis)).toString());
	}
	
	/**
	 * Test java.text.ChoiceFormat class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testChoiceFormat() throws Exception
	{
		DataInputStream dis;
		
		ChoiceFormat chf = new ChoiceFormat("-1#is negative");
		
		
		serializer.write(dos, chf);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(chf.toString(), ((ChoiceFormat)serializer.read(dis)).toString());
	}
	
	/**
	 * Test java.text.DecimalFormat class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testDecimalFormat() throws Exception
	{
		DataInputStream dis;
		
		DecimalFormat df = new DecimalFormat("###");
		
		
		serializer.write(dos, df);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(df.toPattern(), ((DecimalFormat)serializer.read(dis)).toPattern());
	}

	/**
	 * Test java.text.SimpleDateFormat class.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSimpleDateFormat() throws Exception
	{
		DataInputStream dis;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		
		serializer.write(dos, sdf);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		Assert.assertEquals(sdf.toPattern(), ((SimpleDateFormat)serializer.read(dis)).toPattern());
	}

	/**
	 * Test Object[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testObjectArray() throws Exception
	{
		DataInputStream dis;
		
		Object[] o = new Object[] {"Test", new Integer(1)};
		Object[] oResult;
		
		
		serializer.write(dos, o);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		oResult = (Object[])serializer.read(dis);
		
		Assert.assertArrayEquals(o, oResult);
	}
	
	/**
	 * Test Object[][].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testObject2DimArray() throws Exception
	{
		DataInputStream dis;
		
		Object[][] o = new Object[][] { {"Test", new Integer(1)}, {"Test-2", new Integer(2)} };
		Object[][] oResult;
		
		
		serializer.write(dos, o);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		oResult = (Object[][])serializer.read(dis);
		
		Assert.assertArrayEquals(o[0], oResult[0]);
		Assert.assertArrayEquals(o[1], oResult[1]);
	}
	
	/**
	 * Test Object[][][].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testObject3DimArray() throws Exception
	{
		DataInputStream dis;
		
		Object[][][] o = new Object[][][] { { {"Test", new Integer(1)}, {"Test-2", new Integer(2)} } };
		Object[][][] oResult;
		
		
		serializer.write(dos, o);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		oResult = (Object[][][])serializer.read(dis);
		
		Assert.assertArrayEquals(o[0][0], oResult[0][0]);
	}

	/**
	 * Test byte[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testPrimitiveByteArray() throws Exception
	{
		DataInputStream dis;
		
		byte[] by = new byte[] { 0x48, 0x49, 0x50 };
		byte[] byResult;
		
		
		serializer.write(dos, by);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		byResult = (byte[])serializer.read(dis);
		
		Assert.assertArrayEquals(by, byResult);
	}
	
	/**
	 * Test boolean[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testPrimitiveBooleanArray() throws Exception
	{
		DataInputStream dis;
		
		boolean[] bool = new boolean[] { true, true, false, true, false };
		boolean[] boolResult;
		
		
		serializer.write(dos, bool);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		boolResult = (boolean[])serializer.read(dis);
		
		for (int i = 0, anz = bool.length; i < anz; i++)
		{
			Assert.assertEquals(Boolean.valueOf(bool[i]), Boolean.valueOf(boolResult[i]));
		}
	}
	
	/**
	 * Test char[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testPrimitiveCharArray() throws Exception
	{
		DataInputStream dis;
		
		char[] ch = new char[] { 'A', 'B', 'Z' };
		char[] chResult;
		
		
		serializer.write(dos, ch);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		chResult = (char[])serializer.read(dis);
		
		Assert.assertArrayEquals(ch, chResult);
	}
	
	/**
	 * Test float[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testPrimitiveFloatArray() throws Exception
	{
		DataInputStream dis;
		
		float[] fl = new float[] { 100.5f, 88.9999999f, 0.4828139f };
		float[] flResult;
		
		
		serializer.write(dos, fl);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		flResult = (float[])serializer.read(dis);
		
		for (int i = 0, anz = fl.length; i < anz; i++)
		{
			Assert.assertEquals(fl[i], flResult[i], 0);
		}
	}
	
	/**
	 * Test double[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testPrimitiveDoubleArray() throws Exception
	{
		DataInputStream dis;
		
		double[] dbl = new double[] { 100.5f, 88.9999999f, 0.4828139f };
		double[] dblResult;
		
		
		serializer.write(dos, dbl);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		dblResult = (double[])serializer.read(dis);
		
		for (int i = 0, anz = dbl.length; i < anz; i++)
		{
			Assert.assertEquals(dbl[i], dblResult[i], 0);
		}
	}
	
	/**
	 * Test short[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testPrimitiveShortArray() throws Exception
	{
		DataInputStream dis;
		
		short[] sh = new short[] { (short)5, (short)240, (short)255, (short)0 };
		short[] shResult;
		
		
		serializer.write(dos, sh);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		shResult = (short[])serializer.read(dis);
		
		Assert.assertArrayEquals(sh, shResult);
	}
	
	/**
	 * Test int[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testPrimitiveIntArray() throws Exception
	{
		DataInputStream dis;
		
		int[] in = new int[] { 6, 7, 65535, 1050 };
		int[] inResult;
		
		
		serializer.write(dos, in);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		inResult = (int[])serializer.read(dis);
		
		Assert.assertArrayEquals(in, inResult);
	}
	
	/**
	 * Test long[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testPrimitiveLongArray() throws Exception
	{
		DataInputStream dis;
		
		long[] lng = new long[] { 9999999999999L, 102034010201L, 1038187379502998771L };
		long[] lngResult;
		
		
		serializer.write(dos, lng);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		lngResult = (long[])serializer.read(dis);
		
		Assert.assertArrayEquals(lng, lngResult);
	}
	
	/**
	 * Test Integer[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testIntegerArray() throws Exception
	{
		DataInputStream dis;
		
		Integer[] lng = new Integer[] { new Integer(881), new Integer(13401987), new Integer(999999999) };
		Integer[] lngResult;
		
		
		serializer.write(dos, lng);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		lngResult = (Integer[])serializer.read(dis);
		
		Assert.assertArrayEquals(lng, lngResult);
	}

	/**
	 * Test Long[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testLongArray() throws Exception
	{
		DataInputStream dis;
		
		Long[] lng = new Long[] { new Long(88818881L), new Long(10203401987L), new Long(999999999999999999L) };
		Long[] lngResult;
		
		
		serializer.write(dos, lng);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		lngResult = (Long[])serializer.read(dis);
		
		Assert.assertArrayEquals(lng, lngResult);
	}
	
	/**
	 * Test String[].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testStringArray() throws Exception
	{
		DataInputStream dis;
		
		String[] sArray = new String[] {"Test", "Hugo Boss"};
		String[] sResult;
		
		
		serializer.write(dos, sArray);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		sResult = (String[])serializer.read(dis);
		
		Assert.assertArrayEquals(sArray, sResult);
	}
	
	/**
	 * Test String[][].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testString2DimArray() throws Exception
	{
		DataInputStream dis;
		
		String[][] sArray = new String[][] { {"String 1"}, {"String 2"} };
		String[][] sResult;
		
		
		serializer.write(dos, sArray);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		sResult = (String[][])serializer.read(dis);
		
		Assert.assertArrayEquals(sArray[0], sResult[0]);
		Assert.assertArrayEquals(sArray[1], sResult[1]);
	}
	
	/**
	 * Test String[][][].
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testString3DimArray() throws Exception
	{
		DataInputStream dis;
		
		String[][][] sArray = new String[][][] { { {"Element 1"}, {"Element 2"} } };
		String[][][] sResult;
		
		
		serializer.write(dos, sArray);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		sResult = (String[][][])serializer.read(dis);
		
		Assert.assertArrayEquals(sArray[0][0], sResult[0][0]);
	}

	/**
	 * Test Throwable.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testThrowable() throws Exception
	{
		DataInputStream dis;
		
		Exception ex = new Exception("TESTCASE");
		Exception exResult;
		
		
		serializer.write(dos, ex);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		exResult = (Exception)serializer.read(dis);
		
		Assert.assertEquals(ExceptionUtil.dump(ex, true), ExceptionUtil.dump(exResult, true));

		beforeTest();
		
		ex = new Exception("A", new Exception("B", new Exception("C", new Exception("D", new Exception("E")))));
		
		serializer.write(dos, ex);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		exResult = (Exception)serializer.read(dis);
		
		Assert.assertEquals(ExceptionUtil.dump(ex, true), ExceptionUtil.dump(exResult, true));
		
		beforeTest();

		ex = new Exception("A", new DataSourceException("B", new Exception("C")));
		
		serializer.write(dos, ex);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		exResult = (Exception)serializer.read(dis);
		
		Assert.assertEquals(ExceptionUtil.dump(ex, true), ExceptionUtil.dump(exResult, true));
	}
	
	/**
	 * Test ArrayList.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testArrayList() throws Exception
	{
		DataInputStream dis;
		
		ArrayList al = new ArrayList();
		ArrayList alResult;
		
		
		al.add("Hugo Boss");
		al.add(new Integer(2));
		al.add(new SerializableClass("MyClass"));
		
		serializer.write(dos, al);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		alResult = (ArrayList)serializer.read(dis);
		
		Assert.assertTrue(al.size() == alResult.size());

		Assert.assertEquals(al.get(0).toString(), alResult.get(0).toString());
		Assert.assertEquals(((Integer)al.get(1)).intValue(), ((Integer)alResult.get(1)).intValue());
		Assert.assertEquals(((SerializableClass)al.get(2)).toString(), ((SerializableClass)alResult.get(2)).toString());
	}

	/**
	 * Test ArrayUtil.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testArrayUtil() throws Exception
	{
		DataInputStream dis;
		
		ArrayUtil au = new ArrayUtil();
		ArrayUtil auResult;
		
		
		au.add("Hugo Boss");
		au.add(new Integer(2));
		au.add(new SerializableClass("MyClass"));
		
		serializer.write(dos, au);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		auResult = (ArrayUtil)serializer.read(dis);
		
		Assert.assertTrue(au.size() == auResult.size());

		Assert.assertEquals(au.get(0).toString(), auResult.get(0).toString());
		Assert.assertEquals(((Integer)au.get(1)).intValue(), ((Integer)auResult.get(1)).intValue());
		Assert.assertEquals(((SerializableClass)au.get(2)).toString(), ((SerializableClass)auResult.get(2)).toString());
	}

	/**
	 * Test Vector.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testVector() throws Exception
	{
		DataInputStream dis;
		
		Vector v = new Vector();
		Vector vResult;
		
		
		v.add("Hugo Boss im Vector");
		v.add(new Integer(5));
		v.add(new SerializableClass("MyClass name"));
		
		serializer.write(dos, v);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		vResult = (Vector)serializer.read(dis);
		
		Assert.assertTrue(v.size() == vResult.size());

		Assert.assertEquals(v.get(0).toString(), vResult.get(0).toString());
		Assert.assertEquals(((Integer)v.get(1)).intValue(), ((Integer)vResult.get(1)).intValue());
		Assert.assertEquals(((SerializableClass)v.get(2)).toString(), ((SerializableClass)vResult.get(2)).toString());
	}
	
	/**
	 * Test Hashtable.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testHashtable() throws Exception
	{
		DataInputStream dis;
		
		Hashtable ht = new Hashtable();
		Hashtable htResult;
		
		
		ht.put(new Integer(1), "Hugo Boss im Vector");
		ht.put("KEY", new Integer(5));
		ht.put(new Integer(2), new SerializableClass("MyClass name"));
		
		serializer.write(dos, ht);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		htResult = (Hashtable)serializer.read(dis);
		
		Assert.assertTrue(ht.size() == htResult.size());

		Assert.assertEquals(ht.get(new Integer(1)).toString(), htResult.get(new Integer(1)).toString());
		Assert.assertEquals(((Integer)ht.get("KEY")).intValue(), ((Integer)htResult.get("KEY")).intValue());
		Assert.assertEquals(((SerializableClass)ht.get(new Integer(2))).toString(), ((SerializableClass)htResult.get(new Integer(2))).toString());
	}
	
	/**
	 * Test HashMap.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testHashMap() throws Exception
	{
		DataInputStream dis;
		
		HashMap hmp = new HashMap();
		HashMap hmpResult;
		
		
		hmp.put(null, "NULL");
		hmp.put(new Integer(1), "Hugo Boss im Vector");
		hmp.put("KEY", new Integer(5));
		hmp.put(new Integer(2), new SerializableClass("MyClass name"));
		
		serializer.write(dos, hmp);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		hmpResult = (HashMap)serializer.read(dis);
		
		Assert.assertTrue(hmp.size() == hmpResult.size());
		
		Assert.assertEquals(hmp.get(new Integer(1)).toString(), hmpResult.get(new Integer(1)).toString());
		Assert.assertEquals(((Integer)hmp.get("KEY")).intValue(), ((Integer)hmpResult.get("KEY")).intValue());
		Assert.assertEquals(((SerializableClass)hmp.get(new Integer(2))).toString(), ((SerializableClass)hmpResult.get(new Integer(2))).toString());
	}
	
	/**
	 * Test Properties.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testProperties() throws Exception
	{
		DataInputStream dis;
		
		Properties prop = new Properties();
		Properties propResult;
		
		
		prop.put("KEY-1", "VALUE-1");
		prop.setProperty("KEY-2", "VALUE-2");
		prop.put(new Integer(1), "VALUE-3");
		
		serializer.write(dos, prop);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		propResult = (Properties)serializer.read(dis);
		
		Assert.assertTrue(prop.size() == propResult.size());
		
		Assert.assertEquals(prop.get("KEY-1").toString(), propResult.get("KEY-1").toString());
		Assert.assertEquals(prop.get("KEY-2").toString(), propResult.get("KEY-2").toString());
		Assert.assertEquals(prop.get(new Integer(1)).toString(), propResult.get(new Integer(1)).toString());
	}
	
	/**
	 * Test Properties.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testSerializableArray() throws Exception
	{
		DataInputStream dis;
		
		SerializableClass[] sc = new SerializableClass[] {new SerializableClass("EINS"), new SerializableClass("ZWEI")};
		SerializableClass[] scResult;
		
		
		serializer.write(dos, sc);
		
		dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
		
		scResult = (SerializableClass[])serializer.read(dis);
		
		Assert.assertTrue(sc.length == scResult.length);
		
		Assert.assertEquals(sc[0].toString(), scResult[0].toString());
		Assert.assertEquals(sc[1].toString(), scResult[1].toString());
	}
	
} 	// TestByteSerializer

/**
 * Userdefined serializable test class.
 *  
 * @author René Jahn
 */
class SerializableClass implements Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** name placeholder. */
	private String sName = "Noname";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SerializableClass</code>.
	 * 
	 * @param pName name
	 */
	public SerializableClass(String pName)
	{
		this.sName = pName;
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
		return sName;
	}
	
}	// SerializableClass
