/*
 * Copyright 2021 SIB Visions GmbH
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
 * 21.11.2008 - [JR] - creation
 * 04.12.2008 - [JR] - showError implemented
 * 06.12.2008 - [JR] - createInstance: handled InvocationTargetException
 * 11.12.2008 - [JR] - showError: printStackTrace
 * 26.05.2009 - [JR] - showError moved to SwingFactory
 *                   - renamed to ApplicationUtil and moved to the ui package
 * 24.06.2009 - [JR] - getJNLPCodebase implemented (test JNLP access)
 * 06.10.2009 - [JR] - get/setRegistryKey implemented   
 * 14.11.2009 - [JR] - getRegistryApplicationName implemented     
 * 18.03.2011 - [JR] - getRegistryKey: null check for application parameter   
 * 07.11.2011 - [JR] - createApplication with custom class loader
 * 28.03.2012 - [JR] - #568: check exception [mac os fix]     
 * 28.08.2013 - [JR] - #782: getRegistryApplicationName now returns the URL as slash separated value   
 * 25.04.2014 - [JR] - #1018: configureFrameBounds introduced
 * ----
 * 31.08.2021 - [JR] - copied from ApplicationUtil (deprecated)
 */
package com.sibvisions.rad.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.rad.application.IApplication;
import javax.rad.application.ILauncher;

import com.sibvisions.util.Reflective;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * The <code>LauncherUtil</code> is a utility for application launchers.
 * <ul>
 *   <li>Application creation</li>
 *   <li>Config loading</li>
 *   <li>Parameter handling</li>
 *   <li>Registry access</li>
 * </ul>
 * 
 * @author René Jahn
 */
