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
 * 03.06.2014 - [JR] - creation
 */
package javax.rad.remote.event;

import javax.rad.util.ExceptionHandler;
import javax.rad.util.UIInvoker;

/**
 * The <code>CallBackForward</code> is a special {@link ICallBackListener} implementation which
 * will replace the event source of the received {@link CallBackEvent} with another
 * source.
 * 
 * @author René Jahn
 */
public class CallBackForward implements ICallBackListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the reference to the correct UI thread. */
    private UIInvoker uiInvoker;
    
    /** the source object for the forwarded {@link CallBackEvent}. */
    private Object oSource;

    /** the listener which should receive the {@link CallBackEvent}. */
    private ICallBackListener listener;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>CallBackForward</code>.
     * 
     * @param pSource the "new" event source for the received event
     * @param pListener the original listener
     */
    public CallBackForward(Object pSource, ICallBackListener pListener)
    {
        this(null, pSource, pListener);
    }

    /**
     * Creates a new instance of <code>CallBackForward</code>.
     * 
     * 
     * @param pUIInvoker the invokeLater helper from the right calling thread
     * @param pSource the "new" event source for the received event
     * @param pListener the original listener
     */
    public CallBackForward(UIInvoker pUIInvoker, Object pSource, ICallBackListener pListener)
    {
        uiInvoker = pUIInvoker;
        
        oSource = pSource;
        listener = pListener;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ICallBackListener
    
    /**
     * {@inheritDoc}
     */
    public void callBack(final CallBackEvent pEvent)
    {
        if (listener != null)
        {
            Object obj = null;
            Throwable throwable = null;
            
            try
            {
                obj = pEvent.getObject();
            }
            catch (Throwable th)
            {
                throwable = th;
            }
            
            //Change object source
            final CallBackEvent cbe = new CallBackEvent(oSource, 
                                                        pEvent.getObjectName(), 
                                                        pEvent.getMethodName(),
                                                        obj,
                                                        throwable,
                                                        pEvent.getRequestTime(),
                                                        pEvent.getResponseTime());

            if (uiInvoker != null)
            {
                uiInvoker.invokeLater(new Runnable()
                {
                    public void run()
                    {
                    	try
                    	{
                    		listener.callBack(cbe);
                    	}
                    	catch (Throwable th)
                    	{
                    		ExceptionHandler.show(th);
                    	}
                    }
                });
            }
            else
            {
            	try
            	{
	            	//forward the "new" event to the original listener
	                listener.callBack(cbe);
            	}
            	catch (Throwable th)
            	{
            		ExceptionHandler.show(th);
            	}
            }
        }
    }
    
}   // CallBackForward
