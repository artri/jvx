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
 * 24.01.2009 - [JR] - testTokenizer
 */
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.rad.ui.IComponent;
import javax.rad.ui.component.IIcon;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.StringUtil;

import remote.RemoteFile;
import remote.TemporaryRemoteFile;

/**
 * Tests any interesting feature.
 * 
 * @author René Jahn
 */
public class TestFeatures
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the call count for {@link #getStrings()}. */
	private int iStringCallCount = 0;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * A method without functionality.
	 * 
	 * @param pValues a vararg string parameter
	 * @return the <code>pValues</code>
	 */
	private String[] callVarArgs(String... pValues)
	{
		return pValues;
	}
	
	/**
	 * Returns a default string array.
	 * 
	 * @return {"1", "2", "3", "4", "5"}
	 */
	private String[] getStrings()
	{
		iStringCallCount++;
		
		return new String[] {"1", "2", "3", "4", "5"};
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests some arraycopy calls.
	 */
	@Test
	public void testArrayCopy()
	{
		String[] sMethod = new String[] {"Method-1", "Method-2"};
		
		Object[][] oParams = new Object[][] { {"Param - 1.1", "Param - 1.2"}, {"Param - 2.1"} };
		
		String[] sMethodCopy = new String[sMethod.length + 1];
		
		Object[][] oParamsCopy = new Object[oParams.length + 2][];
		
		
		System.arraycopy(sMethod, 0, sMethodCopy, 1, sMethod.length);
		System.arraycopy(oParams, 0, oParamsCopy, 1, oParams.length);
		
		sMethodCopy[0] = "Neu - 0";
		oParamsCopy[0] = new Object[] {"Param - 0.1"};
		
		/*
		for (int i = 0, anz = sMethodCopy.length; i < anz; i++)
		{
			if (i > 0)
			{
				System.out.print(", ");
			}
			
			System.out.print(sMethodCopy[i]);
		}
		
		System.out.println();
		
		for (int i = 0, anz = oParamsCopy.length; i < anz; i++)
		{
			if (oParamsCopy[i] == null)
			{
				System.out.print("null");
			}
			else
			{
				for (int j = 0, anzj = oParamsCopy[i].length; j < anzj; j++)
				{
					if (j > 0)
					{
						System.out.print(", ");
					}
					
					System.out.print(oParamsCopy[i][j]);
				}
			}
			
			System.out.println();
		}
		*/
		
		Assert.assertArrayEquals(new String[] {"Neu - 0", "Method-1", "Method-2"}, sMethodCopy);
		Assert.assertArrayEquals(new Object[][] { {"Param - 0.1"}, {"Param - 1.1", "Param - 1.2"}, {"Param - 2.1"}, null }, oParamsCopy);
	}
	
	/**
	 * Tests some regular expressions.
	 */
	@Test
	public void testRegexp()
	{
		Pattern pat = Pattern.compile("(.*):(.*):(.*)$");
		
		Matcher mat = pat.matcher("Note: cannot read: NotExisting.java");
		
		
		Assert.assertTrue("Regular expression does not match", mat.find());
		
		Assert.assertEquals("Note", mat.group(1));
		Assert.assertEquals(" cannot read", mat.group(2));
		Assert.assertEquals(" NotExisting.java", mat.group(3));
		
		
		long lStart = System.currentTimeMillis();
		
		pat = Pattern.compile("[\\.\\;\\ \\\t\\<\\(\\{]Application[\\.\\;\\ \\\t\\<\\(\\{\\)\\}\\>]$?");
		
		//                               16        26     33               50          62        72    78         89        99
		String sText = "This is my first Application with ApplicationBuilder and other Application-Tools created. Application";
		
		int iX = 0;
		for (int i = 0; i < 1000000; i++)
		{
			mat = pat.matcher(sText);
			
			while (mat.find())
			{
				iX++;
			}
		}
		
		System.out.println(iX);
		
		System.out.println("Duration: " + (System.currentTimeMillis() - lStart));
		
		lStart = System.currentTimeMillis();
		
		iX = 0;

		for (int i = 0; i < 1000000; i++)
		{
			int iPos = -1;
			
			while ((iPos = sText.indexOf("Application", iPos + 1)) >= 0)
			{
				iX++;
			}
		}

		System.out.println(iX);
		
		System.out.println("Duration: " + (System.currentTimeMillis() - lStart));

		
		pat = Pattern.compile("[\\.\\;\\ \\\t\\<\\(\\{]Session[\\.\\;\\ \\\t\\<\\(\\{\\)\\}\\>]?");
		
		sText = "}	// Session";
		
		mat = pat.matcher(sText);
		
		while (mat.find())
		{
			System.out.println("Comment: " + mat.start());
		}
		
		pat = Pattern.compile("\\\"[.[\\\"]]*Application[.[\\\"]]*\\\"");
		
		sText = "unitTest(\"Test\", Application.class, \"Application\");";
		
		mat = pat.matcher(sText);
		
		while (mat.find())
		{
			System.out.println("String: " + mat.start());
		}
	}
	
	/**
	 * Tests reflective object calls.
	 */
	@Test
	public void testReflective()
	{
		RemoteFile rfConfig = new RemoteFile(new File("config.xml"));
		
		Field[] fiObjects = rfConfig.getClass().getDeclaredFields();
		
		
		//Ändern des Wertes einer Property aus einer instanzierten Klasse
		
		for (int i = 0, anz = fiObjects.length; i < anz; i++)
		{
			try
			{
				fiObjects[i].setAccessible(true);
				System.out.println("[A] Field: " + fiObjects[i].getName() + " = " +  fiObjects[i].get(rfConfig));
				
				if (fiObjects[i].getName().equals("file"))
				{
					fiObjects[i].set(rfConfig, new File("unknown.xml"));
					
					System.out.println("[A] " + fiObjects[i].getName() + " - " +  fiObjects[i].get(rfConfig));
				}
				
				fiObjects[i].setAccessible(false);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		System.out.println("[A] Path: " + rfConfig.getAbsolutePath());
		System.out.println();
		
		//Ändern des Wertes einer Property aus dem Parent einer instanzierten Klasse

		TemporaryRemoteFile trfConfig = new TemporaryRemoteFile(new File("config.xml"));
		
		fiObjects = trfConfig.getClass().getDeclaredFields();
		
		for (int i = 0, anz = fiObjects.length; i < anz; i++)
		{
			try
			{
				fiObjects[i].setAccessible(true);
				System.out.println("[B] Field: " + fiObjects[i].getName() + " - " +  fiObjects[i].get(trfConfig));
				
				
				Annotation anon = fiObjects[i].getAnnotation(Inject.class);
				
				if (anon != null)
				{
					System.out.println("[B] Annotation found: " + fiObjects[i].getName());
				}
				
				
				if (fiObjects[i].getName().equals("file"))
				{
					fiObjects[i].set(trfConfig, new File("unknown.xml"));
					
					System.out.println("[B] " + fiObjects[i].getName() + " - " +  fiObjects[i].get(trfConfig));
				}
				
				fiObjects[i].setAccessible(false);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		System.out.println("[B] Path: " + trfConfig.getAbsolutePath());
	}
	
	/**
	 * Search if a found text is part of a quoted string.
	 */
	@Test
	public void testFindString()
	{
		String[] sText = {"unitTest(\"Test\", Application.class, \" Application.class\");",
				          "unitTest(\"Test\\\", Application.class\", \" Application.class\");",
				          "unitTest(\"Test\\\", Application.class\", \" Application.class);",
				          " ApplicationObject Application ",
				          "Application ",
				          "Application",
				          " ApplicationObject Application"}; 
		
		Pattern pat = Pattern.compile("(^|[\\.\\;\\ \\\t\\<\\(\\{\\}])(Application)([\\.\\;\\ \\\t\\<\\(\\{\\)\\}\\>]|$)");
		
		Matcher mat;
		
		
		for (int i = 0, anz = sText.length; i < anz; i++)
		{
			mat = pat.matcher(sText[i]);			

			System.out.println(Arrays.toString(StringUtil.getQuotedBoundaries(sText[i])));
			
			while (mat.find())
			{
				System.out.println(sText[i] + " : " + mat.start(2) + ", " + mat.end(2));
				
				for (int j = 0, start = mat.start(2), end = mat.end(2); j < end; j++)
				{
					if (j < start)
					{
						System.out.print(" ");
					}
					else
					{
						if (j == start || j == end - 1)
						{
							System.out.print("^");
						}
						else
						{
							System.out.print("~");
						}
					}
				}
				
				System.out.println();
			}
			
			System.out.println("----------------------------------------------------------------");
		}
	}

	/**
	 * Tests remove keys from a {@link Hashtable} while iterating it.
	 * 
	 * @throws Exception an expected or unknown exception
	 */
	@Test (expected = ConcurrentModificationException.class)
	public void testHashtableRemovalWithEnumeration() throws Exception
	{
		Hashtable<String, String> htElements = new Hashtable<String, String>();
		
		htElements.put("1", "A");
		htElements.put("2", "B");
		htElements.put("3", "C");
		htElements.put("4", "D");
		htElements.put("5", "E");
		htElements.put("6", "F");
		htElements.put("7", "G");
		
		for (Map.Entry<String, String> entry : htElements.entrySet())
		{
			if (entry.getValue().equals("B"))
			{
				htElements.remove(entry.getKey());
			}
		}
	}

	/**
	 * Tests remove keys from a {@link Hashtable} while iterating it.
	 * 
	 * @throws Exception an expected or unknown exception
	 */
	@Test (expected = ConcurrentModificationException.class)
	public void testHashtableRemovalWithIterator() throws Exception
	{
		Hashtable<String, String> htElements = new Hashtable<String, String>();
		
		htElements.put("1", "A");
		htElements.put("2", "B");
		htElements.put("3", "C");
		htElements.put("4", "D");
		htElements.put("5", "E");
		htElements.put("6", "F");
		htElements.put("7", "G");
		
		Map.Entry<String, String> entry;
		
		for (Iterator<Map.Entry<String, String>> it = htElements.entrySet().iterator(); it.hasNext();)
		{
			entry = it.next();
			
			if (entry.getValue().equals("B"))
			{
				htElements.remove(entry.getKey());
			}
		}
	}
	
	/**
	 * Tests tokenize a string.
	 */
	@Test
	public void testTokenizer()
	{
		//------------------------------------------------------
		// BaseTools
		//------------------------------------------------------
		
		ArrayUtil<String> auList = StringUtil.separateList("window.location.href", ".", false);

		String[] sTokens = new String[auList.size()];
		auList.toArray(sTokens);
		
		
		Assert.assertArrayEquals(new String[] {"window", "location", "href"}, sTokens);
		
		auList = StringUtil.separateList("window", ".", false);

		sTokens = new String[auList.size()];
		auList.toArray(sTokens);
		
		Assert.assertArrayEquals(new String[] {"window"}, sTokens);
		
		auList = StringUtil.separateList("", ".", false);

		sTokens = new String[auList.size()];
		auList.toArray(sTokens);
		
		Assert.assertArrayEquals(new String[] {""}, sTokens);

		auList = StringUtil.separateList(null, ".", false);

		sTokens = new String[auList.size()];
		auList.toArray(sTokens);
		
		Assert.assertArrayEquals(new String[] {}, sTokens);

		//------------------------------------------------------
		// StringTokenizer
		//------------------------------------------------------
		
		StringTokenizer stok = new StringTokenizer("window.location.href", ".");
		
		Assert.assertEquals(3, stok.countTokens(), 0);
		
		stok = new StringTokenizer("window", ".");
		
		Assert.assertEquals(1, stok.countTokens(), 0);

		stok = new StringTokenizer("", ".");
		
		Assert.assertEquals(0, stok.countTokens(), 0);
		
		//------------------------------------------------------
		// String.split
		//------------------------------------------------------
		
		sTokens = "window.location.href".split("\\.");
		
		Assert.assertArrayEquals(new String[] {"window", "location", "href"}, sTokens);

		sTokens = "window".split("\\.");
		
		Assert.assertArrayEquals(new String[] {"window"}, sTokens);

		sTokens = "".split("\\.");
		
		Assert.assertArrayEquals(new String[] {""}, sTokens);
	}

	/**
	 * Tests text encoding.
	 * 
	 * @throws Exception if the encoding fails
	 */
	@Test
	public void testEncode() throws Exception
	{
		byte[] by = new String("€").getBytes("UTF8");
		
		for (int i = 0, anz = by.length; i < anz; i++)
		{
			System.out.println(by[i] & 255);
		}
		
		System.out.println(CodecUtil.encodeURLPart("Ergänzungen"));
		
		
		System.out.println(CodecUtil.decodeHex("7061636b756e672e6c6f67696e2e6b6579"));
		System.out.println(CodecUtil.decodeHex("307837303631363336623735366536373265366336663637363936653265366236353739"));
		System.out.println(CodecUtil.decodeHex("7061636b756e672e757365726e616d65"));
	}

	/**
	 * Tests array class type handling.
	 */
	@Test
	public void testArray()
	{
		System.out.println(Arrays.toString((String[])null));

		String[][][][] array = new String[1][][][];
		
		System.out.println(array.getClass());
	}
	
	/**
	 * Tests Collection functions.
	 */
	@Test
	public void testCollection()
	{
		ArrayUtil<String> au = new ArrayUtil<String>();
		
		ArrayUtil<String> auSub = new ArrayUtil<String>();
		auSub.add("First");
		auSub.add("Second");
		
		au.addAll(0, auSub);
		
		ArrayUtil<String> auSubSub = new ArrayUtil<String>();
		auSubSub.add("0");
		auSubSub.add("1");
		
		au.addAll(0, auSubSub);
		
		Assert.assertEquals("[0, 1, First, Second]", au.toString());
	}
	
	/**
	 * Tests a method call with vararg.
	 */
	@Test
	public void testVarArg()
	{
		Assert.assertArrayEquals(new String[] {"First"}, callVarArgs("First"));
		Assert.assertTrue(0 == (callVarArgs()).length);
		Assert.assertNull(callVarArgs((String[])null));
		Assert.assertTrue(2 == callVarArgs(null, null).length);
		Assert.assertNotNull(callVarArgs());
		Assert.assertTrue(0 == callVarArgs().length);
	}
	
/*
	@Test
	public void testRenameFiles()
	{
		File[] files = new File("D:\\temp\\show\\").listFiles();
		
		
		for (int i = 0; i < files.length; i++)
		{
			files[i].renameTo(new File(files[i].getParentFile(), "large_" + files[i].getName()));
		}	
	}
*/

	/**
	 * Tests date class handling.
	 */
	@Test
	public void testDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

		GregorianCalendar cal = new GregorianCalendar(1900, 1, 11);
		
		Date dateFirst = cal.getTime();
		
		System.out.println(dateFirst.getTime());
		System.out.println(dateFirst);
		
		Date date = new Date(0);
		
		System.out.println(sdf.format(date));
	}

	/**
	 * Tests {@link String}[] instantiation.
	 */
	public void testStringInit()
	{
		String[] sArray = {"1", "2"};

		//OK
		callVarArgs(new String[] {"1", "2"});
		callVarArgs(sArray);
		
		//ERROR
		//callStringArray({"1", "2"});
	}
	
	/**
	 * Test loop conditions.
	 */
	@Test
	public void testLoop()
	{
		iStringCallCount = 0;
		
		//getStrings() should be called only once!
		for (String element : getStrings())
		{
			//nothing to be done
		}
		
		Assert.assertEquals(1, iStringCallCount);
	}

	/**
	 * Tests memory usage of empty Arrays.
	 */
	@Test
	public void testEmptyArray()
	{
		System.out.println(new String[] { });
		System.out.println(new String[] { });
		System.out.println(new String[0]);
		System.out.println(new String[0]);
	}

	/**
	 * A simple test case for constructor invocation.
	 */
	@Test
	public void testConstructor()
	{
		//A(), B()
		new B();
		//A(), B(String)
		new B("");
		//A(String), C(String)
		new C("");
		
		new E();
	}
	
//	@Test
//	public void testImageResize() throws Exception
//	{
//		File fiDir = new File("C:\\temp\\fotos\\thumbs\\");
//		
//		File[] fiPics = fiDir.listFiles();
//		
//		BufferedImage image;
//		
//		for (File fiPic : fiPics)
//		{
//			if (!fiPic.isDirectory())
//			{
//				image = ImageIO.read(new FileInputStream(fiPic));
//	
//				ImageIO.write((BufferedImage)ImageUtil.getScaledImage(image, 128, 128, true), "jpg", 
//			                  new File(fiDir, "small/" + fiPic.getName().substring(0, fiPic.getName().length() - 4) + "_tn.jpg"));
//
//				ImageIO.write((BufferedImage)ImageUtil.getScaledImage(image, 800, 600, true), "jpg", 
//							  new File(fiDir, "large/" + fiPic.getName()));
//			}
//		}
//	}

//	/**
//	 * Tests if its possible to close sessions from remote hosts.
//	 * 
//	 * @throws Throwable if the test fails
//	 */
//	
//	@Test
//	public void testCloseOtherSessions() throws Throwable
//	{
//		Throwable thError = null;
//		
//		System.setProperty("http.proxyHost", "10.0.0.1");
//		System.setProperty("http.proxyPort", "3128");
//		
//		IConnection conDemo = new HttpConnection("http://demo.sibvisions.org/showcase/services/Server");
////		IConnection conClose = new HttpConnection(new ByteSerializer(), "http://demo.sibvisions.org/packung/services/Server");
//
//		ConnectionInfo coninfo = new ConnectionInfo();
//		
//		
//		coninfo.getProperties().put(IConnectionConstants.APPLICATION, "showcase");
//		coninfo.getProperties().put(IConnectionConstants.USERNAME, "admin");
//		coninfo.getProperties().put(IConnectionConstants.PASSWORD, "admin");
//
//		try
//		{
//			conDemo.open(coninfo);
//			
//			System.out.println("Hello");
//
//			List<Object> liSessions = (List<Object>)(conDemo.call
//			(
//				coninfo,
//				new String[] {"monitoring"}, 
//				new String[] {"getSessionIds"}, 
//				null,
//				null
//			)[0]);
//			
//			System.out.println(liSessions.size());
//
//			ConnectionInfo coni;
//			for (Object oSessionId : liSessions)
//			{
//				if (!oSessionId.equals(coninfo.getConnectionId()))
//				{
//					System.out.println("Try to close: " + oSessionId);
//
//					coni = new ConnectionInfo();
//					coni.setConnectionId(oSessionId);
//					coni.setLastCallTime(System.currentTimeMillis());
//					
//					//depends on the serializer
////					conClose.close(coni);
//					conDemo.close(coni);
//					
//					System.out.println("Closed: " + oSessionId);
//				}
//			}
//			
//			liSessions = (List<Object>)(conDemo.call
//			(
//				coninfo,
//				new String[] {"monitoring"}, 
//				new String[] {"getSessionIds"}, 
//				null,
//				null
//			)[0]);
//
//			System.out.println(liSessions.size());
//		}
//		catch (Throwable th)
//		{
//			thError = th;
//		}
//		finally
//		{
//			try
//			{
//				conDemo.close(coninfo);
//			}
//			catch (Throwable th)
//			{
//				if (thError == null)
//				{
//					thError = th;
//				}
//			}
//			
//			if (thError != null)
//			{
//				throw thError;
//			}
//		}
//	}

	/**
	 * Tests simple instanceof checks.
	 */
	@Test
	public void testInstanceOf()
	{
		String a = null;
		
		Assert.assertFalse(a instanceof String);
	}
	
	/**
	 * Tests {@link Class#isAssignableFrom(Class)}.
	 */
	@Test
	public void testIsAssignableFrom()
	{
		Assert.assertTrue(IComponent.class.equals(IComponent.class));
		Assert.assertTrue(IComponent.class.isAssignableFrom(IComponent.class));
		Assert.assertTrue(IComponent.class.isAssignableFrom(IIcon.class));
		Assert.assertFalse(IIcon.class.isAssignableFrom(IComponent.class));
	}
	
	/**
	 * Tests current timestamp creation.
	 */
	@Test
	public void testCurrentTimestamp()
	{
		//should be fast enough that there is no difference between the method calls!
		Assert.assertEquals(new Timestamp(System.currentTimeMillis()), new Timestamp(new Date().getTime()));
	}
	
	/**
	 * Tests {@link String#replace(CharSequence, CharSequence)} performance.
	 */
	@Test
	public void testReplace()
	{
		String sStmt = "ALTER TABLE @TABLE_NAME ADD CONSTRAINT @CONSTRAINT_NAME FOREIGN KEY (@COLUMNS) REFERENCES @REF_TABLE_NAME (@REF_COLUMNS)";
		
		long lStart = System.currentTimeMillis();
		
		String sNew;
		for (int i = 0; i < 10; i++)
		{
			sNew = sStmt.replace("@TABLE_NAME", "table");
			sNew = sNew.replace("@CONSTRAINT_NAME", "constraint");
			sNew = sNew.replace("@COLUMNS", "columns");
			sNew = sNew.replace("@REF_TABLE_NAME", "reftable");
			sNew = sNew.replace("@REF_COLUMNS", "refcolumns");
		}
		
		System.out.println(System.currentTimeMillis() - lStart);
		
		lStart = System.currentTimeMillis();

		for (int i = 0; i < 10; i++)
		{
			sNew = StringUtil.replace(sStmt, "@TABLE_NAME", "table");
			sNew = StringUtil.replace(sNew, "@CONSTRAINT_NAME", "constraint");
			sNew = StringUtil.replace(sNew, "@COLUMNS", "columns");
			sNew = StringUtil.replace(sNew, "@REF_TABLE_NAME", "reftable");
			sNew = StringUtil.replace(sNew, "@REF_COLUMNS", "refcolumns");
		}
		
		System.out.println(System.currentTimeMillis() - lStart);
	}
	
	/**
	 * Tests exceptions in finally block.
	 */
	@Test
	public void testException()
	{
		try
		{
		    Exception e = new Exception("B");
			
			try
			{
				throw new Exception("A");
			}
			finally
			{
				if (e != null)
				{
					throw e;
				}
			}
		}
		catch (Exception e)
		{
			Assert.assertEquals("B", e.getMessage());
		}
	}
	
	/**
	 * Tests stack trace manipulation.
	 */
	@Test
	public void testStackTraceManipulation()
	{
	    Exception e1 = new Exception("FIRST");
	    
	    Exception e2 = new Exception("SECOND");
	    
	    
	    StackTraceElement[] st1 = e1.getStackTrace();
	    StackTraceElement[] st2 = e2.getStackTrace();
	    
	    
	    StackTraceElement[] st3 = new StackTraceElement[st1.length + st2.length];
	    
	    int pos = 0;
	    
	    for (int i = 0; i < st1.length; i++)
	    {
	        st3[pos++] = st1[i];
	    }
	    
        for (int j = 0; j < st2.length; j++)
        {
            st3[pos++] = new StackTraceElement("> " + st2[j].getClassName(), st2[j].getMethodName(), st2[j].getFileName(), st2[j].getLineNumber());
        }

        e1.setStackTrace(st3);

        Exception e3 = new Exception("CLIENT", e1);
        
	    System.out.println(ExceptionUtil.dump(e3, true));
	}
	
	/**
	 * Tests {@link ConcurrentHashMap}.
	 */
	@Test
	public void testConcurrentHashMap()
	{
		ConcurrentHashMap<String, A> chmp = new ConcurrentHashMap<String, A>();
		
		A a1 = new A();
		A a2 = new A();
		
		chmp.put("A", a1);
		chmp.put("A", a2);
		
		Assert.assertFalse(chmp.remove("A", a1));
	}
	
	/**
	 * Tests file object.
	 * 
	 * @throws Exception if test fails
	 */
	@Test
	public void testFile() throws Exception
	{
		File fie = new File("test.jar");
		
		Assert.assertNotNull(fie.getAbsolutePath());
		Assert.assertNull(fie.getParent());
	}
	
	/**
	 * Tests boolean parsing.
	 */
	@Test
	public void testBoolean()
	{
		Assert.assertSame(Boolean.TRUE, Boolean.valueOf("true"));
		Assert.assertSame(Boolean.TRUE, Boolean.valueOf("TrUe"));
		Assert.assertSame(Boolean.FALSE, Boolean.valueOf("yes"));
		Assert.assertSame(Boolean.FALSE, Boolean.valueOf("false"));
		Assert.assertSame(Boolean.FALSE, Boolean.valueOf("FALSE"));
		Assert.assertSame(Boolean.FALSE, Boolean.valueOf("FalSe"));
		Assert.assertSame(Boolean.FALSE, Boolean.valueOf(null));
	}
	
	/**
	 * Tests static initialization.
	 */
	@Test
	public void testStatic()
	{
		StaticA sa = new StaticA();
		StaticB sb = new StaticB();
		
		System.out.println(StringUtil.toString(sa.getMappings()));
	}
	
	/**
	 * Tests random number generation :-).
	 */
	@Test
	public void testRandom()
	{
		for (int i = 0; i < 10000; i++)
		{
			int iValue = 20 + (int)(Math.random() * 20);

			if (iValue < 20 || iValue > 40)
			{
				Assert.fail("Value " + iValue + " not between 20 and 40!");
			}
		}
	}
	
	/**
	 * Tests null comparison.
	 */
	@Test
	public void testNull()
	{
		Object n1 = null;
		Object n2 = null;
		
		if (n1 == n2)
		{
			System.out.println("null == null");
		}

		Object res = new Object();
		Object uiResource = res;
		
		if (res == uiResource)
		{
			System.out.println("Objects are same");
		}
		
		if ((res != null || uiResource != null) && res == uiResource)
		{
			System.out.println("Objects are not null and same");
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * A simple class with constructors.
	 * 
	 * @author René Jahn
	 */
	class A
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Default.
		 */
		public A()
		{
			System.out.println("A()");
		}
		
		/**
		 * Constructor with String parameter.
		 * 
		 * @param pValue anything
		 */
		public A(String pValue)
		{
			System.out.println("A(String)");
		}
		
	}	// A
	
	/**
	 * A subclass of {@link A} with specific constructors.
	 * 
	 * @author René Jahn
	 */
	class B extends A
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Invokes {@link A#A()}.
		 */
		public B()
		{
			System.out.println("B()");
		}
	
		/**
		 * Invokes {@link A#A()}.
		 * 
		 * @param pValue anything
		 */
		public B(String pValue)
		{
			System.out.println("B(String)");
		}
		
	}	// B
	
	/**
	 * A subclass of {@link A} with specific constructors.
	 * 
	 * @author René Jahn
	 */
	class C extends A
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Invokes {@link A#A(String)}.
		 * 
		 * @param pValue anything
		 */
		public C(String pValue)
		{
			super(pValue);
			
			System.out.println("C(String)");
		}
		
	}	// C
	
	/**
	 * The <code>D</code> class is a simple class with two constructors. One is protected
	 * and the other one is public.
	 * 
	 * @author René Jahn
	 *
	 */
	public class D
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the name. */
		private String name;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>D</code> with a specific name.
		 * 
		 * @param pName the name
		 */
		protected D(String pName)
		{
			name = pName;

			System.out.println("D(String)");
		}
		
		/**
		 * Creates a new instance of <code>D</code>.
		 */
		public D()
		{
			name = "D";
			
			System.out.println("D()");
		}
		
	}	// D
	
	/**
	 * The <code>D</code> class extends {@link D} and tests constructors.
	 * 
	 * @author René Jahn
	 */
	public class E extends D
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>E</code>.
		 */
		public E()
		{
			super("E");
			
			System.out.println("E()");
		}
		
	}	// E
	
	/**
	 * A simple static base class.
	 * 
	 * @author René Jahn
	 */
	public abstract static class BaseStatic
	{
		/** the mappings. */
		private static HashMap<String, String> hmpMappings = new HashMap<String, String>();
		
		/**
		 * Adds a mapping.
		 * 
		 * @param pName the name
		 * @param pValue the value
		 */
		public static void addMapping(String pName, String pValue)
		{
			hmpMappings.put(pName, pValue);
		}
		
		/**
		 * Gets all mapping.
		 * 
		 * @return the mappings
		 */
		public static List<String> getMappings()
		{
			ArrayUtil<String> list = new ArrayUtil<String>();
			
			for (Entry<String, String> entry : hmpMappings.entrySet())
			{
				list.add(entry.getValue());
			}
			
			return list;
		}
		
	}	// BaseStatic
	
	/**
	 * A simple static class.
	 *  
	 * @author René Jahn
	 */
	public static class StaticA extends BaseStatic
	{
		static
		{
			addMapping("A", "A.class");
		}
		
	}	// StaticA
	
	/**
	 * A simple static class.
	 *  
	 * @author René Jahn
	 */
	public static class StaticB extends BaseStatic
	{
		static
		{
			addMapping("B", "B.class");
		}
		
	}	// StaticB
	
} 	// TestFeatures
