/*
 * Copyright 2016 SIB Visions GmbH
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
 * 08.06.2016 - [JR] - creation
 * 27.05.2018 - [JR] - #1928: publishMaster implemented
 * 09.03.2019 - [JR] - #1996: refactoring and PublishMode used
 */
package com.sibvisions.rad.server;

import java.util.List;

import javax.rad.remote.IConnection;
import javax.rad.server.ICallBackBroker;
import javax.rad.server.ISession;
import javax.rad.server.ResultObject;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>SessionCallBackBroker</code> is the {@link ICallBackBroker} implementation for {@link javax.rad.server.ISession}.
 * 
 * @author René Jahn
 */
final class SessionCallBackBroker implements ICallBackBroker
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the logger. */
    private static ILogger log = LoggerFactory.getInstance(SessionCallBackBroker.class);
    
    /** the session. */
    private WrappedSession session;
    
    /** the master session. */
    private WrappedSession master;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>SessionCallBackBroker</code>.
     * 
     * @param pSession the session
     */
    SessionCallBackBroker(ISession pSession)
    {
		AbstractSession sessIntern = null;

		if (pSession instanceof WrappedSession)
    	{
    		session = (WrappedSession)pSession;
    		
    		sessIntern = session.session;
    	}
    	else if (pSession instanceof AbstractSession)
    	{
    		sessIntern = (AbstractSession)pSession;
    		
    		session = new WrappedSession(sessIntern);
    	}
		
		if (sessIntern instanceof MasterSession)
		{    			
			master = session;
		}
		else if (sessIntern instanceof SubSession)
    	{
    		master = new WrappedSession(((SubSession)sessIntern).getMasterSession());
    	}
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public PublishState publish(String pInstruction, Object pObject, PublishMode... pMode)
    {
    	if (pMode == null || pMode.length == 0)
    	{
    		return publishIntern(pInstruction, pObject, PublishMode.CurrentSession);
    	}
    	else
    	{
    		int iCompleted = 0;
    		int iError = 0;
    		int iCount = 0;
    		
    		PublishState stCurrent;
    		
    		for (PublishMode mode : pMode)
    		{
    			iCount++;
    			
    			stCurrent = publishIntern(pInstruction, pObject, mode);

    			switch (stCurrent)
    			{
	    			case Failed:
	    				iError++;
	    				break;
	    			case Completed:
	    				iCompleted++;
    				default:
    					break;
    			}
    		}

    		if (iCount == iCompleted)
    		{
    			return PublishState.Completed;
    		}
    		else if (iCount == iError)
    		{
    			return PublishState.Failed;
    		}
    		else
    		{
    			return PublishState.Partial;
    		}
    	}
    	
    }    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Publishses an object for a given instruction with given {@link PublishMode}.
     * 
     * @param pInstruction the instruction
     * @param pObject the object
     * @param pMode the publish mode
     * @return the {@link PublishState}
     */
    protected PublishState publishIntern(String pInstruction, Object pObject, PublishMode pMode)
    {
    	switch (pMode)
    	{
    		case AllCurrentSessions:
    		case AllOtherSessions:
    		case AllMasterSessions:
    		case AllOtherMasterSessions:
    			return publishAll(pInstruction, pObject, pMode);    		
    		case OtherSessions:
    			return publishOtherSessions(pInstruction, pObject);
    		case CurrentMasterSession:
    			return publishCurrentMasterSession(pInstruction, pObject);
    		case CurrentSession:
    			return publishCurrentSession(pInstruction, pObject);
    		default:
    			throw new IllegalArgumentException("Unsupported publish mode: " + pMode);
    	}
    }
    
    /**
     * Publishes an object for a given instruction to the current session.
     * 
     * @param pInstruction the instruction
     * @param pObject the object
     * @return the {@link PublishState}
     */
    public PublishState publishCurrentSession(String pInstruction, Object pObject)
    {
        if (master != null)
        {
            log.info("Publish message to current client (session) ", pInstruction, " ", master);

            if (session.isValid() && master.isValid())
            {
                master.session.addCallBackResult(new ResultObject(IConnection.TYPE_CALLBACKRESULT_RESULT, pObject, pInstruction + "@" + session.getId()));

                return PublishState.Completed;
            }
        }
        else
        {
            log.info("Publish message to current client failed because master session isn't set ", pInstruction);
        }
        
        return PublishState.Failed;
    }
    
    /**
     * Publishes an object for a given instruction to the current master session.
     * 
     * @param pInstruction the instruction
     * @param pObject the object
     * @return the {@link PublishState}
     */
    public PublishState publishCurrentMasterSession(String pInstruction, Object pObject)
    {
        if (master != null)
        {
            log.info("Publish message to current client (master session) ", pInstruction, " ", master);

            if (master.isValid())
            {
                master.session.addCallBackResult(new ResultObject(IConnection.TYPE_CALLBACKRESULT_RESULT, pObject, pInstruction + "@" + master.getId()));

                return PublishState.Completed;
            }
        }
        else
        {
            log.info("Publish message to current client failed because master session isn't set ", pInstruction);
        }
        
        return PublishState.Failed;
    }
    
    /**
     * Publishes an object for a given instruction to all sub session of the current master session, but not to the
     * current session.
     * 
     * @param pInstruction the instruction
     * @param pObject the object
     * @return the {@link PublishState}
     */
    public PublishState publishOtherSessions(String pInstruction, Object pObject)
    {
        if (master != null)
        {
            log.info("Publish message to current client (other sessions) ", pInstruction, " ", session);

            List<SubSession> sessions = ((MasterSession)master.session).getSubSessions();
            
            boolean bPartial = false;
            
            if (sessions != null)
            {
            	for (SubSession sub : sessions)
            	{
            		if (sub != session.session)
            		{
            			try
            			{
            				master.session.addCallBackResult(new ResultObject(IConnection.TYPE_CALLBACKRESULT_RESULT, pObject, pInstruction + "@" + sub.getId()));
	                    }
	                    catch (Exception e)
	                    {
	                        bPartial = true;
	                    }
            		}
            	}
            }
            
            if (bPartial)
            {
                return PublishState.Partial;
            }
            else
            {
                return PublishState.Completed;
            }
        }
        else
        {
            log.info("Publish message to all clients failed because master session isn't set ", pInstruction);
        }
        
        return PublishState.Failed;    	
    }
    
    /**
     * Publishes an object for a given instruction to the all available sessions which fit to the given {@link PublishMode}.
     * 
     * @param pInstruction the instruction
     * @param pObject the object
     * @param pMode the mode
     * @return the {@link PublishState}
     */
    private PublishState publishAll(String pInstruction, Object pObject, PublishMode pMode)
    {
        if (master != null)
        {
            log.info("Publish message to all clients (current session) ", pInstruction, " ", master);

            String sName = session.session.getApplicationName();
            String sLCO  = session.session.getLifeCycleName();
            String sLCOMaster = master.session.getLifeCycleName();
            
            DefaultSessionManager sman = master.session.getSessionManager();
            
            ArrayUtil<Object> liIds = sman.getSessionIds();


            if (liIds != null)
            {
                boolean bPartial = false;
                
                AbstractSession sessNotify;
                
                for (int i = 0, cnt = liIds.size(); i < cnt; i++)
                {
                    try
                    {
                        sessNotify = sman.get(liIds.get(i));
                        
                        if (sessNotify.getApplicationName().equals(sName))
                        {
	                        switch (pMode)
	                        {
		                        case AllCurrentSessions:
		                            if (sessNotify instanceof SubSession
		                                && sessNotify.getLifeCycleName().equals(sLCO))
	                                {
	                                    sessNotify.addCallBackResult(new ResultObject(IConnection.TYPE_CALLBACKRESULT_RESULT, 
	                                    		                                      pObject, pInstruction + "@" + sessNotify.getId()));
	                                }
		                            break;
		                        case AllOtherSessions:
		                            if (sessNotify instanceof SubSession
			                            && sessNotify.getLifeCycleName().equals(sLCO)
			                            && sessNotify != session.session)
	                                {
	                                    sessNotify.addCallBackResult(new ResultObject(IConnection.TYPE_CALLBACKRESULT_RESULT, 
	                                    		                                      pObject, pInstruction + "@" + sessNotify.getId()));
	                                }
		                            break;
		                        case AllMasterSessions:
		                            if (sessNotify instanceof MasterSession 
		                            	&& sessNotify.getLifeCycleName().equals(sLCOMaster))
	                                {
	                                    sessNotify.addCallBackResult(new ResultObject(IConnection.TYPE_CALLBACKRESULT_RESULT, 
	                                    		                                      pObject, pInstruction + "@" + sessNotify.getId()));
	                                }
		                            break;
		                        case AllOtherMasterSessions:
		                            if (sessNotify instanceof MasterSession 
		                            	&& sessNotify.getLifeCycleName().equals(sLCOMaster)
		                            	&& sessNotify != master.session)
	                                {
	                                    sessNotify.addCallBackResult(new ResultObject(IConnection.TYPE_CALLBACKRESULT_RESULT, 
	                                    		                                      pObject, pInstruction + "@" + sessNotify.getId()));
	                                }
		                            break;
	                        	default:
	                        		throw new IllegalArgumentException("Publish mode not supported: " + pMode);
	                        }
                        }
                    }
                    catch (Exception e)
                    {
                        bPartial = true;
                    }
                }
                
                if (bPartial)
                {
                    return PublishState.Partial;
                }
                else
                {
                    return PublishState.Completed;
                }
            }
        }
        else
        {
            log.info("Publish message to all clients failed because master session isn't set ", pInstruction);
        }
        
        return PublishState.Failed;
    	
    }

}   // SessionCallBackBroker
