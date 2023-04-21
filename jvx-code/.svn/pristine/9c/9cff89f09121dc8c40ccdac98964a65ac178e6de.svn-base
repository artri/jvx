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
 * 06.11.2008 - [JR] - log/error implemented
 * 07.11.2008 - [JR] - log/error removed
 * 04.12.2008 - [JR] - exit defined
 *                   - IExceptionListener extended
 * 09.02.2009 - [JR] - createDownload, createUpload implemented
 * 10.02.2009 - [JR] - defined PARAM_SERVERBASE  
 * 13.02.2009 - [JR] - showDocument: added pBounds                 
 * 17.02.2009 - [JR] - set/getCursor defined
 * 19.02.2009 - [JR] - removed set/getCursor -> only available in IComponent
 * 15.04.2009 - [JR] - getLogger implemented
 *                   - added log constants
 * 16.04.2009 - [JR] - moved log handling to IApplication   
 * 24.04.2009 - [JR] - added PARAM_LOGFACTORY  
 * 04.06.2009 - [JR] - added title to FileHandle methods    
 * 31.07.2009 - [JR] - cancelPendingThreads defined
 * 06.10.2009 - [JR] - set/getRegistryKey defined
 * 13.01.2011 - [JR] - PARAM_APPLICATIONLANGUAGE defined
 * 13.07.2012 - [JR] - PARAM_UIFACTORY defined
 */
package javax.rad.application;

import java.util.Locale;
import java.util.TimeZone;

import javax.rad.io.IFileHandle;
import javax.rad.ui.IRectangle;
import javax.rad.ui.container.IFrame;
import javax.rad.util.event.IExceptionListener;

/**
 * The <code>ILauncher</code> defines a platform and technology 
 * independent {@link IApplication} launcher.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author René Jahn
 */
