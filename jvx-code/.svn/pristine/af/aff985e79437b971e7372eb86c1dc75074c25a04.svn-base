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
 * 29.04.2009 - [JR] - getServerDir implemented
 * 26.08.2009 - [JR] - initDirectories: check the directory structure if the path contains the basedir
 * 26.08.2010 - [JR] - removed classes property because it's not relevant for the general configuration
 * 04.11.2010 - [JR] - isApplication implemented
 * 18.11.2010 - [JR] - #206: changed ApplicationZone creation and isApplication check
 * 11.04.2011 - [JR] - #333: application names are now case sensitive
 * 13.04.2011 - [JR] - ApplicationListOption used
 * 28.07.2011 - [JR] - #445: support virtual filesystems e.g. JBoss
 * 16.06.2013 - [JR] - #673: support external apps folders (see AppSettings)
 * 03.07.2013 - [JR] - #713: getApplicationZone: search config.xml
 * 02.10.2014 - [JR] - #1126: changed application zone and server zone detection
 * 15.05.2015 - [JR] - #1387: support VFS e.g. WildFly (refactoring of #445 and "improvements")
 * 22.11.2016 - [JR] - used AppLocation instead of String and checked isIncluded
 * 28.03.2019 - [JR] - #2002: support extracting rad directory from war resource
 */
package com.sibvisions.rad.server.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.InitialContext;

import com.sibvisions.rad.IPackageSetup;
import com.sibvisions.rad.server.config.AppSettings.AppLocation;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.SecureHash;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * The <code>Configuration</code> enables the access to the server zone and 
 * all available application zones.
 * The zones are organized in following directories:
 * <pre>
 * <b>&lt;CONFIG-DIRECTORY&gt;</b>
 * |- <b>apps</b>                   (applications directory)
 *    |- <b>&lt;application name&gt;</b>  <b>(application zone)</b> (application directory - case sensitive)
 *       |- <b>src</b>              (source files)
 *       |- <b>libs</b>             (additional libraries)
 *       |- <b>classes</b>          (compiled sources)
 *       |- config.xml       (application configuration file)
 * |- <b>server</b>                 <b>(server zone)</b> (server directory)
 *    |- config.xml          (server configuration file)
 * </pre>
 * 
 * @author René Jahn
 */
