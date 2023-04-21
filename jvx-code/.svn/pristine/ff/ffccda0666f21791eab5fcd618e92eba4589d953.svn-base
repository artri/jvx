/*
 * Copyright 2020 SIB Visions GmbH
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
 * 23.07.2020 - [JR] - creation
 */
package com.sibvisions.util;

import java.io.File;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>FileWatchdog</code> observes files and collects data like exists and modified.
 * This information is read in a separate thread to reduce file locking in environments with
 * slow cuncurrent disk IO.
 * 
 * @author René Jahn
 */
public final class FileWatchdog 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the logger. */
	private static ILogger log = LoggerFactory.getInstance(FileWatchdog.class);
	
	/** the check interval. */
	private static long lCheckInterval = 2000L;
	
	/** the duration for temp cache files. */
	private static long lTempDuration = 60000L;

	/** the path list. */
	private static ConcurrentHashMap<String, FileInfo> hmpPath = new ConcurrentHashMap<String, FileInfo>();
	
	/** thread for continuous file verifying. */
	private static Thread thCheck = null;
	
	/** whether file watchdog is enabled. */
	private static boolean bEnabled = true;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
		//Property is set? Use the value
		String sProp = System.getProperty("com.sibvisions.util.FileWatchDog.enabled");
		
		if (StringUtil.isEmpty(sProp))
		{
			bEnabled = Boolean.parseBoolean(sProp);
		}
	}
	
	/**
	 * Invisible constructor because <code>FileWatchdog</code> is a utility
	 * class.
	 */
	private FileWatchdog()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a file path to the watchlist.
	 * 
	 * @param pFile the file
	 */
	public static void add(File pFile)
	{
		if (bEnabled)
		{
			if (pFile == null)
			{
				return;
			}
		
			hmpPath.put(pFile.getAbsolutePath(), update(pFile, new FileInfo()));
			
			startCheck();
		}
	}
	
	/**
	 * Adds a file to the temporary watchlist. The file will be removed after a
	 * period of millis.
	 * 
	 * @param pFile the file
	 */
	public static void addTemporary(File pFile)
	{
		if (bEnabled)
		{
			if (pFile == null)
			{
				return;
			}

			hmpPath.put(pFile.getAbsolutePath(), update(pFile, new FileInfo(lTempDuration)));
		
			startCheck();
		}
	}
	
	/**
	 * Adds a file path to the watchlist.
	 * 
	 * @param pPath the file path
	 */
	public static void add(String pPath)
	{
		if (bEnabled)
		{
			if (pPath == null)
			{
				return;
			}
			
			add(new File(pPath));
		}
	}
	
	/**
	 * Removes a file from the watchlist.
	 * 
	 * @param pFile the file
	 */
	public static void remove(File pFile)
	{
		if (bEnabled)
		{
			if (pFile == null)
			{
				return;
			}
			
			remove(pFile.getAbsolutePath());
		}
	}
	
	/**
	 * Removes a file path from the watchlist.
	 * 
	 * @param pPath the file path
	 */
	public static void remove(String pPath)
	{
		if (bEnabled)
		{
			if (pPath == null)
			{
				return;
			}

			hmpPath.remove(pPath);
			
			startCheck();
		}
	}
	
	/**
	 * Gets last modified of file. This method checks the cache and
	 * if not found, delegates to the file.
	 * 
	 * @param pFile the file
	 * @return last modified millis
	 */
	public static long lastModified(File pFile)
	{
		startCheck();
		
		if (pFile == null)
		{
			return -1;
		}
		
		FileInfo fi = hmpPath.get(pFile.getAbsolutePath());
		
		if (fi != null)
		{
			return fi.lastModified;
		}
		else
		{
			addTemporary(pFile);
			
			return pFile.lastModified();
		}
	}
	
	/**
	 * Gets last modified of file path. This method checks the cache and
	 * if not found, delegates to the file.
	 * 
	 * @param pPath the file path
	 * @return last modified millis
	 */
	public static long lastModified(String pPath)
	{
		startCheck();

		if (pPath == null)
		{
			return -1;
		}

		FileInfo fi = hmpPath.get(pPath);
		
		if (fi != null)
		{
			return fi.lastModified;
		}
		else
		{
			File file = new File(pPath);
					
			addTemporary(file);
			
			return file.lastModified();
		}
	}
	
	/**
	 * Gets whether the file exists. This method checks the cache and
	 * if not found, delegates to the file.
	 * 
	 * @param pFile the file
	 * @return <code>true</code> if file exists, <code>false</code> otherwise
	 */
	public static boolean exists(File pFile)
	{
		startCheck();
		
		if (pFile == null)
		{
			return false;
		}

		FileInfo fi = hmpPath.get(pFile.getAbsolutePath());
		
		if (fi != null)
		{
			return fi.exists;
		}
		else
		{
			addTemporary(pFile);

			return pFile.exists();
		}
	}
	
	/**
	 * Gets whether the file exists. This method checks the cache and
	 * if not found, delegates to the file.
	 * 
	 * @param pPath the file path
	 * @return <code>true</code> if file exists, <code>false</code> otherwise
	 */
	public static boolean exists(String pPath)
	{
		startCheck();

		if (pPath == null)
		{
			return false;
		}

		FileInfo fi = hmpPath.get(pPath);
		
		if (fi != null)
		{
			return fi.exists;
		}
		else
		{
			File file = new File(pPath);
			
			addTemporary(file);
			
			return file.exists();
		}
	}
	
	/**
	 * Updates the cached file information.
	 * 
	 * @param pFile the file
	 * @param pInfo the available info
	 * @return the changed info
	 */
	private static FileInfo update(File pFile, FileInfo pInfo)
	{
		pInfo.lastModified = pFile.lastModified();
		pInfo.exists = pFile.exists();
		
		return pInfo;
	}
	
	/**
	 * Stars the check thread if necessary.
	 */
	private static void startCheck()
	{
		if (bEnabled)
		{
			if (ThreadHandler.isStopped(thCheck))
			{
				thCheck = ThreadHandler.start(new FileCheck());
	
				log.debug("Start FileCheck: ", thCheck.getName());
			}
		}
	}
	
	/**
	 * Stops check thread.
	 */
	private static void stopCheck()
	{
		thCheck = ThreadHandler.stop(thCheck);
	}
	
	/**
	 * Sets the watchdog enabled.
	 * 
	 * @param pEnabled <code>true</code> enable, <code>false</code> to disable
	 */
	public static void setEnabled(boolean pEnabled)
	{
		bEnabled = pEnabled;
		
		if (!bEnabled)
		{
			stopCheck();
			
			hmpPath.clear();
		}
	}
	
	/**
	 * Gets whether the watchdog is enabled.
	 * 
	 * @return <code>true</code> if enabled, <code>false</code> otherwise
	 */
	public boolean isEnabled()
	{
		return bEnabled;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>FileInfo</code> is a struct for File information.
	 * 
	 * @author René Jahn
	 */
	private static class FileInfo
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the creation time. */
		private long creation = System.currentTimeMillis();
		
		/** the max cache duration. */
		private long lExpireTime = -1;

		/** the last modified in millis. */
		private long lastModified;
		
		/** whether file exists. */
		private boolean exists;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new persistent instance of <code>FileInfo</code>.
		 */
		public FileInfo()
		{
		}
		
		/**
		 * Creates a new temporary instance of <code>FileInfo</code>.
		 * 
		 * @param pDuration the max duration millis before the info will expire
		 */
		public FileInfo(long pDuration)
		{
			lExpireTime = creation + pDuration;
		}
		
	}	// FileInfo
	
	/**
	 * The <code>FileCheck</code> checks the session activity
	 * and kills zombies.
	 * 
	 * @author René Jahn
	 */
	private static final class FileCheck extends Thread
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run()
		{
			try
			{
				while (!ThreadHandler.isStopped())
				{
					FileInfo info;
					
					long lNow;
					
					for (Entry<String, FileInfo> entry : hmpPath.entrySet())
					{
						info = entry.getValue();
						
						lNow = System.currentTimeMillis();
						
						if (info.lExpireTime > 0 && info.lExpireTime < lNow)
						{							
							//maybe it was already put as file to watch... in this case, remove won't do anything
							hmpPath.remove(entry.getKey(), entry.getValue());
						}
						else
						{
							if (info.creation + lCheckInterval < lNow)
							{
								update(new File(entry.getKey()), entry.getValue());
							}
						}
					}
					
					Thread.sleep(lCheckInterval);
				}
			}
			catch (Throwable th)
			{
				//doesn't matter, because the controller will be started when a new session will
				//be created
				log.debug(th);
			}
			
			log.error("FileCheck stopped: ", getName());
		}
		
	}	// FileCheck
	
}	// FileWatchdog
