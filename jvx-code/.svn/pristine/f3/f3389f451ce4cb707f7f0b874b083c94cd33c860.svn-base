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
 * 27.05.2015 - [JR] - creation
 * 16.12.2015 - [JR] - #1544: use correct index in RecordingCallEventHandler.add
 * 15.01.2016 - [JR] - #1550: don't fire events without real call 
 */
package com.sibvisions.rad.server;

import java.util.List;

import javax.rad.server.ICallHandler;
import javax.rad.server.ISession;
import javax.rad.server.event.CallEvent;
import javax.rad.server.event.CallEventHandler;
import javax.rad.server.event.CallResultEvent;
import javax.rad.server.event.SessionEvent;
import javax.rad.server.event.type.IAfterCallListener;
import javax.rad.server.event.type.IAfterLastCallListener;
import javax.rad.server.event.type.IBeforeCallListener;
import javax.rad.server.event.type.IBeforeFirstCallListener;
import javax.rad.util.EventHandler;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>SessionCallHandler</code> is the {@link ICallHandler} for {@link AbstractSession} based server calls.
 * Every {@link MasterSession} holds an instance of this handler and {@link SubSession}s use the handler from the
 * master session.
 * 
 * @author René Jahn
 */
class SessionCallHandler implements ICallHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the EventHandler for creating dynamic Runnable interfaces. */
    private static EventHandler<Runnable> ehRunnable = new EventHandler<Runnable>(Runnable.class);

    /** the associated session. */
    private WrappedSession wsessCurrent;
    
    /** the master call handler. */
    private SessionCallHandler chMaster;
    
    /** the invoke "after call" queue. */
    private ArrayUtil<Runnable> auInvokeAfterCall;
    /** the invoke "after last call" queue. */
    private ArrayUtil<Runnable> auInvokeAfterLastCall;
    /** the invoke finally queue. */
    private ArrayUtil<Runnable> auInvokeFinally;

    /** the current call. */
    private Call callCurrent;
    
    /** the "before first call" event. */
    private RecordingCallEventHandler<IBeforeFirstCallListener> eventBeforeFirstCall;
    /** the "after last call" event. */
    private CallEventHandler<IAfterLastCallListener> eventAfterLastCall;
    /** the "before call" event. */
    private RecordingCallEventHandler<IBeforeCallListener> eventBeforeCall;
    /** the "after call" event. */
    private CallEventHandler<IAfterCallListener> eventAfterCall;
    
    /** the mark whether {@link #fireBeforeCall(Call)} was called. */
    private boolean bHadCall = false;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>SessionCallHandler</code>.
     * 
     * @param pSession the connected session
     */
    SessionCallHandler(AbstractSession pSession)
    {
        wsessCurrent = new WrappedSession(pSession);
        
        if (pSession instanceof SubSession)
        {
            chMaster = ((SubSession)pSession).getMasterSession().getCallHandler();
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public RecordingCallEventHandler<IBeforeFirstCallListener> eventBeforeFirstCall()
    {
        if (eventBeforeFirstCall == null)
        {
            eventBeforeFirstCall = new RecordingCallEventHandler<IBeforeFirstCallListener>(IBeforeFirstCallListener.class);
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
            eventAfterLastCall = new CallEventHandler<IAfterLastCallListener>(IAfterLastCallListener.class);
        }
        
        return eventAfterLastCall;
    }

    /**
     * {@inheritDoc}
     */
    public RecordingCallEventHandler<IBeforeCallListener> eventBeforeCall()
    {
        if (eventBeforeCall == null)
        {
            eventBeforeCall = new RecordingCallEventHandler<IBeforeCallListener>(IBeforeCallListener.class);
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
            eventAfterCall = new CallEventHandler<IAfterCallListener>(IAfterCallListener.class);
        }
        
        return eventAfterCall;
    }

    /**
     * {@inheritDoc}
     */
    public void invokeAfterCall(Runnable pRunnable)
    {
        if (auInvokeAfterCall == null)
        {
            auInvokeAfterCall = new ArrayUtil<Runnable>();
        }
        
        auInvokeAfterCall.add(pRunnable);
    }    

    /**
     * {@inheritDoc}
     */
    public void invokeAfterCall(Object pObject, String pMethod)
    {
        if (auInvokeAfterCall == null)
        {
            auInvokeAfterCall = new ArrayUtil<Runnable>();
        }

        auInvokeAfterCall.add(ehRunnable.createListener(pObject, pMethod));
    }
    
    /**
     * {@inheritDoc}
     */
    public void invokeAfterLastCall(Runnable pRunnable)
    {
        if (auInvokeAfterLastCall == null)
        {
            auInvokeAfterLastCall = new ArrayUtil<Runnable>();
        }
        
        auInvokeAfterLastCall.add(pRunnable);
    }

    /**
     * {@inheritDoc}
     */
    public void invokeAfterLastCall(Object pObject, String pMethod)
    {
        if (auInvokeAfterLastCall == null)
        {
            auInvokeAfterLastCall = new ArrayUtil<Runnable>();
        }

        auInvokeAfterLastCall.add(ehRunnable.createListener(pObject, pMethod));
    }

    /**
     * {@inheritDoc}
     */
    public void invokeFinally(Runnable pRunnable)
    {
        if (auInvokeFinally == null)
        {
            auInvokeFinally = new ArrayUtil<Runnable>();
        }

        auInvokeFinally.add(pRunnable);
    }

    /**
     * {@inheritDoc}
     */
    public void invokeFinally(Object pObject, String pMethod)
    {
        if (auInvokeFinally == null)
        {
            auInvokeFinally = new ArrayUtil<Runnable>();
        }

        auInvokeFinally.add(ehRunnable.createListener(pObject, pMethod));
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the session.
     * 
     * @return the session
     */
    public ISession getSession()
    {
        return wsessCurrent;
    }

    /**
     * Notifies all listeners before the first call.
     */
    void fireBeforeFirstCall()
    {
        fireBeforeFirstCall(wsessCurrent);
    }
    
    /**
     * Notifies all listeners before the first call.
     * 
     * @param pSource the source session
     */
    private void fireBeforeFirstCall(ISession pSource)
    {
        setHadCall(false);
        
        if (eventBeforeFirstCall != null)
        {
            //fire events because the session is already initialized
            if (chMaster != null)
            {
                chMaster.fireBeforeFirstCall(pSource);
            }

            eventBeforeFirstCall.dispatchEvent(new SessionEvent(wsessCurrent, pSource));

            //enable recording to detect new listeners during LCO creation
            //(after dispatchEvent, because listener registration is not allowed while dispatching)
            eventBeforeFirstCall.bRecord = true;
        }
        else
        {
            //DON'T fire master handler, because we need the correct event order -> will be done in postObjectCreation
            
            //we need the instance
            eventBeforeFirstCall().bRecord = true;
        }
    }
    
    /**
     * Notifies all listeners after the last call.
     * 
     * @param pCallError <code>true</code> if at least one call throwed an exception
     */
    void fireAfterLastCall(boolean pCallError)
    {
        fireAfterLastCall(wsessCurrent, pCallError);
    }
    
    /**
     * Notifies all listeners after the last call.
     * 
     * @param pSource the source session
     * @param pCallError <code>true</code> if at least one call throwed an exception
     */
    private void fireAfterLastCall(ISession pSource, boolean pCallError)
    {
        try
        {
            if (eventAfterLastCall != null)
            {
                //no call was executed -> don't send after last call because it's not what we expect
                if (bHadCall)
                {
                    eventAfterLastCall.dispatchEvent(new SessionEvent(wsessCurrent, pSource, pCallError));
                }
            }
            
            processInvokeAfterLastCall();
            
            if (chMaster != null)
            {
                chMaster.fireAfterLastCall(pSource, pCallError);
            }
            
            if (eventBeforeFirstCall != null)
            {
                eventBeforeFirstCall.reset();
            }
        }
        finally
        {
            setHadCall(false);
        }
    }

    /**
     * Notifies all listeners before a call.
     * 
     * @param pCall the call definition
     */
    void fireBeforeCall(Call pCall)
    {
        fireBeforeCall(wsessCurrent, pCall);
    }
    
    /**
     * Notifies all listeners before a call.
     * 
     * @param pSource the source session
     * @param pCall the call definition
     */
    private void fireBeforeCall(ISession pSource, Call pCall)
    {
        setHadCall(true);
        
        callCurrent = pCall;
        
        if (eventBeforeCall != null)
        {
            //fire events because the session is already initialized
            if (chMaster != null)
            {
                chMaster.fireBeforeCall(pSource, pCall);
            }

            eventBeforeCall.dispatchEvent(new CallEvent(wsessCurrent, 
                                                        pSource,
                                                        pCall.getObjectName(), 
                                                        pCall.getMethodName(), 
                                                        pCall.getParameters(), 
                                                        pCall.isCallBack()));
            
            //enable recording to detect new listeners during LCO creation 
            //(after dispatchEvent, because listener registration is not allowed while dispatching)
            eventBeforeCall.bRecord = true;
        }
        else
        {
            //DON'T fire master handler, because we need the correct event order -> will be done in postObjectCreation
            if (chMaster != null)
            {
                chMaster.callCurrent = pCall;
            }
            
            //we need the instance
            eventBeforeCall().bRecord = true;
        }
    }
    
    /**
     * Notifies all listeners after a call.
     * 
     * @param pCall the call definition
     * @param pObject result value from the method call. If 
     *                the method call returned a result, the exception is undefined!
     * @param pThrowable exception from the the remote method call. If 
     *                   the method call returned an error, the result is undefined!
     */
    void fireAfterCall(Call pCall, Object pObject, Throwable pThrowable)
    {
        fireAfterCall(wsessCurrent, pCall, pObject, pThrowable);
    }
    
    /**
     * Notifies all listeners after a call.
     * 
     * @param pSource the source session
     * @param pCall the call definition
     * @param pObject result value from the method call. If 
     *                the method call returned a result, the exception is undefined!
     * @param pThrowable exception from the the remote method call. If 
     *                   the method call returned an error, the result is undefined!
     */
    private void fireAfterCall(ISession pSource, Call pCall, Object pObject, Throwable pThrowable)
    {
        if (eventAfterCall != null)
        {
            eventAfterCall.dispatchEvent(new CallResultEvent(wsessCurrent, 
                                                             pSource,
                                                             pCall.getObjectName(), 
                                                             pCall.getMethodName(), 
                                                             pCall.getParameters(), 
                                                             pCall.isCallBack(), 
                                                             pObject, 
                                                             pThrowable));
        }

        processInvokeAfterCall();
        
        if (chMaster != null)
        {
            chMaster.fireAfterCall(pSource, pCall, pObject, pThrowable);
        }
        
        if (eventBeforeCall != null)
        {
            eventBeforeCall.reset();
        }
        
        callCurrent = null;
    }

    /**
     * Process invoke after call (and finally) operations.
     */
    private void processInvokeAfterCall()
    {
        clear("invokeAfterCall", auInvokeAfterCall);
        
        auInvokeAfterCall = null;
    }
    
    /**
     * Process invoke after last call (and finally) operations.
     */
    private void processInvokeAfterLastCall()
    {
        clear("invokeAfterLastCall", auInvokeAfterLastCall);
        clear("invokeFinally", auInvokeFinally);
        
        //it's possible that invokeFinally calls invokeLater, but we won't support such calls
        auInvokeAfterLastCall = null;
        auInvokeFinally = null;
    }
    
    /**
     * Clears the given queue by removing and running all entries, one by one.
     * 
     * @param pName the name of the queue
     * @param pQueue the queue
     */
    private void clear(String pName, ArrayUtil<Runnable> pQueue)
    {
        if (pQueue != null)
        {
            Runnable runnable;
            
            while (!pQueue.isEmpty())
            {
                runnable = pQueue.remove(0);

                try
                {
                    runnable.run();
                }
                catch (Throwable th)
                {
                    LoggerFactory.getInstance(SessionCallHandler.class).error("Error in ", pName, " call", th);
                }
            }
        }
    }

    /**
     * Notification if LCO creation of connected session was done. This method is important to
     * notify listeners which were added in constructor of LCO, because such listeners are "lazy" 
     * and not available before the LCO was created!
     */
    void postObjectCreation()
    {
        //It's possible to access the ISession before a call, e.g. via validateAuthentication in ISecurityManager.
        //In this case, we shouldn't notify our listeners because we didn't execute a "real call" 
        if (callCurrent != null)
        {
            redispatchBeforeFirstCall(wsessCurrent);
            redispatchBeforeCall(wsessCurrent);
        }
    }
    
    /**
     * Redispatches "before first call" event to recorded listeners.
     * 
     * @param pSource the source session
     */
    private void redispatchBeforeFirstCall(ISession pSource)
    {
        if (chMaster != null && chMaster.eventBeforeFirstCall != null)
        {
            //needed to use the right event order
            chMaster.eventBeforeFirstCall.dispatchEvent(new SessionEvent(chMaster.wsessCurrent, pSource));
            
            chMaster.redispatchBeforeFirstCall(pSource);
        }
        
        if (eventBeforeFirstCall != null)
        {
            eventBeforeFirstCall.redispatchEvent(new SessionEvent(wsessCurrent, pSource));
        }
    }
    
    /**
     * Redispatches "before call" event to recorded listeners.
     * 
     * @param pSource the source session
     */
    private void redispatchBeforeCall(ISession pSource)
    {
        if (callCurrent != null)
        {
            if (chMaster != null && chMaster.eventBeforeCall != null)
            {
                //needed to use the right event order
                chMaster.eventBeforeCall.dispatchEvent(new CallEvent(chMaster.wsessCurrent,
                                                                     pSource,
                                                                     callCurrent.getObjectName(),
                                                                     callCurrent.getMethodName(),
                                                                     callCurrent.getParameters(),
                                                                     callCurrent.isCallBack()));
                
                chMaster.redispatchBeforeCall(pSource);
            }
            
            if (eventBeforeCall != null)
            {
                eventBeforeCall.redispatchEvent(new CallEvent(wsessCurrent,
                                                              pSource,
                                                              callCurrent.getObjectName(),
                                                              callCurrent.getMethodName(),
                                                              callCurrent.getParameters(),
                                                              callCurrent.isCallBack()));
            }
        }
    }
    
    /**
     * Sets whether the handler had a valid {@link Call}.
     * 
     * @param pHadCall <code>true</code> if at least one call is available, <code>false</code> otherwise
     */
    private void setHadCall(boolean pHadCall)
    {
        bHadCall = pHadCall;
        
        if (chMaster != null)
        {
            chMaster.bHadCall = pHadCall;
        }
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************

    /**
     * The <code>RecordingCallEventHandler</code> extends the {@link CallEventHandler}
     * and supports listener recording.
     * 
     * @author René Jahn
     *
     * @param <L> the listener type
     */
    private static final class RecordingCallEventHandler<L> extends CallEventHandler<L>
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the list of recorded handlers. */
        private List<ListenerHandler> liHandler;

        /** whether recording is enabled. */
        private boolean bRecord;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Creates a new instance of <code>RecordingCallEventHandler</code>.
         *  
         * @param pClass the listener class
         */
        private RecordingCallEventHandler(Class<L> pClass)
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
            super.addHandler(pIndex, pHandler);

            if (liHandler == null)
            {
                liHandler = new ArrayUtil<ListenerHandler>();
            }

            if (bRecord)
            {
                if (pIndex < 0)
                {
                    liHandler.add(pHandler);
                }
                else
                {
                    liHandler.add(pIndex, pHandler);
                }
            }
            else
            {
                if (pIndex < 0)
                {
                    liHandler.add(null);
                }
                else
                {
                    liHandler.add(pIndex, null);
                }
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected ListenerHandler removeHandler(int pIndex)
        {
            if (liHandler != null)
            {
                liHandler.remove(pIndex);
                
                if (liHandler.isEmpty())
                {
                    liHandler = null;
                }
            }
            
            return super.removeHandler(pIndex);
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Dispatches the given events to all recorded listeners.
         * 
         * @param pEventParameter the event parameter
         * @return <code>null</code>
         */
        public Object redispatchEvent(Object... pEventParameter)
        {
            //don't add new handlers!
            bRecord = false;
            
            if (liHandler != null)
            {
                try
                {
                    ListenerHandler handler;
                    
                    for (int i = 0, iSize = liHandler.size(); i < iSize; i++)
                    {
                        handler = liHandler.get(i);
                        
                        if (handler != null)
                        {
                            liHandler.get(i).dispatchEvent(pEventParameter);
                        }
                    }
                }
                catch (Throwable pThrowable)
                {
                    ExceptionHandler.raise(pThrowable);
                }
                finally
                {
                    clearHandler();
                }
            }

            return null;
        }

        /**
         * Clears recorded handlers.
         */
        private void clearHandler()
        {
            if (liHandler != null)
            {
                for (int i = 0, iSize = liHandler.size(); i < iSize; i++)
                {
                    liHandler.set(i, null);
                }
            }
        }
        
        /**
         * Stops recording and all recorded handlers.
         */
        private void reset()
        {
            bRecord = false;

            clearHandler();
        }
        
    }   // RecordingCallEventHandler
    
}   // SessionCallHandler
