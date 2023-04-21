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
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * Tests all functions of the <code>DBToolbox</code> utility class.
 * 
 * @author René Jahn
 * @see DBToolbox
 */
public class TestDBToolbox
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
//	/** max. test streets. */
//	private static final int STRASSEN = 5000;
//
//	/** max. test adresses. */
//	private static final int ADRESSEN = 10000;
//	
//	/** max. test persons. */
//	private static final int PERSONEN = 100000;
//	
//	/** max. test postcodes. */
//	private static final int POSTLEITZAHLEN = 8000;
//	
//	/** max. test divisions. */
//	private static final int ABTEILUNGEN = 5000;
//	
//	/** max. test companies. */
//	private static final int FIRMEN = 1000;
	
	/** max. test streets. */
	private static final int STRASSEN = 100;

	/** max. test adresses. */
	private static final int ADRESSEN = 100;
	
	/** max. test persons. */
	private static final int PERSONEN = 100;
	
	/** max. test postcodes. */
	private static final int POSTLEITZAHLEN = 80;
	
	/** max. test divisions. */
	private static final int ABTEILUNGEN = 50;
	
	/** max. test companies. */
	private static final int FIRMEN = 10;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests this class.
	 * 
	 * @param pArgs not needed
	 */
	public static void main(String[] pArgs)
	{
		JUnitCore.runClasses(TestDBToolbox.class);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the directory where to store a database.
	 * 
	 * @param pDbName name of the database file
	 * @return absolute path to the database file
	 */
	private String getDirectory(String pDbName)
	{
		String sOs = System.getProperty("os.name").toLowerCase();

		
		if (sOs.indexOf("linux") >= 0 || sOs.indexOf("mac") >= 0)
		{
			String sDir = System.getProperty("user.home");
			
			if (new File(sDir).exists())
			{
				sDir += "/dbs/" + pDbName.substring(0, pDbName.length() - 2) + "/";
				
				File fiDir = new File(sDir);
				
				if (!fiDir.exists())
				{
					fiDir.mkdirs();
				}
				
				return sDir + pDbName;
			}
			else
			{
				return "/tmp/" + pDbName;
			}
		}
		else
		{
			return "c:\\temp\\" + pDbName;
		}
	}
	
	/**
	 * Returns the number if <code>iId > 0</code> or <code>"null"</code> if
	 * it's not the case.
	 *  
	 * @param iId any number
	 * @return string representation of the number
	 */
	private String getString(int iId)
	{
		if (iId > 0)
		{
			return "" + iId;
		}
		
		return "null";
	}
	
	/**
	 * Returns the object of the primitive int value, if <code>iId > 0</code>.
	 * 
	 * @param iId any number
	 * @return <code>Integer</code> representation of the value or null 
	 */
	private Object getObject(int iId)
	{
		if (iId > 0)
		{
			return Integer.valueOf(iId);
		}
		
		return null;
	}
	
	/**
	 * Returns a random number in a predefined range.
	 * 
	 * @param iFirst first possible number
	 * @param iLast last possible number
	 * @return number between <code>iFirst</code> and <code>iLast</code>
	 */
	private int getRandom(int iFirst, int iLast)
	{
		return (int)(Math.random() * (float)(iLast - iFirst)) + iFirst;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Test the creation of a new demo database.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDoNewDemo() throws Throwable
	{
		new DBToolbox(new String[] {DBToolbox.NEW, "testdbs/demo/create.sql", getDirectory("demodb"), "ISO-8859-15"});
	}

	/**
	 * Test the creation of a new root database.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDoNewTest() throws Throwable
	{
		new DBToolbox(new String[] {DBToolbox.NEW, "testdbs/test/create.sql", getDirectory("testdb"), "ISO-8859-15"});
	}
	
	/**
	 * Test the creation of a new persons database.
	 * 
	 * @throws Throwable if the test fails
	 */
	@Test
	public void testDoNewPersons() throws Throwable
	{
		Connection con = null;
		
		String sOutput = getDirectory("personsdb");
		
		Statement stmt = null;
		
		PreparedStatement psStmt = null;
		
		
		new DBToolbox(new String[] {DBToolbox.NEW, "testdbs/persons/create.sql", sOutput, "ISO-8859-15"});
		
		//Testdata

		try
		{
	        Class.forName("org.hsqldb.jdbcDriver");
	
	        con = DriverManager.getConnection("jdbc:hsqldb:file:" + sOutput + ";shutdown=true", "sa", "");
	        
	        stmt = con.createStatement();
	        
	        //fill zip codes
	        for (int i = 1000, anz = i + TestDBToolbox.POSTLEITZAHLEN; i <= anz; i++)
	        {
	        	stmt.execute("insert into POSTLEITZAHLEN (PLZ, ORT) VALUES (" + i + ", 'Ort (" + i + ")')");
	        }
	        
	        con.commit();
	        
	        //fill streets
	        for (int i = 1; i <= TestDBToolbox.STRASSEN; i++)
	        {
	        	stmt.execute("insert into STRASSEN (NAME) VALUES ('Straße (" + i + ")')");
	        }
	        
	        con.commit();

	        //fill salutations
	    	stmt.execute("insert into ANREDEN (BEZEICHNUNG) VALUES ('Herr')");
	    	stmt.execute("insert into ANREDEN (BEZEICHNUNG) VALUES ('Frau')");
	    	stmt.execute("insert into ANREDEN (BEZEICHNUNG) VALUES ('Oida')");
	    	stmt.execute("insert into ANREDEN (BEZEICHNUNG) VALUES ('Oide')");
	    	
	        con.commit();

	        //fill academic titles
	    	stmt.execute("insert into TITEL (BEZEICHNUNG) VALUES ('Ing.')");
	    	stmt.execute("insert into TITEL (BEZEICHNUNG) VALUES ('Mag.')");
	    	stmt.execute("insert into TITEL (BEZEICHNUNG) VALUES ('DI')");
	    	stmt.execute("insert into TITEL (BEZEICHNUNG) VALUES ('Mag. (FH)')");
	    	stmt.execute("insert into TITEL (BEZEICHNUNG) VALUES ('DDr.')");
	    	stmt.execute("insert into TITEL (BEZEICHNUNG) VALUES ('Dr.')");
	    	
	        con.commit();

	    	//fill address
	    	for (int i = 1; i <= TestDBToolbox.ADRESSEN; i++)
	    	{
	    		stmt.execute("insert into ADRESSEN (POST_ID, STRA_ID, HAUSNUMMER, STIEGE, TUERNUMMER) " +
	    				                  " VALUES (" + getRandom(0, TestDBToolbox.POSTLEITZAHLEN - 1) + ", " + 
	    				                  				getRandom(0, TestDBToolbox.STRASSEN - 1) + ", " + 
	    				                  				getRandom(0, 49) + ", " +
	    				                                getString(getRandom(0, 9)) + "," + 
	    				                                getString(getRandom(0, 100)) + ")");
	    	}

	    	con.commit();

	    	psStmt = con.prepareStatement("insert into PERSONEN (PERS_ID, ANRE_ID, TITE_ID, ADRE_ID, VORNAME, NACHNAME, GEBDAT) VALUES (?, ?, ?, ?, ?, ?, ?)");
	    	
	    	//fill persons
	    	for (int i = 1; i <= TestDBToolbox.PERSONEN; i++)
	    	{
	    		psStmt.setObject(1, getObject(getRandom(0, i - 1)), Types.DECIMAL);
	    		psStmt.setObject(2, Integer.valueOf(getRandom(0, 3)), Types.DECIMAL);
	    		psStmt.setObject(3, getObject(getRandom(0, 5)), Types.DECIMAL);
	    		psStmt.setObject(4, getObject(getRandom(0, TestDBToolbox.ADRESSEN - 1)), Types.DECIMAL);
	    		psStmt.setObject(5, "Vorname von " + (i - 1), Types.VARCHAR);
	    		psStmt.setObject(6, "Nachname von " + (i - 1), Types.VARCHAR);
	    		psStmt.setObject(7, new GregorianCalendar(getRandom(1960, 2008), getRandom(1, 12), getRandom(1, 28)).getTime(), Types.DATE);
	    		
	    		psStmt.execute();
	    	}
	    	
	    	psStmt.close();
	    	
	    	con.commit();
	    	
	        //fill companies
	        for (int i = 1; i <= TestDBToolbox.FIRMEN; i++)
	        {
	        	stmt.execute("insert into FIRMEN (ADRE_ID, NAME) " +
	        			                " VALUES (" + getRandom(0, TestDBToolbox.ADRESSEN - 1) + ", " + 
	        			                              "'Firma (" + i + ")')");
	        }
	        
	    	con.commit();

	    	//fill chiefs
	        for (int i = 0; i < TestDBToolbox.FIRMEN; i++)
	        {
	        	Vector<Integer> vAdded = new Vector<Integer>();
	        	
	        	int iPers;
	        	
	        	for (int j = 1, anz = getRandom(2, 5); j < anz; j++)
	        	{
	        		//quick and dirty, and wit autoboxing :) but ok to violate unique key
	        		do
	        		{
	        			iPers = getRandom(0, TestDBToolbox.PERSONEN);
	        		}
	        		while (vAdded.contains(Integer.valueOf(iPers)));
	        		
		        	stmt.execute("insert into FIRM_PERS (FIRM_ID, PERS_ID) " +
			                                   " VALUES (" + i + ", " + iPers + ")");
		        	
		        	vAdded.add(Integer.valueOf(iPers));
	        	}
	        }	    	

	    	con.commit();

	    	//assign units
	        for (int i = 1; i <= TestDBToolbox.ABTEILUNGEN; i++)
	        {
	        	stmt.execute("insert into ABTEILUNGEN (FIRM_ID, ADRE_ID, BEZEICHNUNG) " +
	        			                " VALUES (" + getRandom(0, TestDBToolbox.FIRMEN - 1) + ", " + 
	        			                			  getRandom(0, TestDBToolbox.ADRESSEN - 1) + ", " +
	        			                			  "'Abteilung (" + i + ")')");
	        }
	        
	    	con.commit();

	    	//assign persons to units
	        for (int i = 0; i < TestDBToolbox.ABTEILUNGEN; i++)
	        {
	        	Vector<Integer> vAdded = new Vector<Integer>();
	        	
	        	int iPers;
	        	
	        	for (int j = 1, anz = getRandom(0, 20); j < anz; j++)
	        	{
	        		//quick and dirty, and wit autoboxing :) but ok to violate unique key
	        		do
	        		{
	        			iPers = getRandom(0, TestDBToolbox.PERSONEN);
	        		}
	        		while (vAdded.contains(Integer.valueOf(iPers)));
	        		
	        		String sAbtLeit;
	        		
	        		if (j == 10)
	        		{
	        			sAbtLeit = "Y";
	        		}
	        		else
	        		{
	        			sAbtLeit = "N";
	        		}
	        		
		        	stmt.execute("insert into ABTE_PERS (ABTE_ID, PERS_ID, ABTEILUNGSLEITER) " +
			                                " VALUES (" + i + ", " + iPers + ", '" + sAbtLeit + "')");
		        	
		        	vAdded.add(Integer.valueOf(iPers));
	        	}
	        }
	        
	    	con.commit();
		}
		finally
		{
			if (stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			if (psStmt != null)
			{
				try
				{
					psStmt.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
} 	// TestDBToolbox
