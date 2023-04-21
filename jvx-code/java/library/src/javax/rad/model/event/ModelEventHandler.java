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
 * 25.06.2009 - [HM] - creation
 */
package javax.rad.model.event;

import javax.rad.model.ModelException;
import javax.rad.util.EventHandler;
import javax.rad.util.SilentAbortException;

/**
 * The <code>ModelEventHandler</code> is a <code>EventHandler</code> that 
 * handles Events, and throws ModelExceptions. 
 * 
 * @author Martin Handsteiner
 * 
 * @param <L> the Listener type
 */
public class ModelEventHandler<L> extends EventHandler<L>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ModelEventHandler</code>.
	 *  
	 * @param pListenerType the listener type interface.
	 */
	public ModelEventHandler(Class<L> pListenerType)
	{
		super(pListenerType);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     * 
     * @deprecated use {@link #isDispatchable(boolean)}
     */
    @Override
    @Deprecated
    public boolean isDispatchable()
    {
       return super.isDispatchableIntern(true);
    }
    
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated use {@link #dispatchEvent(boolean, Object)}
	 */
	@Override
	@Deprecated
	public Object dispatchEvent(Object... pEventParameter) throws ModelException
	{
		return dispatchEvent(true, pEventParameter);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns true, if dispatch will invoke any listener.
     * This is true if:
     * <ul>
     *   <li>isDispatchEventEnabled is true</li>
     *   <li>at least one listener is added or a default listener is set</li>
     *   <li>it is not called recursive</li>
     * </ul>
     * An external enabled flag allows to disable events, but ensures that internal listeners are still executed.
     * 
     * @param pExternalEnabled an external enabled flag.
     * @return true, if dispatch will invoke any listener.
     */
    public boolean isDispatchable(boolean pExternalEnabled)
    {
        return super.isDispatchableIntern(pExternalEnabled);
    }
	
    /**
     * Returns true, if dispatch will invoke any listener.
     * This is true if:
     * <ul>
     *   <li>isDispatchEventEnabled is true</li>
     *   <li>at least one listener is added or a default listener is set</li>
     *   <li>it is not called recursive</li>
     * </ul>
     * 
     * @param pEventHandler the event handler to check, if it is dispatchable.
     * @return true, if dispatch will invoke any listener.
     */
    @Deprecated
    public static boolean isDispatchable(EventHandler pEventHandler)
    {
        return isDispatchableIntern(true, pEventHandler);
    }
    
    /**
     * Returns true, if dispatch will invoke any listener.
     * This is true if:
     * <ul>
     *   <li>isDispatchEventEnabled is true</li>
     *   <li>at least one listener is added or a default listener is set</li>
     *   <li>it is not called recursive</li>
     * </ul>
     * An external enabled flag allows to disable events, but ensures that internal listeners are still executed.
     * 
     * @param pExternalEnabled an external enabled flag.
     * @param pEventHandler the event handler to check, if it is dispatchable.
     * @return true, if dispatch will invoke any listener.
     */
    public static boolean isDispatchable(boolean pExternalEnabled, EventHandler pEventHandler)
    {
        return isDispatchableIntern(pExternalEnabled, pEventHandler);
    }

    /**
     * Dispatches the given events to all listeners.
     * An external enabled flag allows to disable events, but ensures that internal listeners are still executed.
     * 
     * @param pExternalEnabled an external enabled flag.
     * @param pEventParameter the event parameter.
     * @return the return value of the deaultListener, if it is called, or null if dispatching is disabled or 
     *         no listeners were called
     * @throws ModelException if dispatching event fails.
     */
    public Object dispatchEvent(boolean pExternalEnabled, Object pEventParameter) throws ModelException
    {
        try
        {
            return dispatchEventIntern(pExternalEnabled, pEventParameter);
        }
        catch (SilentAbortException pSilentAbortException)
        {
            throw pSilentAbortException;
        }
        catch (ModelException pModelException)
        {
            throw pModelException;
        }
        catch (Throwable pThrowable)
        {
            throw new ModelException("Exception in Listenermethod!", pThrowable);
        }
    }
	
}	// ModelEventHandler
