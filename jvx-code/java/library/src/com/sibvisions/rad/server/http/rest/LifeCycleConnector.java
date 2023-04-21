/*
 * Copyright 2011 SIB Visions GmbH
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
 * 11.12.2011 - [JR] - creation
 * 23.12.2012 - [JR] - #534: lifecycle name detection improved
 * 19.09.2014 - [JR] - #1115: used find of IAccessController
 * 14.09.2016 - [JR] - #1687: fixed NPE
 * 
 */
package com.sibvisions.rad.server.http.rest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.rad.server.ServerContext;

import org.restlet.Request;
import org.restlet.security.User;

import com.sibvisions.rad.server.DirectServerSession;
import com.sibvisions.rad.server.Server;
import com.sibvisions.rad.server.http.rest.service.RESTSessionContextImpl;
import com.sibvisions.rad.server.security.IAccessController;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.FileSearch;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>LifeCycleConnector</code> class is a <code>org.restlet.security.User</code> that contains
 * the connection for the user credentials.
 * 
 * @author René Jahn
 */
public class LifeCycleConnector extends User
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the application adapter. */
    private RESTAdapter adapter;
    
	/** the current session. */
	private DirectServerSession sesMaster;
	
	/** the current session. */
	private DirectServerSession sesSub;
	
	/** the LifeCycle cache. */
	private Hashtable<String, String> htCache = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>LifeCycleConnector</code> for the given server session.
	 * 
	 * @param pAdapter the application/REST adapter
	 * @param pMaster the master session
	 * @param pProperties the additional session properties
	 * @param pLifeCycleName the name of the sub session life-cycle object
	 * @throws Throwable if sub session creation fails
	 */
	LifeCycleConnector(RESTAdapter pAdapter, DirectServerSession pMaster, Map<String, Object> pProperties, String pLifeCycleName) throws Throwable
	{
		super(pMaster.getUserName());
		
		adapter = pAdapter;
		sesMaster = pMaster;
		
		String sLifeCycleName = pLifeCycleName;
		
		ClassLoader loader = adapter.getClassLoader(pMaster);

		//auto-detect life-cycle class name if the class is not available or the simple class name is
		//used
		try
		{
		    loadClass(loader, sLifeCycleName);
		}
		catch (ClassNotFoundException cnfe)
		{
			boolean bFound = true;
			
			IAccessController acc = Server.getInstance().getSessionManager().get(pMaster.getId()).getAccessController();
			
			//#1687
			if (acc != null)
			{
    			String sDotName = pLifeCycleName;
    			
    			if (sDotName.indexOf('.') < 0)
    			{
    			    sDotName = "." + sDotName;
    			}
    			
    			sLifeCycleName = acc.find(loader, sDotName);
			}
			else
			{
			    sLifeCycleName = null;
			}
			
			if (sLifeCycleName == null)
			{
				//try to find the class
				
				// 1) cache
				//---------------------------------
				
				if (htCache != null)
				{
					String sFoundName = htCache.get(pLifeCycleName);
					
					if (sFoundName != null)
					{
						sLifeCycleName = sFoundName;
						bFound = true;
					}
				}
				
				if (!bFound)
				{
					// 2) classes or lib
					//---------------------------------
					
					String sMaster = sesMaster.getLifeCycleName();
					
					String sMasterLoc = ResourceUtil.getLocationForClass(loader, ResourceUtil.getFqClassName(sMaster));
					
					if (sMasterLoc != null)
					{
						String sName = "/" + pLifeCycleName.replace(".", "/") + ".class";
	
						if (sMasterLoc.toLowerCase().endsWith(".jar"))
						{
							FileInputStream fis = null;
							
							try
							{
								fis = new FileInputStream(sMasterLoc);
								
								List<String> liJar = FileUtil.listZipEntries(fis);
								
								String sFoundName = null;
								
								int iFound = 0;
								
								for (String sEntry : liJar)
								{
									if (sEntry.endsWith(sName))
									{
										sFoundName = sEntry;
										iFound++;
									}
								}
								
								//more than one -> can't load the right one!
								if (iFound == 1)
								{
									sLifeCycleName = sFoundName.substring(0, sFoundName.length() - 6).replace("/", ".");
									
									bFound = true;
								}
							}
							finally
							{
								if (fis != null)
								{
									try
									{
										fis.close();
									}
									catch (Exception e)
									{
										//nothing to be done
									}
								}
							}
						}
						else
						{
							//could be a directory
							File fiMasterLoc = new File(sMasterLoc);
							
							if (fiMasterLoc.exists())
							{
								FileSearch fs = new FileSearch();
								fs.search(fiMasterLoc, true, "*" + sName);
								
								List<String> fiFound = fs.getFoundFiles();
								
								if (fiFound.size() == 1)
								{
									String sFile = fiFound.get(0);
									
									//remove directory
									sFile = sFile.substring(fiMasterLoc.getAbsolutePath().length() + 1);
									//remove .class
									sFile = sFile.substring(0, sFile.length() - 6);
									sFile = sFile.replace(File.separatorChar, '.');
									
									sLifeCycleName = sFile;
									
									bFound = true;
								}
							}
						}
					}
	
					//verify found class
					
					if (bFound)
					{
						try
						{
						    loadClass(loader, sLifeCycleName);
						}
						catch (ClassNotFoundException cnfex)
						{
							bFound = false;
						}
					}
					
					// 3) specific locations
					//---------------------------------
					
					if (!bFound)
					{
						int iPos = sMaster.lastIndexOf('.');
	
						String sMasterPack;
						
						//detect the package of the master session
						if (iPos >= 0)
						{
							sMasterPack = sMaster.substring(0, iPos);
						}
						else
						{
							sMasterPack = "";
						}
	
						
						List<XmlNode> liNodes = sesMaster.getConfig().getNodes("/application/rest/search/path");
						
						ArrayUtil<String> auSearchPath = new ArrayUtil<String>();
						//add default locations
						auSearchPath.add(sMasterPack);
						auSearchPath.add(sMasterPack + ".screens");
						
						if (liNodes != null)
						{
							String sValue;
							
							for (XmlNode xmn : liNodes)
							{
								sValue = xmn.getValue();
								
								if (sValue != null && sValue.trim().length() > 0 && auSearchPath.indexOf(sValue) < 0)
								{
									auSearchPath.add(sValue);
								}
							}
						}
						
						String sFoundName = null;
						
						for (int i = 0, anz = auSearchPath.size(); i < anz; i++)
						{
							try
							{
							    loadClass(loader, auSearchPath.get(i) + "." + pLifeCycleName);
								
								if (bFound)
								{
									sFoundName = null;
								}
								else
								{
									sFoundName = auSearchPath.get(i) + "." + pLifeCycleName;
									bFound = true;
								}
							}
							catch (ClassNotFoundException cnfex)
							{
								//search next path
							}
						}
						
						if (bFound && sFoundName != null)
						{
							//more than one -> can't load the right one!
							sLifeCycleName = sFoundName;
						}
					}
					
					//important: check instance!!!
					if (sLifeCycleName != pLifeCycleName)
					{
						if (htCache == null)
						{
							htCache = new Hashtable<String, String>();
						}
						
						htCache.put(pLifeCycleName, sLifeCycleName);
					}
				}
			}
		}
		
		adapter.configureSessionParameter(pProperties);
		
		if (sLifeCycleName == null)
		{
			throw new SecurityException("LifeCycle object '" + pLifeCycleName + "' wasn't found!");
		}

		//if we try to access the master LCO, don't create a sub session
		if (!sLifeCycleName.equals(pMaster.getLifeCycleName()))
		{
			
			sesSub = pMaster.createSubSession(sLifeCycleName, pProperties);
			sesSub.setSessionContextImpl(RESTSessionContextImpl.class);
			
			adapter.configureSession(sesSub);
		}
		else
		{
			sesSub = pMaster;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	public DirectServerSession getSession()
	{
		return sesSub;
	}
	
	/**
	 * Loads a class with the given classloader.
	 * 
	 * @param pLoader the loader
	 * @param pClassName the full qualified class name
	 * @return the class
	 * @throws ClassNotFoundException if the class was not found
	 */
	private Class loadClass(ClassLoader pLoader, String pClassName) throws ClassNotFoundException
	{
        return Class.forName(pClassName, false, pLoader);
	}
	
	/**
	 * Destroys the session.
	 */
	@Deprecated
	void destroy()
	{
		sesMaster.destroy();
	}

	/**
	 * Destroys the {@link LifeCycleConnector} from the current request, if available.
	 * 
	 * @param pRequest the current request
	 */
	@Deprecated
	public static void destroy(Request pRequest)
	{
		ServerContext ctxt = ServerContext.getCurrentInstance();

		if (ctxt instanceof RESTServerContextImpl 
			&& ((RESTServerContextImpl)ctxt).isManaged())
		{
			return;
		}

		LifeCycleConnector lcon = (LifeCycleConnector)pRequest.getClientInfo().getUser();

		if (lcon != null)
		{
			try
			{
			    lcon.destroy();
			}
			catch (Throwable th)
			{
				//nothing to be done
			}
		}		
	}
	
}	// LifeCycleConnector
