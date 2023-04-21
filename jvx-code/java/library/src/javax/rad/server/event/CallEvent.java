/*
 * Copyright 2015 SIB Visions GmbH
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
 * 28.05.2015 - [JR] - creation
 */
package javax.rad.server.event;

import javax.rad.server.ISession;

/**
 * The <code>CallEvent</code> serves information about a server call.
 * 
 * @author René Jahn
 */
public class CallEvent extends SessionEvent
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the object name. */
    private String sObjectName;
    
    /** the method name. */
    private String sMethodName;
    
    /** the parameters. */
    private Object[] oaParameter;
    
    /** whether the call is a callback call. */
    private boolean bIsCallBack;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>CallEvent</code>.
     * 
     * @param pSession the session
     * @param pSource the source session
     * @param pObjectName the object name for the call
     * @param pMethodName the method name for the call
     * @param pParameter the parameter list
     * @param pIsCallBack whether the call is a callback call
     */
    public CallEvent(ISession pSession, ISession pSource, String pObjectName, String pMethodName, Object[] pParameter, boolean pIsCallBack)
    {
        this(pSession, pSource, pObjectName, pMethodName, pParameter, pIsCallBack, false);
    }

    /**
     * Creates a new instance of <code>CallEvent</code>.
     * 
     * @param pSession the session
     * @param pSource the source session
     * @param pObjectName the object name for the call
     * @param pMethodName the method name for the call
     * @param pParameter the parameter list
     * @param pIsCallBack whether the call is a callback call
     * @param pHasError whether the event should marked faulty
     */
    protected CallEvent(ISession pSession, ISession pSource, String pObjectName, String pMethodName, Object[] pParameter, boolean pIsCallBack, boolean pHasError)
    {
        super(pSession, pSource, pHasError);
        
        sObjectName = pObjectName;
        sMethodName = pMethodName;
        oaParameter = pParameter;
        bIsCallBack = pIsCallBack;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the object name for the call.
     * 
     * @return the object name
     */
    public String getObjectName()
    {
        return sObjectName;
    }
    
    /**
     * Gets the method name for the call.
     * 
     * @return the object name
     */
    public String getMethodName()
    {
        return sMethodName;
    }
    
    /**
     * Gets all parameters for the call.
     * 
     * @return the parameters
     */
    public Object[] getParameter()
    {
        return oaParameter;
    }
    
    /**
     * Gets whether the call is a callback call.
     * 
     * @return <code>true</code> if the call is a callback call, <code>false</code> otherwise
     */
    public boolean isCallBack()
    {
        return bIsCallBack;
    }    
    
}   // CallEvent