public final class Configuration
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Application name restriction. */
	public enum ApplicationListOption
	{
		/** All application names are allowed. */
		All,
		/** Only visible application names are allowed. */
		Visible,
		/** Only hidden application names are allowed. */
		Hidden
	}
	
	/** the name of the configuration directory. */
	private static final String NAME_RAD = "rad";

	/** the name of the applications directory. */
	private static final String NAME_APPS = "apps";
	
	/** the name of the server directory. */
	private static final String NAME_SERVER = "server";
	
	
	/** the last value of the system property for the base directory. */
	private static String sOldBaseDir = null;
	
	/** the resource directory. */
	private static File fiBaseDir = null;
	
	/** the configuration directory. */
	private static File fiConfigDir = null;
	
	/** the applications directory. */
	private static File fiAppsDir = null;
	
	/** the server directory. */
	private static File fiServerDir = null;
	
	/** cached application zones. */
	private static Hashtable<String, ApplicationZone> htAppZones = null;
	
	/** the server zone. */
	private static ServerZone zoServer = null;
	
	/** the app settings. */
	private static AppSettings asApps = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invisible constructor, because the <code>Configuration</code> class is a utility class.
	 */
	private Configuration()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Initialize the directory locations. The locations are dependent of the
	 * system Property {@value IPackageSetup#CONFIG_BASEDIR}. If the system property
	 * is not set, the location of the directories is dependent from the location
	 * of the <code>Configuration</code> class path.
	 */
	private static void initDirectories()
	{
		String sBaseDir = ResourceUtil.getAccessibleProperty(IPackageSetup.CONFIG_BASEDIR, "");
		
		//Only the system property can be changed, the location of the Configuration.class is
		//always in the same directory!
		if (sOldBaseDir == null || !sBaseDir.equals(sOldBaseDir))
		{
		    clearCache();
		    
			String sConfigClassName = ResourceUtil.getFqClassName(Configuration.class); 
			
			//Start searching at the place where classes or jar files are contained
			String sPath = ResourceUtil.getLocationForClass(sConfigClassName);

			if (sPath != null)
			{
				//If the system property is set, then the directory must exist!
				if (sBaseDir.trim().length() == 0 || !new File(sBaseDir).exists())
				{
					File fiPath = new File(sPath);
					
					if (!fiPath.isDirectory())
					{
						//The classes are included in a jar file? We assume that class files are contained
						//in a classes directory of the parent directory
						fiPath = new File(fiPath.getParentFile().getParentFile(), "classes");
					}
					
					//Check if we are a sub directory of the application, e.g. during development:
					//<Directory>/rad/apps/<name>/libs/server/<jarfile>
					fiBaseDir = fiPath.getParentFile();
				}
				else
				{
					//Use the system property
					fiBaseDir = new File(sBaseDir);
				}

				//FIRST step: check RAD
                fiConfigDir = new File(fiBaseDir, NAME_RAD);
				
                if (!fiConfigDir.exists())
                {
    				//Check if the current basedir is a sub directory of the expected basedir
    				File fiSub = fiBaseDir;
    				String sSubName;
    				
    				int iDepth = 0;
    				
    				while (fiSub != null && iDepth < 5)
    				{				
    				    iDepth++;
    				    
    					sSubName = fiSub.getName();
    					
    					if (sSubName.equalsIgnoreCase(NAME_APPS) || sSubName.equalsIgnoreCase(NAME_SERVER))
    					{
    						fiSub = fiSub.getParentFile();
    
    						//parent directory must be the RAD directory!
    						if (fiSub != null && fiSub.getName().equals(NAME_RAD))
    						{
    							fiBaseDir = fiSub.getParentFile();
    							
    							//FOUND
    			                fiConfigDir = new File(fiBaseDir, NAME_RAD);

    			                fiSub = null;
    						}
    					}
    					else
    					{
    						fiSub = fiSub.getParentFile();
    					}
    				}
                }
				
                if (!fiConfigDir.exists())
                {
                    InputStream stream = null;
                    
                    URL urlZip = null;

                    try
                    {
                        URL urlJar = ResourceUtil.getResource("/jvx_rad.jar");
                    
                        InputStream streamJar = urlJar.openStream(); 
                    
                        if (streamJar != null)
                        {
                            urlZip = urlJar;
                            
                            stream = streamJar;
                        }
                    }
                    catch (Exception e)
                    {
                        LoggerFactory.getInstance(Configuration.class).debug("Loading /jvx_rad.jar failed!", e);
                    }
                    
                    //resource was not found -> try to load from filesystem
                    if (stream == null)
                    {
                        URL url = ResourceUtil.getResource(ResourceUtil.getFqClassName(Configuration.class));

                        String sUrl = url.toExternalForm();

                        sUrl = sUrl.substring(0, sUrl.indexOf(sConfigClassName));

                        try
                        {
                            int iPos = sUrl.lastIndexOf('/');
                            
                            iPos = sUrl.lastIndexOf('/', iPos - 1);
                            
                            String sBaseUrl = sUrl.substring(0, iPos);

                            sUrl = sBaseUrl + "/lib/jvx_rad.jar";
                            
                            try
                            {
                                URL urlJar = new URL(sUrl);
                                
                                InputStream streamJar = urlJar.openStream(); 
                            
                                if (streamJar != null)
                                {
                                    urlZip = urlJar;
                                    
                                    stream = streamJar;
                                }
                            }
                            catch (Exception e)
                            {
                                LoggerFactory.getInstance(Configuration.class).debug("Loading /lib/jvx_rad.jar failed!", e);
                            }
                            
                            if (stream == null)
                            {
                            	//War file mode (not exploded/unpacked)
                            	if (sBaseUrl.indexOf("*/") >= 0)
                            	{
                            		String sBaseUrlNoAsterisk = sBaseUrl.replace("*/", "!/");
                            		
                            		sUrl = sBaseUrlNoAsterisk + "/lib/jvx_rad.jar";
                                	
                            		String[] sTypes = new String[] {"jar:", "war:"};
                            		
                            		for (int i = 0; i < sTypes.length && stream == null; i++)
                            		{
	                            		try
	                            		{
	                            			URL urlJar = new URL(sUrl.replace("jar:war:", sTypes[i]));
	                            			
	                                        InputStream streamJar = urlJar.openStream(); 
	                                        
	                                        if (streamJar != null)
	                                        {
	                                            urlZip = urlJar;
	                                            
	                                            stream = streamJar;
	                                        }
	                            		}
	                            		catch (Exception e)
	                            		{
	                            			LoggerFactory.getInstance(Configuration.class).debug("Loading /lib/jvx_rad.jar failed!", e);
	                            		}
                            		}
                            	}
                            }

                            //Fallback
                            if (stream == null)
                            {
                            	try
                            	{
	                                sUrl = sBaseUrl + "/rad.zip";
	    
	                                urlZip = new URL(sUrl);
	    
	                                stream = urlZip.openStream();
                            	}
                            	catch (Exception e)
                            	{
                            		urlZip = null;
                            		
	                                LoggerFactory.getInstance(Configuration.class).debug("Loading /rad.zip failed!", e);
                            	}                            	
                            }
                        }
                        catch (Exception e)
                        {
                            LoggerFactory.getInstance(Configuration.class).debug("Loading rad from resource failed!", e);
                        }
                    }
                    
                    if (stream != null)
                    {
                        try
                        {
                            String sMD5 = SecureHash.getHash(SecureHash.MD5, stream);
                     
                            stream = CommonUtil.close(stream);
                            stream = urlZip.openStream();
                                                        
                            File fiTemp = new File(System.getProperty("java.io.tmpdir"), "rad_" + sMD5);
        
                            //unzip only when the archive was changed. It is allowed to change
                            //the configuration of already installed applications, until it is installed
                            //again
                            if (!fiTemp.exists())
                            {
                                FileUtil.unzip(stream, fiTemp);
                            }
                            
                            fiConfigDir = new File(fiTemp, NAME_RAD);
                        }
                        catch (Exception e)
                        {
                            //config not found!
                            
                            LoggerFactory.getInstance(Configuration.class).debug("Base directory is not available!", e);
                        }
                    }
                }
                
				fiAppsDir   = new File(fiConfigDir, NAME_APPS);
				fiServerDir = new File(fiConfigDir, NAME_SERVER);
				
				sOldBaseDir = sBaseDir;
			}
		}
	}
	
	/**
	 * Returns the directory with the configuration files.
	 * 
	 * @return the configuration directory
	 */
	public static File getConfigurationDir()
	{
		initDirectories();
		
		return fiConfigDir;
	}
	
	/**
	 * Returns the directory with the applications.
	 * 
	 * @return the applications directory
	 */
	public static File getApplicationsDir()
	{
		initDirectories();
		
		return fiAppsDir;
	}
	
	/**
	 * Returns the server directory.
	 * 
	 * @return the server directory
	 */
	public static File getServerDir()
	{
		initDirectories();
		
		return fiServerDir;
	}

	/**
	 * Returns the current zone for an application.
	 * 
	 * @param pApplicationName the desired application
	 * @return the zone for the application of null if the application is not available
	 * @throws Exception if the application zone has errors
	 */
	public static ApplicationZone getApplicationZone(String pApplicationName) throws Exception
	{
		ApplicationZone app = null;
				
		if (pApplicationName == null || pApplicationName.trim().length() == 0)
		{
			app = new ApplicationZone((File)null);
		}
		else
		{
			if (htAppZones != null)
			{
				app = htAppZones.get(pApplicationName);
				
				if (app != null)
				{
					//always use live config (resets unwanted user-defined setting!)
					app.setUpdateEnabled(true);
				}
			}
			
			//It's possible that the application will be deleted. In that case, the
			//already loaded zone will be invalid!
			//It's better to clean the cache and try loading the configuration again,
			//to throw the expected exceptions
			if (app != null && !app.isValid())
			{
				htAppZones.remove(pApplicationName);
				
				app = null;
			}
			
			//Try to load the application zone from the applications directory
			if (app == null)
			{
				File fiApp = new File(getApplicationsDir(), pApplicationName);
				
				if (!ApplicationZone.isValid(fiApp))
				{
					AppSettings aset = getAppSettings();
					
					List<AppLocation> liLocations = aset.getAppLocations();
					
					if (liLocations != null)
					{
						File fiExternalApp;
						
						AppLocation loc;
						
						for (int i = 0, anz = liLocations.size(); i < anz && app == null; i++)
						{
						    loc = liLocations.get(i);
						    
							fiExternalApp = new File(loc.getPath(), pApplicationName); 
							
							if (ApplicationZone.isValid(fiExternalApp))
							{
								app = new ApplicationZone(fiExternalApp);
							}
						}
					}
				}
				else
				{
					//valid application found
					app = new ApplicationZone(fiApp);
				}
				
				if (app == null)
				{
					//try to find config.xml "upwards" (but config.xml must be an application config.xml!
					
					File fiParent = getApplicationsDir();
					
					File fiConfig;
					
					XmlWorker xmw = new XmlWorker();
					XmlNode xmn;
				
					int iDepth = 0;
					
					while (fiParent != null 
						   && app == null
						   && iDepth < 5)
					{
					    iDepth++;
					    
						fiConfig = new File(fiParent, Zone.NAME_CONFIG);
						
						if (fiConfig.exists() && fiConfig.canRead())
						{
							//found directory that equals the application name -> config.xml is ok
							if (pApplicationName.equals(fiParent.getName()))
							{
								app = new ApplicationZone(fiParent);
							}
							else
							{
								try
								{
									xmn = xmw.read(fiConfig);
									
									//an empty xml is ok, and if not empty, it must contain /application
									if (xmn == null 
										|| xmn.size() == 0 // the same as xmn.getSubNodes() == null  
										|| xmn.getNode("/application") != null)
									{
										app = new ApplicationZone(fiParent);
									}
								}
								catch (Exception e)
								{
									LoggerFactory.getInstance(Configuration.class).debug("Invalid config.xml '", fiConfig, "'", e);
								}
							}
						}
						
						fiParent = fiParent.getParentFile();
					}
				}				

				if (app == null)
				{
    				//JNDI support
    				InputStream is = loadConfiguration(pApplicationName.toLowerCase());
    				
    				if (is != null)
    				{
    				    app = new ApplicationZone(is);
    				}
				}
				
				//try to load the app config as resource => automatic validation, if found
				if (app == null)
				{
				    if (isSearchClassPath())
				    {
    				    InputStream stream = ResourceUtil.getResourceAsStream("/rad/apps/" + pApplicationName.toLowerCase() + "/config.xml");
    				    
                        if (stream == null)
                        {
                            stream = ResourceUtil.getResourceAsStream(pApplicationName.toLowerCase() + ".xml");
                        }

                        if (stream == null)
    				    {
    				        stream = ResourceUtil.getResourceAsStream("/config.xml");
    				    }
    				    
    				    if (stream != null)
    				    {
    				        app = new ApplicationZone(stream);
    				    }
				    }
				}
				
				//Throw an exception if not found!
				if (app == null)
				{
					app = new ApplicationZone(fiApp);
				}
				
				if (htAppZones == null)
				{
					htAppZones = new Hashtable<String, ApplicationZone>();
				}
				
				htAppZones.put(pApplicationName, app);
			}
		}
		
		return app;
	}
	
	/**
	 * Returns the server zone.
	 * 
	 * @return the server zone
	 */
	public static ServerZone getServerZone()
	{
		if (zoServer == null)
		{
            //try to find the server zone, if server directory doesn't exist

		    File fiDir = getServerDir(); 
		    
		    if (fiDir != null)
		    {
    		    if (!fiDir.exists())
    		    {
                    File fiParent = fiDir;
                    
                    File fiConfig;
                    
                    XmlWorker xmw = new XmlWorker();
                    XmlNode xmn;
                    
                    ServerZone zone = null;
                    
                    int iDepth = 0;
                    
                    while (fiParent != null 
                           && zone == null
                           && iDepth < 5)
                    {
                        iDepth++;
                        
                        fiConfig = new File(fiParent, "server.xml");
                        
                        if (fiConfig.exists() && fiConfig.canRead())
                        {
                            try
                            {
                                xmn = xmw.read(fiConfig);
                                
                                //an empty xml is ok, and if not empty, it must contain /server
                                if (xmn == null 
                                    || xmn.size() == 0 // the same as xmn.getSubNodes() == null 
                                    || xmn.getNode("/server") != null)
                                {
                                    zoServer = new ServerZone(fiParent, "server.xml");
                                    
                                    return zoServer;
                                }
                            }
                            catch (Exception e)
                            {
                                LoggerFactory.getInstance(Configuration.class).debug("Invalid server.xml '", fiConfig, "'", e);
                            }
                        }
                        
                        fiParent = fiParent.getParentFile();
                    }
                }
    		    else
    		    {
    	            try
    	            {
    	                ServerZone zone = new ServerZone(fiDir);
    	                
    	                //server node must exist!
    	                if (zone.getNode("/server") != null)
    	                {
    	                    zoServer = zone;
    	                    
    	                    return zoServer;
    	                }
    	            }
    	            catch (Exception e)
    	            {
    	                //should not happen because we checked the file 
    	                throw new RuntimeException(e);
    	            }
    		    }
		    }
		    
            //JNDI support
            InputStream is = loadConfiguration("server");
            
            if (is != null)
            {
                try
                {
                    zoServer = new ServerZone(is);
                    
                    return zoServer;
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
		    
            //try to load the app config as resource
            if (isSearchClassPath())
            {
                InputStream stream = ResourceUtil.getResourceAsStream("/rad/server/config.xml");
                
                if (stream == null)
                {
                    stream = ResourceUtil.getResourceAsStream("/server.xml");
                }
                
                if (stream != null)
                {
                    try
                    {
                        zoServer = new ServerZone(stream);
                        
                        return zoServer;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
		    
		    //create a virtual ServerZone
		    zoServer = new ServerZone();
		}
		else
		{
			//if the server zone is invalid, there must be a general problem -> null
			//causes an error in the accesing component 
			if (!zoServer.isValid())
			{
			    zoServer = null;
			}
			else
			{
				//always use live config (resets unwanted user-defined setting!)
				zoServer.setUpdateEnabled(true);
			}
		}
		
		return zoServer;
	}
	
	/**
	 * Gets a list of all available application names. This method doesn't check if applications
	 * are valid.
	 * 
	 * @param pListOption an option to ignore/list specific applications
	 * @return a list of available application names
	 */
	public static List<String> listApplicationNames(ApplicationListOption pListOption)
	{
		List<String> liApps = new ArrayUtil<String>(); 
		
		for (File file : listApplicationDirectories(pListOption))
		{
			liApps.add(file.getName());
		}
		
		return liApps;
	}
	
	/**
	 * Checks if a given application name exists as valid application.
	 * 
	 * @param pApplicationName an application name
	 * @return <code>true</code> if the application is available and is valid (configuration is available),
	 *         <code>false</code> otherwise
	 */
	public static boolean isApplication(String pApplicationName)
	{
		if (pApplicationName != null)
		{
			if (!ApplicationZone.isValid(new File(getApplicationsDir(), pApplicationName)))
			{
				AppSettings aset = getAppSettings();
				
				List<AppLocation> liLocations = aset.getAppLocations();
				
				if (liLocations != null)
				{
					File fiExternalApp;
					
					for (int i = 0, anz = liLocations.size(); i < anz; i++)
					{
						if (liLocations.get(i).isIncluded(pApplicationName))
						{
							fiExternalApp = new File(liLocations.get(i).getPath(), pApplicationName); 
							
							if (ApplicationZone.isValid(fiExternalApp))
							{
								return true;
							}
						}
					}
				}
				
				return false;
			}
			else
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets whether the given application is an internal application. This means that the path is inside
	 * the applications directory.
	 * 
	 * @param pApplicationName the name of the application
	 * @return <code>true</code> if application is available in applications directory, <code>false</code> otherwise
	 */
	public static boolean isInternalApplication(String pApplicationName)
	{
		if (pApplicationName != null)
		{
			return ApplicationZone.isValid(new File(getApplicationsDir(), pApplicationName));
		}

		return false;
	}
	
	/**
	 * Gets whether the given application is an external application. This means that the path is outside of
	 * the applications directory.
	 * 
	 * @param pApplicationName the name of the application
	 * @return <code>true</code> if application is not available in applications directory, <code>false</code> otherwise
	 */
	public static boolean isExternalApplication(String pApplicationName)
	{
		if (pApplicationName != null)
		{
			if (!ApplicationZone.isValid(new File(getApplicationsDir(), pApplicationName)))
			{
				AppSettings aset = getAppSettings();
				
				List<AppLocation> liLocations = aset.getAppLocations();
				
				if (liLocations != null)
				{
					File fiExternalApp;
					
					for (int i = 0, anz = liLocations.size(); i < anz; i++)
					{
						if (liLocations.get(i).isIncluded(pApplicationName))
						{
							fiExternalApp = new File(liLocations.get(i).getPath(), pApplicationName); 
							
							if (ApplicationZone.isValid(fiExternalApp))
							{
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * Gets the application settings.
	 * 
	 * @return the application settings
	 */
	public static AppSettings getAppSettings()
	{
		if (asApps == null)
		{
			initDirectories();

			try
			{
				asApps = new AppSettings(fiConfigDir);
			}
			catch (Exception e)
			{
				//shouldn't happen because AppSettings are optional
				throw new RuntimeException(e);
			}
		}
		
		return asApps;
	}
	
	/**
	 * Lists all applications directories.
	 * 
	 * @param pListOption the list option
	 * @return the list of application directories
	 */
	public static List<File> listApplicationDirectories(ApplicationListOption pListOption)
	{
		ArrayUtil<File> auApps = new ArrayUtil<File>();
		
		File[] fiApps = getApplicationsDir().listFiles();
		
		AppSettings aset = getAppSettings();
		
		List<AppLocation> liLocations = aset.getAppLocations();
		
		if (liLocations != null)
		{
			File[] fiExtApps;
			
			File fiDir;
			
			for (AppLocation loc : liLocations)
			{
				fiDir = new File(loc.getPath());
				
				if (fiDir.exists())
				{
					fiExtApps = fiDir.listFiles();
					
					if (fiExtApps != null && fiExtApps.length > 0)
					{
					    for (int i = 0; i < fiExtApps.length; i++)
					    {
					        if (fiExtApps[i].isDirectory() && loc.isIncluded(fiExtApps[i].getName()))
					        {
		                        fiApps = ArrayUtil.add(fiApps, fiExtApps[i]);
					        }
					    }
					}
				}
			}
		}
		
		if (fiApps != null)
		{
			if (pListOption == null)
			{
				pListOption = ApplicationListOption.Visible;
			}
			
			File fiConfig;
			
			for (File file : fiApps)
			{
				if (file.isDirectory())
				{
					fiConfig = new File(file, Zone.NAME_CONFIG);
					
					if (fiConfig.exists() && fiConfig.canRead())
					{
						switch (pListOption)
						{
							case Hidden:
								if (file.isHidden() || file.getName().charAt(0) == '.')
								{
									auApps.add(file);
								}
								break;
							case Visible:
								if (!file.isHidden() && file.getName().charAt(0) != '.')
								{
									auApps.add(file);
								}
								break;
							default:
								auApps.add(file);
						}
					}
				}
			}
				
		}
		
		return auApps;
	}
	
	/**
	 * Loads the zone configuration via JNDI.
	 * 
	 * @param pName the context name, e.g the appliacation name or <code>server</code>
	 * @return the stream for the zone configuration
	 */
	private static InputStream loadConfiguration(String pName)
    {
        try
        {
            InitialContext ctxt = new InitialContext();
            
            try
            {
                Object objInstance = ctxt.lookup("java:/comp/env/jvx/" + pName + "/config");
                
                if (objInstance instanceof String)
                {
                    InputStream stream = ResourceUtil.getResourceAsStream((String)objInstance);
                    
                    if (stream == null)
                    {
                        ByteArrayInputStream bais = new ByteArrayInputStream(((String)objInstance).getBytes("UTF-8"));
                        
                        //maybe XML (no special node checks)
                        XmlWorker.readNode(bais);
                        
                        bais.reset();
                        
                        return bais;
                    }
                    else
                    {
                        return stream;
                    }
                }
                else if (objInstance instanceof IVirtualZone)
                {
                    return ((IVirtualZone)objInstance).getConfiguration();
                }
                else if (objInstance instanceof InputStream)
                {
                    return (InputStream)objInstance;
                }
                
                return null;
            }
            finally
            {
                ctxt.close();
            }
        }
        catch (Exception ex)
        {
            LoggerFactory.getInstance(Configuration.class).debug("Couldn't load virtual zone '", pName, "' via JNDI!", ex);
            
            return null;
        }
    }

	/**
	 * Clears the zone cache.
	 */
	public static void clearCache()
	{
        zoServer = null;
        htAppZones = null;
        asApps = null;
	}
	
	/**
	 * Removes cached zones if not valid anymore.
	 */
	public static void cleanup()
	{
		if (htAppZones != null)
		{
			Hashtable<String, ApplicationZone> htCopy = new Hashtable<String, ApplicationZone>(htAppZones);
			
			ApplicationZone az;
			
			boolean bValid;
			
			for (Entry<String, ApplicationZone> entry : htCopy.entrySet())
			{
				az = entry.getValue();
				
				try
				{
					bValid = az.isValid();
				}
				catch (Exception e)
				{
					LoggerFactory.getInstance(Configuration.class).debug(e);
					
					bValid = false;
				}
				
				if (!bValid)
				{
					htAppZones.remove(entry.getKey());
				}
			}
		}
		
		if (zoServer != null)
		{
			if (!zoServer.isValid())
			{
				zoServer = null;
			}
		}
	}
	
	/**
	 * Gets whether resource search via classpath is enabled.
	 * 
	 * @return <code>true</code> if search resource via classpath is enabled
	 * @see IPackageSetup#CONFIG_SEARCH_CLASSPATH
	 */
	public static boolean isSearchClassPath()
	{
	    return ResourceUtil.getAccessibleBoolean(IPackageSetup.CONFIG_SEARCH_CLASSPATH, false);	    
	}
	
}	// Configuration
