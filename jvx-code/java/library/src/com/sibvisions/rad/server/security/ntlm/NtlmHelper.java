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
 * 03.11.2008 - [JR] - creation
 * 12.12.2008 - [JR] - constructor for reading the application configuration
 *                   - setProperty removed
 *                   - application name switching implemented
 *                   - synchronized access
 *                   - made a singleton, because of the jcifs library design
 */
package com.sibvisions.rad.server.security.ntlm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.rad.server.IConfiguration;
import javax.rad.server.SessionContext;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import jcifs.smb.NtlmChallenge;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;
import jcifs.util.Base64;
import jcifs.util.LogStream;

import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>NtlmHelper</code> provides ntlm authentication through jcifs.
 * 
 * @author René Jahn
 */
public final class NtlmHelper
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the default authentication-info timeout. */
    private static final int DEFAULT_ALIVEDELAY = 15000;
    
	/** the logger. */
	private ILogger log = LoggerFactory.getInstance(getClass());

	
	/** the singleton instance of <code>NtlmHelper</code>. */
	private static NtlmHelper instance = null;
	
	/** the list of configured domain controllers. */
    private ArrayList<String> alDomainController;
    
    /** the domain controller which is currently used. */
    private UniAddress uaCurrentDomainController;

    /** the synchronization object. */
    private Object oSync = new Object();

    /** the application name for which the <code>NtlmHelper</code> is configured. */
    private String sApplicationName = null;

    /** the current jcifs configuration. */
    private String sCurrentConfig = null;
    
    /** the index in the list, for the next domain controller. */ 
    private int iCurrentDomainController;

	/** the mark for using load balancing. */
    private boolean bLoadBalance;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invisible constructor because <code>NtlmHelper</code> is a singleton.
     */
    private NtlmHelper()
    {
    }
    
    /**
     * Gets the current instance of <code>NtlmHelper</code>.
     * 
     * @return the singleton instance of <code>NtlmHelper</code>
     */
    public static synchronized NtlmHelper getInstance()
    {
    	if (instance == null)
    	{
    		instance = new NtlmHelper();
    	}
    	
    	return instance;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the application name where the configuration will be found.
     * 
     * @param pApplicationName the application name
     */
    public void setApplicatioName(String pApplicationName)
    {
		sApplicationName = pApplicationName;
    }
	
    /**
     * Sets the configuration for the jcifs library, if the application or
     * the configuration of the application has changed. The configuration
     * is always up-to-date.
     * 
     * @throws Exception if the application configuration has errors
     */
	private void setConfig() throws Exception
	{
		IConfiguration config = SessionContext.getCurrentSessionConfig();
		
		List<XmlNode> liParams;
		
		if (config == null)
		{
			liParams = Configuration.getApplicationZone(sApplicationName).getNodes("/application/ntlm/init-param");
		}
		else
		{
			liParams = config.getNodes("/application/ntlm/init-param");
		}

		//caching is needed to avoid unimportant configuration updates
		if (sCurrentConfig == null || !sCurrentConfig.equals(liParams.toString()))
		{
			//-------------------------------------------------
			// Load configuration
			//-------------------------------------------------

			//Defaults
			Config.setProperty("jcifs.netbios.cachePolicy", "1200");

			//copy settings to jcifs Config
			if (liParams != null)
			{
				XmlNode ndParam;
				XmlNode ndName;
				XmlNode ndValue;
				
				for (int i = 0, anz = liParams.size(); i < anz; i++)
				{
					ndParam = liParams.get(i);
					
					ndName = ndParam.getNode("param-name"); 
					ndValue = ndParam.getNode("param-value");
					
					if (ndName != null && ndValue != null)
					{
						//ignore defaults
						Config.setProperty(ndName.getValue(), ndValue.getValue());
					}
					else
					{
						log.debug("Parameter: '", ndName, "' is incorrect!");
					}
				}
			}

			//Important settings (otherwise we would have problems)
			Config.setProperty("jcifs.lmCompatibility", "0");
			Config.setProperty("jcifs.smb.client.useExtendedSecurity", "false");
			
			//-------------------------------------------------
			// Cache values
			//-------------------------------------------------
			
			// Authentication properties
	
			String sDefaultDomain = Config.getProperty("jcifs.smb.client.domain");
			String sDomainControllerList = Config.getProperty("jcifs.http.domainController");
			
			if (sDomainControllerList == null)
			{
				bLoadBalance = Config.getBoolean("jcifs.http.loadBalance", true);
			
				if (sDefaultDomain == null) 
				{
					try 
					{
						sDomainControllerList = InetAddress.getLocalHost().getHostName();
			        }
					catch (UnknownHostException uhe) 
					{
						sDomainControllerList = "127.0.0.1";
			        }
			    }
				else 
				{
					sDomainControllerList = sDefaultDomain;
				}
	
				sDomainControllerList = sDefaultDomain;
			}
			else
			{
				bLoadBalance = false;
			}
			
		    if (!bLoadBalance) 
		    {
		    	//support for multiple domain controller
		        alDomainController = new ArrayList<String>();
		        StringTokenizer tok = new StringTokenizer(sDomainControllerList, ",");
		        
		        while (tok.hasMoreTokens()) 
		        {
		        	alDomainController.add(tok.nextToken());
		        }
		        
		        iCurrentDomainController = 0;
		        uaCurrentDomainController = null;
		    }
			
			// Logging
		    
			int iLevel = Config.getInt("jcifs.util.loglevel", -1);
			if (iLevel != -1)
			{
				LogStream.setLevel(iLevel);
			}
			
			sCurrentConfig = liParams.toString();
		}
	}

	/**
	 * Returns whether the load balance option will be used for authentication.
	 * 
	 * @return <code>true</code> if load balance will be used; otherwise <code>false</code>
	 */
	public boolean isLoadBalance()
	{
		synchronized(oSync)
		{
			return bLoadBalance;
		}
	}
	
	/**
	 * Gets a session with for a domain controller with the adequate challenge.
	 * 
	 * @return the session
	 * @throws Throwable if it's not possible to detect a domain controller or challenge
	 */
	public NtlmSession getSession() throws Throwable
	{
		synchronized(oSync)
		{
			setConfig();
			
			return getSessionIntern();
		}
	}

	/**
	 * Gets a session with for a domain controller with the adequate challenge. This
	 * method is NOT synchronized and does NOT update the configuration. 
	 * Use it with care and only in pre-configured synchronized blocks!
	 * 
	 * @return the session
	 * @throws Throwable if it's not possible to detect a domain controller or challenge
	 */
	private NtlmSession getSessionIntern() throws Throwable
	{
		UniAddress uaDomainController;
		
		byte[] challenge = null;

	
		if (bLoadBalance)
		{
			NtlmChallenge chal = SmbSession.getChallengeForDomain();

			uaDomainController = chal.dc;
			challenge = chal.challenge;
		}
		else
		{
			//domain controller list support
			int iDomainController = iCurrentDomainController;
			
			uaDomainController = uaCurrentDomainController;

			do 
			{
				try 
				{
					if (uaDomainController == null)
					{
						uaDomainController = UniAddress.getByName(alDomainController.get(iDomainController), true);
					}

					challenge = SmbSession.getChallenge(uaDomainController);

					log.info("Mode DomainController '",
							 uaDomainController,
							 "', #",
							 Integer.valueOf(iDomainController), 
						     " challenge = ",
						     (challenge != null ? Base64.encode(challenge) : null));
					
					uaCurrentDomainController = uaDomainController;
					iCurrentDomainController  = iDomainController;
				}
				catch (Throwable th) 
				{
					log.info("Mode DomainController '",
							 alDomainController.get(iDomainController), 
							 "', #",
							 Integer.valueOf(iDomainController),
							 " failed");

					//try the next domain controller
					uaDomainController = null;
					iDomainController = (iDomainController + 1) % alDomainController.size();

					//only one rotation is possible
					if (iDomainController == iCurrentDomainController) 
					{
						throw th;
					}
				}
			}
			while (uaCurrentDomainController == null);
		}
		
		return new NtlmSession(uaCurrentDomainController, challenge);
	}
	
	/**
	 * Logs on to a domaincontroller with username and password. The configured domain name will
	 * be used, if the username doesn't contain a domain information (DOAMIN&#092;username).
	 * 
	 * @param pDomain the domain for the logon
	 * @param pUserName the username
	 * @param pPassword the password
	 * @return the authentication information
	 * @throws Throwable if it is not possible to logon
	 */
	public NtlmAuthInfo logon(String pDomain, String pUserName, String pPassword) throws Throwable
	{
		synchronized(oSync)
		{
			setConfig();

			NtlmSession session = getSessionIntern();
			
			NtlmPasswordAuthentication ntlmPwd = new NtlmPasswordAuthentication(pDomain, pUserName, pPassword);
			
			return logon(session, ntlmPwd);
		}
	}
	
	/**
	 * Logs on to a domaincontroller with the current credentials. The credentials
	 * will be detected automatically.
	 * 
	 * @return the authentication information or <code>null</code> if it's not possible
	 *         to log on with the credentials
	 * @throws Throwable if the credentials are valid but the logon failed
	 */
	public NtlmAuthInfo logon() throws Throwable
	{
		String sCredentials;

		//-------------------------------------------------
		// Class access
		//-------------------------------------------------
		
		Class<?> clsAuth = Class.forName("sun.net.www.protocol.http.NTLMAuthSequence");
		
		Constructor<?> consAuth = clsAuth.getDeclaredConstructor(String.class, String.class, String.class);
		
		consAuth.setAccessible(true);
		
		Object oAuth = consAuth.newInstance(null, null, null);
		
		Method methAuth = clsAuth.getMethod("getAuthHeader", String.class);
		
		//-------------------------------------------------
		// Prepare credentials (copied from NtlmSsp class)
		//-------------------------------------------------

		sCredentials = (String)methAuth.invoke(oAuth, (String)null);

		synchronized(oSync)
		{
			setConfig();

			NtlmSession session = getSessionIntern();
			
			byte[] bySrc = Base64.decode(sCredentials);
	        
	        if (bySrc[8] == 1) 
	        {
	            Type1Message type1 = new Type1Message(bySrc);
	            Type2Message type2 = new Type2Message(type1, session.getChallenge(), null);
	            
	            sCredentials = (String)methAuth.invoke(oAuth, Base64.encode(type2.toByteArray()));
	        } 
	        	            
	        bySrc = Base64.decode(sCredentials);
	        	            
	        if (bySrc[8] == 3) 
	        {
	            Type3Message type3 = new Type3Message(bySrc);
	            
	            byte[] lmResponse = type3.getLMResponse();
	            
	            if (lmResponse == null)
	            {	
	            	lmResponse = new byte[0];
	            }
	            
	            byte[] ntResponse = type3.getNTResponse();
	            
	            if (ntResponse == null) 
	            {
	            	ntResponse = new byte[0];
	            }
	            
	            NtlmPasswordAuthentication ntlmPwd = new NtlmPasswordAuthentication(type3.getDomain(),
	            															   		type3.getUser(), 
	            															   		session.getChallenge(), 
	            															   		lmResponse, 
	            															   		ntResponse); 
	            
	            return logon(session, ntlmPwd);
	        }
	        
	        return null;
		}
	}
	
	/**
	 * Logs on to a domaincontroller with prevalidated credentials.
	 *
	 * @param pSession the domaincontroller and challenge
	 * @param pAuthentication prevalidated credentials 
	 * @return the authentication information
	 * @throws Throwable if it is not possible to logon
	 */
	public NtlmAuthInfo logon(NtlmSession pSession, NtlmPasswordAuthentication pAuthentication) throws Throwable
	{
		try
		{
			synchronized(oSync)
			{
				setConfig();

				SmbSession.logon(pSession.getDomainController(), pAuthentication);
			}

			log.info(pAuthentication,
					 " successfully authenticated against ",
					 pSession.getDomainController());
			
			//store the key for later use
			return new NtlmAuthInfo(pAuthentication, pSession, Config.getInt("alivedelay", DEFAULT_ALIVEDELAY));
		}
		catch (SmbAuthException sae)
		{
			log.info(pAuthentication.getName(),
					 ": 0x",
					 jcifs.util.Hexdump.toHexString(sae.getNtStatus(), 8),
					 ": ",
					 sae);
			
			throw sae;
		}
	}
	
}	// NtlmHelper
