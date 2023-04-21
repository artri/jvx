/*
 * Copyright 2018 SIB Visions GmbH
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
 * 20.06.2018 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest.service;

import javax.rad.server.ICallBackBroker;
import javax.rad.server.ICallHandler;
import javax.rad.server.ISession;
import javax.rad.server.event.CallEventHandler;
import javax.rad.server.event.type.IAfterCallListener;
import javax.rad.server.event.type.IAfterLastCallListener;
import javax.rad.server.event.type.IBeforeCallListener;
import javax.rad.server.event.type.IBeforeFirstCallListener;

import com.sibvisions.rad.server.AbstractSessionContext;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>RESTSessionContextImpl</code> extends the {@link AbstractSessionContext} for REST services.
 * It's a special context because REST doesn't support {@link ICallBackBroker} and {@link ICallHandler}.
 * 
 * @author René Jahn
 */
public final class RESTSessionContextImpl extends AbstractSessionContext 
                                          implements ICallHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** the "before first call" event. */
    private SilentCallEventHandler<IBeforeFirstCallListener> eventBeforeFirstCall;
    /** the "after last call" event. */
    private SilentCallEventHandler<IAfterLastCallListener> eventAfterLastCall;
    /** the "before call" event. */
    private SilentCallEventHandler<IBeforeCallListener> eventBeforeCall;
    /** the "after call" event. */
    private SilentCallEventHandler<IAfterCallListener> eventAfterCall;
    
    /** the callback broker. */
    private static ICallBackBroker broker;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>RESTSessionContextImpl</code> for a specific
	 * {@link ISession}.
	 * 
	 * @param pSession the associated session for this {@link javax.rad.server.SessionContext}
	 */
	public RESTSessionContextImpl(ISession pSession)
	{
		super(pSession);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICallHandler getCallHandler() 
	{
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICallBackBroker getCallBackBroker() 
	{
		if (broker == null)
		{
			broker = new RESTCallBackBroker();
		}
		
		return broker;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//ICALLHANDLER
	
	/**
	 * {@inheritDoc}
	 */
    public CallEventHandler<IBeforeFirstCallListener> eventBeforeFirstCall()
    {
        if (eventBeforeFirstCall == null)
        {
            eventBeforeFirstCall = new SilentCallEventHandler<IBeforeFirstCallListener>(IBeforeFirstCallListener.class);
        }
        
        return eventBeforeFirstCall;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public CallEventHandler<IAfterLastCallListener> eventAfterLastCall()
    {
        if (eventAfterLastCall == null)
        {
            eventAfterLastCall = new SilentCallEventHandler<IAfterLastCallListener>(IAfterLastCallListener.class);
        }
        
        return eventAfterLastCall;
    }    

	/**
	 * {@inheritDoc}
	 */
    public CallEventHandler<IBeforeCallListener> eventBeforeCall()
    {
        if (eventBeforeCall == null)
        {
            eventBeforeCall = new SilentCallEventHandler<IBeforeCallListener>(IBeforeCallListener.class);
        }
        
        return eventBeforeCall;
    }

	/**
	 * {@inheritDoc}
	 */
    public CallEventHandler<IAfterCallListener> eventAfterCall()
    {
        if (eventAfterCall == null)
        {
            eventAfterCall = new SilentCallEventHandler<IAfterCallListener>(IAfterCallListener.class);
        }
        
        return eventAfterCall;
    }
	
	/**
	 * {@inheritDoc}
	 */
    public void invokeAfterCall(Runnable pRunnable)
    {
    	LoggerFactory.getInstance(getClass()).debug("CallHandler isn't supported for REST services!");
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void invokeAfterCall(Object pObject, String pMethod)
    {
    	invokeAfterCall(null);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void invokeAfterLastCall(Runnable pRunnable)
    {
    	invokeAfterCall(null);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void invokeAfterLastCall(Object pObject, String pMethod)
    {
    	invokeAfterCall(null);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void invokeFinally(Runnable pRunnable)
    {
    	invokeAfterCall(null);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void invokeFinally(Object pObject, String pMethod)
    {
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setObjectName(String pObjectName)
	{
		//needed because we need package private access for this method
		super.setObjectName(pObjectName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setMethodName(String pObjectName)
	{
		//needed because we need package private access for this method
		super.setMethodName(pObjectName);
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * The <code>SilentCallEventHandler</code> is a {@link CallEventHandler} without dispatching.
	 * 
	 * @author René Jahn
	 * @param <L> the listener type
	 */
	private static final class SilentCallEventHandler<L> extends CallEventHandler<L>
	{	
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>SilentCallEventHandler</code>.
		 * 
		 * @param pClass the listener class
		 */
		public SilentCallEventHandler(Class<L> pClass) 
		{
			super(pClass);
		}
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Overwritten methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void addHandler(int pIndex, ListenerHandler pHandler)
		{
			LoggerFactory.getInstance(getClass()).debug("CallHandler isn't supported for REST services!");
			
			super.addHandler(pIndex, pHandler);
		}
		
	}	// SilentCallEventHandler
	
	/**
	 * The <code>RESTCallBackBroker</code> is the {@link ICallBackBroker} for REST. 
	 * It's not implemented.
	 * 
	 * @author René Jahn
	 */
	private static final class RESTCallBackBroker implements ICallBackBroker
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Interface implementation
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc}
		 */
	    public PublishState publish(String pInstruction, Object pObject, PublishMode... pMode)
	    {
	    	LoggerFactory.getInstance(RESTSessionContextImpl.class).debug("CallBackBroker isn't supported for REST services!");
	    	
	    	return PublishState.Failed;
	    }

	}	// RESTCallBackBroker

}	// RESTSessionContextImpl
