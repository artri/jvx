/*
 * Copyright 2014 SIB Visions GmbH
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
 * 22.12.2014 - [JR] - creation
 */
package com.sibvisions.util;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>ProxyUtil</code> detects a system proxy.
 * 
 * @author René Jahn
 */
public final class ProxyUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the detected proxy. */
	private static Proxy proxy = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Starts system proxy detection.
	 * 
	 * @param pArgs the first parameter should contain the http(s) URL
	 * @throws Exception if detection fails
	 */
	public static void main(String[] pArgs) throws Exception
	{
		ProxySelector prosel = ProxySelector.getDefault();
		
		List<Proxy> liProxy = prosel.select(new URI(pArgs[0]));
		
		if (liProxy != null)
		{
			if (liProxy.size() > 0)
			{
				InetSocketAddress adr = (InetSocketAddress)liProxy.get(0).address();
				
				if (adr != null)
				{
					System.out.println(liProxy.get(0).type().toString());
					System.out.println(adr.getHostName());
					System.out.println(adr.getPort());
				}
			}
		}
	}

	/**
	 * Invisible constructor because <code>ClassUtil</code> is a utility
	 * class.
	 */
	private ProxyUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the system proxy for the given URL. If a proxy is detected, it will be re-used for other
	 * URLs too. Multi-Proxy environments are not supported.
	 * 
	 * @param pURL the URL
	 * @return the detected proxy or <code>null</code> if no proxy is detected or used
	 * @throws Exception if detection fails
	 */
	public static Proxy getSystemProxy(String pURL) throws Exception
	{
		if (proxy == null)
		{
			ArrayUtil<String> auDirs = new ArrayUtil<String>();
			
			//build classpath
			add(auDirs, ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(ProxyUtil.class)));
			add(auDirs, ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(Execute.class)));
			
			StringBuilder sbPath = new StringBuilder();
			
			for (String sDir : auDirs)
			{
				sbPath.append(sDir);
				sbPath.append(File.pathSeparator);
			}
		
			//execute java
			Execute exec = new Execute();
			exec.setProgram(System.getProperty("java.home") + "/bin/java");
			exec.addParameter("-cp");
			exec.addParameter(sbPath.toString());
			//have to be set before the program is started (does not always work when the program runs)
			exec.addParameter("-Djava.net.useSystemProxies=true");
			exec.addParameter(ProxyUtil.class.getName());
			exec.addParameter(pURL);
			
			exec.execute(true);
			
			String sOutput = exec.getOutput();
			
			if (sOutput != null && sOutput.length() > 0)
			{
				List<String> liProxy = StringUtil.separateList(sOutput, "\n", true);
				
				proxy = new Proxy(Type.valueOf(liProxy.get(0)), new InetSocketAddress(liProxy.get(1), Integer.parseInt(liProxy.get(2))));
			}
		}
		
		return proxy;
	}
	
	/**
	 * Adds a directory and/or the found jar files (in the directory) to a path list.
	 * 
	 * @param pList the list of available path elements
	 * @param pLocation a directory or jar file
	 */
	private static void add(List<String> pList, String pLocation)
	{
		if (pLocation != null)
		{
			FileSearch fs = new FileSearch();
			fs.search(pLocation, false, "*.jar");
			
			if (pList.indexOf(pLocation) < 0)
			{
				pList.add(pLocation);
			}
			
			for (String sFile : fs.getFoundFiles())
			{
				if (pList.indexOf(sFile) < 0)
				{
					pList.add(sFile);
				}
			}
		}
	}
	
}	// ProxyUtil
