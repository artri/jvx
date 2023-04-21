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
 * 16.06.2016 - [JR] - creation
 */
package javax.rad.remote.event;

import javax.rad.util.ExceptionHandler;
import javax.rad.util.UIInvoker;

/**
 * The <code>CallBackResultForward</code> is a special {@link ICallBackResultListener} implementation which
 * will handle listener exceptions.
 * 
 * @author René Jahn
 */
public class CallBackResultForward implements ICallBackResultListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the reference to the correct UI thread. */
    private UIInvoker uiInvoker;
    
    /** the listener which should receive the {@link CallBackResultEvent}. */
    private ICallBackResultListener listener;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>CallBackResultForward</code>.
     * 
     * @param pListener the original listener
     */
    public CallBackResultForward(ICallBackResultListener pListener)
    {
        this(null, pListener);
    }

    /**
     * Creates a new instance of <code>CallBackResultForward</code>.
     * 
     * 
     * @param pUIInvoker the invokeLater helper from the right calling thread
     * @param pListener the original listener
     */
    public CallBackResultForward(UIInvoker pUIInvoker, ICallBackResultListener pListener)
    {
        uiInvoker = pUIInvoker;
        
        listener = pListener;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ICallBackResultListener
    
    /**
     * {@inheritDoc}
     */
    public void callBackResult(final CallBackResultEvent pEvent)
    {
        if (listener != null)
        {
            if (uiInvoker != null)
            {
                uiInvoker.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        try
                    	{
                    		listener.callBackResult(pEvent);
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
	                listener.callBackResult(pEvent);
            	}
            	catch (Throwable th)
            	{
            		ExceptionHandler.show(th);
            	}
            }
        }
    }
    
}   // CallBackResultForward