public class LauncherUtil
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** whether the OS is MacOS. */
    private static boolean bIsMacOS;
    /** whether the OS is windows. */
    private static boolean bIsWindows;
    /** whether the OS is linux. */
    private static boolean bIsLinux;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    static
    {
        try
        {
            String sOS = System.getProperty("os.name").toLowerCase(); 
            
            bIsMacOS = sOS.indexOf("mac") >= 0;
            bIsWindows = sOS.indexOf("windows") >= 0;
            bIsLinux = sOS.indexOf("linux") >= 0;
        }
        catch (Exception e)
        {
            //not allowed to access os.name property
        }
    }

    /**
	 * Invisible constructor, because the <code>LauncherUtil</code> class is a utility class.
	 */
	protected LauncherUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates an instance of an {@link IApplication}.
	 * 
	 * @param pLauncher the launch configuration
	 * @param pClassName the full qualified class name of the desired class
	 * @return a new {@link IApplication} instance 
	 * @throws Throwable if the instance can not be created
	 */
	public static IApplication createApplication(ILauncher pLauncher, String pClassName) throws Throwable
	{
		return (IApplication)Reflective.construct(pClassName, pLauncher);
	}

	/**
	 * Creates an instance of an {@link IApplication}.
	 * 
	 * @param pLauncher the launch configuration
	 * @param pClassLoader the class loader
	 * @param pClassName the full qualified class name of the desired class
	 * @return a new {@link IApplication} instance 
	 * @throws Throwable if the instance can not be created
	 */
	public static IApplication createApplication(ILauncher pLauncher, ClassLoader pClassLoader, String pClassName) throws Throwable
	{
		return (IApplication)Reflective.construct(pClassLoader, pClassName, pLauncher);
	}
	
	/**
	 * Gets the parsed xml configuration.
	 * 
	 * @param pName the configuration name or path
	 * @return the parsed configuration, or <code>null</code> if the config was not found
	 * @throws Exception xml parse error
	 */
	public static XmlNode getConfig(String pName) throws Exception
	{
		InputStream isConfig = ResourceUtil.getResourceAsStream(pName);
		
		if (isConfig != null)
		{
			try
			{
				XmlWorker xmw = new XmlWorker();
				
				return xmw.read(isConfig);
			}
			finally
			{
				if (isConfig != null)
				{
					try
					{
						isConfig.close();
					}
					catch (IOException ioe)
					{
						//egal
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Replaces a parameter placeholder with the desired parameter.
	 * The placeholder looks like the following: [SERVER]
	 * The placeholder will be replaced with the value of the SERVER parameter.
	 * 
	 * @param pValue the value of an application parameter
	 * @param pLauncher the launcher
	 * @return the value with replaced placeholder(s)
	 */
	public static String replaceParameter(String pValue, ILauncher pLauncher)
	{
		if (pValue != null)
		{
			String sReplaceValue;
			String sReplaceParam;
			
			int iParamEnd = 0;
			int iParamStart = pValue.indexOf("[", iParamEnd);
			
			//check parameter replacement
			while (iParamStart >= 0
				   && iParamEnd != -1)
			{
				iParamEnd = pValue.indexOf(']', iParamStart);
				
				if (iParamEnd > iParamStart)
				{
					sReplaceParam = pValue.substring(iParamStart + 1, iParamEnd);
					
					//-> be careful with parameter definition -> recursive calls possible!
					sReplaceValue = pLauncher.getParameter(sReplaceParam);
					
					if (sReplaceValue != null)
					{
						pValue = pValue.replace("[" + sReplaceParam + "]", sReplaceValue);
						
						iParamEnd = iParamStart + sReplaceValue.length();
					}
				}
				
				iParamStart = pValue.indexOf("[", iParamEnd);
			}
			
			return pValue;
		}

		return null;
	}	
	
	/**
	 * Sets the value for an application specific registry key.
	 * 
	 * @param pApplication the application name
	 * @param pKey the key to set
	 * @param pValue the value to set
	 * @throws SecurityException if a {@link SecurityManager} is present and denies the registry access
	 * @throws RuntimeException if it's not possible to save the registry key
	 */
	public static void setRegistryKey(String pApplication, String pKey, String pValue)
	{
	    if (bIsMacOS)
	    {
	        try
	        {
	            File file = getRegistryFile();
	            
	            XmlWorker xmw = new XmlWorker();
	            
	            XmlNode xmn;
	            
	            if (file.exists())
	            {
	                xmn = xmw.read(file);
	            }
	            else
	            {
	                new File(FileUtil.getDirectory(file.getAbsolutePath())).mkdirs();
	                
	                xmn = XmlNode.createXmlDeclaration();
	            }
	            
	            String sTag = "/registry/" + XmlNode.getValidTagName(pApplication) + "/" + XmlNode.getValidTagName(pKey);
	            
	            if (pValue == null)
	            {
	                xmn.removeNode(sTag);
	            }
	            else
	            {
	                xmn.setNode(sTag, pValue);
	            }
	            
	            xmw.write(file, xmn);
	        }
	        catch (Exception ex)
	        {
	            throw new SecurityException(ex);
	        }
	    }
	    else
	    {
            Preferences pref = Preferences.userRoot().node(pApplication);
    
    		//#568
    		try
    		{
    			if (pValue == null)
    			{
    				pref.remove(pKey);
    			}
    			else
    			{
    				pref.put(pKey, pValue);
    			}
    		}
    		catch (IllegalStateException ise)
    		{
    			//should not happen, but is possible on older MacOSX versions
    			ise.printStackTrace();
    			
    			return;
    		}
    		
    		try
    		{
    		    pref.flush();
    			pref.sync();
    		}
    		catch (BackingStoreException bse)
    		{
    			throw new RuntimeException("Error saving registry key: " + pApplication + " " + pKey, bse);
    		}
	    }
	}

	/**
	 * Gets the custom registry file.
	 * 
	 * @return the registry file
	 */
	private static File getRegistryFile()
	{
	    if (bIsMacOS)
	    {	    	
	        return new File(System.getProperty("user.home"), "/Library/Preferences/com.sibvisions.rad.Registry.xml");
	    }
	    else if (bIsLinux)
	    {
	        return new File(System.getProperty("user.home"), "/Java/registry.xml");
	    }
	    else if (bIsWindows)
	    {
	        return new File(System.getenv("APPDATA"), "/.java/preferences/registry.xml");
	    }
	    
	    return null;
	}
	
	/**
	 * Gets the value for an application specific registry key.
	 * 
	 * @param pApplication the application name
	 * @param pKey the key to get
	 * @return the value for the <code>pKey</code> or <code>null</code> if the application or key is not visible
	 * @throws SecurityException if a {@link SecurityManager} is present and denies the registry access
	 */
	public static String getRegistryKey(String pApplication, String pKey)
	{
	    if (bIsMacOS)
	    {
	        try
	        {
                File file = getRegistryFile();
                
                if (file.exists())
                {
                    XmlWorker xmw = new XmlWorker();
                   
                    XmlNode xmn = xmw.read(file);
                
                    return xmn.getNodeValue("/registry/" + XmlNode.getValidTagName(pApplication) + "/" + XmlNode.getValidTagName(pKey));
                }
                
                return null;
	        }
	        catch (Exception ex)
	        {
	            throw new SecurityException(ex);
	        }
	    }
	    else
	    {
    		Preferences pref = Preferences.userRoot();

    		try
    		{
    			if (pApplication != null && pref.nodeExists(pApplication))
    			{
    				return pref.node(pApplication).get(pKey, null);
    			}
    			
    			return null;
    		}
    		catch (BackingStoreException bse)
    		{
    			return null;
    		}
	    }
	}
	
	/**
	 * Gets the application name out of the launcher. If the serverbase param is configured, then the
	 * value of this parameter will be the prefix for the application.
	 * 
	 * @param pLauncher the launcher with properties for the application
	 * @return the application name prefixed with the serverbase
	 */
	public static String getRegistryApplicationName(ILauncher pLauncher)
	{
		String sName = pLauncher.getParameter(ILauncher.PARAM_CODEBASE);
		
		if (sName == null)
		{
			sName = pLauncher.getParameter(ILauncher.PARAM_SERVERBASE);
		}
    	
    	if (sName != null)
    	{
    		sName = sName.replace("://", "/").replace(":", "/").replaceAll("//", "/");
    		
    		if (sName.endsWith("/"))
    		{
    			return sName + pLauncher.getParameter(ILauncher.PARAM_APPLICATIONNAME);
    		}
    		else
    		{
    			return sName + "/" + pLauncher.getParameter(ILauncher.PARAM_APPLICATIONNAME); 
    		}
    	}
    	else
    	{
    		sName = pLauncher.getParameter(ILauncher.PARAM_APPLICATIONNAME);
    		
    		if (sName == null)
    		{
    			sName = pLauncher.getTitle();
    		}
    		if (sName == null)
    		{
    			sName = pLauncher.getClass().getSimpleName();
    		}
    		
    		return sName;
    	}
	}
	
    /**
     * Parse Rectangle from String Bounds.
     * @param pBounds String bounds
     * @return the Rectangle
     */
    private static Rectangle parseBounds(String pBounds)
    {
        try
        {
            String[] saBounds = pBounds.split(",");
            
            return new Rectangle(Integer.parseInt(saBounds[0].trim()), Integer.parseInt(saBounds[1].trim()),
                             Integer.parseInt(saBounds[2].trim()), Integer.parseInt(saBounds[3].trim()));
        }
        catch (Exception ex)
        {
            // System.err.println("Can't set frame bounds\n" + ExceptionUtil.dump(ex, true));
        }
        return null;
    }

    /**
     * Gets the device bounds considering task bars and menus from operating systems.
     * 
     * @param pGraphicsConfiguration the graphics configuration
     * @return the device bounds considering task bars and menus from operating systems.
     */
    public static Rectangle getDeviceBounds(GraphicsConfiguration pGraphicsConfiguration)
    {
        Rectangle screenBounds = pGraphicsConfiguration.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(pGraphicsConfiguration);
        
        screenBounds.x += screenInsets.left;
        screenBounds.y += screenInsets.top;
        screenBounds.width -= screenInsets.left + screenInsets.right;
        screenBounds.height -= screenInsets.top + screenInsets.bottom;
        
        return screenBounds;
    }
    
    /**
     * Configures the bounds for the given frame, if the System property "framebounds" was set.
     *
     * @param pLauncher the launcher
     * @param pFrame the frame to change
     * @return <code>true</code> if bounds were changed, <code>false</code> otherwise
     */
    public static boolean configureFrameBounds(ILauncher pLauncher, Frame pFrame)
    {
        boolean bChanged = false;

        try
        {
            Rectangle rBounds = parseBounds(System.getProperty("framebounds")); 

            if (rBounds == null) // Only restore size, if bounds are not set manually
            {
                rBounds = parseBounds(pLauncher.getRegistryKey("framebounds"));
                boolean maximized = Boolean.parseBoolean(pLauncher.getRegistryKey("maximized"));
                
                GraphicsDevice device = null;
                Rectangle deviceBounds = null; // Search device bounds, if it exists
                if (rBounds != null)
                {
                    GraphicsDevice[] gdev = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
                    long maxSize = 0;
                    
                    for (int i = 0; i < gdev.length; i++)
                    {
                        GraphicsDevice tmpDevice = gdev[i];
                        Rectangle tmpBounds = getDeviceBounds(tmpDevice.getDefaultConfiguration());
                        Rectangle iBounds = tmpBounds.intersection(rBounds);
                        if (iBounds.width < 0) // Fix intersection bug
                        {
                            iBounds.width = 0;
                        }
                        if (iBounds.height < 0)
                        {
                            iBounds.height = 0;
                        }
                        long size = (long)iBounds.width * (long)iBounds.width + (long)iBounds.height * (long)iBounds.height;
                        
                        if (size > maxSize)
                        {
                            device = tmpDevice;
                            deviceBounds = tmpBounds;
                            
                            maxSize = size;
                        }
                    }
                }

                if (deviceBounds == null) // if device does not exists, use default device, and try to restore size
                {
                    device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    deviceBounds = getDeviceBounds(device.getDefaultConfiguration());
                }
                
                Dimension minSize = pFrame.getMinimumSize(); // At least minimum size 250x100 smaller window makes no sense.
                if (minSize.width < 250)
                {
                    minSize.width = 250;
                }
                if (minSize.height < 100)
                {
                    minSize.height = 100;
                }
                if (rBounds != null && (rBounds.width <= minSize.width || rBounds.height <= minSize.height))
                {
                    rBounds = null;
                }
                
                if (rBounds != null)
                {
                    Insets ins = pFrame.getInsets(); // Check with insets and 5px tolerance
                    Rectangle checkBounds = new Rectangle(rBounds.x + ins.left + 5, rBounds.y + ins.top + 5,
                            rBounds.width - ins.left - ins.right - 10, rBounds.height - ins.top - ins.bottom - 10);
                    if (rBounds.width > deviceBounds.width || rBounds.height > deviceBounds.height
                            || checkBounds.x < deviceBounds.x || checkBounds.y < deviceBounds.y
                            || checkBounds.x + checkBounds.width > deviceBounds.x + deviceBounds.width
                            || checkBounds.y + checkBounds.height > deviceBounds.y + deviceBounds.height)
                    {
                        Dimension size = pFrame.getPreferredSize(); // If bounds are violated, ensure good size again.
                        if (rBounds.width < size.width)
                        {
                            rBounds.width = size.width;
                        }
                        if (rBounds.width > deviceBounds.width)
                        {
                            rBounds.width = deviceBounds.width * 3 / 4;
                        }
                        if (rBounds.height < size.height)
                        {
                            rBounds.height = size.height;
                        }
                        if (rBounds.height > deviceBounds.height)
                        {
                            rBounds.height = deviceBounds.height * 3 / 4;
                        }
                        
                        pFrame.setBounds(new Rectangle(
                                deviceBounds.x + (deviceBounds.width - rBounds.width) / 2, 
                                deviceBounds.y + (deviceBounds.height - rBounds.height) / 2, 
                                rBounds.width, rBounds.height));
                    }
                    else
                    {
                        pFrame.setBounds(rBounds);

                        bChanged = true;
                    }
                }
                else
                {
                    Dimension size = pFrame.getSize();
                    if (size.width <= minSize.width || size.height <= minSize.height)
                    {
                        size = pFrame.getPreferredSize();
                    }
                    if (size.width > deviceBounds.width) // ensure screen is not bigger than screen
                    {
                        size.width = deviceBounds.width;
                    }
                    if (size.height > deviceBounds.height)
                    {
                        size.height = deviceBounds.height;
                    }
                    
                    pFrame.setBounds(new Rectangle(
                            deviceBounds.x + (deviceBounds.width - size.width) / 2, 
                            deviceBounds.y + (deviceBounds.height - size.height) / 2, 
                            size.width, size.height));
                }

                if (maximized) // if it was maximized, maximize it again, if device was not found, maximize it on default device.
                {
                    // Notify the Frame immediately, before it is maximized, to get correct restore bounds. 
                    pFrame.dispatchEvent(new ComponentEvent(pFrame, ComponentEvent.COMPONENT_RESIZED));
                    
                    //maximize
                    pFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
                    
                    bChanged = true;
                }
            }
            else
            {
                pFrame.setBounds(rBounds);
                    
                bChanged = true;
            }
        }
        catch (Exception se)
        {
            //access not allowed
        }
        
        return bChanged;
    }
    
	/**
	 * Gets whether the operating system is MacOS.
	 * 
	 * @return <code>true</code> if MacOS, <code>false</code> otherwise
	 */
	public static boolean isMacOS()
	{
	    return bIsMacOS;
	}
	
    /**
     * Gets whether the operating system is Linux.
     * 
     * @return <code>true</code> if Linux, <code>false</code> otherwise
     */
	public static boolean isLinux()
	{
	    return bIsLinux;
	}
	
    /**
     * Gets whether the operating system is Windows.
     * 
     * @return <code>true</code> if Windows, <code>false</code> otherwise
     */
	public static boolean isWindows()
	{
	    return bIsWindows;
	}

	/**
	 * Splits image properties. The definition of an image should be in following format:
	 * <pre>imagename;prop1=value1;prop2=value2</pre>.
	 * 
	 * The imagename will be returned as property with the identifier <code>name</code>.
	 * If a property with name <code>name</code> was set, it won't be returned.
	 * 
	 * @param pImageDefinition the image definition
	 * @return the parsed properties 
	 */
    public static HashMap<String, String> splitImageProperties(String pImageDefinition)
    {
        List<String> liProps = StringUtil.separateList(pImageDefinition, ";", true);
        
        HashMap<String, String> hmpProps = new HashMap<String, String>();
        
        if (!liProps.isEmpty())
        {
            hmpProps.put("name", liProps.get(0));
            
            String sProp;
            String sName;
            String[] sPropParsed;
            
            for (int i = 1, cnt = liProps.size(); i < cnt; i++)
            {
                sProp = liProps.get(i);
                
                if (!StringUtil.isEmpty(sProp))
                {
	                sPropParsed = sProp.split("=");
	                
	                sName = sPropParsed[0].toLowerCase();
	                
	                if (!"name".equals(sName))
	                {
	                    hmpProps.put(sName, sPropParsed[1]);
	                }
                }
            }
        }
        
        return hmpProps;
    }
	
	
}	// LauncherUtil
