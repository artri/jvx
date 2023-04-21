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
 * 06.06.2014 - [JR] - creation
 */
package javax.rad.util;

import java.lang.reflect.Method;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>UIInvoker</code> class is bound to the current UI factory and is able to
 * execute calls in the right UI thread via invokeLater of the factory.
 * 
 * @author René Jahn
 */
public class UIInvoker
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the logger instance. */ 
    private static ILogger log = LoggerFactory.getInstance(UIInvoker.class);
    
    
    /** the factory for the current thread. */
    private Object oThreadFactory = null;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>UIInvoker</code> for thread safety.
     */
    public UIInvoker()
    {
        oThreadFactory = null;
        
        try
        {
            Class<?> clsManager = Class.forName("javax.rad.genui.UIFactoryManager");
            
            try
            {
                Method metGetFactory = clsManager.getMethod("getFactory");
                
                oThreadFactory = metGetFactory.invoke(null);
            }
            catch (Exception ex)
            {
                log.error("'UIFactoryManager.getFactory()' threw an exception. Invoke direct!", ex);
            }
        }
        catch (ClassNotFoundException cnfe)
        {
            log.debug("'UIFactoryManager' is not available. Invoke direct!");
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Causes <code>pRunnable.run()</code> to be executed asynchronously on the
     * current event dispatching thread. There are different technologies
     * with different <code>invokeLater</code> implementations. To find the UI
     * dependent method the current factory manager will be used. If there is
     * no factory manager, then the <code>pRunnable.run()</code> will be 
     * started within the current thread.
     * 
     * @param pRunnable specific functionality to run asynchronous
     */
    public void invokeLater(Runnable pRunnable)
    {
        boolean bInvokeDirect = false;

        if (oThreadFactory != null)
        {
            try
            {
                Method metInvokeLater = oThreadFactory.getClass().getMethod("invokeLater", Runnable.class);
                
                metInvokeLater.invoke(oThreadFactory, pRunnable);
                
                log.debug("invokeLater() of '",
                          oThreadFactory.getClass().getName(),
                          "' successful called!");
            }
            catch (Exception ex)
            {
                log.info("An UI factory is available: '", 
                          oThreadFactory.getClass().getName(), 
                          "', but it's not possible to call invokeLater(). Invoke direct!");
                
                bInvokeDirect = true;
            }
        }
        else
        {
            log.info("UI factory is NOT available. Invoke direct!");
            
            bInvokeDirect = true;
        }
        
        if (bInvokeDirect)
        {
            pRunnable.run();
        }
    }
        
}   // UIInvoker