public interface ILauncher extends IFrame,
                                   IExceptionListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the name for headless environment. */
	public static final String ENVIRONMENT_HEADLESS = "HEADLESS";
	/** the name for desktop environment. */
	public static final String ENVIRONMENT_DESKTOP 	= "DESKTOP";
	/** the name for web environment. */
	public static final String ENVIRONMENT_WEB 		= "WEB";
	/** the name for REST environment. */
	public static final String ENVIRONMENT_REST		= ENVIRONMENT_WEB + ":rest";
	/** the name for mobile environment. */
	public static final String ENVIRONMENT_MOBILE 	= "MOBILE";

	
	/** the prefix for launcher parameters. */
	public static final String PREFIX_LAUNCHER  	= "Launcher.";

	/** the prefix for launcher parameters. */
	public static final String PREFIX_APPLICATION  	= "Application.";
	
	
	/** the parameter name for the full qualified class name of the UI factory. */
	public static final String PARAM_UIFACTORY 		= PREFIX_LAUNCHER + "uifactory";

	/** the parameter name for the launcher environment. */
	public static final String PARAM_ENVIRONMENT 	= PREFIX_LAUNCHER + "environment";

	/** 
	 * the parameter name for the codebase. Used for loading resources
	 * during application lifecycle. 
	 */
	public static final String PARAM_CODEBASE 		= PREFIX_LAUNCHER + "codebase";
	
	/** 
	 * the parameter name for the server base. This property contains
	 * the server connection information, e.g. http://localhost:8080/demo.
	 * Other connection relevant properties should reference this parameter.
	 */
	public static final String PARAM_SERVERBASE				= PREFIX_APPLICATION + "serverbase";

	/** the parameter name for the application name. */
	public static final String PARAM_APPLICATIONNAME 		= PREFIX_APPLICATION + "name";
	
	/** the parameter name for the application language. */
	public static final String PARAM_APPLICATIONLANGUAGE 	= PREFIX_APPLICATION + "language";

	/** the parameter name for the application time zone. */
	public static final String PARAM_APPLICATIONTIMEZONE 	= PREFIX_APPLICATION + "timeZone";

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets a configuration parameter of the application.
	 * 
	 * @param pName the parameter name
	 * @return the value of the parameter or <code>null</code> if the parameter does not exist
	 */
	public String getParameter(String pName);
	
	/**
	 * Sets a configuration parameter of the application.
	 * 
	 * @param pName the parameter name
	 * @param pValue the value for the parameter or <code>null</code> to delete the parameter
	 */
	public void setParameter(String pName, String pValue);

    /**
     * Requests that external application shows a document indicated by the <code>pDocumentname</code> argument. The
     * <code>target</code> argument indicates where the document should be displayed.
     * Applications can also ignore the <code>target</code> information.
     *
     * @param  pDocumentName 	the location/filename of the document. (local or network adress)
     * @param  pBounds			the bounds for the document, if supported from the implementation
     * @param  pTarget			a <code>String</code> indicating where to display the document.
	 * @throws Throwable	    if the document couldn't opened/shown
	 */
	public void showDocument(String pDocumentName, IRectangle pBounds, String pTarget)  throws Throwable;

    /**
     * Technology independent showFileHandle.
     * A file chooser dialog should occur. This Function returns immediate. 
     * 
     * @param  pFileHandle the IFileHandle to save.
     * @param  pBounds	   the bounds for the document, if supported from the implementation
     * @param  pTarget	   a <code>String</code> indicating where to display the document.
     * @throws Throwable if an error occurs during saving the file.
     */
    public void showFileHandle(IFileHandle pFileHandle, IRectangle pBounds, String pTarget) throws Throwable;

    /**
     * Technology independent showFileHandle.
     * A file chooser dialog should occur. This Function returns immediate. 
     * 
     * @param  pFileHandle the IFileHandle to save.
     * @throws Throwable if an error occurs during saving the file.
     */
    public void showFileHandle(IFileHandle pFileHandle) throws Throwable;

    /**
     * Technology independent saveFileHandle.
     * A file chooser dialog should occur. This Function returns immediate. 
     * 
     * @param pFileHandle the IFileHandle to save.
     * @param pTitle the title for the dialog or <code>null</code> to set a default title
     * @throws Throwable if an error occurs during saving the file.
     */
    public void saveFileHandle(IFileHandle pFileHandle, String pTitle) throws Throwable;

    /**
     * Technology independent getFileHandle.
     * A file chooser dialog should occur, and as result this function informs the IFileHandle receiver. 
     * 
     * @param pFileHandleReceiver the IFileHandle receiver that should receive the file.
     * @param pTitle the title for the dialog or <code>null</code> to set a default title
     * @throws Throwable if an error occurs during getting the file.
     */
    public void getFileHandle(IFileHandleReceiver pFileHandleReceiver, String pTitle) throws Throwable;
    
    /**
     * Cancel/Interrupts all pending threads.
     */
    public void cancelPendingThreads();
    
    /**
     * Sets the value for a specific key to the applications registry. The registry is a local
     * store for persistent values.
     *  
     * @param pKey the key to set
     * @param pValue the value to set or <code>null</code> to remove the key from the registry
     */
    public void setRegistryKey(String pKey, String pValue);
    
    /**
     * Gets the value for a specific key from the applications registry.
     * 
     * @param pKey the key to get
     * @return the value for the key or <code>null</code> if the key is not available
     */
    public String getRegistryKey(String pKey);
    
    /**
     * Gets the name of the environment for this launcher. The environment could be used to 
     * en-/disable features.
     * 
     * @return the name of the environment, e.g. {@link ILauncher#ENVIRONMENT_HEADLESS}, 
     *         {@link ILauncher#ENVIRONMENT_DESKTOP} 
     */
    public String getEnvironmentName();

    /**
     * Gets the application started by this launcher.
     * 
     * @return the application
     */
    public IApplication getApplication();
    
    /**
     * Gets the locale for this launcher.
     *  
     * @return the locale
     */
    public Locale getLocale();
    
    /**
     * Sets the locale for this launcher.
     * 
     * @param pLocale the locale
     */
    public void setLocale(Locale pLocale);
    
    /**
     * Gets the time zone for this launcher.
     * 
     * @return the time zone
     */
    public TimeZone getTimeZone();
    
    /**
     * Sets the time zone for this launcher.
     * 
     * @param pTimeZone the time zone
     */
    public void setTimeZone(TimeZone pTimeZone);
    
}	// ILauncher
