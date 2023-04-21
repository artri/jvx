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
 * 03.06.2009 - [JR] - creation
 * 06.10.2009 - [JR] - set/getRegistryKey implemented
 * 15.09.2013 - [JR] - #793: put/getObject implemented
 * 30.09.2013 - [JR] - #811: is...Environment methods added
 */
package javax.rad.application.genui;

import java.util.Locale;
import java.util.TimeZone;

import javax.rad.application.IApplication;
import javax.rad.application.IFileHandleReceiver;
import javax.rad.application.ILauncher;
import javax.rad.genui.container.AbstractFrame;
import javax.rad.genui.event.ResourceEvent.EventType;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.io.IFileHandle;
import javax.rad.ui.IRectangle;
import javax.rad.util.EventHandler;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>UILauncher</code> is an {@link AbstractFrame} extension. It will be
 * used for platform and technology dependent {@link ILauncher} implementations.
 * 
 * @author René Jahn
 */
public class UILauncher extends AbstractFrame<ILauncher> 
                        implements ILauncher
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The file handle receiver provider. */
	private static EventHandler<IFileHandleReceiver> fileHandleReceiverProvider = new EventHandler<IFileHandleReceiver>(IFileHandleReceiver.class);
	
	/** the simple environment name. */
	private String sSimpleEnvName;
	
	/** the cached environment parameter. */
	private String sEnvParameter;
	
	/** whether the environment name is already detected. */
	private boolean bEnvNameDetected = false;
	
	/** whether the environment parameter is already detected. */
	private boolean bEnvParameterDetected = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of UILauncher with an {@link ILauncher} implementation.
	 * 
	 * @param pLauncher the {@link ILauncher}
	 */
	public UILauncher(ILauncher pLauncher)
	{
		super(pLauncher);
		
		setLayout(new UIBorderLayout());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void getFileHandle(IFileHandleReceiver pFileHandleReceiver, String pTitle) throws Throwable
	{
		uiResource.getFileHandle(pFileHandleReceiver, pTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getParameter(String pName)
	{
		return uiResource.getParameter(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveFileHandle(IFileHandle pFileHandle, String pTitle) throws Throwable
	{
		uiResource.saveFileHandle(pFileHandle, pTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParameter(String pName, String pValue)
	{
	    Object oOldValue;
	    
	    if (hasResourceHandler())
	    {
	        oOldValue = uiResource.getParameter(pName);
	    }
	    else
	    {
	        oOldValue = null;
	    }
	    
		uiResource.setParameter(pName, pValue);
		
	    fireResourceChanged(EventType.Parameter, pName, oOldValue, pValue);
	}

	/**
	 * {@inheritDoc}
	 */
	public void showDocument(String pDocumentName, IRectangle pBounds, String pTarget) throws Throwable
	{
		uiResource.showDocument(pDocumentName, pBounds, pTarget);
	}

	/**
	 * {@inheritDoc}
	 */
	public void showFileHandle(IFileHandle pFileHandle, IRectangle pBounds, String pTarget) throws Throwable
	{
		uiResource.showFileHandle(pFileHandle, pBounds, pTarget);
	}

	/**
	 * {@inheritDoc}
	 */
	public void showFileHandle(IFileHandle pFileHandle) throws Throwable
	{
		uiResource.showFileHandle(pFileHandle);
	}

    /**
     * {@inheritDoc}
     */
    public void cancelPendingThreads()
    {
    	uiResource.cancelPendingThreads();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void handleException(Throwable pThrowable)
	{
		uiResource.handleException(pThrowable);
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void setRegistryKey(String pKey, String pValue)
    {
    	uiResource.setRegistryKey(pKey, pValue);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public String getRegistryKey(String pKey)
    {
    	return uiResource.getRegistryKey(pKey); 
    }
    
	/**
	 * {@inheritDoc}
	 */
	public String getEnvironmentName()
	{
		return uiResource.getEnvironmentName();
	}

	/**
	 * {@inheritDoc}
	 */
    public IApplication getApplication()
    {
    	return uiResource.getApplication();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public Locale getLocale()
    {
    	return uiResource.getLocale();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setLocale(Locale pLocale)
    {
    	uiResource.setLocale(pLocale);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public TimeZone getTimeZone()
    {
    	return uiResource.getTimeZone();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setTimeZone(TimeZone pTimeZone)
    {
    	uiResource.setTimeZone(pTimeZone);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Technology independent saveFileHandle.
     * A file chooser dialog should occur. This Function returns immediate. 
     * 
     * @param pFileHandle the IFileHandle to save.
     * @throws Throwable if an error occurs during saving the file.
     */
	public void saveFileHandle(IFileHandle pFileHandle) throws Throwable
	{
		uiResource.saveFileHandle(pFileHandle, null);
	}

    /**
     * Technology independent getFileHandle.
     * A file chooser dialog should occur, and as result this function informs the IFileHandle receiver. 
     * 
     * @param pFileHandleReceiver the IFileHandle receiver that should receive the file.
     * @throws Throwable if an error occurs during getting the file.
     */
	public void getFileHandle(IFileHandleReceiver pFileHandleReceiver) throws Throwable
	{
		getFileHandle(pFileHandleReceiver, null);
	}

    /**
     * Technology independent getFileHandle.
     * A file chooser dialog should occur, and as result this function informs the IFileHandle receiver. 
     * 
     * @param pFileHandleReceiver the IFileHandle receiver that should receive the file.
     * @param pMethodName the method that is invoked.
     * @param pTitle the title.
     * @throws Throwable if an error occurs during getting the file.
     */
	public void getFileHandle(Object pFileHandleReceiver, String pMethodName, String pTitle) throws Throwable
	{
		getFileHandle(createFileHandleReceiver(pFileHandleReceiver, pMethodName), pTitle);
	}

    /**
     * Technology independent getFileHandle.
     * A file chooser dialog should occur, and as result this function informs the IFileHandle receiver. 
     * 
     * @param pFileHandleReceiver the IFileHandle receiver that should receive the file.
     * @param pMethodName the method that is invoked.
     * @throws Throwable if an error occurs during getting the file.
     */
	public void getFileHandle(Object pFileHandleReceiver, String pMethodName) throws Throwable
	{
		getFileHandle(pFileHandleReceiver, pMethodName, null);
	}

    /**
     * Creates a file handle receiver instance with the given object and method name.
     * @param pFileHandleReceiver the object.
     * @param pMethodName the method name.
     * @return the file handle receiver.
     */
	public static IFileHandleReceiver createFileHandleReceiver(Object pFileHandleReceiver, String pMethodName)
	{
		return fileHandleReceiverProvider.createListener(pFileHandleReceiver, pMethodName);
	}

	/**
	 * Gets whether the launcher was started in a mobile environment.
	 * 
	 * @return <code>true</code> if the launcher runs in a mobile environment
	 */
	public boolean isMobileEnvironment()
	{
		return ILauncher.ENVIRONMENT_MOBILE.equals(getSimpleEnvironmentName());
	}

	/**
	 * Gets whether the launcher was started in a web environment.
	 * 
	 * @return <code>true</code> if the launcher runs in a web environment
	 */
	public boolean isWebEnvironment()
	{
		return ILauncher.ENVIRONMENT_WEB.equals(getSimpleEnvironmentName());
	}
	
	/**
	 * Gets whether the launcher was started in a REST environment.
	 * 
	 * @return <code>true</code> if the launcher runs in a web environment
	 */
	public boolean isRESTEnvironment()
	{
		return ILauncher.ENVIRONMENT_REST.equals(getSimpleEnvironmentName());
	}

	/**
	 * Gets whether the launcher was started in a desktop environment.
	 * 
	 * @return <code>true</code> if the launcher runs in a desktop environment
	 */
	public boolean isDesktopEnvironment()
	{
		return ILauncher.ENVIRONMENT_DESKTOP.equals(getSimpleEnvironmentName());
	}

	/**
	 * Gets whether the launcher was started in a headless (without GUI) environment.
	 * 
	 * @return <code>true</code> if the launcher runs in a headless (without GUI) environment
	 */
	public boolean isHeadlessEnvironment()
	{
		return ILauncher.ENVIRONMENT_HEADLESS.equals(getSimpleEnvironmentName());
	}

    /**
     * Gets the value for the given application parameter as boolean value.
     * 
     * @param pParameterName the name of the parameter
     * @param pDefault the default value if parameter was not set
     * @return <code>pDefault</code> if the parameter was not set, otherwise the boolean representation of the value
     */
    public boolean getParameterAsBoolean(String pParameterName, boolean pDefault)
    {
        String sValue = getParameter(pParameterName);
        
        if (StringUtil.isEmpty(sValue))
        {
            return pDefault;
        }
        
        return Boolean.parseBoolean(sValue);
    } 	
    
    /**
     * Gets the environment name without additional information. The environment could contain additional
     * information, separated by {@code :}, e.g. NAME:PLATFORM -&gt; NAME will be returned.
     * 
     * @param pEnvironment the full environment name
     * @return the simple environment name 
     */
    public static String getSimpleEnvironmentName(String pEnvironment)
    {
        if (!StringUtil.isEmpty(pEnvironment))
        {
            int iPos = pEnvironment.indexOf(":");
            
            if (iPos >= 0)
            {
                return pEnvironment.substring(0, iPos);
            }
            else
            {
            	return pEnvironment;
            }
        }
        else
        {
        	return null;
        }
    }
    
    /**
     * Gets the environment name without additional information. The environment could contain additional
     * information, separated by {@code :}, e.g. NAME:PLATFORM -&gt; NAME will be returned.
     * 
     * @return the simple environment name 
     */
    public String getSimpleEnvironmentName()
    {
    	if (!bEnvNameDetected)
    	{
            sSimpleEnvName = getSimpleEnvironmentName(uiResource.getEnvironmentName());
            
            bEnvNameDetected = true;
    	}
        
        return sSimpleEnvName;
    }
    
    /**
     * Gets the additional information from the environment name. The environment could contain additional
     * information, separated by {@code :}, e.g. NAME:PLATFORM -&gt; PLATFORM will be returned.
     * 
     * @param pEnvironment the full environment name
     * @return the additional information from the full environment name 
     */
    public String getEnvironmentNameParameter(String pEnvironment)
    {
        if (!StringUtil.isEmpty(pEnvironment))
        {
            int iPos = pEnvironment.indexOf(":");
            
            if (iPos >= 0)
            {
                return pEnvironment.substring(iPos + 1);
            }
        }
        
    	return null;
    }
    
    /**
     * Gets the additional information from the environment name. The environment could contain additional
     * information, separated by {@code :}, e.g. NAME:PLATFORM -&gt; PLATFORM will be returned.
     * 
     * @return the additional information from the environment name 
     */
    public String getEnvironmentNameParameter()
    {
    	if (!bEnvParameterDetected)
    	{
    		sEnvParameter = getEnvironmentNameParameter(uiResource.getEnvironmentName());
	        
	        bEnvParameterDetected = true;
    	}
        
        return sEnvParameter;
    }
	
}	// UILauncher
