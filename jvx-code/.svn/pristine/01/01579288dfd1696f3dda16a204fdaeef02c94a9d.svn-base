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
 * 29.12.2009 - [JR] - initFormatSymbols implemented (lazy loading of formats, performance boost for Android emulator)
 * 18.01.2009 - [JR] - read: Throwable.class - read Throwable before create instances otherwise an EOF Exception
 *                     is possible!
 *                   - FLOAT_ZERO used
 * 15.02.2010 - [JR] - read Throwable: fixed bug reading cause in exception case                   
 */
package com.sibvisions.rad.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ChoiceFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>ByteSerializer</code> converts an object state into a byte stream in such a way that 
 * the byte stream can be converted back into a copy of the object. For simple types, the conversion 
 * is not VM dependent, e.g. serialization can be done with jdk 1.3 and deserialization can be done 
 * with jdk 1.5.<p>
 * All complex types are serialized via VM serialization mode.
 * 
 * @author René Jahn
 * @see ISerializer
 * @deprecated Use {@link UniversalSerializer}
 */
public final class ByteSerializer implements ISerializer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** zero float value. */
	private static final Float FLOAT_ZERO = new Float(0);
	
	/** <code>null</code> type. */
	private static final int TYPE_NULL = 0;
	
	/** <code>Byte</code> type. */
	private static final int TYPE_BYTE = 1;
	
	/** <code>Character</code> type. */
	private static final int TYPE_CHARACTER = 2;

	/** <code>Boolean true</code> type. */
	private static final int TYPE_TRUE = 3;
	
	/** <code>Boolean false</code> type. */
	private static final int TYPE_FALSE = 4;

	/** <code>Float</code> type. */
	private static final int TYPE_FLOAT = 5;

	/** <code>Float</code> type, if the value equals 0. */
	private static final int TYPE_FLOAT_0 = 6;

	/** <code>Double</code> type. */
	private static final int TYPE_DOUBLE = 7;
	
	/** <code>Double</code> type, if the value equals 0. */
	private static final int TYPE_DOUBLE_0 = 8;

	/** <code>Short</code> type. */
	private static final int TYPE_SHORT = 9;
	
	/** <code>Short</code> type, if the value equals 0. */
	private static final int TYPE_SHORT_0 = 10;

	/** <code>Integer</code> type. */
	private static final int TYPE_INTEGER = 11;
	
	/** <code>Integer</code> type, if the value equals 0. */
	private static final int TYPE_INTEGER_0 = 12;

	/** <code>Long</code> type. */
	private static final int TYPE_LONG = 13;
	
	/** <code>Long</code> type, if the value equals 0. */
	private static final int TYPE_LONG_0 = 14;

	/** <code>java.util.Date</code> type. */
	private static final int TYPE_UTIL_DATE = 15;
	
	/** <code>java.sql.Date</code> type. */
	private static final int TYPE_SQL_DATE = 16; 

	/** <code>java.sql.Time</code> type. */
	private static final int TYPE_SQL_TIME = 17; 

	/** <code>java.sql.Timestamp</code> type. */
	private static final int TYPE_SQL_TIMESTAMP = 18;

	/** <code>java.sql.Timestamp</code> type. */
	private static final int TYPE_SQL_TIMESTAMP_NONANOS = 19;

	/** <code>StringBuffer</code> type. */
	private static final int TYPE_STRINGBUFFER = 20;

	/** <code>StringBuilder</code> type. */
	private static final int TYPE_STRINGBUILDER = 21;
	
	/** <code>Object</code> type. */
	private static final int TYPE_OBJECT = 22;
	
	/** <code>Throwable</code> type. */
	private static final int TYPE_THROWABLE = 23;
	
	/** <code>Reflective</code> type. (create instances reflective) */
	private static final int TYPE_REFLECTIVE = 24;

	/** min. byte value of a type with variable length. */
	private static final int TYPE_VARIABLE_MIN = 37;

	/** max. byte value of a type with variable length. */
	private static final int TYPE_VARIABLE_MAX = 59;

	/** <code>byte[]</code> type. */
	private static final int TYPE_VAR_PRIMITIVE_BYTE = 0;

	/** <code>char[]</code> type. */
	private static final int TYPE_VAR_PRIMITIVE_CHARACTER = 1;

	/** <code>boolean[]</code> type. */
	private static final int TYPE_VAR_PRIMITIVE_BOOLEAN = 2;
	
	/** <code>float[]</code> type. */
	private static final int TYPE_VAR_PRIMITIVE_FLOAT = 3;

	/** <code>double[]</code> type. */
	private static final int TYPE_VAR_PRIMITIVE_DOUBLE = 4;
	
	/** <code>short[]</code> type. */
	private static final int TYPE_VAR_PRIMITIVE_SHORT = 5;

	/** <code>int[]</code> type. */
	private static final int TYPE_VAR_PRIMITIVE_INT = 6;

	/** <code>long[]</code> type. */
	private static final int TYPE_VAR_PRIMITIVE_LONG = 7;

	/** <code>Integer[]</code> type. */
	private static final int TYPE_VAR_INTEGER = 8;

	/** <code>Long[]</code> type. */
	private static final int TYPE_VAR_LONG = 9;

	/** <code>Object[]</code> type. */
	private static final int TYPE_VAR_OBJECT = 10;

	/** <code>Object[][]</code> type. */
	private static final int TYPE_VAR_OBJECT_2DIM = 11;

	/** <code>String[]</code> type. */
	private static final int TYPE_VAR_STRING = 12;

	/** <code>String[][]</code> type. */
	private static final int TYPE_VAR_STRING_2DIM = 13;

	/** <code>ArrayList</code> type. */
	private static final int TYPE_VAR_ARRAYLIST = 14;

	/** <code>LinkedList</code> type. */
	private static final int TYPE_VAR_ARRAYUTIL = 15;

	/** <code>Vector</code> type. */
	private static final int TYPE_VAR_VECTOR = 16;

	/** <code>Hashtable</code> type. */
	private static final int TYPE_VAR_HASHTABLE = 17;

	/** <code>HashMap</code> type. */
	private static final int TYPE_VAR_HASHMAP = 18;

	/** <code>StackTraceElement</code> type. */
	private static final int TYPE_VAR_STACKTRACEELEMENT = 19;

	/** Unknown Object array type. */
	private static final int TYPE_VAR_REFLECTIVE = 20;

	/** <code>Properties</code> type. The length will be stored in a new byte. */
	private static final int TYPE_VAR_PROPERTIES = 21;

	/** <code>Object[][][]</code> type. The length will be stored in a new byte. */
	private static final int TYPE_VAR_OBJECT_3DIM = 22;
	
	/** <code>String[][][]</code> type. The length will be stored in a new byte. */
	private static final int TYPE_VAR_STRING_3DIM = 23;
	
	/** min. byte value of <code>BigInteger</code> type. */
	private static final int TYPE_BIGINTEGER_MIN = 59;
	
	/** max. byte value of <code>BigInteger</code> type. */
	private static final int TYPE_BIGINTEGER_MAX = 69;

	/** min. byte value of <code>BigDecimal</code> type. */
	private static final int TYPE_BIGDECIMAL_MIN = 79;

	/** max. byte value of <code>BigDecimal</code> type. */
	private static final int TYPE_BIGDECIMAL_MAX = 89;

	/** min. byte value of <code>BigDecimal</code> type, if scale equals 0. */
	private static final int TYPE_BIGDECIMAL_NOSCALE_MIN = 89;

	/** max. byte value of <code>BigDecimal</code> type, if scale equals 0. */
	private static final int TYPE_BIGDECIMAL_NOSCALE_MAX = 99;

	/** min. byte value of <code>String</code> type. */
	private static final int TYPE_STRING_MIN = 99;

	/** max. byte value of <code>String</code> type. */
	private static final int TYPE_STRING_MAX = 256;

	/** the default decimal format symbols of the system for the current default locale. */
	private static final DecimalFormatSymbols SYSTEM_DECSYMBOLS = new DecimalFormatSymbols();

	/** the default date format symbols of the system for the current default locale. */
	private static final DateFormatSymbols SYSTEM_DATESYMBOLS = new DateFormatSymbols();

	
	/** static calendar for writing dates/timestamps. */
	private Calendar calDate = Calendar.getInstance(); 

	/** cached decimal format symbols for special locales. */
	private static HashMap<DecimalFormatSymbols, Locale> hmpDecFormatSymbols = null;

	/** cached date format symbols for special locales. */
	private static HashMap<DateFormatSymbols, Locale> hmpDateFormatSymbols = null;
	
	/** whether object streams are enabled. */
	private boolean bObjectStreamEnabled = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ByteSerializer</code>.
	 */
	public ByteSerializer()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public final void write(DataOutputStream pOut, Object pObject) throws Exception
	{
		if (pObject == null)
		{
			pOut.write(ByteSerializer.TYPE_NULL);
		}
		else
		{
			Class clsType = pObject.getClass();
			
			//Class dependent serialization
			if (clsType == String.class)
			{
				byte[] byContent = ((String)pObject).getBytes("UTF8");
				
				writeCalculatedType(pOut, byContent.length, ByteSerializer.TYPE_STRING_MIN, ByteSerializer.TYPE_STRING_MAX);
				pOut.write(byContent);
			}
			else if (clsType == String[].class)
			{
				String[] sObject = (String[])pObject;
				
				writeCalculatedType(pOut, sObject.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_STRING);

				for (int i = 0, anz = sObject.length; i < anz; i++)
				{
					write(pOut, sObject[i]);
				}
			}
			else if (clsType == String[][].class)
			{
				String[][] sArray2 = (String[][])pObject;
				
				writeCalculatedType(pOut, sArray2.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_STRING_2DIM);
				
				for (int i = 0, anz = sArray2.length; i < anz; i++)
				{
					write(pOut, sArray2[i]);
				}
			}
			else if (clsType == String[][][].class)
			{
				String[][][] sArray3 = (String[][][])pObject;
				
				writeCalculatedType(pOut, sArray3.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_STRING_3DIM);
				
				for (int i = 0, anz = sArray3.length; i < anz; i++)
				{
					write(pOut, sArray3[i]);
				}
			}
			else if (clsType == ArrayList.class)
			{
				ArrayList alArray = (ArrayList)pObject;
				
				writeCalculatedType(pOut, alArray.size(), ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_ARRAYLIST);
				
				for (int i = 0, anz = alArray.size(); i < anz; i++)
				{
					write(pOut, alArray.get(i));
				}
			}
			else if (clsType == ArrayUtil.class)
			{
				ArrayUtil auArray = (ArrayUtil)pObject;
				
				writeCalculatedType(pOut, auArray.size(), ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_ARRAYUTIL);
				
				for (int i = 0, anz = auArray.size(); i < anz; i++)
				{
					write(pOut, auArray.get(i));
				}
			}
			else if (clsType == Vector.class)
			{
				Vector vArray = (Vector)pObject;
				
				writeCalculatedType(pOut, vArray.size(), ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_VECTOR);
				
				for (int i = 0, anz = vArray.size(); i < anz; i++)
				{
					write(pOut, vArray.get(i));
				}
			}
			else if (clsType == Hashtable.class)
			{
				Hashtable htArray = (Hashtable)pObject;
				
				writeCalculatedType(pOut, htArray.size(), ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_HASHTABLE);
				
				for (Enumeration en = htArray.keys(); en.hasMoreElements();)
				{
					Object oKey = en.nextElement();
					
					write(pOut, oKey);
					write(pOut, htArray.get(oKey));
				}
			}
			else if (clsType == HashMap.class)
			{
				HashMap hmpArray = (HashMap)pObject;
				
				writeCalculatedType(pOut, hmpArray.size(), ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_HASHMAP);
				
				for (Iterator it = hmpArray.entrySet().iterator(); it.hasNext();)
				{
					Entry eMapping = (Entry)it.next();
					
					write(pOut, eMapping.getKey());
					write(pOut, eMapping.getValue());
				}
			}
			else if (clsType == Object[].class)
			{
				Object[] oArray = (Object[])pObject;
				
				writeCalculatedType(pOut, oArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_OBJECT);
				
				for (int i = 0, anz = oArray.length; i < anz; i++)
				{
					write(pOut, oArray[i]);
				}
			}
			else if (clsType == Object[][].class)
			{
				Object[][] oArray2 = (Object[][])pObject;
				
				writeCalculatedType(pOut, oArray2.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_OBJECT_2DIM);
				
				for (int i = 0, anz = oArray2.length; i < anz; i++)
				{
					write(pOut, oArray2[i]);
				}
			}
			else if (clsType == Object[][][].class)
			{
				Object[][][] oArray3 = (Object[][][])pObject;
				
				writeCalculatedType(pOut, oArray3.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_OBJECT_3DIM);
				
				for (int i = 0, anz = oArray3.length; i < anz; i++)
				{
					write(pOut, oArray3[i]);
				}
			}
			else if (clsType == Byte.class)
			{
				pOut.write(ByteSerializer.TYPE_BYTE);
				pOut.writeByte(((Byte)pObject).intValue());
			}
			else if (clsType == Character.class)
			{
				pOut.write(ByteSerializer.TYPE_CHARACTER);
				pOut.writeChar(((Character)pObject).charValue());
			}
			else if (clsType == Boolean.class)
			{
				if (((Boolean)pObject).booleanValue())
				{
					pOut.write(ByteSerializer.TYPE_TRUE);
				}
				else
				{
					pOut.write(ByteSerializer.TYPE_FALSE);
				}
			}
			else if (clsType == Float.class)
			{
				if (((Float)pObject).floatValue() == 0)
				{
					pOut.write(ByteSerializer.TYPE_FLOAT_0);
				}
				else
				{
					pOut.write(ByteSerializer.TYPE_FLOAT);
					pOut.writeFloat(((Float)pObject).floatValue());
				}
			}
			else if (clsType == Double.class)
			{
				if (((Double)pObject).doubleValue() == 0)
				{
					pOut.write(ByteSerializer.TYPE_DOUBLE_0);
				}
				else
				{
					pOut.write(ByteSerializer.TYPE_DOUBLE);
					pOut.writeDouble(((Double)pObject).doubleValue());
				}
			}
			else if (clsType == Short.class)
			{
				if (((Short)pObject).shortValue() == 0)
				{
					pOut.write(ByteSerializer.TYPE_SHORT_0);
				}
				else
				{
					pOut.write(ByteSerializer.TYPE_SHORT);
					pOut.writeShort(((Short)pObject).shortValue());
				}
			}
			else if (clsType == Integer.class)
			{
				if (((Integer)pObject).intValue() == 0)
				{
					pOut.write(ByteSerializer.TYPE_INTEGER_0);
				}
				else
				{
					pOut.write(ByteSerializer.TYPE_INTEGER);
					pOut.writeInt(((Integer)pObject).intValue());
				}
			}
			else if (clsType == Long.class)
			{
				if (((Long)pObject).longValue() == 0)
				{
					pOut.write(ByteSerializer.TYPE_LONG_0);
				}
				else
				{
					pOut.write(ByteSerializer.TYPE_LONG);
					pOut.writeLong(((Long)pObject).longValue());
				}
			}
			else if (clsType == java.util.Date.class)
			{
				pOut.write(ByteSerializer.TYPE_UTIL_DATE);
				pOut.writeLong(((java.util.Date)pObject).getTime());
			}
			else if (clsType == java.sql.Date.class)
			{
				pOut.write(ByteSerializer.TYPE_SQL_DATE);
				pOut.writeLong(((java.sql.Date)pObject).getTime());
			}
			else if (clsType == java.sql.Time.class)
			{
				pOut.write(ByteSerializer.TYPE_SQL_TIME);
				pOut.writeLong(((java.sql.Time)pObject).getTime());
			}
			else if (clsType == java.sql.Timestamp.class)
			{
				int iNanos = ((java.sql.Timestamp)pObject).getNanos();

				if (iNanos > 0)
				{
					pOut.write(ByteSerializer.TYPE_SQL_TIMESTAMP);
				}
				else
				{
					pOut.write(ByteSerializer.TYPE_SQL_TIMESTAMP_NONANOS);
				}
				
				synchronized(calDate)
				{
					calDate.setTime((java.sql.Timestamp)pObject);
					
		    	    pOut.write(calDate.get(Calendar.DATE));
		    	    pOut.write(calDate.get(Calendar.MONTH));
		    	    pOut.writeShort(calDate.get(Calendar.YEAR));
		    	    pOut.write(calDate.get(Calendar.HOUR_OF_DAY));
		    	    pOut.write(calDate.get(Calendar.MINUTE));
		    	    pOut.write(calDate.get(Calendar.SECOND));
		    	    
		    	    if (iNanos > 0)
		    	    {
		    	    	pOut.writeInt(iNanos);
		    	    }
				}
			}
			else if (clsType == StringBuffer.class)
			{
				pOut.write(ByteSerializer.TYPE_STRINGBUFFER);
				write(pOut, ((StringBuffer)pObject).toString());
			}
			else if (clsType == StringBuilder.class)
			{
				pOut.write(ByteSerializer.TYPE_STRINGBUILDER);
				write(pOut, ((StringBuilder)pObject).toString());
			}
			else if (clsType == BigInteger.class)
			{
				byte[] byContent = ((BigInteger)pObject).toByteArray();
				
				writeCalculatedType(pOut, byContent.length, ByteSerializer.TYPE_BIGINTEGER_MIN, ByteSerializer.TYPE_BIGINTEGER_MAX);
				pOut.write(byContent);
			}
			else if (clsType == BigDecimal.class)
			{
				BigDecimal bdObject = (BigDecimal)pObject;
				
				int iScale = bdObject.scale();
				
				byte[] byContent = bdObject.movePointRight(iScale).toBigInteger().toByteArray();
				
				if (iScale == 0)
				{
					writeCalculatedType(pOut, byContent.length, ByteSerializer.TYPE_BIGDECIMAL_NOSCALE_MIN, ByteSerializer.TYPE_BIGDECIMAL_NOSCALE_MAX);
					pOut.write(byContent);
				}
				else
				{
					writeCalculatedType(pOut, byContent.length, ByteSerializer.TYPE_BIGDECIMAL_MIN, ByteSerializer.TYPE_BIGDECIMAL_MAX);
					pOut.write(byContent);
					pOut.writeShort(iScale);
				}
			}
			else if (pObject instanceof Throwable)
			{
				Throwable th = (Throwable)pObject;
				
				pOut.write(ByteSerializer.TYPE_THROWABLE);
				write(pOut, clsType.getName());
				write(pOut, th.getMessage());
				write(pOut, th.getStackTrace());

				//null marks the end of the chain (null uses 1 byte. That is just as good as serialize
				//the exception as "Array")
				write(pOut, th.getCause());
			}
			else if (clsType == byte[].class)
			{
				byte[] byArray = (byte[])pObject;
				
				writeCalculatedType(pOut, byArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PRIMITIVE_BYTE);
				pOut.write(byArray);
			}
			else if (clsType == char[].class)
			{
				char[] chArray = (char[])pObject; 
				
				writeCalculatedType(pOut, chArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PRIMITIVE_CHARACTER);
				
				for (int i = 0, anz = chArray.length; i < anz; i++)
				{
					pOut.writeChar(chArray[i]);
				}
			}
			else if (clsType == boolean[].class)
			{
				boolean[] bArray = (boolean[])pObject; 
				
				writeCalculatedType(pOut, bArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PRIMITIVE_BOOLEAN);
				
				for (int i = 0, anz = bArray.length; i < anz; i++)
				{
					pOut.writeBoolean(bArray[i]);
				}
			}
			else if (clsType == float[].class)
			{
				float[] fArray = (float[])pObject; 
				
				writeCalculatedType(pOut, fArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PRIMITIVE_FLOAT);
				
				for (int i = 0, anz = fArray.length; i < anz; i++)
				{
					pOut.writeFloat(fArray[i]);
				}
			}
			else if (clsType == double[].class)
			{
				double[] dArray = (double[])pObject; 
				
				writeCalculatedType(pOut, dArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PRIMITIVE_DOUBLE);
				
				for (int i = 0, anz = dArray.length; i < anz; i++)
				{
					pOut.writeDouble(dArray[i]);
				}
			}
			else if (clsType == short[].class)
			{
				short[] shArray = (short[])pObject; 
				
				writeCalculatedType(pOut, shArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PRIMITIVE_SHORT);
				
				for (int i = 0, anz = shArray.length; i < anz; i++)
				{
					pOut.writeShort(shArray[i]);
				}
			}
			else if (clsType == int[].class)
			{
				int[] iArray = (int[])pObject; 
				
				writeCalculatedType(pOut, iArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PRIMITIVE_INT);
				
				for (int i = 0, anz = iArray.length; i < anz; i++)
				{
					pOut.writeInt(iArray[i]);
				}
			}
			else if (clsType == long[].class)
			{
				long[] lArray = (long[])pObject; 
				
				writeCalculatedType(pOut, lArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PRIMITIVE_LONG);
				
				for (int i = 0, anz = lArray.length; i < anz; i++)
				{
					pOut.writeLong(lArray[i]);
				}
			}
			else if (clsType == Integer[].class)
			{
				Integer[] iArray = (Integer[])pObject; 
				
				writeCalculatedType(pOut, iArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_INTEGER);
				
				for (int i = 0, anz = iArray.length; i < anz; i++)
				{
					pOut.writeInt(iArray[i].intValue());
				}
			}
			else if (clsType == Long[].class)
			{
				Long[] lArray = (Long[])pObject; 
				
				writeCalculatedType(pOut, lArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_LONG);
				
				for (int i = 0, anz = lArray.length; i < anz; i++)
				{
					pOut.writeLong(lArray[i].longValue());
				}
			}
			else if (clsType == StackTraceElement[].class)
			{
				StackTraceElement[] stPath = (StackTraceElement[])pObject;
			
				writeCalculatedType(pOut, stPath.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_STACKTRACEELEMENT);
				
				for (int i = 0, anz = stPath.length; i < anz; i++)
				{
					write(pOut, stPath[i].getClassName());
					write(pOut, stPath[i].getMethodName());
					write(pOut, stPath[i].getFileName());
					write(pOut, Integer.valueOf(stPath[i].getLineNumber()));
				}
			}
			else if (clsType == Properties.class)
			{
				Properties prop = (Properties)pObject;
				
				writeCalculatedType(pOut, prop.size(), ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_PROPERTIES);
				
				for (Enumeration en = prop.keys(); en.hasMoreElements();)
				{
					Object oKey = en.nextElement();
					
					write(pOut, oKey);
					write(pOut, prop.get(oKey));
				}
			}
			else if (pObject instanceof Object[])
			{
				Object[] oArray = (Object[])pObject;
				
				writeCalculatedType(pOut, oArray.length, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
				pOut.write(ByteSerializer.TYPE_VAR_REFLECTIVE);
				
				write(pOut, clsType.getComponentType().getName());
				
				for (int i = 0, anz = oArray.length; i < anz; i++)
				{
					write(pOut, oArray[i]);
				}
			}
			else
			{
				Object[] oArray = null;
				
				//Objects which have problems with deserialization through differend JDKs, will not
				//serialized with standard object serialization of the JDK!
				//Transfer the information which are important to create a new instance of the same
				//object on the remote side! The serializer will create a new instance based on the
				//transfered information!
				
				if (pObject instanceof MessageFormat)
				{
					oArray = new Object[] {((MessageFormat)pObject).toPattern()};
				}
				else if (pObject instanceof ChoiceFormat)
				{
					oArray = new Object[] {((ChoiceFormat)pObject).toPattern()};
				}
				else if (pObject instanceof DecimalFormat)
				{
					DecimalFormat dfParam = (DecimalFormat)pObject;
					
					oArray = new Object[] {dfParam.toPattern(), dfParam.getDecimalFormatSymbols()};
				}
				else if (pObject instanceof SimpleDateFormat)
				{
					SimpleDateFormat sdfParam = (SimpleDateFormat)pObject;
					
					oArray = new Object[] {sdfParam.toPattern(), sdfParam.getDateFormatSymbols()};
				}
				else if (clsType == DecimalFormatSymbols.class)
				{
					if (hmpDecFormatSymbols == null)
					{
						initFormatSymbols();
					}
						
					oArray = new Object[] {hmpDecFormatSymbols.get((DecimalFormatSymbols)pObject)};
					
					if (ByteSerializer.SYSTEM_DECSYMBOLS.equals((DecimalFormatSymbols)pObject) || oArray[0] == null)
					{
						oArray[0] = Locale.getDefault();
					}
				}
				else if (clsType == DateFormatSymbols.class)
				{
					if (hmpDateFormatSymbols == null)
					{
						initFormatSymbols();
					}
					
					oArray = new Object[] {hmpDateFormatSymbols.get((DateFormatSymbols)pObject)};
					
					if (ByteSerializer.SYSTEM_DATESYMBOLS.equals((DateFormatSymbols)pObject) || oArray[0] == null)
					{
						oArray[0] = Locale.getDefault();
					}
				}
					
		        if (oArray != null)
		        {
		        	//A new instance, from the remote side, will be created based on following options
		        	pOut.write(ByteSerializer.TYPE_REFLECTIVE);
		        	
		        	write(pOut, clsType.getName());
		        	write(pOut, oArray);
		        }
		        else
		        {
		        	if (bObjectStreamEnabled)
		        	{
			        	//Use JDK serialization (Standard serialization)
			        	pOut.write(ByteSerializer.TYPE_OBJECT);
		        	
			        	ObjectOutputStream oos = new ObjectOutputStream(pOut);
			        	oos.writeObject(pObject);
		        	}
		        	else
		        	{
		        		throw new SecurityException("It's not allowed to use Object types!");		        		
		        	}
		        }
			}
		}
	}

	/**
	 * Reads in an object from a serialized <code>DataInputStream</code>.
	 * 
	 * @param pIn serialized <code>DataInputStream</code>
	 * @return deserialized object
	 * @throws Exception if deserialization fails or if the object type is {@link #TYPE_OBJECT} and the object cannot be read
	 */
	public final Object read(DataInputStream pIn) throws Exception 
	{
		int iType = pIn.read();
		
			
		if (iType == -1)
		{
			throw new IOException("Type definition was not found");
		}
		else if (iType >= ByteSerializer.TYPE_STRING_MIN && iType < ByteSerializer.TYPE_STRING_MAX)
		{
			int iLength = readCalculatedType(pIn, iType, ByteSerializer.TYPE_STRING_MIN, ByteSerializer.TYPE_STRING_MAX);
			
			byte[] byContent = new byte[iLength];
			pIn.readFully(byContent);
			
			return new String(byContent, "UTF8");
		}
		else if (iType == ByteSerializer.TYPE_NULL)
		{
			return null;
		}
		else if (iType == ByteSerializer.TYPE_BYTE)
		{
			return Byte.valueOf(pIn.readByte());
		}
		else if (iType == ByteSerializer.TYPE_CHARACTER)
		{
			return Character.valueOf(pIn.readChar());
		}
		else if (iType == ByteSerializer.TYPE_TRUE)
		{
			return Boolean.valueOf(true);
		}
		else if (iType == ByteSerializer.TYPE_FALSE)
		{
			return Boolean.valueOf(false);
		}
		else if (iType == ByteSerializer.TYPE_FLOAT_0)
		{
			return FLOAT_ZERO;
		}
		else if (iType == ByteSerializer.TYPE_FLOAT)
		{
			return new Float(pIn.readFloat());
		}
		else if (iType == ByteSerializer.TYPE_DOUBLE_0)
		{
			return Double.valueOf(0);
		}
		else if (iType == ByteSerializer.TYPE_DOUBLE)
		{
			return Double.valueOf(pIn.readDouble());
		}
		else if (iType == ByteSerializer.TYPE_SHORT_0)
		{
			return Short.valueOf((short)0);
		}
		else if (iType == ByteSerializer.TYPE_SHORT)
		{
			return Short.valueOf(pIn.readShort());
		}
		else if (iType == ByteSerializer.TYPE_INTEGER_0)
		{
			return Integer.valueOf(0);
		}
		else if (iType == ByteSerializer.TYPE_INTEGER)
		{
			return Integer.valueOf(pIn.readInt());
		}
		else if (iType == ByteSerializer.TYPE_LONG_0)
		{
			return Long.valueOf(0);
		}
		else if (iType == ByteSerializer.TYPE_LONG)
		{
			return Long.valueOf(pIn.readLong());
		}
		else if (iType == ByteSerializer.TYPE_UTIL_DATE)
		{
			return new java.util.Date(pIn.readLong());
		}
		else if (iType == ByteSerializer.TYPE_SQL_DATE)
		{
			return new java.sql.Date(pIn.readLong());
		}
		else if (iType == ByteSerializer.TYPE_SQL_TIME)
		{
			return new java.sql.Time(pIn.readLong());
		}
		else if (iType == ByteSerializer.TYPE_SQL_TIMESTAMP || iType == ByteSerializer.TYPE_SQL_TIMESTAMP_NONANOS)
		{				
			java.sql.Timestamp tsRead;
			
			synchronized (calDate)
			{
				calDate.set(Calendar.DATE, pIn.read());
				calDate.set(Calendar.MONTH, pIn.read());
				calDate.set(Calendar.YEAR, pIn.readShort());
				calDate.set(Calendar.HOUR_OF_DAY, pIn.read());
				calDate.set(Calendar.MINUTE, pIn.read());
				calDate.set(Calendar.SECOND, pIn.read());
				calDate.set(Calendar.MILLISECOND, 0);
				
				tsRead = new java.sql.Timestamp(calDate.getTime().getTime());
				
				if (iType == ByteSerializer.TYPE_SQL_TIMESTAMP)
				{
					tsRead.setNanos(pIn.readInt());
				}
			}
				
			return tsRead;
		}
		else if (iType == ByteSerializer.TYPE_STRINGBUFFER)
		{
			return new StringBuffer((String)read(pIn));
		}
		else if (iType == ByteSerializer.TYPE_STRINGBUILDER)
		{
			return new StringBuilder((String)read(pIn));
		}
		else if (iType >= ByteSerializer.TYPE_BIGINTEGER_MIN && iType < ByteSerializer.TYPE_BIGINTEGER_MAX)
		{
			int iLength = readCalculatedType(pIn, iType, ByteSerializer.TYPE_BIGINTEGER_MIN, ByteSerializer.TYPE_BIGINTEGER_MAX);
			
			byte[] byContent = new byte[iLength];
			pIn.readFully(byContent);
			
			return new BigInteger(byContent);
		}
		else if (iType >= ByteSerializer.TYPE_BIGDECIMAL_MIN && iType < ByteSerializer.TYPE_BIGDECIMAL_MAX)
		{
			int iLength = readCalculatedType(pIn, iType, ByteSerializer.TYPE_BIGDECIMAL_MIN, ByteSerializer.TYPE_BIGDECIMAL_MAX);
			
			byte[] byContent = new byte[iLength];
			pIn.readFully(byContent);
			
			return new BigDecimal(new BigInteger(byContent), pIn.readShort());
		}
		else if (iType >= ByteSerializer.TYPE_BIGDECIMAL_NOSCALE_MIN && iType < ByteSerializer.TYPE_BIGDECIMAL_NOSCALE_MAX)
		{
			int iLength = readCalculatedType(pIn, iType, ByteSerializer.TYPE_BIGDECIMAL_NOSCALE_MIN, ByteSerializer.TYPE_BIGDECIMAL_NOSCALE_MAX);
			
			byte[] byContent = new byte[iLength];
			pIn.readFully(byContent);
			
			return new BigDecimal(new BigInteger(byContent));
		}
		else if (iType == ByteSerializer.TYPE_REFLECTIVE)
		{
			String sClassName = (String)read(pIn);
			
			Object[] oParams = (Object[])read(pIn);

			Constructor[] conClass = Class.forName(sClassName).getConstructors();
			
			//Call the appropriate constructor to create a new instance
			for (int i = 0, anz = conClass.length; i < anz; i++)
			{
				try
				{
					if (oParams.length == conClass[i].getParameterTypes().length)
					{
						return conClass[i].newInstance(oParams);
					}
				}
				catch (Throwable th)
				{
					//try the next constructor
				}
			}

			throw new IOException("Reflective creation of '" + sClassName + "' failed!");
		}
		else if (iType == ByteSerializer.TYPE_OBJECT)
		{
        	if (bObjectStreamEnabled)
        	{
				ObjectInputStream ois = new ObjectInputStream(pIn);
				
				return ois.readObject();
        	}
        	
        	throw new SecurityException("It's not allowed to use Object types!");
		}
		else if (iType == ByteSerializer.TYPE_THROWABLE)
		{
			String sClassName = (String)read(pIn);
			String sMessage = (String)read(pIn);
			
			StackTraceElement[] stPath = (StackTraceElement[])read(pIn);
			Throwable throwable = (Throwable)read(pIn);

			try
			{
				Throwable thObject = (Throwable)Class.forName(sClassName).getConstructor(new Class[] {String.class, Throwable.class}).newInstance(sMessage, throwable);
				
				thObject.setStackTrace(stPath);

				return thObject;
			}
			catch (Throwable th)
			{
				try
				{
					//NoSuchMethodException e.g. no constructor with parameter Throwable!
					Throwable thObject = (Throwable)Class.forName(sClassName).getConstructor(new Class[] {String.class}).newInstance(sMessage);
					
					thObject.setStackTrace(stPath);

					return thObject;
				}
				catch (Throwable thr)
				{
					Exception exObject = new Exception(sClassName + ": " + sMessage, throwable);
					
					exObject.setStackTrace(stPath);
					
					return exObject;
				}
			}
		}
		else if (iType >= ByteSerializer.TYPE_VARIABLE_MIN && iType < ByteSerializer.TYPE_VARIABLE_MAX)
		{
			int iLength = readCalculatedType(pIn, iType, ByteSerializer.TYPE_VARIABLE_MIN, ByteSerializer.TYPE_VARIABLE_MAX);
			
			iType = pIn.read();
			
			if (iType == ByteSerializer.TYPE_VAR_STRING)
			{
				String[] sArray = new String[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					sArray[i] = (String)read(pIn);
				}
				
				return sArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_STRING_2DIM)
			{
				String[][] sArray2 = new String[iLength][];
				
				for (int i = 0; i < iLength; i++)
				{
					sArray2[i] = (String[])read(pIn);
				}
				
				return sArray2;
			}
			else if (iType == ByteSerializer.TYPE_VAR_STRING_3DIM)
			{
				String[][][] sArray3 = new String[iLength][][];
				
				for (int i = 0; i < iLength; i++)
				{
					sArray3[i] = (String[][])read(pIn);
				}
				
				return sArray3;
			}
			else if (iType == ByteSerializer.TYPE_VAR_OBJECT)
			{
				Object[] oArray = new Object[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					oArray[i] = read(pIn);
				}
				
				return oArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_OBJECT_2DIM)
			{
				Object[][] oArray2 = new Object[iLength][];
				
				for (int i = 0; i < iLength; i++)
				{
					oArray2[i] = (Object[])read(pIn);
				}
				
				return oArray2;
			}
			else if (iType == ByteSerializer.TYPE_VAR_OBJECT_3DIM)
			{
				Object[][][] oArray3 = new Object[iLength][][];
				
				for (int i = 0; i < iLength; i++)
				{
					oArray3[i] = (Object[][])read(pIn);
				}
				
				return oArray3;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PRIMITIVE_BYTE)
			{
				byte[] byContent = new byte[iLength];
				
				pIn.readFully(byContent);
				
				return byContent;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PRIMITIVE_BOOLEAN)
			{
				boolean[] bArray = new boolean[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					bArray[i] = pIn.readBoolean();
				}
				
				return bArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PRIMITIVE_CHARACTER)
			{
				char[] chArray = new char[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					chArray[i] = pIn.readChar();
				}
				
				return chArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PRIMITIVE_FLOAT)
			{
				float[] fArray = new float[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					fArray[i] = pIn.readFloat();
				}
				
				return fArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PRIMITIVE_DOUBLE)
			{
				double[] dArray = new double[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					dArray[i] = pIn.readDouble();
				}
				
				return dArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PRIMITIVE_SHORT)
			{
				short[] shArray = new short[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					shArray[i] = pIn.readShort();
				}

				return shArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PRIMITIVE_INT)
			{
				int[] iArray = new int[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					iArray[i] = pIn.readInt();
				}

				return iArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PRIMITIVE_LONG)
			{
				long[] lArray = new long[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					lArray[i] = pIn.readLong();
				}

				return lArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_INTEGER)
			{
				Integer[] iArray = new Integer[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					iArray[i] = Integer.valueOf(pIn.readInt());
				}

				return iArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_LONG)
			{
				Long[] lArray = new Long[iLength];
				
				for (int i = 0; i < iLength; i++)
				{
					lArray[i] = Long.valueOf(pIn.readLong());
				}

				return lArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_STACKTRACEELEMENT)
			{
				StackTraceElement[] stPath = new StackTraceElement[iLength];
				
				for (int i = 0, anz = stPath.length; i < anz; i++)
				{
					stPath[i] = new StackTraceElement
					(
						(String)read(pIn),
						(String)read(pIn),
						(String)read(pIn),
						((Integer)read(pIn)).intValue()
					);
				}
				
				return stPath;
			}
			else if (iType == ByteSerializer.TYPE_VAR_ARRAYUTIL)
			{
				ArrayUtil auArray = new ArrayUtil();
				
				for (int i = 0; i < iLength; i++)
				{
					auArray.add(read(pIn));
				}
				
				return auArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_ARRAYLIST)
			{
				ArrayList alArray = new ArrayList(iLength);
				
				for (int i = 0; i < iLength; i++)
				{
					alArray.add(read(pIn));
				}
				
				return alArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_VECTOR)
			{
				Vector vArray = new Vector(iLength);
				
				for (int i = 0; i < iLength; i++)
				{
					vArray.add(read(pIn));
				}
				
				return vArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_HASHTABLE)
			{
				Hashtable htArray = new Hashtable(iLength);
				
				for (int i = 0; i < iLength; i++)
				{
					htArray.put(read(pIn), read(pIn));
				}
				
				return htArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_HASHMAP)
			{
				HashMap hmpArray = new HashMap(iLength);
				
				for (int i = 0; i < iLength; i++)
				{
					hmpArray.put(read(pIn), read(pIn));
				}
				
				return hmpArray;
			}
			else if (iType == ByteSerializer.TYPE_VAR_PROPERTIES)
			{
				Properties prop = new Properties();
				
				for (int i = 0; i < iLength; i++)
				{
					prop.put(read(pIn), read(pIn));
				}
				
				return prop;
			}
			else if (iType == ByteSerializer.TYPE_VAR_REFLECTIVE)
			{
				String sClassType = (String)read(pIn);
				
				Object oArray = Array.newInstance(Class.forName(sClassType), iLength);
				
				for (int i = 0; i < iLength; i++)
				{
					Array.set(oArray, i, read(pIn));
				}
				
		        return oArray;				
			}
			else
			{
				throw new IOException("Unknown subtype: " + iType);
			}
		}
		else
		{
			throw new IOException("Unknown object type: " + iType);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Writes the type definition which includes the length of the content.
	 * 
	 * @param pOut output stream
	 * @param pLength length of the content
	 * @param pMin start value for the type definition
	 * @param pMax max value for the type definition
	 * @throws IOException if it is not possible to write the calculated type definition to the stream
	 */
	private final void writeCalculatedType(DataOutputStream pOut, int pLength, int pMin, int pMax) throws IOException 
	{
		int iMinMaxDiff = pMax - pMin;
		
		
		if (pLength < iMinMaxDiff - 3)
		{
			pOut.write(pMin + pLength);
		}
		else if (pLength < 253 + iMinMaxDiff)
		{
			pOut.write(pMin + iMinMaxDiff - 3);
			pOut.write(pLength - iMinMaxDiff + 3);
		}
		else if (pLength < 65789 + iMinMaxDiff)
		{
			pOut.write(pMin + iMinMaxDiff - 2);
			pOut.writeShort(pLength - 253 - iMinMaxDiff);
		}
		else
		{
			pOut.write(pMin + iMinMaxDiff - 1);
			pOut.writeInt(pLength);
		}
	}		
	
	/**
	 * Calculates the content length from the type identifier.
	 * 
	 * @param pIn input stream
	 * @param pCalculatedType type definition with included content length
	 * @param pMin start value for the type definition
	 * @param pMax max value for the type definition
	 * @return content length
	 * @throws IOException if the content length cannot be calculated
	 */
	private final int readCalculatedType(DataInputStream pIn, int pCalculatedType, int pMin, int pMax) throws IOException 
	{
		int iMinMaxDiff = pMax - pMin;
		
		
		if (pCalculatedType < pMin + iMinMaxDiff - 3)
		{
			return pCalculatedType - pMin;
		}
		else if (pCalculatedType == pMin + iMinMaxDiff - 3)
		{
			return pIn.readUnsignedByte() + iMinMaxDiff - 3;
		}
		else if (pCalculatedType == pMin + iMinMaxDiff - 2)
		{
			return pIn.readUnsignedShort() + 253 + iMinMaxDiff;
		}
		else
		{
			return pIn.readInt();
		}
	}	

	/**
	 * Initializes the format symbols for specific Locales.
	 */
	private static void initFormatSymbols()
	{
		//only use the most important Locales (NOT ALL available)
		Locale[] loc = new Locale[]
		{
			Locale.KOREA, Locale.KOREAN, Locale.JAPAN, Locale.JAPANESE, Locale.CHINA, Locale.CHINESE, 
			Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.PRC, Locale.TAIWAN,
			Locale.UK, Locale.US, Locale.ENGLISH, Locale.CANADA, Locale.CANADA_FRENCH,
			Locale.FRANCE, Locale.FRENCH, Locale.ITALIAN, Locale.ITALY, Locale.GERMANY, 
			Locale.GERMAN
		};
		
		hmpDateFormatSymbols = new HashMap<DateFormatSymbols, Locale>();
		hmpDecFormatSymbols = new HashMap<DecimalFormatSymbols, Locale>();
		
		//Prepare cache!
	    for (int i = 0, anz = loc.length; i < anz; i++)
	    {
	    	hmpDateFormatSymbols.put(new DateFormatSymbols(loc[i]), loc[i]);
	    	hmpDecFormatSymbols.put(new DecimalFormatSymbols(loc[i]), loc[i]);
	    }
	}
	
	/**
	 * Sets whether object streaming is enabled.
	 * 
	 * @param pEnabled <code>true</code> to enable, <code>false</code> to disable
	 */
	public void setObjectStreamEnabled(boolean pEnabled)
	{
		bObjectStreamEnabled = pEnabled;
	}
	
	/**
	 * Gets whether object streaming is enabled.
	 * 
	 * @return <code>true</code> if enabled, <code>false</code> otherwise
	 */
	public boolean isObjectStreamEnabled()
	{
		return bObjectStreamEnabled;
	}
	
}	// ByteSerializer
