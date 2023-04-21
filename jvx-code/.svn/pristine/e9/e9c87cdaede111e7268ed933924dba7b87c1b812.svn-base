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
 * 01.02.2009 - [JR] - added database HTTPCONNECTION test functions
 */
package demo;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.rad.application.ILauncher;
import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.persist.ColumnMetaData;
import javax.rad.server.ICallBackBroker;
import javax.rad.server.ICallBackBroker.PublishState;
import javax.rad.server.SessionContext;

import com.sibvisions.rad.persist.event.StorageEvent;
import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.persist.jdbc.HSQLDBAccess;
import com.sibvisions.rad.server.GenericBean;
import com.sibvisions.rad.server.annotation.NotAccessible;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;

import remote.RemoteFile;

/**
 * Session object for unit tests.
 * 
 * @author RenÈ Jahn
 */
public class Session extends Application
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the internal statement count for statement execution. */
	private static int iStmtCount = 0;
	
    /** the event queue. */
    private ArrayUtil<String> auEventQueue = new ArrayUtil<String>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Session</code>.
	 */
	public Session()
	{
	    InstanceChecker.add(Session.class);
	    
		addCall("Session()");
		
		System.out.println("CALL: Session.<init> [" + SessionContext.getCurrentSession().getLifeCycleName() + "]");
	}
	
	/**
	 * Initialization of the logfile property.
	 * 
	 * @return new <code>RemoteFile</code> instance
	 */
	@SuppressWarnings("unused")
	private RemoteFile initRemoteFile()
	{
		return new RemoteFile(new File("session.xml"));
	}
	
	/**
	 * Initializes the session datasource. 
	 * 
	 * @return the datasource
	 * @throws Throwable if the initialization throws an error
	 */
	@SuppressWarnings("unused")
	private DBAccess initDataSource() throws Throwable
	{
		HSQLDBAccess dba = new HSQLDBAccess();
		
		dba.setUrl("jdbc:hsqldb:hsql://localhost/personsdb;ifexists=true"); 
		dba.setUsername("sa");
		dba.setPassword("");
		
		Properties pDBProperties = new Properties(); 
		pDBProperties.put("shutdown", Boolean.TRUE); 
		dba.setDBProperties(pDBProperties);
		
		dba.open();
				
		return dba;
	}
	
	/**
	 * Initializes the session test datasource. 
	 * 
	 * @return the test datasource
	 * @throws Throwable if the initialization throws an error
	 */
	@SuppressWarnings("unused")
	private DBAccess initTestDataSource() throws Throwable
	{
		HSQLDBAccess dba = new HSQLDBAccess();
		
		dba.setUrl("jdbc:hsqldb:hsql://localhost/testdb");
		dba.setUsername("sa");
		dba.setPassword("");

		Properties pDBProperties = new Properties();
		pDBProperties.put("shutdown", Boolean.TRUE);
		dba.setDBProperties(pDBProperties);
						
		dba.open();
				
		return dba;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Returns "getData()".
	 * 
	 * @return "getData()"
	 */
	public String getData()
	{
		addCall("getData()");
		
		return "getData()";
	}
	
	/**
	 * Returns "delayedGetData()" after 3 seconds.
	 * 
	 * @return "delayedGetData()" 
	 * @throws InterruptedException if the delay was interrupted
	 */
	public String delayedGetData() throws InterruptedException
	{
		addCall("delayedGetData()");

		Thread.sleep(3000);
		
		return "delayedGetData()";
	}
	
	/**
	 * Throws an Exception after 3 seconds.
	 * 
	 * @throws Exception always after 3 seconds or if the delay was interrupted
	 */
	public void delayedException() throws Exception
	{
	    delayedException(3000);
	}
	
	/**
	 * Throws an Exception after the given delay.
	 * 
	 * @param pDelay the delay
	 * @throws Exception always after given delay or if the delay was interrupted
	 */
	public void delayedException(long pDelay) throws Exception
	{
        addCall("delayedException()");
        
        Thread.sleep(pDelay);
        
        throw new Exception("DELAYED_EXCEPTION");
	}
	
	/**
	 * Throws an Exception.
	 * 
	 * @throws Exception always
	 */
	public void immediateException() throws Exception
	{
	    addCall("immediateException()");
	    
	    throw new Exception("IMMEDIATE_EXCEPTION");
	}
	
	/**
	 * Returns "finished" after the given number of milliseconds.
	 * 
	 * @param pDelay the delay, in milliseconds, before data will be returned
	 * @return <code>finished</code>
	 * @throws Exception if delaying fails
	 */
	public String delayedAction(long pDelay) throws Exception
	{
		Thread.sleep(pDelay);
		
		return "finished";
	}
	
	/**
	 * Gets the remote log file.
	 * 
	 * @return the remote log file
	 */
	public RemoteFile getRemoteFile()
	{
		addCall("getSession()");

		return (RemoteFile)get("remoteFile");
	}
	
	/**
	 * Gets a {@link String} (database HTTPCONNECTION test).
	 * 
	 * @return <code>**************************************************** This is Report call</code>
	 */
    public String getReport()
    {
        return "**************************************************** This is a report call";
    }
    
	/**
	 * Gets an {@link Integer} (database HTTPCONNECTION test).
	 * 
	 * @return <code>Integer.valueOf(2049)</code>
	 */
    public Integer getInt()
    {
        return Integer.valueOf(2049);
    }
    
	/**
	 * Gets an Object[] (database HTTPCONNECTION test).
	 * 
	 * @return <code>new Object[] {Integer.valueOf(1), "as", null, Integer.valueOf(3)}</code>
	 */
    public Object[] getObjectArray()
    {
        return new Object[] {Integer.valueOf(1), "as", null, Integer.valueOf(3)};
    }
    
	/**
	 * Gets an ArrayUtil with Object[] as element (database HTTPCONNECTION test).
	 * 
	 * @return an {@link ArrayUtil} with <code>new Object[] {"TEST", Integer.valueOf(202)}</code> as element
	 */
    public ArrayUtil getAU()
    {
        ArrayUtil au = new ArrayUtil();
        
        au.add(new Object[] {"TEST", Integer.valueOf(202)});
        
        return au;
    }
    
    /**
     * Returns an Object[] with parameters from the call.
     * 
     * @param pValues {@link String} or {@link Integer} parameters
     * @param pInt an {@link Integer}
     * @return a new Object[] with the first element of <code>pValues</code> and 
     *           the <code>pInt</code>
     */
    public Object[] getInput(Object[] pValues, Integer pInt)
    {
        return new Object[] {pValues[0], pInt};
    }
    
    /**
     * Returns an Object[] with parameters from the call.
     * (REST method)
     * 
     * @param pValues {@link String} or {@link Integer} parameters
     * @param pInt an {@link Integer}
     * @return a new Object[] with the first element of <code>pValues</code> and 
     *         the <code>pInt</code>
     */
    public Object[] getInput(List<Object> pValues, Number pInt)
    {
    	return new Object[] {pValues.get(0), pInt};
    }

    /**
     * Returns an Object[] with parameters from the call.
     * (REST method)
     * 
     * @param pValues {@link String} or {@link Integer} parameters
     * @param pInt an {@link Integer}
     * @return a new Object[] with the first element of <code>pValues</code> and 
     *         the <code>pInt</code>
     */
    public Object[] getInputWithArray(Object[] pValues, Number pInt)
    {
    	return new Object[] {pValues[0], pInt};
    }

    /**
	 * Gets the DBAccess of this Demo Session.
	 * 
	 * @return the DBAccess of this Demo Session
	 */
	public DBAccess getDataSource()
	{
		return (DBAccess)get("dataSource");
	}
	
	/**
	 * Gets the DBAccess of this Test DB.
	 * 
	 * @return the DBAccess of this Test DB.
	 */
	public DBAccess getTestDataSource()
	{
		return (DBAccess)get("testDataSource");
	}

	/**
	 * Executes a select statement sent from the client.
	 * 
	 * @param pStatment the statement
	 * @return the object name for the statement access
	 * @throws Exception if the statement causes an exception 
	 */
	public String execute(String pStatment) throws Exception
	{
		DBStorage dbs = new DBStorage();
		dbs.setDBAccess(getDataSource());
		dbs.setFromClause("(" + pStatment + ")");
		dbs.open();
		
		String sCall = "call " + (iStmtCount++);
		
		put(sCall, dbs);
		
		return sCall;
	}
	
	/**
	 * Gets the sent metadata back.
	 * 
	 * @param pData the sent meta data
	 * @return same as <code>pData</code>
	 */
	public ColumnMetaData getMetaData(ColumnMetaData pData)
	{
		return pData;
	}

	/**
	 * Returns the address storage.
	 * 
	 * @return the address storage.
	 * @throws Throwable if the storage can not be opened
	 */
	public DBStorage getAdrData() throws Throwable
	{
		DBStorage dbs = (DBStorage)get("adrData");
		
		if (dbs == null)
		{
			dbs = new DBStorage();

			dbs.setDBAccess(getDataSource());
			dbs.setFromClause("ADRESSEN");
			dbs.setWritebackTable("ADRESSEN");
			dbs.open();
			
			put("adrData", dbs);
		}
		
		return dbs;
	}	

	/**
	 * This method shouldn't be accessible via object provider.
	 * 
	 * @return the string <code>NotAccessible</code>
	 */
	@NotAccessible
	public String getNotAccessible()
	{
	    return "NotAccessible";
	}
	
	/**
	 * This method shouldn't be accessible via object provider, but only for REST calls.
	 * 
	 * @return the string <code>NotAccessible</code>
	 */
	@NotAccessible(environment = ILauncher.ENVIRONMENT_WEB + ":rest")
	public String getNotAccessibleREST()
	{
		return "NotAccessibleREST";
	}
	
	/**
	 * Sets a connection property.
	 * 
	 * @param pName the name
	 * @param pValue the value
	 */
	public void setConnectionProperty(String pName, String pValue)
	{
	    SessionContext.getCurrentSession().setProperty(pName, pValue);
	}

	/**
	 * Gets a {@link RemoteFileHandle} for jndi_config.xml.
	 * 
	 * @return the file handle
	 * @throws Exception if creating file handle failed
	 */
	public IFileHandle getRemoteFileHandle() throws Exception
	{
	    return new RemoteFileHandle("jndi_config.xml", ResourceUtil.getResourceAsStream("/jndi_config.xml"));
	}

    /**
     * Creates a dummy RTF report based on given template.
     * 
     * @param pFileHandle the input file handle
     * @return the file handle
     * @throws Exception if creating file handle failed
     */
	public IFileHandle createRTFReport(IFileHandle pFileHandle) throws Exception
	{
	    if (pFileHandle.getLength() < 10000)
	    {
	        throw new Exception("Upload failed! Length = " + pFileHandle.getLength());
	    }
	    
	    File fiTemp = File.createTempFile("rtfReport", ".rtf");
	    
	    FileUtil.save(fiTemp, pFileHandle.getInputStream());
	    
	    Hashtable<String, String> htMapping = new Hashtable<String, String>();
	    htMapping.put("[LAGERBEZEICHNUNG]", "Vienna-LA");
        htMapping.put("[ADRESSE]", "Wehlistraﬂe 29");
        htMapping.put("[PLZ]", "1200");
        htMapping.put("[ORT]", "Vienna");
        htMapping.put("[LIEFERSCHEIN_NR]", "12345");
        htMapping.put("[DRUCKDATUM]", "01.01.1976");
        htMapping.put("[LOOP@POSITIONEN]", "");
        htMapping.put("[ARTIKEL]", "A1");
        htMapping.put("[SERIENNUMMER]", "01219-1921-11");
        htMapping.put("[VERSANDART]", "UPS");
        htMapping.put("[ANSPRECHPERSON]", "Mr. Doe");
        htMapping.put("[TELEFON]", "01/123 312");
        htMapping.put("[MOBIL]", "0123/123 312 1");
        htMapping.put("[FAX]", "01/123 312 4");
        
        htMapping.put("[EMPF_NAME]", "Peter Haupt");
        htMapping.put("[EMPF_ADRESSE]", "Hauptstraﬂe 211");
        htMapping.put("[EMPF_PLZ]", "77628");
        htMapping.put("[EMPF_ORT]", "Germany");
	    
	    FileUtil.replace(fiTemp, fiTemp, htMapping, null);
	    
	    return new RemoteFileHandle(fiTemp);
	}
	
    /**
     * Adds an event to the queue.
     * 
     * @param pEvent the event
     */
    protected void addEvent(String pEvent)
    {
        Session session = getEventSession();
        
        //automatically add events to LCO of parent session
        if (session != null)
        {
            session.addEvent(pEvent);
        }
        else
        {
            auEventQueue.add(pEvent);
        }
    }
    
    /**
     * Gets the current event list.
     * 
     * @return the event list
     */
    public ArrayUtil<String> getEvents()
    {
        Session session = getEventSession();
        
        //automatically add events to LCO of parent session
        if (session != null)
        {
            return session.getEvents();
        }
        else
        {
            return auEventQueue;
        }
    }	
	
    /**
     * Gets the session instance for event logging.
     * 
     * @return the MasterSession LCO
     */
    private Session getEventSession()
    {
        GenericBean bnParent = (GenericBean)getParent();
        GenericBean bnCurrent = this;
        
        while (bnParent != null)
        {
            if (bnParent.getClass() == Application.class)
            {
                if (bnCurrent == this)
                {
                    return null;
                }
                else
                {
                    return (Session)bnCurrent;
                }
            }
            
            bnCurrent = bnParent;
            bnParent = (GenericBean)bnParent.getParent();
        }
        
        return null;
    }
    
    /**
     * Gets the current working directory as {@link File}. A file isn't serializable
     * and should cause an Exception.
     * 
     * @return the {@link File}
     */
    public File getNotSerializableFile()
    {
        return new File("");
    }
    
    /**
     * Starts a thread which publishs an {@link Integer} as callback result with instruction "INT_VALUE".
     * 
     * @param pStart the start number
     * @param pCount the max publish count
     */
    public void startCallBackThread(final int pStart, final int pCount)
    {
        final ICallBackBroker broker = SessionContext.getCurrentInstance().getCallBackBroker();
        
        Thread th = new Thread(new Runnable()
        {
            private int i = pStart;
            
            public void run()
            {
                PublishState state = PublishState.Completed;
                
                while (state == PublishState.Completed && i < pCount + pStart)
                {
                    try
                    {
                        Thread.sleep(200);
                        
                        state = broker.publish("INT_VALUE", Integer.valueOf(i++));
                    }
                    catch (InterruptedException ie)
                    {
                        //done
                    }
                }
            }
        });
        
        th.start();
    }
    
    /**
     * Gets the user name from the current session.
     * 
     * @return the user name
     */
    public String getSessionUserName()
    {
    	return SessionContext.getCurrentSession().getUserName();
    }
    
	/**
	 * Returns the address storage.
	 * 
	 * @return the address storage.
	 * @throws Throwable if the storage can not be opened
	 */
	public DBStorage getCompany() throws Throwable
	{
		DBStorage dbs = (DBStorage)get("company");
		
		if (dbs == null)
		{
			dbs = new DBStorage();

			dbs.setDBAccess(getDataSource());
			dbs.setFromClause("FIRMEN");
			dbs.setWritebackTable("FIRMEN");
			dbs.open();
			
			dbs.eventBeforeUpdate().addListener(this, "doBeforeUpdate");
			
			put("company", dbs);
		}
		
		return dbs;
	}	
	
	/**
	 * Invoked before updating an address.
	 * 
	 * @param pEvent the storage event
	 */
	public void doBeforeUpdate(StorageEvent pEvent)
	{
		pEvent.getNew().put("NAME", SessionContext.getCurrentSession().getUserName());
	}
	
	/**
	 * A simple action which returns "It works: ${pId}".
	 *  
	 * @param pId the id to return
	 * @return the text as byte array
	 * @throws Throwable if UTF-8 encoding isn't supported
	 */
	public byte[] getByteArray(Number pId) throws Throwable 
	{
		return ("It works: " + pId).getBytes("UTF-8");
	}	
	
}	// Session

