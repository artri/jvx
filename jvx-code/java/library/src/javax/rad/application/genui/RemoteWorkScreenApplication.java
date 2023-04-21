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
 * 26.02.2010 - [JR] - creation
 * 27.07.2017 - [JR] - #1811: openWorkScreen with Parameter list
 */
package javax.rad.application.genui;

import java.util.HashMap;
import java.util.Map;

import javax.rad.application.IWorkScreen;
import javax.rad.application.IWorkScreenApplication;
import javax.rad.remote.AbstractConnection;
import javax.rad.util.Parameter;

/**
 * The <code>RemoteWorkScreenApplication</code> is a {@link RemoteApplication} with {@link IWorkScreenApplication}
 * implemented. It will be used to make the usage of {@link IWorkScreenApplication} easier and provides some additional
 * methods.
 * 
 * @author René Jahn
 */
public abstract class RemoteWorkScreenApplication extends RemoteApplication
                                                  implements IWorkScreenApplication
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>RemoteApplication</code> with
	 * a desired launcher.
	 * 
	 * @param pLauncher the launcher of this application
	 */
	public RemoteWorkScreenApplication(UILauncher pLauncher)
	{
		this(pLauncher, null);
	}
	
	/**
	 * Creates a new instance of <code>RemoteApplication</code> with a desired
	 * launcher and connection.
	 * 
	 * @param pLauncher the launcher of this application
	 * @param pConnection the connection
	 */
	public RemoteWorkScreenApplication(UILauncher pLauncher, AbstractConnection pConnection)
	{
		super(pLauncher);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Opens a work screen with modality mode of the work screen.
	 * 
	 * @param pClassName the class name of the work screen
	 * @return the work screen
	 * @throws Throwable if the content could not be initialized
	 */
	public IWorkScreen openWorkScreen(String pClassName) throws Throwable
	{
		return openWorkScreen(pClassName, Modality.WorkScreen, (Map<String, Object>)null);
	}

    /**
     * Opens a work screen with modality mode of the work screen.
     * 
     * @param pClassName the class name of the work screen
     * @param pParameter additional work screen parameters
     * @return the work screen
     * @throws Throwable if the content could not be initialized
     */
    public IWorkScreen openWorkScreen(String pClassName, Map<String, Object> pParameter) throws Throwable
    {
        return openWorkScreen(pClassName, Modality.WorkScreen, pParameter);
    }
    
    /**
     * Opens a work screen with modality mode of the work screen.
     * 
     * @param pClassName the class name of the work screen
     * @param pParameter additional work screen parameters
     * @return the work screen
     * @throws Throwable if the content could not be initialized
     */
    public IWorkScreen openWorkScreen(String pClassName, Parameter... pParameter) throws Throwable
    {
        return openWorkScreen(pClassName, Modality.WorkScreen, pParameter);
    }

    /**
     * Opens a work screen with specific modality.
     * 
     * @param pClassName the class name of the work screen
     * @param pModality the modality mode
     * @param pParameter the work screen parameters
     * @return the work screen
     * @throws Throwable if the content could not be initialized
     */
    public IWorkScreen openWorkScreen(String pClassName, Modality pModality, Parameter... pParameter) throws Throwable
	{
        Map<String, Object> mpParameter;
        
	    if (pParameter == null || pParameter.length == 0)
	    {
	        mpParameter = null;
	    }
	    else
	    {
	        mpParameter = new HashMap<String, Object>();
	        
	        for (int i = 0; i < pParameter.length; i++)
	        {
	            mpParameter.put(pParameter[i].getName(), pParameter[i].getValue());
	        }
	    }
	    
	    return openWorkScreen(pClassName, pModality, mpParameter);
	}
    
}	// RemoteWorkScreenApplication
